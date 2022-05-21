package elucent.eidolon.network;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import elucent.eidolon.capability.IKnowledge;
import elucent.eidolon.entity.ChantCasterEntity;
import elucent.eidolon.gui.ResearchTableContainer;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.spell.Signs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraft.world.level.Level;

public class ResearchActionPacket {
	public static enum Action {
		SUBMIT_GOAL, STAMP
	}
	Action action;
	int index;

    public ResearchActionPacket(Action action, int index) {
        this.action = action;
        this.index = index;
    }

    public ResearchActionPacket(Action action) {
        this(action, 0);
    }

    public static void encode(ResearchActionPacket object, FriendlyByteBuf buffer) {
        buffer.writeInt(object.action.ordinal());
        buffer.writeInt(object.index);
    }

    public static ResearchActionPacket decode(FriendlyByteBuf buffer) {
        Action action = Action.values()[buffer.readInt()];
        int index = buffer.readInt();
        return new ResearchActionPacket(action, index);
    }

    public static void consume(ResearchActionPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER;

            ServerPlayer player = ctx.get().getSender();
            AbstractContainerMenu menu = player.containerMenu;
            Level world = ctx.get().getSender().level;
            if (world != null && menu instanceof ResearchTableContainer rc) {
                if (packet.action == Action.SUBMIT_GOAL) {
                	rc.trySubmitGoal(player, packet.index);
                }
                else if (packet.action == Action.STAMP) {
                	rc.tryStamp(player);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
