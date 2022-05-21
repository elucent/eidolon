package elucent.eidolon.tile;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import javax.annotation.Nullable;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.block.WoodenStandBlock;
import elucent.eidolon.gui.ResearchTableContainer;
import elucent.eidolon.gui.WoodenBrewingStandContainer;
import elucent.eidolon.research.Research;
import elucent.eidolon.research.Researches;
import elucent.eidolon.research.ResearchTask;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BrewingStandBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.LazyOptional;

public class ResearchTableTileEntity extends TileEntityBase implements WorldlyContainer, MenuProvider {
    private static final int[] SLOTS = new int[]{0, 1};
    private NonNullList<ItemStack> stacks = NonNullList.withSize(2, ItemStack.EMPTY);
    private NonNullList<ResearchTask> tasks = NonNullList.create();
    private Component name;
    private int progress;
    private int worldSeed;
    private List<ContainerListener> listeners = new ArrayList<>();
    
    static final int SEED = 1418644859;

    public final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            switch(index) {
                case 0:
                    return ResearchTableTileEntity.this.progress;
                case 1:
                    return ResearchTableTileEntity.this.worldSeed;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch(index) {
                case 0:
                    ResearchTableTileEntity.this.progress = value;
                    break;
                case 1:
                    break; // can't set seed
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public ResearchTableTileEntity(BlockPos pos, BlockState state) {
        super(Registry.RESEARCH_TABLE_TILE_ENTITY, pos, state);
        progress = 0;
    }
    
    @Override
    public void onLoad() {
    	super.onLoad();
        if (!level.isClientSide) worldSeed = SEED + 978060631 * (int)((ServerLevel)level).getSeed();
    }

    protected Component getDefaultName() {
        return new TranslatableComponent("container." + Eidolon.MODID + ".research_table");
    }
    
    public void addListener(ContainerListener listener) {
    	this.listeners.add(listener);
    }
    
    public void removeListener(ContainerListener listener) {
    	this.listeners.remove(listener);
    }

    @Override
    public int getContainerSize() {
        return this.stacks.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : this.stacks) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onDestroyed(BlockState state, BlockPos pos) {
    	for (ItemStack stack : stacks) {
    		if (!stack.isEmpty()) Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
    	}
    }

    public void tick() {
    	if (progress < 0) {
    		progress = 0;
    		sync();
    	}
        if (progress > 0) {
        	if (stacks.get(0).isEmpty() || stacks.get(0).getItem() != Registry.RESEARCH_NOTES.get()) {
        		progress = 0;
        		sync();
        		for (ContainerListener listener : listeners) 
        			listener.dataChanged((AbstractContainerMenu)listener, 0, progress);
        		return;
        	}
        	progress --;
        	if (progress == 0) {
        		ItemStack notes = stacks.get(0);
        		CompoundTag notesTag = notes.getTag();
        		Research r = Researches.find(new ResourceLocation(notesTag.getString("research")));
        		int done = notesTag.getInt("stepsDone");
        		done ++;
        		notesTag.putInt("stepsDone", done);
        		for (ContainerListener listener : listeners) 
        			listener.slotChanged((AbstractContainerMenu)listener, 0, stacks.get(0));
        	}
        	sync();
    		for (ContainerListener listener : listeners) 
    			listener.dataChanged((AbstractContainerMenu)listener, 0, progress);
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.stacks);
        this.progress = nbt.getInt("progress");
        this.worldSeed = nbt.getInt("worldSeed");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("progress", this.progress);
        compound.putInt("worldSeed", worldSeed);
        ContainerHelper.saveAllItems(compound, this.stacks);
    }

    @Override
    public ItemStack getItem(int index) {
        return index >= 0 && index < this.stacks.size() ? this.stacks.get(index) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return ContainerHelper.removeItem(this.stacks, index, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(this.stacks, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (index >= 0 && index < this.stacks.size()) {
            this.stacks.set(index, stack);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return !(player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) > 64.0D);
        }
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        if (index == 0) {
            return stack.is(Registry.RESEARCH_NOTES.get());
        } else if (index == 1) {
            return stack.is(Registry.ARCANE_SEAL.get());
        }
        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return this.canPlaceItem(index, itemStackIn);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        if (index == 0) {
            return stack.is(Registry.COMPLETED_RESEARCH.get());
        } else {
            return false;
        }
    }

    @Override
    public void clearContent() {
        this.stacks.clear();
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new ResearchTableContainer(id, inv, this, this.dataAccess);
    }

    net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler> handler =
        LazyOptional.of(() -> new net.minecraftforge.items.wrapper.InvWrapper(this));

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
        if (!this.remove && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        handler.invalidate();
    }

	@Override
	public Component getDisplayName() {
		return new TextComponent(""); // Not rendered in the GUI.
	}
}
