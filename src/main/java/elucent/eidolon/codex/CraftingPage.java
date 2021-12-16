package elucent.eidolon.codex;

import com.mojang.blaze3d.vertex.PoseStack;

import elucent.eidolon.Eidolon;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CraftingPage extends Page {
    public static final ResourceLocation BACKGROUND = new ResourceLocation(Eidolon.MODID, "textures/gui/codex_crafting_page.png");
    ItemStack result;
    ItemStack[] inputs;

    public CraftingPage(ItemStack result, ItemStack... inputs) {
        super(BACKGROUND);
        this.result = result;
        this.inputs = inputs;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderIngredients(CodexGui gui, PoseStack mStack, int x, int y, int mouseX, int mouseY) {
        for (int i = 0; i < 3; i ++) {
            for (int j = 0; j < 3; j ++) {
                int index = i * 3 + j;
                if (index < inputs.length && !inputs[index].isEmpty())
                    drawItem(gui, mStack, inputs[index], x + 36 + j * 20, y + 36 + i * 20, mouseX, mouseY);
            }
        }
        drawItem(gui, mStack, result,x + 56, y + 112, mouseX, mouseY);
    }
}
