package elucent.eidolon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;

import elucent.eidolon.world.CatacombPieces;
import elucent.eidolon.world.CatacombStructure;
import elucent.eidolon.world.LabStructure;
import elucent.eidolon.world.RandomlyRotatedPiece;
import elucent.eidolon.world.StrayTowerStructure;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class WorldGen {
    static List<PlacedFeature> ORES = new ArrayList<>();
    static PlacedFeature LEAD_ORE_GEN, SILVER_ORE_GEN, DEEP_LEAD_ORE_GEN, DEEP_SILVER_ORE_GEN;
    static DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, Eidolon.MODID);
    static List<StructureFeature<?>> STRUCTURE_LIST = new ArrayList<>();
    static Map<ResourceLocation, StructureFeatureConfiguration> STRUCTURE_SETTINGS = new HashMap<>();
    static RuleTest IN_STONE = new TagMatchTest(Tags.Blocks.STONE);
    static RuleTest IN_DEEPSLATE = new BlockMatchTest(Blocks.DEEPSLATE);


    static StructurePieceType register(StructurePieceType type, String name) {
        net.minecraft.core.Registry.register(net.minecraft.core.Registry.STRUCTURE_PIECE, new ResourceLocation(Eidolon.MODID, name), type);
        return type;
    }

    static <C extends FeatureConfiguration, F extends Feature<C>> ConfiguredFeature<C, F> register(ConfiguredFeature<C, F> feature, String name) {
        BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(Eidolon.MODID, name), feature);
        return feature;
    }

    static <C extends FeatureConfiguration, S extends StructureFeature<C>> ConfiguredStructureFeature<C, S> register(ConfiguredStructureFeature<C, S> feature, String name) {
        BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(Eidolon.MODID, name), feature);
        return feature;
    }

    static <C extends FeatureConfiguration> RegistryObject<StructureFeature<C>> addStructure(String name, StructureFeature<C> structure, GenerationStep.Decoration stage, StructureFeatureConfiguration settings) {
        StructureFeature.STRUCTURES_REGISTRY.put(Eidolon.MODID + ":" + name, structure);
        StructureFeature.STEP.put(structure, stage);
        STRUCTURE_LIST.add(structure);
        STRUCTURE_SETTINGS.put(new ResourceLocation(Eidolon.MODID, name), settings);
        if (stage != GenerationStep.Decoration.UNDERGROUND_STRUCTURES) {
            StructureFeature.NOISE_AFFECTING_FEATURES = ImmutableList.<StructureFeature<?>>builder().addAll(StructureFeature.NOISE_AFFECTING_FEATURES).add(structure).build();
        }

        return STRUCTURES.register(name, () -> structure);
    }

    public static StructurePieceType LAB_PIECE, STRAY_TOWER_PIECE;

    public static RegistryObject<StructureFeature<NoneFeatureConfiguration>> LAB_STRUCTURE = addStructure("lab",
        new LabStructure(NoneFeatureConfiguration.CODEC),
        GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
        new StructureFeatureConfiguration(7, 5, 1337));

    public static RegistryObject<StructureFeature<NoneFeatureConfiguration>> STRAY_TOWER_STRUCTURE = addStructure("stray_tower",
        new StrayTowerStructure(NoneFeatureConfiguration.CODEC),
        GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
        new StructureFeatureConfiguration(16, 8, 1341));

    public static RegistryObject<StructureFeature<NoneFeatureConfiguration>> CATACOMB_STRUCTURE = addStructure("catacomb",
        new CatacombStructure(NoneFeatureConfiguration.CODEC),
        GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
        new StructureFeatureConfiguration(11, 7, 1347));

    public static ConfiguredStructureFeature<NoneFeatureConfiguration, ? extends StructureFeature<NoneFeatureConfiguration>> LAB_FEATURE, STRAY_TOWER_FEATURE, CATACOMB_FEATURE;

    public static void preInit() {
        STRUCTURES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static void init() {
        ConfiguredFeature<?, ?> LEAD_ORE = register(Feature.ORE.configured(new OreConfiguration(IN_STONE,
            Registry.LEAD_ORE.get().defaultBlockState(), Config.LEAD_VEIN_SIZE.get())), "lead_ore");
        LEAD_ORE_GEN = PlacementUtils.register("lead_ore", LEAD_ORE.placed(
    		InSquarePlacement.spread(), BiomeFilter.biome(), CountPlacement.of(Config.LEAD_VEIN_COUNT.get()), 
    		HeightRangePlacement.uniform(VerticalAnchor.absolute(Config.LEAD_MIN_Y.get()), VerticalAnchor.absolute(Config.LEAD_MAX_Y.get()))
		));
        ConfiguredFeature<?, ?> DEEP_LEAD_ORE = register(Feature.ORE.configured(new OreConfiguration(IN_DEEPSLATE,
            Registry.DEEP_LEAD_ORE.get().defaultBlockState(), Config.LEAD_VEIN_SIZE.get())), "deep_lead_ore");
        DEEP_LEAD_ORE_GEN = PlacementUtils.register("deep_lead_ore", DEEP_LEAD_ORE.placed(
    		InSquarePlacement.spread(), BiomeFilter.biome(), CountPlacement.of(Config.LEAD_VEIN_COUNT.get()), 
    		HeightRangePlacement.uniform(VerticalAnchor.absolute(Config.LEAD_MIN_Y.get()), VerticalAnchor.absolute(Config.LEAD_MAX_Y.get()))
		));
        if (Config.LEAD_ENABLED.get()) {
        	ORES.add(LEAD_ORE_GEN);
        	ORES.add(DEEP_LEAD_ORE_GEN);
        }
        
        ConfiguredFeature<?, ?> SILVER_ORE = register(Feature.ORE.configured(new OreConfiguration(IN_STONE,
            Registry.SILVER_ORE.get().defaultBlockState(), Config.SILVER_VEIN_SIZE.get())), "silver_ore");
        SILVER_ORE_GEN = PlacementUtils.register("silver_ore", SILVER_ORE.placed(
    		InSquarePlacement.spread(), BiomeFilter.biome(), CountPlacement.of(Config.SILVER_VEIN_COUNT.get()), 
    		HeightRangePlacement.uniform(VerticalAnchor.absolute(Config.SILVER_MIN_Y.get()), VerticalAnchor.absolute(Config.SILVER_MAX_Y.get()))
        ));
        ConfiguredFeature<?, ?> DEEP_SILVER_ORE = register(Feature.ORE.configured(new OreConfiguration(IN_DEEPSLATE,
            Registry.DEEP_SILVER_ORE.get().defaultBlockState(), Config.SILVER_VEIN_SIZE.get())), "deep_silver_ore");
        DEEP_SILVER_ORE_GEN = PlacementUtils.register("deep_silver_ore", DEEP_SILVER_ORE.placed(
    		InSquarePlacement.spread(), BiomeFilter.biome(), CountPlacement.of(Config.SILVER_VEIN_COUNT.get()), 
    		HeightRangePlacement.uniform(VerticalAnchor.absolute(Config.SILVER_MIN_Y.get()), VerticalAnchor.absolute(Config.SILVER_MAX_Y.get()))
        ));
        if (Config.SILVER_ENABLED.get()) {
        	ORES.add(SILVER_ORE_GEN);
        	ORES.add(DEEP_SILVER_ORE_GEN);
        }

        LAB_PIECE = register((ctx, tag) -> new RandomlyRotatedPiece(LAB_PIECE, tag, ctx.structureManager()), "lab");
        LAB_FEATURE = register(LAB_STRUCTURE.get().configured(NoneFeatureConfiguration.INSTANCE), "lab");

        STRAY_TOWER_PIECE = register((ctx, tag) -> new RandomlyRotatedPiece(STRAY_TOWER_PIECE, tag, ctx.structureManager()), "stray_tower");
        STRAY_TOWER_FEATURE = register(STRAY_TOWER_STRUCTURE.get().configured(NoneFeatureConfiguration.INSTANCE), "stray_tower");

        CatacombPieces.CORRIDOR_CENTER = register((ctx, tag) -> new CatacombPieces.CorridorCenter(ctx.structureManager(), tag), CatacombPieces.CORRIDOR_CENTER_ID.getPath());
        CatacombPieces.CORRIDOR_DOOR = register((ctx, tag) -> new CatacombPieces.CorridorDoor(ctx.structureManager(), tag), CatacombPieces.CORRIDOR_DOOR_ID.getPath());
        CatacombPieces.SMALL_ROOM = register((ctx, tag) -> new CatacombPieces.SmallRoom(ctx.structureManager(), tag), CatacombPieces.SMALL_ROOM_ID.getPath());
        CatacombPieces.SHRINE = register((ctx, tag) -> new CatacombPieces.Shrine(ctx.structureManager(), tag), CatacombPieces.SHRINE_ID.getPath());
        CatacombPieces.TRAP = register((ctx, tag) -> new CatacombPieces.Trap(ctx.structureManager(), tag), CatacombPieces.TRAP_ID.getPath());
        CatacombPieces.SKULL = register((ctx, tag) -> new CatacombPieces.Skull(ctx.structureManager(), tag), CatacombPieces.SKULL_ID.getPath());
        CatacombPieces.SPAWNER = register((ctx, tag) -> new CatacombPieces.Spawner(ctx.structureManager(), tag), CatacombPieces.SPAWNER_ID.getPath());
        CatacombPieces.COFFIN = register((ctx, tag) -> new CatacombPieces.Coffin(ctx.structureManager(), tag), CatacombPieces.COFFIN_ID.getPath());
        CatacombPieces.MEDIUM_ROOM = register((ctx, tag) -> new CatacombPieces.MediumRoom(ctx.structureManager(), tag), CatacombPieces.MEDIUM_ROOM_ID.getPath());
        CatacombPieces.GRAVEYARD = register((ctx, tag) -> new CatacombPieces.Graveyard(ctx.structureManager(), tag), CatacombPieces.GRAVEYARD_ID.getPath());
        CatacombPieces.TURNAROUND = register((ctx, tag) -> new CatacombPieces.Turnaround(ctx.structureManager(), tag), CatacombPieces.TURNAROUND_ID.getPath());
        CatacombPieces.LAB = register((ctx, tag) -> new CatacombPieces.Lab(ctx.structureManager(), tag), CatacombPieces.LAB_ID.getPath());
        CATACOMB_FEATURE = register(CATACOMB_STRUCTURE.get().configured(NoneFeatureConfiguration.INSTANCE), "catacomb");

        for (StructureFeature<?> s : STRUCTURE_LIST) {
            StructureSettings.DEFAULTS = // Default structures
                ImmutableMap.<StructureFeature<?>, StructureFeatureConfiguration>builder()
                    .putAll(StructureSettings.DEFAULTS)
                    .put(s, STRUCTURE_SETTINGS.get(s.getRegistryName()))
                    .build();

            NoiseGeneratorSettings overworld = BuiltinRegistries.NOISE_GENERATOR_SETTINGS.get(NoiseGeneratorSettings.OVERWORLD);
            overworld.structureSettings().structureConfig.put(s, STRUCTURE_SETTINGS.get(s.getRegistryName()));
        }
    }
    
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
//        if (event.getWorld() instanceof ServerLevel level) {
//            StructureSettings settings = level.getChunkSource().getGenerator().getSettings();
//            
//            ImmutableMap<StructureFeature<?>, ImmutableMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>>
//	        if (Config.LAB_ENABLED.get())
//	        	Structure
//	            settings.structureConfig.computeIfAbsent(LAB_STRUCTURE.get(), ).addFeature(LAB_FEATURE);
//	        if (Config.CATACOMB_ENABLED.get())
//	            event.getGeneration().addStructureStart(CATACOMB_FEATURE);
//	        if (event.getCategory() == Biome.BiomeCategory.ICY && Config.STRAY_TOWER_ENABLED.get())
//	            event.getGeneration().addStructureStart(STRAY_TOWER_FEATURE);
//        }
    }

    @SubscribeEvent
    public void onBiomeLoad(BiomeLoadingEvent event) {
        for (PlacedFeature feature : ORES) {
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, feature);
        }
    }
}
