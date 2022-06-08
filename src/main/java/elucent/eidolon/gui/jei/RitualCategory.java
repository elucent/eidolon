package elucent.eidolon.gui.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.codex.CodexGui;
import elucent.eidolon.ritual.IRequirement;
import elucent.eidolon.ritual.ItemRequirement;
import elucent.eidolon.ritual.MultiItemSacrifice;
import elucent.eidolon.ritual.RitualRegistry;
import elucent.eidolon.util.StackUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class RitualCategory implements IRecipeCategory<RecipeWrappers.RitualRecipe> {
    static final ResourceLocation UID = new ResourceLocation(Eidolon.MODID, "ritual");
    private final IDrawable background, icon;

    public RitualCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(new ResourceLocation(Eidolon.MODID, "textures/gui/jei_page_bg.png"), 0, 0, 138, 172);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(Registry.BRAZIER.get()));
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class getRecipeClass() {
        return RecipeWrappers.RitualRecipe.class;
    }

    @Override
    public String getTitle() {
        return I18n.get("jei." + Eidolon.MODID + ".ritual");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(RecipeWrappers.RitualRecipe wrapper, IIngredients ingredients) {
        List<Ingredient> inputs = new ArrayList<>();
        Object sacrifice = wrapper.sacrifice;
        if (wrapper.page == null) wrapper.page = RitualRegistry.getDefaultPage(wrapper.ritual, wrapper.sacrifice);
        int slot = 0;
        for (IRequirement r : wrapper.ritual.getRequirements()) {
            if (r instanceof ItemRequirement)
                inputs.add(StackUtil.ingredientFromObject(((ItemRequirement)r).getMatch()));
            slot ++;
        }
        inputs.add(StackUtil.ingredientFromObject(sacrifice instanceof MultiItemSacrifice ? ((MultiItemSacrifice)sacrifice).main : sacrifice));
        ingredients.setInputIngredients(inputs);
    }

    @Override
    public void setRecipe(IRecipeLayout layout, RecipeWrappers.RitualRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup stacks = layout.getItemStacks();
        List<List<ItemStack>> items = ingredients.getInputs(VanillaTypes.ITEM);

        float angleStep = Math.min(30, 180 / (items.size() - 1));
        double rootAngle = 90 - (items.size() - 2) * angleStep / 2;
        for (int i = 0; i < items.size() - 1; i ++) {
            double a = Math.toRadians(rootAngle + angleStep * i);
            int dx = (int)(68 + 48 * Math.cos(a));
            int dy = (int)(91 + 48 * Math.sin(a));
            stacks.init(i, true, dx - 8, dy - 8);
        }
        stacks.init(items.size() - 1, true, 60, 83);

        stacks.set(ingredients);
    }

    @Override
    public void draw(RecipeWrappers.RitualRecipe recipe, PoseStack mStack, double mouseX, double mouseY) {
        recipe.page.renderBackground(CodexGui.DUMMY, mStack, 5, 4, (int)mouseX, (int)mouseY);
        recipe.page.render(CodexGui.DUMMY, mStack, 5, 4, (int)mouseX, (int)mouseY);
    }
}
