package elucent.eidolon.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public interface ISidedProxy {
    PlayerEntity getPlayer();
    World getWorld();
    void init();

    void openCodexGui();
}
