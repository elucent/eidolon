package elucent.eidolon.entity;
// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.util.Mth;

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
		texWidth = 64;
		texHeight = 64;

		body = new ModelRenderer(this);
		body.setPos(0.0F, 24.0F, 0.0F);
		body.texOffs(16, 20).addBox(-4.0F, -24.0F, -3.0F, 8.0F, 12.0F, 6.0F, delta, false);

		clothing = new ModelRenderer(this);
		clothing.setPos(0.0F, -24.0F, 0.0F);
		body.addChild(clothing);
		clothing.texOffs(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 20.0F, 6.0F, delta + 0.5F, false);
		// clothing.setTextureOffset(64, 0).addBox(-4.5F, 17.0F, -3.5F, 9.0F, 4.0F, 7.0F, delta + 0.5F, false);

		leftLeg = new ModelRenderer(this);
		leftLeg.setPos(2.0F, -12.0F, 0.0F);
		body.addChild(leftLeg);
		leftLeg.texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, delta, true);

		rightLeg = new ModelRenderer(this);
		rightLeg.setPos(-2.0F, -12.0F, 0.0F);
		body.addChild(rightLeg);
		rightLeg.texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, delta, false);

		head = new ModelRenderer(this);
		head.setPos(0.0F, -24.0F, 0.0F);
		body.addChild(head);
		head.texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, delta, false);
		head.texOffs(32, 0).addBox(-4.0F, -12.0F, -4.0F, 8.0F, 12.0F, 8.0F, delta + 0.45F, false);
		head.texOffs(24, 0).addBox(-1.0F, -3.0F, -6.0F, 2.0F, 4.0F, 2.0F, delta, false);

		arms = new ModelRenderer(this);
		arms.setPos(0.0F, -20.0F, -2.0F);
		body.addChild(arms);
		setRotationAngle(arms, -0.7854F, 0.0F, 0.0F);
		arms.texOffs(40, 38).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 4.0F, 4.0F, delta, false);
		arms.texOffs(44, 22).addBox(4.0F, -4.0F, -2.0F, 4.0F, 8.0F, 4.0F, delta, true);
		arms.texOffs(44, 22).addBox(-8.0F, -4.0F, -2.0F, 4.0F, 8.0F, 4.0F, delta, false);

		rightArm = new ModelRenderer(this);
		rightArm.setPos(-6.0F, -24.0F, 0.0F);
		body.addChild(rightArm);
		rightArm.texOffs(40, 46).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, delta, false);

		leftArm = new ModelRenderer(this);
		leftArm.setPos(6.0F, -24.0F, 0.0F);
		body.addChild(leftArm);
		leftArm.texOffs(40, 46).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, delta, true);
	}

	@Override
	public void setupAnim(NecromancerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
		this.head.xRot = headPitch * ((float)Math.PI / 180F);
		this.arms.xRot = -0.75F;
		if (this.riding) {
			this.rightArm.xRot = (-(float)Math.PI / 5F);
			this.rightArm.yRot = 0.0F;
			this.rightArm.zRot = 0.0F;
			this.leftArm.xRot = (-(float)Math.PI / 5F);
			this.leftArm.yRot = 0.0F;
			this.leftArm.zRot = 0.0F;
			this.rightLeg.xRot = -1.4137167F;
			this.rightLeg.yRot = ((float)Math.PI / 10F);
			this.rightLeg.zRot = 0.07853982F;
			this.leftLeg.xRot = -1.4137167F;
			this.leftLeg.yRot = (-(float)Math.PI / 10F);
			this.leftLeg.zRot = -0.07853982F;
		} else {
			this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F;
			this.rightArm.yRot = 0.0F;
			this.rightArm.zRot = 0.0F;
			this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
			this.leftArm.yRot = 0.0F;
			this.leftArm.zRot = 0.0F;
			this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
			this.rightLeg.yRot = 0.0F;
			this.rightLeg.zRot = 0.0F;
			this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
			this.leftLeg.yRot = 0.0F;
			this.leftLeg.zRot = 0.0F;
		}

		AbstractIllagerEntity.ArmPose armpose = entity.getArmPose();
		if (armpose == AbstractIllagerEntity.ArmPose.ATTACKING) {
			if (entity.getMainHandItem().isEmpty()) {
				ModelHelper.animateZombieArms(this.leftArm, this.rightArm, true, this.attackTime, ageInTicks);
			} else {
				ModelHelper.swingWeaponDown(this.rightArm, this.leftArm, entity, this.attackTime, ageInTicks);
			}
		} else if (armpose == AbstractIllagerEntity.ArmPose.SPELLCASTING) {
			this.rightArm.z = 0.0F;
			this.rightArm.x = -5.0F;
			this.leftArm.z = 0.0F;
			this.leftArm.x = 5.0F;
			this.rightArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.25F;
			this.leftArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.25F;
			this.rightArm.zRot = 2.3561945F;
			this.leftArm.zRot = -2.3561945F;
			this.rightArm.yRot = 0.0F;
			this.leftArm.yRot = 0.0F;
		} else if (armpose == AbstractIllagerEntity.ArmPose.BOW_AND_ARROW) {
			this.rightArm.yRot = -0.1F + this.head.yRot;
			this.rightArm.xRot = (-(float)Math.PI / 2F) + this.head.xRot;
			this.leftArm.xRot = -0.9424779F + this.head.xRot;
			this.leftArm.yRot = this.head.yRot - 0.4F;
			this.leftArm.zRot = ((float)Math.PI / 2F);
		} else if (armpose == AbstractIllagerEntity.ArmPose.CROSSBOW_HOLD) {
			ModelHelper.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
		} else if (armpose == AbstractIllagerEntity.ArmPose.CROSSBOW_CHARGE) {
			ModelHelper.animateCrossbowCharge(this.rightArm, this.leftArm, entity, true);
		} else if (armpose == AbstractIllagerEntity.ArmPose.CELEBRATING) {
			this.rightArm.z = 0.0F;
			this.rightArm.x = -5.0F;
			this.rightArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.05F;
			this.rightArm.zRot = 2.670354F;
			this.rightArm.yRot = 0.0F;
			this.leftArm.z = 0.0F;
			this.leftArm.x = 5.0F;
			this.leftArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.05F;
			this.leftArm.zRot = -2.3561945F;
			this.leftArm.yRot = 0.0F;
		}

		boolean flag = armpose == AbstractIllagerEntity.ArmPose.CROSSED;
		this.arms.visible = flag;
		this.leftArm.visible = !flag;
		this.rightArm.visible = !flag;
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		body.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}