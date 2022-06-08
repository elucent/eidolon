package elucent.eidolon.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.INBT;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class ReputationStorage implements Capability.IStorage<IReputation> {
    @Override
    public INBT writeNBT(Capability<IReputation> capability, IReputation instance, Direction side) {
        CompoundTag data = new CompoundTag();
        CompoundTag reps = new CompoundTag();
        for (Entry<UUID, Map<ResourceLocation, ReputationEntry>> e : instance.getReputationMap().entrySet()) {
            CompoundTag tag = new CompoundTag();
            for (Entry<ResourceLocation, ReputationEntry> e2 : e.getValue().entrySet()) {
                CompoundTag entry = new CompoundTag();
                entry.putDouble("rep", e2.getValue().reputation);
                if (e2.getValue().lock != null) entry.putString("lock", e2.getValue().lock.toString());
                tag.put(e2.getKey().toString(), entry);
            }
            reps.put(e.getKey().toString(), tag);
        }
        CompoundTag times = new CompoundTag();
        for (Entry<UUID, Long> e : instance.getPrayerTimes().entrySet()) {
            times.putLong(e.getKey().toString(), e.getValue());
        }
        data.put("reps", reps);
        data.put("times", times);
        return data;
    }

    @Override
    public void readNBT(Capability<IReputation> capability, IReputation instance, Direction side, INBT nbt) {
        CompoundTag data = (CompoundTag)nbt;
        instance.getReputationMap().clear();
        if (data.contains("reps")) {
            CompoundTag reps = data.getCompound("reps");
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
        if (data.contains("times")) {
            CompoundTag times = data.getCompound("times");
            for (String uuidString : times.getAllKeys()) {
                UUID uuid = UUID.fromString(uuidString);
                instance.pray(uuid, times.getLong(uuidString));
            }
        }
    }
}