package elucent.eidolon.capability;

import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReputationImpl implements IReputation {
    Map<UUID, Map<ResourceLocation, Double>> reputationMap = new HashMap<>();

    @Override
    public double getReputation(UUID player, ResourceLocation deity) {
        return getReputationMap(player).putIfAbsent(deity, 0.0);
    }

    @Override
    public void addReputation(UUID player, ResourceLocation deity, double amount) {
        getReputationMap(player).put(deity, getReputation(player, deity) + amount);
    }

    @Override
    public void subtractReputation(UUID player, ResourceLocation deity, double amount) {
        getReputationMap(player).put(deity, Math.max(0, getReputation(player, deity) - amount));
    }

    @Override
    public void setReputation(UUID player, ResourceLocation deity, double amount) {
        getReputationMap(player).put(deity, amount);
    }

    @Override
    public Map<UUID, Map<ResourceLocation, Double>> getReputationMap() {
        return reputationMap;
    }
}
