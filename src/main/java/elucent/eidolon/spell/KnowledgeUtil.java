package elucent.eidolon.spell;

import elucent.eidolon.capability.IKnowledge;
import elucent.eidolon.network.KnowledgeUpdatePacket;
import elucent.eidolon.network.Networking;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TranslatableComponent;

public class KnowledgeUtil {
    public static void grantSign(Entity entity, Sign sign) {
        if (!(entity instanceof Player)) return;
        entity.getCapability(IKnowledge.INSTANCE, null).ifPresent((k) -> {
            if (k.knowsSign(sign)) return;
            k.addSign(sign);

            ((ServerPlayer)entity).connection.send(new ClientboundSetActionBarTextPacket(new TranslatableComponent("eidolon.title.new_sign", new TranslatableComponent(sign.key.getNamespace() + ".sign." + sign.key.getPath()))));
            Networking.sendTo((Player)entity, new KnowledgeUpdatePacket((Player)entity, true));
        });
    }

    public static void grantFact(Entity entity, ResourceLocation fact) {
        if (!(entity instanceof Player)) return;
        entity.getCapability(IKnowledge.INSTANCE, null).ifPresent((k) -> {
            if (k.knowsFact(fact)) return;
            k.addFact(fact);

            ((ServerPlayer)entity).connection.send(new ClientboundSetActionBarTextPacket(new TranslatableComponent("eidolon.title.new_fact")));
            Networking.sendTo((Player)entity, new KnowledgeUpdatePacket((Player)entity, true));
        });
    }

    public static boolean knowsSign(Player player, Sign sign) {
        if (player.getCapability(IKnowledge.INSTANCE).isPresent()) {
            return player.getCapability(IKnowledge.INSTANCE).resolve().get().knowsSign(sign);
        }
        return false;
    }

    public static boolean knowsFact(Player player, ResourceLocation fact) {
        if (player.getCapability(IKnowledge.INSTANCE).isPresent()) {
            return player.getCapability(IKnowledge.INSTANCE).resolve().get().knowsFact(fact);
        }
        return false;
    }
}
