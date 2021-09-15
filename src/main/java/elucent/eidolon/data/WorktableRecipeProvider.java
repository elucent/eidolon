package elucent.eidolon.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import elucent.eidolon.Eidolon;
import elucent.eidolon.recipe.WorktableRecipe;
import elucent.eidolon.recipe.WorktableRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ForgeRecipeProvider;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class WorktableRecipeProvider extends ForgeRecipeProvider {
    public WorktableRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        registerInfrastructureRecipes(consumer);
    }

    protected void registerInfrastructureRecipes(Consumer<IFinishedRecipe> consumer) {
        WorktableRegistry.init();
        WorktableRegistry.recipes.values().forEach(consumer);
    }
}
