package elucent.eidolon.codex;

import com.mojang.blaze3d.vertex.PoseStack;
import elucent.eidolon.Eidolon;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TextPage extends Page {
    public static final ResourceLocation BACKGROUND = new ResourceLocation(Eidolon.MODID, "textures/gui/codex_blank_page.png");
    String text;

    public TextPage(String textKey) {
        super(BACKGROUND);
        this.text = textKey;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(CodexGui gui, PoseStack mStack, int x, int y, int mouseX, int mouseY) {
        drawWrappingText(gui, mStack, I18n.get(text), x + 4, y + 4, 120);
    }
}
