package elucent.eidolon.recipe.recipeobj;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.item.crafting.Ingredient;

public class RecipeBlock extends RecipeObject<Block> {
    public RecipeBlock(Block obj) {
        super(obj);
    }

    @Override
    public Ingredient getIngredient() {
        return Ingredient.of(obj);
    }

    @Override
    public boolean matches(ItemStack obj) {
        return this.obj.asItem() == obj.getItem();
    }

    @Override
    public JsonObject toJson() {
        return new Ingredient.SingleItemList(new ItemStack(obj)).serialize();
    }

    @Override
    public Block fromJson(JsonObject json) {
        return ((BlockItem) (Ingredient.valueFromJson(json).getItems().toArray(new ItemStack[0])[0].getItem())).getBlock();
    }

    @Override
    public CompoundTag toNBT() {
        CompoundTag nbt = new CompoundTag();
        new ItemStack(obj).save(nbt);
        return nbt;
    }

    @Override
    public Block fromNBT(CompoundTag nbt) {
        return ((BlockItem) ItemStack.of(nbt).getItem()).getBlock();
    }

    @Override
    public RecipeObjectType<Block, ? extends RecipeObject<Block>> getType() {
        return RecipeObjectRegisterHandler.BLOCK;
    }
}
