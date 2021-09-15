package elucent.eidolon.recipe.recipeobj;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;

public abstract class RecipeObject<T> {
    T obj;

    public RecipeObject(T obj) {
        this.obj = obj;
    }

    public abstract Ingredient getIngredient();

    public abstract boolean matches(ItemStack itemStack);

    public abstract JsonObject toJson();

    public abstract T fromJson(JsonObject json);

    public abstract CompoundNBT toNBT();

    public abstract T fromNBT(CompoundNBT nbt);

    public abstract RecipeObjectType<T, ? extends RecipeObject<T>> getType();
}
