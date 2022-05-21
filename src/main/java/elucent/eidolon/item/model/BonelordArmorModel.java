package elucent.eidolon.item.model;

import elucent.eidolon.Registry;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;
import top.theillusivec4.curios.api.CuriosApi;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.util.Mth;

public class BonelordArmorModel extends ArmorModel {
	public BonelordArmorModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = HumanoidModel.createMesh(new CubeDeformation(0), 0);
		PartDefinition root = createHumanoidAlias(mesh);

		PartDefinition body = root.getChild("body");
		PartDefinition right_leg = root.getChild("right_legging");
		PartDefinition left_leg = root.getChild("left_legging");
		PartDefinition left_arm = root.getChild("left_arm");
		PartDefinition right_arm = root.getChild("right_arm");
		PartDefinition head = root.getChild("head");

		PartDefinition chest = body.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(0, 30).addBox(-5.5F, -24.0F, -4.5F, 11.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
			.texOffs(38, 30).addBox(-1.5F, -24.5F, 2.0F, 3.0F, 11.0F, 3.0F, new CubeDeformation(0.0F))
			.texOffs(0, 44).addBox(-5.0F, -18.0F, -3.25F, 10.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition hood = chest.addOrReplaceChild("hood", CubeListBuilder.create().texOffs(38, 44).addBox(-6.5F, 0.0F, 0.0F, 13.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -28.0F, 5.0F, -0.2618F, 0.0F, 0.0F));
		PartDefinition cape = hood.addOrReplaceChild("cape", CubeListBuilder.create().texOffs(68, 9).addBox(-6.0F, 0.0F, 0.0F, 12.0F, 16.0F, 2.0F, new CubeDeformation(0.0F))
			.texOffs(52, 9).addBox(2.0F, 16.0F, 0.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
			.texOffs(52, 9).mirror().addBox(-6.0F, 16.0F, 0.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.3927F, 0.0F, 0.0F));
		PartDefinition left_point = cape.addOrReplaceChild("left_point", CubeListBuilder.create().texOffs(50, 29).addBox(0.0F, -1.0F, -1.5F, 8.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 14.75F, 1.0F, 0.0F, 0.0F, 0.5236F));
		PartDefinition right_point = cape.addOrReplaceChild("right_point", CubeListBuilder.create().texOffs(50, 29).mirror().addBox(-8.0F, -1.0F, -1.51F, 8.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.5F, 14.75F, 1.0F, 0.0F, 0.0F, -0.5236F));
		PartDefinition upper_right_rib = chest.addOrReplaceChild("upper_right_rib", CubeListBuilder.create().texOffs(36, 8).addBox(-5.0F, -2.0F, -1.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
			.texOffs(45, 0).addBox(-7.0F, -2.0F, -4.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -15.0F, 2.0F, 0.0F, 0.2618F, 0.2618F));
		PartDefinition upper_right_rib_tip = upper_right_rib.addOrReplaceChild("upper_right_rib_tip", CubeListBuilder.create().texOffs(36, 8).addBox(-5.0F, -0.99F, -2.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -1.0F, -4.0F, -3.1416F, -0.8727F, 3.1416F));
		PartDefinition mid_right_rib = chest.addOrReplaceChild("mid_right_rib", CubeListBuilder.create().texOffs(36, 8).addBox(-5.0F, -2.0F, -1.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
			.texOffs(45, 0).addBox(-7.0F, -2.0F, -4.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -14.25F, 2.0F, 0.0F, 0.2618F, -0.1309F));
		PartDefinition mid_right_rib_tip = mid_right_rib.addOrReplaceChild("mid_right_rib_tip", CubeListBuilder.create().texOffs(36, 8).addBox(-5.0F, -0.99F, -2.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -1.0F, -4.0F, -3.1416F, -0.8727F, 3.1416F));
		PartDefinition lower_right_rib = chest.addOrReplaceChild("lower_right_rib", CubeListBuilder.create().texOffs(36, 8).addBox(-5.0F, -2.0F, -1.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
			.texOffs(45, 0).addBox(-7.0F, -2.0F, -4.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -13.5F, 2.0F, 0.0F, 0.2618F, -0.5236F));
		PartDefinition lower_right_rib_tip = lower_right_rib.addOrReplaceChild("lower_right_rib_tip", CubeListBuilder.create().texOffs(36, 8).addBox(-5.0F, -0.99F, -2.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -1.0F, -4.0F, -3.1416F, -0.8727F, 3.1416F));
		PartDefinition upper_left_rib = chest.addOrReplaceChild("upper_left_rib", CubeListBuilder.create().texOffs(36, 8).mirror().addBox(0.0F, -2.0F, -1.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
			.texOffs(45, 0).mirror().addBox(5.0F, -2.0F, -4.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.5F, -15.0F, 2.0F, 0.0F, -0.2618F, -0.2618F));
		PartDefinition upper_left_rib_tip = upper_left_rib.addOrReplaceChild("upper_left_rib_tip", CubeListBuilder.create().texOffs(36, 8).mirror().addBox(0.0F, -1.99F, -2.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(7.0F, 0.0F, -4.0F, -3.1416F, 0.8727F, -3.1416F));
		PartDefinition mid_left_rib = chest.addOrReplaceChild("mid_left_rib", CubeListBuilder.create().texOffs(36, 8).mirror().addBox(0.0F, -2.0F, -1.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
			.texOffs(45, 0).mirror().addBox(5.0F, -2.0F, -4.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.5F, -14.25F, 2.0F, 0.0F, -0.2618F, 0.1309F));
		PartDefinition mid_left_rib_tip = mid_left_rib.addOrReplaceChild("mid_left_rib_tip", CubeListBuilder.create().texOffs(36, 8).mirror().addBox(0.0F, -0.99F, -2.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(7.0F, -1.0F, -4.0F, -3.1416F, 0.8727F, -3.1416F));
		PartDefinition lower_left_rib = chest.addOrReplaceChild("lower_left_rib", CubeListBuilder.create().texOffs(36, 8).mirror().addBox(0.0F, -2.0F, -1.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
			.texOffs(45, 0).mirror().addBox(5.0F, -2.0F, -4.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.5F, -13.5F, 2.0F, 0.0F, -0.2618F, 0.5236F));
		PartDefinition lower_left_rib_tip = lower_left_rib.addOrReplaceChild("lower_left_rib_tip", CubeListBuilder.create().texOffs(36, 8).mirror().addBox(0.0F, -0.99F, -2.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(7.0F, -1.0F, -4.0F, -3.1416F, 0.8727F, -3.1416F));
		PartDefinition coccyx = chest.addOrReplaceChild("coccyx", CubeListBuilder.create().texOffs(30, 30).addBox(-1.0F, 0.0F, -2.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -13.0F, 5.0F, -0.1745F, 0.0F, 0.0F));
		PartDefinition left_greave = left_leg.addOrReplaceChild("left_greave", CubeListBuilder.create().texOffs(72, 27).addBox(0.0F, -0.5F, 0.0F, 4.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 2.0F, -3.0F, 0.0F, -0.0873F, 0.0F));
		PartDefinition left_knee = left_greave.addOrReplaceChild("left_knee", CubeListBuilder.create().texOffs(84, 27).addBox(-4.5F, -0.5F, 0.0F, 5.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, -2.0F, -0.5F, 0.0F, -0.0873F, 0.0F));
		PartDefinition right_greave = right_leg.addOrReplaceChild("right_greave", CubeListBuilder.create().texOffs(72, 27).mirror().addBox(-4.0F, -0.5F, 0.0F, 4.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.0F, 2.0F, -3.0F, 0.0F, 0.0873F, 0.0F));
		PartDefinition right_knee = right_greave.addOrReplaceChild("right_knee", CubeListBuilder.create().texOffs(84, 27).mirror().addBox(-0.5F, -0.5F, 0.0F, 5.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.5F, -2.0F, -0.5F, 0.0F, 0.0873F, 0.0F));
		PartDefinition right_shoulder = right_arm.addOrReplaceChild("right_shoulder", CubeListBuilder.create().texOffs(44, 17).mirror().addBox(0.0F, 0.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.5F, -2.0F, 0.0F));
		PartDefinition left_shoulder = left_arm.addOrReplaceChild("left_shoulder", CubeListBuilder.create().texOffs(44, 17).addBox(-6.0F, 0.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(4.5F, -2.0F, 0.0F));
		PartDefinition helm = head.addOrReplaceChild("helm", CubeListBuilder.create().texOffs(0, 16).addBox(-5.5F, -3.0F, -5.5F, 11.0F, 3.0F, 11.0F, new CubeDeformation(0.0F))
			.texOffs(0, 0).addBox(-4.5F, -9.0F, -4.5F, 9.0F, 7.0F, 9.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 1.0F, 0.0F));
		PartDefinition cowl = helm.addOrReplaceChild("cowl", CubeListBuilder.create().texOffs(33, 16).addBox(-2.0F, 0.0F, -1.5F, 5.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -3.5F, -4.5F, -0.2618F, 0.0F, 0.0F));
		PartDefinition right_prong = helm.addOrReplaceChild("right_prong", CubeListBuilder.create().texOffs(27, 0).mirror().addBox(-5.0F, -2.0F, 0.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.5F, -3.0F, -4.0F, 0.0F, 0.5236F, 0.5236F));
		PartDefinition back_right_prong = helm.addOrReplaceChild("back_right_prong", CubeListBuilder.create().texOffs(27, 4).mirror().addBox(-7.0F, -2.0F, 0.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.5F, -5.0F, 1.0F, 0.0F, 1.0472F, 0.7854F));
		PartDefinition left_prong = helm.addOrReplaceChild("left_prong", CubeListBuilder.create().texOffs(27, 0).addBox(0.0F, -2.0F, 0.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, -3.0F, -4.0F, 0.0F, -0.5236F, -0.5236F));
		PartDefinition back_left_prong = helm.addOrReplaceChild("back_left_prong", CubeListBuilder.create().texOffs(27, 4).addBox(0.0F, -2.0F, 0.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, -5.0F, 1.0F, 0.0F, -1.0472F, -0.7854F));

		return LayerDefinition.create(mesh, 128, 64);
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
			body.getChild("chest").getChild("hood").getChild("cape").xRot = 0.3927F + Mth.abs(Mth.cos(limbSwing * 0.6662f) * 0.4f * limbSwingAmount / f);
		}
	}
}