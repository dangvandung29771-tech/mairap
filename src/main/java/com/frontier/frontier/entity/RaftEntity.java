package com.frontier.frontier.entity;

import com.frontier.frontier.init.FrontierItems;
import com.frontier.frontier.init.FrontierSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

/**
 * A large hand-built raft: slower than a vanilla boat, but it carries a
 * chest, a furnace and up to four passengers — a small mobile base.
 */
public class RaftEntity extends Entity implements HasCustomInventoryScreen {
    private static final EntityDataAccessor<Integer> DATA_HURT = SynchedEntityData.defineId(RaftEntity.class, EntityDataSerializers.INT);
    public static final int PASSENGER_SLOTS = 4;
    public static final int CHEST_SLOTS = 27;

    private final SimpleContainer chest = new SimpleContainer(CHEST_SLOTS);
    /** Client animation state. */
    public float oarPhase;
    public float bobPhase;
    private float speed;
    private float steerInput;
    private boolean inputForward;

    public RaftEntity(EntityType<? extends RaftEntity> type, Level level) {
        super(type, level);
        this.blocksBuilding = true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_HURT, 0);
    }

    public double getPassengerRidingOffset() {
        return 0.55D;
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().size() < PASSENGER_SLOTS;
    }

    @Override
    public void positionRider(Entity passenger, MoveFunction callback) {
        if (this.hasPassenger(passenger)) {
            float offsetAngle = this.getYRot();
            int index = this.getPassengers().indexOf(passenger);
            float side = index == 1 ? 0.65F : index == 2 ? -0.65F : 0.0F;
            float fore = index >= 1 && index <= 2 ? 0.55F : index == 3 ? -0.9F : 0.9F;
            double x = this.getX() + (-side * Mth.cos(offsetAngle * Mth.DEG_TO_RAD) - fore * Mth.sin(offsetAngle * Mth.DEG_TO_RAD));
            double z = this.getZ() + (-side * Mth.sin(offsetAngle * Mth.DEG_TO_RAD) + fore * Mth.cos(offsetAngle * Mth.DEG_TO_RAD));
            callback.accept(passenger, x, this.getY() + this.getPassengerRidingOffset(), z);
            passenger.setYBodyRot(offsetAngle);
        }
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    public boolean isMoving() {
        return inputForward || Math.abs(speed) > 0.01F;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.isSecondaryUseActive()) {
            if (!this.level().isClientSide) {
                this.openCustomInventoryScreen(player);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        if (!this.level().isClientSide) {
            return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void openCustomInventoryScreen(Player player) {
        if (!this.level().isClientSide) {
            player.openMenu(new SimpleMenuProvider(
                    (id, inv, p) -> ChestMenu.threeRows(id, inv, chest),
                    Component.translatable("container.frontier.large_raft")));
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        if (!this.level().isClientSide && !this.isRemoved()) {
            this.setHurtTime(40);
            this.markHurt();
            if (amount >= 4.0F || this.random.nextInt(3) == 0) {
                this.dropAll();
                this.discard();
            }
        }
        return true;
    }

    private void dropAll() {
        Containers.dropContents(this.level(), this, chest);
        this.spawnAtLocation(new ItemStack(FrontierItems.LARGE_RAFT.get()));
    }

    @Override
    public void tick() {
        super.tick();
        bobPhase += 0.05F;
        if (this.isMoving()) {
            oarPhase += 0.18F;
        }

        if (this.level().isClientSide) {
            return;
        }

        this.controlRaft();
        this.floatAndMove();

        int hurt = this.getHurtTime();
        if (hurt > 0) {
            this.setHurtTime(hurt - 1);
        }
        if (this.fallDistance > 3.0F && this.onGround()) {
            this.playSound(FrontierSounds.RAFT_CREAK.get(), 1.0F, 0.8F);
        }
        if (this.isMoving() && this.tickCount % 14 == 0) {
            this.playSound(FrontierSounds.RAFT_WATER.get(), 0.6F, 0.9F + this.random.nextFloat() * 0.2F);
        }
    }

    private void controlRaft() {
        Entity controller = this.getControllingPassenger();
        inputForward = false;
        steerInput = 0;
        if (controller instanceof Player player) {
            inputForward = player.zza > 0.01F;
            steerInput = player.xxa;
            if (player.zza < -0.01F) {
                speed -= 0.004F; // gentle braking/reverse
            }
        }
        if (inputForward) {
            speed = Math.min(speed + 0.006F, 0.075F); // deliberately slower than a boat
        } else {
            speed *= 0.92F;
        }
        this.setYRot(this.getYRot() + steerInput * 2.4F);
    }

    private void floatAndMove() {
        double gravity = -0.04D;
        Vec3 motion = this.getDeltaMovement();
        double waterHeight = this.getWaterLevel();
        boolean inWater = waterHeight > -1.0D;

        double targetY;
        if (inWater) {
            targetY = waterHeight;
            double dy = targetY - this.getY();
            double vertical = Mth.clamp(dy * 0.25D, -0.08D, 0.1D);
            motion = new Vec3(motion.x * 0.9D, motion.y * 0.6D + vertical, motion.z * 0.9D);
            gravity = 0;
        }

        float yawRad = this.getYRot() * Mth.DEG_TO_RAD;
        motion = motion.add(-Mth.sin(yawRad) * speed, gravity, Mth.cos(yawRad) * speed);

        this.setDeltaMovement(motion);
        this.move(MoverType.SELF, this.getDeltaMovement());

        if (this.horizontalCollision) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(-0.3D, 1.0D, -0.3D));
            if (!this.level().isClientSide && this.speed > 0.05F) {
                this.playSound(FrontierSounds.RAFT_CREAK.get(), 1.0F, 0.7F);
            }
            this.speed *= 0.4F;
        }
        if (this.onGround() && !inWater) {
            this.speed *= 0.5F; // dragging over land is rough
        }
    }

    private double getWaterLevel() {
        double best = -1.0D;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                BlockPos pos = BlockPos.containing(this.getX() + dx * 0.7, this.getY(), this.getZ() + dz * 0.7);
                FluidState fluid = this.level().getFluidState(pos);
                if (fluid.is(FluidTags.WATER)) {
                    double height = pos.getY() + fluid.getHeight(this.level(), pos);
                    best = Math.max(best, height);
                }
            }
        }
        return best;
    }

    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        passenger.setYBodyRot(this.getYRot());
    }

    @Override
    public net.minecraft.world.entity.LivingEntity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null
                : this.getPassengers().get(0) instanceof net.minecraft.world.entity.LivingEntity living ? living : null;
    }

    public int getHurtTime() {
        return this.entityData.get(DATA_HURT);
    }

    public void setHurtTime(int time) {
        this.entityData.set(DATA_HURT, time);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("Items", 9)) {
            chest.fromTag(tag.getList("Items", 10), this.registryAccess());
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.put("Items", chest.createTag(this.registryAccess()));
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(FrontierItems.LARGE_RAFT.get());
    }
}
