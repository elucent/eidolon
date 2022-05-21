package elucent.eidolon.gui;

import com.mojang.blaze3d.vertex.PoseStack;

import java.util.List;

import javax.swing.plaf.basic.BasicBorders.MenuBarBorder;

import com.mojang.blaze3d.systems.RenderSystem;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.network.Networking;
import elucent.eidolon.network.ResearchActionPacket;
import elucent.eidolon.research.Research;
import elucent.eidolon.research.Researches;
import elucent.eidolon.research.ResearchTask;
import elucent.eidolon.research.ResearchTask.CompletenessResult;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ResearchTableScreen extends AbstractContainerScreen<ResearchTableContainer> {
    private static final ResourceLocation RESEARCH_TABLE_TEXTURE = new ResourceLocation(Eidolon.MODID, "textures/gui/research_table.png");

    public ResearchTableScreen(ResearchTableContainer table, Inventory inv, Component p_i51097_3_) {
        super(table, inv, p_i51097_3_);
        this.imageHeight = 224;
        this.imageWidth = 192;
    }

    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
    	RenderSystem.setShaderTexture(0, RESEARCH_TABLE_TEXTURE);
        int i = this.leftPos;
        int j = (this.height - this.imageHeight) / 2;
        
        double mouseX = Minecraft.getInstance().mouseHandler.xpos();
        double mouseY = Minecraft.getInstance().mouseHandler.ypos();
        mouseX = mouseX * (double)Minecraft.getInstance().getWindow().getGuiScaledWidth() / (double)Minecraft.getInstance().getWindow().getScreenWidth();
        mouseY = mouseY * (double)Minecraft.getInstance().getWindow().getGuiScaledHeight() / (double)Minecraft.getInstance().getWindow().getScreenHeight();
        
        if (menu.slots.get(0).getItem().getItem() == Registry.RESEARCH_NOTES.get() && menu.getProgress() == 0) {
        	ItemStack notes = menu.slots.get(0).getItem();
        	if (!notes.hasTag() || !notes.getTag().contains("research")) return;
        	Research r = Researches.find(new ResourceLocation(notes.getTag().getString("research")));
        	if (r == null) return;

        	int nstars = r.getStars();
        	int done = notes.getTag().getInt("stepsDone");
        	
        	if (done < nstars) {
	        	List<ResearchTask> tasks = r.getTasks(menu.getSeed(), notes.getTag().getInt("stepsDone"));
	        	int slotStart = 38;
	        	for (int k = 0; k < tasks.size(); k ++) {
	        		ResearchTask task = tasks.get(k);
	    			int xx = 164, yy = 16 + 36 * k;
	    	        if (isHovering(xx + 10, yy + 6, 20, 20, mouseX, mouseY)) {
	    	        	task.drawTooltip(matrixStack, this, mouseX - i, mouseY - j);
		    	        RenderSystem.setShaderTexture(0, RESEARCH_TABLE_TEXTURE);
	    	        }
	        	}
        	}
        }
    }
    
    @Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
    	if (menu.slots.get(0).getItem().getItem() == Registry.RESEARCH_NOTES.get() && menu.getProgress() == 0) {
        	ItemStack notes = menu.slots.get(0).getItem();
        	if (!notes.hasTag() || !notes.getTag().contains("research")) return false;
        	Research r = Researches.find(new ResourceLocation(notes.getTag().getString("research")));
        	if (r == null) return false;
        	
        	List<ResearchTask> tasks = r.getTasks(menu.getSeed(), notes.getTag().getInt("stepsDone"));
        	int slotStart = 38;
        	for (int k = 0; k < tasks.size(); k ++) {
        		ResearchTask task = tasks.get(k);
    			int xx = 164, yy = 16 + 36 * k;
    	        
    	        CompletenessResult isComplete = task.isComplete(menu, Minecraft.getInstance().player, slotStart);
    	        if (isComplete.complete() && isHovering(xx + task.getWidth() - 30, yy + 9, 20, 13, mouseX, mouseY)) {
            		Networking.sendToServer(new ResearchActionPacket(ResearchActionPacket.Action.SUBMIT_GOAL, k));
            		return true;
    	        }
    			slotStart = isComplete.nextSlot();
        	}
        	
        	int nstars = r.getStars();
        	int done = notes.getTag().getInt("stepsDone");
        	
        	if (done >= nstars && isHovering(75, 51, 17, 14, mouseX, mouseY) && !menu.getSlot(1).getItem().isEmpty()) {
        		Networking.sendToServer(new ResearchActionPacket(ResearchActionPacket.Action.STAMP));
        		return true;
        	}
        }
    	return super.mouseClicked(mouseX, mouseY, button);
    }   
    
    protected void slotClicked(Slot p_97778_, int p_97779_, int p_97780_, ClickType p_97781_) {
    	if (p_97779_ >= menu.slots.size()) return;
		super.slotClicked(p_97778_, p_97779_, p_97780_, p_97781_);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.setShaderTexture(0, RESEARCH_TABLE_TEXTURE);
        int i = this.leftPos;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        
        double mouseX = Minecraft.getInstance().mouseHandler.xpos();
        double mouseY = Minecraft.getInstance().mouseHandler.ypos();
        mouseX = mouseX * (double)Minecraft.getInstance().getWindow().getGuiScaledWidth() / (double)Minecraft.getInstance().getWindow().getScreenWidth();
        mouseY = mouseY * (double)Minecraft.getInstance().getWindow().getGuiScaledHeight() / (double)Minecraft.getInstance().getWindow().getScreenHeight();
        
        if (menu.slots.get(0).getItem().getItem() == Registry.RESEARCH_NOTES.get()) {
        	ItemStack notes = menu.slots.get(0).getItem();
        	if (!notes.hasTag() || !notes.getTag().contains("research")) return;
        	Research r = Researches.find(new ResourceLocation(notes.getTag().getString("research")));
        	if (r == null) return;
        	
        	int progress = menu.getProgress();
        	int amt = progress * 104 / 200;
        	if (progress > 0) blit(matrixStack, i + 137, j + 17 + amt, 192, 92 + amt, 9, 104 - amt);

        	int nstars = r.getStars();
        	int done = notes.getTag().getInt("stepsDone");
        	
        	if (done < nstars && progress == 0) {
	        	List<ResearchTask> tasks = r.getTasks(menu.getSeed(), notes.getTag().getInt("stepsDone"));
	        	int slotStart = 38;
	        	for (int k = 0; k < tasks.size(); k ++) {
	        		ResearchTask task = tasks.get(k);
	    			int xx = 164, yy = 16 + 36 * k;
	    	        RenderSystem.setShaderTexture(0, RESEARCH_TABLE_TEXTURE);
	    			blit(matrixStack, i + xx, j + yy, 80, 224, 8, 32);
	    			blit(matrixStack, i + xx + 8, j + yy, 112, 224, 24, 32);
	    			task.drawIcon(matrixStack, i + xx + 12, j + yy + 8);
	    	        RenderSystem.setShaderTexture(0, RESEARCH_TABLE_TEXTURE);
	    			task.drawCustom(matrixStack, i + xx + 32, j + yy);
	    	        RenderSystem.setShaderTexture(0, RESEARCH_TABLE_TEXTURE);
	    	        
	    	        CompletenessResult isComplete = task.isComplete(menu, Minecraft.getInstance().player, slotStart);
	    	        if (isComplete.complete()) {
	    	        	if (isHovering(xx + task.getWidth() - 30, yy + 9, 20, 13, mouseX, mouseY)) {
	        	        	blit(matrixStack, i + xx + task.getWidth() - 32, j + yy, 184, 224, 24, 32);
	    	        	}
	    	        	else blit(matrixStack, i + xx + task.getWidth() - 32, j + yy, 160, 224, 24, 32);
	    	        }
	    	        else {
	    	        	blit(matrixStack, i + xx + task.getWidth() - 32, j + yy, 136, 224, 24, 32);
	    	        }
	    			blit(matrixStack, i + xx + task.getWidth() - 8, j + yy, 96, 224, 8, 32);
	    			
	    			slotStart = isComplete.nextSlot();
	        	}
        	}
        	
        	int starsY = 61 + 5 * nstars;
        	for (int k = 0; k < nstars; k ++) {
        		if (k < done) blit(matrixStack, i + 152, j + starsY - k * 10, 201, 82, 9, 10);
        		else blit(matrixStack, i + 152, j + starsY - k * 10, 192, 82, 9, 10);
        	}
        	
        	if (done >= nstars && !menu.getSlot(1).getItem().isEmpty()) {
	        	if (isHovering(75, 51, 17, 14, mouseX, mouseY)) {
	        		blit(matrixStack, i + 73, j + 49, 234, 64, 21, 18);
	        	}
	        	else blit(matrixStack, i + 73, j + 49, 213, 64, 21, 18);
        	}
        }
    }
}

