package elucent.eidolon.entity;

import elucent.eidolon.Eidolon;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ZombieBruteRenderer extends MobRenderer<ZombieBruteEntity, ZombieBruteModel> {
    public ZombieBruteRenderer(Context rendererManager, ZombieBruteModel entityModelIn, float shadowSizeIn) {
        super(rendererManager, entityModelIn, shadowSizeIn);
    }

    @Override
    public ResourceLocation getTextureLocation(ZombieBruteEntity entity) {
        return new ResourceLocation(Eidolon.MODID, "textures/entity/zombie_brute.png");
    }
}
