package elucent.eidolon.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.function.Consumer;

public class RecipeUtils {
    public static void forEach(IInventory inv, Consumer<ItemStack> action) {
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            action.accept(inv.getStackInSlot(i));
        }
    }
}
