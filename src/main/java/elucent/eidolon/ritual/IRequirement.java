package elucent.eidolon.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface IRequirement {
    RequirementInfo isMet(Ritual ritual, Level world, BlockPos pos);
    default void whenMet(Ritual ritual, Level world, BlockPos pos, RequirementInfo info) {
        //
    }
}
