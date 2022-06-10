package elucent.eidolon.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Map;
import java.util.UUID;

public class ReputationProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
    public static Capability<IReputation> CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

    IReputation instance = new ReputationImpl();

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == CAPABILITY ? LazyOptional.of(() -> (T)instance) : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag data = new CompoundTag();
        CompoundTag reps = new CompoundTag();
        for (Map.Entry<UUID, Map<ResourceLocation, ReputationEntry>> e : instance.getReputationMap().entrySet()) {
            CompoundTag tag = new CompoundTag();
            for (Map.Entry<ResourceLocation, ReputationEntry> e2 : e.getValue().entrySet()) {
                CompoundTag entry = new CompoundTag();
                entry.putDouble("rep", e2.getValue().reputation);
                if (e2.getValue().lock != null) entry.putString("lock", e2.getValue().lock.toString());
                tag.put(e2.getKey().toString(), entry);
            }
            reps.put(e.getKey().toString(), tag);
        }
        CompoundTag times = new CompoundTag();
        for (Map.Entry<UUID, Long> e : instance.getPrayerTimes().entrySet()) {
            times.putLong(e.getKey().toString(), e.getValue());
        }
        data.put("reps", reps);
        data.put("times", times);
        return data;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        instance.getReputationMap().clear();
        if (nbt.contains("reps")) {
            CompoundTag reps = nbt.getCompound("reps");
            for (String uuidString : reps.getAllKeys()) {
                UUID uuid = UUID.fromString(uuidString);
                CompoundTag tag = reps.getCompound(uuidString);
                for (String deity : tag.getAllKeys()) {
                    CompoundTag entry = tag.getCompound(deity);
                    instance.setReputation(uuid, new ResourceLocation(deity), entry.getDouble("rep"));
                    if (entry.contains("lock"))
                        instance.lock(uuid, new ResourceLocation(deity), new ResourceLocation(entry.getString("lock")));
                }
            }
        }
        if (nbt.contains("times")) {
            CompoundTag times = nbt.getCompound("times");
            for (String uuidString : times.getAllKeys()) {
                UUID uuid = UUID.fromString(uuidString);
                instance.pray(uuid, times.getLong(uuidString));
            }
        }
    }
}