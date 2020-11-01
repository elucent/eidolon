package elucent.eidolon.ritual;

import elucent.eidolon.Eidolon;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SanguineRitual extends Ritual {
    public static final ResourceLocation SYMBOL = new ResourceLocation(Eidolon.MODID, "particle/sanguine_ritual");
    ItemStack result;

    public SanguineRitual(ItemStack result) {
        super(SYMBOL, ColorUtil.packColor(255, 255, 51, 85));
        this.result = result;
    }

    @Override
    public RitualResult start(World world, BlockPos pos) {
        if (!world.isRemote) {
            world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 2.5, pos.getZ() + 0.5, result.copy()));
        }
        return RitualResult.TERMINATE;
    }
}
