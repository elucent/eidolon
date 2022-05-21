package elucent.eidolon.spell;

import java.util.Arrays;
import java.util.List;

import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public abstract class StaticSpell extends Spell {
    SignSequence signs;

    public StaticSpell(ResourceLocation name, Sign... signs) {
        super(name);
        this.signs = new SignSequence(signs);
    }

    @Override
    public boolean matches(SignSequence signs) {
        return this.signs.equals(signs);
    }

    public abstract boolean canCast(Level world, BlockPos pos, Player player);

    @Override
    public boolean canCast(Level world, BlockPos pos, Player player, SignSequence signs) {
        return canCast(world, pos, player);
    }

    public abstract void cast(Level world, BlockPos pos, Player player);

    @Override
    public void cast(Level world, BlockPos pos, Player player, SignSequence signs) {
        cast(world, pos, player);
    }
}
