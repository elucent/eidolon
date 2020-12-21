package elucent.eidolon.tile;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.block.WoodenStandBlock;
import elucent.eidolon.gui.WoodenBrewingStandContainer;
import net.minecraft.block.BlockState;
import net.minecraft.block.BrewingStandBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.PotionBrewing;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

import javax.annotation.Nullable;
import java.util.Arrays;

public class WoodenStandTileEntity extends LockableTileEntity implements ISidedInventory, ITickableTileEntity {
    private static final int[] SLOTS_FOR_UP = new int[]{3};
    private static final int[] SLOTS_FOR_DOWN = new int[]{0, 1, 2, 3};
    private static final int[] OUTPUT_SLOTS = new int[]{0, 1, 2};
    private NonNullList<ItemStack> brewingItemStacks = NonNullList.withSize(4, ItemStack.EMPTY);
    private int brewTime, heat;
    private boolean[] filledSlots;
    private Item ingredientID;
    public final IIntArray field_213954_a = new IIntArray() {
        public int get(int index) {
            switch(index) {
                case 0:
                    return WoodenStandTileEntity.this.brewTime;
                case 1:
                    return WoodenStandTileEntity.this.heat;
                default:
                    return 0;
            }
        }

        public void set(int index, int value) {
            switch(index) {
                case 0:
                    WoodenStandTileEntity.this.brewTime = value;
                    break;
                case 1:
                    WoodenStandTileEntity.this.heat = value;
            }
        }

        public int size() {
            return 2;
        }
    };

    public WoodenStandTileEntity() {
        super(Registry.WOODEN_STAND_TILE_ENTITY);
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container." + Eidolon.MODID + ".wooden_brewing_stand");
    }

    @Override
    public int getSizeInventory() {
        return this.brewingItemStacks.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : this.brewingItemStacks) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void tick() {
        boolean flag = this.canBrew();
        boolean flag1 = this.brewTime > 0;
        ItemStack itemstack1 = this.brewingItemStacks.get(3);
        if (world.getGameTime() % 20 == 0) {
            TileEntity below = world.getTileEntity(pos.down());
            if (below instanceof CrucibleTileEntity) {
                int prevHeat = heat;
                heat = ((CrucibleTileEntity)below).boiling ? 1 : 0;
                if (prevHeat != heat) markDirty();
            }
        }
        if (flag1) {
            --this.brewTime;
            boolean flag2 = this.brewTime == 0;
            if (flag2 && flag) {
                this.brewPotions();
                this.markDirty();
            } else if (!flag || heat == 0) {
                this.brewTime = 0;
                this.markDirty();
            } else if (this.ingredientID != itemstack1.getItem()) {
                this.brewTime = 0;
                this.markDirty();
            }
        } else if (flag && heat > 0) {
            this.brewTime = 800;
            this.ingredientID = itemstack1.getItem();
            this.markDirty();
        }

        if (!this.world.isRemote) {
            boolean[] aboolean = this.createFilledSlotsArray();
            if (!Arrays.equals(aboolean, this.filledSlots)) {
                this.filledSlots = aboolean;
                BlockState blockstate = this.world.getBlockState(this.getPos());
                if (!(blockstate.getBlock() instanceof WoodenStandBlock)) {
                    return;
                }

                for(int i = 0; i < BrewingStandBlock.HAS_BOTTLE.length; ++i) {
                    blockstate = blockstate.with(BrewingStandBlock.HAS_BOTTLE[i], Boolean.valueOf(aboolean[i]));
                }

                this.world.setBlockState(this.pos, blockstate, 2);
            }
        }
    }

    public boolean[] createFilledSlotsArray() {
        boolean[] aboolean = new boolean[3];

        for(int i = 0; i < 3; ++i) {
            if (!this.brewingItemStacks.get(i).isEmpty()) {
                aboolean[i] = true;
            }
        }

        return aboolean;
    }

    private boolean canBrew() {
        ItemStack itemstack = this.brewingItemStacks.get(3);
        if (!itemstack.isEmpty()) return net.minecraftforge.common.brewing.BrewingRecipeRegistry.canBrew(brewingItemStacks, itemstack, OUTPUT_SLOTS); // divert to VanillaBrewingRegistry
        if (itemstack.isEmpty()) {
            return false;
        } else if (!PotionBrewing.isReagent(itemstack)) {
            return false;
        } else {
            for(int i = 0; i < 3; ++i) {
                ItemStack itemstack1 = this.brewingItemStacks.get(i);
                if (!itemstack1.isEmpty() && PotionBrewing.hasConversions(itemstack1, itemstack)) {
                    return true;
                }
            }

            return false;
        }
    }

    private void brewPotions() {
        if (net.minecraftforge.event.ForgeEventFactory.onPotionAttemptBrew(brewingItemStacks)) return;
        ItemStack itemstack = this.brewingItemStacks.get(3);

        net.minecraftforge.common.brewing.BrewingRecipeRegistry.brewPotions(brewingItemStacks, itemstack, OUTPUT_SLOTS);
        net.minecraftforge.event.ForgeEventFactory.onPotionBrewed(brewingItemStacks);
        BlockPos blockpos = this.getPos();
        if (itemstack.hasContainerItem()) {
            ItemStack itemstack1 = itemstack.getContainerItem();
            itemstack.shrink(1);
            if (itemstack.isEmpty()) {
                itemstack = itemstack1;
            } else if (!this.world.isRemote) {
                InventoryHelper.spawnItemStack(this.world, (double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ(), itemstack1);
            }
        }
        else itemstack.shrink(1);

        this.brewingItemStacks.set(3, itemstack);
        this.world.playEvent(1035, blockpos, 0);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.brewingItemStacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.brewingItemStacks);
        this.brewTime = nbt.getShort("BrewTime");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putShort("BrewTime", (short)this.brewTime);
        ItemStackHelper.saveAllItems(compound, this.brewingItemStacks);
        return compound;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return index >= 0 && index < this.brewingItemStacks.size() ? this.brewingItemStacks.get(index) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.brewingItemStacks, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.brewingItemStacks, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index >= 0 && index < this.brewingItemStacks.size()) {
            this.brewingItemStacks.set(index, stack);
        }

    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return !(player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) > 64.0D);
        }
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 3) {
            return net.minecraftforge.common.brewing.BrewingRecipeRegistry.isValidIngredient(stack)
                && !Tags.Items.DUSTS_REDSTONE.contains(stack.getItem())
                && !Tags.Items.DUSTS_GLOWSTONE.contains(stack.getItem());
        } else {
            Item item = stack.getItem();
            return net.minecraftforge.common.brewing.BrewingRecipeRegistry.isValidInput(stack) && this.getStackInSlot(index).isEmpty();
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.UP) {
            return SLOTS_FOR_UP;
        } else {
            return side == Direction.DOWN ? SLOTS_FOR_DOWN : OUTPUT_SLOTS;
        }
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        if (index == 3) {
            return stack.getItem() == Items.GLASS_BOTTLE;
        } else {
            return true;
        }
    }

    @Override
    public void clear() {
        this.brewingItemStacks.clear();
    }

    @Override
    public Container createMenu(int id, PlayerInventory player) {
        return new WoodenBrewingStandContainer(id, player, this, this.field_213954_a);
    }

    net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers =
        net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
        if (!this.removed && facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == Direction.UP)
                return handlers[0].cast();
            else if (facing == Direction.DOWN)
                return handlers[1].cast();
            else
                return handlers[2].cast();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void remove() {
        super.remove();
        for (int x = 0; x < handlers.length; x++)
            handlers[x].invalidate();
    }
}
