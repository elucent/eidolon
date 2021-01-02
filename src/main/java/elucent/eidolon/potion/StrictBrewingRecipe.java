package elucent.eidolon.potion;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipe;

import javax.annotation.Nonnull;

public class StrictBrewingRecipe extends BrewingRecipe {
    ItemStack inputStack;
    public StrictBrewingRecipe(ItemStack input, Ingredient ingredient, ItemStack output) {
        super(Ingredient.fromStacks(input), ingredient, output);
        this.inputStack = input;
    }

    @Override
    public boolean isInput(@Nonnull ItemStack stack) {
        return ItemStack.areItemsEqual(inputStack, stack)
            && ItemStack.areItemStackTagsEqual(inputStack, stack);
    }
}
