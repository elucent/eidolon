package elucent.eidolon.particle;

import com.mojang.blaze3d.vertex.IVertexBuilder;

import elucent.eidolon.ClientConfig;
import elucent.eidolon.ClientEvents;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.util.RenderUtil;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;

public class SignParticle extends SpriteTexturedParticle {
    Sign sign;
    public SignParticle(ClientWorld world, Sign sign, double x, double y, double z, double vx, double vy, double vz) {
        super(world, x, y, z, vx, vy, vz);
        this.setPosition(x, y, z);
        this.sign = sign;
        this.motionX = vx;
        this.motionY = vy;
        this.motionZ = vz;
        this.setMaxAge(20);
        this.particleGravity = -0.05f;
        setColor(sign.getRed(), sign.getGreen(), sign.getBlue());
        updateTraits();
    }

    protected float getCoeff() {
        return (float)this.age / this.maxAge;
    }

    protected void updateTraits() {
        float coeff = getCoeff();
        particleScale = MathHelper.lerp(coeff, 0.25f, 0.125f);
        setAlphaF(MathHelper.lerp(coeff * coeff, 0.75f, 0));
    }

    @Override
    public void tick() {
        updateTraits();
        super.tick();
        motionX *= 0.98;
        motionY *= 0.98;
        motionZ *= 0.98;
    }

    @Override
    public void renderParticle(IVertexBuilder b, ActiveRenderInfo info, float pticks) {
        super.renderParticle(ClientConfig.BETTER_LAYERING.get() ? ClientEvents.getDelayedRender().getBuffer(RenderUtil.GLOWING_BLOCK_PARTICLE) : b, info, pticks);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return SignParticleRenderType.INSTANCE;
    }
}
