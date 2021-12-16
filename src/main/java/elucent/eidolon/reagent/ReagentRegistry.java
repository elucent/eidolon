package elucent.eidolon.reagent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import elucent.eidolon.Eidolon;
import net.minecraft.resources.ResourceLocation;

public class ReagentRegistry {
    static Map<ResourceLocation, Reagent> reagents = new HashMap<>();

    public static Reagent register(Reagent r) {
        reagents.put(r.getRegistryName(), r);
        return r;
    }

    public static Collection<Reagent> getReagents() {
        return reagents.values();
    }

    @Nullable
    public static Reagent find(ResourceLocation location) {
        return reagents.getOrDefault(location, null);
    }

    public static Reagent
        STEAM = register(new SteamReagent(new ResourceLocation(Eidolon.MODID, "steam"))),
        ESPRIT = register(new EspritReagent(new ResourceLocation(Eidolon.MODID, "esprit"))),
        CRIMSOL = register(new CrimsolReagent(new ResourceLocation(Eidolon.MODID, "crimsol")));
}
