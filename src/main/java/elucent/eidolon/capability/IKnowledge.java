package elucent.eidolon.capability;

import java.util.Set;

import elucent.eidolon.spell.Sign;
import net.minecraft.util.ResourceLocation;

public interface IKnowledge {
    boolean knowsSign(Sign sign);
    void addSign(Sign sign);
    Set<Sign> getKnownSigns();

    boolean knowsFact(ResourceLocation fact);
    void addFact(ResourceLocation fact);
    Set<ResourceLocation> getKnownFacts();
}
