package elucent.eidolon.capability;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import elucent.eidolon.item.IWingsItem;
import elucent.eidolon.network.Networking;
import elucent.eidolon.network.WingsDashPacket;
import elucent.eidolon.network.WingsFlapPacket;
import elucent.eidolon.spell.Rune;
import elucent.eidolon.spell.Runes;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.spell.Signs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.util.INBTSerializable;

public class PlayerDataImpl implements IPlayerData, INBTSerializable<CompoundTag> {
    int wingCharges = 0, dashTicks = 0;
    long lastFlapTime = 0, flightStartTime = 0;
    boolean isFlying = false;

	@Override
	public int getWingCharges(Player player) {
		return wingCharges;
	}

	@Override
	public void rechargeWings(Player player) {
		wingCharges = getMaxWingCharges(player);
	}

	@Override
	public boolean tryFlapWings(Player player) {
		if (wingCharges <= 0 || getWingsItem(player).isEmpty()) {
			wingCharges = 0;
			return false;
		}
		else if (canFlap(player)) {
			wingCharges --;
			ItemStack wings = getWingsItem(player);
			if (wings.getItem() instanceof IWingsItem i) {
				if (isDashing(player)) {
					i.onDashFlap(player, player.level, wings, getDashTicks(player));
				}
				else {
					i.onFlap(player, player.level, wings, getDashTicks(player));
				}
				lastFlapTime = player.level.getGameTime();
				startFlying(player);
				if (player.level.isClientSide) Networking.sendToServer(new WingsFlapPacket(player));
			}
			return true;
		}
		return false;
	}
	
	public int getDashTicks(Player player) {
		return dashTicks;
	}
	
	public void doDashTick(Player player) {
		if (dashTicks > 0) {
			ItemStack wings = getWingsItem(player);
			if (wings.getItem() instanceof IWingsItem i) {
				i.onDashTick(player, player.level, wings, dashTicks);
			}
			dashTicks --;
			if (dashTicks == 0) {
				if (wings.getItem() instanceof IWingsItem i) {
					i.onDashEnd(player, player.level, wings);
				}
			}
		}
	}
	
	public boolean tryDash(Player player) {
		if (wingCharges > 0 && canFlap(player)) {
			wingCharges --;
			ItemStack wings = getWingsItem(player);
			if (wings.getItem() instanceof IWingsItem i) {
				i.onDashStart(player, player.level, wings);
				lastFlapTime = player.level.getGameTime();
				if (!isFlying) flightStartTime = lastFlapTime;
				isFlying = true;
				this.dashTicks = i.getDashTicks(wings);
				if (player.level.isClientSide) Networking.sendToServer(new WingsDashPacket(player));
			}
			return true;
		}
		else return false;
	}

	@Override
	public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("wingCharges", wingCharges); 
        tag.putInt("dashTicks", dashTicks);
        tag.putLong("lastFlapTime", lastFlapTime);
        tag.putLong("flightStartTime", flightStartTime);
        tag.putBoolean("isFlying", isFlying);
        return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
        wingCharges = nbt.getInt("wingCharges");
        dashTicks = nbt.getInt("dashTicks");
        lastFlapTime = nbt.getLong("lastFlapTime");
        flightStartTime = nbt.getLong("flightStartTime");
        isFlying = nbt.getBoolean("isFlying");
	}

	@Override
	public long getLastFlapTime(Player player) {
		return lastFlapTime;
	}

	@Override
	public boolean isFlying(Player player) {
		return isFlying;
	}
	
	static final AttributeModifier WINGS_SLOWFALL = new AttributeModifier(new UUID(4035878977813972397L, 2208061895106560443L), "Wings slow falling", -0.6, AttributeModifier.Operation.MULTIPLY_TOTAL);
    
	@Override
	public void startFlying(Player player) {
		if (!isFlying) {
			this.flightStartTime = player.level.getGameTime();
			isFlying = true;
			AttributeInstance attr = player.getAttribute(ForgeMod.ENTITY_GRAVITY.get());
			if (attr != null && !attr.hasModifier(WINGS_SLOWFALL)) attr.addTransientModifier(WINGS_SLOWFALL);
		}
	}

	@Override
	public void stopFlying(Player player) {
		if (isFlying) {
			isFlying = false;
			if (isDashing(player)) {
				setDashTicks(0);
				ItemStack wings = getWingsItem(player);
				if (wings.getItem() instanceof IWingsItem i) {
					i.onDashEnd(player, player.level, wings);
				}
			}
			this.flightStartTime = player.level.getGameTime();
			AttributeInstance attr = player.getAttribute(ForgeMod.ENTITY_GRAVITY.get());
			if (attr != null && attr.hasModifier(WINGS_SLOWFALL)) attr.removeModifier(WINGS_SLOWFALL);
		}
	}

	@Override
	public void setDashTicks(int ticks) {
		dashTicks = ticks;
	}

	@Override
	public void setLastFlapTime(long lastFlapTime) {
		this.lastFlapTime = lastFlapTime;
	}

	@Override
	public long getFlightStartTime(Player player) {
		return this.flightStartTime;
	}
}
