package elucent.eidolon.recipe;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class RecipeUtils {
    public static void forEach(Container inv, Consumer<ItemStack> action) {
        for (int i = 0; i < inv.getContainerSize(); i++) {
            action.accept(inv.getItem(i));
        }
    }
}
