package elucent.eidolon.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;

public class TwoHighBlockBase extends BlockBase {
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;

    public TwoHighBlockBase(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return super.canSurvive(state, world, pos) && world.isEmptyBlock(pos.above());
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
        world.setBlockAndUpdate(pos.above(), defaultBlockState().setValue(HALF, Half.TOP));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(HALF, Half.BOTTOM);
    }

    @Override
    public void breakBlock(BlockState state, Level world, BlockPos pos) {
        if (state.getValue(HALF) == Half.BOTTOM && world.getBlockState(pos.above()).getBlock() == this)
            world.destroyBlock(pos.above(), false);
        else if (state.getValue(HALF) == Half.TOP && world.getBlockState(pos.below()).getBlock() == this)
            world.destroyBlock(pos.below(), false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF);
    }
}
