package elucent.eidolon.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class NecroticFocusBlock extends HorizontalBlockBase {
    VoxelShape SOUTH = VoxelShapes.create(0, 0, 0, 1, 1, 0.5),
               NORTH = VoxelShapes.create(0, 0, 0.5, 1, 1, 1),
               WEST = VoxelShapes.create(0.5, 0, 0, 1, 1, 1),
               EAST = VoxelShapes.create(0, 0, 0, 0.5, 1, 1);

    public NecroticFocusBlock(Properties properties) {
        super(properties);
    }

    VoxelShape shapeForState(BlockState state) {
        switch (state.get(HORIZONTAL_FACING)) {
            case NORTH:
                return NORTH;
            case SOUTH:
                return SOUTH;
            case WEST:
                return WEST;
            case EAST:
            default:
                return EAST;
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
        return shapeForState(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
        return shapeForState(state);
    }

    @Override
    public VoxelShape getRaytraceShape(BlockState state, IBlockReader world, BlockPos pos) {
        return shapeForState(state);
    }
}
