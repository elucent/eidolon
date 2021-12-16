package elucent.eidolon.ritual;

import java.util.ArrayList;
import java.util.List;

import elucent.eidolon.Registry;
import elucent.eidolon.network.Networking;
import elucent.eidolon.network.RitualConsumePacket;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class HealthRequirement implements IRequirement {
    float health;

    public HealthRequirement(float health) {
        this.health = health;
    }

    @Override
    public RequirementInfo isMet(Ritual ritual, Level world, BlockPos pos) {
        List<PathfinderMob> entities = world.getEntitiesOfClass(PathfinderMob.class, Ritual.getDefaultBounds(pos), (entity) -> !entity.isInvertedHealAndHarm());
        List<Player> players = world.getEntitiesOfClass(Player.class, Ritual.getDefaultBounds(pos));
        List<LivingEntity> targets = new ArrayList<>();
        targets.addAll(entities);
        targets.addAll(players);
        float acc = 0;
        for (int i = 0; i < targets.size(); i ++) {
            acc += targets.get(i).getHealth();
            if (acc >= this.health) return RequirementInfo.TRUE;
        }
        return RequirementInfo.FALSE;
    }

    public void whenMet(Ritual ritual, Level world, BlockPos pos, RequirementInfo info) {
        List<PathfinderMob> entities = world.getEntitiesOfClass(PathfinderMob.class, Ritual.getDefaultBounds(pos), (entity) -> !entity.isInvertedHealAndHarm());
        List<Player> players = world.getEntitiesOfClass(Player.class, Ritual.getDefaultBounds(pos));
        List<LivingEntity> targets = new ArrayList<>();
        targets.addAll(entities);
        targets.addAll(players);
        float acc = 0;
        for (int i = 0; i < targets.size(); i ++) {
            float targetHealth = targets.get(i).getHealth();
            if (this.health - acc < targetHealth)
                targets.get(i).hurt(Registry.RITUAL_DAMAGE, this.health - acc);
            else targets.get(i).hurt(Registry.RITUAL_DAMAGE, targetHealth);

            acc += targetHealth;
            if (!world.isClientSide) Networking.sendToTracking(world, pos, new RitualConsumePacket(targets.get(i).blockPosition(), pos, ritual.getRed(), ritual.getGreen(), ritual.getBlue()));
            if (acc >= this.health) return;
        }
    }
}
