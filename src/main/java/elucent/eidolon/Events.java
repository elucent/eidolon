package elucent.eidolon;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import elucent.eidolon.capability.IKnowledge;
import elucent.eidolon.capability.KnowledgeProvider;
import elucent.eidolon.capability.ReputationProvider;
import elucent.eidolon.entity.ai.GenericBarterGoal;
import elucent.eidolon.entity.ai.WitchBarterGoal;
import elucent.eidolon.item.CleavingAxeItem;
import elucent.eidolon.item.CodexItem;
import elucent.eidolon.item.ReaperScytheItem;
import elucent.eidolon.network.CrystallizeEffectPacket;
import elucent.eidolon.network.KnowledgeUpdatePacket;
import elucent.eidolon.network.Networking;
import elucent.eidolon.spell.Signs;
import elucent.eidolon.util.RenderUtil;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import sun.net.www.content.text.Generic;

import java.util.HashMap;
import java.util.Map;

public class Events {
    @SubscribeEvent
    public void attachWorldCaps(AttachCapabilitiesEvent<World> event) {
        if (event.getObject() instanceof World) event.addCapability(new ResourceLocation(Eidolon.MODID, "reputation"), new ReputationProvider());
    }

    @SubscribeEvent
    public void attachEntityCaps(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Entity) event.addCapability(new ResourceLocation(Eidolon.MODID, "knowledge"), new KnowledgeProvider());
    }

    @SubscribeEvent
    public void onClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            Capability<IKnowledge> KNOWLEDGE = KnowledgeProvider.CAPABILITY;
            KNOWLEDGE.getStorage().readNBT(
                KNOWLEDGE,
                event.getPlayer().getCapability(KNOWLEDGE, null).resolve().get(),
                null,
                KNOWLEDGE.getStorage().writeNBT(KNOWLEDGE, event.getOriginal().getCapability(KNOWLEDGE, null).resolve().get(), null)
            );
            if (!event.getPlayer().world.isRemote) {
                Networking.sendTo(event.getPlayer(), new KnowledgeUpdatePacket(event.getPlayer(), false));
            }
        }
    }

    @SubscribeEvent
    public void onDeath(LivingDropsEvent event) {
        if (event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof LivingEntity) {
            LivingEntity source = (LivingEntity) event.getSource().getTrueSource();
            ItemStack held = source.getHeldItemMainhand();
            LivingEntity entity = event.getEntityLiving();
            if (!event.getEntity().world.isRemote && held.getItem() instanceof ReaperScytheItem && entity.isEntityUndead()) {
                int looting = ForgeHooks.getLootingLevel(entity, source, event.getSource());
                event.getDrops().clear();
                ItemEntity drop = new ItemEntity(source.world, entity.getPosX(), entity.getPosY(), entity.getPosZ(),
                    new ItemStack(Registry.SOUL_SHARD.get(), 1 + source.world.rand.nextInt(2 + looting)));
                drop.setDefaultPickupDelay();
                event.getDrops().add(drop);
                Networking.sendToTracking(entity.world, entity.getPosition(), new CrystallizeEffectPacket(entity.getPosition()));
            }
            if (!entity.world.isRemote && held.getItem() instanceof CleavingAxeItem) {
                int looting = ForgeHooks.getLootingLevel(entity, source, event.getSource());
                ItemStack head = ItemStack.EMPTY;
                if (entity instanceof WitherSkeletonEntity) head = new ItemStack(Items.WITHER_SKELETON_SKULL);
                else if (entity instanceof SkeletonEntity) head = new ItemStack(Items.SKELETON_SKULL);
                else if (entity instanceof ZombieEntity) head = new ItemStack(Items.ZOMBIE_HEAD);
                else if (entity instanceof CreeperEntity) head = new ItemStack(Items.CREEPER_HEAD);
                else if (entity instanceof EnderDragonEntity) head = new ItemStack(Items.DRAGON_HEAD);
                else if (entity instanceof PlayerEntity) {
                    head = new ItemStack(Items.PLAYER_HEAD);
                    GameProfile gameprofile = ((PlayerEntity)entity).getGameProfile();
                    head.getOrCreateTag().put("SkullOwner", NBTUtil.writeGameProfile(new CompoundNBT(), gameprofile));
                }
                if (!head.isEmpty()) {
                    boolean doDrop = false;
                    if (entity.world.rand.nextInt(20) == 0) doDrop = true;
                    else for (int i = 0; i < looting; i++) {
                        if (entity.world.rand.nextInt(40) == 0) {
                            doDrop = true;
                            break;
                        }
                    }
                    if (doDrop) {
                        ItemEntity drop = new ItemEntity(source.world, entity.getPosX(), entity.getPosY(), entity.getPosZ(), head);
                        drop.setDefaultPickupDelay();
                        event.getDrops().add(drop);
                    }
                }
            }
        }
        if (event.getSource().getDamageType() == Registry.RITUAL_DAMAGE.getDamageType())
            event.getDrops().clear();
    }

    @SubscribeEvent
    public void registerSpawns(BiomeLoadingEvent ev) {
        if (ev.getCategory() != Biome.Category.MUSHROOM && ev.getCategory() != Biome.Category.OCEAN && ev.getCategory() != Biome.Category.NETHER && ev.getCategory() != Biome.Category.THEEND) {
            ev.getSpawns().withSpawner(EntityClassification.MONSTER,
                new MobSpawnInfo.Spawners(Registry.WRAITH.get(), Config.WRAITH_SPAWN_WEIGHT.get(), 1, 2));
            ev.getSpawns().withSpawner(EntityClassification.MONSTER,
                new MobSpawnInfo.Spawners(Registry.ZOMBIE_BRUTE.get(), Config.ZOMBIE_BRUTE_SPAWN_WEIGHT.get(), 1, 2));
        }
    }

    @SubscribeEvent
    public void registerCustomAI(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof LivingEntity && !event.getWorld().isRemote) {
            if (event.getEntity() instanceof PlayerEntity) {
                Networking.sendTo((PlayerEntity)event.getEntity(), new KnowledgeUpdatePacket((PlayerEntity)event.getEntity(), false));
            }
            if (event.getEntity() instanceof WitchEntity) {
                ((WitchEntity)event.getEntity()).goalSelector.addGoal(1, new WitchBarterGoal(
                    (WitchEntity)event.getEntity(),
                    (stack) -> stack.getItem() == Registry.CODEX.get(),
                    (stack) -> CodexItem.withSign(stack, Signs.WICKED_SIGN)
                ));
            }
            if (event.getEntity() instanceof VillagerEntity) {
                ((VillagerEntity)event.getEntity()).goalSelector.addGoal(1, new GenericBarterGoal<VillagerEntity>(
                    (VillagerEntity)event.getEntity(),
                    (stack) -> stack.getItem() == Registry.CODEX.get(),
                    (stack) -> CodexItem.withSign(stack, Signs.SACRED_SIGN)
                ));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    static IRenderTypeBuffer.Impl DELAYED_RENDER = null;

    @OnlyIn(Dist.CLIENT)
    public static IRenderTypeBuffer.Impl getDelayedRender() {
        if (DELAYED_RENDER == null) {
            Map<RenderType, BufferBuilder> buffers = new HashMap<>();
            for (RenderType type : new RenderType[]{
                RenderUtil.DELAYED_PARTICLE,
                RenderUtil.GLOWING_PARTICLE,
                RenderUtil.GLOWING_BLOCK_PARTICLE,
                RenderUtil.GLOWING,
                RenderUtil.GLOWING_SPRITE}) {
                buffers.put(type, new BufferBuilder(type.getBufferSize()));
            }
            DELAYED_RENDER = IRenderTypeBuffer.getImpl(buffers, new BufferBuilder(256));
        }
        return DELAYED_RENDER;
    }

    @OnlyIn(Dist.CLIENT)
    static float clientTicks = 0;

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onRenderLast(RenderWorldLastEvent event) {
        if (ClientConfig.BETTER_LAYERING.get()) {
            RenderSystem.pushMatrix(); // this feels...cheaty
            RenderSystem.multMatrix(event.getMatrixStack().getLast().getMatrix());
            getDelayedRender().finish(RenderUtil.DELAYED_PARTICLE);
            getDelayedRender().finish(RenderUtil.GLOWING_PARTICLE);
            getDelayedRender().finish(RenderUtil.GLOWING_BLOCK_PARTICLE);
            RenderSystem.popMatrix();

            getDelayedRender().finish(RenderUtil.GLOWING_SPRITE);
            getDelayedRender().finish(RenderUtil.GLOWING);
        }
        clientTicks += event.getPartialTicks();
    }

    @OnlyIn(Dist.CLIENT)
    public static float getClientTicks() {
        return clientTicks;
    }
}
