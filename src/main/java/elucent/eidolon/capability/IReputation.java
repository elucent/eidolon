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
    boolean isLocked(UUID player, ResourceLocation deity);
    void lock(UUID player, ResourceLocation deity, ResourceLocation key);
    boolean unlock(UUID player, ResourceLocation deity, ResourceLocation key);
    void pray(UUID player, long time);
    boolean canPray(UUID player, long time);

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

    default void lock(PlayerEntity player, ResourceLocation deity, ResourceLocation key) {
        lock(player.getUniqueID(), deity, key);
    }

    default boolean unlock(PlayerEntity player, ResourceLocation deity, ResourceLocation key) {
        return unlock(player.getUniqueID(), deity, key);
    }

    default void pray(PlayerEntity player, long time) {
        pray(player.getUniqueID(), time);
    }

    default boolean canPray(PlayerEntity player, long time) {
        return player.isCreative() || canPray(player.getUniqueID(), time);
    }

    Map<UUID, Long> getPrayerTimes();
    Map<UUID, Map<ResourceLocation, ReputationEntry>> getReputationMap();
    default Map<ResourceLocation, ReputationEntry> getReputationMap(UUID player) {
        return getReputationMap().computeIfAbsent(player, (k) -> new HashMap<>());
    }
}
