package elucent.eidolon.ritual;

import java.util.List;
import java.util.function.Predicate;

import elucent.eidolon.Eidolon;
import elucent.eidolon.item.IRechargeableWand;
import elucent.eidolon.network.Networking;
import elucent.eidolon.network.RitualConsumePacket;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class RechargingRitual extends Ritual {
    public static final ResourceLocation SYMBOL = new ResourceLocation(Eidolon.MODID, "particle/recharge_ritual");
    
    public RechargingRitual() {
        super(SYMBOL, ColorUtil.packColor(255, 220, 180, 701));
    }

    @Override
    public RitualResult start(Level world, BlockPos pos) {
        List<IRitualItemFocus> tiles = Ritual.getTilesWithinAABB(IRitualItemFocus.class, world, getSearchBounds(pos));
        if (!tiles.isEmpty()) for (int i = 0; i < tiles.size(); i ++) {
            ItemStack stack = tiles.get(i).provide();
            if (stack.getItem() instanceof IRechargeableWand) {
            	tiles.get(i).replace(((IRechargeableWand)stack.getItem()).recharge(stack));
                if (!world.isClientSide && tiles.get(i) instanceof BlockEntity b) {
                    Networking.sendToTracking(world, b.getBlockPos(), new RitualConsumePacket(pos.above(2), b.getBlockPos(), getRed(), getGreen(), getBlue()));
                }
            	break;
            }
        }
        return RitualResult.TERMINATE;
    }
}
