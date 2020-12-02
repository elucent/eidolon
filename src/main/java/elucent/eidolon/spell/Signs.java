package elucent.eidolon.spell;

import elucent.eidolon.Eidolon;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        ));
}
