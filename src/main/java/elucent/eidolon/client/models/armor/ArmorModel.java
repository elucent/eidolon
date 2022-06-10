package elucent.eidolon.client.models.armor;// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EquipmentSlot;

public abstract class ArmorModel extends HumanoidModel {
	EquipmentSlot slot;

	public ArmorModel(EquipmentSlot slot, ModelPart part) {
		super(part);
		this.slot = slot;
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		hat.visible = false;
		body.visible = leftArm.visible = rightArm.visible =
			head.visible = leftLeg.visible = rightLeg.visible = false;

		if (slot == EquipmentSlot.CHEST) {
			body.visible = true;
			leftArm.visible = true;
			rightArm.visible = true;
		}

		if (slot == EquipmentSlot.HEAD) {
			head.visible = true;
		}

		if (slot == EquipmentSlot.FEET) {
			leftLeg.visible = true;
			rightLeg.visible = true;
		}
		super.renderToBuffer(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}