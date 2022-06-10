package elucent.eidolon.gui.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.codex.CodexGui;
import elucent.eidolon.recipe.CrucibleRecipe;
import elucent.eidolon.recipe.CrucibleRegistry;
import elucent.eidolon.util.StackUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CrucibleCategory implements IRecipeCategory<RecipeWrappers.Crucible> {
    static final ResourceLocation UID = new ResourceLocation(Eidolon.MODID, "crucible");
    private final IDrawable background, icon;

    public CrucibleCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(new ResourceLocation(Eidolon.MODID, "textures/gui/jei_page_bg.png"), 0, 0, 138, 172);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(Registry.CRUCIBLE.get()));
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class getRecipeClass() {
        return RecipeWrappers.Crucible.class;
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("jei." + Eidolon.MODID + ".crucible");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    protected static class StackIngredient {
        ItemStack stack;
        Ingredient ingredient;

        public StackIngredient(ItemStack stack, Ingredient ingredient) {
            this.stack = stack;
            this.ingredient = ingredient;
        }
    };

    public static void condense(List<StackIngredient> stacks) {
        Iterator<StackIngredient> iter = stacks.iterator();
        StackIngredient last = new StackIngredient(ItemStack.EMPTY, Ingredient.EMPTY);
        while (iter.hasNext()) {
            StackIngredient i = iter.next();
            if (!ItemStack.isSame(i.stack, last.stack) || !ItemStack.tagMatches(i.stack, last.stack) || last.stack.getCount() + i.stack.getCount() > last.stack.getMaxStackSize()) {
                last = i;
            }
            else {
                last.stack.grow(i.stack.getCount());
                iter.remove();
            }
        }
    }

    @Override
    public void setIngredients(RecipeWrappers.Crucible wrapper, IIngredients ingredients) {
        if (wrapper.page == null) wrapper.page = CrucibleRegistry.getDefaultPage(wrapper.recipe);

        List<List<ItemStack>> inputs = new ArrayList<>();
        for (CrucibleRecipe.Step step : wrapper.recipe.getSteps()) {
            List<StackIngredient> stepInputs = new ArrayList<>();
            for (Object o : step.matches) {
                ItemStack stack = StackUtil.stackFromObject(o);
                if (!stack.isEmpty()) stepInputs.add(new StackIngredient(stack, StackUtil.ingredientFromObject(o)));
            }
            condense(stepInputs);
            for (StackIngredient i : stepInputs) {
                ItemStack[] valid = i.ingredient.getItems();
                for (ItemStack stack : valid) stack.setCount(i.stack.getCount());
                inputs.add(Arrays.asList(valid));
            }
        }
        ingredients.setInputLists(VanillaTypes.ITEM, inputs);
        ingredients.setOutput(VanillaTypes.ITEM, wrapper.recipe.getResult());
    }

    @Override
    public void setRecipe(IRecipeLayout layout, RecipeWrappers.Crucible recipe, IIngredients ingredients) {
        IGuiItemStackGroup stacks = layout.getItemStacks();

        List<CrucibleRecipe.Step> steps = recipe.recipe.getSteps();
        int h = steps.size() * 20 + 32;
        int yoff = 80 - h / 2;
        int slot = 0;
        for (int i = 0; i < steps.size(); i ++) {
            int tx = 4, ty = 3 + yoff + i * 20;
            tx += 24;

            List<StackIngredient> stepInputs = new ArrayList<>();
            for (Object o : steps.get(i).matches) {
                ItemStack stack = StackUtil.stackFromObject(o);
                if (!stack.isEmpty()) stepInputs.add(new StackIngredient(stack, Ingredient.EMPTY));
            }
            condense(stepInputs);

            for (int j = 0; j < stepInputs.size(); j ++) {
                stacks.init(slot ++, true, tx, ty);
                tx += 17;
            }
        }

        stacks.init(slot ++, false, 60, yoff + steps.size() * 20 + 14);
        stacks.set(ingredients);
    }

    @Override
    public void draw(RecipeWrappers.Crucible recipe, PoseStack mStack, double mouseX, double mouseY) {
        recipe.page.renderBackground(CodexGui.DUMMY, mStack, 5, 4, (int)mouseX, (int)mouseY);
        recipe.page.render(CodexGui.DUMMY, mStack, 5, 4, (int)mouseX, (int)mouseY);
    }
}
