package elucent.eidolon.data;

import elucent.eidolon.data.recipes.DataGenRecipes;
import elucent.eidolon.data.recipes.CrucibleRecipes;
import elucent.eidolon.data.recipes.WorktableRecipes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author DustW
 **/
public class RecipesProvider extends RecipeProvider {

    public RecipesProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    protected List<DataGenRecipes> recipes = new ArrayList<>();

    @Override
    public void run(HashCache pCache) {
        super.run(pCache);

        recipes.forEach(dataGenRecipes -> {
            dataGenRecipes.getRecipes().forEach((name, entry) -> save(pCache, name, entry));
        });
    }

    protected void save(HashCache pCache, ResourceLocation name, Map.Entry<String, String> entry) {
        String json = entry.getKey();
        String subPath = entry.getValue();

        Path path = this.generator.getOutputFolder();

        saveRecipe(pCache, json, path.resolve("data/" + name.getNamespace() + "/recipes/" + subPath + "/" + name.getPath() + ".json"));
    }

    private static void saveRecipe(HashCache pCache, String recipe, Path pPath) {
        try {
            String s1 = SHA1.hashUnencodedChars(recipe).toString();
            if (!Objects.equals(pCache.getHash(pPath), s1) || !Files.exists(pPath)) {
                Files.createDirectories(pPath.getParent());
                BufferedWriter bufferedwriter = Files.newBufferedWriter(pPath);

                try {
                    bufferedwriter.write(recipe);
                } catch (Throwable throwable1) {
                    try {
                        bufferedwriter.close();
                    } catch (Throwable throwable) {
                        throwable1.addSuppressed(throwable);
                    }

                    throw throwable1;
                }

                bufferedwriter.close();
            }

            pCache.putNew(pPath, s1);
        } catch (IOException ignored) {
        }
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        addAvarusRecipes();
    }

    protected void addAvarusRecipes() {
        recipes.add(new CrucibleRecipes());
        recipes.add(new WorktableRecipes());
    }
}
