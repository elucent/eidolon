package elucent.eidolon.ritual;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.network.CrystallizeEffectPacket;
import elucent.eidolon.network.Networking;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SUpdateTimePacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.ServerWorldInfo;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.List;

public class DaylightRitual extends Ritual {
    public static final ResourceLocation SYMBOL = new ResourceLocation(Eidolon.MODID, "particle/daylight_ritual");

    public DaylightRitual() {
        super(SYMBOL, ColorUtil.packColor(255, 255, 245, 130));
    }

    @Override
    public RitualResult tick(World world, BlockPos pos) {
        if (world.getDayTime() < 1000 || world.getDayTime() >= 12000) {
            if (!world.isRemote) {
                ((ServerWorldInfo) world.getWorldInfo()).setDayTime((world.getDayTime() + 100) % 24000);
                for (ServerPlayerEntity player : ((ServerWorld) world).getPlayers()) {
                    player.connection.sendPacket(new SUpdateTimePacket(world.getGameTime(), world.getDayTime(), world.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)));
                }
            }
            return RitualResult.PASS;
        }
        else return RitualResult.TERMINATE;
    }
}
