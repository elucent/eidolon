package elucent.eidolon.gui;

import java.util.Optional;

import elucent.eidolon.Registry;
import elucent.eidolon.recipe.WorktableRecipe;
import elucent.eidolon.recipe.WorktableRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class WorktableContainer extends Container {
    CraftingInventory core = new CraftingInventory(this, 3, 3), extras = new CraftingInventory(this, 2, 2);
    CraftResultInventory result = new CraftResultInventory();
    PlayerEntity player;
    IWorldPosCallable callable;

    public WorktableContainer(int id, PlayerInventory inventory) {
        this(id, inventory, IWorldPosCallable.DUMMY);
    }

    public WorktableContainer(int id, PlayerInventory inventory, IWorldPosCallable callable) {
        super(Registry.WORKTABLE_CONTAINER.get(), id);
        this.player = inventory.player;
        this.callable = callable;
        this.addSlot(new WorktableResultSlot(inventory.player, core, extras, result, 0, 163, 58));

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j) {
                this.addSlot(new Slot(this.core, j + i * 3, 40 + j * 18, 40 + i * 18));
            }
        }
        this.addSlot(new Slot(this.extras, 0, 58, 18));
        this.addSlot(new Slot(this.extras, 1, 98, 58));
        this.addSlot(new Slot(this.extras, 2, 58, 98));
        this.addSlot(new Slot(this.extras, 3, 18, 58));

        for(int k = 0; k < 3; ++k) {
            for(int i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(inventory, i1 + k * 9 + 9, 16 + i1 * 18, 142 + k * 18));
            }
        }

        for(int l = 0; l < 9; ++l) {
            this.addSlot(new Slot(inventory, l, 16 + l * 18, 200));
        }
    }

    protected void updateCraftingResult(int id, World world, PlayerEntity player, CraftingInventory inventory, CraftResultInventory inventoryResult) {
        if (!world.isRemote) {
            ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)player;
            ItemStack itemstack = ItemStack.EMPTY;
            WorktableRecipe recipe = WorktableRegistry.find(core, extras);
            if (recipe != null) {
                itemstack = recipe.getResult();
            }
            else {
                Optional<ICraftingRecipe> optional = world.getServer().getRecipeManager().getRecipe(IRecipeType.CRAFTING, inventory, world);
                if (optional.isPresent()) {
                    ICraftingRecipe icraftingrecipe = optional.get();
                    if (inventoryResult.canUseRecipe(world, serverplayerentity, icraftingrecipe)) {
                        itemstack = icraftingrecipe.getCraftingResult(inventory);
                    }
                }
            }

            inventoryResult.setInventorySlotContents(0, itemstack);
            serverplayerentity.connection.sendPacket(new SSetSlotPacket(id, 0, itemstack));
        }
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        callable.consume((p_217069_1_, p_217069_2_) -> {
            updateCraftingResult(this.windowId, p_217069_1_, player, core, result);
        });
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        callable.consume((p_217068_2_, p_217068_3_) -> {
            this.clearContainer(playerIn, p_217068_2_, this.core);
            this.clearContainer(playerIn, p_217068_2_, this.extras);
        });
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(this.callable, playerIn, Registry.WORKTABLE.get());
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index == 0) {
                callable.consume((p_217067_2_, p_217067_3_) -> {
                    itemstack1.getItem().onCreated(itemstack1, p_217067_2_, playerIn);
                });
                if (!this.mergeItemStack(itemstack1, 14, 50, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index >= 14 && index < 50) {
                if (!this.mergeItemStack(itemstack1, 1, 14, false)) {
                    if (index < 41) {
                        if (!this.mergeItemStack(itemstack1, 41, 50, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.mergeItemStack(itemstack1, 14, 41, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.mergeItemStack(itemstack1, 14, 50, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);
            if (index == 0) {
                playerIn.dropItem(itemstack2, false);
            }
        }

        return itemstack;
    }

    @OnlyIn(Dist.CLIENT)
    public int getSize() {
        return 14;
    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return slotIn.inventory != result && super.canMergeSlot(stack, slotIn);
    }

    public int getOutputSlot() {
        return 0;
    }
}
