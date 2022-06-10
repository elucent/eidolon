package elucent.eidolon.recipe.recipeobj;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class RecipeItem extends RecipeObject<Item> {
    public RecipeItem(Item obj) {
        super(obj);
    }

    @Override
    public Ingredient getIngredient() {
        return Ingredient.of(obj);
    }

    @Override
    public boolean matches(ItemStack obj) {
        return this.obj == obj.getItem();
    }

    @Override
    public JsonObject toJson() {
        return new Ingredient.ItemValue(new ItemStack(obj)).serialize();
    }

    @Override
    public Item fromJson(JsonObject json) {
        return Ingredient.valueFromJson(json).getItems().toArray(new ItemStack[0])[0].getItem();
    }

    @Override
    public CompoundTag toNBT() {
        CompoundTag nbt = new CompoundTag();
        new ItemStack(obj).save(nbt);
        return nbt;
    }

    @Override
    public Item fromNBT(CompoundTag nbt) {
        return ItemStack.of(nbt).getItem();
    }

    @Override
    public RecipeObjectType<Item, ? extends RecipeObject<Item>> getType() {
        return RecipeObjectRegisterHandler.ITEM;
    }
}
