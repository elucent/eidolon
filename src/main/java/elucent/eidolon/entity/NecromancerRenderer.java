package elucent.eidolon.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import elucent.eidolon.Eidolon;
import elucent.eidolon.util.RenderUtil;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.SpiderEyesLayer;
import net.minecraft.client.renderer.entity.model.SpiderModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

public class NecromancerRenderer extends MobRenderer<NecromancerEntity, NecromancerModel> {
    public NecromancerRenderer(EntityRendererManager rendererManager, NecromancerModel model, float shadowSizeIn) {
        super(rendererManager, model, shadowSizeIn);
        this.addLayer(new NecromancerEyesLayer(this));
    }

    public static class NecromancerEyesLayer extends LayerRenderer<NecromancerEntity, NecromancerModel> {
        NecromancerModel model;

        private static final RenderType RENDER_TYPE = RenderType.makeType(
            Eidolon.MODID+":necromancer_eyes",
            DefaultVertexFormats.ENTITY,
            GL11.GL_QUADS, 256,
            RenderType.State.getBuilder()
                .shadeModel(new RenderState.ShadeModelState(true))
                .writeMask(new RenderState.WriteMaskState(true, false))
                .lightmap(new RenderState.LightmapState(false))
                .diffuseLighting(new RenderState.DiffuseLightingState(false))
                .transparency(RenderUtil.ADDITIVE_TRANSPARENCY)
                .texture(new RenderState.TextureState(new ResourceLocation(Eidolon.MODID,"textures/entity/necromancer_eyes.png"), false, false))
                .build(false)
        );

        public NecromancerEyesLayer(IEntityRenderer<NecromancerEntity, NecromancerModel> entityRendererIn) {
            super(entityRendererIn);
            this.model = new NecromancerModel(0.5f);
        }

        public RenderType getRenderType() {
            return RENDER_TYPE;
        }

        @Override
        public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, NecromancerEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.getRenderType());
            this.model.setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            model.render(matrixStackIn, ivertexbuilder, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public ResourceLocation getEntityTexture(NecromancerEntity entity) {
        return new ResourceLocation(Eidolon.MODID, "textures/entity/necromancer.png");
    }
}
