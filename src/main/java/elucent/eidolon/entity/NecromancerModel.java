package elucent.eidolon.entity;
// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.util.math.MathHelper;

public class NecromancerModel extends EntityModel<NecromancerEntity> {
	private final ModelRenderer body;
	private final ModelRenderer clothing;
	private final ModelRenderer leftLeg;
	private final ModelRenderer rightLeg;
	private final ModelRenderer head;
	private final ModelRenderer arms;
	private final ModelRenderer rightArm;
	private final ModelRenderer leftArm;

	public NecromancerModel(float delta) {
		textureWidth = 64;
		textureHeight = 64;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 24.0F, 0.0F);
		body.setTextureOffset(16, 20).addBox(-4.0F, -24.0F, -3.0F, 8.0F, 12.0F, 6.0F, delta, false);

		clothing = new ModelRenderer(this);
		clothing.setRotationPoint(0.0F, -24.0F, 0.0F);
		body.addChild(clothing);
		clothing.setTextureOffset(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 20.0F, 6.0F, delta + 0.5F, false);
		// clothing.setTextureOffset(64, 0).addBox(-4.5F, 17.0F, -3.5F, 9.0F, 4.0F, 7.0F, delta + 0.5F, false);

		leftLeg = new ModelRenderer(this);
		leftLeg.setRotationPoint(2.0F, -12.0F, 0.0F);
		body.addChild(leftLeg);
		leftLeg.setTextureOffset(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, delta, true);

