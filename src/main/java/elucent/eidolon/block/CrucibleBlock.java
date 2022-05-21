package elucent.eidolon.block;

import javax.annotation.Nullable;

import elucent.eidolon.tile.CrucibleTileEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CrucibleBlock extends BlockBase implements EntityBlock {
    public CrucibleBlock(Properties properties) {
        super(properties);
    }

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CrucibleTileEntity(pos, state);
	}

	@Override
    @Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
    	return new BlockEntityTicker<T>() {
			@Override
			public void tick(Level level, BlockPos pos, BlockState state, T tile) {
				((CrucibleTileEntity)tile).tick();
			}
    	};
    }
}
