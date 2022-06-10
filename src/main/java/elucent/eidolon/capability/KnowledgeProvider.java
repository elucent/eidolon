package elucent.eidolon.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;

public class KnowledgeProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
    public static Capability<IKnowledge> CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

    IKnowledge instance = new KnowledgeImpl();

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return CAPABILITY.orEmpty(cap, LazyOptional.of(() -> instance));
    }

    @Override
    public CompoundTag serializeNBT() {
        return instance.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        instance.deserializeNBT(nbt);
    }
}