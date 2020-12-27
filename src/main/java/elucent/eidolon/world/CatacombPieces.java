package elucent.eidolon.world;

import elucent.eidolon.Eidolon;
import elucent.eidolon.WorldGen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

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

    public static IStructurePieceType
        CORRIDOR_CENTER = null, CORRIDOR_DOOR = null,
        SMALL_ROOM = null, TRAP = null, SHRINE = null, SKULL = null, SPAWNER = null,
        COFFIN = null, MEDIUM_ROOM = null, GRAVEYARD = null, TURNAROUND = null, LAB = null;

    public static class CorridorCenter extends RandomlyRotatedPiece {
        public CorridorCenter(TemplateManager templateManager, CompoundNBT nbt) { super(CORRIDOR_CENTER, CORRIDOR_CENTER_ID, templateManager, nbt); }
        public CorridorCenter(TemplateManager templateManager, BlockPos pos, Random random) { super(CORRIDOR_CENTER, CORRIDOR_CENTER_ID, templateManager, pos, random); }
    }

    public static class CorridorDoor extends BasicPiece {
        public CorridorDoor(TemplateManager templateManager, CompoundNBT nbt) { super(CORRIDOR_DOOR, CORRIDOR_DOOR_ID, templateManager, nbt); }
        public CorridorDoor(TemplateManager templateManager, BlockPos pos, Rotation rot, Random random) {
            super(CORRIDOR_DOOR, CORRIDOR_DOOR_ID, templateManager, pos, rot, Mirror.NONE, random);
        }
    }

    public static class SmallRoom extends RandomlyRotatedPiece {
        public SmallRoom(TemplateManager templateManager, CompoundNBT nbt) { super(SMALL_ROOM, SMALL_ROOM_ID, templateManager, nbt); }
        public SmallRoom(TemplateManager templateManager, BlockPos pos, Random random) { super(SMALL_ROOM, SMALL_ROOM_ID, templateManager, pos, random); }
    }

    public static class Trap extends RandomlyRotatedPiece {
        public Trap(TemplateManager templateManager, CompoundNBT nbt) { super(TRAP, TRAP_ID, templateManager, nbt); }
        public Trap(TemplateManager templateManager, BlockPos pos, Random random) { super(TRAP, TRAP_ID, templateManager, pos, random); }
    }

    public static class Shrine extends RandomlyRotatedPiece {
        public Shrine(TemplateManager templateManager, CompoundNBT nbt) { super(SHRINE, SHRINE_ID, templateManager, nbt); }
        public Shrine(TemplateManager templateManager, BlockPos pos, Random random) { super(SHRINE, SHRINE_ID, templateManager, pos, random); }
    }

    public static class Skull extends RandomlyRotatedPiece {
        public Skull(TemplateManager templateManager, CompoundNBT nbt) { super(SKULL, SKULL_ID, templateManager, nbt); }
        public Skull(TemplateManager templateManager, BlockPos pos, Random random) { super(SKULL, SKULL_ID, templateManager, pos, random); }
    }

    public static class Spawner extends RandomlyRotatedPiece {
        public Spawner(TemplateManager templateManager, CompoundNBT nbt) { super(SPAWNER, SPAWNER_ID, templateManager, nbt); }
        public Spawner(TemplateManager templateManager, BlockPos pos, Random random) { super(SPAWNER, SPAWNER_ID, templateManager, pos, random); }
    }

    public static class Coffin extends RandomlyRotatedPiece {
        public Coffin(TemplateManager templateManager, CompoundNBT nbt) { super(COFFIN, COFFIN_ID, templateManager, nbt); }
        public Coffin(TemplateManager templateManager, BlockPos pos, Random random) { super(COFFIN, COFFIN_ID, templateManager, pos, random); }
    }

    public static class MediumRoom extends RandomlyRotatedPiece {
        public MediumRoom(TemplateManager templateManager, CompoundNBT nbt) { super(MEDIUM_ROOM, MEDIUM_ROOM_ID, templateManager, nbt); }
        public MediumRoom(TemplateManager templateManager, BlockPos pos, Random random) { super(MEDIUM_ROOM, MEDIUM_ROOM_ID, templateManager, pos, random); }
    }

    public static class Graveyard extends RandomlyRotatedPiece {
        public Graveyard(TemplateManager templateManager, CompoundNBT nbt) { super(GRAVEYARD, GRAVEYARD_ID, templateManager, nbt); }
        public Graveyard(TemplateManager templateManager, BlockPos pos, Random random) { super(GRAVEYARD, GRAVEYARD_ID, templateManager, pos, random); }
    }

    public static class Turnaround extends RandomlyRotatedPiece {
        public Turnaround(TemplateManager templateManager, CompoundNBT nbt) { super(TURNAROUND, TURNAROUND_ID, templateManager, nbt); }
        public Turnaround(TemplateManager templateManager, BlockPos pos, Random random) { super(TURNAROUND, TURNAROUND_ID, templateManager, pos, random); }
    }

    public static class Lab extends RandomlyRotatedPiece {
        public Lab(TemplateManager templateManager, CompoundNBT nbt) { super(LAB, LAB_ID, templateManager, nbt); }
        public Lab(TemplateManager templateManager, BlockPos pos, Random random) { super(LAB, LAB_ID, templateManager, pos, random); }
    }
}
