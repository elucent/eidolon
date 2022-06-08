package elucent.eidolon.tile;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.block.WoodenStandBlock;
import elucent.eidolon.gui.WoodenBrewingStandContainer;
import mezz.jei.common.plugins.vanilla.ingredients.item.ItemStackHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.level.block.BrewingStandBlock;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;
import java.util.Arrays;

public class WoodenStandTileEntity extends BaseContainerBlockEntity implements WorldlyContainer {
    private static final int[] SLOTS_FOR_UP = new int[]{3};
    private static final int[] SLOTS_FOR_DOWN = new int[]{0, 1, 2, 3};
    private static final int[] OUTPUT_SLOTS = new int[]{0, 1, 2};
    private NonNullList<ItemStack> brewingItemStacks = NonNullList.withSize(4, ItemStack.EMPTY);
    private int brewTime, heat;
    private boolean[] filledSlots;
    private Item ingredientID;
    public final ContainerData dataAccess = new ContainerData() {
        @Override
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

        @Override
        public void set(int index, int value) {
            switch(index) {
                case 0:
                    WoodenStandTileEntity.this.brewTime = value;
                    break;
                case 1:
                    WoodenStandTileEntity.this.heat = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public WoodenStandTileEntity() {
        super(Registry.WOODEN_STAND_TILE_ENTITY);
    }

    @Override
    protected TextComponent getDefaultName() {
        return new TranslatableComponent("container." + Eidolon.MODID + ".wooden_brewing_stand");
    }

    @Override
    public int getContainerSize() {
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
        if (level.getGameTime() % 20 == 0) {
            BlockEntity below = level.getBlockEntity(worldPosition.below());
            if (below instanceof CrucibleTileEntity) {
                int prevHeat = heat;
                heat = ((CrucibleTileEntity)below).boiling ? 1 : 0;
                if (prevHeat != heat) setChanged();
            }
        }
        if (flag1) {
            --this.brewTime;
            boolean flag2 = this.brewTime == 0;
            if (flag2 && flag) {
                this.brewPotions();
                this.setChanged();
            } else if (!flag || heat == 0) {
                this.brewTime = 0;
                this.setChanged();
            } else if (this.ingredientID != itemstack1.getItem()) {
                this.brewTime = 0;
                this.setChanged();
            }
        } else if (flag && heat > 0) {
            this.brewTime = 800;
            this.ingredientID = itemstack1.getItem();
            this.setChanged();
        }

        if (!this.level.isClientSide) {
            boolean[] aboolean = this.createFilledSlotsArray();
            if (!Arrays.equals(aboolean, this.filledSlots)) {
                this.filledSlots = aboolean;
                BlockState blockstate = this.level.getBlockState(this.getBlockPos());
                if (!(blockstate.getBlock() instanceof WoodenStandBlock)) {
                    return;
                }

                for(int i = 0; i < BrewingStandBlock.HAS_BOTTLE.length; ++i) {
                    blockstate = blockstate.setValue(BrewingStandBlock.HAS_BOTTLE[i], Boolean.valueOf(aboolean[i]));
                }

                this.level.setBlock(this.worldPosition, blockstate, 2);
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
        } else if (!PotionBrewing.isIngredient(itemstack)) {
            return false;
        } else {
            for(int i = 0; i < 3; ++i) {
                ItemStack itemstack1 = this.brewingItemStacks.get(i);
                if (!itemstack1.isEmpty() && PotionBrewing.hasMix(itemstack1, itemstack)) {
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
        BlockPos blockpos = this.getBlockPos();
        if (itemstack.hasContainerItem()) {
            ItemStack itemstack1 = itemstack.getContainerItem();
            itemstack.shrink(1);
            if (itemstack.isEmpty()) {
                itemstack = itemstack1;
            } else if (!this.level.isClientSide) {
                InventoryHelper.dropItemStack(this.level, (double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ(), itemstack1);
            }
        }
        else itemstack.shrink(1);

        this.brewingItemStacks.set(3, itemstack);
        this.level.levelEvent(1035, blockpos, 0);
    }

    @Override
    public void load(BlockState state, CompoundTag nbt) {
        super.load(state, nbt);
        this.brewingItemStacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.brewingItemStacks);
        this.brewTime = nbt.getShort("BrewTime");
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        compound.putShort("BrewTime", (short)this.brewTime);
        ItemStackHelper.saveAllItems(compound, this.brewingItemStacks);
        return compound;
    }

    @Override
    public ItemStack getItem(int index) {
        return index >= 0 && index < this.brewingItemStacks.size() ? this.brewingItemStacks.get(index) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return ItemStackHelper.removeItem(this.brewingItemStacks, index, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ItemStackHelper.takeItem(this.brewingItemStacks, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (index >= 0 && index < this.brewingItemStacks.size()) {
            this.brewingItemStacks.set(index, stack);
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
        if (index == 3) {
            return net.minecraftforge.common.brewing.BrewingRecipeRegistry.isValidIngredient(stack)
                && !Tags.Items.DUSTS_REDSTONE.contains(stack.getItem())
                && !Tags.Items.DUSTS_GLOWSTONE.contains(stack.getItem());
        } else {
            return net.minecraftforge.common.brewing.BrewingRecipeRegistry.isValidInput(stack) && this.getItem(index).isEmpty();
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
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return this.canPlaceItem(index, itemStackIn);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        if (index == 3) {
            return stack.getItem() == Items.GLASS_BOTTLE;
        } else {
            return true;
        }
    }

    @Override
    public void clearContent() {
        this.brewingItemStacks.clear();
    }

    @Override
    public Container createMenu(int id, Inventory player) {
        return new WoodenBrewingStandContainer(id, player, this, this.dataAccess);
    }

    net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers =
        net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
        if (!this.remove && facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
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
    public void setRemoved() {
        super.setRemoved();
        for (int x = 0; x < handlers.length; x++)
            handlers[x].invalidate();
    }
}
