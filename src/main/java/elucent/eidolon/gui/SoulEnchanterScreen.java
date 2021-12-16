package elucent.eidolon.gui;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;

import elucent.eidolon.Eidolon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.gui.screens.inventory.EnchantmentNames;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;

public class SoulEnchanterScreen extends AbstractContainerScreen<SoulEnchanterContainer> {
    private static final ResourceLocation ENCHANTMENT_TABLE_GUI_TEXTURE = new ResourceLocation(Eidolon.MODID,"textures/gui/soul_enchanter.png");
    private static final ResourceLocation ENCHANTMENT_TABLE_BOOK_TEXTURE = new ResourceLocation(Eidolon.MODID,"textures/entity/enchanter_book.png");
    private static BookModel MODEL_BOOK = null;
    private final Random random = new Random();
    public int ticks;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;
    public float open;
    public float oOpen;
    private ItemStack last = ItemStack.EMPTY;

    public SoulEnchanterScreen(SoulEnchanterContainer container, Inventory playerInventory, Component textComponent) {
        super(container, playerInventory, textComponent);
        if (MODEL_BOOK == null) MODEL_BOOK = new BookModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.BOOK));
    }

    public void containerTick() {
        super.containerTick();
        this.tickBook();
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;

        for(int k = 0; k < 3; ++k) {
            double d0 = mouseX - (double)(i + 60);
            double d1 = mouseY - (double)(j + 14 + 19 * k);
            if (d0 >= 0.0D && d1 >= 0.0D && d0 < 108.0D && d1 < 19.0D && this.menu.clickMenuButton(this.minecraft.player, k)) {
                this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, k);
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {Lighting.setupForFlatItems();
	    RenderSystem.setShader(GameRenderer::getPositionTexShader);
	    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
	    RenderSystem.setShaderTexture(0, ENCHANTMENT_TABLE_GUI_TEXTURE);
	    int i = (this.width - this.imageWidth) / 2;
	    int j = (this.height - this.imageHeight) / 2;
	    this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
	    int k = (int)this.minecraft.getWindow().getGuiScale();
	    RenderSystem.viewport((this.width - 320) / 2 * k, (this.height - 240) / 2 * k, 320 * k, 240 * k);
	    Matrix4f matrix4f = Matrix4f.createTranslateMatrix(-0.34F, 0.23F, 0.0F);
	    matrix4f.multiply(Matrix4f.perspective(90.0D, 1.3333334F, 9.0F, 80.0F));
	    RenderSystem.backupProjectionMatrix();
	    RenderSystem.setProjectionMatrix(matrix4f);
	    matrixStack.pushPose();
	    PoseStack.Pose posestack$pose = matrixStack.last();
	    posestack$pose.pose().setIdentity();
	    posestack$pose.normal().setIdentity();
	    matrixStack.translate(0.0D, (double)3.3F, 1984.0D);
	    float f = 5.0F;
	    matrixStack.scale(5.0F, 5.0F, 5.0F);
	    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
	    matrixStack.mulPose(Vector3f.XP.rotationDegrees(20.0F));
	    float f1 = Mth.lerp(partialTicks, this.oOpen, this.open);
	    matrixStack.translate((double)((1.0F - f1) * 0.2F), (double)((1.0F - f1) * 0.1F), (double)((1.0F - f1) * 0.25F));
	    float f2 = -(1.0F - f1) * 90.0F - 90.0F;
	    matrixStack.mulPose(Vector3f.YP.rotationDegrees(f2));
	    matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
	    float f3 = Mth.lerp(partialTicks, this.oFlip, this.flip) + 0.25F;
	    float f4 = Mth.lerp(partialTicks, this.oFlip, this.flip) + 0.75F;
	    f3 = (f3 - (float)Mth.fastFloor((double)f3)) * 1.6F - 0.3F;
	    f4 = (f4 - (float)Mth.fastFloor((double)f4)) * 1.6F - 0.3F;
	    if (f3 < 0.0F) {
	       f3 = 0.0F;
	    }
	
	    if (f4 < 0.0F) {
	       f4 = 0.0F;
	    }
	
	    if (f3 > 1.0F) {
	       f3 = 1.0F;
	    }
	
	    if (f4 > 1.0F) {
	       f4 = 1.0F;
	    }
	
	    MODEL_BOOK.setupAnim(0.0F, f3, f4, f1);
	    MultiBufferSource.BufferSource multibuffersource$buffersource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
	    VertexConsumer vertexconsumer = multibuffersource$buffersource.getBuffer(MODEL_BOOK.renderType(ENCHANTMENT_TABLE_BOOK_TEXTURE));
	    MODEL_BOOK.renderToBuffer(matrixStack, vertexconsumer, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	    multibuffersource$buffersource.endBatch();
	    matrixStack.popPose();
	    RenderSystem.viewport(0, 0, this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight());
	    RenderSystem.restoreProjectionMatrix();
	    Lighting.setupFor3DItems();
	    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        EnchantmentNames.getInstance().initSeed((long)this.menu.getXPSeed());
        int l = this.menu.getSoulShardAmount();

        for(int i1 = 0; i1 < 3; ++i1) {
            int j1 = i + 60;
            int k1 = j1 + 20;
            this.setBlitOffset(0);
            RenderSystem.setShaderTexture(0, ENCHANTMENT_TABLE_GUI_TEXTURE);
            int l1 = menu.worldClue[i1];
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            int i2 = 86;
            FormattedText itextproperties = EnchantmentNames.getInstance().getRandomName(this.font, i2);
            int j2 = 6839882;
            if (l1 < 1) {
                this.blit(matrixStack, j1, j + 14 + 19 * i1, 0, 185, 108, 19);
            } else {
                if (((l == 0 || this.minecraft.player.experienceLevel < l1) && !this.minecraft.player.getAbilities().instabuild) || this.menu.enchantClue[i1] == -1) { // Forge: render buttons as disabled when enchantable but enchantability not met on lower levels
                    this.blit(matrixStack, j1, j + 14 + 19 * i1, 0, 185, 108, 19);
                    this.blit(matrixStack, j1 + 1, j + 15 + 19 * i1, 16 * (menu.worldClue[i1] - 1), 239, 16, 16);
                    this.font.drawWordWrap(itextproperties, k1, j + 16 + 19 * i1, i2, (j2 & 16711422) >> 1);
                    j2 = 4226832;
                } else {
                    int k2 = x - (i + 60);
                    int l2 = y - (j + 14 + 19 * i1);
                    if (k2 >= 0 && l2 >= 0 && k2 < 108 && l2 < 19) {
                        this.blit(matrixStack, j1, j + 14 + 19 * i1, 0, 204, 108, 19);
                        j2 = 16777088;
                    } else {
                        this.blit(matrixStack, j1, j + 14 + 19 * i1, 0, 166, 108, 19);
                    }

                    this.blit(matrixStack, j1 + 1, j + 15 + 19 * i1, 16 * (menu.worldClue[i1] - 1), 223, 16, 16);
                    this.font.drawWordWrap(itextproperties, k1, j + 16 + 19 * i1, i2, j2);
                    j2 = 8453920;
                }
            }
        }
    }

    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        partialTicks = this.minecraft.getFrameTime();
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
        boolean flag = this.minecraft.player.getAbilities().instabuild;
        int i = this.menu.getSoulShardAmount();

        for(int j = 0; j < 3; ++j) {
            Enchantment enchantment = Enchantment.byId((this.menu).enchantClue[j]);
            int l = (this.menu).worldClue[j];
            int i1 = j + 1;
            if (this.isHovering(60, 14 + 19 * j, 108, 17, (double)mouseX, (double)mouseY) && l > 0) {
                List<Component> list = Lists.newArrayList();
                list.add(enchantment == null ? new TextComponent("") : enchantment.getFullname(l));
                if(enchantment == null) {
                    list.add(new TextComponent(""));
                    list.add(new TranslatableComponent("forge.container.enchant.limitedEnchantability").withStyle(ChatFormatting.RED));
                } else if (!flag) {
                    list.add(TextComponent.EMPTY);
                    if (this.minecraft.player.experienceLevel < l) {
                        list.add((new TranslatableComponent("container.enchant.level.requirement", l)).withStyle(ChatFormatting.RED));
                    } else {
                        MutableComponent iformattabletextcomponent = new TranslatableComponent("container.eidolon.enchant.shard.one", 1);

                        list.add(iformattabletextcomponent.withStyle(minecraft.player.experienceLevel >= l ? ChatFormatting.GRAY : ChatFormatting.RED));
                        MutableComponent iformattabletextcomponent1;
                        if (l == 1) {
                            iformattabletextcomponent1 = new TranslatableComponent("container.enchant.level.one");
                        } else {
                            iformattabletextcomponent1 = new TranslatableComponent("container.enchant.level.many", menu.worldClue[i1 - 1]);
                        }

                        list.add(iformattabletextcomponent1.withStyle(ChatFormatting.GRAY));
                    }
                }

                this.renderComponentTooltip(matrixStack, list, mouseX, mouseY);
                break;
            }
        }

    }

    public void tickBook() {
        ItemStack itemstack = this.menu.getSlot(0).getItem();
        if (!ItemStack.matches(itemstack, this.last)) {
            this.last = itemstack;

            do {
                this.flipT += (float)(this.random.nextInt(4) - this.random.nextInt(4));
            } while(this.flip <= this.flipT + 1.0F && this.flip >= this.flipT - 1.0F);
        }

        ++this.ticks;
        this.oFlip = this.flip;
        this.oOpen = this.open;
        boolean flag = false;

        for(int i = 0; i < 3; ++i) {
            if ((this.menu).worldClue[i] != 0) {
                flag = true;
            }
        }

        if (flag) {
            this.open += 0.2F;
        } else {
            this.open -= 0.2F;
        }

        this.open = Mth.clamp(this.open, 0.0F, 1.0F);
        float f1 = (this.flipT - this.flip) * 0.4F;
        float f = 0.2F;
        f1 = Mth.clamp(f1, -0.2F, 0.2F);
        this.flipA += (f1 - this.flipA) * 0.9F;
        this.flip += this.flipA;
    }
}
