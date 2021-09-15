package elucent.eidolon.mixin;

import elucent.eidolon.Registry;
import elucent.eidolon.event.SpeedFactorEvent;
import elucent.eidolon.item.ReaperScytheItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "getSpeedFactor()F", at = @At("RETURN"), cancellable = true)
    private void customGetSpeedFactor(CallbackInfoReturnable<Float> cir) {
        float factor = cir.getReturnValue();
        SpeedFactorEvent event = new SpeedFactorEvent((Entity)(Object)this, factor);
        MinecraftForge.EVENT_BUS.post(event);
        cir.setReturnValue(event.getSpeedFactor());
    }

    @Inject(method = "dropLoot", at = @At("HEAD"), cancellable = true)
    protected void customDropLoot(DamageSource source, boolean hitRecently, CallbackInfo ci) {
        if (((LivingEntity)(Object)this).isEntityUndead()
            && (source.getDamageType() == Registry.RITUAL_DAMAGE.getDamageType()
                || source.getTrueSource() instanceof LivingEntity
                    && ((LivingEntity) source.getTrueSource()).getHeldItemMainhand().getItem() instanceof ReaperScytheItem)) {
            ci.cancel();
        }
    }
}
