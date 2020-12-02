package elucent.eidolon.capability;

import elucent.eidolon.spell.Sign;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface IKnowledge {
    boolean knowsSign(Sign sign);
    void addSign(Sign sign);
    Set<Sign> getKnownSigns();
}
