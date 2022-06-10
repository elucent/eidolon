package elucent.eidolon.client.models.entity;
// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import elucent.eidolon.entity.NecromancerEntity;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.AbstractIllager;


public class NecromancerModel extends EntityModel<NecromancerEntity> {
	private final ModelPart body;
	private final ModelPart clothing;
	private final ModelPart leftLeg;
	private final ModelPart rightLeg;
	private final ModelPart head;
	private final ModelPart arms;
	private final ModelPart rightArm;
	private final ModelPart leftArm;

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition
				.addOrReplaceChild("body", CubeListBuilder.create()
						.texOffs(16, 20)
						.addBox(-4.0F, -24.0F, -3.0F,
								8.0F, 12.0F, 6.0F,
								new CubeDeformation(0.0F)),
						PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition clothing = body.
				addOrReplaceChild("clothing", CubeListBuilder.create()
						.texOffs(0, 38)
						.addBox(-4.0F, 0.0F, -3.0F,
								8.0F, 20.0F, 6.0F,
								new CubeDeformation(0.5F)),
						PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition leftLeg = body.addOrReplaceChild("leftLeg", CubeListBuilder.create()
						.texOffs(0, 22).mirror()
						.addBox(-2.0F, 0.0F, -2.0F,
								4.0F, 12.0F, 4.0F,
								new CubeDeformation(0.0F)).mirror(false),
						PartPose.offset(2.0F, -12.0F, 0.0F));

		PartDefinition rightLeg = body.addOrReplaceChild("rightLeg", CubeListBuilder.create()
				.texOffs(0, 22)
				.addBox(-2.0F, 0.0F, -2.0F,
						4.0F, 12.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offset(-2.0F, -12.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-4.0F, -10.0F, -4.0F,
						8.0F, 10.0F, 8.0F,
						new CubeDeformation(0.0F))
				.texOffs(32, 0)
				.addBox(-4.0F, -12.0F, -4.0F,
						8.0F, 12.0F, 8.0F,
						new CubeDeformation(0.45F))
				.texOffs(24, 0)
				.addBox(-1.0F, -3.0F, -6.0F,
						2.0F, 4.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition arms = body.addOrReplaceChild("arms", CubeListBuilder.create()
						.texOffs(40, 38)
						.addBox(-4.0F, 0.0F, -2.0F,
								8.0F, 4.0F, 4.0F,
								new CubeDeformation(0.0F))
						.texOffs(44, 22).mirror()
						.addBox(4.0F, -4.0F, -2.0F,
								4.0F, 8.0F, 4.0F,
								new CubeDeformation(0.45F)).mirror(false)
						.texOffs(44, 22)
						.addBox(-8.0F, -4.0F, -2.0F,
								4.0F, 8.0F, 4.0F,
								new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -20.0F, -2.0F,
						-0.7854F, 0.0F, 0.0F));

		PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create()
				.texOffs(40, 46)
				.addBox(-2.0F, 0.0F, -2.0F,
						4.0F, 12.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offset(-6.0F, -24.0F, 0.0F));

		PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create()
						.texOffs(40, 46).mirror()
						.addBox(-2.0F, 0.0F, -2.0F,
								4.0F, 12.0F, 4.0F,
								new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(-6.0F, -24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	public NecromancerModel(ModelPart root) {
		this.body = root.getChild("body");
		clothing = body.getChild("clothing");
		leftLeg = body.getChild("leftLeg");
		rightLeg = body.getChild("rightLeg");
		head = body.getChild("head");
		arms = body.getChild("arms");
		rightArm = body.getChild("rightArm");
		leftArm = body.getChild("leftArm");
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

		var pose = entity.getArmPose();

		switch (pose) {
			case ATTACKING -> {
				if (entity.getMainHandItem().isEmpty()) {
					AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, true, this.attackTime, ageInTicks);
				} else {
					AnimationUtils.swingWeaponDown(this.rightArm, this.leftArm, entity, this.attackTime, ageInTicks);
				}
			}
			case SPELLCASTING -> {
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
			}
			case BOW_AND_ARROW -> {
				this.rightArm.yRot = -0.1F + this.head.yRot;
				this.rightArm.xRot = (-(float)Math.PI / 2F) + this.head.xRot;
				this.leftArm.xRot = -0.9424779F + this.head.xRot;
				this.leftArm.yRot = this.head.yRot - 0.4F;
				this.leftArm.zRot = ((float)Math.PI / 2F);
			}
			case CROSSBOW_HOLD -> {
				AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
			}
			case CROSSBOW_CHARGE -> {
				AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, entity, true);
			}
			case CELEBRATING -> {
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
		}

		boolean flag = pose == AbstractIllager.IllagerArmPose.CROSSED;
		this.arms.visible = flag;
		this.leftArm.visible = !flag;
		this.rightArm.visible = !flag;
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		body.render(matrixStack, buffer, packedLight, packedOverlay);
	}
}