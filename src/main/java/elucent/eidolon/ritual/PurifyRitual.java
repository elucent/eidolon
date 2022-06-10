package elucent.eidolon.ritual;

import elucent.eidolon.Eidolon;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.level.Level;

import java.util.List;

public class PurifyRitual extends Ritual {
    public static final ResourceLocation SYMBOL = new ResourceLocation(Eidolon.MODID, "particle/purify_ritual");

    public PurifyRitual() {
        super(SYMBOL, ColorUtil.packColor(255, 163, 252, 255));
    }

    @Override
    public RitualResult start(Level world, BlockPos pos) {
        List<PathfinderMob> purifiable = world.getEntitiesOfClass(PathfinderMob.class, Ritual.getDefaultBounds(pos), (entity) -> entity instanceof ZombieVillager || entity instanceof ZombifiedPiglin || entity instanceof Zoglin);

        if (purifiable.size() > 0 && !world.isClientSide) world.playSound(null, pos, SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.PLAYERS, 1.0f, 1.0f);
        if (!world.isClientSide) for (PathfinderMob entity : purifiable) {
            if (entity instanceof ZombieVillager zombieVillager) {
                zombieVillager.finishConversion((ServerLevel)world);
            }
            if (entity instanceof ZombifiedPiglin) {
                entity.discard();
                var piglin = new Piglin(EntityType.PIGLIN, world);
                piglin.copyPosition(entity);
                piglin.finalizeSpawn((ServerLevel)world, world.getCurrentDifficultyAt(pos), MobSpawnType.MOB_SUMMONED, null, null);
                world.addFreshEntity(piglin);
            }
            if (entity instanceof Zoglin) {
                entity.discard();
                var hoglin = new Hoglin(EntityType.HOGLIN, world);
                hoglin.copyPosition(entity);
                hoglin.finalizeSpawn((ServerLevel)world, world.getCurrentDifficultyAt(pos), MobSpawnType.MOB_SUMMONED, null, null);
                world.addFreshEntity(hoglin);
            }
        }
        return RitualResult.TERMINATE;
    }
}
