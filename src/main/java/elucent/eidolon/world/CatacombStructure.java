package elucent.eidolon.world;

import com.mojang.serialization.Codec;
import elucent.eidolon.Config;
import elucent.eidolon.Eidolon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.*;

public class CatacombStructure extends Structure<NoFeatureConfig> {
    public CatacombStructure(Codec<NoFeatureConfig> codec) {
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
        return prob < (1 / Config.CATACOMB_RARITY.get());
    }

    @Override
    public String getStructureName() {
        return new ResourceLocation(Eidolon.MODID, "catacomb").toString();
    }

    public static class Start extends StructureStart<NoFeatureConfig> {
        public Start(Structure<NoFeatureConfig> config, int chunkX, int chunkZ, MutableBoundingBox bounds, int refs, long seed) {
            super(config, chunkX, chunkZ, bounds, refs, seed);
        }

        public void func_230364_a_(DynamicRegistries registries, ChunkGenerator generator, TemplateManager templateManager, int chunkX, int chunkZ, Biome biome, NoFeatureConfig config) {
            int i = chunkX * 16;
            int j = chunkZ * 16;
            int k = Math.min(32, generator.getNoiseHeight(i, j, Heightmap.Type.OCEAN_FLOOR_WG));
            if (k < 17) k = 17;
            BlockPos blockpos = new BlockPos(i + rand.nextInt(16), rand.nextInt(k - 16) + 8, j + rand.nextInt(16));

            generate(templateManager, blockpos, rand, components);
            this.recalculateStructureSize();
        }

        enum RoomType {
            // LARGE_ROOM,
            MEDIUM_ROOM,
            SMALL_ROOM,
            CORRIDOR,
            EMPTY
        }

        interface ICatacombFactory {
            StructurePiece create(TemplateManager tm, BlockPos pos, Random random);
        }

        static ICatacombFactory[][] POOLS = new ICatacombFactory[][]{
            new ICatacombFactory[]{
                CatacombPieces.MediumRoom::new,
                CatacombPieces.MediumRoom::new,
                CatacombPieces.Turnaround::new,
                CatacombPieces.Turnaround::new,
                CatacombPieces.Graveyard::new,
                CatacombPieces.Graveyard::new,
                CatacombPieces.Coffin::new,
                CatacombPieces.Lab::new
            },
            new ICatacombFactory[]{
                CatacombPieces.SmallRoom::new,
                CatacombPieces.SmallRoom::new,
                CatacombPieces.SmallRoom::new,
                CatacombPieces.SmallRoom::new,
                CatacombPieces.SmallRoom::new,
                CatacombPieces.SmallRoom::new,
                CatacombPieces.Skull::new,
                CatacombPieces.Skull::new,
                CatacombPieces.Skull::new,
                CatacombPieces.Shrine::new,
                CatacombPieces.Spawner::new,
                CatacombPieces.Trap::new,
            },
            new ICatacombFactory[]{ CatacombPieces.CorridorCenter::new },
            new ICatacombFactory[]{}
        };

        RoomType at(Map<BlockPos, RoomType> rooms, int x, int z) {
            return rooms.getOrDefault(new BlockPos(x, 0, z), RoomType.EMPTY);
        }

        boolean canPlace(Map<BlockPos, RoomType> rooms, int x, int z, int w, int h) {
            for (int i = 0; i < w; i ++) {
                for (int j = 0; j < h; j ++) {
                    if (at(rooms, x + i, z + j) != RoomType.EMPTY) return false;
                }
            }
            return true;
        }

        ICatacombFactory roomFor(RoomType type, Random random) {
            ICatacombFactory[] pool = POOLS[type.ordinal()];
            return pool[random.nextInt(pool.length)];
        }

        BlockPos adjDims(ResourceLocation rl, TemplateManager tm) {
            Template t = tm.getTemplate(rl);
            BlockPos dims = t.getSize();
            return new BlockPos((dims.getX() - 3) / 4, 0, (dims.getZ() - 3) / 4);
        }

