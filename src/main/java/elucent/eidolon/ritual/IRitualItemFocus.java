package elucent.eidolon.ritual;

import net.minecraft.world.item.ItemStack;

public interface IRitualItemFocus extends IRitualItemProvider {
	void replace(ItemStack stack);
}
