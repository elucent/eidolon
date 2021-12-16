package elucent.eidolon.entity;// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import elucent.eidolon.ClientEvents;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class RavenModel extends EntityModel<RavenEntity> {
	private final ModelPart body;

	public RavenModel(ModelPart root) {
		this.body = root.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, -0.5F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -3.0F, -3.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 6).addBox(-0.5F, -2.0F, -5.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, -1.5F));

		PartDefinition wings = body.addOrReplaceChild("wings", CubeListBuilder.create().texOffs(0, 14).addBox(-2.0F, -3.5F, -0.5F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, -2.5F, -0.5236F, 0.0F, 0.0F));

		PartDefinition tailMid = body.addOrReplaceChild("tailMid", CubeListBuilder.create().texOffs(13, 0).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.5F, 3.0F, -0.2618F, 0.0F, 0.0F));

		PartDefinition leftTail = body.addOrReplaceChild("leftTail", CubeListBuilder.create().texOffs(16, 14).mirror().addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.75F, -3.0F, 2.5F, -0.2618F, 0.2618F, 0.0F));

		PartDefinition rightTail = body.addOrReplaceChild("rightTail", CubeListBuilder.create().texOffs(16, 14).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.75F, -3.0F, 2.5F, -0.2618F, -0.2618F, 0.0F));

		PartDefinition leftWing = body.addOrReplaceChild("leftWing", CubeListBuilder.create().texOffs(0, 25).addBox(0.0F, 0.0F, 0.0F, 10.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -5.5F, -1.5F));

		PartDefinition rightWing = body.addOrReplaceChild("rightWing", CubeListBuilder.create().texOffs(0, 25).mirror().addBox(-10.0F, 0.0F, 0.0F, 10.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.0F, -5.5F, -1.5F));

		PartDefinition leftLeg = body.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(12, 7).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -3.0F, 0.5F));

		PartDefinition rightLeg = body.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(12, 7).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -3.0F, 0.5F));

		PartDefinition chest = body.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(1, 6).addBox(-1.5F, -3.0F, 0.0F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, -2.5F, -0.5236F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	public void renderOnShoulder(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float p_228284_5_, float p_228284_6_, float p_228284_7_, float p_228284_8_, int p_228284_9_) {
		body.getChild("leftWing").visible = false;
		body.getChild("rightWing").visible = false;
		body.getChild("wings").visible = true;
		body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
	}

	@Override
	public void setupAnim(RavenEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		ModelPart leftWing = body.getChild("leftWing"), rightWing = body.getChild("rightWing"),
				wings = body.getChild("wings"), rightLeg = body.getChild("rightLeg"), leftLeg = body.getChild("leftLeg"),
				head = body.getChild("head");
		body.y = 24.0f;
		if (entity.isOnGround()) {
			leftWing.visible = false;
			rightWing.visible = false;
			wings.visible = true;

			if (entity.isTame() && entity.isInSittingPose()) {
				rightLeg.xRot = -(float)Math.PI / 3;
				leftLeg.xRot = -(float)Math.PI / 3;
				body.y = 25.5f;
			}
			else {
				rightLeg.xRot = Mth.cos(limbSwing * 2F + (float)Math.PI) * 2F * limbSwingAmount;
				leftLeg.xRot = Mth.cos(limbSwing * 2F) * 2F * limbSwingAmount;
			}
		}
		else {
			leftWing.visible = true;
			rightWing.visible = true;
			wings.visible = false;
			rightLeg.xRot = 0;
			leftLeg.xRot = 0;

			if (entity.getDeltaMovement().y < 0) {
				rightWing.zRot = Mth.sin(ClientEvents.getClientTicks()) * 0.1f;
				leftWing.zRot = -Mth.sin(0.97f + ClientEvents.getClientTicks()) * 0.1f;
			}
			else {
				rightWing.zRot = (float)Math.sin(ClientEvents.getClientTicks()) * 0.4f;
				leftWing.zRot = -(float)Math.sin(ClientEvents.getClientTicks()) * 0.4f;
			}
		}

		head.xRot = (float)Math.toRadians(headPitch);
		head.yRot = (float)Math.toRadians(netHeadYaw);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		body.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}