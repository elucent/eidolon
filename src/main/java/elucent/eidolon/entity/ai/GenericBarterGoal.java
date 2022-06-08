package elucent.eidolon.entity.ai;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.AABB;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;

public class GenericBarterGoal<E extends PathfinderMob> extends Goal {
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
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.TARGET));
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public void tick() {
        if (cooldown > 0) return;
        if (progress > 0 && !backupHack.isEmpty())
            entity.setItemInHand(InteractionHand.MAIN_HAND, backupHack);

        entity.setTarget(null);
        if (progress > 0) {
            progress --;
            entity.getNavigation().stop();
            if (progress == 0) {
                if (!entity.level.isClientSide) {
                    entity.level.addFreshEntity(new ItemEntity(entity.level, entity.getX(), entity.getY() + 0.1, entity.getZ(), result.apply(entity.getMainHandItem().copy())));
                }
                entity.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                cooldown = 600;
            }
        }
        else {
            List<ItemEntity> items = entity.level.getEntitiesOfClass(ItemEntity.class, new AABB(entity.blockPosition().offset(-8, -8, -8), entity.blockPosition().offset(8, 8, 8)), (item) -> valid.test(item.getItem()));
            ItemEntity nearest = items.stream().min(Comparator.comparingDouble(a -> a.distanceToSqr(entity))).get();
            if (nearest.distanceToSqr(entity) < 2.25) {
                progress = 100;
                entity.setItemInHand(InteractionHand.MAIN_HAND, nearest.getItem());
                nearest.remove();
                entity.level.playSound(null, entity.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.HOSTILE, 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            }
            entity.getNavigation().moveTo(nearest.getX(), nearest.getY(), nearest.getZ(), 1.0f);
        }

        if (!entity.getMainHandItem().isEmpty())
            backupHack = entity.getMainHandItem();
    }

    @Override
    public boolean canUse() {
        if (-- cooldown > 0) return false;
        if (progress > 0 || entity.tickCount < lastTick + 20) return false;
        lastTick = entity.tickCount;
        List<ItemEntity> items = entity.level.getEntitiesOfClass(ItemEntity.class, new AABB(entity.blockPosition().offset(-8, -8, -8), entity.blockPosition().offset(8, 8, 8)), (item) -> valid.test(item.getItem()));
        return items.size() > 0;
    }

    @Override
    public boolean canContinueToUse() {
        if (progress > 0) return true;
        else { // walking towards item
            List<ItemEntity> items = entity.level.getEntitiesOfClass(ItemEntity.class, new AABB(entity.blockPosition().offset(-8, -8, -8), entity.blockPosition().offset(8, 8, 8)), (item) -> valid.test(item.getItem()));
            return items.size() > 0;
        }
    }
}
