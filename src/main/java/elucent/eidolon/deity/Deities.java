package elucent.eidolon.deity;

import java.util.HashMap;
import java.util.Map;

import elucent.eidolon.Eidolon;
import net.minecraft.resources.ResourceLocation;

public class Deities {
    static Map<ResourceLocation, Deity> deities = new HashMap<>();

    public static Deity register(Deity deity) {
        deities.put(deity.getId(), deity);
        return deity;
    }

    public static Deity find(ResourceLocation deity) {
        return deities.getOrDefault(deity, null);
    }

    public static final Deity
        DARK_DEITY = register(new DarkDeity(new ResourceLocation(Eidolon.MODID, "dark"), 154, 77, 255)),
        LIGHT_DEITY = register(new LightDeity(new ResourceLocation(Eidolon.MODID, "light"), 255, 230, 117));
}
