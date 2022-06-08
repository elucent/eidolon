package elucent.eidolon.proxy;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ServerProxy implements ISidedProxy {
    @Override
    public Player getPlayer() {
        return null;
    }

    @Override
    public Level getWorld() {
        return null;
    }

    @Override
    public void init() {
        //
    }

    @Override
    public void openCodexGui() {
        //
    }
}
