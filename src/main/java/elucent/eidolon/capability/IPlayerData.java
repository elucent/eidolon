package elucent.eidolon.capability;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import elucent.eidolon.Registry;
import elucent.eidolon.deity.Deities;
import elucent.eidolon.deity.Deity;
import elucent.eidolon.item.IWingsItem;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosApi;

public interface IPlayerData {
	public static final Capability<IPlayerData> INSTANCE = CapabilityManager.get(new CapabilityToken<>(){});
	
	public static class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
		PlayerDataImpl impl = new PlayerDataImpl();
		
		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
			if (cap == INSTANCE) return (LazyOptional<T>) LazyOptional.of(() -> impl);
			else return LazyOptional.empty();
		}

		@Override
		public CompoundTag serializeNBT() {
			return impl.serializeNBT();
		}

		@Override
		public void deserializeNBT(CompoundTag nbt) {
			impl.deserializeNBT(nbt);
		}
	}

	default ItemStack getWingsItem(Player player) {
		ItemStack result[] = new ItemStack[] { ItemStack.EMPTY };
		CuriosApi.getCuriosHelper().getEquippedCurios(player).ifPresent((h) -> {
			for (int i = 0; i < h.getSlots(); i ++) {
				ItemStack s = h.getStackInSlot(i);
				if (s.getItem() instanceof IWingsItem) {
					result[0] = s;
					break;
				}
			}
		});
		return result[0];
	}
	
	default int getMaxWingCharges(Player player) {
		ItemStack wings = getWingsItem(player);
		if (wings.getItem() instanceof IWingsItem i) return i.getMaxCharges(wings);
		return 0;
	}
	
	default boolean isDashing(Player player) {
		return getDashTicks(player) > 0;
	}
	
	default boolean canFlap(Player player) {
		return !player.isOnGround() && !player.isInPowderSnow && !player.isSwimming() && !player.isPassenger() && !player.getAbilities().flying;
	}
	
	int getDashTicks(Player player);
	void doDashTick(Player player);
	boolean tryDash(Player player);
    int getWingCharges(Player player);
    void rechargeWings(Player player);
    boolean tryFlapWings(Player player);
    long getFlightStartTime(Player player);
    long getLastFlapTime(Player player);
    boolean isFlying(Player player);
    void startFlying(Player player);
    void stopFlying(Player player);
    void setDashTicks(int ticks);
    void setLastFlapTime(long lastFlapTime);
}
