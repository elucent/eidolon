package elucent.eidolon.ritual;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.entity.ai.GoToPositionGoal;
import elucent.eidolon.network.CrystallizeEffectPacket;
import elucent.eidolon.network.Networking;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

import java.util.List;
import java.util.stream.Collectors;

public class RepellingRitual extends Ritual {
    public static final ResourceLocation SYMBOL = new ResourceLocation(Eidolon.MODID, "particle/repelling_ritual");

    public RepellingRitual() {
        super(SYMBOL, ColorUtil.packColor(255, 190, 212, 184));
    }

    @Override
    public RitualResult tick(World world, BlockPos pos) {
        if (world.getGameTime() % 200 == 0) {
            List<MonsterEntity> monsters = world.getEntitiesWithinAABB(MonsterEntity.class, new AxisAlignedBB(pos).grow(96, 16, 96));
            for (MonsterEntity a : monsters) {
                boolean hasGoal = a.goalSelector.getRunningGoals()
                    .filter((goal) -> goal.getGoal() instanceof GoToPositionGoal)
                    .count() > 0;
                if (!hasGoal && a.getDistanceSq(pos.getX(), pos.getY(), pos.getZ()) <= 80 * 80) {
                    Vector3i diff = a.getPosition().subtract(pos);
                    Vector3d diffv = new Vector3d(diff.getX(), 0, diff.getZ());
                    diffv = diffv.scale(90 / diffv.length());
                    int i = pos.getX() + (int)diffv.x, j = pos.getZ() + (int)diffv.z;
                    BlockPos target = world.getHeight(Heightmap.Type.WORLD_SURFACE, new BlockPos(i, 0, j));
                    a.goalSelector.addGoal(1, new GoToPositionGoal(a, target, 1.0));
                } else if (hasGoal && a.getDistanceSq(pos.getX(), pos.getY(), pos.getZ()) > 88 * 88) {
                    List<Goal> goals = a.goalSelector.getRunningGoals().filter((goal) -> goal.getGoal() instanceof GoToPositionGoal)
                        .collect(Collectors.toList());
                    for (Goal g : goals) a.goalSelector.removeGoal(g);
                }
            }
        }
        return RitualResult.PASS;
    }
}
