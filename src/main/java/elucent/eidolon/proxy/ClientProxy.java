package elucent.eidolon.proxy;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientProxy implements ISidedProxy {
    @Override
    public PlayerEntity getPlayer() {
        return Minecraft.getInstance().player;
    }

    @Override
    public World getWorld() {
        return Minecraft.getInstance().world;
    }

    @Override
    public void init() {
        Registry.clientInit();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(Eidolon::clientSetup);
    }
}
