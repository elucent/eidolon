package elucent.eidolon.client.models.armor;// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import elucent.eidolon.client.models.ModelRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;

public class TopHatModel extends ArmorModel {
	private final ModelPart hat;

	static TopHatModel instance;

	public static TopHatModel get() {
		return instance == null ? instance = new TopHatModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelRegistry.TOP_HAT)) : instance;
	}

	public static LayerDefinition createBodyLayer() {
		var mesh = HumanoidModel.createMesh(new CubeDeformation(0), 0);
		PartDefinition partdefinition = mesh.getRoot();

		PartDefinition hat = partdefinition.addOrReplaceChild("hat_", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-5.0F, -2.0F, -5.0F, 10.0F, 2.0F, 10.0F,
						new CubeDeformation(0.0F))
				.texOffs(0, 12)
				.addBox(-4.0F, -12.0F, -4.0F, 8.0F, 10.0F, 8.0F,
						new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -7.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 32);
	}

	public TopHatModel(ModelPart part) {
		super(EquipmentSlot.HEAD, part);

		hat = part.getChild("hat_");
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//
	}
}