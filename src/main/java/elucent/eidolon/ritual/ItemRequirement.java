package elucent.eidolon.ritual;

import elucent.eidolon.network.Networking;
import elucent.eidolon.network.RitualConsumePacket;
import elucent.eidolon.ritual.Ritual.SetupResult;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.tags.Tag;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;

import java.util.List;

public class ItemRequirement implements IRequirement {
    Object match;

    public ItemRequirement(ItemStack item) {
        this.match = item;
    }

    public ItemRequirement(Item item) {
        this.match = item;
    }

    public ItemRequirement(Block block) {
        this.match = Item.getItemFromBlock(block);
    }

    public ItemRequirement(ITag<Item> item) {
        this.match = item;
    }

    @Override
    public RequirementInfo isMet(Ritual ritual, World world, BlockPos pos) {
        List<IRitualItemProvider> tiles = Ritual.getTilesWithinAABB(IRitualItemProvider.class, world, ritual.getSearchBounds(pos));
        if (tiles.isEmpty()) return RequirementInfo.FALSE;
        for (int i = 0; i < tiles.size(); i ++) {
            ItemStack stack = tiles.get(i).provide();

            if (match instanceof ItemStack && ItemStack.areItemStacksEqual((ItemStack)match, stack)) {
                return new RequirementInfo(true, ((TileEntity)tiles.get(i)).getPos());
            }
            else if (match instanceof Item && stack.getItem() == (Item)match) {
                return new RequirementInfo(true, ((TileEntity)tiles.get(i)).getPos());
            }
            else if (match instanceof ITag && ((ITag<Item>)match).contains(stack.getItem())) {
                return new RequirementInfo(true, ((TileEntity)tiles.get(i)).getPos());
            }
        }

        return RequirementInfo.FALSE;
    }

    public void whenMet(Ritual ritual, World world, BlockPos pos, RequirementInfo info) {
        ((IRitualItemProvider)world.getTileEntity(info.getPos())).take();
        if (!world.isRemote) {
            Networking.sendToTracking(world, pos.up(2), new RitualConsumePacket(info.getPos(), pos.up(2), ritual.getRed(), ritual.getGreen(), ritual.getBlue()));
        }
    }
}
