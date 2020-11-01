package elucent.eidolon.particle;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import elucent.eidolon.ClientConfig;
import elucent.eidolon.Events;
import elucent.eidolon.util.RenderUtil;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.tileentity.BedTileEntityRenderer;
import net.minecraft.client.world.ClientWorld;

public class SparkleParticle extends GenericParticle {
    public SparkleParticle(ClientWorld world, GenericParticleData data, double x, double y, double z, double vx, double vy, double vz) {
        super(world, data, x, y, z, vx, vy, vz);
        BedTileEntityRenderer ch;
    }

    @Override
    protected int getBrightnessForRender(float partialTicks) {
        return 0xF000F0;
    }

    @Override
    public void renderParticle(IVertexBuilder b, ActiveRenderInfo info, float pticks) {
        super.renderParticle(ClientConfig.BETTER_LAYERING.get() ? Events.getDelayedRender().getBuffer(RenderUtil.GLOWING_PARTICLE) : b, info, pticks);
    }
}
