package elucent.eidolon.recipe.recipeobj;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;

public class RecipeItemStack extends RecipeObject<ItemStack> {
    public RecipeItemStack(ItemStack obj) {
        super(obj);
    }

    @Override
    public Ingredient getIngredient() {
        return Ingredient.fromStacks(obj);
    }

    @Override
    public boolean matches(ItemStack obj) {
        return ItemStack.areItemStacksEqual(this.obj, obj);
    }

    @Override
    public JsonObject toJson() {
        return new Ingredient.SingleItemList(obj).serialize();
    }

    @Override
    public ItemStack fromJson(JsonObject json) {
        return Ingredient.deserializeItemList(json).getStacks().toArray(new ItemStack[0])[0];
    }

    @Override
    public CompoundNBT toNBT() {
        CompoundNBT nbt = new CompoundNBT();
        obj.write(nbt);
        return nbt;
    }

    @Override
    public ItemStack fromNBT(CompoundNBT nbt) {
        return ItemStack.read(nbt);
    }

    @Override
    public RecipeObjectType<ItemStack, ? extends RecipeObject<ItemStack>> getType() {
        return RecipeObjectRegisterHandler.ITEM_STACK;
    }
}
