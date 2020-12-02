package elucent.eidolon.entity;

import elucent.eidolon.Registry;
import elucent.eidolon.network.MagicBurstEffectPacket;
import elucent.eidolon.network.Networking;
import elucent.eidolon.particle.Particles;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;
import java.util.UUID;

public class SoulfireProjectileEntity extends SpellProjectileEntity {
    public SoulfireProjectileEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    public void tick() {
        super.tick();

        Vector3d motion = getMotion();
        Vector3d pos = getPositionVec();
        Vector3d norm = motion.normalize().scale(0.025f);
        for (int i = 0; i < 8; i ++) {
            double lerpX = MathHelper.lerp(i / 8.0f, prevPosX, pos.x);
            double lerpY = MathHelper.lerp(i / 8.0f, prevPosY, pos.y);
            double lerpZ = MathHelper.lerp(i / 8.0f, prevPosZ, pos.z);
            Particles.create(Registry.SPARKLE_PARTICLE)
                .addVelocity(-norm.x, -norm.y, -norm.z)
                .setAlpha(0.375f, 0).setScale(0.375f, 0)
                .setColor(1, 0.875f, 0.5f, 0.5f, 0.25f, 1)
                .setLifetime(5)
                .spawn(world, lerpX, lerpY, lerpZ);
            Particles.create(Registry.WISP_PARTICLE)
                .addVelocity(-norm.x, -norm.y, -norm.z)
                .setAlpha(0.125f, 0).setScale(0.25f, 0.125f)
                .setColor(1, 0.5f, 0.625f, 0.5f, 0.25f, 1)
                .setLifetime(20)
                .spawn(world, lerpX, lerpY, lerpZ);
        }
    }

    @Override
    protected void onImpact(RayTraceResult ray, LivingEntity target) {
        target.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, world.getPlayerByUuid(casterId)), 7.0f);
        onImpact(ray);
    }

    @Override
    protected void onImpact(RayTraceResult ray) {
        setDead();
        if (!world.isRemote) {
            Vector3d pos = ray.getHitVec();
            world.playSound(null, pos.x, pos.y, pos.z, Registry.SPLASH_SOULFIRE_EVENT.get(), SoundCategory.NEUTRAL, 0.6f, rand.nextFloat() * 0.2f + 0.9f);
            Networking.sendToTracking(world, getPosition(), new MagicBurstEffectPacket(pos.x, pos.y, pos.z, ColorUtil.packColor(255, 255, 229, 125), ColorUtil.packColor(255, 124, 57, 247)));
        }
    }
}
