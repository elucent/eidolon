package elucent.eidolon.world.worldgen;

import elucent.eidolon.Config;
import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;

/**
 * @author DustW
 **/
public class Ores {
    static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURE =
            DeferredRegister.create(BuiltinRegistries.CONFIGURED_FEATURE.key(), Eidolon.MODID);

    static final DeferredRegister<PlacedFeature> PLACED_FEATURE =
            DeferredRegister.create(BuiltinRegistries.PLACED_FEATURE.key(), Eidolon.MODID);

    public static final RegistryObject<ConfiguredFeature<?, ?>> LEAD_ORE =
            CONFIGURED_FEATURE.register("lead_ore", () -> new ConfiguredFeature<>(
                    Feature.ORE,
                    new OreConfiguration(
                            OreFeatures.NATURAL_STONE,
                            Registry.LEAD_ORE.get().defaultBlockState(),
                            Config.LEAD_VEIN_SIZE.get()
                    )
            ));

    public static final RegistryObject<PlacedFeature> LEAD_ORE_PLACED =
            PLACED_FEATURE.register("lead_ore_placed",
                    () -> new PlacedFeature(LEAD_ORE.getHolder().get(),
                            Arrays.asList(InSquarePlacement.spread(),
                                    HeightRangePlacement.of(ConstantHeight.of(VerticalAnchor.belowTop(Config.LEAD_MAX_Y.get()))),
                                    CountPlacement.of(Config.LEAD_VEIN_COUNT.get()))));
}
