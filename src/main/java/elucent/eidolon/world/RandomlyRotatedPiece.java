package elucent.eidolon.world;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

public class RandomlyRotatedPiece extends BasicPiece {

    public RandomlyRotatedPiece(IStructurePieceType structurePieceTypeIn, CompoundTag nbt) {
        super(structurePieceTypeIn, nbt);
    }

    public RandomlyRotatedPiece(IStructurePieceType type, ResourceLocation key, TemplateManager templateManager, CompoundTag nbt) {
        super(type, key, templateManager, nbt);
    }

    public RandomlyRotatedPiece(IStructurePieceType type, ResourceLocation key, TemplateManager templateManager, BlockPos pos, Random random) {
        super(type, key, templateManager, pos, Rotation.NONE, Mirror.NONE, random);
    }
}
