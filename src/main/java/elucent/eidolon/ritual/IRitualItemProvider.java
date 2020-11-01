package elucent.eidolon.ritual;

import net.minecraft.item.ItemStack;

public interface IRitualItemProvider {
    ItemStack provide();
    void take();
}
