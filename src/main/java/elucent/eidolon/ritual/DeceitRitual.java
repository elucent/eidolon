package elucent.eidolon.ritual;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.network.CrystallizeEffectPacket;
import elucent.eidolon.network.Networking;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ReturnToVillageGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.GossipManager;
import net.minecraft.world.World;

import java.util.List;

public class DeceitRitual extends Ritual {
    public static final ResourceLocation SYMBOL = new ResourceLocation(Eidolon.MODID, "particle/deceit_ritual");

    public DeceitRitual() {
        super(SYMBOL, ColorUtil.packColor(255, 64, 255, 96));
    }

    @Override
    public RitualResult tick(World world, BlockPos pos) {
        if (world.getGameTime() % 20 == 0) {
            List<VillagerEntity> villagers = world.getEntitiesWithinAABB(VillagerEntity.class, new AxisAlignedBB(pos).grow(48, 16, 48));
            for (VillagerEntity v : villagers) {
                if (world.rand.nextInt(120) == 0) v.getGossip().tick();
            }
        }
        return RitualResult.PASS;
    }
}
