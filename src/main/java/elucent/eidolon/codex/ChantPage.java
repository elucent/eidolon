package elucent.eidolon.codex;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import elucent.eidolon.ClientEvents;
import elucent.eidolon.Eidolon;
import elucent.eidolon.capability.IKnowledge;
import elucent.eidolon.capability.KnowledgeProvider;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.util.ColorUtil;
import elucent.eidolon.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

public class ChantPage extends Page {
    public static final ResourceLocation BACKGROUND = new ResourceLocation(Eidolon.MODID, "textures/gui/codex_chant_page.png");
    Sign[] chant;
    String text, title;

    public ChantPage(String textKey, Sign... chant) {
        super(BACKGROUND);
        this.text = textKey;
        this.title = textKey + ".title";
        this.chant = chant;
    }

    @OnlyIn(Dist.CLIENT)
    static void colorBlit(MatrixStack mStack, int x, int y, int uOffset, int vOffset, int width, int height, int textureWidth, int textureHeight, int color) {
        Matrix4f matrix = mStack.getLast().getMatrix();
        int maxX = x + width, maxY = y + height;
        float minU = (float)uOffset / textureWidth, minV = (float)vOffset / textureHeight;
        float maxU = minU + (float)width / textureWidth, maxV = minV + (float)height / textureHeight;
        int r = ColorUtil.getRed(color),
            g = ColorUtil.getGreen(color),
            b = ColorUtil.getBlue(color);
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(matrix, (float)x, (float)maxY, 0).tex(minU, maxV).color(r, g, b, 255).endVertex();
        bufferbuilder.pos(matrix, (float)maxX, (float)maxY, 0).tex(maxU, maxV).color(r, g, b, 255).endVertex();
        bufferbuilder.pos(matrix, (float)maxX, (float)y, 0).tex(maxU, minV).color(r, g, b, 255).endVertex();
        bufferbuilder.pos(matrix, (float)x, (float)y, 0).tex(minU, minV).color(r, g, b, 255).endVertex();
        bufferbuilder.finishDrawing();
        RenderSystem.enableAlphaTest();
        WorldVertexBufferUploader.draw(bufferbuilder);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(CodexGui gui, MatrixStack mStack, int x, int y, int mouseX, int mouseY) {
        String title = I18n.format(this.title);
        int titleWidth = Minecraft.getInstance().fontRenderer.getStringWidth(title);
        drawText(gui, mStack, title, x + 64 - titleWidth / 2, y + 15 - Minecraft.getInstance().fontRenderer.FONT_HEIGHT);

        Minecraft.getInstance().getTextureManager().bindTexture(CodexGui.CODEX_BACKGROUND);
        PlayerEntity entity = Minecraft.getInstance().player;
        IKnowledge knowledge = entity.getCapability(KnowledgeProvider.CAPABILITY, null).resolve().get();
        int w = chant.length * 24;
        int baseX = x + 64 - w / 2;
        gui.blit(mStack, baseX - 16, y + 28, 256, 208, 16, 32, 512, 512);
        for (int i = 0; i < chant.length; i ++) {
            gui.blit(mStack, baseX + i * 24, y + 28, 272, 208, 24, 32, 512, 512);
        }
        gui.blit(mStack, baseX + w, y + 28, 296, 208, 16, 32, 512, 512);

        Tessellator tess = Tessellator.getInstance();
        RenderSystem.enableBlend();
        RenderSystem.alphaFunc(GL11.GL_GEQUAL, 1f / 256f);
        for (int i = 0; i < chant.length; i ++) {
            Minecraft.getInstance().getTextureManager().bindTexture(CodexGui.CODEX_BACKGROUND);
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            gui.blit(mStack, baseX + i * 24, y + 28, 312, 208, 24, 24, 512, 512);

            Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
            Sign sign = chant[i];
            float flicker = 0.875f + 0.125f * (float)Math.sin(Math.toRadians(12 * ClientEvents.getClientTicks()));
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            RenderUtil.litQuad(mStack, IRenderTypeBuffer.getImpl(tess.getBuffer()), baseX + i * 24 + 4, y + 32, 16, 16,
                sign.getRed(), sign.getGreen(), sign.getBlue(), Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(sign.getSprite()));
            tess.draw();
            RenderUtil.litQuad(mStack, IRenderTypeBuffer.getImpl(tess.getBuffer()), baseX + i * 24 + 4, y + 32, 16, 16,
                sign.getRed() * flicker, sign.getGreen() * flicker, sign.getBlue() * flicker, Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(sign.getSprite()));
            tess.draw();
        }
        RenderSystem.defaultAlphaFunc();
        RenderSystem.disableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        drawWrappingText(gui, mStack, I18n.format(text), x + 4, y + 72, 120);
    }
}
