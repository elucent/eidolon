package elucent.eidolon.world;

import java.util.Random;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

public class BasicPiece extends TemplateStructurePiece {
    private ResourceLocation loc;
    Rotation rotation;
    Mirror mirror;

    void modifyPlacement(StructurePlaceSettings settings, BlockPos pos) {
        //
    }

    void init(BlockPos pos, Random random) {
        //
    }
    
    public BasicPiece(StructurePieceType structurePieceTypeIn, CompoundTag nbt, StructureManager manager) {
        super(structurePieceTypeIn, nbt, manager, (rl) -> {
            return new StructurePlaceSettings().setRotation(Rotation.valueOf(nbt.getString("rot"))).setMirror(Mirror.valueOf(nbt.getString("mirror"))).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        });
        this.loc = new ResourceLocation(nbt.getString("Template"));
        this.rotation = Rotation.valueOf(nbt.getString("rot"));
        this.mirror = Mirror.valueOf(nbt.getString("mirror"));
    }

    public BasicPiece(StructurePieceType type, ResourceLocation key, StructureManager templateManager, BlockPos pos, Random random) {
        this(type, key, templateManager, pos, Rotation.NONE, Mirror.NONE, random);
    }

    public BasicPiece(StructurePieceType type, ResourceLocation key, StructureManager templateManager, BlockPos pos, Rotation rot, Mirror mirror, Random random) {
        super(type, 0, templateManager, key, "eidolon.structure." + key.getPath(),
        		new StructurePlaceSettings().setRotation(rot).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK), pos);
        this.templatePosition = pos;
        this.loc = key;
        this.rotation = rot;
        this.mirror = mirror;

        this.template = templateManager.getOrCreate(key);
        Vec3i d = template.getSize();
        BlockPos o = new BlockPos(-((d.getX() - 1) / 2), 0, -(d.getZ() - 1) / 2);
        templatePosition = templatePosition.offset(o.rotate(this.rotation)).subtract(o);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tagCompound) {
        super.addAdditionalSaveData(ctx, tagCompound);
        tagCompound.putString("rot", rotation.name());
        tagCompound.putString("mirror", mirror.name());
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, ServerLevelAccessor worldIn, Random rand, BoundingBox sbb) {}
}
