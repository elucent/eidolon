package elucent.eidolon.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class ReputationStorage implements Capability.IStorage<IReputation> {
    @Override
    public INBT writeNBT(Capability<IReputation> capability, IReputation instance, Direction side) {
        CompoundNBT data = new CompoundNBT();
        for (Entry<UUID, Map<ResourceLocation, Double>> e : instance.getReputationMap().entrySet()) {
            CompoundNBT tag = new CompoundNBT();
            for (Entry<ResourceLocation, Double> e2 : e.getValue().entrySet()) {
                tag.putDouble(e2.getKey().toString(), e2.getValue());
            }
            data.put(e.getKey().toString(), tag);
        }
        return data;
    }

    @Override
    public void readNBT(Capability<IReputation> capability, IReputation instance, Direction side, INBT nbt) {
        CompoundNBT data = (CompoundNBT)nbt;
        instance.getReputationMap().clear();
        for (String uuidString : data.keySet()) {
            UUID uuid = UUID.fromString(uuidString);
            CompoundNBT tag = data.getCompound(uuidString);
            for (String deity : tag.keySet()) {
                instance.setReputation(uuid, new ResourceLocation(deity), tag.getDouble(deity));
            }
        }
    }
}