package elucent.eidolon.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

public class ChunkUtil {
    public static void load(Level world, ChunkPos pos) {
        assert !world.isClientSide;
        ((ServerLevel)world).setChunkForced(pos.x, pos.z, true);
    }

    public static void unload(Level world, ChunkPos pos) {
        assert !world.isClientSide;
        ((ServerLevel)world).setChunkForced(pos.x, pos.z, false);
    }
}
