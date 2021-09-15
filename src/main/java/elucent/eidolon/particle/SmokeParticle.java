package elucent.eidolon.particle;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import elucent.eidolon.ClientConfig;
import elucent.eidolon.ClientEvents;
import elucent.eidolon.util.RenderUtil;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;

public class SmokeParticle extends GenericParticle {
    public SmokeParticle(ClientWorld world, GenericParticleData data, double x, double y, double z, double vx, double vy, double vz) {
        super(world, data, x, y, z, vx, vy, vz);
    }

    @Override
    protected float getCoeff() {
        return 1.0f - MathHelper.sin((float)Math.PI * (float)this.age / this.maxAge);
    }

    @Override
    public void tick() {
        super.tick();
        motionY *= 0.98;
    }

    @Override
    public void renderParticle(IVertexBuilder b, ActiveRenderInfo info, float pticks) {
        super.renderParticle(ClientConfig.BETTER_LAYERING.get() ? ClientEvents.getDelayedRender().getBuffer(RenderUtil.DELAYED_PARTICLE) : b, info, pticks);
    }
}
