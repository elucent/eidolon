package elucent.eidolon.block;

import elucent.eidolon.gui.SoulEnchanterContainer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;

import net.minecraft.world.level.block.state.BlockBehaviour;

public class SoulEnchanterBlock extends HorizontalBlockBase {
    public SoulEnchanterBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            player.openMenu(new SimpleNamedContainerProvider((id, inventory, p) -> {
                return new SoulEnchanterContainer(id, inventory, IWorldPosCallable.create(world, pos));
            }, new TextComponent("")));
            player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
            return InteractionResult.CONSUME;
        }
    }
}
