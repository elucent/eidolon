package elucent.eidolon.tile;

import elucent.eidolon.Registry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
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
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        if (tag.contains("type")) type = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(tag.getString("type")));
        else type = null;
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        if (type != null) tag.putString("type", type.getRegistryName().toString());
        return tag;
    }
}
