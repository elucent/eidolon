package elucent.eidolon.ritual;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import elucent.eidolon.network.Networking;
import elucent.eidolon.network.RitualConsumePacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class FocusItemPresentRequirement extends FocusItemRequirement {
    public FocusItemPresentRequirement(ItemStack item) {
        super(item);
    }

    public FocusItemPresentRequirement(Item item) {
        super(item);
    }

    public FocusItemPresentRequirement(Block block) {
        super(block);
    }

    public FocusItemPresentRequirement(Tag<Item> item) {
        super(item);
    }

    public FocusItemPresentRequirement(Function<ItemStack, Boolean> item) {
    	super(item);
    }

    public void whenMet(Ritual ritual, Level world, BlockPos pos, RequirementInfo info) {
    	//
    }
}
