package elucent.eidolon.entity.ai;

import java.util.function.Function;
import java.util.function.Predicate;

import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;

public class PriestBarterGoal extends GenericBarterGoal<Villager> {
    public PriestBarterGoal(Villager entity, Predicate<ItemStack> valid, Function<ItemStack, ItemStack> result) {
        super(entity, valid, result);
    }

    @Override
    public boolean canUse() {
        if (entity.getVillagerData().getProfession() != VillagerProfession.CLERIC) return false;
        return super.canUse();
    }

    @Override
    public void tick() {
        super.tick();
    }
}
