package elucent.eidolon.capability;

import java.util.HashSet;
import java.util.Set;

import elucent.eidolon.spell.Rune;
import elucent.eidolon.spell.Runes;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.spell.Signs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

public class KnowledgeImpl implements IKnowledge, INBTSerializable<CompoundTag> {
    Set<Sign> signs = new HashSet<>();
    Set<ResourceLocation> facts = new HashSet<>();
    Set<ResourceLocation> researches = new HashSet<>();
    Set<Rune> runes = new HashSet<>();

    @Override
    public boolean knowsSign(Sign sign) {
        return signs.contains(sign);
    }

    @Override
    public void addSign(Sign sign) {
        signs.add(sign);
    }

	@Override
	public void removeSign(Sign sign) {
		signs.remove(sign);
	}

	@Override
	public void resetSigns() {
		signs.clear();
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
	public void removeFact(ResourceLocation fact) {
		facts.remove(fact);
	}

	@Override
	public void resetFacts() {
		facts.clear();
	}

    @Override
    public Set<ResourceLocation> getKnownFacts() {
        return facts;
    }

	@Override
	public boolean knowsResearch(ResourceLocation research) {
		return researches.contains(research);
	}

	@Override
	public void addResearch(ResourceLocation research) {
		researches.add(research);
	}

	@Override
	public void removeResearch(ResourceLocation research) {
		researches.remove(research);
	}

	@Override
	public void resetResearch() {
		researches.clear();
	}

	@Override
	public Set<ResourceLocation> getKnownResearches() {
		return researches;
	}

	@Override
	public boolean knowsRune(Rune rune) {
		return runes.contains(rune);
	}

	@Override
	public void addRune(Rune rune) {
		runes.add(rune);
	}

	@Override
	public void removeRune(Rune rune) {
		runes.remove(rune);
	}

	@Override
	public void resetRunes() {
		runes.clear();
	}

	@Override
	public Set<Rune> getKnownRunes() {
		return runes;
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
        ListTag researches = new ListTag();
        for (ResourceLocation s : getKnownResearches()) {
        	researches.add(StringTag.valueOf(s.toString()));
        }
        ListTag runes = new ListTag();
        for (Rune r : getKnownRunes()) {
            runes.add(StringTag.valueOf(r.getRegistryName().toString()));
        }
        CompoundTag wrapper = new CompoundTag();
        wrapper.put("signs", signs);
        wrapper.put("facts", facts);
        wrapper.put("researches", researches);
        wrapper.put("runes", runes);
        return wrapper;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
        getKnownSigns().clear();
        getKnownFacts().clear();
        getKnownResearches().clear();
        getKnownRunes().clear();

        if (((CompoundTag)nbt).contains("signs")) {
            ListTag signs = ((CompoundTag) nbt).getList("signs", Tag.TAG_STRING);
            for (int i = 0; i < signs.size(); i++) {
                ResourceLocation loc = new ResourceLocation(signs.getString(i));
                Sign s = Signs.find(loc);
                if (s != null) addSign(s);
            }
        }

        if (((CompoundTag)nbt).contains("facts")) {
            ListTag facts = ((CompoundTag) nbt).getList("facts", Tag.TAG_STRING);
            for (int i = 0; i < facts.size(); i++) {
                addFact(new ResourceLocation(facts.getString(i)));
            }
        }

        if (((CompoundTag)nbt).contains("researches")) {
            ListTag researches = ((CompoundTag) nbt).getList("researches", Tag.TAG_STRING);
            for (int i = 0; i < researches.size(); i++) {
                addResearch(new ResourceLocation(researches.getString(i)));
            }
        }

        if (((CompoundTag)nbt).contains("runes")) {
            ListTag runes = ((CompoundTag) nbt).getList("runes", Tag.TAG_STRING);
            for (int i = 0; i < runes.size(); i++) {
                ResourceLocation loc = new ResourceLocation(runes.getString(i));
                Rune r = Runes.find(loc);
                if (r != null) addRune(r);
            }
        }
	}
}
