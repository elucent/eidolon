package elucent.eidolon.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.Mth;

public class ZombieBruteModel extends EntityModel<ZombieBruteEntity> {
	private final ModelRenderer chest;
	private final ModelRenderer right_leg;
	private final ModelRenderer head;
	private final ModelRenderer left_arm;
	private final ModelRenderer right_arm;
	private final ModelRenderer left_leg;

	public ZombieBruteModel() {
		texWidth = 96;
		texHeight = 96;

		chest = new ModelRenderer(this);
		chest.setPos(0.0F, 9.0F, 0.0F);
		chest.texOffs(20, 16).addBox(-6.0F, -15.0F, -3.0F, 12.0F, 15.0F, 6.0F, 0.0F, false);

		right_leg = new ModelRenderer(this);
		right_leg.setPos(-3.0F, 0.0F, 0.0F);
		chest.addChild(right_leg);
		right_leg.texOffs(0, 16).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 15.0F, 5.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setPos(0.0F, -15.0F, 0.0F);
		chest.addChild(head);
		head.texOffs(0, 37).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, 0.0F, false);

		left_arm = new ModelRenderer(this);
		left_arm.setPos(9.0F, -12.0F, 0.0F);
		chest.addChild(left_arm);
		left_arm.texOffs(56, 16).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 13.0F, 5.0F, 0.0F, true);
		left_arm.texOffs(48, 4).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, 0.0F, false);

		right_arm = new ModelRenderer(this);
		right_arm.setPos(-9.0F, -12.0F, 0.0F);
		chest.addChild(right_arm);
		right_arm.texOffs(48, 4).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, 0.0F, false);
		right_arm.texOffs(56, 16).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 13.0F, 5.0F, 0.0F, false);

		left_leg = new ModelRenderer(this);
		left_leg.setPos(3.0F, 0.0F, 0.0F);
		chest.addChild(left_leg);
		left_leg.texOffs(0, 16).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 15.0F, 5.0F, 0.0F, true);
	}

	@Override
	public void setupAnim(ZombieBruteEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		right_leg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
		left_leg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		right_arm.xRot = (float)Math.toRadians(-80);
		left_arm.xRot = (float)Math.toRadians(-80);
		right_arm.zRot = (float)Math.toRadians(5);
		left_arm.zRot = (float)Math.toRadians(-5);

		head.xRot = (float)Math.toRadians(headPitch);
		head.yRot = (float)Math.toRadians(netHeadYaw);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		chest.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}