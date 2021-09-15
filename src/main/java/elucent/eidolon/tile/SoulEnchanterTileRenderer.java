package elucent.eidolon.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import elucent.eidolon.ClientEvents;
import elucent.eidolon.Eidolon;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BookModel;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class SoulEnchanterTileRenderer extends TileEntityRenderer<SoulEnchanterTileEntity> {
    public static final ResourceLocation BOOK_TEXTURE = new ResourceLocation(Eidolon.MODID, "entity/enchanter_book");
    public static final RenderMaterial BOOK_MATERIAL = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, BOOK_TEXTURE);
    private final BookModel model = new BookModel();

    public SoulEnchanterTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(SoulEnchanterTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.push();
        matrixStackIn.translate(0.5D, 0.75D, 0.5D);
        float f = ClientEvents.getClientTicks();
        matrixStackIn.translate(0.0D, (double)(0.1F + MathHelper.sin(f * 0.1F) * 0.01F), 0.0D);

        float f1;
        for(f1 = tileEntityIn.nextPageAngle - tileEntityIn.pageAngle; f1 >= (float)Math.PI; f1 -= ((float)Math.PI * 2F)) {
        }

        while(f1 < -(float)Math.PI) {
            f1 += ((float)Math.PI * 2F);
        }

        float f2 = tileEntityIn.pageAngle + f1 * partialTicks;
        matrixStackIn.rotate(Vector3f.YP.rotation(-f2));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(80.0F));
        float f3 = MathHelper.lerp(partialTicks, tileEntityIn.field_195524_g, tileEntityIn.field_195523_f);
        float f4 = MathHelper.frac(f3 + 0.25F) * 1.6F - 0.3F;
        float f5 = MathHelper.frac(f3 + 0.75F) * 1.6F - 0.3F;
        float f6 = MathHelper.lerp(partialTicks, tileEntityIn.pageTurningSpeed, tileEntityIn.nextPageTurningSpeed);
        this.model.setBookState(f, MathHelper.clamp(f4, 0.0F, 1.0F), MathHelper.clamp(f5, 0.0F, 1.0F), f6);
        IVertexBuilder ivertexbuilder = BOOK_MATERIAL.getBuffer(bufferIn, RenderType::getEntitySolid);
        this.model.renderAll(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();
    }
}
