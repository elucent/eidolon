package elucent.eidolon.recipe.recipes.register;

import elucent.eidolon.recipe.recipes.base.BaseRecipe;
import elucent.eidolon.recipe.recipes.recipe.CrucibleRecipe;
import elucent.eidolon.recipe.recipes.recipe.WorktableRecipe;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author DustW
 **/
public class RecipeTypes {
    private static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(Registry.RECIPE_TYPE.key(), RecipeManager.MOD_ID);

    public static final RegistryObject<RecipeType<CrucibleRecipe>> CRUCIBLE = register("crucible");
    public static final RegistryObject<RecipeType<WorktableRecipe>> WORKTABLE = register("worktable");

    private static <TYPE extends BaseRecipe<TYPE>> RegistryObject<RecipeType<TYPE>> register(String name) {
        return TYPES.register(name, () -> new RecipeType<>() {
            @Override
            public String toString() {
                return new ResourceLocation(RecipeManager.MOD_ID, name).toString();
            }
        });
    }

    public static void register(IEventBus bus) {
        TYPES.register(bus);
    }
}
