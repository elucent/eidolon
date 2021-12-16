package elucent.eidolon.entity.ai;

import java.util.function.Function;
import java.util.function.Predicate;

import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.item.ItemStack;

public class WitchBarterGoal extends GenericBarterGoal<Witch> {
    public WitchBarterGoal(Witch entity, Predicate<ItemStack> valid, Function<ItemStack, ItemStack> result) {
        super(entity, valid, result);
    }

    @Override
    public void tick() {
        super.tick();
    }
}
