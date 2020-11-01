package elucent.eidolon.ritual;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.network.CrystallizeEffectPacket;
import elucent.eidolon.network.Networking;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class InfuseRitual extends Ritual {
    public static final ResourceLocation SYMBOL = new ResourceLocation(Eidolon.MODID, "particle/infuse_ritual");
    ItemStack result;

    public InfuseRitual(ItemStack result, int r, int g, int b) {
        super(SYMBOL, ColorUtil.packColor(255, r, g, b));
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
