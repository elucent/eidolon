package elucent.eidolon.world;

import com.mojang.serialization.Codec;
import elucent.eidolon.Config;
import elucent.eidolon.Eidolon;
import elucent.eidolon.world.worldgen.WorldGen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Random;

public class LabStructure extends StructureFeature<NoneFeatureConfiguration> {
    public LabStructure(Codec<NoneFeatureConfiguration> codec) {
        super(codec, LabStructure::pieceGeneratorSupplier);
    }

    protected static boolean isFeatureChunk(long seed, Random rand, int chunkX, int chunkZ) {
        int i = chunkX >> 4;
        int j = chunkZ >> 4;
        rand.setSeed((long) (i ^ j << 4) ^ seed);
        double prob = rand.nextInt(10000) / 10000.0f;
        return prob < (1 / Config.LAB_RARITY.get());
    }

    private static @NotNull Optional<PieceGenerator<NoneFeatureConfiguration>>
        pieceGeneratorSupplier(PieceGeneratorSupplier.Context<NoneFeatureConfiguration> context) {

        var chunkPos = context.chunkPos();
        var chunkX = chunkPos.x;
        var chunkZ = chunkPos.z;

        var seed = context.seed();
        var random = WorldGen.RAND;
        random.setSeed(seed);

        if (!isFeatureChunk(seed, random, chunkX, chunkZ)) {
            return Optional.empty();
        }

        var generator = context.chunkGenerator();

        int i = chunkX * 16;
        int j = chunkZ * 16;
        int k = Math.min(generator.getSeaLevel(), generator.getFirstFreeHeight(i, j, Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor()));
        if (k < 33) k = 33;
        BlockPos blockpos = new BlockPos(i + random.nextInt(16), random.nextInt(k - 32) + 8, j + random.nextInt(16));
        Rotation rotation = Rotation.getRandom(random);

        return Optional.of((piecesBuilder, configurationContext) -> piecesBuilder
                .addPiece(new Piece(configurationContext.structureManager(), blockpos, rotation, random)));
    }

    private static final ResourceLocation PART = new ResourceLocation(Eidolon.MODID,"lab");

    public static class Piece extends BasicPiece {
        public Piece(StructurePieceSerializationContext context, CompoundTag nbt) {
            super(WorldGen.LAB_PIECE.get(), PART, context.structureManager(), nbt);
            mirror = Mirror.NONE;
        }

        public Piece(StructureManager templateManager, BlockPos pos, Rotation rot, Random random) {
            super(WorldGen.LAB_PIECE.get(), PART, templateManager, pos, rot, Mirror.NONE, random);
        }
    }
}
