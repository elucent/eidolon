package elucent.eidolon.recipe;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class WorktableRecipe implements Recipe<Container> {
    Ingredient[] core, extras;
    ItemStack result;
    ResourceLocation registryName;

    public WorktableRecipe(Ingredient[] core, Ingredient[] extras, ItemStack result) {
        this.core = core;
        this.extras = extras;
        this.result = result;
    }

    public ResourceLocation getRegistryName() {
        return registryName;
    }

    public WorktableRecipe setRegistryName(String domain, String path) {
        this.registryName = new ResourceLocation(domain, path);
        return this;
    }

    public WorktableRecipe setRegistryName(ResourceLocation registryName) {
        this.registryName = registryName;
        return this;
    }

    public Ingredient[] getCore() {
        return core;
    }

    public Ingredient[] getOuter() {
        return extras;
    }

    public boolean matches(Container coreInv, Container extraInv) {
        if (coreInv.getContainerSize() < 9 || extraInv.getContainerSize() < 4) return false;
        for (int i = 0; i < core.length; i ++) {
            if (!core[i].test(coreInv.getItem(i))) return false;
        }
        for (int i = 0; i < extras.length; i ++) {
            if (!extras[i].test(extraInv.getItem(i))) return false;
        }
        return true;
    }

    public NonNullList<ItemStack> getRemainingItems(Container coreInv, Container extraInv) {
        NonNullList<ItemStack> items = NonNullList.withSize(13, ItemStack.EMPTY);

        for(int i = 0; i < items.size(); ++i) {
            Container inv = i < 9 ? coreInv : extraInv;
            ItemStack item = inv.getItem(i < 9 ? i : i - 9);
            if (item.hasContainerItem()) {
                items.set(i, item.getContainerItem());
            }
        }

        return items;
    }

    public ItemStack getResult() {
        return result.copy();
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

    public static class Type implements RecipeType<WorktableRecipe> {
        @Override
        public String toString () {
            return Eidolon.MODID + ":worktable";
        }

        public static final Type INSTANCE = new Type();
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<WorktableRecipe> {
        @Override
        public WorktableRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            Map<String, Ingredient> ingredientMap = new HashMap<>();
            JsonObject keys = json.getAsJsonObject("key");
            for (Map.Entry<String, JsonElement> e : keys.entrySet()) {
                if (e.getKey().length() != 1) throw new RuntimeException("Recipe ingredient key must be a single character");
                ingredientMap.put(e.getKey(), Ingredient.fromJson(e.getValue().getAsJsonObject()));
            }
            Ingredient[] core = new Ingredient[9], extras = new Ingredient[4];
            JsonArray pattern = json.getAsJsonArray("pattern");
            if (pattern.size() != 3) throw new JsonSyntaxException("All worktable recipes must have three rows.");
            for (int i = 0; i < 3; i ++) {
                if (pattern.get(i).getAsString().length() != 3) throw new JsonSyntaxException("All worktable recipe rows must have three columns.");
                for (int j = 0; j < 3; j ++) {
                    String key = pattern.get(i).getAsString().substring(j, j + 1);
                    Ingredient item = key.equals(" ") ? Ingredient.EMPTY : ingredientMap.get(key);
                    core[i * 3 + j] = item;
                }
            }
            String reagents = json.get("reagents").getAsString();
            if (reagents.length() != 4) throw new JsonSyntaxException("All worktable recipes must have reagent strings of length 4.");
            for (int i = 0; i < 4; i ++) {
                String key = reagents.substring(i, i + 1);
                extras[i] = key.equals(" ") ? Ingredient.EMPTY : ingredientMap.get(key);
            }
            ItemStack result = CraftingHelper.getItemStack(json.getAsJsonObject("result"), true);
            return WorktableRegistry.register(new WorktableRecipe(core, extras, result).setRegistryName(recipeId));
        }

        @Override
        public WorktableRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient[] core = new Ingredient[9], extras = new Ingredient[4];
            for (int i = 0; i < 9; i ++) core[i] = Ingredient.fromNetwork(buffer);
            for (int i = 0; i < 4; i ++) extras[i] = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            return WorktableRegistry.register(new WorktableRecipe(core, extras, result).setRegistryName(recipeId));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, WorktableRecipe recipe) {
            for (int i = 0; i < 9; i ++) recipe.core[i].toNetwork(buffer);
            for (int i = 0; i < 4; i ++) recipe.extras[i].toNetwork(buffer);
            buffer.writeItem(recipe.result);
        }
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Registry.WORKTABLE_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public boolean isSpecial() {
        return true; // needed to prevent errors loading modded recipes in the recipe book
    }
}
