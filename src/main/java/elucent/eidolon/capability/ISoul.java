package elucent.eidolon.capability;

import java.util.Set;

import javax.xml.stream.events.Attribute;

import elucent.eidolon.Registry;
import elucent.eidolon.spell.Sign;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public interface ISoul {
	public static final Capability<ISoul> INSTANCE = CapabilityManager.get(new CapabilityToken<>(){});
	
	public static class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
		SoulImpl impl = new SoulImpl();
		
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
	
	boolean hasEtherealHealth();
	float getMaxEtherealHealth();
	float getEtherealHealth();
	void setEtherealHealth(float health);
	void setMaxEtherealHealth(float max);
	
	default void hurtEtherealHealth(float amount, float persistentHealth) {
		amount = Math.max(0, amount);
		float oldHealth = getEtherealHealth();
		setMaxEtherealHealth(Math.max(getMaxEtherealHealth() - amount, Math.min(persistentHealth, getMaxEtherealHealth())));
		setEtherealHealth(oldHealth - amount);
	}
	
	default void healEtherealHealth(float amount, float persistentHealth) {
		amount = Math.max(0, amount);
		setEtherealHealth(Math.min(Math.max(getEtherealHealth(), persistentHealth), getEtherealHealth() + amount));
	}
	
	boolean hasMagic();
	float getMaxMagic();
	float getMagic();
	void setMagic(float magic);
	void setMaxMagic(float max);
	
	default void takeMagic(float amount) {
		amount = Math.max(0, amount);
		setMagic(getMagic() - amount);
	}
	
	default void giveMagic(float amount) {
		amount = Math.max(0, amount);
		setMagic(getMagic() + amount);
	}
	
	public static float getPersistentHealth(LivingEntity entity) {
		AttributeInstance attr = entity.getAttribute(Registry.PERSISTENT_SOUL_HEARTS.get());
		if (attr != null) return (float)attr.getValue();
		else return 0;
	}
}
