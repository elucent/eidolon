package elucent.eidolon.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

public class TableBlockBase extends BlockBase implements IWaterLoggable {
    VoxelShape NORMAL = VoxelShapes.create(0, 0.75, 0, 1, 1, 1),
        CORNER = VoxelShapes.combine(
            NORMAL,
            VoxelShapes.create(0.0625, 0, 0.0625, 0.9375, 0.75, 0.9375),
            IBooleanFunction.OR
        );

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static BooleanProperty NX = BooleanProperty.create("nx"),
        PX = BooleanProperty.create("px"),
        NZ = BooleanProperty.create("nz"),
        PZ = BooleanProperty.create("pz");

    public TableBlockBase(Properties properties) {
        super(properties);
        setDefaultState(super.getDefaultState()
            .with(NX, false)
            .with(PX, false)
            .with(NZ, false)
            .with(PZ, false));
    }

    public TableBlockBase setMainShape(VoxelShape shape) {
        NORMAL = shape;
        CORNER = VoxelShapes.combine(
            NORMAL,
            VoxelShapes.create(0.0625, 0, 0.0625, 0.9375, 0.75, 0.9375),
            IBooleanFunction.OR
        );
        return this;
    }

    @Override
    public VoxelShape getRaytraceShape(BlockState state, IBlockReader world, BlockPos pos) {
        boolean nx = state.get(NX), px = state.get(PX), nz = state.get(NZ), pz = state.get(PZ);
        if ((!nx && !nz) || (!nx && !pz) || (!px && !pz) || (!px && !nz)) return CORNER;
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
            .with(NZ, conn1).with(PX, conn2)
            .with(PZ, conn3).with(NX, conn4);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        IBlockReader iblockreader = context.getWorld();
        BlockPos blockpos = context.getPos();
        return updateCorners(iblockreader, blockpos, super.getStateForPlacement(context)).with(WATERLOGGED, false);
    }

    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos) {
        if (state.get(WATERLOGGED)) {
            world.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return updateCorners(world, pos, state);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NX, PX, NZ, PZ, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return !state.get(WATERLOGGED);
    }
}
