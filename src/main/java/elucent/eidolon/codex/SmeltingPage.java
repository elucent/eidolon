package elucent.eidolon.codex;

import com.mojang.blaze3d.matrix.MatrixStack;
import elucent.eidolon.Eidolon;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class SmeltingPage extends Page {
    public static final ResourceLocation BACKGROUND = new ResourceLocation(Eidolon.MODID, "textures/gui/codex_smelting_page.png");
    ItemStack result;
    ItemStack input;

    public SmeltingPage(ItemStack result, ItemStack input) {
        super(BACKGROUND);
        this.result = result;
        this.input = input;
    }

    @Override
    public void renderIngredients(CodexGui gui, MatrixStack mStack, int x, int y, int mouseX, int mouseY) {
        drawItem(gui, mStack, input, x + 56, y + 34, mouseX, mouseY);
        drawItem(gui, mStack, result,x + 56, y + 107, mouseX, mouseY);
    }
}
