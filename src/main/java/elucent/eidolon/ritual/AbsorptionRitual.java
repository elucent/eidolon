package elucent.eidolon.ritual;

import java.util.List;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.item.IRechargeableWand;
import elucent.eidolon.item.SummoningStaffItem;
import elucent.eidolon.network.CrystallizeEffectPacket;
import elucent.eidolon.network.MagicBurstEffectPacket;
import elucent.eidolon.network.Networking;
import elucent.eidolon.network.RitualConsumePacket;
import elucent.eidolon.util.ColorUtil;
import elucent.eidolon.util.EntityUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class AbsorptionRitual extends Ritual {
    public static final ResourceLocation SYMBOL = new ResourceLocation(Eidolon.MODID, "particle/absorption_ritual");

    public AbsorptionRitual() {
        super(SYMBOL, ColorUtil.packColor(255, 123, 140, 70));
    }

    @Override
    public RitualResult start(Level world, BlockPos pos) {
        List<IRitualItemFocus> tiles = Ritual.getTilesWithinAABB(IRitualItemFocus.class, world, getSearchBounds(pos));
        BlockPos toRecharge = null;
        if (!tiles.isEmpty()) for (int i = 0; i < tiles.size(); i ++) {
            ItemStack stack = tiles.get(i).provide();
            if (stack.getItem() instanceof SummoningStaffItem s) {
            	toRecharge = ((BlockEntity)tiles.get(i)).getBlockPos();
            	break;
            }
        }
        
        List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, getSearchBounds(pos), (e) -> Eidolon.getTrueMobType(e) == MobType.UNDEAD && !EntityUtil.isEnthralled(e) && e.getHealth() <= e.getMaxHealth() / 5);
        ListTag entityTags = new ListTag();
        for (LivingEntity e : entities) {
        	e.setHealth(e.getMaxHealth());
            if (!world.isClientSide) {
                Networking.sendToTracking(world, e.blockPosition(), new MagicBurstEffectPacket(e.getX(), e.getY() + 0.1, e.getZ(),
                		ColorUtil.packColor(255, 61, 70, 35), ColorUtil.packColor(255, 36, 24, 41)));
                if (toRecharge != null) {
                	Networking.sendToTracking(world, toRecharge, new RitualConsumePacket(e.blockPosition().above(), toRecharge, getRed(), getGreen(), getBlue()));
                }
            }
            CompoundTag eTag = e.serializeNBT();
            entityTags.add(eTag);
            entityTags.add(eTag);
            entityTags.add(eTag);
            entityTags.add(eTag);
            entityTags.add(eTag);
            e.remove(RemovalReason.KILLED);
        }
        if (!tiles.isEmpty()) for (int i = 0; i < tiles.size(); i ++) {
            ItemStack stack = tiles.get(i).provide();
            if (stack.getItem() instanceof SummoningStaffItem s) {
            	tiles.get(i).replace(s.addCharges(stack, entityTags));
            	break;
            }
        }
        return RitualResult.TERMINATE;
    }
}
