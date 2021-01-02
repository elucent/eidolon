package elucent.eidolon.codex;

import com.mojang.blaze3d.matrix.MatrixStack;
import elucent.eidolon.Eidolon;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class WorktablePage extends Page {
    public static final ResourceLocation BACKGROUND = new ResourceLocation(Eidolon.MODID, "textures/gui/codex_worktable_page.png");
    ItemStack result;
    ItemStack[] inputs;

    public WorktablePage(ItemStack result, ItemStack... inputs) {
        super(BACKGROUND);
        this.result = result;
        this.inputs = inputs;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderIngredients(CodexGui gui, MatrixStack mStack, int x, int y, int mouseX, int mouseY) {
        for (int i = 0; i < 3; i ++) {
            for (int j = 0; j < 3; j ++) {
                int index = i * 3 + j;
                if (index < inputs.length && !inputs[index].isEmpty())
                    drawItem(gui, mStack, inputs[index], x + 39 + j * 17, y + 33 + i * 17, mouseX, mouseY);
            }
        }
        drawItem(gui, mStack, inputs[9], x + 56, y + 11, mouseX, mouseY);
        drawItem(gui, mStack, inputs[10], x + 95, y + 50, mouseX, mouseY);
        drawItem(gui, mStack, inputs[11], x + 56, y + 89, mouseX, mouseY);
        drawItem(gui, mStack, inputs[12], x + 17, y + 50, mouseX, mouseY);
        drawItem(gui, mStack, result,x + 56, y + 129, mouseX, mouseY);
    }
}
