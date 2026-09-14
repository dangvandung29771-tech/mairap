package com.frontier.frontier.entity;

import com.frontier.frontier.init.FrontierItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A prospector that never stopped working. Swings its pick with real intent
 * and occasionally tunnels through soft ground — dropping gold fragments.
 */
public class GoldDiggerZombie extends Zombie {
    private static final EntityDataAccessor<Boolean> DATA_DIGGING =
            SynchedEntityData.defineId(GoldDiggerZombie.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState idleState = new AnimationState();
    public final AnimationState swingState = new AnimationState();
    public final AnimationState digState = new AnimationState();

    private int digCooldown = 0;

    public GoldDiggerZombie(EntityType<? extends GoldDiggerZombie> type, Level level) {
        super(type, level);
        this.xpReward = 7;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes()
                .add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.ATTACK_DAMAGE, 3.5D)
                .add(Attributes.FOLLOW_RANGE, 28.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_DIGGING, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.1D, false));
        this.goalSelector.addGoal(4, new DigGoal());
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.95D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public boolean isDigging() {
        return this.entityData.get(DATA_DIGGING);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            if (!this.idleState.isStarted()) {
                this.idleState.animateWhen(true, this.tickCount);
            }
            if (this.isDigging() && !this.digState.isStarted()) {
                this.digState.animateWhen(true, this.tickCount);
            }
            if (!this.isDigging() && this.digState.isStarted() && this.digState.getAccumulatedTime() > 400) {
                this.digState.stop();
            }
        } else if (digCooldown > 0) {
            digCooldown--;
        }
    }

    @Override
    public boolean doHurtTarget(net.minecraft.world.entity.Entity target) {
        boolean hit = super.doHurtTarget(target);
        if (hit && !this.swingState.isStarted()) {
            this.swingState.animateWhen(true, this.tickCount);
        }
        return hit;
    }

    /** Occasionally excavates soft blocks in front of the digger. */
    private class DigGoal extends net.minecraft.world.entity.ai.goal.Goal {
        private BlockPos target = BlockPos.ZERO;
        private int progress;

        @Override
        public boolean canUse() {
            if (digCooldown > 0 || GoldDiggerZombie.this.level().isClientSide) {
                return false;
            }
            if (GoldDiggerZombie.this.random.nextFloat() > 0.006F) {
                return false;
            }
            BlockPos candidate = GoldDiggerZombie.this.blockPosition()
                    .relative(GoldDiggerZombie.this.getDirection());
            BlockState state = GoldDiggerZombie.this.level().getBlockState(candidate);
            if (isDiggable(state)) {
                target = candidate;
                return true;
            }
            candidate = candidate.below();
            state = GoldDiggerZombie.this.level().getBlockState(candidate);
            if (isDiggable(state)) {
                target = candidate;
                return true;
            }
            return false;
        }

        private boolean isDiggable(BlockState state) {
            return state.is(BlockTags.DIRT) || state.is(Blocks.GRAVEL) || state.is(Blocks.SAND)
                    || state.is(Blocks.CLAY) || state.is(Blocks.SOUL_SAND);
        }

        @Override
        public boolean canContinueToUse() {
            return progress < 36 && !GoldDiggerZombie.this.level().getBlockState(target).isAir();
        }

        @Override
        public void start() {
            progress = 0;
            digCooldown = 240;
            GoldDiggerZombie.this.entityData.set(DATA_DIGGING, true);
        }

        @Override
        public void tick() {
            progress++;
            if (progress % 9 == 0) {
                GoldDiggerZombie.this.playSound(SoundEvents.UI_STONECUTTER_TAKE_RESULT, 0.6F, 0.7F);
                GoldDiggerZombie.this.level().levelEvent(2001, target,
                        net.minecraft.world.level.block.Block.getId(GoldDiggerZombie.this.level().getBlockState(target)));
            }
        }

        @Override
        public void stop() {
            GoldDiggerZombie.this.entityData.set(DATA_DIGGING, false);
            GoldDiggerZombie.this.digState.stop();
            if (!GoldDiggerZombie.this.level().isClientSide
                    && !GoldDiggerZombie.this.level().getBlockState(target).isAir()) {
                GoldDiggerZombie.this.level().destroyBlock(target, true);
                float roll = GoldDiggerZombie.this.random.nextFloat();
                if (roll < 0.28F) {
                    GoldDiggerZombie.this.spawnAtLocation(new ItemStack(FrontierItems.GOLD_FRAGMENT.get()));
                } else if (roll < 0.33F) {
                    GoldDiggerZombie.this.spawnAtLocation(new ItemStack(FrontierItems.ANCIENT_SHARD.get()));
                }
            }
        }
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);
        int fragments = 1 + this.random.nextInt(2 + looting);
        for (int i = 0; i < fragments; i++) {
            this.spawnAtLocation(new ItemStack(FrontierItems.GOLD_FRAGMENT.get()));
        }
        if (this.random.nextFloat() < 0.35F) {
            this.spawnAtLocation(new ItemStack(Items.RAW_GOLD));
        }
        if (this.random.nextFloat() < 0.06F + looting * 0.02F) {
            this.spawnAtLocation(new ItemStack(FrontierItems.ANCIENT_SHARD.get()));
        }
    }
}
