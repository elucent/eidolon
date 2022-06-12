package elucent.eidolon.recipe.recipes.recipe;

import com.google.gson.annotations.Expose;
import elucent.eidolon.recipe.recipes.base.BaseRecipe;
import elucent.eidolon.recipe.recipes.register.RecipeSerializers;
import elucent.eidolon.recipe.recipes.register.RecipeTypes;
import elucent.eidolon.tile.CrucibleTileEntity.CrucibleStep;
import elucent.eidolon.util.StackUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CrucibleRecipe extends BaseRecipe<CrucibleRecipe> {
    @Expose
    public List<Step> steps = new ArrayList<>();
    @Expose
    public ItemStack result;

    public ItemStack getResult() {
        return result;
    }

    @Override
    public boolean matches(List<ItemStack> inputs) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return result.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializers.CRUCIBLE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeTypes.CRUCIBLE.get();
    }

    public static class Step {
        @Expose
        public List<Ingredient> matches;
        @Expose
        public int stirs;

        public Step(int stirs, List<Object> matches) {
            this.stirs = stirs;
            this.matches = StackUtil.ingredientsFromObjects(matches);
        }
    };

    public CrucibleRecipe(ItemStack result) {
        this.result = result;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public CrucibleRecipe addStep(Object... matches) {
        addStirringStep(0, matches);
        return this;
    }

    public CrucibleRecipe addStep(int stirs) {
        addStirringStep(stirs, new Object[]{});
        return this;
    }

    public CrucibleRecipe addStirringStep(int stirs, Object... matches) {
        steps.add(new Step(stirs, Arrays.asList(matches)));
        return this;
    }

    public boolean matches(List<CrucibleStep> items, boolean a) {
        if (steps.size() != items.size()) return false;

        List<Ingredient> matchList = new ArrayList<>();
        List<ItemStack> itemList = new ArrayList<>();

        for (int i = 0; i < steps.size(); i ++) {
            Step correct = steps.get(i);
            CrucibleStep provided = items.get(i);
            if (correct.stirs != provided.getStirs()) return false;

            matchList.clear();
            itemList.clear();
            matchList.addAll(correct.matches);
            itemList.addAll(provided.getContents());

            for (int j = 0; j < matchList.size(); j ++) {
                for (int k = 0; k < itemList.size(); k ++) {
                    if (matchList.get(j).test(itemList.get(k))) {
                        matchList.remove(j --);
                        itemList.remove(k --);
                        break;
                    }
                }
            }

            if (matchList.size() != 0) return false;
        }

        return true;
    }
}
