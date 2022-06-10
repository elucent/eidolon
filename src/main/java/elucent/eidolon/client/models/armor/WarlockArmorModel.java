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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

public class WarlockArmorModel extends ArmorModel {
	private ModelPart left_side = null;
	private ModelPart right_side = null;
	private ModelPart back_side = null;

	public static WarlockArmorModel CHEST;
	public static WarlockArmorModel FEET;
	public static WarlockArmorModel HEAD;

	public static WarlockArmorModel get(EquipmentSlot slot) {
		return switch (slot) {
			case CHEST -> CHEST == null ? CHEST = new WarlockArmorModel(EquipmentSlot.CHEST,
					Minecraft.getInstance().getEntityModels().bakeLayer(ModelRegistry.WARLOCK_CHEST)) : CHEST;
			case FEET -> FEET == null ? FEET = new WarlockArmorModel(EquipmentSlot.FEET,
					Minecraft.getInstance().getEntityModels().bakeLayer(ModelRegistry.WARLOCK_FEET)) : FEET;
			case HEAD -> HEAD == null ? HEAD = new WarlockArmorModel(EquipmentSlot.HEAD,
					Minecraft.getInstance().getEntityModels().bakeLayer(ModelRegistry.WARLOCK_HEAD)) : HEAD;
			default -> null;
		};
	}

	public WarlockArmorModel(EquipmentSlot slot, ModelPart part) {
		super(slot, part);
		if (slot == EquipmentSlot.CHEST) {
			left_side = part.getChild("left_side");
			right_side = part.getChild("right_side");
			back_side = part.getChild("back_side");
		}
	}

	public static LayerDefinition createBodyLayer(EquipmentSlot slot) {
		var mesh = HumanoidModel.createMesh(new CubeDeformation(0), 0);
		PartDefinition partdefinition = mesh.getRoot();

		var rightLeg = partdefinition.getChild("right_leg");
		var head = partdefinition.getChild("head");
		var hat = partdefinition.getChild("hat");
		var body = partdefinition.getChild("body");
		var right_arm = partdefinition.getChild("right_arm");
		var left_arm = partdefinition.getChild("left_arm");
		var right_leg = partdefinition.getChild("right_leg");
		var left_leg = partdefinition.getChild("left_leg");

		if (slot == EquipmentSlot.FEET) {
			PartDefinition right_boot = rightLeg.addOrReplaceChild("right_boot", CubeListBuilder.create()
							.texOffs(0, 84)
							.addBox(-2.5F, 5.5F, -2.5F, 5.0F, 7.0F, 5.0F,
									new CubeDeformation(0.0F))
							.texOffs(0, 80)
							.addBox(-2.5F, 9.5F, -3.5F, 5.0F, 3.0F, 1.0F,
									new CubeDeformation(0.0F)),
					PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F,
							0.0F, 0.0873F, 0.0F));

			PartDefinition right_boot_cuff = right_boot.addOrReplaceChild("right_boot_cuff", CubeListBuilder.create()
							.texOffs(20, 84)
							.addBox(-3.0F, -1.0F, -3.0F, 6.0F, 2.0F, 6.0F,
									new CubeDeformation(0.0F)),
					PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F,
							0.0F, 0.0F, -0.0873F));

