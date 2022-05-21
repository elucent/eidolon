package elucent.eidolon.block;

import javax.annotation.Nullable;

import elucent.eidolon.gui.ResearchTableContainer;
import elucent.eidolon.gui.WoodenBrewingStandContainer;
import elucent.eidolon.gui.WorktableContainer;
import elucent.eidolon.tile.ResearchTableTileEntity;
import elucent.eidolon.tile.WoodenStandTileEntity;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class ResearchTableBlock extends HorizontalBlockBase implements EntityBlock {
    public ResearchTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof ResearchTableTileEntity) {
                NetworkHooks.openGui((ServerPlayer)player, new SimpleMenuProvider((id, inventory, p) -> {
                    return new ResearchTableContainer(id, inventory, ((ResearchTableTileEntity)tileentity), ((ResearchTableTileEntity)tileentity).dataAccess);
                }, ((ResearchTableTileEntity)tileentity).getDisplayName()), pos);
            }

            return InteractionResult.CONSUME;
        }
    }

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ResearchTableTileEntity(pos, state);
	}

	@Override
    @Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
    	return new BlockEntityTicker<T>() {
			@Override
			public void tick(Level level, BlockPos pos, BlockState state, T tile) {
				((ResearchTableTileEntity)tile).tick();
			}
    	};
    }
}
