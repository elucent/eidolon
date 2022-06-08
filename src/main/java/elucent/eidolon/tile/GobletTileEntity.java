package elucent.eidolon.tile;

import elucent.eidolon.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class GobletTileEntity extends TileEntityBase {
    EntityType type = null;

    public GobletTileEntity() {
        super(Registry.GOBLET_TILE_ENTITY);
    }

    public EntityType getEntityType() {
        return type;
    }

    public void setEntityType(EntityType type) {
        this.type = type;
        sync();
    }

    @Override
    public void load(BlockState state, CompoundTag tag) {
        super.load(state, tag);
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
