package elucent.eidolon.ritual;

import java.util.List;

import elucent.eidolon.network.Networking;
import elucent.eidolon.network.RitualConsumePacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class FocusItemRequirement implements IRequirement {
    Object match;

    public FocusItemRequirement(ItemStack item) {
        this.match = item;
    }

    public FocusItemRequirement(Item item) {
        this.match = item;
    }

    public FocusItemRequirement(Block block) {
        this.match = Item.byBlock(block);
    }

    public FocusItemRequirement(Tag<Item> item) {
        this.match = item;
    }

    @Override
    public RequirementInfo isMet(Ritual ritual, Level world, BlockPos pos) {
        List<IRitualItemProvider> tiles = Ritual.getTilesWithinAABB(IRitualItemProvider.class, world, ritual.getSearchBounds(pos));
        System.out.println(tiles.size());
        if (tiles.isEmpty()) return RequirementInfo.FALSE;
        for (int i = 0; i < tiles.size(); i ++) {
            ItemStack stack = tiles.get(i).provide();

            if (match instanceof ItemStack && ItemStack.matches((ItemStack)match, stack)) {
                return new RequirementInfo(true, ((BlockEntity)tiles.get(i)).getBlockPos());
            }
            else if (match instanceof Item && stack.getItem() == (Item)match) {
                return new RequirementInfo(true, ((BlockEntity)tiles.get(i)).getBlockPos());
            }
            else if (match instanceof Tag && ((Tag<Item>)match).contains(stack.getItem())) {
                return new RequirementInfo(true, ((BlockEntity)tiles.get(i)).getBlockPos());
            }
        }

        return RequirementInfo.FALSE;
    }

    public void whenMet(Ritual ritual, Level world, BlockPos pos, RequirementInfo info) {
        ((IRitualItemProvider)world.getBlockEntity(info.getPos())).take();
        if (!world.isClientSide) {
            Networking.sendToTracking(world, pos.above(2), new RitualConsumePacket(info.getPos(), pos.above(2), ritual.getRed(), ritual.getGreen(), ritual.getBlue()));
        }
    }
}
