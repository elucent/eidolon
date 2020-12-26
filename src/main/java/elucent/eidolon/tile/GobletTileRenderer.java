package elucent.eidolon.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;

public class GobletTileRenderer extends TileEntityRenderer<GobletTileEntity> {

    public GobletTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(GobletTileEntity tile, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Minecraft mc = Minecraft.getInstance();

        if (tile.getEntityType() != null) {
            TextureAtlasSprite water = mc.getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE)
                .apply(new ResourceLocation("minecraft", "block/water_still"));
            IVertexBuilder builder = bufferIn.getBuffer(RenderType.getTranslucentNoCrumbling());
            Matrix4f mat = matrixStackIn.getLast().getMatrix();
            builder.pos(mat, 0.375f, 0.46875f, 0.375f).color(192, 16, 32, 224).tex(water.getInterpolatedU(6), water.getInterpolatedV(6)).lightmap(combinedLightIn).normal(0, 1, 0).endVertex();
            builder.pos(mat, 0.375f, 0.46875f, 0.625f).color(192, 16, 32, 224).tex(water.getInterpolatedU(10), water.getInterpolatedV(6)).lightmap(combinedLightIn).normal(0, 1, 0).endVertex();
            builder.pos(mat, 0.625f, 0.46875f, 0.625f).color(192, 16, 32, 224).tex(water.getInterpolatedU(10), water.getInterpolatedV(10)).lightmap(combinedLightIn).normal(0, 1, 0).endVertex();
            builder.pos(mat, 0.625f, 0.46875f, 0.375f).color(192, 16, 32, 224).tex(water.getInterpolatedU(6), water.getInterpolatedV(10)).lightmap(combinedLightIn).normal(0, 1, 0).endVertex();
        }
    }
}
