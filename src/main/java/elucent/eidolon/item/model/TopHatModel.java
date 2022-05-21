package elucent.eidolon.item.model;// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;

public class TopHatModel extends ArmorModel {
	public TopHatModel(ModelPart part) {
		super(part);
	}
	
	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = HumanoidModel.createMesh(new CubeDeformation(0), 0);
		PartDefinition root = createHumanoidAlias(mesh);
		
		PartDefinition head = root.getChild("head");
	
		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create()
			.texOffs(0, 0).addBox(-5.0F, -2.0F, -5.0F, 10.0F, 2.0F, 10.0F, new CubeDeformation(0))
			.texOffs(0, 12).addBox(-4.0F, -12.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0)),
			PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, -0.0873F, 0.0F, 0.0F));
		
		return LayerDefinition.create(mesh, 64, 32);
	}

	@Override
	public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//
	}
}