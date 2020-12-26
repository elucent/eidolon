package elucent.eidolon.ritual;

import java.util.List;
import java.util.stream.Collectors;

import elucent.eidolon.Eidolon;
import elucent.eidolon.entity.ai.GoToPositionGoal;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AllureRitual extends Ritual {
    public static final ResourceLocation SYMBOL = new ResourceLocation(Eidolon.MODID, "particle/allure_ritual");

    public AllureRitual() {
        super(SYMBOL, ColorUtil.packColor(255, 255, 43, 75));
    }

    @Override
    public RitualResult tick(World world, BlockPos pos) {
        if (world.getGameTime() % 200 == 0) {
            List<AnimalEntity> animals = world.getEntitiesWithinAABB(AnimalEntity.class, new AxisAlignedBB(pos).grow(96, 16, 96));
            for (AnimalEntity a : animals) {
                boolean hasGoal = a.goalSelector.getRunningGoals()
                    .filter((goal) -> goal.getGoal() instanceof GoToPositionGoal)
                    .count() > 0;
                if (!hasGoal && a.getDistanceSq(pos.getX(), pos.getY(), pos.getZ()) >= 12 * 12 && world.rand.nextInt(40) == 0) {
                    BlockPos target = pos.down().add(world.rand.nextInt(9) - 4, 0, world.rand.nextInt(9) - 4);
                    a.goalSelector.addGoal(1, new GoToPositionGoal(a, target, 1.0));
                } else if (hasGoal && a.getDistanceSq(pos.getX(), pos.getY(), pos.getZ()) < 8 * 8) {
                    List<Goal> goals = a.goalSelector.getRunningGoals().filter((goal) -> goal.getGoal() instanceof GoToPositionGoal)
                        .collect(Collectors.toList());
                    for (Goal g : goals) a.goalSelector.removeGoal(g);
                }
            }
        }
        return RitualResult.PASS;
    }
}
