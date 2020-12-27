package elucent.eidolon.world;

import java.util.Random;

import com.mojang.serialization.Codec;

import elucent.eidolon.Config;
import elucent.eidolon.Eidolon;
import elucent.eidolon.WorldGen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
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

public class LabStructure extends Structure<NoFeatureConfig> {
    public LabStructure(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public GenerationStage.Decoration getDecorationStage() {
        return GenerationStage.Decoration.UNDERGROUND_STRUCTURES;
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return Start::new;
    }

    @Override
    protected boolean func_230363_a_(ChunkGenerator generator, BiomeProvider provider, long seed, SharedSeedRandom rand, int chunkX, int chunkZ, Biome biome, ChunkPos pos, NoFeatureConfig config) {
        int i = chunkX >> 4;
        int j = chunkZ >> 4;
        rand.setSeed((long) (i ^ j << 4) ^ seed);
        double prob = rand.nextInt(10000) / 10000.0f;
        return prob < (1 / Config.LAB_RARITY.get());
    }

    @Override
    public String getStructureName() {
        return new ResourceLocation(Eidolon.MODID, "lab").toString();
    }

    public static class Start extends StructureStart<NoFeatureConfig> {
        public Start(Structure<NoFeatureConfig> config, int chunkX, int chunkZ, MutableBoundingBox bounds, int refs, long seed) {
            super(config, chunkX, chunkZ, bounds, refs, seed);
        }

        public void func_230364_a_(DynamicRegistries registries, ChunkGenerator generator, TemplateManager templateManager, int chunkX, int chunkZ, Biome biome, NoFeatureConfig config) {
            int i = chunkX * 16;
            int j = chunkZ * 16;
            int k = generator.getNoiseHeight(i, j, Heightmap.Type.OCEAN_FLOOR_WG);
            if (k < 33) k = 33;
            BlockPos blockpos = new BlockPos(i + rand.nextInt(16), rand.nextInt(k - 32) + 8, j + rand.nextInt(16));
            Rotation rotation = Rotation.randomRotation(rand);

            components.add(new Piece(templateManager, PART, blockpos, rotation, rand));
            this.recalculateStructureSize();
        }
    }

    private static final ResourceLocation PART = new ResourceLocation(Eidolon.MODID,"lab");

    public static class Piece extends TemplateStructurePiece {
        private ResourceLocation loc;
        private Rotation rotation;

        public Piece(IStructurePieceType structurePieceTypeIn, CompoundNBT nbt) {
            super(structurePieceTypeIn, nbt);
            this.loc = new ResourceLocation(nbt.getString("template"));
            this.rotation = Rotation.valueOf(nbt.getString("rot"));
        }

        public Piece(TemplateManager templateManager, CompoundNBT nbt) {
            this(WorldGen.LAB_PIECE, nbt);
            Template part = templateManager.getTemplateDefaulted(PART);
            PlacementSettings placement = (new PlacementSettings()).setRotation(this.rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
            this.setup(part, this.templatePosition, placement);
            this.template = templateManager.getTemplate(PART);
        }

        public Piece(TemplateManager templateManager, ResourceLocation template, BlockPos pos, Rotation rot, Random random) {
            super(WorldGen.LAB_PIECE, 0);
            this.templatePosition = pos;
            this.rotation = rot;
            this.loc = template;

            Template part = templateManager.getTemplateDefaulted(PART);
            PlacementSettings placement = (new PlacementSettings()).setRotation(this.rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
            this.setup(part, this.templatePosition, placement);
            this.template = templateManager.getTemplate(PART);
        }

        @Override
        protected void readAdditional(CompoundNBT tagCompound) {
            super.readAdditional(tagCompound);
            tagCompound.putString("template", loc.toString());
            tagCompound.putString("rot", rotation.name());
        }

        @Override
        protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand, MutableBoundingBox sbb) {
            //
        }
    }
}
