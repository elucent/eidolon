package elucent.eidolon.item.model;// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.world.entity.EquipmentSlot;

public abstract class ArmorModel extends HumanoidModel {
	EquipmentSlot slot;

	public ModelRenderer copyWithoutBoxes(ModelRenderer box) {
		ModelRenderer newbox = new ModelRenderer(this);
		newbox.setPos(box.x, box.y, box.z);
		setRotationAngle(newbox, box.xRot, box.yRot, box.zRot);
		newbox.mirror = box.mirror;
		newbox.visible = box.visible;
		return newbox;
	}

	public ArmorModel(EquipmentSlot slot, int texWidth, int texHeight) {
		super(0, 0, texWidth, texHeight);
		this.slot = slot;

		head = copyWithoutBoxes(head);
		body = copyWithoutBoxes(body);
		leftArm = copyWithoutBoxes(leftArm);
		leftLeg = copyWithoutBoxes(leftLeg);
		rightArm = copyWithoutBoxes(rightArm);
		rightLeg = copyWithoutBoxes(rightLeg);
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

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}