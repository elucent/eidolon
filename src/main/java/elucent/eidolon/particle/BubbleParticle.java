package elucent.eidolon.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import elucent.eidolon.ClientConfig;
import elucent.eidolon.ClientEvents;
import elucent.eidolon.util.RenderUtil;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.blockentity.BedRenderer;

public class BubbleParticle extends GenericParticle {
    SpriteSet animation;

    public BubbleParticle(ClientLevel world, GenericParticleData data, SpriteSet animation, double x, double y, double z, double vx, double vy, double vz) {
        super(world, data, x, y, z, vx, vy, vz);
        BedRenderer ch;
        this.animation = animation;
    }

    @Override
    public void tick() {
        super.tick();
        this.yd *= 0.8;
        setSpriteFromAge(this.animation);
    }

    @Override
    public void render(VertexConsumer b, Camera info, float pticks) {
        super.render(ClientConfig.BETTER_LAYERING.get() ? ClientEvents.getDelayedRender().getBuffer(RenderUtil.GLOWING_PARTICLE) : b, info, pticks);
    }
}
