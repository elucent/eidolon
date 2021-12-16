package elucent.eidolon.item.model;

// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class UnholySymbolModel extends EntityModel<Entity> {
//	private final ModelPart model;
//	private final ModelPart watchTop;
//	private final ModelPart cube_r1;
//	private final ModelPart cube_r2;
//	private final ModelPart cube_r3;
//	private final ModelPart cube_r4;
//	private final ModelPart cube_r5;
//	private final ModelPart cube_r6;
//	private final ModelPart cube_r7;
//	private final ModelPart cube_r8;
//	private final ModelPart cube_r9;
//	private final ModelPart cube_r10;
//	private final ModelPart cube_r11;
//	private final ModelPart cube_r12;
//	private final ModelPart cube_r13;
//	private final ModelPart cube_r14;
//	private final ModelPart watchBottom;
//	private final ModelPart cube_r15;
//	private final ModelPart cube_r16;
//	private final ModelPart cube_r17;
//	private final ModelPart cube_r18;
//	private final ModelPart cube_r19;
//	private final ModelPart cube_r20;
//	private final ModelPart cube_r21;
//	private final ModelPart cube_r22;
//	private final ModelPart cube_r23;
//	private final ModelPart cube_r24;

	public UnholySymbolModel() {
//		texWidth = 32;
//		texHeight = 16;
//
//		model = new ModelPart(this);
//		model.setPos(0.0F, 29.0F, 2.0F);
//		setRotationAngle(model, -1.5708F, 0.0F, 0.0F);
//		model.texOffs(12, 4).addBox(-1.5F, 0.55F, 3.0F, 3.0F, 3.0F, 2.0F, 0.0F, false);
//
//		watchTop = new ModelPart(this);
//		watchTop.setPos(0.0F, 2.0F, 4.0F);
//		model.addChild(watchTop);
//		setRotationAngle(watchTop, 0.0436F, 0.0F, 0.0F);
//		watchTop.texOffs(12, 0).addBox(-3.5F, 0.0F, -6.0F, 7.0F, 1.0F, 3.0F, 0.0F, false);
//		watchTop.texOffs(22, 4).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 1.0F, 2.0F, 0.0F, false);
//
//		cube_r1 = new ModelPart(this);
//		cube_r1.setPos(0.5F, 1.5F, -1.0F);
//		watchTop.addChild(cube_r1);
//		setRotationAngle(cube_r1, 0.0F, -0.3142F, 0.0F);
//		cube_r1.texOffs(12, 9).addBox(1.75F, -0.97F, -3.65F, 1.0F, 1.0F, 1.0F, 0.0F, true);
//
//		cube_r2 = new ModelPart(this);
//		cube_r2.setPos(3.5901F, 1.05F, -3.2292F);
//		watchTop.addChild(cube_r2);
//		setRotationAngle(cube_r2, 0.0F, -2.1991F, 0.0F);
//		cube_r2.texOffs(12, 9).addBox(-3.275F, -0.53F, 3.1F, 1.0F, 1.0F, 1.0F, 0.0F, true);
//
//		cube_r3 = new ModelPart(this);
//		cube_r3.setPos(-3.5901F, 1.05F, -3.2292F);
//		watchTop.addChild(cube_r3);
//		setRotationAngle(cube_r3, 0.0F, 2.1991F, 0.0F);
//		cube_r3.texOffs(12, 13).addBox(2.275F, -0.51F, 3.1F, 1.0F, 1.0F, 1.0F, 0.0F, false);
//
//		cube_r4 = new ModelPart(this);
//		cube_r4.setPos(-0.5F, 1.5F, -1.0F);
//		watchTop.addChild(cube_r4);
//		setRotationAngle(cube_r4, 0.0F, 0.3142F, 0.0F);
//		cube_r4.texOffs(12, 13).addBox(-2.75F, -0.97F, -3.65F, 1.0F, 1.0F, 1.0F, 0.0F, false);
//
//		cube_r5 = new ModelPart(this);
//		cube_r5.setPos(0.091F, 0.0F, -1.8839F);
//		watchTop.addChild(cube_r5);
//		setRotationAngle(cube_r5, 0.0F, -0.7854F, 0.0F);
//		cube_r5.texOffs(22, 4).addBox(-3.4F, -0.005F, -0.25F, 3.0F, 1.0F, 2.0F, 0.0F, true);
//
//		cube_r6 = new ModelPart(this);
//		cube_r6.setPos(0.0F, 0.0F, -2.0F);
//		watchTop.addChild(cube_r6);
//		setRotationAngle(cube_r6, 0.0F, 0.7854F, 0.0F);
//		cube_r6.texOffs(22, 4).addBox(0.259F, -0.005F, -0.2339F, 3.0F, 1.0F, 2.0F, 0.0F, false);
//
//		cube_r7 = new ModelPart(this);
//		cube_r7.setPos(0.091F, 0.0F, -7.1161F);
//		watchTop.addChild(cube_r7);
//		setRotationAngle(cube_r7, 0.0F, -2.3562F, 0.0F);
//		cube_r7.texOffs(22, 4).addBox(0.4F, -0.005F, -0.25F, 3.0F, 1.0F, 2.0F, 0.0F, true);
//
//		cube_r8 = new ModelPart(this);
//		cube_r8.setPos(-0.091F, 0.0F, -7.1161F);
//		watchTop.addChild(cube_r8);
//		setRotationAngle(cube_r8, 0.0F, 2.3562F, 0.0F);
//		cube_r8.texOffs(22, 4).addBox(-3.4F, -0.005F, -0.25F, 3.0F, 1.0F, 2.0F, 0.0F, false);
//
//		cube_r9 = new ModelPart(this);
//		cube_r9.setPos(0.0F, 0.0F, -7.0F);
//		watchTop.addChild(cube_r9);
//		setRotationAngle(cube_r9, 0.0F, 3.1416F, 0.0F);
//		cube_r9.texOffs(22, 4).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 1.0F, 2.0F, 0.0F, false);
//
//		cube_r10 = new ModelPart(this);
//		cube_r10.setPos(2.25F, 1.0F, -7.5F);
//		watchTop.addChild(cube_r10);
//		setRotationAngle(cube_r10, 1.5708F, -0.3142F, 0.0F);
//		cube_r10.texOffs(4, 8).addBox(-0.5F, -0.01F, -0.5F, 1.0F, 7.0F, 1.0F, 0.0F, true);
//
//		cube_r11 = new ModelPart(this);
//		cube_r11.setPos(2.25F, 1.0F, -7.5F);
//		watchTop.addChild(cube_r11);
//		setRotationAngle(cube_r11, 1.5708F, -0.9425F, 0.0F);
//		cube_r11.texOffs(4, 8).addBox(-0.5F, 0.0F, -0.51F, 1.0F, 7.0F, 1.0F, 0.0F, true);
//
//		cube_r12 = new ModelPart(this);
//		cube_r12.setPos(-3.5F, 1.0F, -3.25F);
//		watchTop.addChild(cube_r12);
//		setRotationAngle(cube_r12, 1.5708F, 1.5708F, 0.0F);
//		cube_r12.texOffs(0, 8).addBox(-0.45F, 0.0F, -0.52F, 1.0F, 7.0F, 1.0F, 0.0F, false);
//
//		cube_r13 = new ModelPart(this);
//		cube_r13.setPos(-2.25F, 1.0F, -7.5F);
//		watchTop.addChild(cube_r13);
//		setRotationAngle(cube_r13, 1.5708F, 0.9425F, 0.0F);
//		cube_r13.texOffs(8, 8).addBox(-0.5F, 0.0F, -0.49F, 1.0F, 7.0F, 1.0F, 0.0F, false);
//
//		cube_r14 = new ModelPart(this);
//		cube_r14.setPos(-2.25F, 1.0F, -7.5F);
//		watchTop.addChild(cube_r14);
//		setRotationAngle(cube_r14, 1.5708F, 0.3142F, 0.0F);
//		cube_r14.texOffs(0, 8).addBox(-0.5F, 0.0F, -0.53F, 1.0F, 7.0F, 1.0F, 0.0F, false);
//
//		watchBottom = new ModelPart(this);
//		watchBottom.setPos(0.0F, 0.0F, 0.0F);
//		model.addChild(watchBottom);
//		watchBottom.texOffs(12, 0).addBox(-3.5F, 0.0F, -2.0F, 7.0F, 1.0F, 3.0F, 0.0F, false);
//		watchBottom.texOffs(22, 4).addBox(-1.5F, 0.0F, 1.0F, 3.0F, 1.0F, 2.0F, 0.0F, false);
//
//		cube_r15 = new ModelPart(this);
//		cube_r15.setPos(1.9F, 1.5F, -4.0F);
//		watchBottom.addChild(cube_r15);
//		setRotationAngle(cube_r15, 2.3562F, 0.0F, -1.5708F);
//		cube_r15.texOffs(0, 4).addBox(-0.5F, 2.3207F, -5.45F, 1.0F, 3.0F, 1.0F, 0.0F, false);
//		cube_r15.texOffs(8, 4).addBox(-0.5F, 2.3207F, 2.175F, 1.0F, 3.0F, 1.0F, 0.0F, false);
//
//		cube_r16 = new ModelPart(this);
//		cube_r16.setPos(1.9F, 1.5F, -4.0F);
//		watchBottom.addChild(cube_r16);
//		setRotationAngle(cube_r16, 0.7854F, 0.0F, -1.5708F);
//		cube_r16.texOffs(0, 4).addBox(-0.5F, -0.3543F, 7.125F, 1.0F, 3.0F, 1.0F, 0.0F, false);
//		cube_r16.texOffs(4, 4).addBox(-0.5F, -0.3543F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
//
//		cube_r17 = new ModelPart(this);
//		cube_r17.setPos(2.0F, 1.5F, -4.0F);
//		watchBottom.addChild(cube_r17);
//		setRotationAngle(cube_r17, 0.0F, 0.0F, -1.5708F);
//		cube_r17.texOffs(8, 0).addBox(-0.5F, -4.0F, -0.6F, 1.0F, 4.0F, 1.0F, 0.0F, false);
//
//		cube_r18 = new ModelPart(this);
//		cube_r18.setPos(3.5F, 1.5F, -2.5F);
//		watchBottom.addChild(cube_r18);
//		setRotationAngle(cube_r18, 1.5708F, 0.0F, -1.5708F);
//		cube_r18.texOffs(4, 0).addBox(-0.5F, 0.0F, 6.6F, 1.0F, 4.0F, 1.0F, 0.0F, false);
//		cube_r18.texOffs(4, 0).addBox(-0.5F, 0.025F, -0.625F, 1.0F, 4.0F, 1.0F, 0.0F, false);
//
//		cube_r19 = new ModelPart(this);
//		cube_r19.setPos(-2.0F, 1.5F, 3.0F);
//		watchBottom.addChild(cube_r19);
//		setRotationAngle(cube_r19, 0.0F, 0.0F, -1.5708F);
//		cube_r19.texOffs(0, 0).addBox(-0.5F, 0.0F, -0.375F, 1.0F, 4.0F, 1.0F, 0.0F, false);
//
//		cube_r20 = new ModelPart(this);
//		cube_r20.setPos(0.091F, 0.0F, 2.1161F);
//		watchBottom.addChild(cube_r20);
//		setRotationAngle(cube_r20, 0.0F, -0.7854F, 0.0F);
//		cube_r20.texOffs(22, 4).addBox(-3.4F, -0.005F, -0.25F, 3.0F, 1.0F, 2.0F, 0.0F, true);
//
//		cube_r21 = new ModelPart(this);
//		cube_r21.setPos(0.0F, 0.0F, 2.0F);
//		watchBottom.addChild(cube_r21);
//		setRotationAngle(cube_r21, 0.0F, 0.7854F, 0.0F);
//		cube_r21.texOffs(22, 4).addBox(0.259F, -0.005F, -0.2339F, 3.0F, 1.0F, 2.0F, 0.0F, false);
//
//		cube_r22 = new ModelPart(this);
//		cube_r22.setPos(0.091F, 0.0F, -3.1161F);
//		watchBottom.addChild(cube_r22);
//		setRotationAngle(cube_r22, 0.0F, -2.3562F, 0.0F);
//		cube_r22.texOffs(22, 4).addBox(0.4F, -0.005F, -0.25F, 3.0F, 1.0F, 2.0F, 0.0F, true);
//
//		cube_r23 = new ModelPart(this);
//		cube_r23.setPos(-0.091F, 0.0F, -3.1161F);
//		watchBottom.addChild(cube_r23);
//		setRotationAngle(cube_r23, 0.0F, 2.3562F, 0.0F);
//		cube_r23.texOffs(22, 4).addBox(-3.4F, -0.005F, -0.25F, 3.0F, 1.0F, 2.0F, 0.0F, false);
//
//		cube_r24 = new ModelPart(this);
//		cube_r24.setPos(0.0F, 0.0F, -3.0F);
//		watchBottom.addChild(cube_r24);
//		setRotationAngle(cube_r24, 0.0F, 3.1416F, 0.0F);
//		cube_r24.texOffs(22, 4).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 1.0F, 2.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
//		model.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}