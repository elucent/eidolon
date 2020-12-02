package elucent.eidolon.capability;

import elucent.eidolon.spell.Sign;
import elucent.eidolon.spell.Signs;
import net.minecraft.nbt.*;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;

import java.util.Map;
import java.util.UUID;

public class KnowledgeStorage implements Capability.IStorage<IKnowledge> {
    @Override
    public INBT writeNBT(Capability<IKnowledge> capability, IKnowledge instance, Direction side) {
        ListNBT list = new ListNBT();
        for (Sign s : instance.getKnownSigns()) {
            list.add(StringNBT.valueOf(s.getRegistryName().toString()));
        }
        CompoundNBT wrapper = new CompoundNBT();
        wrapper.put("list", list);
        return wrapper;
    }

    @Override
    public void readNBT(Capability<IKnowledge> capability, IKnowledge instance, Direction side, INBT nbt) {
        ListNBT list = ((CompoundNBT)nbt).getList("list", Constants.NBT.TAG_STRING);
        instance.getKnownSigns().clear();
        for (int i = 0; i < list.size(); i ++) {
            ResourceLocation loc = new ResourceLocation(list.getString(i));
            Sign s = Signs.find(loc);
            if (s != null) instance.addSign(s);
        }
    }
}