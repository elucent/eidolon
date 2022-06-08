package elucent.eidolon.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import elucent.eidolon.ClientEvents;
import elucent.eidolon.ritual.Ritual;
import elucent.eidolon.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import com.mojang.math.Vector3f;

public class BrazierTileRenderer extends BlockEntityRenderer<BrazierTileEntity> {
    public BrazierTileRenderer(BlockEntityRenderDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(BrazierTileEntity tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer ir = mc.getItemRenderer();
        if (!tileEntityIn.stack.isEmpty()) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5,0.9375, 0.5);
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(3 * (mc.level.getGameTime() % 360 + partialTicks)));
            ir.renderStatic(tileEntityIn.stack, TransformType.GROUND, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
            matrixStackIn.popPose();
        }

        if (tileEntityIn.ritual != null) {
            MultiBufferSource buffer = ClientEvents.getDelayedRender();

            Ritual ritual = tileEntityIn.ritual;
            float r = ritual.getRed(), g = ritual.getGreen(), b = ritual.getBlue();
            RenderUtil.dragon(matrixStackIn, buffer, 0.5, 3.0, 0.5, 1, r, g, b);
            TextureAtlasSprite sprite = mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(ritual.getSymbol());

            BlockPos pos = tileEntityIn.getBlockPos();
            RenderUtil.litBillboard(matrixStackIn, buffer, 0.5, 3.0, 0.5, r, g, b, sprite);
            RenderUtil.litBillboard(matrixStackIn, buffer, 0.5, 3.0, 0.5, r, g, b, sprite);
        }
    }
}
