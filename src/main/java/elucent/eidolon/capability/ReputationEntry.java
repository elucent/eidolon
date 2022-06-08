package elucent.eidolon.capability;


import net.minecraft.resources.ResourceLocation;

public class ReputationEntry {
    double reputation;
    ResourceLocation lock;
    public ReputationEntry() {
        this(0, null);
    }

    public ReputationEntry(double rep) {
        this(rep, null);
    }

    public ReputationEntry(double rep, ResourceLocation lock) {
        this.reputation = rep;
        this.lock = lock;
    }
}