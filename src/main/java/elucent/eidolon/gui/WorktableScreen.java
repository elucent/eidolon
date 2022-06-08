package elucent.eidolon.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import elucent.eidolon.Eidolon;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.TextComponent;

public class WorktableScreen extends ContainerScreen<WorktableContainer> {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Eidolon.MODID,"textures/gui/worktable.png");

    public WorktableScreen(WorktableContainer screenContainer, Inventory inv, TextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageHeight = 224;
        this.imageWidth = 192;
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);

        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        this.minecraft.getTextureManager().bind(BACKGROUND);
        int i = this.leftPos;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }
}
