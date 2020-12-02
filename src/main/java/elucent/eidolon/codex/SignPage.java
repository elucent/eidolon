package elucent.eidolon.codex;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import elucent.eidolon.Eidolon;
import elucent.eidolon.Events;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.util.ColorUtil;
import elucent.eidolon.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import org.lwjgl.opengl.GL11;

public class SignPage extends Page {
    public static final ResourceLocation BACKGROUND = new ResourceLocation(Eidolon.MODID, "textures/gui/codex_sign_page.png");
    Sign sign;

    public SignPage(Sign sign) {
        super(BACKGROUND);
        this.sign = sign;
    }

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
    public void render(CodexGui gui, MatrixStack mStack, int x, int y, int mouseX, int mouseY) {
        Minecraft.getInstance().getTextureManager().bindTexture(BACKGROUND);
        Tessellator tess = Tessellator.getInstance();
        RenderSystem.enableBlend();
        RenderSystem.alphaFunc(GL11.GL_GEQUAL, 1f / 256f);
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        mStack.push();
        mStack.translate(x + 64, y + 80, 0);
        // mStack.scale(0.9f, 0.9f, 0.9f);
        mStack.rotate(Vector3f.ZP.rotationDegrees(Events.getClientTicks() * 1.5f));
        colorBlit(mStack, -40, -40, 128, 96, 80, 80, 256, 256, sign.getColor());
        mStack.pop();
        Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        for (int i = 0; i < 2; i ++) {
            RenderUtil.litQuad(mStack, IRenderTypeBuffer.getImpl(tess.getBuffer()), x + 44, y + 60, 40, 40,
                sign.getRed(), sign.getGreen(), sign.getBlue(), Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(sign.getSprite()));
            tess.draw();
        }
        RenderSystem.defaultAlphaFunc();
        RenderSystem.disableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }
}
