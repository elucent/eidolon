package elucent.eidolon.capability;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

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

    default double getReputation(Player player, ResourceLocation deity) {
        return getReputation(player.getUUID(), deity);
    }

    default void addReputation(Player player, ResourceLocation deity, double amount) {
        addReputation(player.getUUID(), deity, amount);
    }

    default void subtractReputation(Player player, ResourceLocation deity, double amount) {
        subtractReputation(player.getUUID(), deity, amount);
    }

    default void setReputation(Player player, ResourceLocation deity, double amount) {
        setReputation(player.getUUID(), deity, amount);
    }

    default void lock(Player player, ResourceLocation deity, ResourceLocation key) {
        lock(player.getUUID(), deity, key);
    }

    default boolean unlock(Player player, ResourceLocation deity, ResourceLocation key) {
        return unlock(player.getUUID(), deity, key);
    }

    default void pray(Player player, long time) {
        pray(player.getUUID(), time);
    }

    default boolean canPray(Player player, long time) {
        return player.isCreative() || canPray(player.getUUID(), time);
    }

    Map<UUID, Long> getPrayerTimes();
    Map<UUID, Map<ResourceLocation, ReputationEntry>> getReputationMap();
    default Map<ResourceLocation, ReputationEntry> getReputationMap(UUID player) {
        return getReputationMap().computeIfAbsent(player, (k) -> new HashMap<>());
    }
}
