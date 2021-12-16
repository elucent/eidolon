package elucent.eidolon.potion;

import javax.annotation.Nonnull;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipe;

public class StrictBrewingRecipe extends BrewingRecipe {
    ItemStack inputStack;
    public StrictBrewingRecipe(ItemStack input, Ingredient ingredient, ItemStack output) {
        super(Ingredient.of(input), ingredient, output);
        this.inputStack = input;
    }

    @Override
    public boolean isInput(@Nonnull ItemStack stack) {
        return ItemStack.isSame(inputStack, stack)
            && ItemStack.tagMatches(inputStack, stack);
    }
}
