package elucent.eidolon.world;

import java.util.Random;

import com.mojang.serialization.Codec;

import elucent.eidolon.Config;
import elucent.eidolon.Eidolon;
import elucent.eidolon.WorldGen;
import elucent.eidolon.world.LabStructure.LabPieceGenerator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator.Context;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

public class StrayTowerStructure extends StructureFeature<NoneFeatureConfiguration> {
    public StrayTowerStructure(Codec<NoneFeatureConfiguration> codec) {
        super(codec, PieceGeneratorSupplier.simple(StrayTowerStructure::isFeatureChunk, new StrayTowerPieceGenerator()));
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
    }
    
    static Random rand = new Random();

    static boolean isFeatureChunk(PieceGeneratorSupplier.Context<NoneFeatureConfiguration> ctx) {
        int i = ctx.chunkPos().x >> 4;
        int j = ctx.chunkPos().z >> 4;
        rand.setSeed((long) (i ^ j << 4) * 1556190469);
        double prob = rand.nextInt(10000) / 10000.0f;
        return prob < (1 / Config.STRAY_TOWER_RARITY.get());
    }
    
    public static class StrayTowerPieceGenerator implements PieceGenerator<NoneFeatureConfiguration> {
		@Override
		public void generatePieces(StructurePiecesBuilder pieces, Context<NoneFeatureConfiguration> ctx) {
            int i = ctx.chunkPos().x * 16;
            int j = ctx.chunkPos().z * 16;
            pieces.addPiece(new RandomlyRotatedPiece(WorldGen.STRAY_TOWER_PIECE, PART, ctx.structureManager(), new BlockPos(i, ctx.chunkGenerator().getBaseHeight(i, j, Heightmap.Types.WORLD_SURFACE_WG, ctx.heightAccessor()), j), ctx.random()));
		}
    }

    @Override
    public String getFeatureName() {
        return new ResourceLocation(Eidolon.MODID, "stray_tower").toString();
    }

    private static final ResourceLocation PART = new ResourceLocation(Eidolon.MODID,"stray_tower");
}
