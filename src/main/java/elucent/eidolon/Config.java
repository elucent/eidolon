package elucent.eidolon;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import org.apache.commons.lang3.tuple.Pair;

public class Config {
    // mobs
    public static ConfigValue<Integer> WRAITH_SPAWN_WEIGHT, ZOMBIE_BRUTE_SPAWN_WEIGHT;

    // world
    public static ConfigValue<Integer> LEAD_MIN_Y, LEAD_MAX_Y, LEAD_VEIN_SIZE, LEAD_VEIN_COUNT;
    public static ConfigValue<Float> LAB_RARITY, STRAY_TOWER_RARITY;

    public Config(ForgeConfigSpec.Builder builder) {
        builder.comment("Mob settings").push("mobs");
        WRAITH_SPAWN_WEIGHT = builder.comment("Spawn weight for wraith entity")
            .define("wraithSpawnWeight", 40);
        ZOMBIE_BRUTE_SPAWN_WEIGHT = builder.comment("Spawn weight for zombie brute entity")
            .define("zombieBruteSpawnWeight", 40);
        builder.pop();

        builder.comment("World generation settings").push("world");
        LEAD_MIN_Y = builder.comment("Minimum Y value for lead ore veins")
               .define("leadOreMinY", 0);
        LEAD_MAX_Y = builder.comment("Maximum Y value for lead ore veins")
            .define("leadOreMaxY", 41);
        LEAD_VEIN_SIZE = builder.comment("Maximum number of blocks per lead ore vein")
            .define("leadOreVeinSize", 6);
        LEAD_VEIN_COUNT = builder.comment("Number of lead ore veins per chunk")
            .define("leadOreVeinCount", 6);
        LAB_RARITY = builder.comment("Rarity of the lab structure. Higher numbers mean rarer structures.")
            .define("labRarity", 4.0f);
        STRAY_TOWER_RARITY = builder.comment("Rarity of the stray tower structure. Higher numbers mean rarer structures.")
            .define("strayTowerRarity", 4.0f);
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
