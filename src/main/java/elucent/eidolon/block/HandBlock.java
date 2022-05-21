package elucent.eidolon.block;

import elucent.eidolon.tile.HandTileEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;

public class HandBlock extends HorizontalWaterloggableBlock implements EntityBlock {
    public HandBlock(Properties properties) {
        super(properties);
    }

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new HandTileEntity(pos, state);
	}
}
