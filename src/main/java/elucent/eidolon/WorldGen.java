package elucent.eidolon;

import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.feature.template.TagMatchRuleTest;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class WorldGen {
    static List<ConfiguredFeature<?, ?>> ORES = new ArrayList<>();

    static ConfiguredFeature<?, ?> LEAD_ORE_GEN;

    static RuleTest IN_STONE = new TagMatchRuleTest(Tags.Blocks.STONE);

    public static void init() {
        LEAD_ORE_GEN = Feature.ORE.withConfiguration(new OreFeatureConfig(IN_STONE,
            Registry.LEAD_ORE.get().getDefaultState(), Config.LEAD_VEIN_SIZE.get()))
            .func_242728_a()
            .func_242731_b(Config.LEAD_VEIN_COUNT.get()) // per chunk
            .func_242733_d(Config.LEAD_MAX_Y.get()) // maximum Y
            ;

        ORES.add(LEAD_ORE_GEN);
    }

    @SubscribeEvent
    public void onBiomeLoad(BiomeLoadingEvent event) {
        for (ConfiguredFeature<?, ?> feature : ORES) {
            event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, feature);
        }
    }
}
