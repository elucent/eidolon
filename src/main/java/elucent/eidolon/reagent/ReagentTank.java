package elucent.eidolon.reagent;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class ReagentTank {
    int capacity;
    ReagentStack contents;

    public ReagentTank(int capacity) {
        this.capacity = capacity;
        this.contents = new ReagentStack(ReagentRegistry.STEAM, 0);
    }

    public ReagentStack getContents() {
        return contents;
    }

    public int getCapacity() {
        return capacity;
    }

    public float getPressure() {
        return (float)contents.amount / capacity;
    }

    public CompoundTag write() {
        CompoundTag nbt = new CompoundTag();
        if (contents != null) nbt.put("contents", contents.write());
        nbt.putInt("capacity", capacity);
        return nbt;
    }

    public void read(CompoundTag nbt) {
        contents = nbt.contains("contents") ? new ReagentStack(nbt.getCompound("contents")) : new ReagentStack(ReagentRegistry.STEAM, 0);
        capacity = nbt.getInt("capacity");
    }

    public boolean canFill(ReagentStack stack) {
        return contents.isEmpty() || contents.reagent == stack.reagent;
    }

    public boolean fill(Level world, BlockPos pos, ReagentStack stack) {
        if (!contents.isEmpty() && contents.reagent != stack.reagent) return false;
        if (contents.isEmpty()) contents.reagent = stack.reagent;
        contents.amount += stack.amount;
        if (contents.amount > capacity * 1.25) {
            contents.reagent.worldEffect(world, pos, contents.amount);
            world.destroyBlock(pos, false);
        }
        return true;
    }
}
