package elucent.eidolon.capability;

import java.util.Set;

import elucent.eidolon.spell.Sign;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public interface IKnowledge {
	public static final Capability<IKnowledge> INSTANCE = CapabilityManager.get(new CapabilityToken<>(){});
	
	public static class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
		KnowledgeImpl impl = new KnowledgeImpl();
		
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
	
    boolean knowsSign(Sign sign);
    void addSign(Sign sign);
    Set<Sign> getKnownSigns();

    boolean knowsFact(ResourceLocation fact);
    void addFact(ResourceLocation fact);
    Set<ResourceLocation> getKnownFacts();
}
