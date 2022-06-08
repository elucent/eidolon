package elucent.eidolon.codex;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Matrix4f;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.TranslatableComponent;

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

    static void colorBlit(PoseStack mStack, int x, int y, int uOffset, int vOffset, int width, int height, int textureWidth, int textureHeight, int color) {
        Matrix4f matrix = mStack.last().pose();
        int maxX = x + width, maxY = y + height;
        float minU = (float)uOffset / textureWidth, minV = (float)vOffset / textureHeight;
        float maxU = minU + (float)width / textureWidth, maxV = minV + (float)height / textureHeight;
        int r = ColorUtil.getRed(color),
            g = ColorUtil.getGreen(color),
            b = ColorUtil.getBlue(color);
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferbuilder.vertex(matrix, (float)x, (float)maxY, 0).uv(minU, maxV).color(r, g, b, 255).endVertex();
        bufferbuilder.vertex(matrix, (float)maxX, (float)maxY, 0).uv(maxU, maxV).color(r, g, b, 255).endVertex();
        bufferbuilder.vertex(matrix, (float)maxX, (float)y, 0).uv(maxU, minV).color(r, g, b, 255).endVertex();
        bufferbuilder.vertex(matrix, (float)x, (float)y, 0).uv(minU, minV).color(r, g, b, 255).endVertex();
        bufferbuilder.end();
        RenderSystem.enableAlphaTest();
        WorldVertexBufferUploader.end(bufferbuilder);
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

    public void draw(CodexGui gui, PoseStack mStack, int x, int y, boolean right, int mouseX, int mouseY) {
        int w = 36;
        if (!right) x -= 36;
        w += hoveramount * 12;
        if (!right) x -= hoveramount * 12;

        boolean hover = mouseX >= x && mouseY >= y && mouseX <= x + w && mouseY <= y + 19;
        if (hover && hoveramount < 1) hoveramount += Minecraft.getInstance().getFrameTime() / 4;
        else if (!hover && hoveramount > 0) hoveramount -= Minecraft.getInstance().getFrameTime() / 4;
        hoveramount = Mth.clamp(hoveramount, 0, 1);

        if (right) {
            x -= 12;
            x += hoveramount * 12;
        }

        Minecraft.getInstance().getTextureManager().bind(CodexGui.CODEX_BACKGROUND);
        colorBlit(mStack, x, y, 208, right ? 208 : 227, 48, 19, 512, 512, color);
        Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(icon, x + (right ? 23 : 9), y + 1);
    }

    public void drawTooltip(CodexGui gui, PoseStack mStack, int x, int y, boolean right, int mouseX, int mouseY) {
        int w = 36;
        if (!right) x -= 36;
        w += hoveramount * 12;
        if (!right) x -= hoveramount * 12;

        boolean hover = mouseX >= x && mouseY >= y && mouseX <= x + w && mouseY <= y + 19;
        if (hover) gui.renderTooltip(mStack, new TranslatableComponent("eidolon.codex.category." + key), mouseX, mouseY);
    }
}
