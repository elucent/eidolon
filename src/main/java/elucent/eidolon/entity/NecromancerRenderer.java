package elucent.eidolon.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import elucent.eidolon.Eidolon;
import elucent.eidolon.util.RenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class NecromancerRenderer extends MobRenderer<NecromancerEntity, NecromancerModel> {
    public NecromancerRenderer(Context rendererManager, NecromancerModel model, float shadowSizeIn) {
        super(rendererManager, model, shadowSizeIn);
        this.addLayer(new NecromancerEyesLayer(this));
    }

    public static class NecromancerEyesLayer extends LayerRenderer<NecromancerEntity, NecromancerModel> {
        NecromancerModel model;

        private static final RenderType RENDER_TYPE = RenderType.create(
            Eidolon.MODID+":necromancer_eyes",
            DefaultVertexFormat.NEW_ENTITY,
            GL11.GL_QUADS, 256,
            RenderType.State.builder()
                .setShadeModelState(new RenderStateShard.ShadeModelState(true))
                .setWriteMaskState(new RenderStateShard.WriteMaskState(true, false))
                .setLightmapState(new RenderStateShard.LightmapState(false))
                .setDiffuseLightingState(new RenderStateShard.DiffuseLightingState(false))
                .setTransparencyState(RenderUtil.ADDITIVE_TRANSPARENCY)
                .setTextureState(new RenderStateShard.TextureState(new ResourceLocation(Eidolon.MODID,"textures/entity/necromancer_eyes.png"), false, false))
                .createCompositeState(false)
        );

        public NecromancerEyesLayer(IEntityRenderer<NecromancerEntity, NecromancerModel> entityRendererIn) {
            super(entityRendererIn);
            this.model = new NecromancerModel(0.5f);
        }

        public RenderType getRenderType() {
            return RENDER_TYPE;
        }

        @Override
        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, NecromancerEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(this.getRenderType());
            this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            model.renderToBuffer(matrixStackIn, ivertexbuilder, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(NecromancerEntity entity) {
        return new ResourceLocation(Eidolon.MODID, "textures/entity/necromancer.png");
    }
}
