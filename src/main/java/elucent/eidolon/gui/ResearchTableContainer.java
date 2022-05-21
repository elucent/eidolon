package elucent.eidolon.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import elucent.eidolon.Registry;
import elucent.eidolon.mixin.AbstractContainerMenuMixin;
import elucent.eidolon.research.Research;
import elucent.eidolon.research.Researches;
import elucent.eidolon.research.ResearchTask;
import elucent.eidolon.tile.ResearchTableTileEntity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.DistExecutor;

public class ResearchTableContainer extends AbstractContainerMenu implements ContainerListener {
    private final Container tile;
    private final ContainerData intArray;
    protected final List<ResearchTask> tasks;

    public ResearchTableContainer(int id, Inventory playerInventory) {
        this(id, playerInventory, new SimpleContainer(2), new SimpleContainerData(2));
    }

    public ResearchTableContainer(int id, Inventory playerInventory, Container inventory, ContainerData data) {
        super(Registry.RESEARCH_TABLE_CONTAINER.get(), id);
        this.tile = inventory;
        this.intArray = data;
        this.addSlot(new NotesSlot(inventory, 0, 58, 68));
        this.addSlot(new SealSlot(inventory, 1, 58, 32));
        this.addDataSlots(data);
        this.tasks = new ArrayList<>();
        
        for(int k = 0; k < 3; ++k) {
            for(int i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(playerInventory, i1 + k * 9 + 9, 16 + i1 * 18, 142 + k * 18));
            }
        }

        for(int l = 0; l < 9; ++l) {
            this.addSlot(new Slot(playerInventory, l, 16 + l * 18, 200));
        }
        
        if (inventory instanceof ResearchTableTileEntity t) {
        	t.addListener(this);
        }
        
