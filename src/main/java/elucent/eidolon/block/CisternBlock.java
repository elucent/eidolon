package elucent.eidolon.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.Direction;
import elucent.eidolon.tile.reagent.CisternTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;

public class CisternBlock extends BlockBase implements EntityBlock {
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");

    public CisternBlock(Properties properties) {
        super(properties);
    }

    protected boolean canConnectTo(LevelAccessor world, BlockPos pos, Direction dir) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof CisternBlock && dir.getAxis() == Direction.Axis.Y) return true;
        return false;
    }

    protected BlockState getState(Level world, BlockPos pos) {
        return this.defaultBlockState()
            .setValue(TOP, canConnectTo(world, pos.above(), Direction.UP))
            .setValue(BOTTOM, canConnectTo(world, pos.below(), Direction.DOWN));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return getState(context.getLevel(), context.getClickedPos());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos pos, BlockPos facingPos) {
        if (facing == Direction.UP) state = state.setValue(TOP, canConnectTo(world, pos.above(), Direction.UP));
        if (facing == Direction.DOWN) state = state.setValue(BOTTOM, canConnectTo(world, pos.below(), Direction.DOWN));

        return state;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TOP, BOTTOM);
    }

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CisternTileEntity(pos, state);
	}
}
