package elucent.eidolon.capability;

import elucent.eidolon.spell.Sign;
import elucent.eidolon.spell.Signs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public class KnowledgeImpl implements IKnowledge {
    Set<Sign> signs = new HashSet<>();
    Set<ResourceLocation> facts = new HashSet<>();

    @Override
    public boolean knowsSign(Sign sign) {
        return signs.contains(sign);
    }

    @Override
    public void addSign(Sign sign) {
        signs.add(sign);
    }

    @Override
    public Set<Sign> getKnownSigns() {
        return signs;
    }

    @Override
    public boolean knowsFact(ResourceLocation fact) {
        return facts.contains(fact);
    }

    @Override
    public void addFact(ResourceLocation fact) {
        facts.add(fact);
    }

    @Override
    public Set<ResourceLocation> getKnownFacts() {
        return facts;
    }

    @Override
    public CompoundTag serializeNBT() {
        ListTag signs = new ListTag();
        for (Sign s : getKnownSigns()) {
            signs.add(StringTag.valueOf(s.getRegistryName().toString()));
        }
        ListTag facts = new ListTag();
        for (ResourceLocation s : getKnownFacts()) {
            facts.add(StringTag.valueOf(s.toString()));
        }
        CompoundTag wrapper = new CompoundTag();
        wrapper.put("signs", signs);
        wrapper.put("facts", facts);
        return wrapper;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        getKnownSigns().clear();
        getKnownFacts().clear();

        if (nbt.contains("signs")) {
            ListTag signs = nbt.getList("signs", Tag.TAG_STRING);
            for (int i = 0; i < signs.size(); i++) {
                ResourceLocation loc = new ResourceLocation(signs.getString(i));
                Sign s = Signs.find(loc);
                if (s != null) addSign(s);
            }
        }

        if (nbt.contains("facts")) {
            ListTag facts = nbt.getList("facts", Tag.TAG_STRING);
            for (int i = 0; i < facts.size(); i++) {
                addFact(new ResourceLocation(facts.getString(i)));
            }
        }
    }
}
