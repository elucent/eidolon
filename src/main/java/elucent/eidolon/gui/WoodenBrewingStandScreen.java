package elucent.eidolon.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import elucent.eidolon.Eidolon;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WoodenBrewingStandScreen extends ContainerScreen<WoodenBrewingStandContainer> {
    private static final ResourceLocation BREWING_STAND_GUI_TEXTURES = new ResourceLocation(Eidolon.MODID, "textures/gui/wooden_brewing_stand.png");
    private static final int[] BUBBLELENGTHS = new int[]{29, 24, 20, 16, 11, 6, 0};

    public WoodenBrewingStandScreen(WoodenBrewingStandContainer p_i51097_1_, Inventory p_i51097_2_, TextComponent p_i51097_3_) {
        super(p_i51097_1_, p_i51097_2_, p_i51097_3_);
    }

    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(BREWING_STAND_GUI_TEXTURES);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        if (menu.getHeat() > 0) {
            this.blit(matrixStack, i + 32, j + 52, 197, 0, 14, 14);
        }

        int i1 = this.menu.getTime();
        if (i1 > 0) {
            int j1 = (int)(28.0F * (1.0F - (float)i1 / 800));
            if (j1 > 0) {
                this.blit(matrixStack, i + 97, j + 16, 176, 0, 9, j1);
            }

            j1 = BUBBLELENGTHS[i1 / 2 % 7];
            if (j1 > 0) {
                this.blit(matrixStack, i + 63, j + 14 + 29 - j1, 185, 29 - j1, 12, j1);
            }
        }
    }
}

