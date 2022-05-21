package elucent.eidolon.codex;

import com.mojang.blaze3d.vertex.PoseStack;

import java.util.Optional;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import elucent.eidolon.ClientEvents;
import elucent.eidolon.ClientRegistry;
import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.capability.IKnowledge;
import elucent.eidolon.spell.Rune;
import elucent.eidolon.spell.Runes;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.util.ColorUtil;
import elucent.eidolon.util.RenderUtil;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.BufferBuilder;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.blaze3d.vertex.BufferUploader;
import net.minecraft.client.renderer.texture.TextureAtlas;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RuneIndexPage extends Page {
    public static final ResourceLocation BACKGROUND = new ResourceLocation(Eidolon.MODID, "textures/gui/codex_rune_index_page.png");
    Rune[] runes;
    int scroll = 0;

    public static class SignEntry {
        Chapter chapter;
        Sign sign;

        public SignEntry(Chapter chapter, Sign sign) {
            this.chapter = chapter;
            this.sign = sign;
        }
    }

    public RuneIndexPage() {
        super(BACKGROUND);
        this.runes = Runes.getRunes().<Rune>toArray((i) -> new Rune[i]);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean click(CodexGui gui, int x, int y, int mouseX, int mouseY) {
        Player entity = Minecraft.getInstance().player;
        if (entity.getCapability(IKnowledge.INSTANCE, null).resolve().isEmpty()) return false;
        IKnowledge knowledge = entity.getCapability(IKnowledge.INSTANCE, null).resolve().get();
        for (int i = 0; i < runes.length; i ++) {
            int xx = x + 2 + (i % 6) * 20, yy = y + 2 + (i / 6) * 20;
            if (knowledge.knowsRune(runes[i]) && mouseX >= xx && mouseX <= xx + 16 && mouseY >= yy && mouseY <= yy + 16) {
                gui.addToChant(runes[i]);
                entity.playNotifySound(Registry.SELECT_RUNE.get(), SoundSource.NEUTRAL, 0.5f, entity.level.random.nextFloat() * 0.25f + 0.75f);
                return true;
            }
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    static void colorBlit(PoseStack mStack, int x, int y, int uOffset, int vOffset, int width, int height, int textureWidth, int textureHeight, int color) {
        Matrix4f matrix = mStack.last().pose();
        int maxX = x + width, maxY = y + height;
        float minU = (float)uOffset / textureWidth, minV = (float)vOffset / textureHeight;
        float maxU = minU + (float)width / textureWidth, maxV = minV + (float)height / textureHeight;
        int r = ColorUtil.getRed(color),
            g = ColorUtil.getGreen(color),
            b = ColorUtil.getBlue(color);
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferbuilder.vertex(matrix, (float)x, (float)maxY, 0).uv(minU, maxV).color(r, g, b, 255).endVertex();
        bufferbuilder.vertex(matrix, (float)maxX, (float)maxY, 0).uv(maxU, maxV).color(r, g, b, 255).endVertex();
        bufferbuilder.vertex(matrix, (float)maxX, (float)y, 0).uv(maxU, minV).color(r, g, b, 255).endVertex();
        bufferbuilder.vertex(matrix, (float)x, (float)y, 0).uv(minU, minV).color(r, g, b, 255).endVertex();
        bufferbuilder.end();
        BufferUploader.end(bufferbuilder);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(CodexGui gui, PoseStack mStack, int x, int y, int mouseX, int mouseY) {
    	gui.hoveredRune = null;
        Player entity = Minecraft.getInstance().player;
        Optional<IKnowledge> knowledge = entity.getCapability(IKnowledge.INSTANCE, null).resolve();
        for (int i = 0; i < runes.length; i ++) {
        	RenderSystem.setShaderTexture(0, BACKGROUND);
            int xx = x + 2 + (i % 6) * 20, yy = y + 2 + (i / 6) * 20;
            boolean hover = knowledge.isPresent() && knowledge.get().knowsRune(runes[i]) && mouseX >= xx && mouseX <= xx + 16 && mouseY >= yy && mouseY <= yy + 16;
            if (hover) gui.hoveredRune = runes[i];
            gui.blit(mStack, xx, yy, knowledge.isPresent() && knowledge.get().knowsRune(runes[i]) ? 128 : 148, 0, 20, 20);

            if (knowledge.isPresent() && knowledge.get().knowsRune(runes[i])) {
                Tesselator tess = Tesselator.getInstance();
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
                RenderSystem.setShader(ClientRegistry::getGlowingSpriteShader);
                if (hover) {
                    mStack.pushPose();
                    mStack.translate(xx + 10, yy + 10, 0);
                    mStack.mulPose(Vector3f.ZP.rotationDegrees(ClientEvents.getClientTicks() * 1.5f));
                    mStack.scale(0.5f, 0.5f, 1);
                    colorBlit(mStack, -12, -12, 128, 20, 24, 24, 256, 256, ColorUtil.packColor(255, 255, 255, 255));
                    mStack.popPose();
                }
                RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
                for (int j = 0; j < (hover ? 2 : 1); j++) {
                	RenderUtil.litQuad(mStack, MultiBufferSource.immediate(tess.getBuilder()), xx + 6, yy + 6, 8, 8,
                        1, 1, 1, 0.75f, Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(runes[i].getSprite()));
                    tess.end();
                }
                RenderSystem.disableBlend();
                RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, BACKGROUND);
            }
        }
    }
}
