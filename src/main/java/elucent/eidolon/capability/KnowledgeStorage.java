package elucent.eidolon.capability;

import elucent.eidolon.spell.Sign;
import elucent.eidolon.spell.Signs;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;

public class KnowledgeStorage implements Capability.IStorage<IKnowledge> {
    @Override
    public INBT writeNBT(Capability<IKnowledge> capability, IKnowledge instance, Direction side) {
        ListTag signs = new ListTag();
        for (Sign s : instance.getKnownSigns()) {
            signs.add(StringTag.valueOf(s.getRegistryName().toString()));
        }
        ListTag facts = new ListTag();
        for (ResourceLocation s : instance.getKnownFacts()) {
            facts.add(StringTag.valueOf(s.toString()));
        }
        CompoundTag wrapper = new CompoundTag();
        wrapper.put("signs", signs);
        wrapper.put("facts", facts);
        return wrapper;
    }

    @Override
    public void readNBT(Capability<IKnowledge> capability, IKnowledge instance, Direction side, INBT nbt) {
        instance.getKnownSigns().clear();
        instance.getKnownFacts().clear();

        if (((CompoundTag)nbt).contains("signs")) {
            ListTag signs = ((CompoundTag) nbt).getList("signs", Constants.NBT.TAG_STRING);
            for (int i = 0; i < signs.size(); i++) {
                ResourceLocation loc = new ResourceLocation(signs.getString(i));
                Sign s = Signs.find(loc);
                if (s != null) instance.addSign(s);
            }
        }

        if (((CompoundTag)nbt).contains("facts")) {
            ListTag facts = ((CompoundTag) nbt).getList("facts", Constants.NBT.TAG_STRING);
            for (int i = 0; i < facts.size(); i++) {
                instance.addFact(new ResourceLocation(facts.getString(i)));
            }
        }
    }
}