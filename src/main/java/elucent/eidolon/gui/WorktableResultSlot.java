package elucent.eidolon.gui;

import elucent.eidolon.recipe.WorktableRecipe;
import elucent.eidolon.recipe.WorktableRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvents;

import java.util.Optional;

public class WorktableResultSlot extends Slot {
    private final CraftingInventory core, extras;
    private final PlayerEntity player;
    private int amountCrafted;

    public WorktableResultSlot(PlayerEntity player, CraftingInventory core, CraftingInventory extras, IInventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.player = player;
        this.core = core;
        this.extras = extras;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        if (this.getHasStack()) {
            this.amountCrafted += Math.min(amount, this.getStack().getCount());
        }

        return super.decrStackSize(amount);
    }

    @Override
    protected void onCrafting(ItemStack stack, int amount) {
        this.amountCrafted += amount;
        this.onCrafting(stack);
    }

    protected void onSwapCraft(int numItemsCrafted) {
        this.amountCrafted += numItemsCrafted;
    }

    @Override
    protected void onCrafting(ItemStack stack) {
        if (this.amountCrafted > 0) {
            stack.onCrafting(this.player.world, this.player, this.amountCrafted);
            net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerCraftingEvent(this.player, stack, core);
        }

        if (this.inventory instanceof IRecipeHolder) {
            ((IRecipeHolder)this.inventory).onCrafting(this.player);
        }

        player.playSound(SoundEvents.BLOCK_SMITHING_TABLE_USE, 1.0f, 1.0f);

        this.amountCrafted = 0;
    }

    @Override
    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
        this.onCrafting(stack);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(thePlayer);
        WorktableRecipe recipe = WorktableRegistry.find(core, extras);
        NonNullList<ItemStack> items = null;
        if (recipe != null) {
            items = recipe.getRemainingItems(core, extras);
        }
        else {
            items = NonNullList.create();
            items.addAll(thePlayer.world.getRecipeManager().getRecipeNonNull(IRecipeType.CRAFTING, core, thePlayer.world));
            for (int i = 0; i < 4; i ++) items.add(extras.getStackInSlot(i));
        }
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);
        assert items != null;

        for(int i = 0; i < items.size(); ++i) {
            IInventory inv = i < 9 ? core : extras;
            int index = i < 9 ? i : i - 9;
            ItemStack item = inv.getStackInSlot(index);
            ItemStack remaining = items.get(i);
            if (!item.isEmpty()) {
                inv.decrStackSize(index, 1);
                item = inv.getStackInSlot(index);
            }

            if (!remaining.isEmpty()) {
                if (item.isEmpty()) {
                    inv.setInventorySlotContents(index, remaining);
                } else if (ItemStack.areItemsEqual(item, remaining) && ItemStack.areItemStackTagsEqual(item, remaining)) {
                    remaining.grow(item.getCount());
                    inv.setInventorySlotContents(index, remaining);
                } else if (!this.player.inventory.addItemStackToInventory(remaining)) {
                    this.player.dropItem(remaining, false);
                }
            }
        }

        return stack;
    }
}
