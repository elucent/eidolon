package elucent.eidolon.reagent;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class ReagentStack {
    public Reagent reagent;
    public int amount;

    public ReagentStack(Reagent reagent, int amount) {
        this.reagent = reagent;
        this.amount = amount;
    }

    public ReagentStack(CompoundTag nbt) {
        this.reagent = ReagentRegistry.find(new ResourceLocation(nbt.getString("reagent")));
        this.amount = nbt.getInt("amount");
    }

    public boolean isEmpty() {
        return amount == 0;
    }

    public CompoundTag write() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("reagent", reagent.getRegistryName().toString());
        nbt.putInt("amount", amount);
        return nbt;
    }
}
