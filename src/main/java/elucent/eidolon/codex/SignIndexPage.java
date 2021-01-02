package elucent.eidolon.codex;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import elucent.eidolon.ClientEvents;
import elucent.eidolon.Eidolon;
import elucent.eidolon.Events;
import elucent.eidolon.Registry;
import elucent.eidolon.capability.IKnowledge;
import elucent.eidolon.capability.KnowledgeProvider;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.util.ColorUtil;
import elucent.eidolon.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

public class SignIndexPage extends Page {
    public static final ResourceLocation BACKGROUND = new ResourceLocation(Eidolon.MODID, "textures/gui/codex_sign_index_page.png");
    SignEntry[] entries;

    public static class SignEntry {
        Chapter chapter;
        Sign sign;

        public SignEntry(Chapter chapter, Sign sign) {
            this.chapter = chapter;
            this.sign = sign;
        }
    }

    public SignIndexPage(SignEntry... pages) {
        super(BACKGROUND);
        this.entries = pages;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean click(CodexGui gui, int x, int y, int mouseX, int mouseY) {
        PlayerEntity entity = Minecraft.getInstance().player;
        IKnowledge knowledge = entity.getCapability(KnowledgeProvider.CAPABILITY, null).resolve().get();
        for (int i = 0; i < entries.length; i ++) {
            int xx = x + 8 + (i % 2) * 56, yy = y + 4 + (i / 2) * 52;
            if (mouseX >= xx + 38 && mouseY >= yy + 38 && mouseX <= xx + 50 && mouseY <= yy + 50) {
                gui.changeChapter(entries[i].chapter);
                Minecraft.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK, SoundCategory.NEUTRAL, 1.0f, 1.0f);
                return true;
            }
            else if (knowledge.knowsSign(entries[i].sign) && mouseX >= xx && mouseX <= xx + 48 && mouseY >= yy && mouseY <= yy + 48) {
                gui.addToChant(entries[i].sign);
                entity.playSound(Registry.SELECT_SIGN.get(), SoundCategory.NEUTRAL, 0.5f, entity.world.rand.nextFloat() * 0.25f + 0.75f);
                return true;
            }
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    static void colorBlit(MatrixStack mStack, int x, int y, int uOffset, int vOffset, int width, int height, int textureWidth, int textureHeight, int color) {
        Matrix4f matrix = mStack.getLast().getMatrix();
        int maxX = x + width, maxY = y + height;
        float minU = (float)uOffset / textureWidth, minV = (float)vOffset / textureHeight;
        float maxU = minU + (float)width / textureWidth, maxV = minV + (float)height / textureHeight;
        int r = ColorUtil.getRed(color),
            g = ColorUtil.getGreen(color),
            b = ColorUtil.getBlue(color);
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(matrix, (float)x, (float)maxY, 0).tex(minU, maxV).color(r, g, b, 255).endVertex();
        bufferbuilder.pos(matrix, (float)maxX, (float)maxY, 0).tex(maxU, maxV).color(r, g, b, 255).endVertex();
        bufferbuilder.pos(matrix, (float)maxX, (float)y, 0).tex(maxU, minV).color(r, g, b, 255).endVertex();
        bufferbuilder.pos(matrix, (float)x, (float)y, 0).tex(minU, minV).color(r, g, b, 255).endVertex();
        bufferbuilder.finishDrawing();
        RenderSystem.enableAlphaTest();
        WorldVertexBufferUploader.draw(bufferbuilder);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(CodexGui gui, MatrixStack mStack, int x, int y, int mouseX, int mouseY) {
        PlayerEntity entity = Minecraft.getInstance().player;
        IKnowledge knowledge = entity.getCapability(KnowledgeProvider.CAPABILITY, null).resolve().get();
        for (int i = 0; i < entries.length; i ++) {
            Minecraft.getInstance().getTextureManager().bindTexture(BACKGROUND);
            int xx = x + 8 + (i % 2) * 56, yy = y + 4 + (i / 2) * 52;
            Sign sign = entries[i].sign;
            boolean hover = knowledge.knowsSign(sign) && mouseX >= xx && mouseX <= xx + 48 && mouseY >= yy && mouseY <= yy + 48;
            boolean infoHover = mouseX >= xx + 38 && mouseY >= yy + 38 && mouseX <= xx + 50 && mouseY <= yy + 50;
            gui.blit(mStack, xx, yy, knowledge.knowsSign(entries[i].sign) ? 128 : 176, 0, 48, 48);

            if (knowledge.knowsSign(sign)) {
                Tessellator tess = Tessellator.getInstance();
                RenderSystem.enableBlend();
                RenderSystem.alphaFunc(GL11.GL_GEQUAL, 1f / 256f);
                RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
                if (hover && !infoHover) {
                    mStack.push();
                    mStack.translate(xx + 24, yy + 24, 0);
                    mStack.rotate(Vector3f.ZP.rotationDegrees(ClientEvents.getClientTicks() * 1.5f));
                    colorBlit(mStack, -18, -18, 128, 48, 36, 36, 256, 256, sign.getColor());
                    mStack.pop();
                }
                Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
                for (int j = 0; j < (hover && !infoHover ? 2 : 1); j++) {
                    RenderUtil.litQuad(mStack, IRenderTypeBuffer.getImpl(tess.getBuffer()), xx + 12, yy + 12, 24, 24,
                        sign.getRed(), sign.getGreen(), sign.getBlue(), Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(sign.getSprite()));
                    tess.draw();
                }
                RenderSystem.defaultAlphaFunc();
                RenderSystem.disableBlend();
                RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

                Minecraft.getInstance().getTextureManager().bindTexture(BACKGROUND);
                gui.blit(mStack, xx + 38, yy + 38, infoHover ? 188 : 176, 48, 12, 14);

                if (infoHover) {
                    gui.renderTooltip(mStack, new TranslationTextComponent("eidolon.codex.sign_suffix", new TranslationTextComponent(sign.getRegistryName().getNamespace() + ".sign." + sign.getRegistryName().getPath())), mouseX, mouseY);
                }
            }
        }
    }
}
