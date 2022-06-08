package elucent.eidolon.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.Mth;

public class WraithModel extends EntityModel<WraithEntity> {
	private final ModelRenderer body;
	private final ModelRenderer tail;
	private final ModelRenderer right_arm;
	private final ModelRenderer lower_right_arm;
	private final ModelRenderer right_hand;
	private final ModelRenderer left_arm;
	private final ModelRenderer lower_left_arm;
	private final ModelRenderer left_hand;

	public WraithModel() {
		texWidth = 64;
		texHeight = 64;

		body = new ModelRenderer(this);
		body.setPos(0.0F, 8.0F, 0.0F);
		body.texOffs(0, 0).addBox(-4.0F, -16.0F, -4.0F, 8.0F, 16.0F, 8.0F, 0.0F, false);
		body.texOffs(32, 2).addBox(-3.0F, -15.0F, -3.0F, 6.0F, 15.0F, 6.0F, 0.0F, false);

		tail = new ModelRenderer(this);
		tail.setPos(0.0F, 0.0F, -4.0F);
		body.addChild(tail);
		setRotationAngle(tail, 0.2618F, 0.0F, 0.0F);
		tail.texOffs(0, 24).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 16.0F, 8.0F, 0.0F, false);
		tail.texOffs(32, 26).addBox(-3.0F, 0.2588F, 0.9659F, 6.0F, 15.0F, 6.0F, 0.0F, false);

		right_arm = new ModelRenderer(this);
		right_arm.setPos(-5.5F, -6.5F, 0.0F);
		body.addChild(right_arm);
		setRotationAngle(right_arm, -1.0472F, 0.0F, -0.1745F);
		right_arm.texOffs(0, 48).addBox(-1.5F, -1.5F, -1.0F, 3.0F, 6.0F, 3.0F, 0.0F, false);

		lower_right_arm = new ModelRenderer(this);
		lower_right_arm.setPos(0.0F, 6.0F, 0.0F);
		right_arm.addChild(lower_right_arm);
		setRotationAngle(lower_right_arm, -0.7854F, 0.0F, 0.0F);
		lower_right_arm.texOffs(12, 48).addBox(-1.5F, -2.4749F, -2.6464F, 3.0F, 6.0F, 3.0F, 0.0F, false);

		right_hand = new ModelRenderer(this);
		right_hand.setPos(0.0F, 2.5F, 0.0F);
		lower_right_arm.addChild(right_hand);
		setRotationAngle(right_hand, 0.3927F, 0.0F, 0.0F);
		right_hand.texOffs(25, 48).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 3.0F, 3.0F, 0.0F, false);

		left_arm = new ModelRenderer(this);
		left_arm.setPos(5.5F, -6.5F, 0.0F);
		body.addChild(left_arm);
		setRotationAngle(left_arm, -1.0472F, 0.0F, 0.1745F);
		left_arm.texOffs(0, 48).addBox(-1.5F, -1.5F, -1.0F, 3.0F, 6.0F, 3.0F, 0.0F, true);

		lower_left_arm = new ModelRenderer(this);
		lower_left_arm.setPos(-11.0F, 6.0F, 0.0F);
		left_arm.addChild(lower_left_arm);
		setRotationAngle(lower_left_arm, -0.7854F, 0.0F, 0.0F);
		lower_left_arm.texOffs(12, 48).addBox(9.5F, -2.4749F, -2.6464F, 3.0F, 6.0F, 3.0F, 0.0F, true);

		left_hand = new ModelRenderer(this);
		left_hand.setPos(0.0F, 2.5F, 0.0F);
		lower_left_arm.addChild(left_hand);
		setRotationAngle(left_hand, 0.3927F, 0.0F, 0.0F);
		left_hand.texOffs(25, 48).addBox(9.5F, -1.0F, -1.5F, 3.0F, 3.0F, 3.0F, 0.0F, true);
	}

	@Override
	public void setupAnim(WraithEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		body.y = 8.0f + 1.5f * Mth.sin(ageInTicks / 20.0f * (float)Math.PI);
		body.xRot = (float)Math.toRadians(Mth.clamp(
			10.0f * (float)entity.getDeltaMovement().multiply(1, 0, 1).length() / 0.3f,
			-10.0f, 10.0f
		));
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		body.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}