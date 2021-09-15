package elucent.eidolon.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

public class RandomlyRotatedPiece extends BasicPiece {

    public RandomlyRotatedPiece(IStructurePieceType structurePieceTypeIn, CompoundNBT nbt) {
        super(structurePieceTypeIn, nbt);
    }

    public RandomlyRotatedPiece(IStructurePieceType type, ResourceLocation key, TemplateManager templateManager, CompoundNBT nbt) {
        super(type, key, templateManager, nbt);
    }

    public RandomlyRotatedPiece(IStructurePieceType type, ResourceLocation key, TemplateManager templateManager, BlockPos pos, Random random) {
        super(type, key, templateManager, pos, Rotation.NONE, Mirror.NONE, random);
    }
}
