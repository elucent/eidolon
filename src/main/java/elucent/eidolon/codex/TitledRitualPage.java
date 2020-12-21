package elucent.eidolon.codex;

import com.mojang.blaze3d.matrix.MatrixStack;
import elucent.eidolon.ritual.Ritual;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class TitledRitualPage extends RitualPage {
    String title;

    public TitledRitualPage(String textKey, Ritual ritual, ItemStack center, RitualIngredient... inputs) {
        super(ritual, center, inputs);
        this.title = textKey + ".title";
    }

    @Override
    public void render(CodexGui gui, MatrixStack mStack, int x, int y, int mouseX, int mouseY) {
        Minecraft.getInstance().getTextureManager().bindTexture(BACKGROUND);
        gui.blit(mStack, x, y, 128, 64, 128, 24);
        String title = I18n.format(this.title);
        int titleWidth = Minecraft.getInstance().fontRenderer.getStringWidth(title);
        drawText(gui, mStack, title, x + 64 - titleWidth / 2, y + 15 - Minecraft.getInstance().fontRenderer.FONT_HEIGHT);

        Minecraft.getInstance().getTextureManager().bindTexture(BACKGROUND);
        super.render(gui, mStack, x, y, mouseX, mouseY);
    }
}
