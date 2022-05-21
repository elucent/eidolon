package elucent.eidolon.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.capability.IPlayerData;
import elucent.eidolon.item.IWingsItem;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "getMobType", at = @At("HEAD"), cancellable = true)
    public void getMobType(CallbackInfoReturnable<MobType> ci) {
    	if (((LivingEntity)(Object)this).hasEffect(Registry.UNDEATH_EFFECT.get()) && !Eidolon.trueMobType) {
    		ci.setReturnValue(MobType.UNDEAD);
    	}
    }

    @Inject(method = "isFallFlying", at = @At("HEAD"), cancellable = true)
    public void isFallFlying(CallbackInfoReturnable<Boolean> ci) {
       if ((LivingEntity)(Object)this instanceof Player p) {
    	   Optional<IPlayerData> opt = p.getCapability(IPlayerData.INSTANCE).resolve();
    	   if (opt.isPresent() && opt.get().isDashing(p)) ci.setReturnValue(true);
       }
    }
}
