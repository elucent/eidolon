package elucent.eidolon.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import elucent.eidolon.Registry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.RedstoneSide;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;
import java.util.Random;

public class EnchantedAshBlock extends BlockBase {
    public static final EnumProperty<RedstoneSide> NORTH = BlockStateProperties.REDSTONE_NORTH;
    public static final EnumProperty<RedstoneSide> EAST = BlockStateProperties.REDSTONE_EAST;
    public static final EnumProperty<RedstoneSide> SOUTH = BlockStateProperties.REDSTONE_SOUTH;
    public static final EnumProperty<RedstoneSide> WEST = BlockStateProperties.REDSTONE_WEST;
    public static final Map<Direction, EnumProperty<RedstoneSide>> FACING_PROPERTY_MAP = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.SOUTH, SOUTH, Direction.WEST, WEST));
    private static final VoxelShape BARRIER_SHAPE = VoxelShapes.create(0, -4, 0, 1, 5, 1);
    private static final VoxelShape BASE_SHAPE = Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D);
    private static final Map<Direction, VoxelShape> SIDE_TO_SHAPE = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.makeCuboidShape(3.0D, 0.0D, 0.0D, 13.0D, 1.0D, 13.0D), Direction.SOUTH, Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 16.0D), Direction.EAST, Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 16.0D, 1.0D, 13.0D), Direction.WEST, Block.makeCuboidShape(0.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D)));
    private static final Map<Direction, VoxelShape> SIDE_TO_ASCENDING_SHAPE = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, VoxelShapes.or(SIDE_TO_SHAPE.get(Direction.NORTH), Block.makeCuboidShape(3.0D, 0.0D, 0.0D, 13.0D, 16.0D, 1.0D)), Direction.SOUTH, VoxelShapes.or(SIDE_TO_SHAPE.get(Direction.SOUTH), Block.makeCuboidShape(3.0D, 0.0D, 15.0D, 13.0D, 16.0D, 16.0D)), Direction.EAST, VoxelShapes.or(SIDE_TO_SHAPE.get(Direction.EAST), Block.makeCuboidShape(15.0D, 0.0D, 3.0D, 16.0D, 16.0D, 13.0D)), Direction.WEST, VoxelShapes.or(SIDE_TO_SHAPE.get(Direction.WEST), Block.makeCuboidShape(0.0D, 0.0D, 3.0D, 1.0D, 16.0D, 13.0D))));
    private final Map<BlockState, VoxelShape> stateToShapeMap = Maps.newHashMap();
    private final BlockState sideBaseState;

    public EnchantedAshBlock(Properties properties) {
        super(properties);

        this.setDefaultState(this.stateContainer.getBaseState().with(NORTH, RedstoneSide.NONE).with(EAST, RedstoneSide.NONE).with(SOUTH, RedstoneSide.NONE).with(WEST, RedstoneSide.NONE));
        this.sideBaseState = this.getDefaultState().with(NORTH, RedstoneSide.SIDE).with(EAST, RedstoneSide.SIDE).with(SOUTH, RedstoneSide.SIDE).with(WEST, RedstoneSide.SIDE);

        for(BlockState blockstate : this.getStateContainer().getValidStates()) {
            this.stateToShapeMap.put(blockstate, this.getShapeForState(blockstate));
        }
    }

    private VoxelShape getShapeForState(BlockState state) {
        VoxelShape voxelshape = BASE_SHAPE;

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            RedstoneSide redstoneside = state.get(FACING_PROPERTY_MAP.get(direction));
            if (redstoneside == RedstoneSide.SIDE) {
                voxelshape = VoxelShapes.or(voxelshape, SIDE_TO_SHAPE.get(direction));
            } else if (redstoneside == RedstoneSide.UP) {
                voxelshape = VoxelShapes.or(voxelshape, SIDE_TO_ASCENDING_SHAPE.get(direction));
            }
        }

        return voxelshape;
    }

    private static boolean areAllSidesInvalid(BlockState state) {
        return !state.get(NORTH).isValidSide() && !state.get(SOUTH).isValidSide() && !state.get(EAST).isValidSide() && !state.get(WEST).isValidSide();
    }

    private static boolean areAllSidesValid(BlockState state) {
        return state.get(NORTH).isValidSide() && state.get(SOUTH).isValidSide() && state.get(EAST).isValidSide() && state.get(WEST).isValidSide();
    }

    private BlockState recalculateFacingState(IBlockReader reader, BlockState state, BlockPos pos) {
        boolean flag = !reader.getBlockState(pos.up()).isNormalCube(reader, pos);

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            if (!state.get(FACING_PROPERTY_MAP.get(direction)).isValidSide()) {
                RedstoneSide redstoneside = this.recalculateSide(reader, pos, direction, flag);
                state = state.with(FACING_PROPERTY_MAP.get(direction), redstoneside);
            }
        }

        return state;
    }

    private RedstoneSide getSide(IBlockReader worldIn, BlockPos pos, Direction face) {
        RedstoneWireBlock r;
        return this.recalculateSide(worldIn, pos, face, !worldIn.getBlockState(pos.up()).isNormalCube(worldIn, pos));
    }

    public void updateDiagonalNeighbors(BlockState state, IWorld worldIn, BlockPos pos, int flags, int recursionLeft) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            RedstoneSide redstoneside = state.get(FACING_PROPERTY_MAP.get(direction));
            if (redstoneside != RedstoneSide.NONE && !worldIn.getBlockState(blockpos$mutable.setAndMove(pos, direction)).matchesBlock(this)) {
                blockpos$mutable.move(Direction.DOWN);
                BlockState blockstate = worldIn.getBlockState(blockpos$mutable);
                if (!blockstate.matchesBlock(Blocks.OBSERVER)) {
                    BlockPos blockpos = blockpos$mutable.offset(direction.getOpposite());
                    BlockState blockstate1 = blockstate.updatePostPlacement(direction.getOpposite(), worldIn.getBlockState(blockpos), worldIn, blockpos$mutable, blockpos);
                    replaceBlockState(blockstate, blockstate1, worldIn, blockpos$mutable, flags, recursionLeft);
                }

                blockpos$mutable.setAndMove(pos, direction).move(Direction.UP);
                BlockState blockstate3 = worldIn.getBlockState(blockpos$mutable);
                if (!blockstate3.matchesBlock(Blocks.OBSERVER)) {
                    BlockPos blockpos1 = blockpos$mutable.offset(direction.getOpposite());
                    BlockState blockstate2 = blockstate3.updatePostPlacement(direction.getOpposite(), worldIn.getBlockState(blockpos1), worldIn, blockpos$mutable, blockpos1);
                    replaceBlockState(blockstate3, blockstate2, worldIn, blockpos$mutable, flags, recursionLeft);
                }
            }
        }

    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (facing == Direction.DOWN) {
            return stateIn;
        } else if (facing == Direction.UP) {
            return this.getUpdatedState(worldIn, stateIn, currentPos);
        } else {
            RedstoneSide redstoneside = this.getSide(worldIn, currentPos, facing);
            return redstoneside.isValidSide() == stateIn.get(FACING_PROPERTY_MAP.get(facing)).isValidSide() && !areAllSidesValid(stateIn) ? stateIn.with(FACING_PROPERTY_MAP.get(facing), redstoneside) : this.getUpdatedState(worldIn, this.sideBaseState.with(FACING_PROPERTY_MAP.get(facing), redstoneside), currentPos);
        }
    }

    private RedstoneSide recalculateSide(IBlockReader reader, BlockPos pos, Direction direction, boolean nonNormalCubeAbove) {
        BlockPos blockpos = pos.offset(direction);
        BlockState blockstate = reader.getBlockState(blockpos);
        if (nonNormalCubeAbove) {
            boolean flag = this.canPlaceOnTopOf(reader, blockpos, blockstate);
            if (flag && canConnectTo(reader.getBlockState(blockpos.up()), reader, blockpos.up(), null) ) {
                if (blockstate.isSolidSide(reader, blockpos, direction.getOpposite())) {
                    return RedstoneSide.UP;
                }

                return RedstoneSide.SIDE;
            }
        }

        return !canConnectTo(blockstate, reader, blockpos, direction) && (blockstate.isNormalCube(reader, blockpos) || !canConnectTo(reader.getBlockState(blockpos.down()), reader, blockpos.down(), null)) ? RedstoneSide.NONE : RedstoneSide.SIDE;
    }

    private boolean canPlaceOnTopOf(IBlockReader reader, BlockPos pos, BlockState state) {
        return state.isSolidSide(reader, pos, Direction.UP);
    }

    private BlockState getUpdatedState(IBlockReader reader, BlockState state, BlockPos pos) {
        boolean flag = areAllSidesInvalid(state);
        state = this.recalculateFacingState(reader, this.getDefaultState(), pos);
        if (flag && areAllSidesInvalid(state)) {
            return state;
        } else {
            boolean flag1 = state.get(NORTH).isValidSide();
            boolean flag2 = state.get(SOUTH).isValidSide();
            boolean flag3 = state.get(EAST).isValidSide();
            boolean flag4 = state.get(WEST).isValidSide();
            boolean flag5 = !flag1 && !flag2;
            boolean flag6 = !flag3 && !flag4;
            if (!flag4 && flag5) {
                state = state.with(WEST, RedstoneSide.SIDE);
            }

            if (!flag3 && flag5) {
                state = state.with(EAST, RedstoneSide.SIDE);
            }

            if (!flag1 && flag6) {
                state = state.with(NORTH, RedstoneSide.SIDE);
            }

            if (!flag2 && flag6) {
                state = state.with(SOUTH, RedstoneSide.SIDE);
            }

            return state;
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return this.stateToShapeMap.get(state);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getUpdatedState(context.getWorld(), this.sideBaseState, context.getPos());
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.matchesBlock(state.getBlock()) && !worldIn.isRemote) {
            for(Direction direction : Direction.Plane.VERTICAL) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(direction), this);
            }

            this.updateNeighboursStateChange(worldIn, pos);
        }
    }

    @Override
    public boolean collisionExtendsVertically(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
        return entity instanceof LivingEntity && ((LivingEntity)entity).isEntityUndead();
    }

    boolean isBlocked(Entity entity) {
        if (entity == null) return false;
        if (entity instanceof LivingEntity) {
            LivingEntity living = (LivingEntity)entity;
            if (living.isEntityUndead()) return true;
        }
        return entity.getPassengers().stream().anyMatch((e) -> e instanceof LivingEntity && ((LivingEntity) e).isEntityUndead());
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
        return isBlocked(ctx.getEntity()) ? BARRIER_SHAPE : super.getCollisionShape(state, world, pos, ctx);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.down();
        BlockState blockstate = worldIn.getBlockState(blockpos);
        return this.canPlaceOnTopOf(worldIn, blockpos, blockstate);
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving && !state.matchesBlock(newState.getBlock())) {
            super.onReplaced(state, worldIn, pos, newState, isMoving);
            if (!worldIn.isRemote) {
                for(Direction direction : Direction.values()) {
                    worldIn.notifyNeighborsOfStateChange(pos.offset(direction), this);
                }

                this.updateNeighboursStateChange(worldIn, pos);
            }
        }
    }

    private void updateChangedConnections(World world, BlockPos pos, BlockState prevState, BlockState newState) {
        for(Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos blockpos = pos.offset(direction);
            if (prevState.get(FACING_PROPERTY_MAP.get(direction)).isValidSide() != newState.get(FACING_PROPERTY_MAP.get(direction)).isValidSide() && world.getBlockState(blockpos).isNormalCube(world, blockpos)) {
                world.notifyNeighborsOfStateExcept(blockpos, newState.getBlock(), direction.getOpposite());
            }
        }

    }

    private void notifyWireNeighborsOfStateChange(World worldIn, BlockPos pos) {
        if (worldIn.getBlockState(pos).matchesBlock(this)) {
            worldIn.notifyNeighborsOfStateChange(pos, this);

            for(Direction direction : Direction.values()) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(direction), this);
            }
        }
    }

    private void updateNeighboursStateChange(World world, BlockPos pos) {
        for(Direction direction : Direction.Plane.HORIZONTAL) {
            this.notifyWireNeighborsOfStateChange(world, pos.offset(direction));
        }

        for(Direction direction1 : Direction.Plane.HORIZONTAL) {
            BlockPos blockpos = pos.offset(direction1);
            if (world.getBlockState(blockpos).isNormalCube(world, blockpos)) {
                this.notifyWireNeighborsOfStateChange(world, blockpos.up());
            } else {
                this.notifyWireNeighborsOfStateChange(world, blockpos.down());
            }
        }

    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isRemote) {
            if (!state.isValidPosition(worldIn, pos)) {
                spawnDrops(state, worldIn, pos);
                worldIn.removeBlock(pos, false);
            }
        }
    }

    protected static boolean canConnectTo(BlockState blockState, IBlockReader world, BlockPos pos, Direction side) {
        return blockState.matchesBlock(Registry.ENCHANTED_ASH.get());
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        return ActionResultType.PASS;
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        //
    }

    @Override
    public boolean canProvidePower(BlockState state) {
        return false;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST);
    }
}
