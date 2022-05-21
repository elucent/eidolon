package elucent.eidolon.block;

import javax.annotation.Nullable;

import elucent.eidolon.gui.SoulEnchanterContainer;
import elucent.eidolon.tile.SoulEnchanterTileEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SoulEnchanterBlock extends HorizontalBlockBase implements EntityBlock {
    public SoulEnchanterBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            player.openMenu(new SimpleMenuProvider((id, inventory, p) -> {
                return new SoulEnchanterContainer(id, inventory, ContainerLevelAccess.create(world, pos));
            }, new TextComponent("")));
            player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
            return InteractionResult.CONSUME;
        }
    }

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SoulEnchanterTileEntity(pos, state);
	}

	@Override
    @Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
    	return new BlockEntityTicker<T>() {
			@Override
			public void tick(Level level, BlockPos pos, BlockState state, T tile) {
				((SoulEnchanterTileEntity)tile).tick();
			}
    	};
    }
}
