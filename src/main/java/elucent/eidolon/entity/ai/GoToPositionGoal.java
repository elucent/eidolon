package elucent.eidolon.entity.ai;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

import java.util.Random;

public class GoToPositionGoal extends Goal {
    Random random = new Random();
    BlockPos dest;
    CreatureEntity creature;
    double speed;
    boolean running;
    public GoToPositionGoal(CreatureEntity creature, BlockPos pos, double speedIn) {
        this.creature = creature;
        this.dest = pos;
        this.speed = speedIn;
        this.running = true;
    }

    @Override
    public void tick() {
        if (running) {
            creature.getNavigator().tryMoveToXYZ(dest.getX(), dest.getY(), dest.getZ(), speed);
            if (creature.getDistanceSq(dest.getX(), dest.getY(), dest.getZ()) < 8 * 8) running = false;
        }
    }

    @Override
    public boolean shouldExecute() {
        return running;
    }
}
