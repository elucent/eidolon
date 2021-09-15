package elucent.eidolon.particle;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import elucent.eidolon.ClientConfig;
import elucent.eidolon.ClientEvents;
import elucent.eidolon.util.RenderUtil;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.tileentity.BedTileEntityRenderer;
import net.minecraft.client.world.ClientWorld;

public class BubbleParticle extends GenericParticle {
    IAnimatedSprite animation;

    public BubbleParticle(ClientWorld world, GenericParticleData data, IAnimatedSprite animation, double x, double y, double z, double vx, double vy, double vz) {
        super(world, data, x, y, z, vx, vy, vz);
        BedTileEntityRenderer ch;
        this.animation = animation;
    }

    @Override
    public void tick() {
        super.tick();
        this.motionY *= 0.8;
        selectSpriteWithAge(this.animation);
    }

    @Override
    public void renderParticle(IVertexBuilder b, ActiveRenderInfo info, float pticks) {
        super.renderParticle(ClientConfig.BETTER_LAYERING.get() ? ClientEvents.getDelayedRender().getBuffer(RenderUtil.GLOWING_PARTICLE) : b, info, pticks);
    }
}
