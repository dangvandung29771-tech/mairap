package com.frontier.frontier.forge;

import com.frontier.frontier.FrontierMod;
import com.frontier.frontier.init.FrontierComponents;
import com.frontier.frontier.init.FrontierItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Server-authoritative forging rules for the Master Grindstone.
 * A weapon may carry at most two forged properties, each capped at level III
 * with diminishing gains. The same input always produces the same output.
 */
public final class ForgingLogic {
    public static final int MAX_PROPERTIES = 2;
    public static final ResourceLocation MODIFIER_PREFIX = FrontierMod.id("forged/stat");

    private ForgingLogic() {
    }

    public record Result(boolean ok, Component error, ItemStack result) {
        public static Result fail(String key) {
            return new Result(false, Component.translatable(key).withStyle(ChatFormatting.RED), ItemStack.EMPTY);
        }

        public static Result of(ItemStack result) {
            return new Result(true, Component.empty(), result);
        }
    }

    /** Snapshot of the stats shown in the grindstone GUI. */
    public record Stats(float damage, float crit, float reach, int durability, float speed, float knockback) {
        public static final Stats ZERO = new Stats(0, 0, 0, 0, 0, 0);
    }

    public static boolean isWeapon(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof SwordItem || item instanceof AxeItem || item instanceof TridentItem;
    }

    public static boolean isWhetstone(ItemStack stack) {
        return propertyOf(stack.getItem()).map(property -> !property.isCrystal()).orElse(false);
    }

    public static boolean isCrystal(ItemStack stack) {
        return propertyOf(stack.getItem()).map(ForgedProperty::isCrystal).orElse(false);
    }

    public static Optional<ForgedProperty> propertyOf(Item item) {
        if (item == FrontierItems.SHARP_WHETSTONE.get()) return Optional.of(ForgedProperty.SHARP);
        if (item == FrontierItems.BALANCED_WHETSTONE.get()) return Optional.of(ForgedProperty.BALANCED);
        if (item == FrontierItems.HEAVY_WHETSTONE.get()) return Optional.of(ForgedProperty.HEAVY);
        if (item == FrontierItems.PRECISION_CRYSTAL.get()) return Optional.of(ForgedProperty.PRECISION);
        if (item == FrontierItems.BLOOD_CRYSTAL.get()) return Optional.of(ForgedProperty.BLOOD);
        if (item == FrontierItems.VOID_CRYSTAL.get()) return Optional.of(ForgedProperty.VOID);
        return Optional.empty();
    }

    public static ForgedData dataOf(ItemStack stack) {
        ForgedData data = stack.get(FrontierComponents.FORGED.get());
        return data == null ? ForgedData.EMPTY : data;
    }

    public static Result tryForge(ItemStack weapon, ItemStack whetstone, ItemStack crystal) {
        if (weapon.isEmpty() || !isWeapon(weapon)) {
            return Result.fail("forge.frontier.error.no_weapon");
        }
        boolean hasWhetstone = !whetstone.isEmpty() && isWhetstone(whetstone);
        boolean hasCrystal = !crystal.isEmpty() && isCrystal(crystal);
        if (!hasWhetstone && !hasCrystal) {
            return Result.fail("forge.frontier.error.no_material");
        }

        ForgedData data = dataOf(weapon);
        List<ForgedProperty> toApply = new ArrayList<>();
        if (hasWhetstone) toApply.add(propertyOf(whetstone.getItem()).orElseThrow());
        if (hasCrystal) toApply.add(propertyOf(crystal.getItem()).orElseThrow());

        for (ForgedProperty property : toApply) {
            if (data.levelOf(property) >= ForgedProperty.MAX_LEVEL) {
                return Result.fail("forge.frontier.error.max_level");
            }
        }

        long distinct = toApply.stream()
                .filter(p -> data.levelOf(p) == 0)
                .distinct()
                .count();
        long existing = data.entries().size();
        if (existing + distinct > MAX_PROPERTIES) {
            return Result.fail("forge.frontier.error.too_many");
        }

        ForgedData newData = data;
        for (ForgedProperty property : toApply) {
            newData = newData.withLevel(property, data.levelOf(property) + 1);
        }

        ItemStack result = weapon.copy();
        result.setCount(1);
        result.set(FrontierComponents.FORGED.get(), newData);
        applyAttributeModifiers(result, newData);
        applyDurabilityBonus(result);
        return Result.of(result);
    }

    /** Cumulative diminishing gain for a property at the given level. */
    private static float cumulative(ForgedProperty property, int level) {
        float total = 0;
        for (int l = 1; l <= level; l++) {
            total += ForgedProperty.levelWeight(l);
        }
        return total;
    }

