package elucent.eidolon.deity;

import elucent.eidolon.capability.Facts;
import elucent.eidolon.capability.IReputation;
import elucent.eidolon.spell.KnowledgeUtil;
import elucent.eidolon.spell.Signs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public class DarkDeity extends Deity {
    public DarkDeity(ResourceLocation id, int red, int green, int blue) {
        super(id, red, green, blue);
    }

    @Override
    public void onReputationUnlock(PlayerEntity player, IReputation rep, ResourceLocation lock) {
        if (lock.equals(DeityLocks.SACRIFICE_MOB)) {
            KnowledgeUtil.grantSign(player, Signs.SOUL_SIGN);
        }
        else if (lock.equals(DeityLocks.SACRIFICE_VILLAGER)) {
            KnowledgeUtil.grantSign(player, Signs.MIND_SIGN);
        }
    }

    @Override
    public void onReputationChange(PlayerEntity player, IReputation rep, double prev, double current) {
        if (prev < 3 && current >= 3) {
            rep.setReputation(player, id, 3);
            rep.lock(player, id, DeityLocks.SACRIFICE_MOB);
            KnowledgeUtil.grantSign(player, Signs.BLOOD_SIGN);
        }
        else if (prev < 15 && current >= 15) {
            rep.setReputation(player, id, 15);
            rep.lock(player, id, DeityLocks.SACRIFICE_VILLAGER);
            KnowledgeUtil.grantFact(player, Facts.VILLAGER_SACRIFICE);
        }
    }
}
