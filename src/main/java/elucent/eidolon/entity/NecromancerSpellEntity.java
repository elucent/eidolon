package elucent.eidolon.entity;

import elucent.eidolon.Registry;
import elucent.eidolon.network.MagicBurstEffectPacket;
import elucent.eidolon.network.Networking;
import elucent.eidolon.particle.Particles;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class NecromancerSpellEntity extends SpellProjectileEntity {
    public static final DataParameter<Integer> DELAY = EntityDataManager.<Integer>createKey(NecromancerSpellEntity.class, DataSerializers.VARINT);

    public NecromancerSpellEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        getDataManager().register(DELAY, 0);
    }

    public NecromancerSpellEntity(World worldIn, double x, double y, double z, double vx, double vy, double vz, int delay) {
        super(Registry.NECROMANCER_SPELL.get(), worldIn);
        setPosition(x, y, z);
        setMotion(vx, vy, vz);
        getDataManager().register(DELAY, delay);
    }

    @Override
    public void tick() {
        if (getDataManager().get(DELAY) > 0) {
            getDataManager().set(DELAY, getDataManager().get(DELAY) - 1);
            return;
        }
        super.tick();

        Vector3d motion = getMotion();
        Vector3d pos = getPositionVec();
        Vector3d norm = motion.normalize().scale(0.025f);
        for (int i = 0; i < 8; i ++) {
            double lerpX = MathHelper.lerp(i / 8.0f, prevPosX, pos.x);
            double lerpY = MathHelper.lerp(i / 8.0f, prevPosY, pos.y);
            double lerpZ = MathHelper.lerp(i / 8.0f, prevPosZ, pos.z);
            Particles.create(Registry.WISP_PARTICLE)
                .addVelocity(-norm.x, -norm.y, -norm.z)
                .setAlpha(0.375f, 0).setScale(0.25f, 0)
                .setColor(1, 0.3125f, 0.375f, 0.75f, 0.375f, 1)
                .setLifetime(5)
                .spawn(world, lerpX, lerpY, lerpZ);
            Particles.create(Registry.SMOKE_PARTICLE)
                .addVelocity(-norm.x, -norm.y, -norm.z)
                .setAlpha(0.0625f, 0).setScale(0.3125f, 0.125f)
                .setColor(0.625f, 0.375f, 1, 0.25f, 0.25f, 0.75f)
                .randomVelocity(0.025f, 0.025f)
                .setLifetime(20)
                .spawn(world, lerpX, lerpY, lerpZ);
        }
    }

    @Override
    protected void onImpact(RayTraceResult ray, Entity target) {
        target.attackEntityFrom(new IndirectEntityDamageSource(DamageSource.WITHER.getDamageType(), this, world.getEntityByID((int)casterId.getLeastSignificantBits())), 3.0f + world.getDifficulty().getId());
        onImpact(ray);
    }

    @Override
    protected void onImpact(RayTraceResult ray) {
        setDead();
        if (!world.isRemote) {
            Vector3d pos = ray.getHitVec();
            world.playSound(null, pos.x, pos.y, pos.z, SoundEvents.ENTITY_WITHER_SHOOT, SoundCategory.HOSTILE, 0.5f, rand.nextFloat() * 0.2f + 0.9f);
            Networking.sendToTracking(world, getPosition(), new MagicBurstEffectPacket(pos.x, pos.y, pos.z, ColorUtil.packColor(255, 158, 92, 255), ColorUtil.packColor(255, 60, 62, 186)));
        }
    }
}
