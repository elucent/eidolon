package elucent.eidolon.capability;

import java.util.HashSet;
import java.util.Set;

import elucent.eidolon.spell.Sign;
import net.minecraft.util.ResourceLocation;

public class KnowledgeImpl implements IKnowledge {
    Set<Sign> signs = new HashSet<>();
    Set<ResourceLocation> facts = new HashSet<>();

    @Override
    public boolean knowsSign(Sign sign) {
        return signs.contains(sign);
    }

    @Override
    public void addSign(Sign sign) {
        signs.add(sign);
    }

    @Override
    public Set<Sign> getKnownSigns() {
        return signs;
    }

    @Override
    public boolean knowsFact(ResourceLocation fact) {
        return facts.contains(fact);
    }

    @Override
    public void addFact(ResourceLocation fact) {
        facts.add(fact);
    }

    @Override
    public Set<ResourceLocation> getKnownFacts() {
        return facts;
    }
}
