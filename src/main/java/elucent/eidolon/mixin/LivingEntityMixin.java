package elucent.eidolon.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import elucent.eidolon.event.SpeedFactorEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "getSpeedFactor()F", at = @At("RETURN"), cancellable = true)
    private void customGetSpeedFactor(CallbackInfoReturnable<Float> cir) {
        float factor = cir.getReturnValue();
        SpeedFactorEvent event = new SpeedFactorEvent((Entity)(Object)this, factor);
        MinecraftForge.EVENT_BUS.post(event);
        cir.setReturnValue(event.getSpeedFactor());
    }
}
