package com.frontier.frontier.entity;

import com.frontier.frontier.init.FrontierItems;
import com.frontier.frontier.init.FrontierSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * A long-dead barracks guardian still carrying its corroded copper shield.
 * Frontal strikes are blocked — flank it or break its guard with timing.
 */
public class CopperShieldSkeleton extends Monster {
    private static final EntityDataAccessor<Boolean> DATA_BLOCKING =
            SynchedEntityData.defineId(CopperShieldSkeleton.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState idleState = new AnimationState();
    public final AnimationState attackState = new AnimationState();
    public final AnimationState blockState = new AnimationState();
    public final AnimationState hitState = new AnimationState();

    private int blockCooldown = 0;
    private int attackCooldown = 0;

    public CopperShieldSkeleton(EntityType<? extends CopperShieldSkeleton> type, Level level) {
        super(type, level);
        this.xpReward = 8;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 22.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.26D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .add(Attributes.ARMOR, 2.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_BLOCKING, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.15D, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.9D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.setupAnimationStates();
            return;
        }
        if (blockCooldown > 0) blockCooldown--;
        if (attackCooldown > 0) attackCooldown--;
        if (this.tickCount % 40 == 0 && !this.entityData.get(DATA_BLOCKING)) {
            this.entityData.set(DATA_BLOCKING, this.getTarget() != null && this.random.nextBoolean());
        }
    }

    private void setupAnimationStates() {
        this.idleState.animateWhen(this.tickCount > 0 && !this.attackState.isStarted() && !this.blockState.isStarted(), this.tickCount);
        LivingEntity target = this.getTarget();
        if (target != null && this.attackCooldown <= 0 && this.distanceToSqr(target) < 4.0D) {
            this.attackCooldown = 24;
            this.attackState.animateWhen(true, this.tickCount);
        }
        if (this.attackState.isStarted() && this.attackState.getAccumulatedTime() > 600) {
            this.attackState.stop();
        }
        if (this.entityData.get(DATA_BLOCKING) && !this.blockState.isStarted()) {
            this.blockState.animateWhen(true, this.tickCount);
        }
        if (!this.entityData.get(DATA_BLOCKING) && this.blockState.isStarted() && this.blockState.getAccumulatedTime() > 900) {
            this.blockState.stop();
        }
    }

    public boolean isShieldRaised() {
        return this.entityData.get(DATA_BLOCKING);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity attacker = source.getEntity();
        if (attacker instanceof LivingEntity living && !this.level().isClientSide
                && this.blockCooldown <= 0 && this.isFacing(attacker) && !source.is(net.minecraft.tags.DamageTypeTags.IS_PROJECTILE)
                && this.random.nextFloat() < 0.65F) {
            // Shield block: negate the hit, spark the copper, reset.
            this.blockCooldown = 45;
            this.entityData.set(DATA_BLOCKING, true);
            this.blockState.stop();
            this.blockState.animateWhen(true, this.tickCount);
            this.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 0.8F);
            this.playSound(FrontierSounds.GRINDSTONE_IMPACT.get(), 0.3F, 1.6F);
            Vec3 knock = living.position().subtract(this.position()).normalize().scale(0.3D);
            living.knockback(0.25D, -knock.x, -knock.z);
            return false;
        }
        this.entityData.set(DATA_BLOCKING, false);
        if (!this.hitState.isStarted()) {
            this.hitState.animateWhen(true, this.tickCount);
        }
        return super.hurt(source, amount);
    }

    private boolean isFacing(Entity entity) {
        Vec3 look = this.getViewVector(1.0F);
        Vec3 toAttacker = entity.position().subtract(this.position()).normalize();
        return look.dot(toAttacker) > 0.35D;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hit = super.doHurtTarget(target);
        if (hit) {
            this.attackState.stop();
            this.attackState.animateWhen(true, this.tickCount);
        }
        return hit;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    @Override
    protected void dropCustomDeathLoot(net.minecraft.server.level.ServerLevel level, DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);
        int looting = 0; // looting-dependent drops kept simple & server-safe
        int bones = this.random.nextInt(2 + looting);
        for (int i = 0; i < bones; i++) {
            this.spawnAtLocation(Items.BONE);
        }
        int copper = this.random.nextInt(2) + (this.random.nextFloat() < 0.25F ? 1 : 0);
        for (int i = 0; i < copper; i++) {
            this.spawnAtLocation(Items.COPPER_INGOT);
        }
        if (this.random.nextFloat() < 0.08F + looting * 0.02F) {
            this.spawnAtLocation(new ItemStack(FrontierItems.ANCIENT_SHARD.get()));
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.isPersistenceRequired();
    }
}
