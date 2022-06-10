package elucent.eidolon.recipe.recipeobj;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public abstract class RecipeObject<T> {
    T obj;

    public RecipeObject(T obj) {
        this.obj = obj;
    }

    public abstract Ingredient getIngredient();

    public abstract boolean matches(ItemStack itemStack);

    public abstract JsonObject toJson();

    public abstract T fromJson(JsonObject json);

    public abstract CompoundTag toNBT();

    public abstract T fromNBT(CompoundTag nbt);

    public abstract RecipeObjectType<T, ? extends RecipeObject<T>> getType();
}
