package elucent.eidolon.block;

import elucent.eidolon.tile.TileEntityBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class TableBlockBase extends BlockBase {
    static VoxelShape NORMAL = VoxelShapes.create(0, 0.75, 0, 1, 1, 1),
        CORNER = VoxelShapes.combine(
            NORMAL,
            VoxelShapes.create(0.0625, 0, 0.0625, 0.9375, 0.75, 0.9375),
            IBooleanFunction.OR
        );
    public static BooleanProperty NXNY_CORNER = BooleanProperty.create("nxny"),
        PXNY_CORNER = BooleanProperty.create("pxny"),
        PXPY_CORNER = BooleanProperty.create("pxpy"),
        NXPY_CORNER = BooleanProperty.create("nxpy");

    public TableBlockBase(Properties properties) {
        super(properties);
        setDefaultState(super.getDefaultState()
            .with(NXNY_CORNER, false)
            .with(PXNY_CORNER, false)
            .with(PXPY_CORNER, false)
            .with(NXPY_CORNER, false));
    }

    @Override
    public VoxelShape getRaytraceShape(BlockState state, IBlockReader world, BlockPos pos) {
        if (state.get(NXNY_CORNER) || state.get(PXNY_CORNER)
            || state.get(PXPY_CORNER) || state.get(NXPY_CORNER)) return CORNER;
        return NORMAL;
    }

    protected BlockState updateCorners(IBlockReader world, BlockPos pos, BlockState state) {
        BlockState blockstate = world.getBlockState(pos.north());
        BlockState blockstate1 = world.getBlockState(pos.east());
        BlockState blockstate2 = world.getBlockState(pos.south());
        BlockState blockstate3 = world.getBlockState(pos.west());
        boolean conn1 = blockstate.getBlock() == this,
            conn2 = blockstate1.getBlock() == this,
            conn3 = blockstate2.getBlock() == this,
            conn4 = blockstate3.getBlock() == this;
        return state
            .with(NXNY_CORNER, !conn1 && !conn4).with(PXNY_CORNER, !conn1 && !conn2)
            .with(PXPY_CORNER, !conn3 && !conn2).with(NXPY_CORNER, !conn3 && !conn4);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        IBlockReader iblockreader = context.getWorld();
        BlockPos blockpos = context.getPos();
        return updateCorners(iblockreader, blockpos, super.getStateForPlacement(context));
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return updateCorners(worldIn, currentPos, stateIn);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NXNY_CORNER, PXNY_CORNER, PXPY_CORNER, NXPY_CORNER);
    }
}
