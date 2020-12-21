package elucent.eidolon.item.model;// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.math.MathHelper;

public class WarlockArmorModel extends ArmorModel {
	private ModelRenderer left_boot = null;
	private ModelRenderer left_boot_cuff = null;
	private ModelRenderer right_boot = null;
	private ModelRenderer right_boot_cuff = null;
	private ModelRenderer left_sleeve = null;
	private ModelRenderer left_cuff = null;
	private ModelRenderer right_sleeve = null;
	private ModelRenderer right_cuff = null;
	private ModelRenderer hat = null;
	private ModelRenderer hatMid = null;
	private ModelRenderer hatUpper = null;
	private ModelRenderer hatTip = null;
	private ModelRenderer cloak = null;
	private ModelRenderer left_side = null;
	private ModelRenderer right_side = null;
	private ModelRenderer back_side = null;

	public WarlockArmorModel(EquipmentSlotType slot) {
		super(slot, 64, 128);

		if (slot == EquipmentSlotType.FEET) {
			right_boot = new ModelRenderer(this);
			right_boot.setRotationPoint(0.0F, 0.0F, 0.0F);
			bipedRightLeg.addChild(right_boot);
			setRotationAngle(right_boot, 0.0F, 0.0873F, 0.0F);
			right_boot.setTextureOffset(0, 84).addBox(-2.5F, 5.5F, -2.5F, 5.0F, 7.0F, 5.0F, 0.0F, false);
			right_boot.setTextureOffset(0, 80).addBox(-2.5F, 9.5F, -3.5F, 5.0F, 3.0F, 1.0F, 0.0F, false);

			right_boot_cuff = new ModelRenderer(this);
			right_boot_cuff.setRotationPoint(0.0F, 5.0F, 0.0F);
			right_boot.addChild(right_boot_cuff);
			setRotationAngle(right_boot_cuff, 0.0F, 0.0F, -0.0873F);
			right_boot_cuff.setTextureOffset(20, 84).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 2.0F, 6.0F, 0.0F, false);

			left_boot = new ModelRenderer(this);
			left_boot.setRotationPoint(0.0F, 0.0F, 0.0F);
			bipedLeftLeg.addChild(left_boot);
			setRotationAngle(left_boot, 0.0F, -0.0873F, 0.0F);
			left_boot.setTextureOffset(0, 84).addBox(-2.5F, 5.5F, -2.5F, 5.0F, 7.0F, 5.0F, 0.0F, false);
			left_boot.setTextureOffset(0, 80).addBox(-2.5F, 9.5F, -3.5F, 5.0F, 3.0F, 1.0F, 0.0F, false);

			left_boot_cuff = new ModelRenderer(this);
			left_boot_cuff.setRotationPoint(0.0F, 5.0F, 0.0F);
			left_boot.addChild(left_boot_cuff);
			setRotationAngle(left_boot_cuff, 0.0F, 0.0F, 0.0873F);
			left_boot_cuff.setTextureOffset(20, 84).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 2.0F, 6.0F, 0.0F, false);
		}

		if (slot == EquipmentSlotType.CHEST) {
			right_sleeve = new ModelRenderer(this);
			right_sleeve.setRotationPoint(0.0F, 0.0F, 0.0F);
			bipedRightArm.addChild(right_sleeve);
			setRotationAngle(right_sleeve, 0.0F, 0.0F, 0.1745F);
			right_sleeve.setTextureOffset(28, 38).addBox(-4.5F, -2.0F, -2.5F, 5.0F, 5.0F, 5.0F, 0.0F, false);

			right_cuff = new ModelRenderer(this);
			right_cuff.setRotationPoint(-1.0F, 3.0F, 0.0F);
			right_sleeve.addChild(right_cuff);
			setRotationAngle(right_cuff, 0.0F, 0.0873F, 0.0873F);
			right_cuff.setTextureOffset(28, 48).addBox(-4.5F, -0.5F, -3.0F, 6.0F, 2.0F, 6.0F, 0.0F, false);

			left_sleeve = new ModelRenderer(this);
			left_sleeve.setRotationPoint(0.0F, 0.0F, 0.0F);
			bipedLeftArm.addChild(left_sleeve);
			setRotationAngle(left_sleeve, 0.0F, 0.0F, -0.1745F);
			left_sleeve.setTextureOffset(28, 38).addBox(-0.5F, -2.0F, -2.5F, 5.0F, 5.0F, 5.0F, 0.0F, true);

			left_cuff = new ModelRenderer(this);
			left_cuff.setRotationPoint(1.0F, 3.0F, 0.0F);
			left_sleeve.addChild(left_cuff);
			setRotationAngle(left_cuff, 0.0F, -0.0873F, -0.0873F);
			left_cuff.setTextureOffset(28, 48).addBox(-1.5F, -0.5F, -3.0F, 6.0F, 2.0F, 6.0F, 0.0F, true);

			cloak = new ModelRenderer(this);
			cloak.setRotationPoint(2.0F, 12.0F, 0.0F);
			bipedBody.addChild(cloak);
			cloak.setTextureOffset(0, 41).addBox(-6.5F, -12.499F, -2.501F, 9.0F, 15.0F, 5.0F, 0.0F, false);
			cloak.setTextureOffset(28, 56).addBox(-7.0F, -12.5F, -3.0F, 10.0F, 8.0F, 6.0F, 0.0F, false);

			left_side = new ModelRenderer(this);
			left_side.setRotationPoint(-5.0F, -4.5F, -3.0F);
			cloak.addChild(left_side);
			setRotationAngle(left_side, 0.0F, 0.0F, 0.1745F);
			left_side.setTextureOffset(0, 64).addBox(-2.0F, 0.0F, 0.01F, 2.0F, 10.0F, 6.0F, 0.0F, false);

			right_side = new ModelRenderer(this);
			right_side.setRotationPoint(1.0F, -4.5F, -3.0F);
			cloak.addChild(right_side);
			setRotationAngle(right_side, 0.0F, 0.0F, -0.1745F);
			right_side.setTextureOffset(0, 64).addBox(0.0F, 0.0F, 0.01F, 2.0F, 10.0F, 6.0F, 0.0F, false);

			back_side = new ModelRenderer(this);
			back_side.setRotationPoint(-2.0F, -4.5F, 1.0F);
			cloak.addChild(back_side);
			setRotationAngle(back_side, 0.1745F, 0.0F, 0.0F);
			back_side.setTextureOffset(17, 70).addBox(-5.01F, 0.0F, 0.0F, 10.0F, 12.0F, 2.0F, 0.0F, false);
		}

		if (slot == EquipmentSlotType.HEAD) {
			hat = new ModelRenderer(this);
			hat.setRotationPoint(0.0F, -7.0F, 0.0F);
			bipedHead.addChild(hat);
			setRotationAngle(hat, -0.1745F, 0.0F, -0.0873F);
			hat.setTextureOffset(0, 0).addBox(-7.0F, -1.0F, -7.0F, 14.0F, 2.0F, 14.0F, 0.0F, false);

			hatMid = new ModelRenderer(this);
			hatMid.setRotationPoint(0.0F, 0.0F, 0.0F);
			hat.addChild(hatMid);
			setRotationAngle(hatMid, -0.0873F, 0.0F, 0.0873F);
			hatMid.setTextureOffset(0, 16).addBox(-4.5F, -5.75F, -4.5F, 9.0F, 6.0F, 9.0F, 0.0F, false);

			hatUpper = new ModelRenderer(this);
			hatUpper.setRotationPoint(0.0F, -5.0F, 0.0F);
			hatMid.addChild(hatUpper);
			setRotationAngle(hatUpper, -0.1745F, 0.0F, 0.0436F);
			hatUpper.setTextureOffset(0, 31).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 4.0F, 6.0F, 0.0F, false);

			hatTip = new ModelRenderer(this);
			hatTip.setRotationPoint(0.0F, -3.0F, 0.0F);
			hatUpper.addChild(hatTip);
			setRotationAngle(hatTip, -0.2618F, 0.0F, 0.0873F);
			hatTip.setTextureOffset(24, 31).addBox(-1.5F, -4.5F, -1.5F, 3.0F, 4.0F, 3.0F, 0.0F, false);
		}
	}

	@Override
	public void setRotationAngles(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		super.setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		float f = 1.0F;
		if (entity.getTicksElytraFlying() > 4) {
			f = (float)entity.getMotion().lengthSquared();
			f = f / 0.2F;
			f = f * f * f;
		}
		if (f < 1.0F) {
			f = 1.0F;
		}
		if (slot == EquipmentSlotType.CHEST) {
			back_side.rotateAngleX = 0.1745f + MathHelper.abs(MathHelper.cos(limbSwing * 0.6662f) * 0.7f * limbSwingAmount / f);
			left_side.rotateAngleZ = 0.1745f + MathHelper.cos(limbSwing * 0.6662f + (float)Math.PI) * 0.2f * limbSwingAmount / f + 0.1f * limbSwingAmount / f;
			right_side.rotateAngleZ = -0.1745f - MathHelper.cos(limbSwing * 0.6662f) * 0.2f * limbSwingAmount / f - 0.1f * limbSwingAmount / f;
		}
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}