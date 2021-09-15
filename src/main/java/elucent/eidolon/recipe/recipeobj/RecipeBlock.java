package elucent.eidolon.recipe.recipeobj;

import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;

public class RecipeBlock extends RecipeObject<Block> {
    public RecipeBlock(Block obj) {
        super(obj);
    }

    @Override
    public Ingredient getIngredient() {
        return Ingredient.fromItems(obj);
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
        return ((BlockItem) (Ingredient.deserializeItemList(json).getStacks().toArray(new ItemStack[0])[0].getItem())).getBlock();
    }

    @Override
    public CompoundNBT toNBT() {
        CompoundNBT nbt = new CompoundNBT();
        new ItemStack(obj).write(nbt);
        return nbt;
    }

    @Override
    public Block fromNBT(CompoundNBT nbt) {
        return ((BlockItem) ItemStack.read(nbt).getItem()).getBlock();
    }

    @Override
    public RecipeObjectType<Block, ? extends RecipeObject<Block>> getType() {
        return RecipeObjectRegisterHandler.BLOCK;
    }
}