		rightLeg = new ModelRenderer(this);
		rightLeg.setRotationPoint(-2.0F, -12.0F, 0.0F);
		body.addChild(rightLeg);
		rightLeg.setTextureOffset(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, delta, false);

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, -24.0F, 0.0F);
		body.addChild(head);
		head.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, delta, false);
		head.setTextureOffset(32, 0).addBox(-4.0F, -12.0F, -4.0F, 8.0F, 12.0F, 8.0F, delta + 0.45F, false);
		head.setTextureOffset(24, 0).addBox(-1.0F, -3.0F, -6.0F, 2.0F, 4.0F, 2.0F, delta, false);

		arms = new ModelRenderer(this);
		arms.setRotationPoint(0.0F, -20.0F, -2.0F);
		body.addChild(arms);
		setRotationAngle(arms, -0.7854F, 0.0F, 0.0F);
		arms.setTextureOffset(40, 38).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 4.0F, 4.0F, delta, false);
		arms.setTextureOffset(44, 22).addBox(4.0F, -4.0F, -2.0F, 4.0F, 8.0F, 4.0F, delta, true);
		arms.setTextureOffset(44, 22).addBox(-8.0F, -4.0F, -2.0F, 4.0F, 8.0F, 4.0F, delta, false);

		rightArm = new ModelRenderer(this);
		rightArm.setRotationPoint(-6.0F, -24.0F, 0.0F);
		body.addChild(rightArm);
		rightArm.setTextureOffset(40, 46).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, delta, false);

		leftArm = new ModelRenderer(this);
		leftArm.setRotationPoint(6.0F, -24.0F, 0.0F);
		body.addChild(leftArm);
		leftArm.setTextureOffset(40, 46).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, delta, true);
	}

	@Override
	public void setRotationAngles(NecromancerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		this.head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
		this.head.rotateAngleX = headPitch * ((float)Math.PI / 180F);
		this.arms.rotateAngleX = -0.75F;
		if (this.isSitting) {
			this.rightArm.rotateAngleX = (-(float)Math.PI / 5F);
			this.rightArm.rotateAngleY = 0.0F;
			this.rightArm.rotateAngleZ = 0.0F;
			this.leftArm.rotateAngleX = (-(float)Math.PI / 5F);
			this.leftArm.rotateAngleY = 0.0F;
			this.leftArm.rotateAngleZ = 0.0F;
			this.rightLeg.rotateAngleX = -1.4137167F;
			this.rightLeg.rotateAngleY = ((float)Math.PI / 10F);
			this.rightLeg.rotateAngleZ = 0.07853982F;
			this.leftLeg.rotateAngleX = -1.4137167F;
			this.leftLeg.rotateAngleY = (-(float)Math.PI / 10F);
			this.leftLeg.rotateAngleZ = -0.07853982F;
		} else {
			this.rightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F;
			this.rightArm.rotateAngleY = 0.0F;
			this.rightArm.rotateAngleZ = 0.0F;
			this.leftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
			this.leftArm.rotateAngleY = 0.0F;
			this.leftArm.rotateAngleZ = 0.0F;
			this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
			this.rightLeg.rotateAngleY = 0.0F;
			this.rightLeg.rotateAngleZ = 0.0F;
			this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
			this.leftLeg.rotateAngleY = 0.0F;
			this.leftLeg.rotateAngleZ = 0.0F;
		}

		AbstractIllagerEntity.ArmPose armpose = entity.getArmPose();
		if (armpose == AbstractIllagerEntity.ArmPose.ATTACKING) {
			if (entity.getHeldItemMainhand().isEmpty()) {
				ModelHelper.func_239105_a_(this.leftArm, this.rightArm, true, this.swingProgress, ageInTicks);
			} else {
				ModelHelper.func_239103_a_(this.rightArm, this.leftArm, entity, this.swingProgress, ageInTicks);
			}
		} else if (armpose == AbstractIllagerEntity.ArmPose.SPELLCASTING) {
			this.rightArm.rotationPointZ = 0.0F;
			this.rightArm.rotationPointX = -5.0F;
			this.leftArm.rotationPointZ = 0.0F;
			this.leftArm.rotationPointX = 5.0F;
			this.rightArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
			this.leftArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
			this.rightArm.rotateAngleZ = 2.3561945F;
			this.leftArm.rotateAngleZ = -2.3561945F;
			this.rightArm.rotateAngleY = 0.0F;
			this.leftArm.rotateAngleY = 0.0F;
		} else if (armpose == AbstractIllagerEntity.ArmPose.BOW_AND_ARROW) {
			this.rightArm.rotateAngleY = -0.1F + this.head.rotateAngleY;
			this.rightArm.rotateAngleX = (-(float)Math.PI / 2F) + this.head.rotateAngleX;
			this.leftArm.rotateAngleX = -0.9424779F + this.head.rotateAngleX;
			this.leftArm.rotateAngleY = this.head.rotateAngleY - 0.4F;
			this.leftArm.rotateAngleZ = ((float)Math.PI / 2F);
		} else if (armpose == AbstractIllagerEntity.ArmPose.CROSSBOW_HOLD) {
			ModelHelper.func_239104_a_(this.rightArm, this.leftArm, this.head, true);
		} else if (armpose == AbstractIllagerEntity.ArmPose.CROSSBOW_CHARGE) {
			ModelHelper.func_239102_a_(this.rightArm, this.leftArm, entity, true);
		} else if (armpose == AbstractIllagerEntity.ArmPose.CELEBRATING) {
			this.rightArm.rotationPointZ = 0.0F;
			this.rightArm.rotationPointX = -5.0F;
			this.rightArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.05F;
			this.rightArm.rotateAngleZ = 2.670354F;
			this.rightArm.rotateAngleY = 0.0F;
			this.leftArm.rotationPointZ = 0.0F;
			this.leftArm.rotationPointX = 5.0F;
			this.leftArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.05F;
			this.leftArm.rotateAngleZ = -2.3561945F;
			this.leftArm.rotateAngleY = 0.0F;
		}

		boolean flag = armpose == AbstractIllagerEntity.ArmPose.CROSSED;
		this.arms.showModel = flag;
		this.leftArm.showModel = !flag;
		this.rightArm.showModel = !flag;
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		body.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}