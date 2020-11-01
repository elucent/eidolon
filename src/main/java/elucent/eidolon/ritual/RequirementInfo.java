package elucent.eidolon.ritual;

import net.minecraft.util.math.BlockPos;

public class RequirementInfo {
    public static final RequirementInfo
        TRUE = new RequirementInfo(true),
        FALSE = new RequirementInfo(false);

    boolean met = false;
    BlockPos pos;

    public RequirementInfo(boolean met) {
        this.met = met;
    }

    public RequirementInfo(boolean met, BlockPos pos) {
        this.met = met;
        this.pos = pos;
    }

    public boolean isMet() {
        return met;
    }

    public BlockPos getPos() {
        return pos;
    }
}
