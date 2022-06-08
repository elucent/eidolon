package elucent.eidolon.world;

import com.mojang.serialization.Codec;
import elucent.eidolon.Config;
import elucent.eidolon.Eidolon;
import elucent.eidolon.WorldGen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

import net.minecraft.world.gen.feature.structure.Structure.IStartFactory;

public class StrayTowerStructure extends Structure<NoFeatureConfig> {
    public StrayTowerStructure(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public GenerationStage.Decoration step() {
        return GenerationStage.Decoration.UNDERGROUND_STRUCTURES;
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return Start::new;
    }

    @Override
    protected boolean isFeatureChunk(ChunkGenerator generator, BiomeProvider provider, long seed, SharedSeedRandom rand, int chunkX, int chunkZ, Biome biome, ChunkPos pos, NoFeatureConfig config) {
        int i = chunkX >> 4;
        int j = chunkZ >> 4;
        rand.setSeed((long) (i ^ j << 4) ^ seed);
        double prob = rand.nextInt(10000) / 10000.0f;
        return prob < (1 / Config.STRAY_TOWER_RARITY.get());
    }

    @Override
    public String getFeatureName() {
        return new ResourceLocation(Eidolon.MODID, "stray_tower").toString();
    }

    public static class Start extends StructureStart<NoFeatureConfig> {
        public Start(Structure<NoFeatureConfig> config, int chunkX, int chunkZ, MutableBoundingBox bounds, int refs, long seed) {
            super(config, chunkX, chunkZ, bounds, refs, seed);
        }

        public void generatePieces(DynamicRegistries registries, ChunkGenerator generator, TemplateManager templateManager, int chunkX, int chunkZ, Biome biome, NoFeatureConfig config) {
            int i = chunkX * 16 + random.nextInt(16);
            int j = chunkZ * 16 + random.nextInt(16);
            Rotation rotation = Rotation.getRandom(random);

            pieces.add(new Piece(templateManager, PART, new BlockPos(i, generator.getBaseHeight(i, j, Heightmap.Type.WORLD_SURFACE_WG), j), rotation, random));
            this.calculateBoundingBox();
        }
    }

    private static final ResourceLocation PART = new ResourceLocation(Eidolon.MODID,"stray_tower");

    public static class Piece extends TemplateStructurePiece {
        private ResourceLocation loc;
        private Rotation rotation;

        public Piece(IStructurePieceType structurePieceTypeIn, CompoundTag nbt) {
            super(structurePieceTypeIn, nbt);
            this.loc = new ResourceLocation(nbt.getString("template"));
            this.rotation = Rotation.valueOf(nbt.getString("rot"));
        }

        public Piece(TemplateManager templateManager, CompoundTag nbt) {
            this(WorldGen.STRAY_TOWER_PIECE, nbt);
            Template part = templateManager.getOrCreate(PART);
            PlacementSettings placement = (new PlacementSettings()).setRotation(this.rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
            this.setup(part, this.templatePosition, placement);
            this.template = templateManager.get(PART);
        }

        public Piece(TemplateManager templateManager, ResourceLocation template, BlockPos pos, Rotation rot, Random random) {
            super(WorldGen.STRAY_TOWER_PIECE, 0);
            this.templatePosition = pos.below(10);
            this.rotation = rot;
            this.loc = template;

            Template part = templateManager.getOrCreate(PART);
            PlacementSettings placement = (new PlacementSettings()).setRotation(this.rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
            this.setup(part, this.templatePosition, placement);
            this.template = templateManager.get(PART);
        }

        @Override
        protected void addAdditionalSaveData(CompoundTag tagCompound) {
            super.addAdditionalSaveData(tagCompound);
            tagCompound.putString("template", loc.toString());
            tagCompound.putString("rot", rotation.name());
        }

        @Override
        protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand, MutableBoundingBox sbb) {
            //
        }
    }
}
