package com.frontier.frontier.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Supplier;

/** Deploys a Frontier vehicle entity at the aimed location. */
public class VehicleItem extends Item {
    private final Supplier<? extends EntityType<? extends Entity>> entityType;

    public VehicleItem(Properties properties, Supplier<? extends EntityType<? extends Entity>> entityType) {
        super(properties.stacksTo(1));
        this.entityType = entityType;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        HitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
        if (hit.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(stack);
        }
        Vec3 viewVector = player.getViewVector(1.0F);
        List<Entity> entities = level.getEntities(player,
                player.getBoundingBox().expandTowards(viewVector.scale(5.0)).inflate(1.0),
                entity -> entity != player && entity.isPickable());
        if (!entities.isEmpty()) {
            Vec3 eyePosition = player.getEyePosition();
            for (Entity entity : entities) {
                AABB box = entity.getBoundingBox().inflate(entity.getPickRadius());
                if (box.contains(eyePosition)) {
                    return InteractionResultHolder.pass(stack);
                }
            }
        }
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            Entity vehicle = entityType.get().create(serverLevel);
            if (vehicle == null) {
                return InteractionResultHolder.fail(stack);
            }
            vehicle.moveTo(hit.getLocation().x, hit.getLocation().y, hit.getLocation().z,
                    player.getYRot() + 180.0F, 0.0F);
            vehicle.setYBodyRot(player.getYRot() + 180.0F);
            serverLevel.addFreshEntity(vehicle);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
