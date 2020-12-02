package elucent.eidolon.util;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ChunkUtil {
    public static void load(World world, ChunkPos pos) {
        assert !world.isRemote;
        ((ServerWorld)world).forceChunk(pos.x, pos.z, true);
    }

    public static void unload(World world, ChunkPos pos) {
        assert !world.isRemote;
        ((ServerWorld)world).forceChunk(pos.x, pos.z, false);
    }
}