			PartDefinition left_boot = left_leg.addOrReplaceChild("left_boot", CubeListBuilder.create()
							.texOffs(0, 84)
							.addBox(-2.5F, 5.5F, -2.5F, 5.0F, 7.0F, 5.0F,
									new CubeDeformation(0.0F))
							.texOffs(0, 80)
							.addBox(-2.5F, 9.5F, -3.5F, 5.0F, 3.0F, 1.0F,
									new CubeDeformation(0.0F)),
					PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F,
							0.0F, -0.0873F, 0.0F));

			PartDefinition left_boot_cuff = left_boot.addOrReplaceChild("left_boot_cuff", CubeListBuilder.create()
							.texOffs(20, 84)
							.addBox(-3.0F, -1.0F, -3.0F, 6.0F, 2.0F, 6.0F,
									new CubeDeformation(0.0F)),
					PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F,
							0.0F, 0.0F, 0.0873F));
		}

		if (slot == EquipmentSlot.CHEST) {
			// right_sleeve = new ModelRenderer(this);
			//			right_sleeve.setPos(0.0F, 0.0F, 0.0F);
			//			rightArm.addChild(right_sleeve);
			//			setRotationAngle(right_sleeve, 0.0F, 0.0F, 0.1745F);
			//			right_sleeve.texOffs(28, 38).addBox(-4.5F, -2.0F, -2.5F, 5.0F, 5.0F, 5.0F, 0.0F, false);

			PartDefinition right_sleeve = right_arm.addOrReplaceChild("right_sleeve", CubeListBuilder.create()
							.texOffs(28, 38)
							.addBox(-4.5F, -2.0F, -2.5F, 5.0F, 5.0F, 5.0F,
									new CubeDeformation(0.0F)),
					PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F,
							0.0F, 0.0F, 0.1745F));

			//right_cuff = new ModelRenderer(this);
			//			right_cuff.setPos(-1.0F, 3.0F, 0.0F);
			//			right_sleeve.addChild(right_cuff);
			//			setRotationAngle(right_cuff, 0.0F, 0.0873F, 0.0873F);
			//			right_cuff.texOffs(28, 48).addBox(-4.5F, -0.5F, -3.0F, 6.0F, 2.0F, 6.0F, 0.0F, false);

			PartDefinition right_cuff = right_sleeve.addOrReplaceChild("right_cuff", CubeListBuilder.create()
							.texOffs(28, 48)
							.addBox(-4.5F, -0.5F, -3.0F, 6.0F, 2.0F, 6.0F,
									new CubeDeformation(0.0F)),
					PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F,
							0.0F, 0.0873F, 0.0873F));

			//			left_sleeve = new ModelRenderer(this);
			//			left_sleeve.setPos(0.0F, 0.0F, 0.0F);
			//			leftArm.addChild(left_sleeve);
			//			setRotationAngle(left_sleeve, 0.0F, 0.0F, -0.1745F);
			//			left_sleeve.texOffs(28, 38).addBox(-0.5F, -2.0F, -2.5F, 5.0F, 5.0F, 5.0F, 0.0F, true);

			PartDefinition left_sleeve = left_arm.addOrReplaceChild("left_sleeve", CubeListBuilder.create()
							.texOffs(28, 38)
							.addBox(-0.5F, -2.0F, -2.5F, 5.0F, 5.0F, 5.0F,
									new CubeDeformation(0.0F)),
					PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F,
							0.0F, 0.0F, -0.1745F));

			//left_cuff = new ModelRenderer(this);
			//			left_cuff.setPos(1.0F, 3.0F, 0.0F);
			//			left_sleeve.addChild(left_cuff);
			//			setRotationAngle(left_cuff, 0.0F, -0.0873F, -0.0873F);
			//			left_cuff.texOffs(28, 48).addBox(-1.5F, -0.5F, -3.0F, 6.0F, 2.0F, 6.0F, 0.0F, true);

			PartDefinition left_cuff = left_sleeve.addOrReplaceChild("left_cuff", CubeListBuilder.create()
							.texOffs(28, 48)
							.addBox(-1.5F, -0.5F, -3.0F, 6.0F, 2.0F, 6.0F,
									new CubeDeformation(0.0F)),
					PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F,
							0.0F, -0.0873F, -0.0873F));

			//			cloak = new ModelRenderer(this);
			//			cloak.setPos(2.0F, 12.0F, 0.0F);
			//			body.addChild(cloak);
			//			cloak.texOffs(0, 41).addBox(-6.5F, -12.499F, -2.501F, 9.0F, 15.0F, 5.0F, 0.0F, false);
			//			cloak.texOffs(28, 56).addBox(-7.0F, -12.5F, -3.0F, 10.0F, 8.0F, 6.0F, 0.0F, false);

			PartDefinition cloak = body.addOrReplaceChild("cloak", CubeListBuilder.create()
							.texOffs(0, 41)
							.addBox(-6.5F, -12.499F, -2.501F, 9.0F, 15.0F, 5.0F,
									new CubeDeformation(0.0F)),
					PartPose.offsetAndRotation(2.0F, 12.0F, 0.0F,
							0.0F, 0.0F, 0.0F));

			//			left_side = new ModelRenderer(this);
			//			left_side.setPos(-5.0F, -4.5F, -3.0F);
			//			cloak.addChild(left_side);
			//			setRotationAngle(left_side, 0.0F, 0.0F, 0.1745F);
			//			left_side.texOffs(0, 64).addBox(-2.0F, 0.0F, 0.01F, 2.0F, 10.0F, 6.0F, 0.0F, false);

			PartDefinition left_side = cloak.addOrReplaceChild("left_side", CubeListBuilder.create()
							.texOffs(0, 64)
							.addBox(-2.0F, 0.0F, 0.01F, 2.0F, 10.0F, 6.0F,
									new CubeDeformation(0.0F)),
					PartPose.offsetAndRotation(-5.0F, -4.5F, -3.0F,
							0.0F, 0.0F, 0.1745F));

			//			right_side = new ModelRenderer(this);
			//			right_side.setPos(1.0F, -4.5F, -3.0F);
			//			cloak.addChild(right_side);
			//			setRotationAngle(right_side, 0.0F, 0.0F, -0.1745F);
			//			right_side.texOffs(0, 64).addBox(0.0F, 0.0F, 0.01F, 2.0F, 10.0F, 6.0F, 0.0F, false);

			PartDefinition right_side = cloak.addOrReplaceChild("right_side", CubeListBuilder.create()
							.texOffs(0, 64)
							.addBox(0.0F, 0.0F, 0.01F, 2.0F, 10.0F, 6.0F,
									new CubeDeformation(0.0F)),
					PartPose.offsetAndRotation(1.0F, -4.5F, -3.0F,
							0.0F, 0.0F, -0.1745F));

			//			back_side = new ModelRenderer(this);
			//			back_side.setPos(-2.0F, -4.5F, 1.0F);
			//			cloak.addChild(back_side);
			//			setRotationAngle(back_side, 0.1745F, 0.0F, 0.0F);
			//			back_side.texOffs(17, 70).addBox(-5.01F, 0.0F, 0.0F, 10.0F, 12.0F, 2.0F, 0.0F, false);

			PartDefinition back_side = cloak.addOrReplaceChild("back_side", CubeListBuilder.create()
							.texOffs(17, 70)
							.addBox(-5.01F, 0.0F, 0.0F, 10.0F, 12.0F, 2.0F,
									new CubeDeformation(0.0F)),
					PartPose.offsetAndRotation(-2.0F, -4.5F, 1.0F,
							0.1745F, 0.0F, 0.0F));
		}

		if (slot == EquipmentSlot.HEAD) {
			//			hat = new ModelRenderer(this);
			//			hat.setPos(0.0F, -7.0F, 0.0F);
			//			head.addChild(hat);
			//			setRotationAngle(hat, -0.1745F, 0.0F, -0.0873F);
			//			hat.texOffs(0, 0).addBox(-7.0F, -1.0F, -7.0F, 14.0F, 2.0F, 14.0F, 0.0F, false);

			PartDefinition hat_ = head.addOrReplaceChild("hat_", CubeListBuilder.create()
							.texOffs(0, 0)
							.addBox(-7.0F, -1.0F, -7.0F, 14.0F, 2.0F, 14.0F,
									new CubeDeformation(0.0F)),
					PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F,
							-0.1745F, 0.0F, -0.0873F));

			//			hatMid = new ModelRenderer(this);
			//			hatMid.setPos(0.0F, 0.0F, 0.0F);
			//			hat.addChild(hatMid);
			//			setRotationAngle(hatMid, -0.0873F, 0.0F, 0.0873F);
			//			hatMid.texOffs(0, 16).addBox(-4.5F, -5.75F, -4.5F, 9.0F, 6.0F, 9.0F, 0.0F, false);

			PartDefinition hatMid = hat_.addOrReplaceChild("hatMid", CubeListBuilder.create()
							.texOffs(0, 16)
							.addBox(-4.5F, -5.75F, -4.5F, 9.0F, 6.0F, 9.0F,
									new CubeDeformation(0.0F)),
					PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F,
							-0.0873F, 0.0F, 0.0873F));

			//			hatUpper = new ModelRenderer(this);
			//			hatUpper.setPos(0.0F, -5.0F, 0.0F);
			//			hatMid.addChild(hatUpper);
			//			setRotationAngle(hatUpper, -0.1745F, 0.0F, 0.0436F);
			//			hatUpper.texOffs(0, 31).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 4.0F, 6.0F, 0.0F, false);

			PartDefinition hatUpper = hatMid.addOrReplaceChild("hatUpper", CubeListBuilder.create()
							.texOffs(0, 31)
							.addBox(-3.0F, -4.0F, -3.0F, 6.0F, 4.0F, 6.0F,
									new CubeDeformation(0.0F)),
					PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F,
							-0.1745F, 0.0F, 0.0436F));

			//			hatTip = new ModelRenderer(this);
			//			hatTip.setPos(0.0F, -3.0F, 0.0F);
			//			hatUpper.addChild(hatTip);
			//			setRotationAngle(hatTip, -0.2618F, 0.0F, 0.0873F);
			//			hatTip.texOffs(24, 31).addBox(-1.5F, -4.5F, -1.5F, 3.0F, 4.0F, 3.0F, 0.0F, false);

			PartDefinition hatTip = hatUpper.addOrReplaceChild("hatTip", CubeListBuilder.create()
							.texOffs(24, 31)
							.addBox(-1.5F, -4.5F, -1.5F, 3.0F, 4.0F, 3.0F,
									new CubeDeformation(0.0F)),
					PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F,
							-0.2618F, 0.0F, 0.0873F));
		}


		return LayerDefinition.create(mesh, 64, 128);
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
}