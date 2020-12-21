package elucent.eidolon.entity;

import elucent.eidolon.Registry;
import elucent.eidolon.deity.Deities;
import elucent.eidolon.capability.ReputationProvider;
import elucent.eidolon.network.MagicBurstEffectPacket;
import elucent.eidolon.network.Networking;
import elucent.eidolon.particle.Particles;
import elucent.eidolon.util.ColorUtil;
import elucent.eidolon.util.EntityUtil;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

public class NecromancerEntity extends SpellcastingIllagerEntity {
    public NecromancerEntity(EntityType<? extends SpellcastingIllagerEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.UNDEAD;
    }

    @Override
    public boolean canDespawn(double dist) {
        return false;
    }

    boolean hack = false;

    @Override
    public boolean isSpellcasting() {
        return world.isRemote && hack ? false : super.isSpellcasting();
    }

    @Override
    public void tick() {
        hack = true; // Used to avoid the default spell particles from SpellcastingIllagerEntity
        super.tick();
        hack = false;
        if (this.world.isRemote && this.isSpellcasting()) {
            SpellType spelltype = getSpellType();
            float f = this.renderYawOffset * ((float)Math.PI / 180F) + MathHelper.cos((float)this.ticksExisted * 0.6662F) * 0.25F;
            float f1 = MathHelper.cos(f);
            float f2 = MathHelper.sin(f);
            if (spelltype == SpellType.FANGS) {
                Particles.create(Registry.SPARKLE_PARTICLE)
                    .setColor(1, 0.3125f, 0.375f, 0.75f, 0.375f, 1)
                    .randomVelocity(0.05f).randomOffset(0.025f)
                    .setScale(0.25f, 0.125f).setAlpha(0.25f, 0)
                    .setSpin(0.4f)
                    .spawn(world, getPosX() + f1 * 0.875, getPosY() + 2.0, getPosZ() + f2 * 0.875)
                    .spawn(world, getPosX() - f1 * 0.875, getPosY() + 2.0, getPosZ() - f2 * 0.875);
            }
            else if (spelltype == SpellType.SUMMON_VEX) {
                Particles.create(Registry.WISP_PARTICLE)
                    .setColor(0.75f, 1, 1, 0.125f, 0.125f, 0.875f)
                    .randomVelocity(0.05f).randomOffset(0.025f)
                    .setScale(0.25f, 0.125f).setAlpha(0.25f, 0)
                    .spawn(world, getPosX() + f1 * 0.875, getPosY() + 2.0, getPosZ() + f2 * 0.875)
                    .spawn(world, getPosX() - f1 * 0.875, getPosY() + 2.0, getPosZ() - f2 * 0.875);
            }
        }
    }

    @Override
    public boolean isEntityUndead() {
        return true;
    }

    class AttackSpellGoal extends SpellcastingIllagerEntity.UseSpellGoal {
        private AttackSpellGoal() {
        }

