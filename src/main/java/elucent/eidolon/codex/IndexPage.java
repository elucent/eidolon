package elucent.eidolon.codex;

import com.mojang.blaze3d.matrix.MatrixStack;
import elucent.eidolon.Eidolon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

public class IndexPage extends Page {
    public static final ResourceLocation BACKGROUND = new ResourceLocation(Eidolon.MODID, "textures/gui/codex_index_page.png");
    IndexEntry[] entries;

    public static class IndexEntry {
        Chapter chapter;
        ItemStack icon;

        public IndexEntry(Chapter chapter, ItemStack icon) {
            this.chapter = chapter;
            this.icon = icon;
        }
    }

    public IndexPage(IndexEntry... pages) {
        super(BACKGROUND);
        this.entries = pages;
    }

    @Override
    public boolean click(CodexGui gui, int x, int y, int mouseX, int mouseY) {
        for (int i = 0; i < entries.length; i ++) {
            if (mouseX >= x + 2 && mouseX <= x + 124 && mouseY >= y + 8 + i * 20 && mouseY <= y + 26 + i * 20) {
                gui.changeChapter(entries[i].chapter);
                Minecraft.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK, SoundCategory.NEUTRAL, 1.0f, 1.0f);
                return true;
            }
        }
        return false;
    }

    @Override
    public void render(CodexGui gui, MatrixStack mStack, int x, int y, int mouseX, int mouseY) {
        Minecraft.getInstance().getTextureManager().bindTexture(BACKGROUND);
        for (int i = 0; i < entries.length; i ++) {
            gui.blit(mStack, x + 1, y + 7 + i * 20, 128, 0, 122, 18);
        }
        for (int i = 0; i < entries.length; i ++) {
            Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(entries[i].icon, x + 2, y + 8 + i * 20);
            drawText(gui, mStack, I18n.format(entries[i].chapter.titleKey), x + 24, y + 20 + i * 20 - Minecraft.getInstance().fontRenderer.FONT_HEIGHT);
        }
    }
}
