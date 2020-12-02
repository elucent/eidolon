package elucent.eidolon.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class WorktableRecipe {
    Object[] core, extras;
    ItemStack result;
    ResourceLocation registryName;

    public WorktableRecipe(Object[] core, Object[] extras, ItemStack result) {
        this.core = core;
        this.extras = extras;
        this.result = result;
    }

    public ResourceLocation getRegistryName() {
        return registryName;
    }

    public WorktableRecipe setRegistryName(String domain, String path) {
        this.registryName = new ResourceLocation(domain, path);
        return this;
    }

    public WorktableRecipe setRegistryName(ResourceLocation registryName) {
        this.registryName = registryName;
        return this;
    }

    static boolean matches(Object match, ItemStack sacrifice) {
        if (match instanceof ItemStack) {
            if (ItemStack.areItemStacksEqual((ItemStack)match, sacrifice)) return true;
        }
        else if (match instanceof Item) {
            if ((Item)match == sacrifice.getItem()) return true;
        }
        else if (match instanceof ITag) {
            if (((ITag<Item>)match).contains(sacrifice.getItem())) return true;
        }
        return false;
    }

    public boolean matches(IInventory coreInv, IInventory extraInv) {
        if (coreInv.getSizeInventory() < 9 || extraInv.getSizeInventory() < 4) return false;
        for (int i = 0; i < core.length; i ++) {
            if (!matches(core[i], coreInv.getStackInSlot(i))) return false;
        }
        for (int i = 0; i < extras.length; i ++) {
            if (!matches(extras[i], extraInv.getStackInSlot(i))) return false;
        }
        return true;
    }

    public NonNullList<ItemStack> getRemainingItems(IInventory coreInv, IInventory extraInv) {
        NonNullList<ItemStack> items = NonNullList.withSize(13, ItemStack.EMPTY);

        for(int i = 0; i < items.size(); ++i) {
            IInventory inv = i < 9 ? coreInv : extraInv;
            ItemStack item = inv.getStackInSlot(i < 9 ? i : i - 9);
            if (item.hasContainerItem()) {
                items.set(i, item.getContainerItem());
            }
        }

        return items;
    }

    public ItemStack getResult() {
        return result.copy();
    }
}
