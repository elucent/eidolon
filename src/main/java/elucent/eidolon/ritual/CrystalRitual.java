package elucent.eidolon.ritual;

import java.util.List;

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

public class CrystalRitual extends Ritual {
    public static final ResourceLocation SYMBOL = new ResourceLocation(Eidolon.MODID, "particle/crystal_ritual");

    public CrystalRitual() {
        super(SYMBOL, ColorUtil.packColor(255, 247, 156, 220));
    }

    @Override
    public RitualResult start(World world, BlockPos pos) {
        List<LivingEntity> entities = world.getEntitiesWithinAABB(LivingEntity.class, getSearchBounds(pos), (e) -> e.isEntityUndead());
        for (LivingEntity e : entities) {
            e.attackEntityFrom(Registry.RITUAL_DAMAGE, e.getMaxHealth() * 1000);
            if (!world.isRemote) {
                Networking.sendToTracking(world, e.getPosition(), new CrystallizeEffectPacket(e.getPosition()));
                world.addEntity(new ItemEntity(world, e.getPosX(), e.getPosY(), e.getPosZ(), new ItemStack(Registry.SOUL_SHARD.get(), 1 + world.rand.nextInt(3))));
            }
        }
        return RitualResult.TERMINATE;
    }
}
