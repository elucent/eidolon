package elucent.eidolon.mixin;

import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionBrewing;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PotionBrewing.class)
public interface PotionBrewingMixin {
    @Invoker
    public static void callAddMix(Potion input, Item ingredient, Potion output) {}
}
