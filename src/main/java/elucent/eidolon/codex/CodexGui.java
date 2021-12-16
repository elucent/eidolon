package elucent.eidolon.codex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import elucent.eidolon.ClientEvents;
import elucent.eidolon.Eidolon;
import elucent.eidolon.network.AttemptCastPacket;
import elucent.eidolon.network.Networking;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Matrix4f;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

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
        super(new TranslatableComponent("gui.eidolon.codex.title"));
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

    protected void renderChant(PoseStack mStack, int x, int y, int mouseX, int mouseY, float pticks) {
        int chantWidth = 32 + 24 * chant.size();
        int baseX = x + this.xSize / 2 - chantWidth / 2, baseY = y + 180;

        RenderSystem.enableBlend();
        
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
        if (chantHover) renderTooltip(mStack, new TranslatableComponent("eidolon.codex.chant_hover"), mouseX, mouseY);
        if (cancelHover) renderTooltip(mStack, new TranslatableComponent("eidolon.codex.cancel_hover"), mouseX, mouseY);

        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
        bgx = baseX + 16;
        Tesselator tess = Tesselator.getInstance();
        for (int i = 0; i < chant.size(); i ++) {
            Sign sign = chant.get(i);
            RenderUtil.litQuad(mStack, MultiBufferSource.immediate(tess.getBuilder()), bgx + 4, baseY + 4, 16, 16,
                sign.getRed(), sign.getGreen(), sign.getBlue(), Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(sign.getSprite()));
            tess.end();
            bgx += 24;
        }
        bgx = baseX + 16;
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        float flicker = 0.875f + 0.125f * (float)Math.sin(Math.toRadians(12 * ClientEvents.getClientTicks()));
        for (int i = 0; i < chant.size(); i ++) {
            Sign sign = chant.get(i);
            RenderUtil.litQuad(mStack, MultiBufferSource.immediate(tess.getBuilder()), bgx + 4, baseY + 4, 16, 16,
                sign.getRed() * flicker, sign.getGreen() * flicker, sign.getBlue() * flicker, Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(sign.getSprite()));
            tess.end();
            bgx += 24;
        }
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.setShaderTexture(0, BACKGROUND_LOCATION);
    }
    
    boolean hasTooltip = false;
    Matrix4f tooltipMatrix = null;
    Component tooltipText = null;
    int tooltipX = 0, tooltipY = 0;

    @Override
    public void renderTooltip(PoseStack poseStack, Component text, int x, int y) {
       tooltipMatrix = poseStack.last().pose();
       tooltipText = text;
       tooltipX = x;
       tooltipY = y;
       hasTooltip = true;
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    	hasTooltip = false;
        renderBackground(matrixStack);
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.setShaderTexture(0, CODEX_BACKGROUND);

        this.width = mc.getWindow().getGuiScaledWidth();
        this.height = mc.getWindow().getGuiScaledHeight();
        int guiLeft = (width - xSize) / 2, guiTop = (height - ySize) / 2;
        blit(matrixStack, guiLeft, guiTop, 0, 256, xSize, ySize, 512, 512);

        for (int i = 0; i < CodexChapters.categories.size(); i ++) {
            int y = guiTop + 28 + (i % 8) * 20;
            CodexChapters.categories.get(i).draw(this, matrixStack, guiLeft + (i >= 8 ? 304 : 8), y, i >= 8, mouseX, mouseY);
        }

        RenderSystem.setShaderTexture(0, CODEX_BACKGROUND);
        blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize, 512, 512);
        Page left = currentChapter.get(currentPage), right = currentChapter.get(currentPage + 1);
        if (left != null) left.fullRender(this, matrixStack, guiLeft + 14, guiTop + 24, mouseX, mouseY);
        if (right != null) right.fullRender(this, matrixStack, guiLeft + 170, guiTop + 24, mouseX, mouseY);

        RenderSystem.setShaderTexture(0, CODEX_BACKGROUND);
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
        
        if (hasTooltip) {
        	matrixStack.pushPose();
        	matrixStack.setIdentity();
        	matrixStack.mulPoseMatrix(tooltipMatrix);
        	super.renderTooltip(matrixStack, tooltipText, tooltipX, tooltipY);
        	matrixStack.popPose();
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
            Player player = Minecraft.getInstance().player;
            Level world = Minecraft.getInstance().level;
            Networking.sendToServer(new AttemptCastPacket(player, chant));
            chant.clear();
            player.playNotifySound(SoundEvents.UI_BUTTON_CLICK, SoundSource.NEUTRAL, 1.0f, 1.0f);
            this.onClose();
            return true;
        }
        if (cancelHover) {
            chant.clear();
            Minecraft.getInstance().player.playNotifySound(SoundEvents.UI_BUTTON_CLICK, SoundSource.NEUTRAL, 1.0f, 1.0f);
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
            this.width = mc.getWindow().getGuiScaledWidth();
            this.height = mc.getWindow().getGuiScaledHeight();
            int guiLeft = (width - xSize) / 2, guiTop = (height - ySize) / 2;

            if (currentPage > 0) { // left arrow
                int x = guiLeft + 10, y = guiTop + 169;
                if (mouseX >= x && mouseY >= y && mouseX <= x + 32 && mouseY <= y + 16) {
                    currentPage -= 2;
                    Minecraft.getInstance().player.playNotifySound(SoundEvents.BOOK_PAGE_TURN, SoundSource.NEUTRAL, 1.0f, 1.0f);
                    resetPages();
                    return true;
                }
            }
            if (currentPage + 2 < currentChapter.size()) { // right arrow
                int x = guiLeft + 270, y = guiTop + 169;
                if (mouseX >= x && mouseY >= y && mouseX <= x + 32 && mouseY <= y + 16) {
                    currentPage += 2;
                    Minecraft.getInstance().player.playNotifySound(SoundEvents.BOOK_PAGE_TURN, SoundSource.NEUTRAL, 1.0f, 1.0f);
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
    public void renderTooltip(PoseStack mStack, ItemStack stack, int x, int y) {
        if (!stack.isEmpty()) super.renderTooltip(mStack, stack, x, y);
    }
}
