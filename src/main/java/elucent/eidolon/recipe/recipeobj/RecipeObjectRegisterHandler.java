package elucent.eidolon.recipe.recipeobj;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;

import java.util.HashMap;
import java.util.Map;

public class RecipeObjectRegisterHandler {
    public static final Map<Class<?>, RecipeObjectType<?, ? extends RecipeObject<?>>> TYPES = new HashMap<>();

    public static final RecipeObjectType<Item, RecipeItem> ITEM = RecipeObjectType.create(RecipeItem::new);
    public static final RecipeObjectType<ItemStack, RecipeItemStack> ITEM_STACK = RecipeObjectType.create(RecipeItemStack::new);
    public static final RecipeObjectType<ITag.INamedTag<Item>, RecipeItemTag> TAG = RecipeObjectType.create(RecipeItemTag::new);
    public static final RecipeObjectType<Block, RecipeBlock> BLOCK = RecipeObjectType.create(RecipeBlock::new);

    static {
        TYPES.put(Item.class, ITEM.setRegistryName("item"));
        TYPES.put(ItemStack.class, ITEM_STACK.setRegistryName("itemstack"));
        TYPES.put(ITag.INamedTag.class, TAG.setRegistryName("tag"));
        TYPES.put(Block.class, BLOCK.setRegistryName("block"));
    }
}
