package elucent.eidolon.data.recipes;

import elucent.eidolon.recipe.recipes.base.BaseRecipe;
import elucent.eidolon.util.json.JsonUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

/**
 * @author DustW
 **/
public abstract class DataGenRecipes {
    private final Map<ResourceLocation, Map.Entry<String, String>> recipes = new HashMap<>();

    protected abstract void addRecipes();

    protected final void addRecipe(ResourceLocation name, String recipe, String subPath) {
        recipes.put(name, new HashMap.SimpleEntry<>(recipe, subPath));
    }

    public Map<ResourceLocation, Map.Entry<String, String>> getRecipes() {
        addRecipes();
        return recipes;
    }

    protected ResourceLocation defaultName(Item item) {
        return item.getRegistryName();
    }

    int i;

    protected String next() {
        return String.valueOf(i++);
    }

    protected ResourceLocation defaultName(Item item, Object o) {
        return new ResourceLocation(item.getRegistryName().getNamespace(), item.getRegistryName().getPath() + o);
    }

    protected <TYPE extends BaseRecipe<TYPE>> String baseRecipe(BaseRecipe<TYPE> recipe) {
        return JsonUtils.INSTANCE.pretty.toJson(recipe);
    }
}
