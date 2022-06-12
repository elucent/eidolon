package elucent.eidolon.data.recipes;

import elucent.eidolon.recipe.WorktableRegistry;
import elucent.eidolon.recipe.recipes.recipe.WorktableRecipe;

/**
 * @author DustW
 **/
public class WorktableRecipes extends DataGenRecipes {
    @Override
    protected void addRecipes() {
        WorktableRegistry.built().forEach(this::addPlateRecipe);
    }

    protected void addPlateRecipe(WorktableRecipe recipe) {
        addRecipe(defaultName(recipe.getResultItem().getItem(), next()), baseRecipe(recipe), "worktable");
    }
}
