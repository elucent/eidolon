package elucent.eidolon.item;

import com.mojang.blaze3d.vertex.PoseStack;

import elucent.eidolon.Eidolon;
import elucent.eidolon.item.model.UnholySymbolModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.world.item.Item.Properties;

public class UnholySymbolItem extends ItemBase {
    public UnholySymbolItem(Properties builderIn) {
        super(builderIn);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        return stack.copy();
    }

    @OnlyIn(Dist.CLIENT)
    public static class UnholySymbolItemRenderer extends BlockEntityWithoutLevelRenderer {
        static final UnholySymbolModel MODEL = new UnholySymbolModel();

        public UnholySymbolItemRenderer(BlockEntityRenderDispatcher berd, EntityModelSet ems) {
        	super(berd, ems);
        }

        @Override
        public void renderByItem(ItemStack stack, ItemTransforms.TransformType type, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
            RenderType rendertype = RenderType.entityCutoutNoCullZOffset(new ResourceLocation(Eidolon.MODID, "textures/item/unholy_symbol_base.png"));
            matrixStack.pushPose();
            matrixStack.scale(1.0F, -1.0F, -1.0F);
            MODEL.renderToBuffer(matrixStack, buffer.getBuffer(rendertype), combinedLight, combinedOverlay, 1, 1, 1, 1);
            matrixStack.popPose();
        }
    }
}
