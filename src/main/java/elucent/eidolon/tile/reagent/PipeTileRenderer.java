package elucent.eidolon.tile.reagent;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import elucent.eidolon.ClientEvents;
import elucent.eidolon.block.PipeBlock;
import elucent.eidolon.util.RenderUtil;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;

public class PipeTileRenderer implements BlockEntityRenderer<PipeTileEntity> {

    public PipeTileRenderer() {}

    public boolean attached(BlockState b, Direction d) {
        return b.getValue(PipeBlock.IN) == d && b.getValue(PipeBlock.IN_ATTACHED)
            || b.getValue(PipeBlock.OUT) == d && b.getValue(PipeBlock.OUT_ATTACHED);
    }

    static final float L = 0.40625f, U = 0.59375f, W = U - L;

    @Override
    public void render(PipeTileEntity tile, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Minecraft mc = Minecraft.getInstance();
        if (tile.tank.getContents().amount == 0) return;
        float fill = (float)tile.tank.getContents().amount / tile.tank.getCapacity();
        if (fill > 1) fill = 1;
        BlockState state = tile.getBlockState();
        TextureAtlasSprite water = tile.tank.getContents().reagent.getSprite();
        VertexConsumer builder = ClientEvents.getDelayedRender().getBuffer(RenderUtil.VAPOR_TRANSLUCENT);
        boolean nx = state.getValue(PipeBlock.IN) == Direction.WEST || state.getValue(PipeBlock.OUT) == Direction.WEST,
                px = state.getValue(PipeBlock.IN) == Direction.EAST || state.getValue(PipeBlock.OUT) == Direction.EAST,
                ny = state.getValue(PipeBlock.IN) == Direction.DOWN || state.getValue(PipeBlock.OUT) == Direction.DOWN,
                py = state.getValue(PipeBlock.IN) == Direction.UP || state.getValue(PipeBlock.OUT) == Direction.UP,
                nz = state.getValue(PipeBlock.IN) == Direction.NORTH || state.getValue(PipeBlock.OUT) == Direction.NORTH,
                pz = state.getValue(PipeBlock.IN) == Direction.SOUTH || state.getValue(PipeBlock.OUT) == Direction.SOUTH;
        if (py && ny) {
            RenderUtil.vaporCube(matrixStackIn, builder, water,
                L, L, L, U, U, U,
                255, 255, 255, 255, combinedLightIn,
                !nx, !px, !ny, !py, !nz, !pz);
        }
        else if (py) {
            if (fill < 1) RenderUtil.vaporCube(matrixStackIn, builder, water,
                    L, L + fill * W, L, U, U, U,
                    255, 255, 255, 255, combinedLightIn,
                    true, true, !ny, !py, true, true);
            RenderUtil.vaporCube(matrixStackIn, builder, water,
                L, L, L, U, L + fill * W, U,
                255, 255, 255, 255, combinedLightIn,
                !nx, !px, !ny, !py, !nz, !pz);
        }
        else {
            RenderUtil.vaporCube(matrixStackIn, builder, water,
                L, L, L, U, L + fill * W, U,
                255, 255, 255, 255, combinedLightIn,
                !nx, !px, !ny, !py, !nz, !pz);
        }
        if (nx) {
            RenderUtil.vaporCube(matrixStackIn, builder, water,
                0, L, L, L, L + W * fill, U,
                255, 255, 255, 255, combinedLightIn,
                !attached(state, Direction.WEST), false, true, true, true, true);
        }
        if (px) {
            RenderUtil.vaporCube(matrixStackIn, builder, water,
                U, L, L, 1, L + W * fill, U,
                255, 255, 255, 255, combinedLightIn,
                false, !attached(state, Direction.EAST), true, true, true, true);
        }
        if (ny) {
            RenderUtil.vaporCube(matrixStackIn, builder, water,
                L, 0, L, U, L, U,
                255, 255, 255, 255, combinedLightIn,
                true, true, !attached(state, Direction.DOWN), false, true, true);
        }
        if (py) {
            RenderUtil.vaporCube(matrixStackIn, builder, water,
                L, U, L, U, 1, U,
                255, 255, 255, 255, combinedLightIn,
                true, true, false, !attached(state, Direction.UP), true, true);
        }
        if (nz) {
            RenderUtil.vaporCube(matrixStackIn, builder, water,
                L, L, 0, U, L + W * fill, L,
                255, 255, 255, 255, combinedLightIn,
                true, true, true, true, !attached(state, Direction.NORTH), false);
        }
        if (pz) {
            RenderUtil.vaporCube(matrixStackIn, builder, water,
                L, L, U, U, L + W * fill, 1,
                255, 255, 255, 255, combinedLightIn,
                true, true, true, true, false, !attached(state, Direction.SOUTH));
        }
    }
}
