package elucent.eidolon.block;

import elucent.eidolon.Registry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

public class PlinthBlockBase extends BlockBase implements IWaterLoggable {
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public PlinthBlockBase(Properties properties) {
        super(properties);
    }

    protected boolean canConnectTo(Level world, BlockPos pos, Direction dir) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof PlinthBlockBase && dir.getAxis() == Direction.Axis.Y) return true;
        if (dir == Direction.UP && state.getBlock() == Registry.STONE_HAND.get()) return true;
        return false;
    }

    protected BlockState getState(Level world, BlockPos pos) {
        return this.defaultBlockState()
            .setValue(TOP, canConnectTo(world, pos.above(), Direction.UP))
            .setValue(BOTTOM, canConnectTo(world, pos.below(), Direction.DOWN))
            .setValue(WATERLOGGED, Boolean.valueOf(world.getFluidState(pos).getType() == Fluids.WATER));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return !state.getValue(WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return getState(context.getLevel(), context.getClickedPos());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, Level world, BlockPos pos, BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            world.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }

        if (facing == Direction.UP) state = state.setValue(TOP, canConnectTo(world, pos.above(), Direction.UP));
        if (facing == Direction.DOWN) state = state.setValue(BOTTOM, canConnectTo(world, pos.below(), Direction.DOWN));

        return state;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TOP, BOTTOM, WATERLOGGED);
    }
}
