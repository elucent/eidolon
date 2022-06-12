package elucent.eidolon.data.recipes;

import elucent.eidolon.recipe.CrucibleRegistry;
import elucent.eidolon.recipe.recipes.recipe.CrucibleRecipe;

/**
 * @author DustW
 **/
public class CrucibleRecipes extends DataGenRecipes {
    @Override
    protected void addRecipes() {
        CrucibleRegistry.built().forEach(this::addPlateRecipe);
    }

    protected void addPlateRecipe(CrucibleRecipe recipe) {
        addRecipe(defaultName(recipe.result.getItem(), next()), baseRecipe(recipe), "crucible");
    }
}