        public boolean shouldExecute() {
            LivingEntity livingentity = NecromancerEntity.this.getAttackTarget();
            if (livingentity != null && livingentity.isAlive()) {
                if (NecromancerEntity.this.isSpellcasting()) {
                    return false;
                } else {
                    if (NecromancerEntity.this.ticksExisted >= this.spellCooldown) {
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
            return SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON;
        }

        @Override
        protected SpellType getSpellType() {
            return SpellType.FANGS;
        }

        @Override
        protected void castSpell() {
            LivingEntity target = NecromancerEntity.this.getAttackTarget();
            Vector3d diff = target.getPositionVec().subtract(NecromancerEntity.this.getPositionVec());
            Vector3d norm = diff.normalize();
            if (!world.isRemote) {
                for (int i = 0; i < 3; i ++) {
                    NecromancerSpellEntity spell = new NecromancerSpellEntity(world, getPosX(), getPosYEye(), getPosZ(), norm.x + rand.nextFloat() * 0.1 - 0.05, norm.y + 0.04 * diff.length() / 2 + rand.nextFloat() * 0.1 - 0.05, norm.z + rand.nextFloat() * 0.1 - 0.05, i * 5);
                    spell.casterId = new UUID(0, getEntityId());
                    world.addEntity(spell);
                }
            }
        }
    }

    class SummonSpellGoal extends SpellcastingIllagerEntity.UseSpellGoal {
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
            return SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON;
        }

        @Override
        protected SpellType getSpellType() {
            return SpellType.SUMMON_VEX;
        }

        @Override
        protected void castSpell() {
            if (!world.isRemote) {
                EntityType<?> type = rand.nextBoolean() ? EntityType.SKELETON : EntityType.ZOMBIE;
                ResourceLocation biomeKey = ForgeRegistries.BIOMES.getKey(world.getBiome(getPosition()));
                RegistryKey<Biome> biomeEntry = RegistryKey.getOrCreateKey(ForgeRegistries.Keys.BIOMES, biomeKey);
                if (type == EntityType.SKELETON && BiomeDictionary.hasType(biomeEntry, BiomeDictionary.Type.SNOWY))
                    type = EntityType.STRAY;
                if (type == EntityType.ZOMBIE && BiomeDictionary.hasType(biomeEntry, BiomeDictionary.Type.SANDY))
                    type = EntityType.HUSK;
                if (type == EntityType.ZOMBIE && BiomeDictionary.hasType(biomeEntry, BiomeDictionary.Type.OCEAN))
                    type = EntityType.DROWNED;
                MonsterEntity entity = (MonsterEntity)type.create(world);
                entity.setPosition(getPosX(), getPosY(), getPosZ());
                world.addEntity(entity);
                entity.setAttackTarget(getAttackTarget());
                EntityUtil.enthrall(NecromancerEntity.this, entity);
                Networking.sendToTracking(world, getPosition(), new MagicBurstEffectPacket(getPosX(), getPosY() + 1, getPosZ(), ColorUtil.packColor(255, 181, 255, 255), ColorUtil.packColor(255, 28, 31, 212)));
            }
        }
    }

    class CastingSpellGoal extends SpellcastingIllagerEntity.CastingASpellGoal {
        private CastingSpellGoal() {
        }

        public void tick() {
            if (NecromancerEntity.this.getAttackTarget() != null) {
                NecromancerEntity.this.getLookController().setLookPositionWithEntity(NecromancerEntity.this.getAttackTarget(), (float)NecromancerEntity.this.getHorizontalFaceSpeed(), (float)NecromancerEntity.this.getVerticalFaceSpeed());
            }
        }
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new CastingSpellGoal());
        this.goalSelector.addGoal(5, new AttackSpellGoal());
        this.goalSelector.addGoal(4, new SummonSpellGoal());
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, false, false, (e) -> e.getEntityWorld().getCapability(ReputationProvider.CAPABILITY).isPresent() && e.getEntityWorld().getCapability(ReputationProvider.CAPABILITY).resolve().get().getReputation((PlayerEntity)e, Deities.DARK_DEITY.getId()) >= 50)).setUnseenMemoryTicks(300));
    }

    protected SoundEvent getSpellSound() {
        return SoundEvents.ENTITY_EVOKER_CAST_SPELL;
    }

    public static AttributeModifierMap createAttributes() {
        return MonsterEntity.func_234295_eP_()
            .createMutableAttribute(Attributes.MAX_HEALTH, 100.0D)
            .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25D)
            .createMutableAttribute(Attributes.FOLLOW_RANGE, 12.0D)
            .create();
    }

    @Override
    public void applyWaveBonus(int wave, boolean p_213660_2_) {
        return;
    }

    @Override
    public SoundEvent getRaidLossSound() {
        return SoundEvents.ENTITY_EVOKER_CELEBRATE;
    }
}
