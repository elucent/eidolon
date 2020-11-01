package elucent.eidolon.particle;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import elucent.eidolon.ClientConfig;
import elucent.eidolon.Events;
import elucent.eidolon.util.RenderUtil;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.math.MathHelper;

public class LineWispParticle extends GenericParticle {
    double ix, iy, iz, tx, ty, tz;
    public LineWispParticle(ClientWorld world, GenericParticleData data, double x, double y, double z, double vx, double vy, double vz) {
        super(world, data, x, y, z, vx, vy, vz);
        this.ix = posX;
        this.iy = posY;
        this.iz = posZ;
        this.tx = motionX;
        this.ty = motionY;
        this.tz = motionZ;
        motionX = motionY = motionZ = 0;
    }

    @Override
    public void tick() {
        super.tick();
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        float coeff = (float)age / maxAge;
        coeff *= coeff;
        posX = MathHelper.lerp(coeff, ix, tx);
        posY = MathHelper.lerp(1 - (1 - coeff) * (1 - coeff), iy, ty);
        posZ = MathHelper.lerp(coeff, iz, tz);
        SpawnEggItem i;
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
