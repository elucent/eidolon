package elucent.eidolon.capability;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;

public class ReputationStorage implements Capability.IStorage<IReputation> {
    @Override
    public INBT writeNBT(Capability<IReputation> capability, IReputation instance, Direction side) {
        CompoundNBT data = new CompoundNBT();
        CompoundNBT reps = new CompoundNBT();
        for (Entry<UUID, Map<ResourceLocation, ReputationEntry>> e : instance.getReputationMap().entrySet()) {
            CompoundNBT tag = new CompoundNBT();
            for (Entry<ResourceLocation, ReputationEntry> e2 : e.getValue().entrySet()) {
                CompoundNBT entry = new CompoundNBT();
                entry.putDouble("rep", e2.getValue().reputation);
                if (e2.getValue().lock != null) entry.putString("lock", e2.getValue().lock.toString());
                tag.put(e2.getKey().toString(), entry);
            }
            reps.put(e.getKey().toString(), tag);
        }
        CompoundNBT times = new CompoundNBT();
        for (Entry<UUID, Long> e : instance.getPrayerTimes().entrySet()) {
            times.putLong(e.getKey().toString(), e.getValue());
        }
        data.put("reps", reps);
        data.put("times", times);
        return data;
    }

    @Override
    public void readNBT(Capability<IReputation> capability, IReputation instance, Direction side, INBT nbt) {
        CompoundNBT data = (CompoundNBT)nbt;
        instance.getReputationMap().clear();
        if (data.contains("reps")) {
            CompoundNBT reps = data.getCompound("reps");
            for (String uuidString : reps.keySet()) {
                UUID uuid = UUID.fromString(uuidString);
                CompoundNBT tag = reps.getCompound(uuidString);
                for (String deity : tag.keySet()) {
                    CompoundNBT entry = tag.getCompound(deity);
                    instance.setReputation(uuid, new ResourceLocation(deity), entry.getDouble("rep"));
                    if (entry.contains("lock"))
                        instance.lock(uuid, new ResourceLocation(deity), new ResourceLocation(entry.getString("lock")));
                }
            }
        }
        if (data.contains("times")) {
            CompoundNBT times = data.getCompound("times");
            for (String uuidString : times.keySet()) {
                UUID uuid = UUID.fromString(uuidString);
                instance.pray(uuid, times.getLong(uuidString));
            }
        }
    }
}