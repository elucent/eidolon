package elucent.eidolon.recipe.recipeobj;

import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

public class RecipeObjectRegisterHandler {
    public static final Map<Class<?>, RecipeObjectType<?, ? extends RecipeObject<?>>> TYPES = new HashMap<>();

    public static final RecipeObjectType<Item, RecipeItem> ITEM = RecipeObjectType.create(RecipeItem::new);
    public static final RecipeObjectType<ItemStack, RecipeItemStack> ITEM_STACK = RecipeObjectType.create(RecipeItemStack::new);
    public static final RecipeObjectType<Tag.INamedTag<Item>, RecipeItemTag> TAG = RecipeObjectType.create(RecipeItemTag::new);
    public static final RecipeObjectType<Block, RecipeBlock> BLOCK = RecipeObjectType.create(RecipeBlock::new);

    static {
        TYPES.put(Item.class, ITEM.setRegistryName("item"));
        TYPES.put(ItemStack.class, ITEM_STACK.setRegistryName("itemstack"));
        TYPES.put(Tag.INamedTag.class, TAG.setRegistryName("tag"));
        TYPES.put(Block.class, BLOCK.setRegistryName("block"));
    }
}
