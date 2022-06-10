package elucent.eidolon.recipe.recipeobj;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class RecipeItemStack extends RecipeObject<ItemStack> {
    public RecipeItemStack(ItemStack obj) {
        super(obj);
    }

    @Override
    public Ingredient getIngredient() {
        return Ingredient.of(obj);
    }

    @Override
    public boolean matches(ItemStack obj) {
        return ItemStack.matches(this.obj, obj);
    }

    @Override
    public JsonObject toJson() {
        return new Ingredient.ItemValue(obj).serialize();
    }

    @Override
    public ItemStack fromJson(JsonObject json) {
        return Ingredient.valueFromJson(json).getItems().toArray(new ItemStack[0])[0];
    }

    @Override
    public CompoundTag toNBT() {
        CompoundTag nbt = new CompoundTag();
        obj.save(nbt);
        return nbt;
    }

    @Override
    public ItemStack fromNBT(CompoundTag nbt) {
        return ItemStack.of(nbt);
    }

    @Override
    public RecipeObjectType<ItemStack, ? extends RecipeObject<ItemStack>> getType() {
        return RecipeObjectRegisterHandler.ITEM_STACK;
    }
}
