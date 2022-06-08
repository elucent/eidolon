package elucent.eidolon.ritual;

import net.minecraft.world.item.ItemStack;

public interface IRitualItemProvider {
    ItemStack provide();
    void take();
}
