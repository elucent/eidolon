package elucent.eidolon.gui;

import elucent.eidolon.Registry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.BrewingStandContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.ForgeEventFactory;

public class WoodenBrewingStandContainer extends Container {
    private final IInventory tileBrewingStand;
    private final IIntArray intArray;
    private final Slot slot;

    public WoodenBrewingStandContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new Inventory(4), new IntArray(2));
    }

    public WoodenBrewingStandContainer(int id, PlayerInventory playerInventory, IInventory inventory, IIntArray p_i50096_4_) {
        super(Registry.WOODEN_STAND_CONTAINER.get(), id);
        assertInventorySize(inventory, 4);
        assertIntArraySize(p_i50096_4_, 2);
        this.tileBrewingStand = inventory;
        this.intArray = p_i50096_4_;
        this.addSlot(new PotionSlot(inventory, 0, 56, 51));
        this.addSlot(new PotionSlot(inventory, 1, 79, 58));
        this.addSlot(new PotionSlot(inventory, 2, 102, 51));
        this.slot = this.addSlot(new IngredientSlot(inventory, 3, 79, 17));
        this.trackIntArray(p_i50096_4_);

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.tileBrewingStand.isUsableByPlayer(playerIn);
    }

    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if ((index < 0 || index > 2) && index != 3) {
                if (this.slot.isItemValid(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (PotionSlot.canHoldPotion(itemstack) && itemstack.getCount() == 1) {
                    if (!this.mergeItemStack(itemstack1, 0, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 4 && index < 31) {
                    if (!this.mergeItemStack(itemstack1, 31, 40, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 31 && index < 40) {
                    if (!this.mergeItemStack(itemstack1, 4, 31, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.mergeItemStack(itemstack1, 4, 40, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.mergeItemStack(itemstack1, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    @OnlyIn(Dist.CLIENT)
    public int getHeat() {
        return this.intArray.get(1);
    }

    @OnlyIn(Dist.CLIENT)
    public int getTime() {
        return this.intArray.get(0);
    }

    static class IngredientSlot extends Slot {
        public IngredientSlot(IInventory iInventoryIn, int index, int xPosition, int yPosition) {
            super(iInventoryIn, index, xPosition, yPosition);
        }

        public boolean isItemValid(ItemStack stack) {
            return BrewingRecipeRegistry.isValidIngredient(stack)
                && !Tags.Items.DUSTS_REDSTONE.contains(stack.getItem())
                && !Tags.Items.DUSTS_GLOWSTONE.contains(stack.getItem());
        }

        public int getSlotStackLimit() {
            return 64;
        }
    }

    static class PotionSlot extends Slot {
        public PotionSlot(IInventory p_i47598_1_, int p_i47598_2_, int p_i47598_3_, int p_i47598_4_) {
            super(p_i47598_1_, p_i47598_2_, p_i47598_3_, p_i47598_4_);
        }

        public boolean isItemValid(ItemStack stack) {
            return canHoldPotion(stack);
        }

        public int getSlotStackLimit() {
            return 1;
        }

        public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
            Potion potion = PotionUtils.getPotionFromItem(stack);
            if (thePlayer instanceof ServerPlayerEntity) {
                ForgeEventFactory.onPlayerBrewedPotion(thePlayer, stack);
                CriteriaTriggers.BREWED_POTION.trigger((ServerPlayerEntity)thePlayer, potion);
            }

            super.onTake(thePlayer, stack);
            return stack;
        }

        public static boolean canHoldPotion(ItemStack stack) {
            return BrewingRecipeRegistry.isValidInput(stack);
        }
    }
}
