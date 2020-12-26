package elucent.eidolon.tile;

import com.mojang.blaze3d.matrix.MatrixStack;

import elucent.eidolon.ClientEvents;
import elucent.eidolon.ritual.Ritual;
import elucent.eidolon.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;

public class BrazierTileRenderer extends TileEntityRenderer<BrazierTileEntity> {
    public BrazierTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(BrazierTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer ir = mc.getItemRenderer();
        if (!tileEntityIn.stack.isEmpty()) {
            matrixStackIn.push();
            matrixStackIn.translate(0.5,0.9375, 0.5);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(3 * (mc.world.getGameTime() % 360 + partialTicks)));
            ir.renderItem(tileEntityIn.stack, TransformType.GROUND, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
            matrixStackIn.pop();
        }

        if (tileEntityIn.ritual != null) {
            IRenderTypeBuffer buffer = ClientEvents.getDelayedRender();

            Ritual ritual = tileEntityIn.ritual;
            float r = ritual.getRed(), g = ritual.getGreen(), b = ritual.getBlue();
            RenderUtil.dragon(matrixStackIn, buffer, 0.5, 3.0, 0.5, 1, r, g, b);
            TextureAtlasSprite sprite = mc.getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(ritual.getSymbol());

            BlockPos pos = tileEntityIn.getPos();
            RenderUtil.litBillboard(matrixStackIn, buffer, 0.5, 3.0, 0.5, r, g, b, sprite);
            RenderUtil.litBillboard(matrixStackIn, buffer, 0.5, 3.0, 0.5, r, g, b, sprite);
        }
    }
}
