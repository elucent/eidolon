package elucent.eidolon.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;

import elucent.eidolon.ClientConfig;
import elucent.eidolon.ClientEvents;
import elucent.eidolon.util.RenderUtil;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;

public class SmokeParticle extends GenericParticle {
    public SmokeParticle(ClientLevel world, GenericParticleData data, double x, double y, double z, double vx, double vy, double vz) {
        super(world, data, x, y, z, vx, vy, vz);
    }

    @Override
    protected float getCoeff() {
        return 1.0f - Mth.sin((float)Math.PI * (float)this.age / this.lifetime);
    }

    @Override
    public void tick() {
        super.tick();
        yd *= 0.98;
    }

    @Override
    public void render(VertexConsumer b, Camera info, float pticks) {
        super.render(ClientConfig.BETTER_LAYERING.get() ? ClientEvents.getDelayedRender().getBuffer(RenderUtil.DELAYED_PARTICLE) : b, info, pticks);
    }
}
