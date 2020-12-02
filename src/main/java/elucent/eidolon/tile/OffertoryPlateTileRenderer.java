package elucent.eidolon.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

import java.util.Random;

public class OffertoryPlateTileRenderer extends TileEntityRenderer<OffertoryPlateTileEntity> {
    Random random = new Random();

    public OffertoryPlateTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    protected int getModelCount(ItemStack stack) {
        int i = 1;
        if (stack.getCount() > 48) {
            i = 5;
        } else if (stack.getCount() > 32) {
            i = 4;
        } else if (stack.getCount() > 16) {
            i = 3;
        } else if (stack.getCount() > 1) {
            i = 2;
        }
        return i;
    }

    @Override
    public void render(OffertoryPlateTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer ir = mc.getItemRenderer();

        if (!tileEntityIn.stack.isEmpty()) {
            int i = Item.getIdFromItem(tileEntityIn.stack.getItem()) + tileEntityIn.stack.getDamage();
            random.setSeed((long)i);
            int ct = getModelCount(tileEntityIn.stack);

            matrixStackIn.push();
            matrixStackIn.translate(0.5,0.125, 0.5);
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90));
            for (int j = 0; j < ct; j ++) {
                matrixStackIn.push();
                matrixStackIn.rotate(Vector3f.ZP.rotation(random.nextFloat() * 2 * (float)Math.PI));
                matrixStackIn.translate(0,-0.125, 0);
                ir.renderItem(tileEntityIn.stack, TransformType.GROUND, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
                matrixStackIn.pop();
                matrixStackIn.translate(0.0, 0.0, -0.03125f);
            }
            matrixStackIn.pop();
        }
    }
}
