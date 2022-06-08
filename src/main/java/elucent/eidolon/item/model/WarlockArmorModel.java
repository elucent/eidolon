package elucent.eidolon.item.model;// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.Mth;

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

	public WarlockArmorModel(EquipmentSlot slot) {
		super(slot, 64, 128);

		if (slot == EquipmentSlot.FEET) {
			right_boot = new ModelRenderer(this);
			right_boot.setPos(0.0F, 0.0F, 0.0F);
			rightLeg.addChild(right_boot);
			setRotationAngle(right_boot, 0.0F, 0.0873F, 0.0F);
			right_boot.texOffs(0, 84).addBox(-2.5F, 5.5F, -2.5F, 5.0F, 7.0F, 5.0F, 0.0F, false);
			right_boot.texOffs(0, 80).addBox(-2.5F, 9.5F, -3.5F, 5.0F, 3.0F, 1.0F, 0.0F, false);

			right_boot_cuff = new ModelRenderer(this);
			right_boot_cuff.setPos(0.0F, 5.0F, 0.0F);
			right_boot.addChild(right_boot_cuff);
			setRotationAngle(right_boot_cuff, 0.0F, 0.0F, -0.0873F);
			right_boot_cuff.texOffs(20, 84).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 2.0F, 6.0F, 0.0F, false);

			left_boot = new ModelRenderer(this);
			left_boot.setPos(0.0F, 0.0F, 0.0F);
			leftLeg.addChild(left_boot);
			setRotationAngle(left_boot, 0.0F, -0.0873F, 0.0F);
			left_boot.texOffs(0, 84).addBox(-2.5F, 5.5F, -2.5F, 5.0F, 7.0F, 5.0F, 0.0F, false);
			left_boot.texOffs(0, 80).addBox(-2.5F, 9.5F, -3.5F, 5.0F, 3.0F, 1.0F, 0.0F, false);

			left_boot_cuff = new ModelRenderer(this);
			left_boot_cuff.setPos(0.0F, 5.0F, 0.0F);
			left_boot.addChild(left_boot_cuff);
			setRotationAngle(left_boot_cuff, 0.0F, 0.0F, 0.0873F);
			left_boot_cuff.texOffs(20, 84).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 2.0F, 6.0F, 0.0F, false);
		}

		if (slot == EquipmentSlot.CHEST) {
			right_sleeve = new ModelRenderer(this);
			right_sleeve.setPos(0.0F, 0.0F, 0.0F);
			rightArm.addChild(right_sleeve);
			setRotationAngle(right_sleeve, 0.0F, 0.0F, 0.1745F);
			right_sleeve.texOffs(28, 38).addBox(-4.5F, -2.0F, -2.5F, 5.0F, 5.0F, 5.0F, 0.0F, false);

			right_cuff = new ModelRenderer(this);
			right_cuff.setPos(-1.0F, 3.0F, 0.0F);
			right_sleeve.addChild(right_cuff);
			setRotationAngle(right_cuff, 0.0F, 0.0873F, 0.0873F);
			right_cuff.texOffs(28, 48).addBox(-4.5F, -0.5F, -3.0F, 6.0F, 2.0F, 6.0F, 0.0F, false);

			left_sleeve = new ModelRenderer(this);
			left_sleeve.setPos(0.0F, 0.0F, 0.0F);
			leftArm.addChild(left_sleeve);
			setRotationAngle(left_sleeve, 0.0F, 0.0F, -0.1745F);
			left_sleeve.texOffs(28, 38).addBox(-0.5F, -2.0F, -2.5F, 5.0F, 5.0F, 5.0F, 0.0F, true);

			left_cuff = new ModelRenderer(this);
			left_cuff.setPos(1.0F, 3.0F, 0.0F);
			left_sleeve.addChild(left_cuff);
			setRotationAngle(left_cuff, 0.0F, -0.0873F, -0.0873F);
			left_cuff.texOffs(28, 48).addBox(-1.5F, -0.5F, -3.0F, 6.0F, 2.0F, 6.0F, 0.0F, true);

			cloak = new ModelRenderer(this);
			cloak.setPos(2.0F, 12.0F, 0.0F);
			body.addChild(cloak);
			cloak.texOffs(0, 41).addBox(-6.5F, -12.499F, -2.501F, 9.0F, 15.0F, 5.0F, 0.0F, false);
			cloak.texOffs(28, 56).addBox(-7.0F, -12.5F, -3.0F, 10.0F, 8.0F, 6.0F, 0.0F, false);

			left_side = new ModelRenderer(this);
			left_side.setPos(-5.0F, -4.5F, -3.0F);
			cloak.addChild(left_side);
			setRotationAngle(left_side, 0.0F, 0.0F, 0.1745F);
			left_side.texOffs(0, 64).addBox(-2.0F, 0.0F, 0.01F, 2.0F, 10.0F, 6.0F, 0.0F, false);

			right_side = new ModelRenderer(this);
			right_side.setPos(1.0F, -4.5F, -3.0F);
			cloak.addChild(right_side);
			setRotationAngle(right_side, 0.0F, 0.0F, -0.1745F);
			right_side.texOffs(0, 64).addBox(0.0F, 0.0F, 0.01F, 2.0F, 10.0F, 6.0F, 0.0F, false);

			back_side = new ModelRenderer(this);
			back_side.setPos(-2.0F, -4.5F, 1.0F);
			cloak.addChild(back_side);
			setRotationAngle(back_side, 0.1745F, 0.0F, 0.0F);
			back_side.texOffs(17, 70).addBox(-5.01F, 0.0F, 0.0F, 10.0F, 12.0F, 2.0F, 0.0F, false);
		}

		if (slot == EquipmentSlot.HEAD) {
			hat = new ModelRenderer(this);
			hat.setPos(0.0F, -7.0F, 0.0F);
			head.addChild(hat);
			setRotationAngle(hat, -0.1745F, 0.0F, -0.0873F);
			hat.texOffs(0, 0).addBox(-7.0F, -1.0F, -7.0F, 14.0F, 2.0F, 14.0F, 0.0F, false);

			hatMid = new ModelRenderer(this);
			hatMid.setPos(0.0F, 0.0F, 0.0F);
			hat.addChild(hatMid);
			setRotationAngle(hatMid, -0.0873F, 0.0F, 0.0873F);
			hatMid.texOffs(0, 16).addBox(-4.5F, -5.75F, -4.5F, 9.0F, 6.0F, 9.0F, 0.0F, false);

			hatUpper = new ModelRenderer(this);
			hatUpper.setPos(0.0F, -5.0F, 0.0F);
			hatMid.addChild(hatUpper);
			setRotationAngle(hatUpper, -0.1745F, 0.0F, 0.0436F);
			hatUpper.texOffs(0, 31).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 4.0F, 6.0F, 0.0F, false);

			hatTip = new ModelRenderer(this);
			hatTip.setPos(0.0F, -3.0F, 0.0F);
			hatUpper.addChild(hatTip);
			setRotationAngle(hatTip, -0.2618F, 0.0F, 0.0873F);
			hatTip.texOffs(24, 31).addBox(-1.5F, -4.5F, -1.5F, 3.0F, 4.0F, 3.0F, 0.0F, false);
		}
	}

	@Override
	public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		float f = 1.0F;
		if (entity.getFallFlyingTicks() > 4) {
			f = (float)entity.getDeltaMovement().lengthSqr();
			f = f / 0.2F;
			f = f * f * f;
		}
		if (f < 1.0F) {
			f = 1.0F;
		}
		if (slot == EquipmentSlot.CHEST) {
			back_side.xRot = 0.1745f + Mth.abs(Mth.cos(limbSwing * 0.6662f) * 0.7f * limbSwingAmount / f);
			left_side.zRot = 0.1745f + Mth.cos(limbSwing * 0.6662f + (float)Math.PI) * 0.2f * limbSwingAmount / f + 0.1f * limbSwingAmount / f;
			right_side.zRot = -0.1745f - Mth.cos(limbSwing * 0.6662f) * 0.2f * limbSwingAmount / f - 0.1f * limbSwingAmount / f;
		}
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}