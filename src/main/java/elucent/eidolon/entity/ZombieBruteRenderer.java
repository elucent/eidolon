package elucent.eidolon.entity;

import elucent.eidolon.Eidolon;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class ZombieBruteRenderer extends MobRenderer<ZombieBruteEntity, ZombieBruteModel> {
    public ZombieBruteRenderer(EntityRendererManager rendererManager, ZombieBruteModel entityModelIn, float shadowSizeIn) {
        super(rendererManager, entityModelIn, shadowSizeIn);
    }

    @Override
    public ResourceLocation getEntityTexture(ZombieBruteEntity entity) {
        return new ResourceLocation(Eidolon.MODID, "textures/entity/zombie_brute.png");
    }
}