        void tryVisit(Queue<BlockPos> q, Map<BlockPos, RoomType> m, BlockPos p, RoomType type) {
            if (!m.containsKey(p)) {
                m.put(p, RoomType.CORRIDOR);
                q.add(p);
            }
        }

        static class Edge {
            public BlockPos start, end;
            public float weight;

            public Edge(BlockPos a, BlockPos b, float weight) {
                this.weight = weight;
                this.start = a;
                this.end = b;
            }

            @Override
            public boolean equals(Object object) {
                return object instanceof Edge
                    && ((Edge)object).start.equals(start)
                    && ((Edge)object).end.equals(end);
            }

            @Override
            public int hashCode() {
                return (480026207 * start.hashCode()) ^ (1914791117 * end.hashCode());
            }
        }

        BlockPos tryPlace(Map<BlockPos, RoomType> rooms, BlockPos desired, int w, int h) {
            for (int xx = 0; xx < w; xx ++) {
                for (int yy = 0; yy < h; yy ++) {
                    boolean can = true;
                    for (int i = 0; i < w; i ++) {
                        for (int j = 0; j < h; j ++) {
                            if (rooms.getOrDefault(desired.add(i, 0, j), RoomType.CORRIDOR) != RoomType.CORRIDOR)
                                can = false;
                        }
                    }
                    if (can) return desired.add(-xx, 0, -yy);
                }
            }
            return null;
        }

        void setRoom(Map<BlockPos, RoomType> rooms, Set<Edge> edges, RoomType type, BlockPos pos, int w, int h) {
            rooms.put(pos, type);
            for (int i = 0; i < w; i ++) {
                for (int j = 0; j < h; j ++) {
                    if (i != 0 || j != 0) rooms.put(pos.add(i, 0, j), RoomType.EMPTY);
                    if (i > 0) {
                        edges.remove(new Edge(pos.add(i, 0, j), pos.add(i - 1, 0, j), 0));
                        edges.remove(new Edge(pos.add(i - 1, 0, j), pos.add(i, 0, j), 0));
                    }
                    if (j > 0) {
                        edges.remove(new Edge(pos.add(i, 0, j), pos.add(i, 0, j - 1), 0));
                        edges.remove(new Edge(pos.add(i, 0, j - 1), pos.add(i, 0, j), 0));
                    }
                }
            }
        }

