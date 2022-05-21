package elucent.eidolon.item;

import elucent.eidolon.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;

public class TongsItem extends ItemBase {
    public TongsItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        BlockState state = ctx.getLevel().getBlockState(ctx.getClickedPos());
        if (state.getBlock() instanceof PipeBlock && ctx.getClickedFace() != state.getValue(PipeBlock.IN)) {
            BlockState newState = state.setValue(PipeBlock.OUT, ctx.getClickedFace());
            BlockState off = ctx.getLevel().getBlockState(ctx.getClickedPos().relative(ctx.getClickedFace()));
            if (off.getBlock() == state.getBlock() &&
                off.getValue(PipeBlock.OUT) == ctx.getClickedFace().getOpposite())
                return InteractionResult.FAIL;
            if (off.getBlock() != state.getBlock() || off.getValue(PipeBlock.IN) != ctx.getClickedFace())
                newState = newState.setValue(PipeBlock.OUT_ATTACHED, false);
            ctx.getLevel().setBlock(ctx.getClickedPos(), newState, 3);
            return InteractionResult.SUCCESS;
        }
        return super.useOn(ctx);
    }
}
