package elucent.eidolon.codex;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import elucent.eidolon.ClientEvents;
import elucent.eidolon.Eidolon;
import elucent.eidolon.Events;
import elucent.eidolon.network.AttemptCastPacket;
import elucent.eidolon.network.Networking;
import elucent.eidolon.particle.SignParticleData;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class CodexGui extends Screen {
    public static final CodexGui DUMMY = new CodexGui();
    public static final ResourceLocation CODEX_BACKGROUND = new ResourceLocation(Eidolon.MODID, "textures/gui/codex_bg.png");
    static int xSize = 312, ySize = 208;
    List<Sign> chant = new ArrayList<>();

    Chapter currentChapter;
    int currentPage = 0;

    static CodexGui INSTANCE = null;
    public static CodexGui getInstance() {
        for (Category cat : CodexChapters.categories) cat.reset();
        if (INSTANCE != null) return INSTANCE;
        return INSTANCE = new CodexGui();
    }

    protected CodexGui() {
        super(new TranslationTextComponent("gui.eidolon.codex.title"));
        currentChapter = CodexChapters.NATURE_INDEX;
    }

    protected void resetPages() {
        Page left = currentChapter.get(currentPage), right = currentChapter.get(currentPage + 1);
        if (left != null) left.reset();
        if (right != null) right.reset();
    }

    protected void changeChapter(Chapter next) {
        currentChapter = next;
        currentPage = 0;
    }

    public void addToChant(Sign sign) {
        if (this.chant.size() < 9) this.chant.add(sign);
    }

    protected void renderChant(MatrixStack mStack, int x, int y, int mouseX, int mouseY, float pticks) {
        int chantWidth = 32 + 24 * chant.size();
        int baseX = x + this.xSize / 2 - chantWidth / 2, baseY = y + 180;

        RenderSystem.enableBlend();
        RenderSystem.alphaFunc(GL11.GL_GEQUAL, 1f / 256f);

        int bgx = baseX;
        blit(mStack, bgx, baseY, 256, 208, 16, 32, 512, 512);
        bgx += 16;
        for (int i = 0; i < chant.size(); i ++) {
            blit(mStack, bgx, baseY, 272, 208, 24, 32, 512, 512);
            blit(mStack, bgx, baseY, 312, 208, 24, 24, 512, 512);
            bgx += 24;
        }
        blit(mStack, bgx, baseY, 296, 208, 16, 32, 512, 512);
        bgx += 24;
        boolean chantHover = mouseX >= bgx && mouseY >= baseY - 4 && mouseX <= bgx + 32 && mouseY <= baseY + 28;
        blit(mStack, bgx, baseY - 4, 336, chantHover ? 240 : 208, 32, 32, 512, 512);
        bgx += 36;
        boolean cancelHover = mouseX >= bgx && mouseY >= baseY - 4 && mouseX <= bgx + 32 && mouseY <= baseY + 28;
        blit(mStack, bgx, baseY - 4, 368, cancelHover ? 240 : 208, 32, 32, 512, 512);
        if (chantHover) renderTooltip(mStack, new TranslationTextComponent("eidolon.codex.chant_hover"), mouseX, mouseY);
        if (cancelHover) renderTooltip(mStack, new TranslationTextComponent("eidolon.codex.cancel_hover"), mouseX, mouseY);

        RenderSystem.enableBlend();
        RenderSystem.alphaFunc(GL11.GL_GEQUAL, 1f / 256f);
        Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        bgx = baseX + 16;
        Tessellator tess = Tessellator.getInstance();
        for (int i = 0; i < chant.size(); i ++) {
            Sign sign = chant.get(i);
            RenderUtil.litQuad(mStack, IRenderTypeBuffer.getImpl(tess.getBuffer()), bgx + 4, baseY + 4, 16, 16,
                sign.getRed(), sign.getGreen(), sign.getBlue(), Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(sign.getSprite()));
            tess.draw();
            bgx += 24;
        }
        bgx = baseX + 16;
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        float flicker = 0.875f + 0.125f * (float)Math.sin(Math.toRadians(12 * ClientEvents.getClientTicks()));
        for (int i = 0; i < chant.size(); i ++) {
            Sign sign = chant.get(i);
            RenderUtil.litQuad(mStack, IRenderTypeBuffer.getImpl(tess.getBuffer()), bgx + 4, baseY + 4, 16, 16,
                sign.getRed() * flicker, sign.getGreen() * flicker, sign.getBlue() * flicker, Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(sign.getSprite()));
            tess.draw();
            bgx += 24;
        }
        RenderSystem.defaultAlphaFunc();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        Minecraft.getInstance().getTextureManager().bindTexture(BACKGROUND_LOCATION);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bindTexture(CODEX_BACKGROUND);

        this.width = mc.getMainWindow().getScaledWidth();
        this.height = mc.getMainWindow().getScaledHeight();
        int guiLeft = (width - xSize) / 2, guiTop = (height - ySize) / 2;
        blit(matrixStack, guiLeft, guiTop, 0, 256, xSize, ySize, 512, 512);

        for (int i = 0; i < CodexChapters.categories.size(); i ++) {
            int y = guiTop + 28 + (i % 8) * 20;
            CodexChapters.categories.get(i).draw(this, matrixStack, guiLeft + (i >= 8 ? 304 : 8), y, i >= 8, mouseX, mouseY);
        }

        mc.getTextureManager().bindTexture(CODEX_BACKGROUND);
        blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize, 512, 512);
        Page left = currentChapter.get(currentPage), right = currentChapter.get(currentPage + 1);
        if (left != null) left.fullRender(this, matrixStack, guiLeft + 14, guiTop + 24, mouseX, mouseY);
        if (right != null) right.fullRender(this, matrixStack, guiLeft + 170, guiTop + 24, mouseX, mouseY);

        mc.getTextureManager().bindTexture(CODEX_BACKGROUND);
        if (currentPage > 0) { // left arrow
            int x = 10, y = 169;
            int v = 208;
            if (mouseX >= guiLeft + x && mouseY >= guiTop + y && mouseX <= guiLeft + x + 32 && mouseY <= guiTop + y + 16) v += 18;
            blit(matrixStack, guiLeft + x, guiTop + y, 128, v, 32, 18, 512, 512);
        }
        if (currentPage + 2 < currentChapter.size()) { // right arrow
            int x = 270, y = 169;
            int v = 208;
            if (mouseX >= guiLeft + x && mouseY >= guiTop + y && mouseX <= guiLeft + x + 32 && mouseY <= guiTop + y + 16) v += 18;
            blit(matrixStack, guiLeft + x, guiTop + y, 160, v, 32, 18, 512, 512);
        }

        if (chant.size() > 0) renderChant(matrixStack, guiLeft, guiTop, mouseX, mouseY, partialTicks);


        for (int i = 0; i < CodexChapters.categories.size(); i ++) {
            int y = guiTop + 28 + (i % 8) * 20;
            CodexChapters.categories.get(i).drawTooltip(this, matrixStack, guiLeft + (i >= 8 ? 304 : 8), y, i >= 8, mouseX, mouseY);
        }
    }

    protected boolean interactChant(int x, int y, int mouseX, int mouseY) {
        int chantWidth = 32 + 24 * chant.size();
        int baseX = x + this.xSize / 2 - chantWidth / 2, baseY = y + 180;
        int bgx = baseX + chantWidth + 8;
        boolean chantHover = mouseX >= bgx && mouseY >= baseY - 4 && mouseX <= bgx + 32 && mouseY <= baseY + 28;
        bgx += 36;
        boolean cancelHover = mouseX >= bgx && mouseY >= baseY - 4 && mouseX <= bgx + 32 && mouseY <= baseY + 28;
        if (chantHover) {
            PlayerEntity player = Minecraft.getInstance().player;
            World world = Minecraft.getInstance().world;
            Networking.sendToServer(new AttemptCastPacket(player, chant));
            chant.clear();
            player.playSound(SoundEvents.UI_BUTTON_CLICK, SoundCategory.NEUTRAL, 1.0f, 1.0f);
            this.closeScreen();
            return true;
        }
        if (cancelHover) {
            chant.clear();
            Minecraft.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK, SoundCategory.NEUTRAL, 1.0f, 1.0f);
            return true;
        }
        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            Minecraft mc = Minecraft.getInstance();
            this.width = mc.getMainWindow().getScaledWidth();
            this.height = mc.getMainWindow().getScaledHeight();
            int guiLeft = (width - xSize) / 2, guiTop = (height - ySize) / 2;

            if (currentPage > 0) { // left arrow
                int x = guiLeft + 10, y = guiTop + 169;
                if (mouseX >= x && mouseY >= y && mouseX <= x + 32 && mouseY <= y + 16) {
                    currentPage -= 2;
                    Minecraft.getInstance().player.playSound(SoundEvents.ITEM_BOOK_PAGE_TURN, SoundCategory.NEUTRAL, 1.0f, 1.0f);
                    resetPages();
                    return true;
                }
            }
            if (currentPage + 2 < currentChapter.size()) { // right arrow
                int x = guiLeft + 270, y = guiTop + 169;
                if (mouseX >= x && mouseY >= y && mouseX <= x + 32 && mouseY <= y + 16) {
                    currentPage += 2;
                    Minecraft.getInstance().player.playSound(SoundEvents.ITEM_BOOK_PAGE_TURN, SoundCategory.NEUTRAL, 1.0f, 1.0f);
                    resetPages();
                    return true;
                }
            }

            for (int i = 0; i < CodexChapters.categories.size(); i ++) {
                int y = guiTop + 28 + (i % 8) * 20;
                if (CodexChapters.categories.get(i).click(this, guiLeft + (i >= 8 ? 304 : 8), y, i >= 8, (int)mouseX, (int)mouseY)) return true;
            }

            Page left = currentChapter.get(currentPage), right = currentChapter.get(currentPage + 1);
            if (left != null) if (left.click(this,guiLeft + 14, guiTop + 24, (int)mouseX, (int)mouseY)) return true;
            if (right != null) if (right.click(this,guiLeft + 170, guiTop + 24, (int)mouseX, (int)mouseY)) return true;

            if (chant.size() > 0 && interactChant(guiLeft, guiTop, (int)mouseX, (int)mouseY)) return true;
        }
        return false;
    }

    @Override
    public void renderTooltip(MatrixStack mStack, ItemStack stack, int x, int y) {
        if (!stack.isEmpty()) super.renderTooltip(mStack, stack, x, y);
    }
}
