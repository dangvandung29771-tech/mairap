package com.frontier.frontier.event;

import com.frontier.frontier.FrontierMod;
import com.frontier.frontier.block.EnrichedFarmlandBlock;
import com.frontier.frontier.forge.ForgedData;
import com.frontier.frontier.forge.ForgedProperty;
import com.frontier.frontier.forge.ForgingLogic;
import com.frontier.frontier.init.FrontierAttachments;
import com.frontier.frontier.init.FrontierComponents;
import com.frontier.frontier.init.FrontierEntities;
import com.frontier.frontier.init.FrontierItems;
import com.frontier.frontier.init.FrontierSounds;
import com.frontier.frontier.relic.RelicData;
import com.frontier.frontier.relic.RelicEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.FarmBlock;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

/** All gameplay mutations: forged combat rules, relics, farming. */
@EventBusSubscriber(modid = FrontierMod.MODID)
public final class CommonEvents {
    private CommonEvents() {
    }

    // === Mob attributes (mod bus; wired manually in FrontierMod) ===
    public static void onAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(FrontierEntities.COPPER_SHIELD_SKELETON.get(),
                com.frontier.frontier.entity.CopperShieldSkeleton.createAttributes().build());
        event.put(FrontierEntities.GOLD_DIGGER_ZOMBIE.get(),
                com.frontier.frontier.entity.GoldDiggerZombie.createAttributes().build());
    }

    // === Forged weapon: critical chance ===
    @SubscribeEvent
    public static void onCriticalHit(CriticalHitEvent event) {
        Player player = event.getEntity();
        ForgedData data = ForgingLogic.dataOf(player.getMainHandItem());
        if (data.isEmpty()) {
            return;
        }
        float chance = 0.02F * cumulative(data.levelOf(ForgedProperty.SHARP))
                + 0.04F * cumulative(data.levelOf(ForgedProperty.PRECISION));
        if (chance > 0 && player.getRandom().nextFloat() < chance) {
            float bonus = 0.1F * data.levelOf(ForgedProperty.PRECISION);
            event.setDamageModifier(Math.max(event.getDamageModifier(), 1.5F + bonus));
            event.setVanillaCritical(true);
        }
    }

    // === Forged weapon: blood / precision opening strike / void mark ===
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Pre event) {
        DamageSource source = event.getSource();
        if (!(source.getEntity() instanceof Player player)) {
            return;
        }
        ForgedData data = ForgingLogic.dataOf(player.getMainHandItem());
        if (data.isEmpty()) {
            return;
        }
        float damage = event.getNewDamage();
        boolean changed = false;

        int blood = data.levelOf(ForgedProperty.BLOOD);
        if (blood > 0 && player.getHealth() < player.getMaxHealth() * 0.5F) {
            damage *= 1.0F + 0.12F * cumulative(blood);
            changed = true;
        }

        int precision = data.levelOf(ForgedProperty.PRECISION);
        LivingEntity target = event.getEntity();
        if (precision > 0 && target.getHealth() >= target.getMaxHealth() * 0.95F) {
            damage *= 1.0F + 0.1F * precision; // opening strike on weak points
            changed = true;
        }

        int voidLevel = data.levelOf(ForgedProperty.VOID);
        if (voidLevel > 0) {
            CompoundTag custom = player.getCustomData();
            int hits = custom.getInt("frontier:voidHits") + 1;
            custom.putInt("frontier:voidHits", hits);
            if (hits >= 4) {
                custom.putInt("frontier:voidHits", 0);
                damage += 2.0F + voidLevel; // void mark burst
                changed = true;
                if (player.level() instanceof ServerLevel serverLevel) {
                    serverLevel.playSound(null, target.blockPosition(), FrontierSounds.RELIC_ACTIVATE.get(),
                            SoundSource.PLAYERS, 0.5F, 0.5F);
                }
            }
        }

        if (changed) {
            event.getContainer().setNewDamage(damage);
        }
    }

    private static float cumulative(int level) {
        float total = 0;
        for (int l = 1; l <= level; l++) {
            total += ForgedProperty.levelWeight(l);
        }
        return total;
    }

    // === Relics ===
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        RelicEffects.onPlayerTick(event.getEntity());
    }

    @SubscribeEvent
    public static void onFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player
                && RelicEffects.hasRelic(player, FrontierItems.FEATHER_OF_GRACE.get())) {
            event.setDistance(Math.max(0, event.getDistance() - 6.0F));
            event.setDamageMultiplier(event.getDamageMultiplier() * 0.5F);
        }
    }

    @SubscribeEvent
    public static void onKnockBack(LivingKnockBackEvent event) {
        if (event.getEntity() instanceof Player player
                && RelicEffects.hasRelic(player, FrontierItems.STONEHEART.get())) {
            event.setStrength(event.getStrength() * 0.3F);
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        DamageSource source = event.getSource();
        if (source.getEntity() instanceof Player player) {
            RelicData data = player.getData(FrontierAttachments.RELICS.get());
            if (data.hasRelic(FrontierItems.BLOOD_PENDANT.get()) && data.pendantCooldown <= 0
                    && player.getHealth() < player.getMaxHealth()) {
                data.pendantCooldown = 60;
                player.heal(2.0F);
                player.level().playSound(null, player.blockPosition(), FrontierSounds.RELIC_ACTIVATE.get(),
                        SoundSource.PLAYERS, 0.5F, 0.7F);
            }
        }
    }

    // === Farming: compost conversion + fertility inspection ===
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        ItemStack held = event.getItemStack();
        BlockPos pos = event.getPos();
        var state = event.getLevel().getBlockState(pos);

        if (state.getBlock() instanceof FarmBlock && !(state.getBlock() instanceof EnrichedFarmlandBlock)
                && held.is(FrontierItems.COMPOST_CAKE.get())) {
            if (!event.getLevel().isClientSide) {
                event.getLevel().setBlock(pos,
                        com.frontier.frontier.init.FrontierBlocks.ENRICHED_FARMLAND.get().defaultBlockState()
                                .setValue(FarmBlock.MOISTURE, state.getValue(FarmBlock.MOISTURE))
                                .setValue(EnrichedFarmlandBlock.FERTILITY, 7), 3);
                held.shrink(1);
                event.getLevel().playSound(null, pos, net.minecraft.sounds.SoundEvents.COMPOSTER_FILL_SUCCESS,
                        SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            event.setUseBlock(net.neoforged.bus.api.Event.Result.DENY);
            event.setUseItem(net.neoforged.bus.api.Event.Result.DENY);
            return;
        }

        if (state.getBlock() instanceof FarmBlock && player.isSecondaryUseActive() && held.isEmpty()) {
            if (!event.getLevel().isClientSide && player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                if (state.getBlock() instanceof EnrichedFarmlandBlock) {
                    int fertility = state.getValue(EnrichedFarmlandBlock.FERTILITY);
                    serverPlayer.displayClientMessage(Component.translatable("frontier.fertility.display",
                            EnrichedFarmlandBlock.fertilityBar(fertility), fertility).withStyle(ChatFormatting.GREEN), true);
                } else {
                    serverPlayer.displayClientMessage(Component.translatable("frontier.fertility.natural")
                            .withStyle(ChatFormatting.GRAY), true);
                }
            }
        }
    }

    // === Tooltips for forged weapons ===
    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        ForgedData data = event.getItemStack().get(FrontierComponents.FORGED.get());
        if (data == null || data.isEmpty()) {
            return;
        }
        event.getToolTip().add(Component.translatable("forged.frontier.header").withStyle(ChatFormatting.GOLD));
        for (ForgedData.Entry entry : data.entries()) {
            event.getToolTip().add(Component.literal("◆ ").append(entry.property().displayName(entry.level())));
        }
        ForgingLogic.Stats stats = ForgingLogic.statsOf(event.getItemStack());
        event.getToolTip().add(Component.translatable("forged.frontier.summary",
                String.format("%.1f", stats.crit()), String.format("%.2f", stats.reach()))
                .withStyle(ChatFormatting.DARK_GRAY));
    }
}
