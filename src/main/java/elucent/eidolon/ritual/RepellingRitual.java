package elucent.eidolon.ritual;

import elucent.eidolon.Eidolon;
import elucent.eidolon.entity.ai.GoToPositionGoal;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.stream.Collectors;

public class RepellingRitual extends Ritual {
    public static final ResourceLocation SYMBOL = new ResourceLocation(Eidolon.MODID, "particle/repelling_ritual");

    public RepellingRitual() {
        super(SYMBOL, ColorUtil.packColor(255, 190, 212, 184));
    }

    @Override
    public RitualResult tick(Level world, BlockPos pos) {
        if (world.getGameTime() % 200 == 0) {
            List<Monster> monsters = world.getEntitiesOfClass(Monster.class, new AABB(pos).inflate(96, 16, 96));
            for (Monster a : monsters) {
                boolean hasGoal = a.goalSelector.getRunningGoals()
                    .filter((goal) -> goal.getGoal() instanceof GoToPositionGoal)
                    .count() > 0;
                if (!hasGoal && a.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) <= 80 * 80) {
                    var diff = a.blockPosition().subtract(pos);
                    Vec3 diffv = new Vec3(diff.getX(), 0, diff.getZ());
                    diffv = diffv.scale(90 / diffv.length());
                    int i = pos.getX() + (int)diffv.x, j = pos.getZ() + (int)diffv.z;
                    BlockPos target = world.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, new BlockPos(i, 0, j));
                    a.goalSelector.addGoal(1, new GoToPositionGoal(a, target, 1.0));
                } else if (hasGoal && a.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) > 88 * 88) {
                    List<Goal> goals = a.goalSelector.getRunningGoals().filter((goal) -> goal.getGoal() instanceof GoToPositionGoal)
                        .collect(Collectors.toList());
                    for (Goal g : goals) a.goalSelector.removeGoal(g);
                }
            }
        }
        return RitualResult.PASS;
    }
}
