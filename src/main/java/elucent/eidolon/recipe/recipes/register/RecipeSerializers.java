package elucent.eidolon.recipe.recipes.register;

import elucent.eidolon.recipe.recipes.base.BaseSerializer;
import elucent.eidolon.recipe.recipes.recipe.CrucibleRecipe;
import elucent.eidolon.recipe.recipes.recipe.WorktableRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author DustW
 **/
public class RecipeSerializers {
    private static final DeferredRegister<RecipeSerializer<?>> SERIALIZER =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, RecipeManager.MOD_ID);

    public static final RegistryObject<BaseSerializer<?>> WORKTABLE =
            SERIALIZER.register("worktable", () -> new BaseSerializer<>(WorktableRecipe.class));

    public static final RegistryObject<BaseSerializer<?>> CRUCIBLE =
            SERIALIZER.register("crucible", () -> new BaseSerializer<>(CrucibleRecipe.class));


    public static void register(IEventBus bus) {
        SERIALIZER.register(bus);
    }
}
