package elucent.eidolon.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import elucent.eidolon.ClientEvents;
import elucent.eidolon.Eidolon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class RenderUtil extends RenderStateShard {
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

    public static RenderType GLOWING_SPRITE = RenderType.create(
        Eidolon.MODID + ":glowing_sprite",
        DefaultVertexFormat.POSITION_COLOR_TEX,
            VertexFormat.Mode.QUADS, 256,
        false, false,
        RenderType.CompositeState.builder()
            //.setShadeModelState(new RenderStateShard.ShadeModelState(false))
            .setWriteMaskState(new RenderStateShard.WriteMaskStateShard(true, false))
            .setLightmapState(new RenderStateShard.LightmapStateShard(false))
            //.setDiffuseLightingState(new RenderStateShard.DiffuseLightingState(false))
            .setTransparencyState(ADDITIVE_TRANSPARENCY)
                // TODO
                .setShaderState(RENDERTYPE_TRANSLUCENT_SHADER)
            .setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, false))
            .createCompositeState(false)
    ), GLOWING = RenderType.create(
        Eidolon.MODID + ":glowing",
        DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS, 256,
        false, false,
        RenderType.CompositeState.builder()
            //.setShadeModelState(new RenderStateShard.ShadeModelState(true))
            .setWriteMaskState(new RenderStateShard.WriteMaskStateShard(true, false))
            .setLightmapState(new RenderStateShard.LightmapStateShard(false))
            //.setDiffuseLightingState(new RenderStateShard.DiffuseLightingState(false))
            .setTransparencyState(ADDITIVE_TRANSPARENCY)
                // TODO
                .setShaderState(RENDERTYPE_TRANSLUCENT_SHADER)
            .createCompositeState(false)
    ), DELAYED_PARTICLE = RenderType.create(
        Eidolon.MODID + ":delayed_particle",
        DefaultVertexFormat.PARTICLE,
            VertexFormat.Mode.QUADS, 256,
        false, false,
        RenderType.CompositeState.builder()
            //.setShadeModelState(new RenderStateShard.ShadeModelState(true))
            .setWriteMaskState(new RenderStateShard.WriteMaskStateShard(true, false))
            .setLightmapState(new RenderStateShard.LightmapStateShard(false))
            //.setDiffuseLightingState(new RenderStateShard.DiffuseLightingState(false))
            .setTransparencyState(NORMAL_TRANSPARENCY)
            .setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_PARTICLES, false, false))
                // TODO
                .setShaderState(RENDERTYPE_TRANSLUCENT_SHADER)
            .createCompositeState(false)
    ), GLOWING_PARTICLE = RenderType.create(
        Eidolon.MODID + ":glowing_particle",
        DefaultVertexFormat.PARTICLE,
            VertexFormat.Mode.QUADS, 256,
        false, false,
        RenderType.CompositeState.builder()
            //.setShadeModelState(new RenderStateShard.ShadeModelState(true))
            .setWriteMaskState(new RenderStateShard.WriteMaskStateShard(true, false))
            .setLightmapState(new RenderStateShard.LightmapStateShard(false))
            //.setDiffuseLightingState(new RenderStateShard.DiffuseLightingState(false))
            .setTransparencyState(ADDITIVE_TRANSPARENCY)
            .setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_PARTICLES, false, false))
                // TODO
                .setShaderState(RENDERTYPE_TRANSLUCENT_SHADER)
            .createCompositeState(false)
    ), GLOWING_BLOCK_PARTICLE = RenderType.create(
        Eidolon.MODID + ":glowing_particle",
        DefaultVertexFormat.PARTICLE,
            VertexFormat.Mode.QUADS, 256,
        false, false,
        RenderType.CompositeState.builder()
            //.setShadeModelState(new RenderStateShard.ShadeModelState(true))
            .setWriteMaskState(new RenderStateShard.WriteMaskStateShard(true, false))
            .setLightmapState(new RenderStateShard.LightmapStateShard(false))
            //.setDiffuseLightingState(new RenderStateShard.DiffuseLightingState(false))
            .setTransparencyState(ADDITIVE_TRANSPARENCY)
            .setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, false))
                // TODO
                .setShaderState(RENDERTYPE_TRANSLUCENT_SHADER)
            .createCompositeState(false)
    );

    static double ticks = 0;

    public RenderUtil(String pName, Runnable pSetupState, Runnable pClearState) {
        super(pName, pSetupState, pClearState);
    }

    public static void litQuad(PoseStack mStack, MultiBufferSource buffer, double x, double y, double w, double h, float r, float g, float b, TextureAtlasSprite sprite) {
        VertexConsumer builder = buffer.getBuffer(GLOWING_SPRITE);

        float f7 = sprite.getU0();
        float f8 = sprite.getU1();
        float f5 = sprite.getV0();
        float f6 = sprite.getV1();
        Matrix4f mat = mStack.last().pose();
        builder.vertex(mat, (float)x, (float)y + (float)h, 0).color(r, g, b, 1.0f).uv(f7, f6).endVertex();
        builder.vertex(mat, (float)x + (float)w, (float)y + (float)h, 0).color(r, g, b, 1.0f).uv(f8, f6).endVertex();
        builder.vertex(mat, (float)x + (float)w, (float)y, 0).color(r, g, b, 1.0f).uv(f8, f5).endVertex();
        builder.vertex(mat, (float)x, (float)y, 0).color(r, g, b, 1.0f).uv(f7, f5).endVertex();
    }

    public static void litQuad(PoseStack mStack, MultiBufferSource buffer, double x, double y, double w, double h, float r, float g, float b, float u, float v, float uw, float vh) {
        VertexConsumer builder = buffer.getBuffer(GLOWING_SPRITE);

        Matrix4f mat = mStack.last().pose();
        builder.vertex(mat, (float)x, (float)y + (float)h, 0).color(r, g, b, 1.0f).uv(u, v + vh).endVertex();
        builder.vertex(mat, (float)x + (float)w, (float)y + (float)h, 0).color(r, g, b, 1.0f).uv(u + uw, v + vh).endVertex();
        builder.vertex(mat, (float)x + (float)w, (float)y, 0).color(r, g, b, 1.0f).uv(u + uw, v).endVertex();
        builder.vertex(mat, (float)x, (float)y, 0).color(r, g, b, 1.0f).uv(u, v).endVertex();
    }

    public static void litBillboard(PoseStack mStack, MultiBufferSource buffer, double x, double y, double z, float r, float g, float b, TextureAtlasSprite sprite) {
        VertexConsumer builder = buffer.getBuffer(GLOWING_SPRITE);
        var renderInfo = Minecraft.getInstance().gameRenderer.getMainCamera();
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
        builder.vertex(mat, avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).color(r, g, b, 1.0f).uv(f8, f6).endVertex();
        builder.vertex(mat, avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).color(r, g, b, 1.0f).uv(f8, f5).endVertex();
        builder.vertex(mat, avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).color(r, g, b, 1.0f).uv(f7, f5).endVertex();
        builder.vertex(mat, avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).color(r, g, b, 1.0f).uv(f7, f6).endVertex();
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
}
