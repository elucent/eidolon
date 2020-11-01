package elucent.eidolon;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import org.apache.commons.lang3.tuple.Pair;

public class Config {
    // mobs
    public static ConfigValue<Integer> WRAITH_SPAWN_WEIGHT, ZOMBIE_BRUTE_SPAWN_WEIGHT;

    // ores
    public static ConfigValue<Integer> LEAD_MIN_Y, LEAD_MAX_Y, LEAD_VEIN_SIZE, LEAD_VEIN_COUNT;

    public Config(ForgeConfigSpec.Builder builder) {
        builder.comment("Mob settings").push("mobs");
        WRAITH_SPAWN_WEIGHT = builder.comment("Spawn weight for wraith entity")
            .define("wraithSpawnWeight", 50);
        ZOMBIE_BRUTE_SPAWN_WEIGHT = builder.comment("Spawn weight for zombie brute entity")
            .define("zombieBruteSpawnWeight", 25);
        builder.pop();

        builder.comment("Ore generation settings").push("ores");
        LEAD_MIN_Y = builder.comment("Minimum Y value for lead ore veins")
               .define("leadOreMinY", 0);
        LEAD_MAX_Y = builder.comment("Maximum Y value for lead ore veins")
            .define("leadOreMaxY", 41);
        LEAD_VEIN_SIZE = builder.comment("Maximum number of blocks per lead ore vein")
            .define("leadOreVeinSize", 6);
        LEAD_VEIN_COUNT = builder.comment("Number of lead ore veins per chunk")
            .define("leadOreVeinCount", 12);
        builder.pop();
    }

    public static final Config INSTANCE;
    public static final ForgeConfigSpec SPEC;

    static {
        final Pair<Config, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Config::new);
        SPEC = specPair.getRight();
        INSTANCE = specPair.getLeft();
    }
}
