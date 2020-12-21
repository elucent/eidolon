package elucent.eidolon.codex;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Mirror;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.TranslationTextComponent;

public class Category {
    ItemStack icon;
    String key;
    Chapter chapter;
    int color;
    float hoveramount = 0;

    public Category(String name, ItemStack icon, int color, Chapter chapter) {
        this.icon = icon;
        this.key = name;
        this.chapter = chapter;
        this.color = color;
    }

    protected void reset() {
        hoveramount = 0;
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

    public boolean click(CodexGui gui, int x, int y, boolean right, int mouseX, int mouseY) {
        int w = 36;
        if (!right) x -= 36;
        w += hoveramount * 12;
        if (!right) x -= hoveramount * 12;

        boolean hover = mouseX >= x && mouseY >= y && mouseX <= x + w && mouseY <= y + 19;
        if (hover) {
            gui.currentPage = 0;
            gui.currentChapter = chapter;
            gui.resetPages();
            return true;
        }
        return false;
    }

    public void draw(CodexGui gui, MatrixStack mStack, int x, int y, boolean right, int mouseX, int mouseY) {
        int w = 36;
        if (!right) x -= 36;
        w += hoveramount * 12;
        if (!right) x -= hoveramount * 12;

        boolean hover = mouseX >= x && mouseY >= y && mouseX <= x + w && mouseY <= y + 19;
        if (hover && hoveramount < 1) hoveramount += Minecraft.getInstance().getRenderPartialTicks() / 4;
        else if (!hover && hoveramount > 0) hoveramount -= Minecraft.getInstance().getRenderPartialTicks() / 4;
        hoveramount = MathHelper.clamp(hoveramount, 0, 1);

        if (right) {
            x -= 12;
            x += hoveramount * 12;
        }

        Minecraft.getInstance().getTextureManager().bindTexture(CodexGui.CODEX_BACKGROUND);
        colorBlit(mStack, x, y, 208, right ? 208 : 227, 48, 19, 512, 512, color);
        Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(icon, x + (right ? 23 : 9), y + 1);
    }

    public void drawTooltip(CodexGui gui, MatrixStack mStack, int x, int y, boolean right, int mouseX, int mouseY) {
        int w = 36;
        if (!right) x -= 36;
        w += hoveramount * 12;
        if (!right) x -= hoveramount * 12;

        boolean hover = mouseX >= x && mouseY >= y && mouseX <= x + w && mouseY <= y + 19;
        if (hover) gui.renderTooltip(mStack, new TranslationTextComponent("eidolon.codex.category." + key), mouseX, mouseY);
    }
}
