package elucent.eidolon.capability;

import elucent.eidolon.spell.Sign;
import elucent.eidolon.spell.Signs;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;

public class KnowledgeStorage implements Capability.IStorage<IKnowledge> {
    @Override
    public INBT writeNBT(Capability<IKnowledge> capability, IKnowledge instance, Direction side) {
        ListNBT signs = new ListNBT();
        for (Sign s : instance.getKnownSigns()) {
            signs.add(StringNBT.valueOf(s.getRegistryName().toString()));
        }
        ListNBT facts = new ListNBT();
        for (ResourceLocation s : instance.getKnownFacts()) {
            facts.add(StringNBT.valueOf(s.toString()));
        }
        CompoundNBT wrapper = new CompoundNBT();
        wrapper.put("signs", signs);
        wrapper.put("facts", facts);
        return wrapper;
    }

    @Override
    public void readNBT(Capability<IKnowledge> capability, IKnowledge instance, Direction side, INBT nbt) {
        instance.getKnownSigns().clear();
        instance.getKnownFacts().clear();

        if (((CompoundNBT)nbt).contains("signs")) {
            ListNBT signs = ((CompoundNBT) nbt).getList("signs", Constants.NBT.TAG_STRING);
            for (int i = 0; i < signs.size(); i++) {
                ResourceLocation loc = new ResourceLocation(signs.getString(i));
                Sign s = Signs.find(loc);
                if (s != null) instance.addSign(s);
            }
        }

        if (((CompoundNBT)nbt).contains("facts")) {
            ListNBT facts = ((CompoundNBT) nbt).getList("facts", Constants.NBT.TAG_STRING);
            for (int i = 0; i < facts.size(); i++) {
                instance.addFact(new ResourceLocation(facts.getString(i)));
            }
        }
    }
}