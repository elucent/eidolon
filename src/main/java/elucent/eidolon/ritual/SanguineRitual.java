package elucent.eidolon.ritual;

import elucent.eidolon.Eidolon;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class SanguineRitual extends Ritual {
    public static final ResourceLocation SYMBOL = new ResourceLocation(Eidolon.MODID, "particle/sanguine_ritual");
    ItemStack result;

    public SanguineRitual(ItemStack result) {
        super(SYMBOL, ColorUtil.packColor(255, 255, 51, 85));
        this.result = result;
    }

    @Override
    public RitualResult start(Level world, BlockPos pos) {
        if (!world.isClientSide) {
            world.addFreshEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 2.5, pos.getZ() + 0.5, result.copy()));
        }
        return RitualResult.TERMINATE;
    }
}
