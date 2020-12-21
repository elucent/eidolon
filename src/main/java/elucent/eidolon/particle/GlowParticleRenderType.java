package elucent.eidolon.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import elucent.eidolon.ClientEvents;
import elucent.eidolon.Events;
import elucent.eidolon.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class GlowParticleRenderType implements IParticleRenderType {
    public static final GlowParticleRenderType INSTANCE = new GlowParticleRenderType();

    private static void beginRenderCommon(BufferBuilder bufferBuilder, TextureManager textureManager) {
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        RenderSystem.disableAlphaTest();
        RenderSystem.disableLighting();

        textureManager.bindTexture(AtlasTexture.LOCATION_PARTICLES_TEXTURE);
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
    }

    private static void endRenderCommon() {
        Minecraft.getInstance().textureManager.getTexture(AtlasTexture.LOCATION_PARTICLES_TEXTURE).restoreLastBlurMipmap();
        RenderSystem.enableAlphaTest();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
    }

    @Override
    public void beginRender(BufferBuilder b, TextureManager tex) {
        beginRenderCommon(b, tex);
    }

    @Override
    public void finishRender(Tessellator t) {
        t.draw();
        ClientEvents.getDelayedRender().getBuffer(RenderUtil.GLOWING_PARTICLE);
        RenderSystem.enableDepthTest();
        endRenderCommon();
    }
}
