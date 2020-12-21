package elucent.eidolon.codex;

import com.mojang.blaze3d.matrix.MatrixStack;
import elucent.eidolon.Eidolon;
import elucent.eidolon.recipe.CrucibleRecipe;
import elucent.eidolon.recipe.CrucibleRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CruciblePage extends Page {
    public static final ResourceLocation BACKGROUND = new ResourceLocation(Eidolon.MODID, "textures/gui/codex_crucible_page.png");
    ItemStack result;
    CrucibleStep[] steps;

    public static class CrucibleStep {
        ItemStack[] stacks;
        int stirs;

        public CrucibleStep(int stirs) {
            this(stirs, ItemStack.EMPTY);
        }

        public CrucibleStep(ItemStack... stacks) {
            this(0, stacks);
        }

        public CrucibleStep(int stirs, ItemStack... stacks) {
            this.stacks = stacks;
            this.stirs = stirs;
        }
    }

    public CruciblePage(ItemStack result, CrucibleStep... steps) {
        super(BACKGROUND);
        this.result = result;
        this.steps = steps;
    }

    @Override
    public void render(CodexGui gui, MatrixStack mStack, int x, int y, int mouseX, int mouseY) {
        int h = steps.length * 20 + 32;
        int yoff = 80 - h / 2;
        for (int i = 0; i < steps.length; i ++) {
            int tx = x, ty = y + yoff + i * 20;
            gui.blit(mStack, tx, ty, 128, 0, 128, 20);
            tx += 24;
            for (int j = 0; j < steps[i].stacks.length; j ++) {
                if (!steps[i].stacks[j].isEmpty()) {
                    gui.blit(mStack, tx, ty + 1, 176, 32, 16, 17);
                    tx += 17;
                }
            }
            for (int j = 0; j < steps[i].stirs; j ++) {
                gui.blit(mStack, tx, ty + 1, 192, 32, 16, 17);
                tx += 17;
            }
        }
        gui.blit(mStack, x, y + yoff + steps.length * 20, 128, 64, 128, 32);

        FontRenderer font = Minecraft.getInstance().fontRenderer;
        for (int i = 0; i < steps.length; i ++) {
            int tx = x, ty = y + yoff + i * 20;
            drawText(gui, mStack, I18n.format("enchantment.level." + (i + 1)) + ".", tx + 7, ty + 17 - font.FONT_HEIGHT);
            tx += 24;
        }
    }


    @Override
    public void renderIngredients(CodexGui gui, MatrixStack mStack, int x, int y, int mouseX, int mouseY) {
        int h = steps.length * 20 + 32;
        int yoff = 80 - h / 2;
        for (int i = 0; i < steps.length; i ++) {
            int tx = x, ty = y + yoff + i * 20;
            tx += 24;
            for (int j = 0; j < steps[i].stacks.length; j ++) {
                if (!steps[i].stacks[j].isEmpty()) {
                    drawItem(gui, mStack, steps[i].stacks[j], tx, ty + 1, mouseX, mouseY);
                    tx += 17;
                }
            }
        }
        drawItem(gui, mStack, result,x + 56, y + yoff + steps.length * 20 + 11, mouseX, mouseY);
    }

    public CruciblePage linkRecipe(String modid, String recipe) {
        return linkRecipe(new ResourceLocation(modid, recipe));
    }

    public CruciblePage linkRecipe(ResourceLocation recipe) {
        CrucibleRegistry.linkPage(recipe, this);
        return this;
    }
}
