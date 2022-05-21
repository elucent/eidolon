package elucent.eidolon.codex;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import elucent.eidolon.Eidolon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RuneDescPage extends Page {
    public static final ResourceLocation BLANK = new ResourceLocation(Eidolon.MODID, "textures/gui/codex_blank_page.png");
    public static final ResourceLocation BACKGROUND = new ResourceLocation(Eidolon.MODID, "textures/gui/codex_title_page.png");

    public RuneDescPage() {
        super(BACKGROUND);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(CodexGui gui, PoseStack mStack, int x, int y, int mouseX, int mouseY) {
    	if (gui.hoveredRune == null) {
    	}
    	else {
    		ResourceLocation rl = gui.hoveredRune.getRegistryName();
	        String title = I18n.get(rl.getNamespace() + ".rune." + rl.getPath());
	        String text = I18n.get(rl.getNamespace() + ".codex.rune." + rl.getPath());
	        int titleWidth = Minecraft.getInstance().font.width(title);
	        drawText(gui, mStack, title, x + 64 - titleWidth / 2, y + 15 - Minecraft.getInstance().font.lineHeight);
	        drawWrappingText(gui, mStack, text, x + 4, y + 24, 120);
    	}
    }
}
