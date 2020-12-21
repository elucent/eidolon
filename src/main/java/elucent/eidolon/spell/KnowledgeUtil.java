package elucent.eidolon.spell;

import elucent.eidolon.capability.KnowledgeProvider;
import elucent.eidolon.network.KnowledgeUpdatePacket;
import elucent.eidolon.network.Networking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.network.play.server.SUpdateTimePacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;

public class KnowledgeUtil {
    public static void grantSign(Entity entity, Sign sign) {
        if (!(entity instanceof PlayerEntity)) return;
        entity.getCapability(KnowledgeProvider.CAPABILITY, null).ifPresent((k) -> {
            if (k.knowsSign(sign)) return;
            k.addSign(sign);

            ((ServerPlayerEntity)entity).connection.sendPacket(new STitlePacket(STitlePacket.Type.ACTIONBAR, new TranslationTextComponent("eidolon.title.new_sign", new TranslationTextComponent(sign.key.getNamespace() + ".sign." + sign.key.getPath()))));
            Networking.sendTo((PlayerEntity)entity, new KnowledgeUpdatePacket((PlayerEntity)entity, true));
        });
    }

    public static void grantFact(Entity entity, ResourceLocation fact) {
        if (!(entity instanceof PlayerEntity)) return;
        entity.getCapability(KnowledgeProvider.CAPABILITY, null).ifPresent((k) -> {
            if (k.knowsFact(fact)) return;
            k.addFact(fact);

            ((ServerPlayerEntity)entity).connection.sendPacket(new STitlePacket(STitlePacket.Type.ACTIONBAR, new TranslationTextComponent("eidolon.title.new_fact")));
            Networking.sendTo((PlayerEntity)entity, new KnowledgeUpdatePacket((PlayerEntity)entity, true));
        });
    }

    public static boolean knowsSign(PlayerEntity player, Sign sign) {
        if (player.getCapability(KnowledgeProvider.CAPABILITY).isPresent()) {
            return player.getCapability(KnowledgeProvider.CAPABILITY).resolve().get().knowsSign(sign);
        }
        return false;
    }

    public static boolean knowsFact(PlayerEntity player, ResourceLocation fact) {
        if (player.getCapability(KnowledgeProvider.CAPABILITY).isPresent()) {
            return player.getCapability(KnowledgeProvider.CAPABILITY).resolve().get().knowsFact(fact);
        }
        return false;
    }
}
