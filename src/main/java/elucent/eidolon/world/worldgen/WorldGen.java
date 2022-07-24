package elucent.eidolon.world.worldgen;

import com.mojang.datafixers.util.Pair;
import elucent.eidolon.Eidolon;
import elucent.eidolon.world.CatacombPieces;
import elucent.eidolon.world.CatacombStructure;
import elucent.eidolon.world.LabStructure;
import elucent.eidolon.world.StrayTowerStructure;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class WorldGen {

    public static final Random RAND = new Random();

    static DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, Eidolon.MODID);
    static DeferredRegister<StructurePieceType> PIECES = DeferredRegister.create(Registry.STRUCTURE_PIECE.key(), Eidolon.MODID);
    static DeferredRegister<ConfiguredStructureFeature<?, ?>> CONFIGURED_STRUCTURE =
            DeferredRegister.create(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.key(), Eidolon.MODID);
    static DeferredRegister<StructureSet> STRUCTURE_SET = DeferredRegister.create(Registry.STRUCTURE_SET_REGISTRY, Eidolon.MODID);
    static DeferredRegister<StructureTemplatePool> TEMPLATE_POOL = DeferredRegister.create(Registry.TEMPLATE_POOL_REGISTRY, Eidolon.MODID);

    static RegistryObject<StructurePieceType> register(Supplier<StructurePieceType> type, String name) {
        return PIECES.register(name, type);
    }

    static RegistryObject<ConfiguredStructureFeature<NoneFeatureConfiguration, ? extends StructureFeature<NoneFeatureConfiguration>>>
    registerC(Supplier<ConfiguredStructureFeature<NoneFeatureConfiguration, ? extends StructureFeature<NoneFeatureConfiguration>>> type, String name) {
        return CONFIGURED_STRUCTURE.register(name, type);
    }

    static RegistryObject<StructureFeature<NoneFeatureConfiguration>> addStructure(String name, Supplier<StructureFeature<NoneFeatureConfiguration>> structure) {
        return STRUCTURES.register(name, structure);
    }

    public static RegistryObject<StructurePieceType> LAB_PIECE, STRAY_TOWER_PIECE;

    public static RegistryObject<StructureFeature<NoneFeatureConfiguration>> LAB_STRUCTURE = addStructure("lab",
            () -> new LabStructure(NoneFeatureConfiguration.CODEC));

    public static RegistryObject<StructureFeature<NoneFeatureConfiguration>> STRAY_TOWER_STRUCTURE = addStructure("stray_tower",
            () -> new StrayTowerStructure(NoneFeatureConfiguration.CODEC));

    public static RegistryObject<StructureFeature<NoneFeatureConfiguration>> CATACOMB_STRUCTURE = addStructure("catacomb",
            () -> new CatacombStructure(NoneFeatureConfiguration.CODEC));

    public static RegistryObject<ConfiguredStructureFeature<NoneFeatureConfiguration, ? extends StructureFeature<NoneFeatureConfiguration>>>
            LAB_FEATURE, STRAY_TOWER_FEATURE, CATACOMB_FEATURE;

    public static void preInit() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        STRUCTURES.register(bus);
        PIECES.register(bus);
        CONFIGURED_STRUCTURE.register(bus);
        STRUCTURE_SET.register(bus);
        TEMPLATE_POOL.register(bus);
    }

    public static void init() {
        LAB_PIECE = register(() -> LabStructure.Piece::new, "lab");
        LAB_FEATURE = registerC(() -> addStep(LAB_STRUCTURE.get().configured(NoneFeatureConfiguration.INSTANCE, BiomeTags.HAS_VILLAGE_PLAINS)), "lab");

        STRAY_TOWER_PIECE = register(() -> StrayTowerStructure.Piece::new, "stray_tower");
        STRAY_TOWER_FEATURE = registerC(() -> addStep(STRAY_TOWER_STRUCTURE.get().configured(NoneFeatureConfiguration.INSTANCE, BiomeTags.HAS_VILLAGE_PLAINS)), "stray_tower");

        CatacombPieces.CORRIDOR_CENTER = register(() -> CatacombPieces.CorridorCenter::new, CatacombPieces.CORRIDOR_CENTER_ID.getPath());
        CatacombPieces.CORRIDOR_DOOR = register(() -> CatacombPieces.CorridorDoor::new, CatacombPieces.CORRIDOR_DOOR_ID.getPath());
        CatacombPieces.SMALL_ROOM = register(() -> CatacombPieces.SmallRoom::new, CatacombPieces.SMALL_ROOM_ID.getPath());
        CatacombPieces.SHRINE = register(() -> CatacombPieces.Shrine::new, CatacombPieces.SHRINE_ID.getPath());
        CatacombPieces.TRAP = register(() -> CatacombPieces.Trap::new, CatacombPieces.TRAP_ID.getPath());
        CatacombPieces.SKULL = register(() -> CatacombPieces.Skull::new, CatacombPieces.SKULL_ID.getPath());
        CatacombPieces.SPAWNER = register(() -> CatacombPieces.Spawner::new, CatacombPieces.SPAWNER_ID.getPath());
        CatacombPieces.COFFIN = register(() -> CatacombPieces.Coffin::new, CatacombPieces.COFFIN_ID.getPath());
        CatacombPieces.MEDIUM_ROOM = register(() -> CatacombPieces.MediumRoom::new, CatacombPieces.MEDIUM_ROOM_ID.getPath());
        CatacombPieces.GRAVEYARD = register(() -> CatacombPieces.Graveyard::new, CatacombPieces.GRAVEYARD_ID.getPath());
        CatacombPieces.TURNAROUND = register(() -> CatacombPieces.Turnaround::new, CatacombPieces.TURNAROUND_ID.getPath());
        CatacombPieces.LAB = register(() -> CatacombPieces.Lab::new, CatacombPieces.LAB_ID.getPath());
        CATACOMB_FEATURE = registerC(() -> addStep(CATACOMB_STRUCTURE.get().configured(NoneFeatureConfiguration.INSTANCE, BiomeTags.HAS_VILLAGE_SNOWY), GenerationStep.Decoration.UNDERGROUND_STRUCTURES), "catacomb");

        baseTemplate(baseSet(LAB_FEATURE, 2496546));
        baseTemplate(baseSet(STRAY_TOWER_FEATURE, 8146023));
        baseTemplate(baseSet(CATACOMB_FEATURE, 9564599));
    }

    static RegistryObject<StructureSet> baseSet(RegistryObject object, int salt) {
        return STRUCTURE_SET.register(object.getId().getPath(), () -> new StructureSet(
                List.of(StructureSet.entry((Holder<ConfiguredStructureFeature<?,?>>) (object.getHolder().get()), 1)),
                new RandomSpreadStructurePlacement(30, 8, RandomSpreadType.LINEAR, salt)
        ));
    }

    static final ResourceLocation EMPTY_FALLBACK = new ResourceLocation("empty");

    static RegistryObject<StructureTemplatePool> baseTemplate(RegistryObject<StructureSet> object) {
        return TEMPLATE_POOL.register(object.getId().getPath(), () -> new StructureTemplatePool(
                object.getId(), EMPTY_FALLBACK,
                List.of(new Pair<>(new SinglePoolElement(new StructureTemplate()), 1))
        ));
    }

    static ConfiguredStructureFeature<NoneFeatureConfiguration, ? extends StructureFeature<NoneFeatureConfiguration>> addStep(
            ConfiguredStructureFeature<NoneFeatureConfiguration, ? extends StructureFeature<NoneFeatureConfiguration>> feature,
            GenerationStep.Decoration decoration
    ) {
        StructureFeature.STEP.put(feature.feature, decoration);
        return feature;
    }

    static ConfiguredStructureFeature<NoneFeatureConfiguration, ? extends StructureFeature<NoneFeatureConfiguration>> addStep(
            ConfiguredStructureFeature<NoneFeatureConfiguration, ? extends StructureFeature<NoneFeatureConfiguration>> feature
    ) {
        return addStep(feature, GenerationStep.Decoration.SURFACE_STRUCTURES);
    }

    @SubscribeEvent
    public void onBiomeLoad(BiomeLoadingEvent event) {
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Ores.LEAD_ORE_PLACED.getHolder().get());

        // if (Config.LAB_ENABLED.get())
        //     event.getGeneration().addStructureStart(LAB_FEATURE);
        // if (Config.CATACOMB_ENABLED.get())
        //     event.getGeneration().addStructureStart(CATACOMB_FEATURE);
        // if (event.getCategory() == Biome.Category.ICY && Config.STRAY_TOWER_ENABLED.get())
        //     event.getGeneration().addStructureStart(STRAY_TOWER_FEATURE);
    }

    public static void register(IEventBus bus) {
        Ores.CONFIGURED_FEATURE.register(bus);
        Ores.PLACED_FEATURE.register(bus);
    }
}
