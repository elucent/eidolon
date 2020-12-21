package elucent.eidolon.proxy;

import elucent.eidolon.Registry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ServerProxy implements ISidedProxy {
    @Override
    public PlayerEntity getPlayer() {
        return null;
    }

    @Override
    public World getWorld() {
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
