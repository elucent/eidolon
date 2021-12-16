package elucent.eidolon.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SlimySlugRenderer extends MobRenderer<SlimySlugEntity, SlimySlugModel> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Eidolon.MODID, "textures/entity/slimy_slug.png");
    public SlimySlugRenderer(Context erm) {
        super(erm, new SlimySlugModel(erm.bakeLayer(Registry.SLUG_LAYER)), 0.4f);
    }

    @Override
    public ResourceLocation getTextureLocation(SlimySlugEntity entity) {
        return TEXTURE;
    }

    @Override
    protected void scale(SlimySlugEntity e, PoseStack matrixStackIn, float partialTickTime) {
    	matrixStackIn.scale(2 / (1 + e.squishAmount), 2 / (1 + e.squishAmount), e.squishAmount);
    }
}
