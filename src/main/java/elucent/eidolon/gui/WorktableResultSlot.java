package elucent.eidolon.gui;

import elucent.eidolon.recipe.WorktableRecipe;
import elucent.eidolon.recipe.WorktableRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.world.Container;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.container.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.sounds.SoundEvents;

public class WorktableResultSlot extends Slot {
    private final CraftingInventory core, extras;
    private final Player player;
    private int amountCrafted;

    public WorktableResultSlot(Player player, CraftingInventory core, CraftingInventory extras, Container inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.player = player;
        this.core = core;
        this.extras = extras;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack remove(int amount) {
        if (this.hasItem()) {
            this.amountCrafted += Math.min(amount, this.getItem().getCount());
        }

        return super.remove(amount);
    }

    @Override
    protected void onQuickCraft(ItemStack stack, int amount) {
        this.amountCrafted += amount;
        this.checkTakeAchievements(stack);
    }

    protected void onSwapCraft(int numItemsCrafted) {
        this.amountCrafted += numItemsCrafted;
    }

    @Override
    protected void checkTakeAchievements(ItemStack stack) {
        if (this.amountCrafted > 0) {
            stack.onCraftedBy(this.player.level, this.player, this.amountCrafted);
            net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerCraftingEvent(this.player, stack, core);
        }

        if (this.container instanceof IRecipeHolder) {
            ((IRecipeHolder)this.container).awardUsedRecipes(this.player);
        }

        player.playSound(SoundEvents.SMITHING_TABLE_USE, 1.0f, 1.0f);

        this.amountCrafted = 0;
    }

    @Override
    public ItemStack onTake(Player thePlayer, ItemStack stack) {
        this.checkTakeAchievements(stack);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(thePlayer);
        WorktableRecipe recipe = WorktableRegistry.find(thePlayer.level, core, extras);
        NonNullList<ItemStack> items = null;
        if (recipe != null) {
            items = recipe.getRemainingItems(core, extras);
        }
        else {
            items = NonNullList.create();
            items.addAll(thePlayer.level.getRecipeManager().getRemainingItemsFor(RecipeType.CRAFTING, core, thePlayer.level));
            for (int i = 0; i < 4; i ++) items.add(extras.getItem(i));
        }
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);
        assert items != null;

        for(int i = 0; i < (recipe != null ? items.size() : 9); ++i) {
            Container inv = i < 9 ? core : extras;
            int index = i < 9 ? i : i - 9;
            ItemStack item = inv.getItem(index);
            ItemStack remaining = items.get(i);
            if (!item.isEmpty()) {
                inv.removeItem(index, 1);
                item = inv.getItem(index);
            }

            if (!remaining.isEmpty()) {
                if (item.isEmpty()) {
                    inv.setItem(index, remaining);
                } else if (ItemStack.isSame(item, remaining) && ItemStack.tagMatches(item, remaining)) {
                    // remaining.grow(item.getCount());
                    inv.setItem(index, remaining);
                } else if (!this.player.inventory.add(remaining)) {
                    this.player.drop(remaining, false);
                }
            }
        }

        return stack;
    }
}
