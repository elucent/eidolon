package elucent.eidolon.util;

import elucent.eidolon.capability.IKnowledge;
import elucent.eidolon.network.KnowledgeUpdatePacket;
import elucent.eidolon.network.Networking;
import elucent.eidolon.research.Researches;
import elucent.eidolon.spell.Rune;
import elucent.eidolon.spell.Sign;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;

public class KnowledgeUtil {
    public static void grantSign(Entity entity, Sign sign) {
        if (!(entity instanceof ServerPlayer)) return;
        entity.getCapability(IKnowledge.INSTANCE, null).ifPresent((k) -> {
            if (k.knowsSign(sign)) return;
            k.addSign(sign);

            ((ServerPlayer)entity).connection.send(new ClientboundSetActionBarTextPacket(new TranslatableComponent("eidolon.title.new_sign", new TranslatableComponent(sign.getRegistryName().getNamespace() + ".sign." + sign.getRegistryName().getPath()))));
            Networking.sendTo((Player)entity, new KnowledgeUpdatePacket((Player)entity, true));
        });
    }

    public static void grantFact(Entity entity, ResourceLocation fact) {
        if (!(entity instanceof ServerPlayer)) return;
        entity.getCapability(IKnowledge.INSTANCE, null).ifPresent((k) -> {
            if (k.knowsFact(fact)) return;
            k.addFact(fact);

            ((ServerPlayer)entity).connection.send(new ClientboundSetActionBarTextPacket(new TranslatableComponent("eidolon.title.new_fact")));
            Networking.sendTo((Player)entity, new KnowledgeUpdatePacket((Player)entity, true));
        });
    }

    public static void grantResearch(Entity entity, ResourceLocation research) {
        if (!(entity instanceof ServerPlayer)) return;
        entity.getCapability(IKnowledge.INSTANCE, null).ifPresent((k) -> {
            if (k.knowsResearch(research)) return;
            k.addResearch(research);

            ((ServerPlayer)entity).connection.send(new ClientboundSetActionBarTextPacket(new TranslatableComponent("eidolon.title.new_research", ChatFormatting.GOLD + Researches.find(research).getName())));
            Networking.sendTo((Player)entity, new KnowledgeUpdatePacket((Player)entity, true));
        });
    }
    
    public static void grantRune(Entity entity, Rune rune) {
        if (!(entity instanceof ServerPlayer)) return;
        entity.getCapability(IKnowledge.INSTANCE, null).ifPresent((k) -> {
            if (k.knowsRune(rune)) return;
            k.addRune(rune);

            ((ServerPlayer)entity).connection.send(new ClientboundSetActionBarTextPacket(new TranslatableComponent("eidolon.title.new_rune", new TranslatableComponent(rune.getRegistryName().getNamespace() + ".rune." + rune.getRegistryName().getPath()))));
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

    public static boolean knowsResearch(Player player, ResourceLocation research) {
        if (player.getCapability(IKnowledge.INSTANCE).isPresent()) {
            return player.getCapability(IKnowledge.INSTANCE).resolve().get().knowsResearch(research);
        }
        return false;
    }

    public static boolean knowsRune(Player player, Rune rune) {
        if (player.getCapability(IKnowledge.INSTANCE).isPresent()) {
            return player.getCapability(IKnowledge.INSTANCE).resolve().get().knowsRune(rune);
        }
        return false;
    }

    public static void removeSign(Entity entity, Sign sign) {
        if (!(entity instanceof ServerPlayer)) return;
        entity.getCapability(IKnowledge.INSTANCE, null).ifPresent((k) -> {
            if (!k.knowsSign(sign)) return;
            k.removeSign(sign);
            Networking.sendTo((Player)entity, new KnowledgeUpdatePacket((Player)entity, true));
        });
    }

    public static void removeFact(Entity entity, ResourceLocation fact) {
        if (!(entity instanceof ServerPlayer)) return;
        entity.getCapability(IKnowledge.INSTANCE, null).ifPresent((k) -> {
            if (!k.knowsFact(fact)) return;
            k.removeFact(fact);
            Networking.sendTo((Player)entity, new KnowledgeUpdatePacket((Player)entity, true));
        });
    }

    public static void removeResearch(Entity entity, ResourceLocation research) {
        if (!(entity instanceof ServerPlayer)) return;
        entity.getCapability(IKnowledge.INSTANCE, null).ifPresent((k) -> {
            if (!k.knowsResearch(research)) return;
            k.removeResearch(research);
            Networking.sendTo((Player)entity, new KnowledgeUpdatePacket((Player)entity, true));
        });
    }
    
    public static void removeRune(Entity entity, Rune rune) {
        if (!(entity instanceof ServerPlayer)) return;
        entity.getCapability(IKnowledge.INSTANCE, null).ifPresent((k) -> {
            if (!k.knowsRune(rune)) return;
            k.removeRune(rune);
            Networking.sendTo((Player)entity, new KnowledgeUpdatePacket((Player)entity, true));
        });
    }

    public static void resetSigns(Entity entity) {
        if (!(entity instanceof ServerPlayer)) return;
        entity.getCapability(IKnowledge.INSTANCE, null).ifPresent((k) -> {
            k.resetSigns();
            Networking.sendTo((Player)entity, new KnowledgeUpdatePacket((Player)entity, true));
        });
    }

    public static void resetFacts(Entity entity) {
        if (!(entity instanceof ServerPlayer)) return;
        entity.getCapability(IKnowledge.INSTANCE, null).ifPresent((k) -> {
            k.resetFacts();
            Networking.sendTo((Player)entity, new KnowledgeUpdatePacket((Player)entity, true));
        });
    }

    public static void resetResearch(Entity entity) {
        if (!(entity instanceof ServerPlayer)) return;
        entity.getCapability(IKnowledge.INSTANCE, null).ifPresent((k) -> {
            k.resetResearch();
            Networking.sendTo((Player)entity, new KnowledgeUpdatePacket((Player)entity, true));
        });
    }
    
    public static void resetRunes(Entity entity) {
        if (!(entity instanceof ServerPlayer)) return;
        entity.getCapability(IKnowledge.INSTANCE, null).ifPresent((k) -> {
            k.resetRunes();
            Networking.sendTo((Player)entity, new KnowledgeUpdatePacket((Player)entity, true));
        });
    }
}
