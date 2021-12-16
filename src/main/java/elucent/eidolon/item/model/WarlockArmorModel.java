package elucent.eidolon.item.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.util.Mth;

public class WarlockArmorModel extends ArmorModel {
	public WarlockArmorModel(ModelPart root) {
		super(root);
	}    

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = HumanoidModel.createMesh(new CubeDeformation(0), 0);
		PartDefinition root = createHumanoidAlias(mesh);

		PartDefinition body = root.getChild("body");
		PartDefinition right_foot = root.getChild("right_foot");
		PartDefinition left_foot = root.getChild("left_foot");
		PartDefinition left_arm = root.getChild("left_arm");
		PartDefinition right_arm = root.getChild("right_arm");
		PartDefinition head = root.getChild("head");
	
		PartDefinition left_boot = left_foot.addOrReplaceChild("left_boot", CubeListBuilder.create().texOffs(0, 84).addBox(-2.5F, 5.5F, -2.5F, 5.0F, 7.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 80).addBox(-2.5F, 9.5F, -3.5F, 5.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.25F, 0.0F, 0.0F, 0.0F, -0.0873F, 0.0F));
		PartDefinition left_boot_cuff = left_boot.addOrReplaceChild("left_boot_cuff", CubeListBuilder.create().texOffs(20, 84).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.0F, 0.0F, 0.0873F));
		PartDefinition right_boot = right_foot.addOrReplaceChild("right_boot", CubeListBuilder.create().texOffs(0, 84).addBox(-2.5F, 5.5F, -2.5F, 5.0F, 7.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 80).addBox(-2.5F, 9.5F, -3.5F, 5.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.25F, 0.0F, 0.0F, 0.0F, 0.0873F, 0.0F));
		PartDefinition right_boot_cuff = right_boot.addOrReplaceChild("right_boot_cuff", CubeListBuilder.create().texOffs(20, 84).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.0F, 0.0F, -0.0873F));
		PartDefinition right_sleeve = right_arm.addOrReplaceChild("right_sleeve", CubeListBuilder.create().texOffs(28, 38).addBox(-4.5F, -2.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.1745F));
		PartDefinition right_cuff = right_sleeve.addOrReplaceChild("right_cuff", CubeListBuilder.create().texOffs(28, 48).addBox(-4.5F, -0.5F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 3.0F, 0.0F, 0.0F, 0.0873F, 0.0873F));
		PartDefinition left_sleeve = left_arm.addOrReplaceChild("left_sleeve", CubeListBuilder.create().texOffs(28, 38).mirror().addBox(-0.5F, -2.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.1745F));
		PartDefinition left_cuff = left_sleeve.addOrReplaceChild("left_cuff", CubeListBuilder.create().texOffs(28, 48).mirror().addBox(-1.5F, -0.5F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, 3.0F, 0.0F, 0.0F, -0.0873F, -0.0873F));
		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -1.0F, -7.0F, 14.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.5F, 0.0F, -0.1745F, 0.0F, -0.0873F));
		PartDefinition hatMid = hat.addOrReplaceChild("hatMid", CubeListBuilder.create().texOffs(0, 16).addBox(-4.5F, -5.75F, -4.5F, 9.0F, 6.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0873F));
		PartDefinition hatUpper = hatMid.addOrReplaceChild("hatUpper", CubeListBuilder.create().texOffs(0, 31).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F, -0.1745F, 0.0F, 0.0436F));
		PartDefinition hatTip = hatUpper.addOrReplaceChild("hatTip", CubeListBuilder.create().texOffs(24, 31).addBox(-1.5F, -4.5F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, -0.2618F, 0.0F, 0.0873F));
		PartDefinition cloak = body.addOrReplaceChild("cloak", CubeListBuilder.create().texOffs(0, 41).addBox(-6.5F, -12.4F, -2.5F, 9.0F, 15.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(28, 56).addBox(-7.0F, -12.5F, -3.0F, 10.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 12.0F, 0.0F));
		PartDefinition left_side = cloak.addOrReplaceChild("left_side", CubeListBuilder.create().texOffs(0, 64).addBox(-2.0F, 0.0F, 0.01F, 2.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -4.5F, -3.0F, 0.0F, 0.0F, 0.1745F));
		PartDefinition right_side = cloak.addOrReplaceChild("right_side", CubeListBuilder.create().texOffs(0, 64).addBox(0.0F, 0.0F, 0.01F, 2.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -4.5F, -3.0F, 0.0F, 0.0F, -0.1745F));
		PartDefinition back_side = cloak.addOrReplaceChild("back_side", CubeListBuilder.create().texOffs(17, 70).addBox(-5.01F, 0.0F, 0.0F, 10.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -4.5F, 1.0F, 0.1745F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 128);
	}

	@Override
	public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = 1.0F;
		if (entity.getFallFlyingTicks() > 4) {
			f = (float)entity.getDeltaMovement().lengthSqr();
			f = f / 0.2F;
			f = f * f * f;
		}
		if (f < 1.0F) {
			f = 1.0F;
		}
		if (slot == EquipmentSlot.CHEST) {
			body.getChild("cloak").getChild("back_side").xRot = 0.1745f + Mth.abs(Mth.cos(limbSwing * 0.6662f) * 0.7f * limbSwingAmount / f);
			body.getChild("cloak").getChild("left_side").zRot = 0.1745f + Mth.cos(limbSwing * 0.6662f + (float)Math.PI) * 0.2f * limbSwingAmount / f + 0.1f * limbSwingAmount / f;
			body.getChild("cloak").getChild("right_side").zRot = -0.1745f - Mth.cos(limbSwing * 0.6662f) * 0.2f * limbSwingAmount / f - 0.1f * limbSwingAmount / f;
		}
	}
}