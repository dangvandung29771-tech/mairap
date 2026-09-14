package com.frontier.frontier.entity;

import com.frontier.frontier.init.FrontierItems;
import com.frontier.frontier.init.FrontierSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Pocket Airship — a small coal-fired craft. Deliberately not an Elytra
 * replacement: moderate speed, a hard altitude ceiling, real fuel costs and
 * sluggish acceleration. Ascend with jump, descend with sneak.
 */
public class AirshipEntity extends Entity {
    public static final int MAX_FUEL = 4800;                 // 3 coals
    public static final int COAL_FUEL = 1600;
    public static final int CHARCOAL_FUEL = 1200;
    public static final double MAX_RISE = 46.0D;              // ceiling above departure height
    public static final double MAX_Y = 150.0D;
    public static final float MAX_SPEED = 0.21F;

    private static final EntityDataAccessor<Integer> DATA_FUEL =
            SynchedEntityData.defineId(AirshipEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_ENGINE =
            SynchedEntityData.defineId(AirshipEntity.class, EntityDataSerializers.BOOLEAN);

    private double departureY = Double.NaN;
    private float speed;
    private float inputForward;
    private float inputTurn;
    private boolean inputAscend;
    private boolean inputDescend;
    private boolean wasEngineOn;
    /** Client animation helpers. */
    public float propellerPhase;
    public float balloonPhase;

    public AirshipEntity(EntityType<? extends AirshipEntity> type, Level level) {
        super(type, level);
        this.blocksBuilding = true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_FUEL, 0);
        builder.define(DATA_ENGINE, false);
    }

    public int getFuel() {
        return this.entityData.get(DATA_FUEL);
    }

    public boolean isEngineOn() {
        return this.entityData.get(DATA_ENGINE);
    }

    public void setInput(float forward, float turn, boolean ascend, boolean descend) {
        this.inputForward = forward;
        this.inputTurn = turn;
        this.inputAscend = ascend;
        this.inputDescend = descend;
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.72D;
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().isEmpty();
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        if (this.addFuelFrom(held, player)) {
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        if (player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        }
        if (!this.level().isClientSide) {
            return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
        }
        return InteractionResult.SUCCESS;
    }

    private boolean addFuelFrom(ItemStack held, Player player) {
        int value = 0;
        if (held.is(Items.COAL)) {
            value = COAL_FUEL;
        } else if (held.is(Items.CHARCOAL)) {
            value = CHARCOAL_FUEL;
        }
        if (value == 0 || this.getFuel() >= MAX_FUEL) {
            return false;
        }
        if (!this.level().isClientSide) {
            this.entityData.set(DATA_FUEL, Math.min(MAX_FUEL, this.getFuel() + value));
            if (!player.getAbilities().instabuild) {
                held.shrink(1);
            }
            this.level().playSound(null, this.blockPosition(), FrontierSounds.AIRSHIP_STEAM.get(),
                    SoundSource.NEUTRAL, 0.8F, 1.2F);
        }
        return true;
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source) || this.level().isClientSide) {
            return false;
        }
        this.ejectPassengers();
        this.spawnAtLocation(new ItemStack(FrontierItems.POCKET_AIRSHIP.get()));
        this.discard();
        return true;
    }

    private void ejectPassengers() {
        for (Entity passenger : this.getPassengers()) {
            passenger.stopRiding();
        }
    }

    @Override
    public void tick() {
        super.tick();
        balloonPhase += 0.03F;
        if (this.isEngineOn()) {
            propellerPhase += 1.6F;
        } else {
            propellerPhase += 0.08F;
        }

        if (this.level().isClientSide) {
            this.clientEffects();
            return;
        }

        this.runEngine();
        this.fly();

        // Engine state sound cue.
        if (this.isEngineOn() && !this.wasEngineOn) {
            this.level().playSound(null, this.blockPosition(), FrontierSounds.AIRSHIP_ENGINE.get(),
                    SoundSource.NEUTRAL, 1.0F, 1.0F);
        }
        this.wasEngineOn = this.isEngineOn();

        if (Double.isNaN(departureY) && this.getControllingPassenger() != null) {
            departureY = this.getY();
        }
        if (this.getControllingPassenger() == null) {
            departureY = Double.NaN;
        }
    }

    private void runEngine() {
        boolean piloted = this.getControllingPassenger() instanceof Player;
        int fuel = this.getFuel();
        boolean engineOn = piloted && fuel > 0;
        this.entityData.set(DATA_ENGINE, engineOn);
        if (engineOn && this.tickCount % 20 == 0) {
            this.entityData.set(DATA_FUEL, Math.max(0, fuel - 5)); // ~320s per coal
            if (this.tickCount % 60 == 0) {
                this.level().playSound(null, this.blockPosition(), FrontierSounds.AIRSHIP_ENGINE.get(),
                        SoundSource.NEUTRAL, 0.7F, 1.0F);
            }
        }
    }

    private void fly() {
        boolean engineOn = this.isEngineOn();
        Vec3 motion = this.getDeltaMovement();

        // Steering.
        this.setYRot(this.getYRot() + inputTurn * (engineOn ? 2.6F : 1.2F));
        float yawRad = this.getYRot() * Mth.DEG_TO_RAD;

        // Thrust with slow spool-up/down.
        if (engineOn) {
            speed += inputForward * 0.0045F;
        } else {
            speed *= 0.985F;
        }
        speed = Mth.clamp(speed, -MAX_SPEED * 0.5F, MAX_SPEED);

        double vertical = motion.y;
        if (engineOn) {
            if (inputAscend && !this.atAltitudeCeiling()) {
                vertical = Math.min(vertical + 0.02D, 0.16D);
            } else if (inputDescend) {
                vertical = Math.max(vertical - 0.02D, -0.18D);
            } else {
                vertical *= 0.92D; // the balloon holds altitude
            }
        } else {
            vertical = Math.max(vertical - 0.02D, -0.12D); // slow, safe sink
        }

        if (this.atAltitudeCeiling() && vertical > 0) {
            vertical = 0;
        }

        this.setDeltaMovement(-Mth.sin(yawRad) * speed, vertical, Mth.cos(yawRad) * speed);
        this.move(MoverType.SELF, this.getDeltaMovement());

        if (this.horizontalCollision) {
            speed *= -0.2F;
        }
        if (this.onGround()) {
            speed *= 0.6F;
            if (this.verticalCollision && vertical <= 0) {
                this.setDeltaMovement(this.getDeltaMovement().multiply(1, 0, 1));
            }
        }
    }

    private boolean atAltitudeCeiling() {
        return (!Double.isNaN(departureY) && this.getY() - departureY >= MAX_RISE) || this.getY() >= MAX_Y;
    }

    private void clientEffects() {
        if (this.isEngineOn() && this.tickCount % 3 == 0) {
            this.level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    this.getX() + (this.random.nextDouble() - 0.5) * 0.4,
                    this.getY() + 0.5,
                    this.getZ() + (this.random.nextDouble() - 0.5) * 0.4,
                    0, 0.04, 0);
        }
    }

    @Override
    protected Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.entityData.set(DATA_FUEL, tag.getInt("Fuel"));
        if (tag.contains("DepartureY")) {
            this.departureY = tag.getDouble("DepartureY");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Fuel", this.getFuel());
        if (!Double.isNaN(departureY)) {
            tag.putDouble("DepartureY", departureY);
        }
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(FrontierItems.POCKET_AIRSHIP.get());
    }
}
