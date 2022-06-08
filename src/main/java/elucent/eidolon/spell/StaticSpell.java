package elucent.eidolon.spell;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.List;

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

    public abstract boolean canCast(Level world, BlockPos pos, Player player);

    @Override
    public boolean canCast(Level world, BlockPos pos, Player player, List<Sign> signs) {
        return canCast(world, pos, player);
    }

    public abstract void cast(Level world, BlockPos pos, Player player);

    @Override
    public void cast(Level world, BlockPos pos, Player player, List<Sign> signs) {
        cast(world, pos, player);
    }
}
