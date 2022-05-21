package elucent.eidolon.item.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class SilverArmorModel extends ArmorModel {
	public SilverArmorModel(ModelPart part) {
		super(part);
	}
	
	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = HumanoidModel.createMesh(new CubeDeformation(0), 0);
		PartDefinition root = createHumanoidAlias(mesh);

		PartDefinition body = root.getChild("body");
		PartDefinition pelvis = root.getChild("pelvis");
		PartDefinition right_foot = root.getChild("right_foot");
		PartDefinition left_foot = root.getChild("left_foot");
		PartDefinition right_legging = root.getChild("right_legging");
		PartDefinition left_legging = root.getChild("left_legging");
		PartDefinition left_arm = root.getChild("left_arm");
		PartDefinition right_arm = root.getChild("right_arm");
		PartDefinition head = root.getChild("head");

		PartDefinition chest = body.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)), PartPose.offset(0.0F, 12.0F, 0.0F));
		PartDefinition right_shoulder = right_arm.addOrReplaceChild("right_shoulder", new CubeListBuilder().mirror().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(1.0f)), PartPose.ZERO);
		PartDefinition left_shoulder = left_arm.addOrReplaceChild("left_shoulder", new CubeListBuilder().texOffs(40, 16).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(1.0f)), PartPose.ZERO);
		PartDefinition helm = head.addOrReplaceChild("helm", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.0F)), PartPose.ZERO);
		PartDefinition guard = helm.addOrReplaceChild("guard", CubeListBuilder.create().texOffs(6, 41).addBox(-5.0F, -6.0F, -5.5F, 10.0F, 10.0F, 10.0F, new CubeDeformation(1.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
		PartDefinition left_boot = left_foot.addOrReplaceChild("left_boot", new CubeListBuilder().texOffs(0, 22).mirror().addBox(-2.0F, 6.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(1.0f)), PartPose.ZERO);
		PartDefinition right_boot = right_foot.addOrReplaceChild("right_boot", new CubeListBuilder().texOffs(0, 22).addBox(-2.0F, 6.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(1.0f)), PartPose.ZERO);
		PartDefinition left_leg = left_legging.addOrReplaceChild("left_leg", new CubeListBuilder().texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.5f)), PartPose.ZERO);
		PartDefinition right_leg = right_legging.addOrReplaceChild("right_leg", new CubeListBuilder().texOffs(0, 32).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.5f)), PartPose.ZERO);
		PartDefinition codpiece = pelvis.addOrReplaceChild("codpiece", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 5.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 7.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}

	public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}