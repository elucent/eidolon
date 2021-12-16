package elucent.eidolon.tile.reagent;

import elucent.eidolon.Registry;
import elucent.eidolon.block.PillarBlockBase;
import elucent.eidolon.reagent.ReagentStack;
import elucent.eidolon.reagent.ReagentTank;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class CisternTileEntity extends ReagentTankTileEntity {
    float upPressure = 0, downPressure = 0;

    public static class CisternTank extends ReagentTank {
        public CisternTileEntity tile;

        public CisternTank(CisternTileEntity tile, int capacity) {
            super(capacity);
            this.tile = tile;
        }

        @Override
        public boolean canFill(ReagentStack stack) {
            CisternTileEntity t = tile;
            while (t.getBlockState().getValue(PillarBlockBase.BOTTOM)) {
                BlockEntity nt = t.level.getBlockEntity(t.worldPosition.below());
                if (nt instanceof CisternTileEntity) t = (CisternTileEntity)nt;
                else break;
            }
            return t.tank.getContents().isEmpty() || t.tank.getContents().reagent == stack.reagent;
        }
    }

    public CisternTileEntity(BlockPos pos, BlockState state) {
        this(Registry.CISTERN_TILE_ENTITY, pos, state);
    }

    public CisternTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, 2048);
        tank = new CisternTank(this, 2048);
        // tank.fill(world, pos, new ReagentStack(ReagentRegistry.CRIMSOL, 2048));
    }

    @Override
    public void tick() {
        downPressure = upPressure = 0;
        if (getBlockState().getValue(PillarBlockBase.BOTTOM)) {
            BlockEntity t = level.getBlockEntity(worldPosition.below());
            if (t instanceof CisternTileEntity)
                downPressure = ((CisternTileEntity)t).tank.getPressure();
            if (t instanceof CisternTileEntity
                && ((CisternTileEntity)t).tank.getContents().amount < ((CisternTileEntity)t).tank.getCapacity()
                && tank.getContents().amount > 0) {
                CisternTileEntity c = (CisternTileEntity)t;
                int change = tank.getContents().amount;
                if (c.tank.getContents().amount + change > c.tank.getCapacity())
                    change = c.tank.getCapacity() - c.tank.getContents().amount;
                c.tank.fill(level, worldPosition, new ReagentStack(tank.getContents().reagent, change));
                tank.getContents().amount -= change;
                c.onContentsChanged();
                onContentsChanged();
            }
        }
        if (getBlockState().getValue(PillarBlockBase.TOP)) {
            BlockEntity t = level.getBlockEntity(worldPosition.above());
            if (t instanceof CisternTileEntity)
                upPressure = ((CisternTileEntity)t).tank.getPressure();
        }
        super.tick();
    }

    @Override
    public boolean isOutput(Direction direction) {
        return level.getBlockEntity(worldPosition.relative(direction)) instanceof PipeTileEntity;
    }

    @Override
    public boolean isInput(Direction direction) {
        return true;
    }
}
