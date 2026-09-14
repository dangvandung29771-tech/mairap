package com.frontier.frontier.relic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

/**
 * Player-bound relic state: exactly two relic slots plus small per-relic
 * cooldown/charge counters. Persisted via the NeoForge attachment system.
 */
public class RelicData {
    public static final Codec<RelicData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.optionalFieldOf("first", ItemStack.EMPTY).forGetter(d -> d.first),
            ItemStack.CODEC.optionalFieldOf("second", ItemStack.EMPTY).forGetter(d -> d.second),
            Codec.INT.optionalFieldOf("climb_charge", 0).forGetter(d -> d.climbCharge),
            Codec.INT.optionalFieldOf("climb_cooldown", 0).forGetter(d -> d.climbCooldown)
    ).apply(instance, RelicData::new));

    private ItemStack first = ItemStack.EMPTY;
    private ItemStack second = ItemStack.EMPTY;
    public int climbCharge;
    public int climbCooldown;
    public int pendantCooldown;
    public net.minecraft.core.BlockPos lastLightPos;

    public RelicData() {
    }

    private RelicData(ItemStack first, ItemStack second, int climbCharge, int climbCooldown) {
        this.first = first;
        this.second = second;
        this.climbCharge = climbCharge;
        this.climbCooldown = climbCooldown;
    }

    public ItemStack getSlot(int index) {
        return index == 0 ? first : second;
    }

    public void setSlot(int index, ItemStack stack) {
        if (index == 0) {
            first = stack;
        } else {
            second = stack;
        }
    }

    public boolean hasRelic(net.minecraft.world.item.Item item) {
        return first.is(item) || second.is(item);
    }
}
