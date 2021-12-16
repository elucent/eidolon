package elucent.eidolon.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class WraithModel extends EntityModel<WraithEntity> {
	private final ModelPart body;

	public WraithModel(ModelPart root) {
		this.body = root.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -16.0F, -4.0F, 8.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(32, 2).addBox(-3.0F, -15.0F, -3.0F, 6.0F, 15.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, 0.0F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 24).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(32, 26).addBox(-3.0F, 0.2588F, 0.9659F, 6.0F, 15.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -4.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 48).addBox(-1.5F, -1.5F, -1.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.5F, -6.5F, 0.0F, -1.0472F, 0.0F, -0.1745F));

		PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(12, 48).addBox(-1.5F, -2.4749F, -2.6464F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition right_hand = lower_right_arm.addOrReplaceChild("right_hand", CubeListBuilder.create().texOffs(25, 48).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.5F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 48).mirror().addBox(-1.5F, -1.5F, -1.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.5F, -6.5F, 0.0F, -1.0472F, 0.0F, 0.1745F));

		PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(12, 48).mirror().addBox(9.5F, -2.4749F, -2.6464F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-11.0F, 6.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition left_hand = lower_left_arm.addOrReplaceChild("left_hand", CubeListBuilder.create().texOffs(25, 48).mirror().addBox(9.5F, -1.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 2.5F, 0.0F, 0.3927F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(WraithEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		body.y = 8.0f + 1.5f * (float)Mth.sin(ageInTicks / 20.0f * (float)Math.PI);
		body.xRot = (float)Math.toRadians(Mth.clamp(
			15.0f * (float)entity.getDeltaMovement().multiply(1, 0, 1).length() / 0.3f,
			-15.0f, 15.0f
		));
		body.getChild("right_arm").xRot = -1.0472F + Mth.cos(limbSwing * 0.666F + (float)Math.PI) * 0.25F * limbSwingAmount;
		body.getChild("left_arm").xRot = -1.0472F + Mth.cos(limbSwing * 0.666F) * 0.25F * limbSwingAmount;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, buffer, packedLight, packedOverlay);
	}
}