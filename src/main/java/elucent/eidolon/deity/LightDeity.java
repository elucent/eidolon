package elucent.eidolon.deity;

import elucent.eidolon.capability.IReputation;
import elucent.eidolon.spell.Signs;
import elucent.eidolon.util.KnowledgeUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;

public class LightDeity extends Deity {
    public LightDeity(ResourceLocation id, int red, int green, int blue) {
        super(id, red, green, blue);
    }

    @Override
    public void onReputationUnlock(Player player, IReputation rep, ResourceLocation lock) {
        if (lock.equals(DeityLocks.BASIC_INCENSE_PRAYER)) {
            KnowledgeUtil.grantSign(player, Signs.SOUL_SIGN);
        }
    }

    @Override
    public void onReputationChange(Player player, IReputation rep, double prev, double current) {
        if (!KnowledgeUtil.knowsSign(player, Signs.FLAME_SIGN) && current >= 3) {
            rep.setReputation(player, id, 3);
            rep.lock(player, id, DeityLocks.BASIC_INCENSE_PRAYER);
            KnowledgeUtil.grantSign(player, Signs.FLAME_SIGN);
        }
    }
}
