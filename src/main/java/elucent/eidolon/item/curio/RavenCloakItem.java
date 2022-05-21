package elucent.eidolon.item.curio;

import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.capability.IPlayerData;
import elucent.eidolon.item.IWingsItem;
import elucent.eidolon.item.ItemBase;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;

public class RavenCloakItem extends ItemBase implements IWingsItem {
    UUID ATTR_ID = new UUID(261693807752044433L, 1154961640602571210L);
    public RavenCloakItem(Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.addListener(RavenCloakItem::onFall);
    }

    @SubscribeEvent
    public static void onFall(LivingFallEvent event) {
        if (CuriosApi.getCuriosHelper().findEquippedCurio(Registry.GRAVITY_BELT.get(), event.getEntityLiving()).isPresent()) {
            event.setDistance(event.getDistance() / 4);
        }
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag unused) {
        return new EidolonCurio(stack) {
            @Override
            public boolean canRightClickEquip() {
                return true;
            }
        };
    }
    
    public static final int MAX_CHARGES = 12;

	@Override
	public int getMaxCharges(ItemStack stack) {
		return MAX_CHARGES;
	}

	@Override
	public void onFlap(Player player, Level level, ItemStack stack, int nCharges) {
		player.setDeltaMovement(player.getDeltaMovement().add(player.getLookAngle().scale(0.25)).multiply(1, 0, 1).add(0, 0.5, 0));
	}

	@Override
	public int getDashTicks(ItemStack stack) {
		return 100;
	}

	@Override
	public void onDashStart(Player player, Level level, ItemStack stack) {
		//
	}

	@Override
	public void onDashTick(Player player, Level level, ItemStack stack, int remainingTicks) {
		float coeff = remainingTicks / (float)getDashTicks(stack);
		coeff = 1 - (1 - coeff) * (1 - coeff) + 0.25f;
		player.setDeltaMovement(player.getDeltaMovement().scale(0.8).add(player.getLookAngle().scale(coeff * 0.2)));
	}

	@Override
	public void onDashEnd(Player player, Level level, ItemStack stack) {
		//
	}

	@Override
	public void onDashFlap(Player player, Level level, ItemStack stack, int dashTicks) {
		player.getCapability(IPlayerData.INSTANCE).ifPresent(d -> d.setDashTicks(getDashTicks(stack)));
	}
}
