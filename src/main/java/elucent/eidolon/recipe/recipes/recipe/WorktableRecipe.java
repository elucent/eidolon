package elucent.eidolon.recipe.recipes.recipe;

import com.google.gson.annotations.Expose;
import elucent.eidolon.recipe.RecipeUtils;
import elucent.eidolon.recipe.recipes.base.BaseRecipe;
import elucent.eidolon.recipe.recipes.register.RecipeSerializers;
import elucent.eidolon.recipe.recipes.register.RecipeTypes;
import elucent.eidolon.util.StackUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.util.RecipeMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WorktableRecipe extends BaseRecipe<WorktableRecipe> implements CraftingRecipe {
    @Expose
    List<Ingredient> core;
    @Expose
    List<Ingredient> extras;
    @Expose
    ItemStack result;

    public WorktableRecipe(List<Object> core, List<Object> extras, ItemStack result) {
        this.core = StackUtil.ingredientsFromObjects(core);
        this.extras = StackUtil.ingredientsFromObjects(extras);
        this.result = result;
    }

    public List<Ingredient> getCore() {
        return core;
    }

    public List<Ingredient> getOuter() {
        return extras;
    }

    @Override
    public boolean matches(List<ItemStack> inputs) {
        return false;
    }

    public boolean matches(Container coreInv, Container extraInv) {
        ArrayList<ItemStack> core = new ArrayList<>();
        ArrayList<ItemStack> extras = new ArrayList<>();

        RecipeUtils.forEach(coreInv, (itemStack) -> core.add(itemStack.getContainerItem()));
        RecipeUtils.forEach(extraInv, (itemStack) -> extras.add(itemStack.getContainerItem()));

        return RecipeMatcher.findMatches(core, this.core) != null && RecipeMatcher.findMatches(extras, this.extras) != null;
    }

    public NonNullList<ItemStack> getRemainingItems(Container coreInv, Container extraInv) {
        ArrayList<ItemStack> result = new ArrayList<>();

        Consumer<ItemStack> action = itemStack -> result.add(itemStack.getContainerItem());

        RecipeUtils.forEach(coreInv, action);
        RecipeUtils.forEach(extraInv, action);

        return NonNullList.of(ItemStack.EMPTY, result.toArray(new ItemStack[0]));
    }

    @Override
    public ItemStack getResultItem() {
        return result.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializers.WORKTABLE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeTypes.WORKTABLE.get();
    }
}