    private static void applyAttributeModifiers(ItemStack stack, ForgedData data) {
        ItemAttributeModifiers current = stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
        List<ItemAttributeModifiers.Entry> kept = new ArrayList<>();
        for (ItemAttributeModifiers.Entry entry : current.modifiers()) {
            if (!entry.modifier().id().getNamespace().equals(FrontierMod.MODID)) {
                kept.add(entry);
            }
        }

        EquipmentSlotGroup mainhand = EquipmentSlotGroup.MAINHAND;
        int sharp = data.levelOf(ForgedProperty.SHARP);
        if (sharp > 0) {
            addModifier(kept, "sharp_damage", Attributes.ATTACK_DAMAGE, 0.6F * cumulative(ForgedProperty.SHARP, sharp), mainhand);
        }
        int balanced = data.levelOf(ForgedProperty.BALANCED);
        if (balanced > 0) {
            addModifier(kept, "balanced_speed", Attributes.ATTACK_SPEED, 0.15F * cumulative(ForgedProperty.BALANCED, balanced), mainhand);
            addModifier(kept, "balanced_reach", Attributes.ENTITY_INTERACTION_RANGE, 0.25F * cumulative(ForgedProperty.BALANCED, balanced), mainhand);
        }
        int heavy = data.levelOf(ForgedProperty.HEAVY);
        if (heavy > 0) {
            addModifier(kept, "heavy_knockback", Attributes.ATTACK_KNOCKBACK, 0.5F * cumulative(ForgedProperty.HEAVY, heavy), mainhand);
            addModifier(kept, "heavy_damage", Attributes.ATTACK_DAMAGE, 0.3F * cumulative(ForgedProperty.HEAVY, heavy), mainhand);
        }

        stack.set(DataComponents.ATTRIBUTE_MODIFIERS, new ItemAttributeModifiers(List.copyOf(kept), current.showInTooltip()));
    }

    private static void addModifier(List<ItemAttributeModifiers.Entry> list, String name,
                                    Holder<net.minecraft.world.entity.ai.attributes.Attribute> attribute,
                                    float amount, EquipmentSlotGroup group) {
        list.add(new ItemAttributeModifiers.Entry(attribute,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FrontierMod.MODID, "forged/" + name),
                        amount, AttributeModifier.Operation.ADD_VALUE), group));
    }

    private static void applyDurabilityBonus(ItemStack stack) {
        int base = stack.getItem().getDefaultInstance().getMaxDamage();
        if (base <= 0) {
            return;
        }
        int current = stack.getMaxDamage();
        int target = Math.min((int) (base * 1.45F), (int) (current * 1.15F));
        if (target > current) {
            stack.set(DataComponents.MAX_DAMAGE, target);
        }
    }

    public static Stats statsOf(ItemStack stack) {
        if (stack.isEmpty()) {
            return Stats.ZERO;
        }
        float damage = 1.0F;
        float speed = 4.0F;
        float knockback = 0.0F;
        float reach = 3.0F;
        ItemAttributeModifiers modifiers = stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
        for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
            if (entry.slot() != EquipmentSlotGroup.MAINHAND && entry.slot() != EquipmentSlotGroup.ANY) {
                continue;
            }
            if (entry.attribute() == Attributes.ATTACK_DAMAGE) {
                damage = applyOp(damage, entry.modifier());
            } else if (entry.attribute() == Attributes.ATTACK_SPEED) {
                speed = applyOp(speed, entry.modifier());
            } else if (entry.attribute() == Attributes.ATTACK_KNOCKBACK) {
                knockback = applyOp(knockback, entry.modifier());
            } else if (entry.attribute() == Attributes.ENTITY_INTERACTION_RANGE) {
                reach = applyOp(reach, entry.modifier());
            }
        }
        ForgedData data = dataOf(stack);
        float crit = 2.0F * cumulative(ForgedProperty.SHARP, data.levelOf(ForgedProperty.SHARP))
                + 4.0F * cumulative(ForgedProperty.PRECISION, data.levelOf(ForgedProperty.PRECISION));
        return new Stats(round1(damage), round1(crit), round2(reach), stack.getMaxDamage(), round2(speed), round2(knockback));
    }

    private static float applyOp(float base, AttributeModifier modifier) {
        return switch (modifier.operation()) {
            case ADD_VALUE -> base + (float) modifier.amount();
            case ADD_MULTIPLIED_BASE -> base * (1 + (float) modifier.amount());
            case ADD_MULTIPLIED_TOTAL -> base * (1 + (float) modifier.amount());
        };
    }

    private static float round1(float f) {
        return Math.round(f * 10.0F) / 10.0F;
    }

    private static float round2(float f) {
        return Math.round(f * 100.0F) / 100.0F;
    }
}
