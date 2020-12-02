package elucent.eidolon.spell;

import elucent.eidolon.capability.KnowledgeProvider;
import elucent.eidolon.network.KnowledgeUpdatePacket;
import elucent.eidolon.network.Networking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.network.play.server.SUpdateTimePacket;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;

public class SignUtil {
    public static void grantSign(Entity entity, Sign sign) {
        if (!(entity instanceof PlayerEntity)) return;
        entity.getCapability(KnowledgeProvider.CAPABILITY, null).ifPresent((k) -> {
            if (k.knowsSign(sign)) return;
            k.addSign(sign);

            ((ServerPlayerEntity)entity).connection.sendPacket(new STitlePacket(STitlePacket.Type.TITLE, new TranslationTextComponent("eidolon.title.new_sign", new TranslationTextComponent(sign.key.getNamespace() + ".sign." + sign.key.getPath()))));
            Networking.sendTo((PlayerEntity)entity, new KnowledgeUpdatePacket((PlayerEntity)entity, true));
        });
    }
}