        void generate(TemplateManager tm, BlockPos pos, Random random, List<StructurePiece> components) {
            // components.add(new CatacombPieces.CorridorCenter(templateManager, blockpos, rand));
            int size = (random.nextInt(3) + 2) * 16 + 3;
            Map<BlockPos, RoomType> rooms = new HashMap<>();
            Deque<BlockPos> frontier = new ArrayDeque<>();
            boolean breadth = false;
            rooms.put(new BlockPos(0, 0, 0), RoomType.CORRIDOR);
            frontier.add(new BlockPos(0, 0, 0));
            while (!frontier.isEmpty() || rooms.size() < size) {
                if (frontier.isEmpty() && rooms.size() < size) break;
                BlockPos next = frontier.removeLast();
                if (random.nextInt(6) == 0) breadth = !breadth;
                RoomType type = rooms.get(next);
                if (rooms.size() < size) {
                    if (random.nextBoolean()) tryVisit(frontier, rooms, next.north(), type);
                    if (random.nextBoolean()) tryVisit(frontier, rooms, next.south(), type);
                    if (random.nextBoolean()) tryVisit(frontier, rooms, next.west(), type);
                    if (random.nextBoolean()) tryVisit(frontier, rooms, next.east(), type);
                }
            }
            Set<Edge> edges = new HashSet<>(), maze = new HashSet<>();
            List<BlockPos> vertices = new ArrayList<>(rooms.keySet());
            Set<BlockPos> visited = new HashSet<>(), remaining = new HashSet<>(rooms.keySet());
            BlockPos start = vertices.get(random.nextInt(vertices.size()));
            visited.add(start);
            remaining.remove(start);
            for (Map.Entry<BlockPos, RoomType> e : rooms.entrySet()) {
                if (rooms.containsKey(e.getKey().north())) {
                    edges.add(new Edge(e.getKey(), e.getKey().north(), random.nextFloat()));
                    edges.add(new Edge(e.getKey().north(), e.getKey(), random.nextFloat()));
                }
                if (rooms.containsKey(e.getKey().south())) {
                    edges.add(new Edge(e.getKey(), e.getKey().south(), random.nextFloat()));
                    edges.add(new Edge(e.getKey().south(), e.getKey(), random.nextFloat()));
                }
                if (rooms.containsKey(e.getKey().east())) {
                    edges.add(new Edge(e.getKey(), e.getKey().east(), random.nextFloat()));
                    edges.add(new Edge(e.getKey().east(), e.getKey(), random.nextFloat()));
                }
                if (rooms.containsKey(e.getKey().west())) {
                    edges.add(new Edge(e.getKey(), e.getKey().west(), random.nextFloat()));
                    edges.add(new Edge(e.getKey().west(), e.getKey(), random.nextFloat()));
                }
            }
            while (visited.size() < vertices.size()) {
                Edge min = null;
                for (Edge e : edges) {
                    if (visited.contains(e.start) && remaining.contains(e.end)) {
                        min = e;
                    }
                }
                if (min == null) break;
                maze.add(min);
                maze.add(new Edge(min.end, min.start, 0));
                visited.add(min.end);
                remaining.remove(min.end);
            }

            for (int i = 0; i < size / 6; i ++) {
                BlockPos place = tryPlace(rooms, vertices.get(random.nextInt(vertices.size())), 2, 2);
                if (place != null) setRoom(rooms, maze, RoomType.MEDIUM_ROOM, place,2, 2);
            }

            for (int i = 0; i < size / 3; i ++) {
                BlockPos place = tryPlace(rooms, vertices.get(random.nextInt(vertices.size())), 1, 1);
                if (place != null) setRoom(rooms, maze, RoomType.SMALL_ROOM, place,1, 1);
            }

            for (Map.Entry<BlockPos, RoomType> e : rooms.entrySet()) {
                ICatacombFactory factory = e.getValue() == RoomType.EMPTY ? null : roomFor(e.getValue(), random);
                BlockPos loc = pos.add(e.getKey().getX() * 4, 0, e.getKey().getZ() * 4);
                switch (e.getValue()) {
                    case SMALL_ROOM:
                        components.add(factory.create(tm, loc, random));
                        break;
                    case MEDIUM_ROOM:
                        components.add(factory.create(tm, loc, random));
                        break;
                    case CORRIDOR:
                        components.add(factory.create(tm, loc, random));
                        break;
                    default:
                        break;
                }
            }

            for (Map.Entry<BlockPos, RoomType> e : rooms.entrySet()) {
                Edge north = new Edge(e.getKey(), e.getKey().north(), 0),
                    south = new Edge(e.getKey(), e.getKey().south(), 0),
                    west = new Edge(e.getKey(), e.getKey().west(), 0),
                    east = new Edge(e.getKey(), e.getKey().east(), 0);
                BlockPos loc = pos.add(e.getKey().getX() * 4, 0, e.getKey().getZ() * 4);
                if (maze.contains(north))
                    components.add(new CatacombPieces.CorridorDoor(tm, loc, Rotation.NONE, random));
                if (maze.contains(west))
                    components.add(new CatacombPieces.CorridorDoor(tm, loc, Rotation.COUNTERCLOCKWISE_90, random));
                if (maze.contains(south))
                    components.add(new CatacombPieces.CorridorDoor(tm, loc, Rotation.CLOCKWISE_180, random));
                if (maze.contains(east))
                    components.add(new CatacombPieces.CorridorDoor(tm, loc, Rotation.CLOCKWISE_90, random));
            }
            this.recalculateStructureSize();
        }
    }
}
