package elucent.eidolon.ritual;

import elucent.eidolon.Eidolon;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ServerLevelData;

public class MoonlightRitual extends Ritual {
    public static final ResourceLocation SYMBOL = new ResourceLocation(Eidolon.MODID, "particle/moonlight_ritual");

    public MoonlightRitual() {
        super(SYMBOL, ColorUtil.packColor(255, 111, 75, 189));
    }

    @Override
    public RitualResult tick(Level world, BlockPos pos) {
        if (world.getDayTime() % 24000 < 13000 && world.getDayTime() % 24000 >= 0) {
            if (!world.isClientSide) {
                ((ServerLevelData) world.getLevelData()).setDayTime(world.getDayTime() + 100);
                for (ServerPlayer player : ((ServerLevel) world).players()) {
                    player.connection.send(new ClientboundSetTimePacket(world.getGameTime(), world.getDayTime(), world.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)));
                }
            }
            return RitualResult.PASS;
        }
        else return RitualResult.TERMINATE;
    }
}
