package elucent.eidolon.codex;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import elucent.eidolon.Eidolon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
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
    public void render(CodexGui gui, MatrixStack mStack, int x, int y, int mouseX, int mouseY) {
        Entity e = type.create(Minecraft.getInstance().world);
        EntityRenderer renderer = Minecraft.getInstance().getRenderManager().getRenderer(e);
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.enableLighting();
        Tessellator tess = Tessellator.getInstance();
        mStack.push();
        mStack.translate(x + 64, y + 136, 64);
        mStack.rotate(Vector3f.XP.rotationDegrees(-15));
        mStack.rotate(Vector3f.YP.rotationDegrees(-30));
        float scale = 112 / e.getHeight();
        mStack.scale(scale, -scale, scale);
        Minecraft.getInstance().getTextureManager().bindTexture(renderer.getEntityTexture(e));
        renderer.render(e, e.rotationYaw, 0, mStack, IRenderTypeBuffer.getImpl(tess.getBuffer()), 0xf000f0);
        tess.draw();
        mStack.pop();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.disableLighting();
    }
}
