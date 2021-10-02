package elucent.eidolon.recipe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import elucent.eidolon.recipe.recipeobj.RecipeObject;
import elucent.eidolon.recipe.recipeobj.RecipeObjectType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class WorktableRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<WorktableRecipe> {
    @Override
    public WorktableRecipe read(ResourceLocation recipeId, JsonObject json) {
        JsonObject core = json.get("core").getAsJsonObject();
        Object[] coreList = new Object[9];
        for (int i = 0; i < coreList.length; i++) {
            coreList[i] = formJson(core.get("core" + i).getAsJsonObject());
        }
        WorktableRecipe.RecipeCore recipeCore = new WorktableRecipe.RecipeCore(
                coreList[0], coreList[1],coreList[2],
                coreList[3], coreList[4],coreList[5],
                coreList[6], coreList[7],coreList[8]
        );

        JsonObject ex = json.get("extras").getAsJsonObject();
        Object[] exList = new Object[4];
        for (int i = 0; i < exList.length; i++) {
            exList[i] = formJson(ex.get("extras" + i).getAsJsonObject());
        }
        WorktableRecipe.RecipeExtras recipeExtras = new WorktableRecipe.RecipeExtras(
                exList[0], exList[1], exList[2], exList[3]
        );

        ItemStack result = Ingredient.deserializeItemList(json.get("result").getAsJsonObject()).getStacks().iterator().next();
        return new WorktableRecipe(recipeCore, recipeExtras, result).setRegistryName(recipeId);
    }

    Gson gson = new GsonBuilder().create();

    @Nullable
    @Override
    public WorktableRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
        JsonObject json = gson.fromJson(buffer.readString(), JsonElement.class).getAsJsonObject();
        return read(recipeId, json);
    }

    @Override
    public void write(PacketBuffer buffer, WorktableRecipe recipe) {
        JsonObject json = new JsonObject();
        write(json, recipe);
        buffer.writeString(json.toString());
    }

    public void write(JsonObject json, WorktableRecipe recipe) {
        JsonObject core = new JsonObject();
        for (int i = 0; i < recipe.core.CONTEXT.size(); i++) {
            core.add("core" + i, getJson(recipe.core.CONTEXT.get(i)));
        }
        json.add("core", core);

        JsonObject ex = new JsonObject();
        for (int i = 0; i < recipe.extras.CONTEXT.size(); i++) {
            ex.add("extras" + i, getJson(recipe.extras.CONTEXT.get(i)));
        }
        json.add("extras", ex);

        JsonElement result = Ingredient.fromStacks(recipe.result).serialize();
        json.add("result", result);
    }

    JsonObject getJson(RecipeObject<?> obj) {
        JsonObject result = new JsonObject();
        result.add("item", obj.toJson());
        result.addProperty("type", obj.getType().getRegistryName().toString());
        return result;
    }

    Object formJson(JsonObject obj) {
        String type = obj.get("type").getAsString();
        return RecipeObjectType.of(new ResourceLocation(type)).apply(null).fromJson(obj.get("item").getAsJsonObject());
    }
}
