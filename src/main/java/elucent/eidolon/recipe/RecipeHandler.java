package elucent.eidolon.recipe;

import elucent.eidolon.Eidolon;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RecipeHandler {
    public static final WorktableRecipeSerializer WORKTABLE_SERIALIZER = (WorktableRecipeSerializer) new WorktableRecipeSerializer()
            .setRegistryName(new ResourceLocation(Eidolon.MODID, "worktable"));

    @SubscribeEvent
    public static void register(RegistryEvent.Register<RecipeSerializer<?>> event) {
        event.getRegistry().registerAll(
                WORKTABLE_SERIALIZER
        );
    }
}
