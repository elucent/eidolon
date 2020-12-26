package elucent.eidolon.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class ZombieBruteModel extends EntityModel<ZombieBruteEntity> {
	private final ModelRenderer chest;
	private final ModelRenderer right_leg;
	private final ModelRenderer head;
	private final ModelRenderer left_arm;
	private final ModelRenderer right_arm;
	private final ModelRenderer left_leg;

	public ZombieBruteModel() {
		textureWidth = 96;
		textureHeight = 96;

		chest = new ModelRenderer(this);
		chest.setRotationPoint(0.0F, 9.0F, 0.0F);
		chest.setTextureOffset(20, 16).addBox(-6.0F, -15.0F, -3.0F, 12.0F, 15.0F, 6.0F, 0.0F, false);

		right_leg = new ModelRenderer(this);
		right_leg.setRotationPoint(-3.0F, 0.0F, 0.0F);
		chest.addChild(right_leg);
		right_leg.setTextureOffset(0, 16).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 15.0F, 5.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, -15.0F, 0.0F);
		chest.addChild(head);
		head.setTextureOffset(0, 37).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, 0.0F, false);

		left_arm = new ModelRenderer(this);
		left_arm.setRotationPoint(9.0F, -12.0F, 0.0F);
		chest.addChild(left_arm);
		left_arm.setTextureOffset(56, 16).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 13.0F, 5.0F, 0.0F, true);
		left_arm.setTextureOffset(48, 4).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, 0.0F, false);

		right_arm = new ModelRenderer(this);
		right_arm.setRotationPoint(-9.0F, -12.0F, 0.0F);
		chest.addChild(right_arm);
		right_arm.setTextureOffset(48, 4).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, 0.0F, false);
		right_arm.setTextureOffset(56, 16).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 13.0F, 5.0F, 0.0F, false);

		left_leg = new ModelRenderer(this);
		left_leg.setRotationPoint(3.0F, 0.0F, 0.0F);
		chest.addChild(left_leg);
		left_leg.setTextureOffset(0, 16).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 15.0F, 5.0F, 0.0F, true);
	}

	@Override
	public void setRotationAngles(ZombieBruteEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		right_leg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
		left_leg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		right_arm.rotateAngleX = (float)Math.toRadians(-80);
		left_arm.rotateAngleX = (float)Math.toRadians(-80);
		right_arm.rotateAngleZ = (float)Math.toRadians(5);
		left_arm.rotateAngleZ = (float)Math.toRadians(-5);

		head.rotateAngleX = (float)Math.toRadians(headPitch);
		head.rotateAngleY = (float)Math.toRadians(netHeadYaw);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		chest.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}