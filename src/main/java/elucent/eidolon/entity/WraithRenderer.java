package elucent.eidolon.entity;

import elucent.eidolon.Eidolon;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class WraithRenderer extends MobRenderer<WraithEntity, WraithModel> {
    public WraithRenderer(EntityRendererManager rendererManager, WraithModel entityModelIn, float shadowSizeIn) {
        super(rendererManager, entityModelIn, shadowSizeIn);
    }

    @Override
    public ResourceLocation getEntityTexture(WraithEntity entity) {
        return new ResourceLocation(Eidolon.MODID, "textures/entity/wraith.png");
    }
}
