package elucent.eidolon.entity;

import java.util.UUID;

import elucent.eidolon.Registry;
import elucent.eidolon.capability.IReputation;
import elucent.eidolon.deity.Deities;
import elucent.eidolon.network.MagicBurstEffectPacket;
import elucent.eidolon.network.Networking;
import elucent.eidolon.particle.Particles;
import elucent.eidolon.util.ColorUtil;
import elucent.eidolon.util.EntityUtil;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

public class NecromancerEntity extends SpellcasterIllager {
    public NecromancerEntity(EntityType<? extends SpellcasterIllager> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public boolean removeWhenFarAway(double dist) {
        return false;
    }

    boolean hack = false;

    @Override
    public boolean isCastingSpell() {
        return level.isClientSide && hack ? false : super.isCastingSpell();
    }

    @Override
    public void tick() {
        hack = true; // Used to avoid the default spell particles from SpellcastingIllagerEntity
        super.tick();
        hack = false;
        if (this.level.isClientSide && this.isCastingSpell()) {
            IllagerSpell spelltype = getCurrentSpell();
            float f = this.yBodyRot * ((float)Math.PI / 180F) + Mth.cos((float)this.tickCount * 0.6662F) * 0.25F;
            float f1 = Mth.cos(f);
            float f2 = Mth.sin(f);
            if (spelltype == IllagerSpell.FANGS) {
                Particles.create(Registry.SPARKLE_PARTICLE)
                    .setColor(1, 0.3125f, 0.375f, 0.75f, 0.375f, 1)
                    .randomVelocity(0.05f).randomOffset(0.025f)
                    .setScale(0.25f, 0.125f).setAlpha(0.25f, 0)
                    .setSpin(0.4f)
                    .spawn(level, getX() + f1 * 0.875, getY() + 2.0, getZ() + f2 * 0.875)
                    .spawn(level, getX() - f1 * 0.875, getY() + 2.0, getZ() - f2 * 0.875);
            }
            else if (spelltype == IllagerSpell.SUMMON_VEX) {
                Particles.create(Registry.WISP_PARTICLE)
                    .setColor(0.75f, 1, 1, 0.125f, 0.125f, 0.875f)
                    .randomVelocity(0.05f).randomOffset(0.025f)
                    .setScale(0.25f, 0.125f).setAlpha(0.25f, 0)
                    .spawn(level, getX() + f1 * 0.875, getY() + 2.0, getZ() + f2 * 0.875)
                    .spawn(level, getX() - f1 * 0.875, getY() + 2.0, getZ() - f2 * 0.875);
            }
        }
    }

    @Override
    public boolean isInvertedHealAndHarm() {
        return true;
    }

    class AttackSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {
        private AttackSpellGoal() {
        }

        public boolean canUse() {
            LivingEntity livingentity = NecromancerEntity.this.getTarget();
            if (livingentity != null && livingentity.isAlive()) {
                if (NecromancerEntity.this.isCastingSpell()) {
                    return false;
                } else {
                    if (NecromancerEntity.this.tickCount >= this.nextAttackTickCount) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        protected int getCastingTime() {
            return 40;
        }

        @Override
        protected int getCastingInterval() {
            return 80;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        @Override
        protected IllagerSpell getSpell() {
            return IllagerSpell.FANGS;
        }

        @Override
        protected void performSpellCasting() {
            LivingEntity target = NecromancerEntity.this.getTarget();
            Vec3 diff = target.position().subtract(NecromancerEntity.this.position());
            Vec3 norm = diff.normalize();
            if (!level.isClientSide) {
                for (int i = 0; i < 3; i ++) {
                    NecromancerSpellEntity spell = new NecromancerSpellEntity(level, getX(), getEyeY(), getZ(), norm.x + random.nextFloat() * 0.1 - 0.05, norm.y + 0.04 * diff.length() / 2 + random.nextFloat() * 0.1 - 0.05, norm.z + random.nextFloat() * 0.1 - 0.05, i * 5);
                    spell.casterId = new UUID(0, getId());
                    level.addFreshEntity(spell);
                }
            }
        }
    }

    class SummonSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {
        private SummonSpellGoal() {
        }

        @Override
        protected int getCastingTime() {
            return 40;
        }

        @Override
        protected int getCastingInterval() {
            return 200;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        @Override
        protected IllagerSpell getSpell() {
            return IllagerSpell.SUMMON_VEX;
        }

        @Override
        protected void performSpellCasting() {
            if (!level.isClientSide) {
                EntityType<?> type = random.nextBoolean() ? EntityType.SKELETON : EntityType.ZOMBIE;
                ResourceLocation biomeKey = ForgeRegistries.BIOMES.getKey(level.getBiome(blockPosition()));
                ResourceKey<Biome> biomeEntry = ResourceKey.create(ForgeRegistries.Keys.BIOMES, biomeKey);
                if (type == EntityType.SKELETON && BiomeDictionary.hasType(biomeEntry, BiomeDictionary.Type.SNOWY))
                    type = EntityType.STRAY;
                if (type == EntityType.ZOMBIE && BiomeDictionary.hasType(biomeEntry, BiomeDictionary.Type.SANDY))
                    type = EntityType.HUSK;
                if (type == EntityType.ZOMBIE && BiomeDictionary.hasType(biomeEntry, BiomeDictionary.Type.OCEAN))
                    type = EntityType.DROWNED;
                Monster entity = (Monster)type.create(level);
                entity.setPos(getX(), getY(), getZ());
                level.addFreshEntity(entity);
                entity.setTarget(getTarget());
                EntityUtil.enthrall(NecromancerEntity.this, entity);
                Networking.sendToTracking(level, blockPosition(), new MagicBurstEffectPacket(getX(), getY() + 1, getZ(), ColorUtil.packColor(255, 181, 255, 255), ColorUtil.packColor(255, 28, 31, 212)));
            }
        }
    }

    class CastingSpellGoal extends SpellcasterIllager.SpellcasterCastingSpellGoal {
        private CastingSpellGoal() {
        }

        public void tick() {
            if (NecromancerEntity.this.getTarget() != null) {
                NecromancerEntity.this.getLookControl().setLookAt(NecromancerEntity.this.getTarget(), (float)NecromancerEntity.this.getMaxHeadYRot(), (float)NecromancerEntity.this.getMaxHeadXRot());
            }
        }
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new CastingSpellGoal());
        this.goalSelector.addGoal(5, new AttackSpellGoal());
        this.goalSelector.addGoal(4, new SummonSpellGoal());
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (e) -> e.getCommandSenderWorld().getCapability(IReputation.INSTANCE).isPresent() && e.getCommandSenderWorld().getCapability(IReputation.INSTANCE).resolve().get().getReputation((Player)e, Deities.DARK_DEITY.getId()) >= 50)).setUnseenMemoryTicks(300));
    }

    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    public static AttributeSupplier createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 100.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.25D)
            .add(Attributes.FOLLOW_RANGE, 12.0D)
            .build();
    }

    @Override
    public void applyRaidBuffs(int wave, boolean p_213660_2_) {
        return;
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.EVOKER_CELEBRATE;
    }
}
