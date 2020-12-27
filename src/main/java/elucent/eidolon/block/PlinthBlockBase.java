package elucent.eidolon.block;

import elucent.eidolon.Registry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class PlinthBlockBase extends BlockBase implements IWaterLoggable {
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public PlinthBlockBase(Properties properties) {
        super(properties);
    }

    protected boolean canConnectTo(IWorld world, BlockPos pos, Direction dir) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof PlinthBlockBase && dir.getAxis() == Direction.Axis.Y) return true;
        if (dir == Direction.UP && state.getBlock() == Registry.STONE_HAND.get()) return true;
        return false;
    }

    protected BlockState getState(World world, BlockPos pos) {
        return this.getDefaultState()
            .with(TOP, canConnectTo(world, pos.up(), Direction.UP))
            .with(BOTTOM, canConnectTo(world, pos.down(), Direction.DOWN))
            .with(WATERLOGGED, Boolean.valueOf(world.getFluidState(pos).getFluid() == Fluids.WATER));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return !state.get(WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getState(context.getWorld(), context.getPos());
    }

    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos) {
        if (state.get(WATERLOGGED)) {
            world.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        if (facing == Direction.UP) state = state.with(TOP, canConnectTo(world, pos.up(), Direction.UP));
        if (facing == Direction.DOWN) state = state.with(BOTTOM, canConnectTo(world, pos.down(), Direction.DOWN));

        return state;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(TOP, BOTTOM, WATERLOGGED);
    }
}
