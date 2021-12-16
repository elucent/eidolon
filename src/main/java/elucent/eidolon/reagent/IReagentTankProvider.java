package elucent.eidolon.reagent;

import net.minecraft.core.Direction;

public interface IReagentTankProvider {
    ReagentTank getTank();
    boolean isOutput(Direction direction);
    boolean isInput(Direction direction);
    void onContentsChanged();
}
