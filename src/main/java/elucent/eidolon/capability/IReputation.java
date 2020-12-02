package elucent.eidolon.capability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public interface IReputation {
    double getReputation(UUID player, ResourceLocation deity);
    void addReputation(UUID player, ResourceLocation deity, double amount);
    void subtractReputation(UUID player, ResourceLocation deity, double amount);
    void setReputation(UUID player, ResourceLocation deity, double amount);

    default double getReputation(PlayerEntity player, ResourceLocation deity) {
        return getReputation(player.getUniqueID(), deity);
    }

    default void addReputation(PlayerEntity player, ResourceLocation deity, double amount) {
        addReputation(player.getUniqueID(), deity, amount);
    }

    default void subtractReputation(PlayerEntity player, ResourceLocation deity, double amount) {
        subtractReputation(player.getUniqueID(), deity, amount);
    }

    default void setReputation(PlayerEntity player, ResourceLocation deity, double amount) {
        setReputation(player.getUniqueID(), deity, amount);
    }

    Map<UUID, Map<ResourceLocation, Double>> getReputationMap();
    default Map<ResourceLocation, Double> getReputationMap(UUID player) {
        if (!getReputationMap().containsKey(player)) getReputationMap().put(player, new HashMap<>());
        return getReputationMap().get(player);
    }
}
