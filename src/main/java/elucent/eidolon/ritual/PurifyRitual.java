package elucent.eidolon.ritual;

import java.util.List;

import elucent.eidolon.Eidolon;
import elucent.eidolon.mixin.ZombieVillagerEntityMixin;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.entity.monster.ZoglinEntity;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class PurifyRitual extends Ritual {
    public static final ResourceLocation SYMBOL = new ResourceLocation(Eidolon.MODID, "particle/purify_ritual");

    public PurifyRitual() {
        super(SYMBOL, ColorUtil.packColor(255, 163, 252, 255));
    }

    @Override
    public RitualResult start(World world, BlockPos pos) {
        List<CreatureEntity> purifiable = world.getEntitiesWithinAABB(CreatureEntity.class, Ritual.getDefaultBounds(pos), (entity) -> entity instanceof ZombieVillagerEntity || entity instanceof ZombifiedPiglinEntity || entity instanceof ZoglinEntity);

        if (purifiable.size() > 0 && !world.isRemote) world.playSound(null, pos, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS, 1.0f, 1.0f);
        if (!world.isRemote) for (CreatureEntity entity : purifiable) {
            if (entity instanceof ZombieVillagerEntity) {
                ((ZombieVillagerEntityMixin)entity).callCureZombie((ServerWorld)world);
            }
            if (entity instanceof ZombifiedPiglinEntity) {
                entity.remove();
                PiglinEntity piglin = new PiglinEntity(EntityType.PIGLIN, world);
                piglin.copyLocationAndAnglesFrom(entity);
                piglin.onInitialSpawn((ServerWorld)world, world.getDifficultyForLocation(pos), SpawnReason.MOB_SUMMONED, null, null);
                world.addEntity(piglin);
            }
            if (entity instanceof ZoglinEntity) {
                entity.remove();
                HoglinEntity hoglin = new HoglinEntity(EntityType.HOGLIN, world);
                hoglin.copyLocationAndAnglesFrom(entity);
                hoglin.onInitialSpawn((ServerWorld)world, world.getDifficultyForLocation(pos), SpawnReason.MOB_SUMMONED, null, null);
                world.addEntity(hoglin);
            }
        }
        return RitualResult.TERMINATE;
    }
}
