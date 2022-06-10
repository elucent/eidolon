package elucent.eidolon.spell;

import elucent.eidolon.capability.KnowledgeProvider;
import elucent.eidolon.network.KnowledgeUpdatePacket;
import elucent.eidolon.network.Networking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class KnowledgeUtil {
    public static void grantSign(Entity entity, Sign sign) {
        if (!(entity instanceof Player)) return;
        entity.getCapability(KnowledgeProvider.CAPABILITY, null).ifPresent((k) -> {
            if (k.knowsSign(sign)) return;
            k.addSign(sign);

            // todo ((ServerPlayer)entity).connection.send(new STitlePacket(STitlePacket.Type.ACTIONBAR, new TranslatableComponent("eidolon.title.new_sign", new TranslatableComponent(sign.key.getNamespace() + ".sign." + sign.key.getPath()))));
            Networking.sendTo((Player)entity, new KnowledgeUpdatePacket((Player)entity, true));
        });
    }

    public static void grantFact(Entity entity, ResourceLocation fact) {
        if (!(entity instanceof Player)) return;
        entity.getCapability(KnowledgeProvider.CAPABILITY, null).ifPresent((k) -> {
            if (k.knowsFact(fact)) return;
            k.addFact(fact);

            // todo ((ServerPlayer)entity).connection.send(new STitlePacket(STitlePacket.Type.ACTIONBAR, new TranslatableComponent("eidolon.title.new_fact")));
            Networking.sendTo((Player)entity, new KnowledgeUpdatePacket((Player)entity, true));
        });
    }

    public static boolean knowsSign(Player player, Sign sign) {
        if (player.getCapability(KnowledgeProvider.CAPABILITY).isPresent()) {
            return player.getCapability(KnowledgeProvider.CAPABILITY).resolve().get().knowsSign(sign);
        }
        return false;
    }

    public static boolean knowsFact(Player player, ResourceLocation fact) {
        if (player.getCapability(KnowledgeProvider.CAPABILITY).isPresent()) {
            return player.getCapability(KnowledgeProvider.CAPABILITY).resolve().get().knowsFact(fact);
        }
        return false;
    }
}
