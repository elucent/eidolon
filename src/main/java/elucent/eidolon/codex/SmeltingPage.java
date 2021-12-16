package elucent.eidolon.codex;

import com.mojang.blaze3d.vertex.PoseStack;

import elucent.eidolon.Eidolon;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
    @OnlyIn(Dist.CLIENT)
    public void renderIngredients(CodexGui gui, PoseStack mStack, int x, int y, int mouseX, int mouseY) {
        drawItem(gui, mStack, input, x + 56, y + 34, mouseX, mouseY);
        drawItem(gui, mStack, result,x + 56, y + 107, mouseX, mouseY);
    }
}
