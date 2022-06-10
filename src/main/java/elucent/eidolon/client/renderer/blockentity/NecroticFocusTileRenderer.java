package elucent.eidolon.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import elucent.eidolon.block.HorizontalBlockBase;
import elucent.eidolon.tile.NecroticFocusTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;

public class NecroticFocusTileRenderer extends ModBlockEntityRenderer<NecroticFocusTileEntity> {
    public NecroticFocusTileRenderer(BlockEntityRenderDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(NecroticFocusTileEntity tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer ir = mc.getItemRenderer();
        if (!tileEntityIn.stack.isEmpty()) {
            matrixStackIn.pushPose();
            Direction dir = tileEntityIn.getLevel().getBlockState(tileEntityIn.getBlockPos()).getValue(HorizontalBlockBase.HORIZONTAL_FACING);
            matrixStackIn.translate(0.5 + dir.getStepX() * 0.25,0.5 + dir.getStepY() * 0.25, 0.5 + dir.getStepZ() * 0.25);
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(3 * (mc.level.getGameTime() % 360 + partialTicks)));
            ir.renderStatic(tileEntityIn.stack, TransformType.GROUND, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn, 0);
            matrixStackIn.popPose();
        }
    }
}
