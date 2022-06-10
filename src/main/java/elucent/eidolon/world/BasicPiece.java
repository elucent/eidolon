package elucent.eidolon.world;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

import java.util.Random;

public class BasicPiece extends TemplateStructurePiece {
    private ResourceLocation loc;
    Rotation rotation;
    Mirror mirror;

    public BasicPiece(StructurePieceType type, ResourceLocation key, StructureManager templateManager, CompoundTag nbt) {
        super(type, nbt, templateManager, (r) -> (new StructurePlaceSettings()
                .setRotation(Rotation.valueOf(nbt.getString("rot")))
                .setMirror(Mirror.valueOf(nbt.getString("mirror"))))
                .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK));

        this.loc = new ResourceLocation(nbt.getString("template"));
        this.rotation = Rotation.valueOf(nbt.getString("rot"));
        this.mirror = Mirror.valueOf(nbt.getString("mirror"));

        var part = templateManager.getOrCreate(key);

        //this.setup(part, this.templatePosition, placement);
        this.template = templateManager.get(key).get();
    }

    public BasicPiece(StructurePieceType type, ResourceLocation key, StructureManager templateManager, BlockPos pos, Random random) {
        this(type, key, templateManager, pos, Rotation.NONE, Mirror.NONE, random);
    }

    public BasicPiece(StructurePieceType type, ResourceLocation key, StructureManager templateManager, BlockPos pos, Rotation rot, Mirror mirror, Random random) {
        super(type, 0, templateManager, key, key.toString(), new StructurePlaceSettings()
                .setRotation(rot)
                .setMirror(mirror)
                .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK), pos);
        this.templatePosition = pos;
        this.loc = key;
        this.rotation = rot;
        this.mirror = mirror;

        var part = templateManager.getOrCreate(key);
        var d = part.getSize();
        BlockPos o = new BlockPos(-((d.getX() - 1) / 2), 0, -(d.getZ() - 1) / 2);
        templatePosition = templatePosition.offset(o.rotate(this.rotation)).subtract(o);

        // this.setup(part, this.templatePosition, placement);
        this.template = templateManager.get(key).get();
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext pContext, CompoundTag tagCompound) {
        super.addAdditionalSaveData(pContext, tagCompound);
        tagCompound.putString("template", loc.toString());
        tagCompound.putString("rot", rotation.name());
        tagCompound.putString("mirror", mirror.name());
    }

    @Override
    protected void handleDataMarker(String pMarker, BlockPos pPos, ServerLevelAccessor pLevel, Random pRandom, BoundingBox pBox) {

    }
}
