package elucent.eidolon.recipe;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.tile.CrucibleTileEntity.CrucibleStep;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class CrucibleRecipe implements Recipe<Container> {
    List<Step> steps = new ArrayList<>();
    ResourceLocation registryName;
    ItemStack result;

    public ItemStack getResult() {
        return result;
    }

    public static class Step {
        public List<Ingredient> matches = new ArrayList<>();
        public int stirs;

        public Step(int stirs, List<Ingredient> matches) {
            this.stirs = stirs;
            this.matches.addAll(matches);
        }
    };

    public CrucibleRecipe(List<Step> steps, ItemStack result) {
        this.steps = steps;
        this.result = result;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public ResourceLocation getRegistryName() {
        return registryName;
    }

    public CrucibleRecipe setRegistryName(String domain, String path) {
        this.registryName = new ResourceLocation(domain, path);
        return this;
    }

    public CrucibleRecipe setRegistryName(ResourceLocation registryName) {
        this.registryName = registryName;
        return this;
    }

    public boolean matches(List<CrucibleStep> items) {
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

            if (matchList.size() != 0 || itemList.size() != 0) return false;
        }

        return true;
    }

    @Override
    public boolean matches(Container inv, Level worldIn) {
        return false; // we don't use a single inventory, so we ignore this one
    }

    @Override
    public ItemStack assemble(Container inv) {
        return getResultItem();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false; // we don't use a single inventory, so we ignore this one
    }

    @Override
    public ItemStack getResultItem() {
        return result;
    }

    @Override
    public ResourceLocation getId() {
        return registryName;
    }

    public static class Type implements RecipeType<CrucibleRecipe> {
        @Override
        public String toString () {
            return Eidolon.MODID + ":crucible";
        }

        public static final CrucibleRecipe.Type INSTANCE = new CrucibleRecipe.Type();
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<CrucibleRecipe> {
        @Override
        public CrucibleRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            List<Step> steps = new ArrayList<>();
            JsonArray stepArray = json.getAsJsonArray("steps");
            for (JsonElement elt : stepArray) {
                if (!elt.isJsonObject()) throw new JsonSyntaxException("Expected JSON object for crucible step.");
                JsonObject step = elt.getAsJsonObject();
                int stirs = step.has("stirs") ? step.get("stirs").getAsInt() : 0;
                List<Ingredient> matches = new ArrayList<>();
                if (step.has("items")) {
                    JsonArray items = step.get("items").getAsJsonArray();
                    for (JsonElement item : items) matches.add(Ingredient.fromJson(item));
                }
                steps.add(new Step(stirs, matches));
            }
            ItemStack result = CraftingHelper.getItemStack(json.getAsJsonObject("result"), true);
            return CrucibleRegistry.register(new CrucibleRecipe(steps, result).setRegistryName(recipeId));
        }

        @Override
        public CrucibleRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            int count = buffer.readInt();
            List<Step> steps = new ArrayList<>();
            for (int i = 0; i < count; i ++) {
                int stirs = buffer.readInt();
                int ingredients = buffer.readInt();
                List<Ingredient> matches = new ArrayList<>();
                for (int j = 0; j < ingredients; j ++) matches.add(Ingredient.fromNetwork(buffer));
                steps.add(new Step(stirs, matches));
            }
            ItemStack result = buffer.readItem();
            return CrucibleRegistry.register(new CrucibleRecipe(steps, result).setRegistryName(recipeId));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CrucibleRecipe recipe) {
            buffer.writeInt(recipe.steps.size());
            for (Step step : recipe.steps) {
                buffer.writeInt(step.stirs);
                buffer.writeInt(step.matches.size());
                for (Ingredient i : step.matches) i.toNetwork(buffer);
            }
            buffer.writeItem(recipe.result);
        }
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Registry.CRUCIBLE_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CrucibleRecipe.Type.INSTANCE;
    }

    @Override
    public boolean isSpecial() {
        return true; // needed to prevent errors loading modded recipes in the recipe book
    }
}
