package elucent.eidolon.client.renderer.blockentity;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import elucent.eidolon.Eidolon;
import elucent.eidolon.client.models.ModelRegistry;
import elucent.eidolon.tile.CrucibleTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor.ARGB32;
import net.minecraft.world.level.block.CauldronBlock;

public class CrucibleTileRenderer extends ModBlockEntityRenderer<CrucibleTileEntity> {
    private final ModelPart stirrer;
    public static final ResourceLocation STIRRER_TEXTURE = new ResourceLocation(Eidolon.MODID, "textures/block/crucible_stirrer.png");

    public CrucibleTileRenderer(BlockEntityRenderDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
        stirrer = Minecraft.getInstance().getEntityModels().bakeLayer(ModelRegistry.CRUCIBLE);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 8)
                .addBox(-1.5F, 0.0F, -1.5F,
                        3.0F, 3.0F, 3.0F,
                        new CubeDeformation(0.0F))
                .texOffs(0, 0)
                .addBox(-1.0F, 3.0F, -1.0F,
                        2.0F, 6.0F, 2.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void render(CrucibleTileEntity tile, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.setShaderTexture(0, STIRRER_TEXTURE);
        float coeff = tile.stirTicks == 0 ? 0 : (tile.stirTicks - partialTicks) / 20.0f;
        if (!tile.getLevel().getBlockState(tile.getBlockPos().above()).isFaceSturdy(tile.getLevel(), tile.getBlockPos().above(), Direction.DOWN)) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5, 0.625, 0.5);
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(45 + coeff * 360));
            matrixStackIn.translate(0, -0.125 * Math.sin(coeff * Math.PI), 0.125);
            stirrer.xRot = (float) Math.PI / 8 * (1.0f - (float) Math.sin(coeff * Math.PI));
            stirrer.render(matrixStackIn, bufferIn.getBuffer(RenderType.entitySolid(STIRRER_TEXTURE)), combinedLightIn, combinedOverlayIn);
            matrixStackIn.popPose();
        }
        if (tile.hasWater) {
            CauldronBlock block;
            TextureAtlasSprite water = mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
                .apply(new ResourceLocation("minecraft", "block/water_still"));
            VertexConsumer builder = bufferIn.getBuffer(RenderType.translucentNoCrumbling());
            Matrix4f mat = matrixStackIn.last().pose();
            int color = BiomeColors.getAverageWaterColor(tile.getLevel(), tile.getBlockPos());
            int r = ARGB32.red(color), g = ARGB32.green(color),
                b = ARGB32.blue(color), a = ARGB32.alpha(color);

            if (tile.steps.size() > 0) {
                r = (int)(tile.getRed() * 255);
                g = (int)(tile.getGreen() * 255);
                b = (int)(tile.getBlue() * 255);
            }
            builder.vertex(mat, 0.125f, 0.75f, 0.125f).color(r, g, b, 192).uv(water.getU(2), water.getV(2)).uv2(combinedLightIn).normal(0, 1, 0).endVertex();
            builder.vertex(mat, 0.125f, 0.75f, 0.875f).color(r, g, b, 192).uv(water.getU(14), water.getV(2)).uv2(combinedLightIn).normal(0, 1, 0).endVertex();
            builder.vertex(mat, 0.875f, 0.75f, 0.875f).color(r, g, b, 192).uv(water.getU(14), water.getV(14)).uv2(combinedLightIn).normal(0, 1, 0).endVertex();
            builder.vertex(mat, 0.875f, 0.75f, 0.125f).color(r, g, b, 192).uv(water.getU(2), water.getV(14)).uv2(combinedLightIn).normal(0, 1, 0).endVertex();
        }
    }
}
