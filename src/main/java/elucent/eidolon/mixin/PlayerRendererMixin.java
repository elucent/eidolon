package elucent.eidolon.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import elucent.eidolon.entity.RavenVariantLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {
    @Inject(method = "<init>", at = @At("RETURN"), cancellable = true)
    public void constructor(CallbackInfo ci) {
         ((PlayerRenderer)(Object)this).addLayer(new RavenVariantLayer<>(((PlayerRenderer)(Object)this)));
    }
}
