package elucent.eidolon.util;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.world.level.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.Tag;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.server.ServerLifecycleHooks;

public class RecipeUtil {
    public static RecipeManager getRecipeManager() {
        return DistExecutor.unsafeRunForDist(
            () -> () -> Minecraft.getInstance().getConnection().getRecipeManager(),
            () -> () -> ServerLifecycleHooks.getCurrentServer().getRecipeManager()
        );
    }

    public static Ingredient ingredientFromObject(Object object) {
        if (object instanceof Item) return Ingredient.of((Item)object);
        else if (object instanceof Block) return Ingredient.of(new ItemStack((Block)object));
        else if (object instanceof ItemStack) return Ingredient.of((ItemStack)object);
        else if (object instanceof Tag) return Ingredient.of((Tag)object);
        else return Ingredient.EMPTY;
    }

    public static List<Ingredient> ingredientsFromObjects(List<Object> objects) {
        return objects.stream().map(RecipeUtil::ingredientFromObject).collect(Collectors.toList());
    }

    public static ItemStack stackFromObject(Object object) {
        if (object instanceof Item) return new ItemStack((Item)object);
        else if (object instanceof Block) return new ItemStack((Block)object);
        else if (object instanceof ItemStack) return ((ItemStack)object).copy();
        else if (object instanceof Tag) {
            Object first = ((Tag)object).getValues().get(0);
            return stackFromObject(first);
        }
        else return ItemStack.EMPTY;
    }

    public static List<ItemStack> stacksFromObjects(List<Object> objects) {
        return objects.stream().map(RecipeUtil::stackFromObject).collect(Collectors.toList());
    }
}
