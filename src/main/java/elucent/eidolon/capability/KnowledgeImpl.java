package elucent.eidolon.capability;

import elucent.eidolon.spell.Sign;

import java.util.*;

public class KnowledgeImpl implements IKnowledge {
    Set<Sign> signs = new HashSet<>();

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
}
