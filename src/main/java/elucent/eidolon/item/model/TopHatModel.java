package elucent.eidolon.item.model;// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;

public class TopHatModel extends ArmorModel {
	private final ModelRenderer hat;

	public TopHatModel() {
		super(EquipmentSlot.HEAD, 64, 32);
		texWidth = 64;
		texHeight = 32;

		hat = new ModelRenderer(this);
		hat.setPos(0.0F, -7.0F, 0.0F);
		head.addChild(hat);
		setRotationAngle(hat, -0.0873F, 0.0F, 0.0F);
		hat.texOffs(0, 0).addBox(-5.0F, -2.0F, -5.0F, 10.0F, 2.0F, 10.0F, 0.0F, false);
		hat.texOffs(0, 12).addBox(-4.0F, -12.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//
	}
}