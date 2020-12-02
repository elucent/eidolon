package elucent.eidolon.codex;

import com.mojang.blaze3d.matrix.MatrixStack;
import elucent.eidolon.Eidolon;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class TextPage extends Page {
    public static final ResourceLocation BACKGROUND = new ResourceLocation(Eidolon.MODID, "textures/gui/codex_blank_page.png");
    String text;

    public TextPage(String textKey) {
        super(BACKGROUND);
        this.text = textKey;
    }

    @Override
    public void render(CodexGui gui, MatrixStack mStack, int x, int y, int mouseX, int mouseY) {
        drawWrappingText(gui, mStack, I18n.format(text), x + 4, y + 4, 120);
    }
}
