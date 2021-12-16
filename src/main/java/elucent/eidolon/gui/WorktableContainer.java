package elucent.eidolon.gui;

import java.util.Optional;

import elucent.eidolon.Registry;
import elucent.eidolon.recipe.WorktableRecipe;
import elucent.eidolon.recipe.WorktableRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class WorktableContainer extends AbstractContainerMenu {
    CraftingContainer core = new CraftingContainer(this, 3, 3), extras = new CraftingContainer(this, 2, 2);
    ResultContainer result = new ResultContainer();
    Player player;
    ContainerLevelAccess callable;

    public WorktableContainer(int id, Inventory inventory) {
        this(id, inventory, ContainerLevelAccess.NULL);
    }

    public WorktableContainer(int id, Inventory inventory, ContainerLevelAccess callable) {
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

    protected void updateCraftingResult(int id, Level world, Player player, CraftingContainer inventory, ResultContainer inventoryResult) {
        if (!world.isClientSide) {
            ServerPlayer serverplayerentity = (ServerPlayer)player;
            ItemStack itemstack = ItemStack.EMPTY;
            WorktableRecipe recipe = WorktableRegistry.find(core, extras);
            if (recipe != null) {
                itemstack = recipe.getResult();
            }
            else {
                Optional<CraftingRecipe> optional = world.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, inventory, world);
                if (optional.isPresent()) {
                    CraftingRecipe icraftingrecipe = optional.get();
                    if (inventoryResult.setRecipeUsed(world, serverplayerentity, icraftingrecipe)) {
                        itemstack = icraftingrecipe.assemble(inventory);
                    }
                }
            }

            inventoryResult.setItem(0, itemstack);
            serverplayerentity.connection.send(new ClientboundContainerSetSlotPacket(id, incrementStateId(), 0, itemstack));
        }
    }

    @Override
    public void slotsChanged(Container inventoryIn) {
        callable.execute((p_217069_1_, p_217069_2_) -> {
            updateCraftingResult(this.containerId, p_217069_1_, player, core, result);
        });
    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        callable.execute((p_217068_2_, p_217068_3_) -> {
            this.clearContainer(playerIn, this.core);
            this.clearContainer(playerIn, this.extras);
        });
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(this.callable, playerIn, Registry.WORKTABLE.get());
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index == 0) {
                callable.execute((p_217067_2_, p_217067_3_) -> {
                    itemstack1.getItem().onCraftedBy(itemstack1, p_217067_2_, playerIn);
                });
                if (!this.moveItemStackTo(itemstack1, 14, 50, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index >= 14 && index < 50) {
                if (!this.moveItemStackTo(itemstack1, 1, 14, false)) {
                    if (index < 41) {
                        if (!this.moveItemStackTo(itemstack1, 41, 50, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(itemstack1, 14, 41, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(itemstack1, 14, 50, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    @OnlyIn(Dist.CLIENT)
    public int getSize() {
        return 14;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slotIn) {
        return slotIn.container != result && super.canTakeItemForPickAll(stack, slotIn);
    }

    public int getOutputSlot() {
        return 0;
    }
}
