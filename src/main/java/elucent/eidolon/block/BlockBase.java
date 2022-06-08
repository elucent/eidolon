package elucent.eidolon.block;

import elucent.eidolon.tile.TileEntityBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.Explosion;

import net.minecraft.world.level.block.state.BlockBehaviour;

public class BlockBase extends Block {
    VoxelShape shape = null;
    BlockEntityType<? extends BlockEntity> tileEntityType = null;

    public BlockBase(Properties properties) {
        super(properties);
    }

    public BlockBase setShape(VoxelShape shape) {
        this.shape = shape;
        return this;
    }

    public BlockBase setTile(BlockEntityType<? extends BlockEntity> type) {
        this.tileEntityType = type;
        return this;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return this.tileEntityType != null;
    }

    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
        return tileEntityType.create();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return getInteractionShape(state, world, pos);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return getInteractionShape(state, world, pos);
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter world, BlockPos pos) {
        return shape != null ? shape : Shapes.block();
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        breakBlock(state, world, pos);
        super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public void onBlockExploded(BlockState state, Level world, BlockPos pos, Explosion explosion) {
        breakBlock(state, world, pos);
        super.onBlockExploded(state, world, pos, explosion);
    }

    public void breakBlock(BlockState state, BlockGetter world, BlockPos pos) {
        if (hasTileEntity(state)) {
            BlockEntity te = world.getBlockEntity(pos);
            if (te instanceof TileEntityBase) {
                ((TileEntityBase) world.getBlockEntity(pos)).onDestroyed(state, pos);
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        if (hasTileEntity(state)) {
            BlockEntity te = world.getBlockEntity(pos);
            if (te instanceof TileEntityBase) {
                return ((TileEntityBase) world.getBlockEntity(pos)).onActivated(state, pos, player, hand);
            }
        }
        return super.use(state, world, pos, player, hand, ray);
    }
}
