package elucent.eidolon.spell;

import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class StaticSpell extends Spell {
    List<Sign> signs;

    public StaticSpell(ResourceLocation name, Sign... signs) {
        super(name);
        this.signs = Arrays.asList(signs);
    }

    @Override
    public boolean matches(List<Sign> signs) {
        if (this.signs.size() != signs.size()) return false;
        for (int i = 0; i < signs.size(); i ++) if (this.signs.get(i) != signs.get(i)) return false;
        return true;
    }

    public abstract boolean canCast(World world, BlockPos pos, PlayerEntity player);

    @Override
    public boolean canCast(World world, BlockPos pos, PlayerEntity player, List<Sign> signs) {
        return canCast(world, pos, player);
    }

    public abstract void cast(World world, BlockPos pos, PlayerEntity player);

    @Override
    public void cast(World world, BlockPos pos, PlayerEntity player, List<Sign> signs) {
        cast(world, pos, player);
    }
}
