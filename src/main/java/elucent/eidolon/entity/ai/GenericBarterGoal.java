package elucent.eidolon.entity.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class GenericBarterGoal<E extends CreatureEntity> extends Goal {
    static Random rand = new Random();
    Predicate<ItemStack> valid;
    Function<ItemStack, ItemStack> result;
    int progress = 0, cooldown = 0, lastTick = 0;
    E entity;
    ItemStack backupHack = ItemStack.EMPTY;

    public GenericBarterGoal(E entity, Predicate<ItemStack> valid, Function<ItemStack, ItemStack> result) {
        this.entity = entity;
        this.valid = valid;
        this.result = result;
        this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.TARGET));
    }

    @Override
    public boolean isPreemptible() {
        return false;
    }

    @Override
    public void tick() {
        if (cooldown > 0) return;
        if (progress > 0 && !backupHack.isEmpty())
            entity.setHeldItem(Hand.MAIN_HAND, backupHack);

        entity.setAttackTarget(null);
        if (progress > 0) {
            progress --;
            entity.getNavigator().clearPath();
            if (progress == 0) {
                if (!entity.world.isRemote) {
                    entity.world.addEntity(new ItemEntity(entity.world, entity.getPosX(), entity.getPosY() + 0.1, entity.getPosZ(), result.apply(entity.getHeldItemMainhand().copy())));
                }
                entity.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
                cooldown = 600;
            }
        }
        else {
            List<ItemEntity> items = entity.world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(entity.getPosition().add(-8, -8, -8), entity.getPosition().add(8, 8, 8)), (item) -> valid.test(item.getItem()));
            ItemEntity nearest = items.stream().min(Comparator.comparingDouble(a -> a.getDistanceSq(entity))).get();
            if (nearest.getDistanceSq(entity) < 2.25) {
                progress = 100;
                entity.setHeldItem(Hand.MAIN_HAND, nearest.getItem());
                nearest.remove();
                entity.world.playSound(null, entity.getPosition(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.HOSTILE, 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            }
            entity.getNavigator().tryMoveToXYZ(nearest.getPosX(), nearest.getPosY(), nearest.getPosZ(), 1.0f);
        }

        if (!entity.getHeldItemMainhand().isEmpty())
            backupHack = entity.getHeldItemMainhand();
    }

    @Override
    public boolean shouldExecute() {
        if (-- cooldown > 0) return false;
        if (progress > 0 || entity.ticksExisted < lastTick + 20) return false;
        lastTick = entity.ticksExisted;
        List<ItemEntity> items = entity.world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(entity.getPosition().add(-8, -8, -8), entity.getPosition().add(8, 8, 8)), (item) -> valid.test(item.getItem()));
        return items.size() > 0;
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (progress > 0) return true;
        else { // walking towards item
            List<ItemEntity> items = entity.world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(entity.getPosition().add(-8, -8, -8), entity.getPosition().add(8, 8, 8)), (item) -> valid.test(item.getItem()));
            return items.size() > 0;
        }
    }
}
