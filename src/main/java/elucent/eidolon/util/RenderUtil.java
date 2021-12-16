package elucent.eidolon.util;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;

import elucent.eidolon.ClientEvents;
import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderStateShard.ShaderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import net.minecraft.world.phys.Vec3;
import com.mojang.math.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RenderUtil {
    public static final RenderStateShard.TransparencyStateShard ADDITIVE_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("lightning_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    public static final RenderStateShard.TransparencyStateShard NORMAL_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("lightning_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

//    @OnlyIn(Dist.CLIENT)
//    public static class RescaledLightingState extends RenderStateShard.DiffuseLightingStateShard {
//        public RescaledLightingState() {
//            super(true);
//        }
//
//        @Override
//        public void setupRenderState() {
//            RenderSystem.enableLighting();
//            RenderSystem.enableRescaleNormal();
//        }
//
//        @Override
//        public void clearRenderState() {
//            RenderSystem.disableLighting();
//            RenderSystem.disableRescaleNormal();
//        }
//    }

    public static RenderType GLOWING_SPRITE = RenderType.create(
        Eidolon.MODID + ":glowing_sprite",
        DefaultVertexFormat.POSITION_TEX_COLOR,
        Mode.QUADS, 256, true, false,
        RenderType.CompositeState.builder()
            .setWriteMaskState(new RenderStateShard.WriteMaskStateShard(true, false))
            .setLightmapState(new RenderStateShard.LightmapStateShard(false))
            .setTransparencyState(ADDITIVE_TRANSPARENCY)
            .setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, false))
            .setShaderState(new ShaderStateShard(Registry::getGlowingSpriteShader))
            .createCompositeState(false)
    ), GLOWING = RenderType.create(
        Eidolon.MODID + ":glowing",
        DefaultVertexFormat.POSITION_COLOR,
        Mode.QUADS, 256, true, false,
        RenderType.CompositeState.builder()
            .setWriteMaskState(new RenderStateShard.WriteMaskStateShard(true, false))
            .setLightmapState(new RenderStateShard.LightmapStateShard(false))
            .setTransparencyState(ADDITIVE_TRANSPARENCY)
            .setShaderState(new ShaderStateShard(Registry::getGlowingShader))
            .createCompositeState(false)
    ), DELAYED_PARTICLE = RenderType.create(
        Eidolon.MODID + ":delayed_particle",
        DefaultVertexFormat.PARTICLE,
        Mode.QUADS, 256, true, false,
        RenderType.CompositeState.builder()
            .setWriteMaskState(new RenderStateShard.WriteMaskStateShard(true, false))
            .setTransparencyState(NORMAL_TRANSPARENCY)
            .setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_PARTICLES, false, false))
            .setShaderState(new ShaderStateShard(GameRenderer::getParticleShader))
            .createCompositeState(false)
    ), GLOWING_PARTICLE = RenderType.create(
        Eidolon.MODID + ":glowing_particle",
        DefaultVertexFormat.PARTICLE,
        Mode.QUADS, 256, true, false,
        RenderType.CompositeState.builder()
            .setWriteMaskState(new RenderStateShard.WriteMaskStateShard(true, false))
            .setLightmapState(new RenderStateShard.LightmapStateShard(false))
            .setTransparencyState(ADDITIVE_TRANSPARENCY)
            .setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_PARTICLES, false, false))
            .setShaderState(new ShaderStateShard(Registry::getGlowingParticleShader))
            .createCompositeState(false)
    ), GLOWING_BLOCK_PARTICLE = RenderType.create(
        Eidolon.MODID + ":glowing_particle",
        DefaultVertexFormat.PARTICLE,
        Mode.QUADS, 256, true, false,
        RenderType.CompositeState.builder()
            .setWriteMaskState(new RenderStateShard.WriteMaskStateShard(true, false))
            .setLightmapState(new RenderStateShard.LightmapStateShard(false))
            .setTransparencyState(ADDITIVE_TRANSPARENCY)
            .setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, false))
            .setShaderState(new ShaderStateShard(Registry::getGlowingParticleShader))
            .createCompositeState(false)
    ), VAPOR_TRANSLUCENT = RenderType.create(
        Eidolon.MODID + ":vapor_translucent",
        DefaultVertexFormat.BLOCK,
        Mode.QUADS, 256, true, false,
        RenderType.CompositeState.builder()
            .setLightmapState(new RenderStateShard.LightmapStateShard(false))
            .setTransparencyState(NORMAL_TRANSPARENCY)
            .setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, false))
            .setShaderState(new ShaderStateShard(Registry::getVaporShader))
            .createCompositeState(false)
    );

    static double ticks = 0;

    public static void litQuad(PoseStack mStack, MultiBufferSource buffer, double x, double y, double w, double h, float r, float g, float b, TextureAtlasSprite sprite) {
        VertexConsumer builder = buffer.getBuffer(GLOWING_SPRITE);

        float f7 = sprite.getU0();
        float f8 = sprite.getU1();
        float f5 = sprite.getV0();
        float f6 = sprite.getV1();
        Matrix4f mat = mStack.last().pose();
        builder.vertex(mat, (float)x, (float)y + (float)h, 0).uv(f7, f6).color(r, g, b, 1.0f).endVertex();
        builder.vertex(mat, (float)x + (float)w, (float)y + (float)h, 0).uv(f8, f6).color(r, g, b, 1.0f).endVertex();
        builder.vertex(mat, (float)x + (float)w, (float)y, 0).uv(f8, f5).color(r, g, b, 1.0f).endVertex();
        builder.vertex(mat, (float)x, (float)y, 0).uv(f7, f5).color(r, g, b, 1.0f).endVertex();
    }

    public static void litQuad(PoseStack mStack, MultiBufferSource buffer, double x, double y, double w, double h, float r, float g, float b, float u, float v, float uw, float vh) {
        VertexConsumer builder = buffer.getBuffer(GLOWING_SPRITE);

        Matrix4f mat = mStack.last().pose();
        builder.vertex(mat, (float)x, (float)y + (float)h, 0).uv(u, v + vh).color(r, g, b, 1.0f).endVertex();
        builder.vertex(mat, (float)x + (float)w, (float)y + (float)h, 0).uv(u + uw, v + vh).color(r, g, b, 1.0f).endVertex();
        builder.vertex(mat, (float)x + (float)w, (float)y, 0).uv(u + uw, v).color(r, g, b, 1.0f).endVertex();
        builder.vertex(mat, (float)x, (float)y, 0).uv(u, v).color(r, g, b, 1.0f).endVertex();
    }

    public static void litBillboard(PoseStack mStack, MultiBufferSource buffer, double x, double y, double z, float r, float g, float b, TextureAtlasSprite sprite) {
        VertexConsumer builder = buffer.getBuffer(GLOWING_SPRITE);
        Camera renderInfo = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 vector3d = renderInfo.getPosition();
        float partialTicks = Minecraft.getInstance().getFrameTime();
        float f = (float)(x);
        float f1 = (float)(y);
        float f2 = (float)(z);
        Quaternion quaternion = renderInfo.rotation();

        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-0.5f, -0.5f, 0.0f), new Vector3f(-0.5f, 0.5f, 0.0f), new Vector3f(0.5f, 0.5f, 0.0f), new Vector3f(0.5f, -0.5f, 0.0f)};
        float f4 = 1.0f;

        for(int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.transform(quaternion);
            vector3f.mul(f4);
            vector3f.add(f, f1, f2);
        }

        float f7 = sprite.getU0();
        float f8 = sprite.getU1();
        float f5 = sprite.getV0();
        float f6 = sprite.getV1();
        Matrix4f mat = mStack.last().pose();
        builder.vertex(mat, avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).uv(f8, f6).color(r, g, b, 1.0f).endVertex();
        builder.vertex(mat, avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).uv(f8, f5).color(r, g, b, 1.0f).endVertex();
        builder.vertex(mat, avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).uv(f7, f5).color(r, g, b, 1.0f).endVertex();
        builder.vertex(mat, avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).uv(f7, f6).color(r, g, b, 1.0f).endVertex();
    }

    public static void dragon(PoseStack mStack, MultiBufferSource buf, double x, double y, double z, float radius, float r, float g, float b) {
        float f5 = 0.5f; // max number of beams
        float f7 = Math.min(f5 > 0.8F ? (f5 - 0.8F) / 0.2F : 0.0F, 1.0F);
        Random random = new Random(432L);
        VertexConsumer builder = buf.getBuffer(GLOWING);
        mStack.pushPose();
        mStack.translate(x, y, z);

        float rotation = (float)(ClientEvents.getClientTicks() / 200);

        for(int i = 0; (float)i < (f5 + f5 * f5) / 2.0F * 60.0F; ++i) {
            mStack.mulPose(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0F));
            mStack.mulPose(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0F));
            mStack.mulPose(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0F));
            mStack.mulPose(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0F));
            mStack.mulPose(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0F));
            mStack.mulPose(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0F + rotation * 90.0F));
            float f3 = random.nextFloat() * 20.0F + 5.0F + f7 * 10.0F;
            float f4 = random.nextFloat() * 2.0F + 1.0F + f7 * 2.0F;
            f3 *= 0.05f * radius;
            f4 *= 0.05f * radius;
            Matrix4f mat = mStack.last().pose();
            float alpha = 1 - f7;

            builder.vertex(mat, 0.0F, 0.0F, 0.0F).color(r, g, b, alpha).endVertex();
            builder.vertex(mat, 0.0F, 0.0F, 0.0F).color(r, g, b, alpha).endVertex();
            builder.vertex(mat, -ROOT_3 * f4, f3, -0.5F * f4).color(r, g, b, 0).endVertex();
            builder.vertex(mat, ROOT_3 * f4, f3, -0.5F * f4).color(r, g, b, 0).endVertex();
            builder.vertex(mat, 0.0F, 0.0F, 0.0F).color(r, g, b, alpha).endVertex();
            builder.vertex(mat, 0.0F, 0.0F, 0.0F).color(r, g, b, alpha).endVertex();
            builder.vertex(mat, ROOT_3 * f4, f3, -0.5F * f4).color(r, g, b, 0).endVertex();
            builder.vertex(mat, 0.0F, f3, 1.0F * f4).color(r, g, b, 0).endVertex();
            builder.vertex(mat, 0.0F, 0.0F, 0.0F).color(r, g, b, alpha).endVertex();
            builder.vertex(mat, 0.0F, 0.0F, 0.0F).color(r, g, b, alpha).endVertex();
            builder.vertex(mat, 0.0F, f3, 1.0F * f4).color(r, g, b, 0).endVertex();
            builder.vertex(mat, -ROOT_3 * f4, f3, -0.5F * f4).color(r, g, b, 0).endVertex();
        }

        mStack.popPose();
    }

    // copied from EnderDragonRenderer

    private static final float ROOT_3 = (float)(Math.sqrt(3.0D) / 2.0D);

    public static void vaporCube(PoseStack mStack, VertexConsumer builder, TextureAtlasSprite sprite,
                                 float x1, float y1, float z1, float x2, float y2, float z2,
                                 int r, int g, int b, int a, int light,
                                 boolean nx, boolean px, boolean ny, boolean py, boolean nz, boolean pz) {
        Matrix4f mat = mStack.last().pose();
        if (py) {
            builder.vertex(mat, x1, y2, z1).color(r, g, b, a).uv(sprite.getU(z1 * 16), sprite.getV(x1 * 16)).uv2(light).normal(0, 1, 0).endVertex();
            builder.vertex(mat, x1, y2, z2).color(r, g, b, a).uv(sprite.getU(z2 * 16), sprite.getV(x1 * 16)).uv2(light).normal(0, 1, 0).endVertex();
            builder.vertex(mat, x2, y2, z2).color(r, g, b, a).uv(sprite.getU(z2 * 16), sprite.getV(x2 * 16)).uv2(light).normal(0, 1, 0).endVertex();
            builder.vertex(mat, x2, y2, z1).color(r, g, b, a).uv(sprite.getU(z1 * 16), sprite.getV(x2 * 16)).uv2(light).normal(0, 1, 0).endVertex();
        }
        if (ny) {
            builder.vertex(mat, x1, y1, z2).color(r, g, b, a).uv(sprite.getU(z1 * 16), sprite.getV(x1 * 16)).uv2(light).normal(0, -1, 0).endVertex();
            builder.vertex(mat, x1, y1, z1).color(r, g, b, a).uv(sprite.getU(z2 * 16), sprite.getV(x1 * 16)).uv2(light).normal(0, -1, 0).endVertex();
            builder.vertex(mat, x2, y1, z1).color(r, g, b, a).uv(sprite.getU(z2 * 16), sprite.getV(x2 * 16)).uv2(light).normal(0, -1, 0).endVertex();
            builder.vertex(mat, x2, y1, z2).color(r, g, b, a).uv(sprite.getU(z1 * 16), sprite.getV(x2 * 16)).uv2(light).normal(0, -1, 0).endVertex();
        }
        if (nz) {
            builder.vertex(mat, x2, y1, z1).color(r, g, b, a).uv(sprite.getU(x1 * 16), sprite.getV(y1 * 16)).uv2(light).normal(0, 0, -1).endVertex();
            builder.vertex(mat, x1, y1, z1).color(r, g, b, a).uv(sprite.getU(x2 * 16), sprite.getV(y1 * 16)).uv2(light).normal(0, 0, -1).endVertex();
            builder.vertex(mat, x1, y2, z1).color(r, g, b, a).uv(sprite.getU(x2 * 16), sprite.getV(y2 * 16)).uv2(light).normal(0, 0, -1).endVertex();
            builder.vertex(mat, x2, y2, z1).color(r, g, b, a).uv(sprite.getU(x1 * 16), sprite.getV(y2 * 16)).uv2(light).normal(0, 0, -1).endVertex();
        }
        if (pz) {
            builder.vertex(mat, x1, y1, z2).color(r, g, b, a).uv(sprite.getU(x1 * 16), sprite.getV(y1 * 16)).uv2(light).normal(0, 0, 1).endVertex();
            builder.vertex(mat, x2, y1, z2).color(r, g, b, a).uv(sprite.getU(x2 * 16), sprite.getV(y1 * 16)).uv2(light).normal(0, 0, 1).endVertex();
            builder.vertex(mat, x2, y2, z2).color(r, g, b, a).uv(sprite.getU(x2 * 16), sprite.getV(y2 * 16)).uv2(light).normal(0, 0, 1).endVertex();
            builder.vertex(mat, x1, y2, z2).color(r, g, b, a).uv(sprite.getU(x1 * 16), sprite.getV(y2 * 16)).uv2(light).normal(0, 0, 1).endVertex();
        }
        if (nx) {
            builder.vertex(mat, x1, y1, z1).color(r, g, b, a).uv(sprite.getU(z1 * 16), sprite.getV(y1 * 16)).uv2(light).normal(-1, 0, 0).endVertex();
            builder.vertex(mat, x1, y1, z2).color(r, g, b, a).uv(sprite.getU(z2 * 16), sprite.getV(y1 * 16)).uv2(light).normal(-1, 0, 0).endVertex();
            builder.vertex(mat, x1, y2, z2).color(r, g, b, a).uv(sprite.getU(z2 * 16), sprite.getV(y2 * 16)).uv2(light).normal(-1, 0, 0).endVertex();
            builder.vertex(mat, x1, y2, z1).color(r, g, b, a).uv(sprite.getU(z1 * 16), sprite.getV(y2 * 16)).uv2(light).normal(-1, 0, 0).endVertex();
        }
        if (px) {
            builder.vertex(mat, x2, y1, z2).color(r, g, b, a).uv(sprite.getU(z1 * 16), sprite.getV(y1 * 16)).uv2(light).normal(1, 0, 0).endVertex();
            builder.vertex(mat, x2, y1, z1).color(r, g, b, a).uv(sprite.getU(z2 * 16), sprite.getV(y1 * 16)).uv2(light).normal(1, 0, 0).endVertex();
            builder.vertex(mat, x2, y2, z1).color(r, g, b, a).uv(sprite.getU(z2 * 16), sprite.getV(y2 * 16)).uv2(light).normal(1, 0, 0).endVertex();
            builder.vertex(mat, x2, y2, z2).color(r, g, b, a).uv(sprite.getU(z1 * 16), sprite.getV(y2 * 16)).uv2(light).normal(1, 0, 0).endVertex();
        }
    }
}
