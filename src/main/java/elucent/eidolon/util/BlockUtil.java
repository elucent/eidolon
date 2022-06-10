package elucent.eidolon.util;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author DustW
 **/
public class BlockUtil {
    public static int getHarvestLevel(BlockState state) {
        if (state.is(BlockTags.NEEDS_DIAMOND_TOOL))
        {
            return 3;
        }
        else if (state.is(BlockTags.NEEDS_IRON_TOOL))
        {
            return 2;
        }
        else if (state.is(BlockTags.NEEDS_STONE_TOOL))
        {
            return 1;
        }

        return 0;
    }
}
