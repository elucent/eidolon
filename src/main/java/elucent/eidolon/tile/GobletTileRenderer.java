package elucent.eidolon.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Matrix4f;

public class GobletTileRenderer implements BlockEntityRenderer<GobletTileEntity> {

    public GobletTileRenderer() {}

    @Override
    public void render(GobletTileEntity tile, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Minecraft mc = Minecraft.getInstance();

        if (tile.getEntityType() != null) {
            TextureAtlasSprite water = mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
                .apply(new ResourceLocation("minecraft", "block/water_still"));
            VertexConsumer builder = bufferIn.getBuffer(RenderType.translucentNoCrumbling());
            Matrix4f mat = matrixStackIn.last().pose();
            builder.vertex(mat, 0.375f, 0.46875f, 0.375f).color(192, 16, 32, 224).uv(water.getU(6), water.getV(6)).uv2(combinedLightIn).normal(0, 1, 0).endVertex();
            builder.vertex(mat, 0.375f, 0.46875f, 0.625f).color(192, 16, 32, 224).uv(water.getU(10), water.getV(6)).uv2(combinedLightIn).normal(0, 1, 0).endVertex();
            builder.vertex(mat, 0.625f, 0.46875f, 0.625f).color(192, 16, 32, 224).uv(water.getU(10), water.getV(10)).uv2(combinedLightIn).normal(0, 1, 0).endVertex();
            builder.vertex(mat, 0.625f, 0.46875f, 0.375f).color(192, 16, 32, 224).uv(water.getU(6), water.getV(10)).uv2(combinedLightIn).normal(0, 1, 0).endVertex();
        }
    }
}
