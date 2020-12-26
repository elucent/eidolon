package elucent.eidolon.capability;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.util.ResourceLocation;

public class ReputationImpl implements IReputation {
    Map<UUID, Map<ResourceLocation, ReputationEntry>> reputationMap = new HashMap<>();
    Map<UUID, Long> prayerTimes = new HashMap<>();

    @Override
    public double getReputation(UUID player, ResourceLocation deity) {
        return getReputationMap(player).computeIfAbsent(deity, (k) -> new ReputationEntry()).reputation;
    }

    @Override
    public void addReputation(UUID player, ResourceLocation deity, double amount) {
        ReputationEntry entry = getReputationMap(player).computeIfAbsent(deity, (k) -> new ReputationEntry());
        if (entry.lock == null) entry.reputation += amount;
    }

    @Override
    public void subtractReputation(UUID player, ResourceLocation deity, double amount) {
        ReputationEntry entry = getReputationMap(player).computeIfAbsent(deity, (k) -> new ReputationEntry());
        entry.reputation = Math.max(0, entry.reputation - amount);
    }

    @Override
    public void setReputation(UUID player, ResourceLocation deity, double amount) {
        ReputationEntry entry = getReputationMap(player).computeIfAbsent(deity, (k) -> new ReputationEntry());
        if (entry.lock == null || amount < 0) entry.reputation = amount;
    }

    @Override
    public boolean isLocked(UUID player, ResourceLocation deity) {
        return getReputationMap(player).computeIfAbsent(deity, (k) -> new ReputationEntry()).lock != null;
    }

    @Override
    public void lock(UUID player, ResourceLocation deity, ResourceLocation key) {
        getReputationMap(player).computeIfAbsent(deity, (k) -> new ReputationEntry()).lock = key;
    }

    @Override
    public boolean unlock(UUID player, ResourceLocation deity, ResourceLocation key) {
        ReputationEntry entry = getReputationMap(player).computeIfAbsent(deity, (k) -> new ReputationEntry());
        if (entry.lock != null && entry.lock.equals(key)) {
            entry.lock = null;
            return true;
        }
        return false;
    }

    @Override
    public void pray(UUID player, long time) {
        getPrayerTimes().put(player, time);
    }

    @Override
    public boolean canPray(UUID player, long time) {
        return !getPrayerTimes().containsKey(player) || getPrayerTimes().get(player) < time - 21000;
    }

    @Override
    public Map<UUID, Long> getPrayerTimes() {
        return prayerTimes;
    }

    @Override
    public Map<UUID, Map<ResourceLocation, ReputationEntry>> getReputationMap() {
        return reputationMap;
    }
}
