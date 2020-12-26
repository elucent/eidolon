package elucent.eidolon.spell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import elucent.eidolon.Eidolon;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.util.ResourceLocation;

public class Signs {
    static List<Sign> signs = new ArrayList<>();
    static Map<ResourceLocation, Sign> signMap = new HashMap<>();

    public static Sign find(ResourceLocation loc) {
        return signMap.getOrDefault(loc, null);
    }

    public static Sign register(Sign sign) {
        signs.add(sign);
        signMap.put(sign.getRegistryName(), sign);
        return sign;
    }

    public static List<Sign> getSigns() {
        return signs;
    }

    public static Sign
        WICKED_SIGN = register(new Sign(
            new ResourceLocation(Eidolon.MODID, "wicked"),
            new ResourceLocation(Eidolon.MODID, "particle/wicked_sign"),
        ColorUtil.packColor(255, 154, 77, 255)
        )),
        SACRED_SIGN = register(new Sign(
            new ResourceLocation(Eidolon.MODID, "sacred"),
            new ResourceLocation(Eidolon.MODID, "particle/sacred_sign"),
            ColorUtil.packColor(255, 255, 230, 117)
        )),
        BLOOD_SIGN = register(new Sign(
            new ResourceLocation(Eidolon.MODID, "blood"),
            new ResourceLocation(Eidolon.MODID, "particle/blood_sign"),
            ColorUtil.packColor(255, 255, 51, 85)
        )),
        SOUL_SIGN = register(new Sign(
            new ResourceLocation(Eidolon.MODID, "soul"),
            new ResourceLocation(Eidolon.MODID, "particle/soul_sign"),
            ColorUtil.packColor(255, 230, 138, 226)
        )),
        MIND_SIGN = register(new Sign(
            new ResourceLocation(Eidolon.MODID, "mind"),
            new ResourceLocation(Eidolon.MODID, "particle/mind_sign"),
            ColorUtil.packColor(255, 90, 121, 255)
        )),
        WARDING_SIGN = register(new Sign(
            new ResourceLocation(Eidolon.MODID, "warding"),
            new ResourceLocation(Eidolon.MODID, "particle/warding_sign"),
            ColorUtil.packColor(255, 190, 212, 184)
        )),
        ENERGY_SIGN = register(new Sign(
            new ResourceLocation(Eidolon.MODID, "energy"),
            new ResourceLocation(Eidolon.MODID, "particle/energy_sign"),
            ColorUtil.packColor(255, 145, 250, 100)
        ));
}
