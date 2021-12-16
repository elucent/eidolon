package elucent.eidolon.tile.reagent;

import java.util.ArrayList;
import java.util.List;

import elucent.eidolon.reagent.IReagentTankProvider;
import elucent.eidolon.reagent.ReagentStack;
import elucent.eidolon.reagent.ReagentTank;
import elucent.eidolon.tile.TileEntityBase;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

public abstract class ReagentTankTileEntity extends TileEntityBase implements IReagentTankProvider {
    ReagentTank tank;

    public ReagentTankTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int capacity) {
        super(type, pos, state);
        tank = new ReagentTank(capacity);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        tank.read(tag.getCompound("tank"));
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag = super.save(tag);
        tag.put("tank", tank.write());
        return tag;
    }

    public void tick() {
        if (tank.getContents().amount == 0) return;
        List<IReagentTankProvider> tanks = new ArrayList<>();
        for (Direction d : Direction.values()) {
            if (isOutput(d)) {
                BlockEntity t = level.getBlockEntity(worldPosition.relative(d));
                if (t instanceof IReagentTankProvider
                    && ((IReagentTankProvider)t).isInput(d.getOpposite())
                    && ((IReagentTankProvider)t).getTank().canFill(tank.getContents())
                    && (((IReagentTankProvider)t).getTank().getPressure() < tank.getPressure() || d == Direction.DOWN))
                    tanks.add((IReagentTankProvider)t);
            }
        }
        if (tanks.isEmpty()) return;
        float total = tank.getPressure(), count = 1;
        for (IReagentTankProvider t : tanks) {
            total += t.getTank().getPressure();
            count++;
        }
        boolean dirty = false;
        float avg = total / count;
        int shared = (tank.getContents().amount - Mth.ceil(tank.getCapacity() * avg));
        float totalPressure = 0;
        for (IReagentTankProvider t : tanks) totalPressure += avg - t.getTank().getPressure();
        int[] toSend = new int[tanks.size()];
        int i = 0;
        int sum = 0;
        for (IReagentTankProvider t : tanks) {
            float minDiff = 2.0f / t.getTank().getCapacity();
            float diff = avg - t.getTank().getPressure();
            if (diff > 0 && diff < minDiff) diff = minDiff;
            int change = (int) (shared * ((avg - t.getTank().getPressure()) / totalPressure));
            int needed = (int) (diff * t.getTank().getCapacity());
            toSend[i] = Math.min(64, Math.min(change, needed));
            sum += toSend[i];
            i ++;
        }
        if (tank.getContents().amount - sum <= tanks.size() * 4) {
            for (int j = 0; j < tank.getContents().amount - sum; j ++) toSend[j % tanks.size()] ++;
        }
        i = 0;
        for (IReagentTankProvider t : tanks) {
            int change = Math.min(tank.getContents().amount, toSend[i ++]);
            if (change > 0) {
                if (t.getTank().fill(level, ((BlockEntity) t).getBlockPos(), new ReagentStack(tank.getContents().reagent, change))) {
                    tank.getContents().amount -= change;
                    t.onContentsChanged();
                    dirty = true;
                }
            }
        }
        if (dirty) onContentsChanged();
    }

    @Override
    public ReagentTank getTank() {
        return tank;
    }

    @Override
    public void onContentsChanged() {
        sync();
    }
}
