package elucent.eidolon.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import elucent.eidolon.block.HorizontalBlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;

public class NecroticFocusTileRenderer extends TileEntityRenderer<NecroticFocusTileEntity> {
    public NecroticFocusTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(NecroticFocusTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer ir = mc.getItemRenderer();
        if (!tileEntityIn.stack.isEmpty()) {
            matrixStackIn.push();
            Direction dir = tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(HorizontalBlockBase.HORIZONTAL_FACING);
            matrixStackIn.translate(0.5 + dir.getXOffset() * 0.25,0.5 + dir.getYOffset() * 0.25, 0.5 + dir.getZOffset() * 0.25);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(3 * (mc.world.getGameTime() % 360 + partialTicks)));
            ir.renderItem(tileEntityIn.stack, TransformType.GROUND, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
            matrixStackIn.pop();
        }
    }
}
