package elucent.eidolon.world;

import java.util.Random;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

public class RandomlyRotatedPiece extends BasicPiece {

    public RandomlyRotatedPiece(StructurePieceType structurePieceTypeIn, CompoundTag nbt, StructureManager manager) {
        super(structurePieceTypeIn, nbt, manager);
    }

    public RandomlyRotatedPiece(StructurePieceType type, ResourceLocation key, StructureManager templateManager, BlockPos pos, Random random) {
        super(type, key, templateManager, pos, Rotation.NONE, Mirror.NONE, random);
    }
}
