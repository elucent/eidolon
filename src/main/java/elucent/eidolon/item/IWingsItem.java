package elucent.eidolon.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IWingsItem {
	int getMaxCharges(ItemStack stack);
	void onFlap(Player player, Level level, ItemStack stack, int nCharges);
	int getDashTicks(ItemStack stack);
	void onDashStart(Player player, Level level, ItemStack stack);
	void onDashTick(Player player, Level level, ItemStack stack, int remainingTicks);
	void onDashEnd(Player player, Level level, ItemStack stack);
	void onDashFlap(Player player, Level level, ItemStack stack, int dashTicks);
}
