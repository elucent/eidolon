package elucent.eidolon.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import elucent.eidolon.ClientConfig;
import elucent.eidolon.ClientEvents;
import elucent.eidolon.util.RenderUtil;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.tileentity.BedTileEntityRenderer;
import net.minecraft.client.multiplayer.ClientLevel;

public class BubbleParticle extends GenericParticle {
    IAnimatedSprite animation;

    public BubbleParticle(ClientLevel world, GenericParticleData data, IAnimatedSprite animation, double x, double y, double z, double vx, double vy, double vz) {
        super(world, data, x, y, z, vx, vy, vz);
        BedTileEntityRenderer ch;
        this.animation = animation;
    }

    @Override
    public void tick() {
        super.tick();
        this.yd *= 0.8;
        setSpriteFromAge(this.animation);
    }

    @Override
    public void render(VertexConsumer b, ActiveRenderInfo info, float pticks) {
        super.render(ClientConfig.BETTER_LAYERING.get() ? ClientEvents.getDelayedRender().getBuffer(RenderUtil.GLOWING_PARTICLE) : b, info, pticks);
    }
}
