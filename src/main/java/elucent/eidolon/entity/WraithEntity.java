package elucent.eidolon.entity;

import elucent.eidolon.Registry;
import net.minecraft.core.BlockPos;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public class WraithEntity extends Monster {
    public WraithEntity(EntityType<WraithEntity> type, Level worldIn) {
        super(type, worldIn);
        registerGoals();
    }

    @Override
    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

    @Override
    public boolean isInvertedHealAndHarm() {
        return true;
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        boolean flag = super.doHurtTarget(entityIn);
        if (flag && entityIn instanceof LivingEntity) {
            float f = this.level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            ((LivingEntity)entityIn).addEffect(new MobEffectInstance(Registry.CHILLED_EFFECT.get(), 100 + (int)(100 * level.getDifficulty().getId())));
        }
        return flag;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new RandomSwimmingGoal(this));
        this.applyEntityAI();
    }

    public static AttributeSupplier createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 20.0D)
            .add(Attributes.MOVEMENT_SPEED, (double)0.2F)
            .add(Attributes.ATTACK_DAMAGE, 4.0D)
            .add(Attributes.ARMOR, 0.0D)
            .build();
    }

    protected void applyEntityAI() {
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.5D, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public int getExperienceReward(Player player) {
        return 5;
    }

    @Override
    public void aiStep() {
        if (this.level.isDay() && !this.level.isClientSide) {
            float f = this.getBrightness();
            BlockPos blockpos = this.getVehicle() instanceof BoatEntity ? (new BlockPos(this.getX(), (double) Math.round(this.getY()), this.getZ())).above() : new BlockPos(this.getX(), (double) Math.round(this.getY()), this.getZ());
            if (f > 0.5F && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.level.canSeeSky(blockpos)) {
                this.setSecondsOnFire(8);
            }
        }

        // hover over water
        FluidState below = level.getBlockState(getBlockPosBelowThatAffectsMyMovement()).getFluidState();
        if (!below.isEmpty()) {
            Vec3 motion = getDeltaMovement();
            this.setOnGround(true);
            if (getY() + motion.y < getBlockPosBelowThatAffectsMyMovement().getY() + below.getOwnHeight()) {
                setNoGravity(true);
                if (motion.y < 0) setDeltaMovement(motion.multiply(1, 0, 1));
                setPos(getX(), getBlockPosBelowThatAffectsMyMovement().getY() + below.getOwnHeight(), getZ());
            }
        }
        else setNoGravity(false);

        // slow fall
        this.fallDistance = 0;
        Vec3 vector3d = this.getDeltaMovement();
        if (!this.onGround && vector3d.y < 0.0D) {
            this.setDeltaMovement(vector3d.multiply(1.0D, 0.6D, 1.0D));
        }

        super.aiStep();
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.STRAY_DEATH;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.STRAY_AMBIENT;
    }

    @Override
    public SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.STRAY_HURT;
    }
}
