package elucent.eidolon.ritual;

import elucent.eidolon.Eidolon;
import elucent.eidolon.network.Networking;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.block.BedBlock;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SUpdateTimePacket;
import net.minecraft.tileentity.BedTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.ServerWorldInfo;

public class MoonlightRitual extends Ritual {
    public static final ResourceLocation SYMBOL = new ResourceLocation(Eidolon.MODID, "particle/moonlight_ritual");

    public MoonlightRitual() {
        super(SYMBOL, ColorUtil.packColor(255, 111, 75, 189));
    }

    @Override
    public RitualResult tick(World world, BlockPos pos) {
        if (world.getDayTime() % 24000 < 13000 && world.getDayTime() % 24000 >= 0) {
            if (!world.isRemote) {
                ((ServerWorldInfo) world.getWorldInfo()).setDayTime(world.getDayTime() + 100);
                for (ServerPlayerEntity player : ((ServerWorld) world).getPlayers()) {
                    player.connection.sendPacket(new SUpdateTimePacket(world.getGameTime(), world.getDayTime(), world.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)));
                }
            }
            return RitualResult.PASS;
        }
        else return RitualResult.TERMINATE;
    }
}
