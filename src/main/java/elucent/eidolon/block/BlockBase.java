package elucent.eidolon.block;

import elucent.eidolon.tile.TileEntityBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

public class BlockBase extends Block {
    VoxelShape shape = null;
    TileEntityType<? extends TileEntityBase> tileEntityType = null;

    public BlockBase(Properties properties) {
        super(properties);
    }

    public BlockBase setShape(VoxelShape shape) {
        this.shape = shape;
        return this;
    }

    public BlockBase setTile(TileEntityType<? extends TileEntityBase> type) {
        this.tileEntityType = type;
        return this;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return this.tileEntityType != null;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return tileEntityType.create();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
        return getRaytraceShape(state, world, pos);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
        return getRaytraceShape(state, world, pos);
    }

    @Override
    public VoxelShape getRaytraceShape(BlockState state, IBlockReader world, BlockPos pos) {
        return shape != null ? shape : VoxelShapes.fullCube();
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        breakBlock(state, world, pos);
        super.onBlockHarvested(world, pos, state, player);
    }

    @Override
    public void onBlockExploded(BlockState state, World world, BlockPos pos, Explosion explosion) {
        breakBlock(state, world, pos);
        super.onBlockExploded(state, world, pos, explosion);
    }

    public void breakBlock(BlockState state, IBlockReader world, BlockPos pos) {
        if (hasTileEntity(state)) {
            ((TileEntityBase)world.getTileEntity(pos)).onDestroyed(state, pos);
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult ray) {
        if (hasTileEntity(state)) {
            return ((TileEntityBase)world.getTileEntity(pos)).onActivated(state, pos, player, hand);
        }
        return super.onBlockActivated(state, world, pos, player, hand, ray);
    }
}
