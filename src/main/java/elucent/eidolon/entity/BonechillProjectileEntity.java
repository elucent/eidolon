package elucent.eidolon.entity;

import elucent.eidolon.Registry;
import elucent.eidolon.network.MagicBurstEffectPacket;
import elucent.eidolon.network.Networking;
import elucent.eidolon.particle.Particles;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class BonechillProjectileEntity extends SpellProjectileEntity {
    public BonechillProjectileEntity(EntityType<?> entityTypeIn, World worldIn) {
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
            Particles.create(Registry.WISP_PARTICLE)
                .addVelocity(-norm.x, -norm.y, -norm.z)
                .setAlpha(0.0625f, 0).setScale(0.625f, 0)
                .setColor(0.875f, 1, 1, 0.375f, 0.5f, 0.75f)
                .setLifetime(5)
                .spawn(world, lerpX, lerpY, lerpZ);
            Particles.create(Registry.WISP_PARTICLE)
                .addVelocity(-norm.x, -norm.y, -norm.z)
                .setAlpha(0.125f, 0).setScale(0.25f, 0.125f)
                .setColor(1, 0.75f, 0.875f, 0.375f, 0.5f, 0.75f)
                .setLifetime(20)
                .spawn(world, lerpX, lerpY, lerpZ);
        }
    }

    @Override
    protected void onImpact(RayTraceResult ray, Entity target) {
        target.attackEntityFrom(new IndirectEntityDamageSource(Registry.FROST_DAMAGE.getDamageType(), this, world.getPlayerByUuid(casterId)), 4.0f);
        if (target instanceof LivingEntity)
            ((LivingEntity)target).addPotionEffect(new EffectInstance(Registry.CHILLED_EFFECT.get(), 300, 0));
        onImpact(ray);
    }

    @Override
    protected void onImpact(RayTraceResult ray) {
        setDead();
        if (!world.isRemote) {
            Vector3d pos = ray.getHitVec();
            world.playSound(null, pos.x, pos.y, pos.z, Registry.SPLASH_BONECHILL_EVENT.get(), SoundCategory.NEUTRAL, 0.5f, rand.nextFloat() * 0.2f + 0.9f);
            Networking.sendToTracking(world, getPosition(), new MagicBurstEffectPacket(pos.x, pos.y, pos.z, ColorUtil.packColor(255, 192, 224, 255), ColorUtil.packColor(255, 96, 128, 192)));
        }
    }
}
