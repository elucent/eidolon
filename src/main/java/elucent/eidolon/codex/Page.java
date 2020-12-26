package elucent.eidolon.codex;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import elucent.eidolon.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class Page {
    ResourceLocation bg;

    public Page(ResourceLocation background) {
        this.bg = background;
    }

    public void reset() {
        //
    }

    public static void drawItem(CodexGui gui, MatrixStack mStack, ItemStack stack, int x, int y, int mouseX, int mouseY) {
        ItemRenderer ir = Minecraft.getInstance().getItemRenderer();
        ir.renderItemAndEffectIntoGUI(stack, x, y);
        ir.renderItemOverlayIntoGUI(Minecraft.getInstance().fontRenderer, stack, x, y, null);
        if (mouseX >= x && mouseY >= y && mouseX <= x + 16 && mouseY <= y + 16) {
            gui.renderTooltip(mStack, stack, mouseX, mouseY);
        }
    }

    public static void drawText(CodexGui gui, MatrixStack mStack, String text, int x, int y) {
        FontRenderer font = Minecraft.getInstance().fontRenderer;
        font.drawString(mStack, text, x, y - 1, ColorUtil.packColor(128, 255, 255, 255));
        font.drawString(mStack, text, x - 1, y, ColorUtil.packColor(128, 219, 212, 184));
        font.drawString(mStack, text, x + 1, y, ColorUtil.packColor(128, 219, 212, 184));
        font.drawString(mStack, text, x, y + 1, ColorUtil.packColor(128, 191, 179, 138));
        font.drawString(mStack, text, x, y, ColorUtil.packColor(255, 79, 59, 47));
    }

    public static void drawWrappingText(CodexGui gui, MatrixStack mStack, String text, int x, int y, int w) {
        FontRenderer font = Minecraft.getInstance().fontRenderer;
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        String line = "";
        for (String s : words) {
            if (font.getStringWidth(line) + font.getStringWidth(s) > w) {
                lines.add(line);
                line = s + " ";
            }
            else line += s + " ";
        }
        if (!line.isEmpty()) lines.add(line);
        for (int i = 0; i < lines.size(); i ++) {
            drawText(gui, mStack, lines.get(i), x, y + i * (font.FONT_HEIGHT + 1));
        }
    }

    public void fullRender(CodexGui gui, MatrixStack mStack, int x, int y, int mouseX, int mouseY) {
        Minecraft.getInstance().getTextureManager().bindTexture(bg);
        renderBackground(gui, mStack, x, y, mouseX, mouseY);
        render(gui, mStack, x, y, mouseX, mouseY);
        renderIngredients(gui, mStack, x, y, mouseX, mouseY);
    }

    public void renderBackground(CodexGui gui, MatrixStack mStack, int x, int y, int mouseX, int mouseY) {
        Minecraft.getInstance().getTextureManager().bindTexture(bg);
        gui.blit(mStack, x, y, 0, 0, 128, 160);
    }

    public boolean click(CodexGui gui, int x, int y, int mouseX, int mouseY) {
        return false;
    }

    public void render(CodexGui gui, MatrixStack mStack, int x, int y, int mouseX, int mouseY) {}
    public void renderIngredients(CodexGui gui, MatrixStack mStack, int x, int y, int mouseX, int mouseY) {}
}
