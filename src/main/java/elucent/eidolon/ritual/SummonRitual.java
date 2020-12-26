package elucent.eidolon.ritual;

import elucent.eidolon.Eidolon;
import elucent.eidolon.network.CrystallizeEffectPacket;
import elucent.eidolon.network.Networking;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SummonRitual extends Ritual {
    public static final ResourceLocation SYMBOL = new ResourceLocation(Eidolon.MODID, "particle/summon_ritual");

    EntityType entity;
    public SummonRitual(EntityType entity) {
        super(SYMBOL, ColorUtil.packColor(255, 121, 94, 255));
        this.entity = entity;
    }

    @Override
    public RitualResult start(World world, BlockPos pos) {
        if (!world.isRemote) {
            Networking.sendToTracking(world, pos, new CrystallizeEffectPacket(pos));
            Entity e = entity.create(world);
            e.setPosition(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5);
            world.addEntity(e);
        }
        return RitualResult.TERMINATE;
    }
}
