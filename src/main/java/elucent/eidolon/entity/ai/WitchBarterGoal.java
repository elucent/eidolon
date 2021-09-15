package elucent.eidolon.entity.ai;

import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.item.ItemStack;

import java.util.function.Function;
import java.util.function.Predicate;

public class WitchBarterGoal extends GenericBarterGoal<WitchEntity> {
    public WitchBarterGoal(WitchEntity entity, Predicate<ItemStack> valid, Function<ItemStack, ItemStack> result) {
        super(entity, valid, result);
    }

    @Override
    public void tick() {
        super.tick();
    }
}
