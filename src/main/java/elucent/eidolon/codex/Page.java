package elucent.eidolon.codex;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public abstract class Page {
    ResourceLocation bg;

    public Page(ResourceLocation background) {
        this.bg = background;
    }

    public void reset() {
        //
    }

    @OnlyIn(Dist.CLIENT)
    public static void drawItem(CodexGui gui, PoseStack mStack, ItemStack stack, int x, int y, int mouseX, int mouseY) {
        ItemRenderer ir = Minecraft.getInstance().getItemRenderer();
        ir.renderAndDecorateItem(stack, x, y);
        ir.renderGuiItemDecorations(Minecraft.getInstance().font, stack, x, y, null);
        if (mouseX >= x && mouseY >= y && mouseX <= x + 16 && mouseY <= y + 16) {
            gui.renderTooltip(mStack, stack, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void drawText(CodexGui gui, PoseStack mStack, String text, int x, int y) {
        Font font = Minecraft.getInstance().font;
        font.draw(mStack, text, x, y - 1, ColorUtil.packColor(128, 255, 255, 255));
        font.draw(mStack, text, x - 1, y, ColorUtil.packColor(128, 219, 212, 184));
        font.draw(mStack, text, x + 1, y, ColorUtil.packColor(128, 219, 212, 184));
        font.draw(mStack, text, x, y + 1, ColorUtil.packColor(128, 191, 179, 138));
        font.draw(mStack, text, x, y, ColorUtil.packColor(255, 79, 59, 47));
    }

    @OnlyIn(Dist.CLIENT)
    public static void drawWrappingText(CodexGui gui, PoseStack mStack, String text, int x, int y, int w) {
        Font font = Minecraft.getInstance().font;
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        String line = "";
        for (String s : words) {
            if (font.width(line) + font.width(s) > w) {
                lines.add(line);
                line = s + " ";
            }
            else line += s + " ";
        }
        if (!line.isEmpty()) lines.add(line);
        for (int i = 0; i < lines.size(); i ++) {
            drawText(gui, mStack, lines.get(i), x, y + i * (font.lineHeight + 1));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void fullRender(CodexGui gui, PoseStack mStack, int x, int y, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, bg);
        renderBackground(gui, mStack, x, y, mouseX, mouseY);
        render(gui, mStack, x, y, mouseX, mouseY);
        renderIngredients(gui, mStack, x, y, mouseX, mouseY);
    }

    @OnlyIn(Dist.CLIENT)
    public void renderBackground(CodexGui gui, PoseStack mStack, int x, int y, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, bg);
        gui.blit(mStack, x, y, 0, 0, 128, 160);
    }

    @OnlyIn(Dist.CLIENT)
    public boolean click(CodexGui gui, int x, int y, int mouseX, int mouseY) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public void render(CodexGui gui, PoseStack mStack, int x, int y, int mouseX, int mouseY) {}

    @OnlyIn(Dist.CLIENT)
    public void renderIngredients(CodexGui gui, PoseStack mStack, int x, int y, int mouseX, int mouseY) {}
}
