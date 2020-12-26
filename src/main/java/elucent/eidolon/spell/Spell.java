package elucent.eidolon.spell;

import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class Spell {
    ResourceLocation registryName;
    public Spell(ResourceLocation registryName) {
        this.registryName = registryName;
    }

    public ResourceLocation getRegistryName() {
        return registryName;
    }

    public abstract boolean matches(List<Sign> signs);
    public abstract boolean canCast(World world, BlockPos pos, PlayerEntity player, List<Sign> signs);
    public abstract void cast(World world, BlockPos pos, PlayerEntity player, List<Sign> signs);
}
