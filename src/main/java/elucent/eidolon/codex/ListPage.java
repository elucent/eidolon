package elucent.eidolon.codex;

import com.mojang.blaze3d.matrix.MatrixStack;

import elucent.eidolon.Eidolon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ListPage extends Page {
    public static final ResourceLocation BACKGROUND = new ResourceLocation(Eidolon.MODID, "textures/gui/codex_index_page.png");
    ListEntry[] entries;

    public static class ListEntry {
        String key;
        ItemStack icon;

        public ListEntry(String key, ItemStack icon) {
            this.key = key;
            this.icon = icon;
        }
    }

    String key;

    public ListPage(String key, ListEntry... pages) {
        super(BACKGROUND);
        this.key = key;
        this.entries = pages;
    }

    @Override
    public void render(CodexGui gui, MatrixStack mStack, int x, int y, int mouseX, int mouseY) {
        Minecraft.getInstance().getTextureManager().bindTexture(BACKGROUND);
        for (int i = 0; i < entries.length; i ++) {
            gui.blit(mStack, x + 1, y + 7 + i * 20, 128, 0, 122, 18);
        }

        for (int i = 0; i < entries.length; i ++) {
            drawItem(gui, mStack, entries[i].icon, x + 2, y + 8 + i * 20, mouseX, mouseY);
            drawText(gui, mStack, I18n.format(key + "." + entries[i].key), x + 24, y + 20 + i * 20 - Minecraft.getInstance().fontRenderer.FONT_HEIGHT);
        }
    }
}
