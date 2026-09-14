package com.frontier.frontier.relic;

import com.frontier.frontier.init.FrontierAttachments;
import com.frontier.frontier.init.FrontierComponents;
import com.frontier.frontier.init.FrontierItems;
import com.frontier.frontier.init.FrontierSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/** Server-side behaviour of the six relics, evaluated once per player tick. */
public final class RelicEffects {
    private static final int CLIMB_MAX_CHARGE = 26;
    private static final int CLIMB_COOLDOWN = 80;

    private RelicEffects() {
    }

    public static void onPlayerTick(Player player) {
        if (player.level().isClientSide) {
            return;
        }
        RelicData data = player.getData(FrontierAttachments.RELICS.get());
        tickClimbersClaw(player, data);
        tickInfernalRing(player, data);
        tickExplorerCompass(player, data);
        if (data.climbCooldown > 0) data.climbCooldown--;
        if (data.pendantCooldown > 0) data.pendantCooldown--;
    }

    private static void tickClimbersClaw(Player player, RelicData data) {
        if (!data.hasRelic(FrontierItems.CLIMBERS_CLAW.get())) {
            return;
        }
        boolean wallContact = player.horizontalCollision && !player.onGround() && !player.isInWater();
        if (wallContact && data.climbCooldown <= 0 && data.climbCharge < CLIMB_MAX_CHARGE) {
            player.setDeltaMovement(player.getDeltaMovement().x, 0.16D, player.getDeltaMovement().z);
            player.fallDistance = 0;
            data.climbCharge++;
            if (player.tickCount % 8 == 0) {
                player.level().playSound(null, player.blockPosition(), FrontierSounds.RELIC_ACTIVATE.get(),
                        SoundSource.PLAYERS, 0.25F, 1.6F);
            }
        } else if (data.climbCharge > 0 && (!wallContact || player.onGround())) {
            if (data.climbCharge >= CLIMB_MAX_CHARGE) {
                data.climbCooldown = CLIMB_COOLDOWN;
            }
            data.climbCharge = 0;
        }
    }

    private static void tickInfernalRing(Player player, RelicData data) {
        if (!data.hasRelic(FrontierItems.INFERNAL_RING.get())) {
            return;
        }
        float ratio = player.getHealth() / Math.max(1.0F, player.getMaxHealth());
        if (ratio >= 0.3F || player.tickCount % 15 != 0) {
            return;
        }
        BlockPos pos = player.blockPosition().above();
        // Clear the previous glow so the ring leaves no litter behind.
        BlockPos last = data.lastLightPos;
        if (last != null && !last.equals(pos) && last.closerThan(player.blockPosition(), 8)) {
            BlockState old = player.level().getBlockState(last);
            if (old.is(Blocks.LIGHT)) {
                player.level().setBlock(last, Blocks.CAVE_AIR.defaultBlockState(), 3);
            }
        }
        BlockState state = player.level().getBlockState(pos);
        if (state.isAir()) {
            player.level().setBlock(pos, Blocks.LIGHT.defaultBlockState(), 3);
            data.lastLightPos = pos;
            player.level().playSound(null, pos, FrontierSounds.RELIC_ACTIVATE.get(), SoundSource.PLAYERS, 0.3F, 0.8F);
        }
    }

    private static void tickExplorerCompass(Player player, RelicData data) {
        if (!data.hasRelic(FrontierItems.EXPLORER_COMPASS.get()) || player.tickCount % 100 != 0) {
            return;
        }
        ItemStack compass = data.hasRelic(FrontierItems.EXPLORER_COMPASS.get())
                ? findRelicStack(player, data, FrontierItems.EXPLORER_COMPASS.get())
                : ItemStack.EMPTY;
        BlockPos target = compass.get(FrontierComponents.CRYPT_POS.get());
        if (target == null) {
            return;
        }
        double distance = player.blockPosition().distSqr(target);
        if (distance < 24 * 24) {
            player.displayClientMessage(Component.translatable("relic.frontier.compass.nearby")
                    .withStyle(ChatFormatting.DARK_GREEN), true);
            return;
        }
        Vec3 delta = Vec3.atLowerCornerOf(target.subtract(player.blockPosition()));
        double angle = Math.toDegrees(Math.atan2(delta.x, -delta.z)) - player.getYRot();
        angle = ((angle % 360) + 540) % 360 - 180;
        String direction;
        if (angle > -45 && angle <= 45) direction = "relic.frontier.compass.ahead";
        else if (angle > 45 && angle <= 135) direction = "relic.frontier.compass.right";
        else if (angle > -135 && angle <= -45) direction = "relic.frontier.compass.left";
        else direction = "relic.frontier.compass.behind";
        player.displayClientMessage(Component.translatable("relic.frontier.compass.direction",
                Component.translatable(direction), (int) Math.sqrt(distance)).withStyle(ChatFormatting.DARK_GREEN), true);
    }

    private static ItemStack findRelicStack(Player player, RelicData data, net.minecraft.world.item.Item item) {
        if (data.getSlot(0).is(item)) return data.getSlot(0);
        if (data.getSlot(1).is(item)) return data.getSlot(1);
        return ItemStack.EMPTY;
    }

    public static boolean hasRelic(Player player, net.minecraft.world.item.Item item) {
        return player.getData(FrontierAttachments.RELICS.get()).hasRelic(item);
    }
}
