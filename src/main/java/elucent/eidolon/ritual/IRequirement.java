package elucent.eidolon.ritual;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IRequirement {
    RequirementInfo isMet(Ritual ritual, World world, BlockPos pos);
    default void whenMet(Ritual ritual, World world, BlockPos pos, RequirementInfo info) {
        //
    }
}
