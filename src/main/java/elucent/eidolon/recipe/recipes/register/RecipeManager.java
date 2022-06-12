package elucent.eidolon.recipe.recipes.register;

import elucent.eidolon.Eidolon;
import elucent.eidolon.recipe.recipes.base.BaseRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author DustW
 **/
public class RecipeManager {
    public static final String MOD_ID = Eidolon.MODID;

    public static <T extends BaseRecipe<T>> List<T> getRecipe(Level level, RecipeType<T> type, List<ItemStack> itemStacks) {
        return level.getRecipeManager().getAllRecipesFor(type).stream().filter(s -> s.matches(itemStacks)).collect(Collectors.toList());
    }

    public static void register(IEventBus bus) {
        RecipeTypes.register(bus);
        RecipeSerializers.register(bus);
    }
}
