package elucent.eidolon.item.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import elucent.eidolon.capability.IPlayerData;
import elucent.eidolon.item.IWingsItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class RavenCloakModel extends HumanoidModel {
	ModelPart root, cloak, wings, leftWing, rightWing;

	public RavenCloakModel(ModelPart root) {
		super(root);
		this.root = root;
		this.cloak = root.getChild("body").getChild("cloak");
		this.wings = root.getChild("body").getChild("wings");
		this.leftWing = wings.getChild("left_wing");
		this.rightWing = wings.getChild("right_wing");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = HumanoidModel.createMesh(new CubeDeformation(0), 0);
		PartDefinition root = mesh.getRoot();
		PartDefinition body = root.addOrReplaceChild("body", new CubeListBuilder(), PartPose.ZERO);
		PartDefinition head = root.addOrReplaceChild("head", new CubeListBuilder(), PartPose.ZERO);

		PartDefinition hood = head.addOrReplaceChild("hood", CubeListBuilder.create().texOffs(0, 0).addBox(-5.5F, -10.5F, -4.5F, 11.0F, 11.0F, 11.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.5F, 0.2618F, 0.0F, 0.0F));
		PartDefinition left_feather1 = hood.addOrReplaceChild("left_feather1", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(0.0F, -1.5F, 0.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(6.0F, -3.0F, -2.0F, 2.0944F, 0.0F, 0.5236F));
		PartDefinition left_feather2 = hood.addOrReplaceChild("left_feather2", CubeListBuilder.create().texOffs(0, 17).mirror().addBox(0.0F, -1.5F, 0.0F, 0.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(6.0F, -3.0F, 1.0F, 1.5708F, 0.0F, 0.7854F));
		PartDefinition left_feather3 = hood.addOrReplaceChild("left_feather3", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(0.0F, -1.5F, 0.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(6.0F, -2.0F, 4.0F, 1.0472F, 0.0F, 0.5236F));
		PartDefinition right_feather1 = hood.addOrReplaceChild("right_feather1", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -1.5F, 0.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -3.0F, -2.0F, 2.0944F, 0.0F, -0.5236F));
		PartDefinition right_feather2 = hood.addOrReplaceChild("right_feather2", CubeListBuilder.create().texOffs(0, 17).addBox(0.0F, -1.5F, 0.0F, 0.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -3.0F, 1.0F, 1.5708F, 0.0F, -0.7854F));
		PartDefinition right_feather3 = hood.addOrReplaceChild("right_feather3", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -1.5F, 0.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -2.0F, 4.0F, 1.0472F, 0.0F, -0.5236F));
		PartDefinition beak = hood.addOrReplaceChild("beak", CubeListBuilder.create().texOffs(33, 0).addBox(-2.5F, 0.0F, -4.0F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -11.0F, -5.0F, 0.7854F, 0.0F, 0.0F));
		PartDefinition cloak = body.addOrReplaceChild("cloak", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 0.0F));
		PartDefinition left_cloak = cloak.addOrReplaceChild("left_cloak", CubeListBuilder.create().texOffs(0, 28).mirror().addBox(-0.5F, -4.0F, -4.0F, 12.0F, 20.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.5F, 1, -3.0F, 0.1309F, -0.1309F, -0.1309F));
		PartDefinition right_cloak = cloak.addOrReplaceChild("right_cloak", CubeListBuilder.create().texOffs(0, 28).addBox(-11.5F, -4.0F, -4.0F, 12.0F, 20.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 1, -3.0F, 0.1309F, 0.1309F, 0.1309F));
		PartDefinition wings = body.addOrReplaceChild("wings", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 0.0F));
		PartDefinition upper_left_cloak = wings.addOrReplaceChild("upper_left_cloak", CubeListBuilder.create().texOffs(0, 28).mirror().addBox(0.5F, -4.0F, -4.0F, 12.0F, 7.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.5F, 4, -3.0F, 0.1309F, -0.1309F, -0.5236F));
		PartDefinition upper_right_cloak = wings.addOrReplaceChild("upper_right_cloak", CubeListBuilder.create().texOffs(0, 28).addBox(-12.5F, -4.0F, -4.0F, 12.0F, 7.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 4, -3.0F, 0.1309F, 0.1309F, 0.5236F));
		PartDefinition left_wing = wings.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(51, 0).addBox(0.0F, 0.0F, -2.0F, 12.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
			.texOffs(48, 41).addBox(-4.0F, 8.0F, -1.0F, 14.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2, 5.0F, 0.0F, -0.5236F, -0.3927F));
		PartDefinition left_wing_mid = left_wing.addOrReplaceChild("left_wing_mid", CubeListBuilder.create().texOffs(72, 10).addBox(0.0F, 0.0F, -1.0F, 12.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
			.texOffs(44, 27).addBox(-1.0F, 8.0F, -0.5F, 14.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.5F, 0.0F, -0.5F, 0.0F, 0.2618F, 0.1309F));
		PartDefinition left_wing_tip = left_wing_mid.addOrReplaceChild("left_wing_tip", CubeListBuilder.create().texOffs(44, 11).addBox(0.0F, 0.0F, -1.0F, 14.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.5F, 0.0F, 0.5F, 0.0F, 0.5236F, -0.1309F));
		PartDefinition right_wing = wings.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(51, 0).mirror().addBox(-12.0F, 0.0F, -2.0F, 12.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
			.texOffs(48, 41).mirror().addBox(-10.0F, 8.0F, -1.0F, 14.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -2, 5.0F, 0.0F, 0.5236F, 0.3927F));
		PartDefinition right_wing_mid = right_wing.addOrReplaceChild("right_wing_mid", CubeListBuilder.create().texOffs(72, 10).mirror().addBox(-12.0F, 0.0F, -1.0F, 12.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
			.texOffs(44, 27).mirror().addBox(-13.0F, 8.0F, -0.5F, 14.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-12.5F, 0.0F, -0.5F, 0.0F, -0.2618F, -0.1309F));
		PartDefinition right_wing_tip = right_wing_mid.addOrReplaceChild("right_wing_tip", CubeListBuilder.create().texOffs(44, 11).mirror().addBox(-14.0F, 0.0F, -1.0F, 14.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-11.5F, 0.0F, 0.5F, 0.0F, -0.5236F, 0.1309F));

		return LayerDefinition.create(mesh, 128, 64);
	}

	@Override
	public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		if (entity instanceof Player p) {
			float pticks = Minecraft.getInstance().getFrameTime();
			IPlayerData data = p.getCapability(IPlayerData.INSTANCE).resolve().get();
			float timeSinceFlying = Mth.clamp(p.level.getGameTime() - data.getFlightStartTime(p) + pticks, 0, 10);
			float timeSinceFlapping = Mth.clamp(p.level.getGameTime() - data.getLastFlapTime(p) + pticks, 0.01F, 20);
			
			ModelPart leftMid = leftWing.getChild("left_wing_mid");
			ModelPart leftTip = leftMid.getChild("left_wing_tip");
			ModelPart rightMid = rightWing.getChild("right_wing_mid");
			ModelPart rightTip = rightMid.getChild("right_wing_tip");
			
			float unfurl = 0;
			timeSinceFlapping /= 20;
			timeSinceFlapping -= 0.166666666666666F;
			float flap1 = Mth.sin(Mth.PI * 2.4f * timeSinceFlapping) / (2.4f * Mth.PI * timeSinceFlapping);
			timeSinceFlapping += 0.166666666666666F;
			float flap2 = -Mth.sin(Mth.TWO_PI * Mth.sqrt(timeSinceFlapping));
			flap2 *= (1.5f - 0.5f * timeSinceFlapping);
			if (data.isFlying(p)) {
				cloak.visible = false;
				wings.visible = true;
				unfurl = (10 - timeSinceFlying) / 10.0f;
				unfurl *= unfurl;
				unfurl = 1 - unfurl;
			}
			else {
				cloak.visible = timeSinceFlying >= 10;
				wings.visible = timeSinceFlying < 10;
				unfurl = (10 - timeSinceFlying) / 10.0f;
				unfurl *= unfurl;
			}
			
			float leftWingX = 0.7854F, leftWingY = -0.5236F, leftWingZ = -0.7854F;
			float rightWingX = 0.7854F, rightWingY = 0.5236F, rightWingZ = 0.7854F;
			float leftMidX = 0, leftMidY = 0.2618F;
			float leftTipY = 0.5236F;
			float rightMidX = 0, rightMidY = -0.2618F;
			float rightTipY = -0.5236F;
			
			// dashing
			
			if (data.isDashing(p)) {
				ItemStack wings = data.getWingsItem(p);
				int maxDash = wings.getItem() instanceof IWingsItem i ? i.getDashTicks(wings) : 0;
				int dashTicks = data.getDashTicks(p);
				int sinceDashing = maxDash - dashTicks;
				float dashStart = Mth.clamp(sinceDashing / 10.0f, 0.01f, 1);
				
				leftWingZ = Mth.lerp(dashStart, leftWingZ, 0);
				rightWingZ = Mth.lerp(dashStart, rightWingZ, 0);
				leftWingY = Mth.lerp(dashStart, leftWingZ, 0);
				rightWingY = Mth.lerp(dashStart, rightWingZ, 0);
				leftWingX = Mth.lerp(dashStart, leftWingX, 0.2618F);
				rightWingX = Mth.lerp(dashStart, rightWingX, 0.2618F);
			}
			
			// flapping

			leftWingX = Mth.lerp(flap1, leftWingX, 1.5708F);
			leftWingY = Mth.lerp(flap1, leftWingY, 1.047F);
			leftWingZ = Mth.lerp(flap1, leftWingZ, 0.5236F);
			leftMidY = Mth.lerp(flap2, leftMidY, 0.7854F);
			rightWingX = Mth.lerp(flap1, rightWingX, 1.5708F);
			rightWingY = Mth.lerp(flap1, rightWingY, -1.047F);
			rightWingZ = Mth.lerp(flap1, rightWingZ, -0.5236F);
			rightMidY = Mth.lerp(flap2, rightMidY, -0.7854F);
			
			// unfurling
			
			leftWingX = Mth.lerp(unfurl, 0, leftWingX);
			leftWingY = Mth.lerp(unfurl, 0.2618F, leftWingY);
			leftWingZ = Mth.lerp(unfurl, -0.3927F, leftWingZ);
			leftMidY = Mth.lerp(unfurl, 1.4399F, leftMidY);
			leftTipY = Mth.lerp(unfurl, 1.5708F, leftTipY);
			
			rightWingX = Mth.lerp(unfurl, 0, rightWingX);
			rightWingY = Mth.lerp(unfurl, -0.2618F, rightWingY);
			rightWingZ = Mth.lerp(unfurl, 0.3927F, rightWingZ);
			rightMidY = Mth.lerp(unfurl, -1.4399F, rightMidY);
			rightTipY = Mth.lerp(unfurl, -1.5708F, rightTipY);
			
			// crouching
			
			if (p.isCrouching()) {
				leftWingY += 0.3927F;
				rightWingY -= 0.3927F;
				leftWingZ += 0.3927F;
				rightWingZ -= 0.3927F;
			}
			
			// velocity
			
			float clampedDY = (float) Mth.clamp(p.getDeltaMovement().y, -0.5f, 0.5f);
			leftWingZ += clampedDY * 0.5236F;
			rightWingZ -= clampedDY * 0.5236F;
			
			leftWing.xRot = leftWingX;
			leftWing.yRot = leftWingY;
			leftWing.zRot = leftWingZ;
			leftMid.yRot = leftMidY;
			leftTip.yRot = leftTipY;
			
			rightWing.xRot = rightWingX;
			rightWing.yRot = rightWingY;
			rightWing.zRot = rightWingZ;
			rightMid.yRot = rightMidY;
			rightTip.yRot = rightTipY;
		}
	}
	
	@Override
    protected Iterable<ModelPart> headParts() {
		return ImmutableList.of(root.getChild("head"));
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(root.getChild("body"));
    }

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}