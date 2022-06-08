package elucent.eidolon.item;

import net.minecraft.world.item.ItemStack;

public class UnholySymbolItem extends ItemBase {
    public UnholySymbolItem(Properties builderIn) {
        super(builderIn);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        return stack.copy();
    }
}
