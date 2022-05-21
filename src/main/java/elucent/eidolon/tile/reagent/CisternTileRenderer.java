package elucent.eidolon.tile.reagent;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import elucent.eidolon.ClientEvents;
import elucent.eidolon.block.PillarBlockBase;
import elucent.eidolon.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;

public class CisternTileRenderer implements BlockEntityRenderer<CisternTileEntity> {

    public CisternTileRenderer() {}

    @Override
    public void render(CisternTileEntity tile, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Minecraft mc = Minecraft.getInstance();
        if (tile.getBlockState().getValue(PillarBlockBase.BOTTOM) && tile.downPressure < 1 && tile.tank.getPressure() < 1 / 64f) return;
        if (tile.tank.getContents().amount == 0) return;
        TextureAtlasSprite water = tile.tank.getContents().reagent.getSprite();
        float fill = (float)tile.tank.getContents().amount / tile.tank.getCapacity();
        if (fill > 1) fill = 1;
        float h = 0.625f
            + (tile.getBlockState().getValue(PillarBlockBase.BOTTOM) ? 0.1875f : 0)
            + (tile.getBlockState().getValue(PillarBlockBase.TOP) ? 0.1875f : 0);
        float yoff = tile.getBlockState().getValue(PillarBlockBase.BOTTOM) ? 0 : 0.1875f;
        VertexConsumer builder = ClientEvents.getDelayedRender().getBuffer(RenderUtil.VAPOR_TRANSLUCENT);
        RenderUtil.vaporCube(matrixStackIn, builder, water,
            0.125f, yoff, 0.125f,
            0.875f, yoff + h * fill, 0.875f,
            255, 255, 255, 255, combinedLightIn,
            true, true,
            !tile.getBlockState().getValue(PillarBlockBase.BOTTOM),
            fill < 1 || tile.upPressure == 0, true, true);
    }
}
