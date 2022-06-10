package elucent.eidolon.item.curio;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class EidolonCurio implements ICurio, ICapabilityProvider {
    private final ItemStack stack;

    public EidolonCurio(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public <ICurio> LazyOptional<ICurio> getCapability(Capability<ICurio> cap) {
        return cap == CuriosCapability.ITEM ? LazyOptional.of(() -> (ICurio)this) : LazyOptional.empty();
    }

    @Override
    public <ICurio> LazyOptional<ICurio> getCapability(Capability<ICurio> cap, Direction side) {
        return getCapability(cap);
    }

    @Override
    public ItemStack getStack() {
        return stack;
    }
}
