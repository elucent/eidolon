package elucent.eidolon.world;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import java.util.Random;

public class RandomlyRotatedPiece extends BasicPiece {

    public RandomlyRotatedPiece(StructurePieceType type, ResourceLocation key, StructureManager StructureManager, CompoundTag nbt) {
        super(type, key, StructureManager, nbt);
    }

    public RandomlyRotatedPiece(StructurePieceType type, ResourceLocation key, StructureManager StructureManager, BlockPos pos, Random random) {
        super(type, key, StructureManager, pos, Rotation.NONE, Mirror.NONE, random);
    }
}
