package elucent.eidolon.item.curio;

import com.mojang.blaze3d.vertex.PoseStack;

import elucent.eidolon.ClientRegistry;
import elucent.eidolon.Eidolon;
import elucent.eidolon.item.model.RavenCloakModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class RavenCloakRenderer implements ICurioRenderer {
	public static final ResourceLocation TEXTURE = new ResourceLocation(Eidolon.MODID, "textures/entity/raven_cloak.png");
	
	RavenCloakModel model = null;

	@Override
	public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext,
			PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer,
			int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw,
			float headPitch) {
		if (model == null) {
			model = new RavenCloakModel(Minecraft.getInstance().getEntityModels().bakeLayer(ClientRegistry.RAVEN_CLOAK_LAYER));
		}
		LivingEntity entity = slotContext.entity();
        ICurioRenderer.followBodyRotations(entity, model);
        ICurioRenderer.followHeadRotations(entity, model.getHead());
        model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        model.renderToBuffer(matrixStack, renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)), light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
	}
}
