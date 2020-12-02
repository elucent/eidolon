package elucent.eidolon.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import elucent.eidolon.Eidolon;
import elucent.eidolon.Events;
import elucent.eidolon.block.HorizontalBlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BookModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class PodiumTileRenderer extends TileEntityRenderer<PodiumTileEntity> {
    public static final ResourceLocation BOOK_TEXTURE = new ResourceLocation(Eidolon.MODID, "entity/codex_model");
    public static final RenderMaterial BOOK_MATERIAL = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, BOOK_TEXTURE);
    private final BookModel model = new BookModel();

    public PodiumTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(PodiumTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Minecraft mc = Minecraft.getInstance();
        if (!tileEntityIn.stack.isEmpty()) {
            matrixStackIn.push();
            float degrees = tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(HorizontalBlockBase.HORIZONTAL_FACING).getHorizontalAngle();
            matrixStackIn.translate(0.5,0.28125, 0.5);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-degrees - 90));
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(67.5f));
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(180));
            IVertexBuilder ivertexbuilder = BOOK_MATERIAL.getBuffer(bufferIn, RenderType::getEntitySolid);
            model.setBookState(0, 1, 1, 1);
            model.renderAll(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.pop();
        }
    }
}
