package elucent.eidolon.entity;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ZombieBruteRenderer extends MobRenderer<ZombieBruteEntity, ZombieBruteModel> {
    public ZombieBruteRenderer(Context erm) {
        super(erm, new ZombieBruteModel(erm.bakeLayer(Registry.ZOMBIE_BRUTE_LAYER)), 0.6f);
    }

    @Override
    public ResourceLocation getTextureLocation(ZombieBruteEntity entity) {
        return new ResourceLocation(Eidolon.MODID, "textures/entity/zombie_brute.png");
    }
}
