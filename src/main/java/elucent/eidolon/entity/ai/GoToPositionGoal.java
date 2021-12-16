package elucent.eidolon.entity.ai;

import java.util.Random;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.core.BlockPos;

public class GoToPositionGoal extends Goal {
    Random random = new Random();
    BlockPos dest;
    PathfinderMob creature;
    double speed;
    boolean running;
    public GoToPositionGoal(PathfinderMob creature, BlockPos pos, double speedIn) {
        this.creature = creature;
        this.dest = pos;
        this.speed = speedIn;
        this.running = true;
    }

    @Override
    public void tick() {
        if (running) {
            creature.getNavigation().moveTo(dest.getX(), dest.getY(), dest.getZ(), speed);
            if (creature.distanceToSqr(dest.getX(), dest.getY(), dest.getZ()) < 8 * 8) running = false;
        }
    }

    @Override
    public boolean canUse() {
        return running;
    }
}
