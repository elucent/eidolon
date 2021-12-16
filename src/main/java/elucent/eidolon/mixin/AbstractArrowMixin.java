package elucent.eidolon.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;

@Mixin(AbstractArrow.class)
public interface AbstractArrowMixin {
    @Invoker
    ItemStack callGetPickupItem();

    @Invoker
    void callOnHitEntity(EntityHitResult result);
}
