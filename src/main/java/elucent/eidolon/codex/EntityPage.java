package elucent.eidolon.codex;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import elucent.eidolon.Eidolon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityPage extends Page {
    public static final ResourceLocation BACKGROUND = new ResourceLocation(Eidolon.MODID, "textures/gui/codex_entity_page.png");

    EntityType type;

    public <T extends Entity> EntityPage(EntityType<T> type) {
        super(BACKGROUND);
        this.type = type;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(CodexGui gui, PoseStack mStack, int x, int y, int mouseX, int mouseY) {
        Entity e = type.create(Minecraft.getInstance().level);
        EntityRenderer renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(e);
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        Tesselator tess = Tesselator.getInstance();
        mStack.pushPose();
        mStack.translate(x + 64, y + 136, 64);
        mStack.mulPose(Vector3f.XP.rotationDegrees(-15));
        mStack.mulPose(Vector3f.YP.rotationDegrees(-30));
        float scale = 112 / e.getBbHeight();
        mStack.scale(scale, -scale, scale);
        RenderSystem.setShader(GameRenderer::getRendertypeEntityAlphaShader);
        RenderSystem.setupShaderLights(RenderSystem.getShader());
        RenderSystem.setShaderTexture(0, renderer.getTextureLocation(e));
        renderer.render(e, e.getYRot(), 0, mStack, MultiBufferSource.immediate(tess.getBuilder()), 0xf000f0);
        tess.end();
        mStack.popPose();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
    }
}
