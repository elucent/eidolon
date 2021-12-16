package elucent.eidolon.tile;

import elucent.eidolon.Registry;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class GobletTileEntity extends TileEntityBase {
    EntityType type = null;

    public GobletTileEntity(BlockPos pos, BlockState state) {
        super(Registry.GOBLET_TILE_ENTITY, pos, state);
    }

    public EntityType getEntityType() {
        return type;
    }

    public void setEntityType(EntityType type) {
        this.type = type;
        sync();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("type")) type = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(tag.getString("type")));
        else type = null;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag = super.save(tag);
        if (type != null) tag.putString("type", type.getRegistryName().toString());
        return tag;
    }
}
