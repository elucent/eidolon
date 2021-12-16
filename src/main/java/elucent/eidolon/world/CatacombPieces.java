package elucent.eidolon.world;

import java.util.Random;

import elucent.eidolon.Eidolon;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

public class CatacombPieces {
    public static final ResourceLocation
        CORRIDOR_CENTER_ID = new ResourceLocation(Eidolon.MODID, "catacomb_corridor_center"),
        CORRIDOR_DOOR_ID = new ResourceLocation(Eidolon.MODID, "catacomb_corridor_door"),
        SMALL_ROOM_ID = new ResourceLocation(Eidolon.MODID, "catacomb_room_small"),
        TRAP_ID = new ResourceLocation(Eidolon.MODID, "catacomb_trap"),
        SHRINE_ID = new ResourceLocation(Eidolon.MODID, "catacomb_shrine"),
        SKULL_ID = new ResourceLocation(Eidolon.MODID, "catacomb_skull"),
        SPAWNER_ID = new ResourceLocation(Eidolon.MODID, "catacomb_spawner"),
        COFFIN_ID = new ResourceLocation(Eidolon.MODID, "catacomb_coffin"),
        MEDIUM_ROOM_ID = new ResourceLocation(Eidolon.MODID, "catacomb_room_medium"),
        GRAVEYARD_ID = new ResourceLocation(Eidolon.MODID, "catacomb_graveyard"),
        TURNAROUND_ID = new ResourceLocation(Eidolon.MODID, "catacomb_turnaround"),
        LAB_ID = new ResourceLocation(Eidolon.MODID, "catacomb_lab");

    public static StructurePieceType
        CORRIDOR_CENTER = null, CORRIDOR_DOOR = null,
        SMALL_ROOM = null, TRAP = null, SHRINE = null, SKULL = null, SPAWNER = null,
        COFFIN = null, MEDIUM_ROOM = null, GRAVEYARD = null, TURNAROUND = null, LAB = null;

    public static class CorridorCenter extends RandomlyRotatedPiece {
        public CorridorCenter(StructureManager manager, CompoundTag nbt) { super(CORRIDOR_CENTER, nbt, manager); }
        public CorridorCenter(StructureManager templateManager, BlockPos pos, Random random) { super(CORRIDOR_CENTER, CORRIDOR_CENTER_ID, templateManager, pos, random); }
    }

    public static class CorridorDoor extends BasicPiece {
        public CorridorDoor(StructureManager manager, CompoundTag nbt) { super(CORRIDOR_DOOR, nbt, manager); }
        public CorridorDoor(StructureManager templateManager, BlockPos pos, Rotation rot, Random random) {
            super(CORRIDOR_DOOR, CORRIDOR_DOOR_ID, templateManager, pos, rot, Mirror.NONE, random);
        }
    }

    public static class SmallRoom extends RandomlyRotatedPiece {
        public SmallRoom(StructureManager manager, CompoundTag nbt) { super(SMALL_ROOM, nbt, manager); }
        public SmallRoom(StructureManager templateManager, BlockPos pos, Random random) { super(SMALL_ROOM, SMALL_ROOM_ID, templateManager, pos, random); }
    }

    public static class Trap extends RandomlyRotatedPiece {
        public Trap(StructureManager manager, CompoundTag nbt) { super(TRAP, nbt, manager); }
        public Trap(StructureManager templateManager, BlockPos pos, Random random) { super(TRAP, TRAP_ID, templateManager, pos, random); }
    }

    public static class Shrine extends RandomlyRotatedPiece {
        public Shrine(StructureManager manager, CompoundTag nbt) { super(SHRINE, nbt, manager); }
        public Shrine(StructureManager templateManager, BlockPos pos, Random random) { super(SHRINE, SHRINE_ID, templateManager, pos, random); }
    }

    public static class Skull extends RandomlyRotatedPiece {
        public Skull(StructureManager manager, CompoundTag nbt) { super(SKULL, nbt, manager); }
        public Skull(StructureManager templateManager, BlockPos pos, Random random) { super(SKULL, SKULL_ID, templateManager, pos, random); }
    }

    public static class Spawner extends RandomlyRotatedPiece {
        public Spawner(StructureManager manager, CompoundTag nbt) { super(SPAWNER, nbt, manager); }
        public Spawner(StructureManager templateManager, BlockPos pos, Random random) { super(SPAWNER, SPAWNER_ID, templateManager, pos, random); }
    }

    public static class Coffin extends RandomlyRotatedPiece {
        public Coffin(StructureManager manager, CompoundTag nbt) { super(COFFIN, nbt, manager); }
        public Coffin(StructureManager templateManager, BlockPos pos, Random random) { super(COFFIN, COFFIN_ID, templateManager, pos, random); }
    }

    public static class MediumRoom extends RandomlyRotatedPiece {
        public MediumRoom(StructureManager manager, CompoundTag nbt) { super(MEDIUM_ROOM, nbt, manager); }
        public MediumRoom(StructureManager templateManager, BlockPos pos, Random random) { super(MEDIUM_ROOM, MEDIUM_ROOM_ID, templateManager, pos, random); }
    }

    public static class Graveyard extends RandomlyRotatedPiece {
        public Graveyard(StructureManager manager, CompoundTag nbt) { super(GRAVEYARD, nbt, manager); }
        public Graveyard(StructureManager templateManager, BlockPos pos, Random random) { super(GRAVEYARD, GRAVEYARD_ID, templateManager, pos, random); }
    }

    public static class Turnaround extends RandomlyRotatedPiece {
        public Turnaround(StructureManager manager, CompoundTag nbt) { super(TURNAROUND, nbt, manager); }
        public Turnaround(StructureManager templateManager, BlockPos pos, Random random) { super(TURNAROUND, TURNAROUND_ID, templateManager, pos, random); }
    }

    public static class Lab extends RandomlyRotatedPiece {
        public Lab(StructureManager manager, CompoundTag nbt) { super(LAB, nbt, manager); }
        public Lab(StructureManager templateManager, BlockPos pos, Random random) { super(LAB, LAB_ID, templateManager, pos, random); }
    }
}
