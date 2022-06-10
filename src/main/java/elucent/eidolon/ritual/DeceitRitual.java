package elucent.eidolon.ritual;

import elucent.eidolon.Eidolon;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class DeceitRitual extends Ritual {
    public static final ResourceLocation SYMBOL = new ResourceLocation(Eidolon.MODID, "particle/deceit_ritual");

    public DeceitRitual() {
        super(SYMBOL, ColorUtil.packColor(255, 64, 255, 96));
    }

    @Override
    public RitualResult tick(Level world, BlockPos pos) {
        if (world.getGameTime() % 20 == 0) {
            List<Villager> villagers = world.getEntitiesOfClass(Villager.class, new AABB(pos).inflate(48, 16, 48));
            for (var v : villagers) {
                if (world.random.nextInt(120) == 0) v.getGossips().decay();
            }
        }
        return RitualResult.PASS;
    }
}
