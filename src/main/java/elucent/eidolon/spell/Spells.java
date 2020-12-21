package elucent.eidolon.spell;

import elucent.eidolon.Eidolon;
import elucent.eidolon.deity.Deities;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Spells {
    static List<Spell> spells = new ArrayList<>();
    static Map<ResourceLocation, Spell> spellMap = new HashMap<>();

    public static Spell find(ResourceLocation loc) {
        return spellMap.getOrDefault(loc, null);
    }

    public static Spell find(List<Sign> signs) {
        for (Spell spell : spells) if (spell.matches(signs)) return spell;
        return null;
    }

    public static Spell register(Spell spell) {
        spells.add(spell);
        spellMap.put(spell.getRegistryName(), spell);
        return spell;
    }

    public static List<Spell> getSpells() {
        return spells;
    }

    public static Spell
        DARK_PRAYER = register(new PrayerSpell(
            new ResourceLocation(Eidolon.MODID, "dark_prayer"),
            Deities.DARK_DEITY,
            Signs.WICKED_SIGN, Signs.WICKED_SIGN, Signs.WICKED_SIGN
        )),
        DARK_ANIMAL_SACRIFICE = register(new AnimalSacrificeSpell(
            new ResourceLocation(Eidolon.MODID, "dark_animal_sacrifice"),
            Deities.DARK_DEITY,
            Signs.WICKED_SIGN, Signs.BLOOD_SIGN, Signs.WICKED_SIGN
        )),
        DARK_TOUCH = register(new DarkTouchSpell(
            new ResourceLocation(Eidolon.MODID, "dark_touch"),
            Signs.WICKED_SIGN, Signs.SOUL_SIGN, Signs.WICKED_SIGN, Signs.SOUL_SIGN
        )),
        DARK_VILLAGER_SACRIFICE = register(new VillagerSacrificeSpell(
            new ResourceLocation(Eidolon.MODID, "dark_villager_sacrifice"),
            Deities.DARK_DEITY,
            Signs.BLOOD_SIGN, Signs.WICKED_SIGN, Signs.BLOOD_SIGN, Signs.SOUL_SIGN
        ));
}
