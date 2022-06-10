package elucent.eidolon.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.stream.Collectors;

public class StackUtil {
    public static Ingredient ingredientFromObject(Object object) {
        if (object instanceof Item) return Ingredient.of((Item)object);
        else if (object instanceof Block) return Ingredient.of(new ItemStack((Block)object));
        else if (object instanceof ItemStack) return Ingredient.of((ItemStack)object);
        else if (object instanceof TagKey) return Ingredient.of((TagKey) object);
        else return Ingredient.EMPTY;
    }

    public static List<Ingredient> ingredientsFromObjects(List<Object> objects) {
        return objects.stream().map(StackUtil::ingredientFromObject).collect(Collectors.toList());
    }

    public static ItemStack stackFromObject(Object object) {
        if (object instanceof Item) return new ItemStack((Item)object);
        else if (object instanceof Block) return new ItemStack((Block)object);
        else if (object instanceof ItemStack) return ((ItemStack)object).copy();
        else if (object instanceof TagKey) {
            Object first = ForgeRegistries.ITEMS.tags().getTag((TagKey<Item>) object).stream().findFirst().get();
            return stackFromObject(first);
        }
        else return ItemStack.EMPTY;
    }

    public static List<ItemStack> stacksFromObjects(List<Object> objects) {
        return objects.stream().map(StackUtil::stackFromObject).collect(Collectors.toList());
    }
}
