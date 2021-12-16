package elucent.eidolon.block;

import elucent.eidolon.gui.SoulEnchanterContainer;
import elucent.eidolon.tile.CrucibleTileEntity;
import elucent.eidolon.tile.EffigyTileEntity;
import elucent.eidolon.tile.HandTileEntity;
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
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class EffigyBlock extends HorizontalWaterloggableBlock implements EntityBlock {
    public EffigyBlock(Properties properties) {
        super(properties);
    }

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new EffigyTileEntity(pos, state);
	}
}
