package elucent.eidolon.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import elucent.eidolon.Eidolon;
import net.minecraft.block.CauldronBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ColorHelper.PackedColor;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.biome.BiomeColors;

public class CrucibleTileRenderer extends TileEntityRenderer<CrucibleTileEntity> {
    private final ModelRenderer stirrer;
    public static final ResourceLocation STIRRER_TEXTURE = new ResourceLocation(Eidolon.MODID, "textures/block/crucible_stirrer.png");

    public CrucibleTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
        stirrer = new ModelRenderer(16, 16, 0, 0);
        stirrer.setRotationPoint(0.0F, 0.0F, 0.0F);
        stirrer.setTextureOffset(0, 8).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F, 0.0F, false);
        stirrer.setTextureOffset(0, 0).addBox(-1.0F, 3.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);
    }

    @Override
    public void render(CrucibleTileEntity tile, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bindTexture(STIRRER_TEXTURE);
        float coeff = tile.stirTicks == 0 ? 0 : (tile.stirTicks - partialTicks) / 20.0f;
        if (!tile.getWorld().getBlockState(tile.getPos().up()).isSolidSide(tile.getWorld(), tile.getPos().up(), Direction.DOWN)) {
            matrixStackIn.push();
            matrixStackIn.translate(0.5, 0.625, 0.5);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(45 + coeff * 360));
            matrixStackIn.translate(0, -0.125 * Math.sin(coeff * Math.PI), 0.125);
            stirrer.rotateAngleX = (float) Math.PI / 8 * (1.0f - (float) Math.sin(coeff * Math.PI));
            stirrer.render(matrixStackIn, bufferIn.getBuffer(RenderType.getEntitySolid(STIRRER_TEXTURE)), combinedLightIn, combinedOverlayIn);
            matrixStackIn.pop();
        }
        if (tile.hasWater) {
            CauldronBlock block;
            TextureAtlasSprite water = mc.getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE)
                .apply(new ResourceLocation("minecraft", "block/water_still"));
            IVertexBuilder builder = bufferIn.getBuffer(RenderType.getTranslucentNoCrumbling());
            Matrix4f mat = matrixStackIn.getLast().getMatrix();
            int color = BiomeColors.getWaterColor(tile.getWorld(), tile.getPos());
            int r = PackedColor.getRed(color), g = PackedColor.getGreen(color),
                b = PackedColor.getBlue(color), a = PackedColor.getAlpha(color);

            if (tile.steps.size() > 0) {
                r = (int)(tile.getRed() * 255);
                g = (int)(tile.getGreen() * 255);
                b = (int)(tile.getBlue() * 255);
            }
            builder.pos(mat, 0.125f, 0.75f, 0.125f).color(r, g, b, 192).tex(water.getInterpolatedU(2), water.getInterpolatedV(2)).lightmap(combinedLightIn).normal(0, 1, 0).endVertex();
            builder.pos(mat, 0.125f, 0.75f, 0.875f).color(r, g, b, 192).tex(water.getInterpolatedU(14), water.getInterpolatedV(2)).lightmap(combinedLightIn).normal(0, 1, 0).endVertex();
            builder.pos(mat, 0.875f, 0.75f, 0.875f).color(r, g, b, 192).tex(water.getInterpolatedU(14), water.getInterpolatedV(14)).lightmap(combinedLightIn).normal(0, 1, 0).endVertex();
            builder.pos(mat, 0.875f, 0.75f, 0.125f).color(r, g, b, 192).tex(water.getInterpolatedU(2), water.getInterpolatedV(14)).lightmap(combinedLightIn).normal(0, 1, 0).endVertex();
        }
    }
}
