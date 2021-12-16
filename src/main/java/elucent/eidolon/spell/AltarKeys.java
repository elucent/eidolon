package elucent.eidolon.spell;

import java.util.HashSet;
import java.util.Set;

import elucent.eidolon.Eidolon;
import net.minecraft.resources.ResourceLocation;

public class AltarKeys {
    Set<ResourceLocation> keys = new HashSet<>();

    public static final ResourceLocation
        LIGHT_KEY = new ResourceLocation(Eidolon.MODID, "light"),
        SKULL_KEY = new ResourceLocation(Eidolon.MODID, "skull"),
        PLANT_KEY = new ResourceLocation(Eidolon.MODID, "plant"),
        GOBLET_KEY = new ResourceLocation(Eidolon.MODID, "goblet");
}
