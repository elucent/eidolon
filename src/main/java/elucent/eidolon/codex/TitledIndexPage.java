package elucent.eidolon.codex;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class TitledIndexPage extends IndexPage {
    String title;

    public TitledIndexPage(String textKey, IndexEntry... pages) {
        super(pages);
        this.title = textKey + ".title";
    }

    @Override
    public boolean click(CodexGui gui, int x, int y, int mouseX, int mouseY) {
        return super.click(gui, x, y + 16, mouseX, mouseY);
    }

    @Override
    public void render(CodexGui gui, MatrixStack mStack, int x, int y, int mouseX, int mouseY) {
        gui.blit(mStack, x, y, 128, 64, 128, 32);
        String title = I18n.format(this.title);
        int titleWidth = Minecraft.getInstance().fontRenderer.getStringWidth(title);
        drawText(gui, mStack, title, x + 64 - titleWidth / 2, y + 15 - Minecraft.getInstance().fontRenderer.FONT_HEIGHT);

        Minecraft.getInstance().getTextureManager().bindTexture(BACKGROUND);
        super.render(gui, mStack, x, y + 16, mouseX, mouseY);
    }
}
