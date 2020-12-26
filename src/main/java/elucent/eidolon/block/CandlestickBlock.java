package elucent.eidolon.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CandlestickBlock extends BlockBase {
    public static final DirectionProperty FACING = DirectionProperty.create("facing", (d) -> d != Direction.DOWN);
    protected static final VoxelShape UP_SHAPE = Block.makeCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 14.0D, 10.0D);
    private static final VoxelShape[] SHAPES = new VoxelShape[]{
        Block.makeCuboidShape(5.5D, 3.0D, 11.0D, 10.5D, 16.0D, 16.0D),
        Block.makeCuboidShape(5.5D, 3.0D, 0.0D, 10.5D, 16.0D, 5.0D),
        Block.makeCuboidShape(11.0D, 3.0D, 5.5D, 16.0D, 16.0D, 10.5D),
        Block.makeCuboidShape(0.0D, 3.0D, 5.5D, 5.0D, 16.0D, 10.5D)
    };

    public CandlestickBlock(Properties properties) {
        super(properties);
    }

    public static VoxelShape getShapeForState(BlockState state) {
        Direction dir = state.get(FACING);
        if (dir == Direction.UP) return UP_SHAPE;
        else return SHAPES[dir.ordinal() - 2];
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return getShapeForState(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.empty();
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        Direction direction = state.get(FACING);
        BlockPos blockpos = pos.offset(direction.getOpposite());
        BlockState blockstate = worldIn.getBlockState(blockpos);
        return blockstate.isSolidSide(worldIn, blockpos, direction);
    }

    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        Direction direction = state.get(FACING);
        if (direction == Direction.UP) return facing == Direction.DOWN && !this.isValidPosition(state, worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(state, facing, facingState, worldIn, currentPos, facingPos);
        else return facing.getOpposite() == state.get(FACING) && !state.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : state;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState blockstate = this.getDefaultState();
        IWorldReader iworldreader = context.getWorld();
        BlockPos blockpos = context.getPos();
        Direction[] adirection = context.getNearestLookingDirections();

        for(Direction direction : adirection) {
            if (direction != Direction.UP) {
                Direction direction1 = direction.getOpposite();
                blockstate = blockstate.with(FACING, direction1);
                if (blockstate.isValidPosition(iworldreader, blockpos)) {
                    return blockstate;
                }
            }
        }

        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World worldIn, BlockPos pos, Random rand) {
        Direction direction = state.get(FACING);
        double d0 = (double) pos.getX() + 0.5D;
        double d1 = (double) pos.getY() + 0.925D;
        double d2 = (double) pos.getZ() + 0.5D;
        if (direction != Direction.UP) {
            d0 -= 0.3 * direction.getXOffset();
            d1 += 0.125;
            d2 -= 0.3 * direction.getZOffset();
        }
        worldIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        worldIn.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.get(FACING) != Direction.UP ? state.with(FACING, rot.rotate(state.get(FACING))) : state;
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.get(FACING) != Direction.UP ? state.rotate(mirrorIn.toRotation(state.get(FACING))) : state;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