        if (tile instanceof ResearchTableTileEntity) updateSlots();
    }

    protected void popSlot() {
    	slots.remove(slots.size() - 1);
    	List<ItemStack> lastSlots = ((AbstractContainerMenuMixin)(Object)this).getLastSlots();
    	List<ItemStack> remoteSlots = ((AbstractContainerMenuMixin)(Object)this).getRemoteSlots();
    	lastSlots.remove(lastSlots.size() - 1);
    	remoteSlots.remove(remoteSlots.size() - 1);
    }
    
    @Override
    public void clicked(int p_150400_, int p_150401_, ClickType p_150402_, Player p_150403_) {
    	if (p_150400_ >= slots.size()) return;
    	super.clicked(p_150400_, p_150401_, p_150402_, p_150403_);
    }
    
    public void updateSlots() {
    	if (tile instanceof ResearchTableTileEntity t) {
        	for (int i = 38; i < slots.size(); i ++) if (!slots.get(i).getItem().isEmpty()) {
    	        double d0 = t.getBlockPos().getY() + 1.3F;
    	        ItemEntity itementity = new ItemEntity(t.getLevel(), t.getBlockPos().getX() + 0.5, d0, t.getBlockPos().getZ() + 0.5, slots.get(i).getItem());
    	        itementity.setPickUpDelay(40);
    	        t.getLevel().addFreshEntity(itementity);
        	}
    	}
    	while (slots.size() > 38) popSlot(); // Pare down to just the base 2 slots + player inventory.
    	while (slots.get(0).getItem().is(Registry.RESEARCH_NOTES.get())) {
        	if (getProgress() > 0) break; // Slots don't appear when research is in progress.
    		ItemStack stack = slots.get(0).getItem();
    		if (!stack.hasTag() || !stack.getTag().contains("research")) break;
    		Research r = Researches.find(new ResourceLocation(stack.getTag().getString("research")));
    		if (r == null) break;
    		if (stack.getTag().getInt("stepsDone") >= r.getStars()) break;
    		List<ResearchTask> tasks = r.getTasks(getSeed(), stack.getTag().getInt("stepsDone"));
    		for (int i = 0; i < tasks.size(); i ++) {
    			int x = 189, y = 17 + 36 * i;
    			tasks.get(i).modifyContainer(this, x, y);
    		}
    		break;
    	}
    	if (tile instanceof ResearchTableTileEntity) this.broadcastFullState();
    }
    
    @Override
    public void removed(Player player) {
    	super.removed(player);
        if (player instanceof ServerPlayer) {
        	for (int i = 38; i < slots.size(); i ++) if (!slots.get(i).getItem().isEmpty()) {
        		player.drop(slots.get(i).getItem(), false);
        	}
        }
        if (this.tile instanceof ResearchTableTileEntity t) {
        	t.removeListener(this);
        }
    }

    public boolean stillValid(Player playerIn) {
        return this.tile.stillValid(playerIn);
    }

    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem().copy();
            if ((index < 0 || (index > 2 && index < 38))) {
                if (this.slots.get(0).mayPlace(stack)) {
                    if (!this.moveItemStackTo(stack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.slots.get(1).mayPlace(stack)) {
                    if (!this.moveItemStackTo(stack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 2 && index < 29) {
                    if (!this.moveItemStackTo(stack, 29, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 29 && index < 38) {
                    if (!this.moveItemStackTo(stack, 2, 29, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(stack, 2, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(stack, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(stack, itemstack);
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, stack);
        }

        return itemstack;
    }

    public int getProgress() {
        return this.intArray.get(0);
    }

    public int getSeed() {
        return this.intArray.get(1);
    }
    
    @Override
    public void initializeContents(int id, List<ItemStack> items, ItemStack carried) {
        this.slots.get(0).set(items.get(0).copy());
        updateSlots();

        try {
        	super.initializeContents(id, items, carried);
        } catch (IndexOutOfBoundsException e) {}
    }

    class NotesSlot extends Slot {
        public NotesSlot(Container iInventoryIn, int index, int xPosition, int yPosition) {
            super(iInventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.is(Registry.RESEARCH_NOTES.get());
        }
        
        @Override
        public void setChanged() {
        	super.setChanged();
        	if (tile instanceof ResearchTableTileEntity) updateSlots();
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }

    class SealSlot extends Slot {
        public SealSlot(Container iInventoryIn, int index, int xPosition, int yPosition) {
            super(iInventoryIn, index, xPosition, yPosition);
        }

        public boolean mayPlace(ItemStack stack) {
            return stack.is(Registry.ARCANE_SEAL.get());
        }

        public int getMaxStackSize() {
            return 64;
        }
    }

	public void trySubmitGoal(Player player, int index) {
    	if (slots.get(0).getItem().is(Registry.RESEARCH_NOTES.get())) {
    		ItemStack stack = slots.get(0).getItem();
    		if (!stack.hasTag() || !stack.getTag().contains("research")) return;
    		Research r = Researches.find(new ResourceLocation(stack.getTag().getString("research")));
    		if (r == null) return;
    		List<ResearchTask> tasks = r.getTasks(getSeed(), stack.getTag().getInt("stepsDone"));
    		if (tasks.size() < index) return;
    		ResearchTask toComplete = tasks.get(index);
    		int startingSlot = 38;
    		for (int i = 0; i < index; i ++) startingSlot += tasks.get(i).getSlotCount();
    		if (!toComplete.isComplete(this, player, startingSlot).complete()) return;
    		toComplete.onComplete(this, player, startingSlot);
    		this.setData(0, 200); // start progress countdown.
    		this.broadcastChanges();
    		this.updateSlots();
    	}
	}

	public void tryStamp(Player player) {
    	if (slots.get(0).getItem().is(Registry.RESEARCH_NOTES.get()) && slots.get(1).getItem().is(Registry.ARCANE_SEAL.get())) {
    		ItemStack notes = slots.get(0).getItem();
    		if (!notes.hasTag() || !notes.getTag().contains("research")) return;
    		Research r = Researches.find(new ResourceLocation(notes.getTag().getString("research")));
    		if (r == null) return;
    		if (notes.getTag().getInt("stepsDone") < r.getStars()) return;
    		
    		slots.get(1).remove(1);
    		ItemStack completed = new ItemStack(Registry.COMPLETED_RESEARCH.get());
    		completed.getOrCreateTag().putString("research", r.getRegistryName().toString());
    		slots.get(0).set(completed);
    		this.updateSlots();
    	}
	}

	@Override
	public void slotChanged(AbstractContainerMenu menu, int slot, ItemStack stack) {
		if (slot == 0) updateSlots();
	}

	@Override
	public void dataChanged(AbstractContainerMenu menu, int slot, int value) {
		if (slot == 0 && (value == 0 || value == 200)) updateSlots();
	}
}
