package elucent.eidolon.data;

import elucent.eidolon.recipe.WorktableRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraftforge.common.data.ForgeRecipeProvider;

import java.util.function.Consumer;

public class WorktableRecipeProvider extends ForgeRecipeProvider {
    public WorktableRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<FinishedRecipe> consumer) {
        registerInfrastructureRecipes(consumer);
    }

    protected void registerInfrastructureRecipes(Consumer<FinishedRecipe> consumer) {
        WorktableRegistry.init();
        WorktableRegistry.recipes.values().forEach(consumer);
    }
}
