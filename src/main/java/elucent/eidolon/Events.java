package elucent.eidolon;

import com.mojang.authlib.GameProfile;
import elucent.eidolon.capability.IKnowledge;
import elucent.eidolon.capability.KnowledgeProvider;
import elucent.eidolon.capability.ReputationProvider;
import elucent.eidolon.entity.ai.PriestBarterGoal;
import elucent.eidolon.entity.ai.WitchBarterGoal;
import elucent.eidolon.event.SpeedFactorEvent;
import elucent.eidolon.item.CleavingAxeItem;
import elucent.eidolon.item.CodexItem;
import elucent.eidolon.item.ReaperScytheItem;
import elucent.eidolon.item.WarlockRobesItem;
import elucent.eidolon.network.CrystallizeEffectPacket;
import elucent.eidolon.network.KnowledgeUpdatePacket;
import elucent.eidolon.network.Networking;
import elucent.eidolon.ritual.Ritual;
import elucent.eidolon.spell.Signs;
import elucent.eidolon.tile.GobletTileEntity;
import elucent.eidolon.util.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Comparator;
import java.util.List;

public class Events {
    @SubscribeEvent
    public void attachWorldCaps(AttachCapabilitiesEvent<Level> event) {
        if (event.getObject() instanceof Level) event.addCapability(new ResourceLocation(Eidolon.MODID, "reputation"), new ReputationProvider());
    }

    @SubscribeEvent
    public void attachEntityCaps(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Entity) event.addCapability(new ResourceLocation(Eidolon.MODID, "knowledge"), new KnowledgeProvider());
    }

    @SubscribeEvent
    public void onClone(PlayerEvent.Clone event) {
        Capability<IKnowledge> KNOWLEDGE = KnowledgeProvider.CAPABILITY;
        event.getOriginal().reviveCaps();
        event.getOriginal().getCapability(KNOWLEDGE).ifPresent(oldKnowledge -> {
            event.getPlayer().getCapability(KNOWLEDGE).ifPresent(newKnowledge -> {
                newKnowledge.deserializeNBT(oldKnowledge.serializeNBT());
            });
        });
        if (!event.getPlayer().level.isClientSide) {
            Networking.sendTo(event.getPlayer(), new KnowledgeUpdatePacket(event.getPlayer(), false));
        }
    }

    @SubscribeEvent
    public void onTarget(LivingSetAttackTargetEvent event) {
        if (EntityUtil.isEnthralledBy(event.getEntityLiving(), event.getTarget()))
            ((Mob)event.getEntityLiving()).setTarget(null);
    }

    @SubscribeEvent
    public void onDeath(LivingDropsEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (!(entity instanceof Monster)) {
            Level world = entity.level;
            BlockPos pos = entity.blockPosition();
            List<GobletTileEntity> goblets = Ritual.getTilesWithinAABB(GobletTileEntity.class, world, new AABB(pos.offset(-2, -2, -2), pos.offset(3, 3, 3)));
            if (goblets.size() > 0) {
                GobletTileEntity goblet = goblets.stream().min(Comparator.comparingDouble((g) -> g.getBlockPos().distSqr(pos))).get();
                goblet.setEntityType(entity.getType());
            }
        }

        if (entity instanceof Witch || entity instanceof Villager) {
            if (entity.getMainHandItem().getItem() instanceof CodexItem)
                event.getDrops().add(new ItemEntity(entity.level, entity.getX(), entity.getY(), entity.getZ(), entity.getMainHandItem().copy()));
        }

        if (EntityUtil.isEnthralled(entity)) {
            event.getDrops().clear();
            return;
        }

        if (event.getSource().getEntity() != null && event.getSource().getEntity() instanceof LivingEntity) {
            LivingEntity source = (LivingEntity) event.getSource().getEntity();
            ItemStack held = source.getMainHandItem();
            if (!entity.level.isClientSide && held.getItem() instanceof ReaperScytheItem && entity.isInvertedHealAndHarm()) {
                int looting = ForgeHooks.getLootingLevel(entity, source, event.getSource());
                ItemEntity drop = new ItemEntity(source.level, entity.getX(), entity.getY(), entity.getZ(),
                    new ItemStack(Registry.SOUL_SHARD.get(), source.level.random.nextInt(2 + looting)));
                drop.setDefaultPickUpDelay();
                event.getDrops().add(drop);
                Networking.sendToTracking(entity.level, entity.blockPosition(), new CrystallizeEffectPacket(entity.blockPosition()));
            }
            if (!entity.level.isClientSide && held.getItem() instanceof CleavingAxeItem) {
                int looting = ForgeHooks.getLootingLevel(entity, source, event.getSource());
                ItemStack head = ItemStack.EMPTY;
                if (entity instanceof WitherSkeleton) head = new ItemStack(Items.WITHER_SKELETON_SKULL);
                else if (entity instanceof Skeleton) head = new ItemStack(Items.SKELETON_SKULL);
                else if (entity instanceof Zombie) head = new ItemStack(Items.ZOMBIE_HEAD);
                else if (entity instanceof Creeper) head = new ItemStack(Items.CREEPER_HEAD);
                else if (entity instanceof EnderDragon) head = new ItemStack(Items.DRAGON_HEAD);
                else if (entity instanceof Player) {
                    head = new ItemStack(Items.PLAYER_HEAD);
                    GameProfile gameprofile = ((Player)entity).getGameProfile();
                    head.getOrCreateTag().put("SkullOwner", NbtUtils.writeGameProfile(new CompoundTag(), gameprofile));
                }
                if (!head.isEmpty()) {
                    boolean doDrop = false;
                    if (entity.level.random.nextInt(20) == 0) doDrop = true;
                    else for (int i = 0; i < looting; i++) {
                        if (entity.level.random.nextInt(40) == 0) {
                            doDrop = true;
                            break;
                        }
                    }
                    if (doDrop) {
                        ItemEntity drop = new ItemEntity(source.level, entity.getX(), entity.getY(), entity.getZ(), head);
                        drop.setDefaultPickUpDelay();
                        event.getDrops().add(drop);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void registerSpawns(BiomeLoadingEvent ev) {
        ResourceKey<Biome> key = ResourceKey.<Biome>create(ForgeRegistries.Keys.BIOMES, ev.getName());
        if (BiomeDictionary.hasType(key, BiomeDictionary.Type.OVERWORLD) && ev.getCategory() != Biome.BiomeCategory.MUSHROOM) {
            ev.getSpawns().addSpawn(MobCategory.MONSTER,
                new MobSpawnSettings.SpawnerData(Registry.WRAITH.get(), Config.WRAITH_SPAWN_WEIGHT.get(), 1, 2));
            ev.getSpawns().addSpawn(MobCategory.MONSTER,
                new MobSpawnSettings.SpawnerData(Registry.ZOMBIE_BRUTE.get(), Config.ZOMBIE_BRUTE_SPAWN_WEIGHT.get(), 1, 2));
        }
    }

    @SubscribeEvent
    public void registerCustomAI(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof LivingEntity && !event.getWorld().isClientSide) {
            if (event.getEntity() instanceof Player) {
                Networking.sendTo((Player)event.getEntity(), new KnowledgeUpdatePacket((Player)event.getEntity(), false));
            }
            if (event.getEntity() instanceof Witch) {
                ((Witch)event.getEntity()).goalSelector.addGoal(1, new WitchBarterGoal(
                    (Witch)event.getEntity(),
                    (stack) -> stack.getItem() == Registry.CODEX.get(),
                    (stack) -> CodexItem.withSign(stack, Signs.WICKED_SIGN)
                ));
            }
            if (event.getEntity() instanceof Villager) {
                ((Villager)event.getEntity()).goalSelector.addGoal(1, new PriestBarterGoal(
                    (Villager)event.getEntity(),
                    (stack) -> stack.getItem() == Registry.CODEX.get(),
                    (stack) -> CodexItem.withSign(stack, Signs.SACRED_SIGN)
                ));
            }
        }
    }

    @SubscribeEvent
    public void onApplyPotion(PotionEvent.PotionApplicableEvent event) {
        if (event.getPotionEffect().getEffect() == MobEffects.MOVEMENT_SLOWDOWN && event.getEntityLiving().getItemBySlot(EquipmentSlot.FEET).getItem() instanceof WarlockRobesItem) {
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        if ((event.getSource().getMsgId() == DamageSource.WITHER.getMsgId() || event.getSource().isMagic())) {
            if (event.getSource().getEntity() instanceof LivingEntity
                && ((LivingEntity)event.getSource().getEntity()).getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof WarlockRobesItem) {
                event.setAmount(event.getAmount() * 1.5f);
                if (event.getSource().getMsgId() == DamageSource.WITHER.getMsgId())
                    ((LivingEntity) event.getSource().getEntity()).heal(event.getAmount() / 2);
            }
            if (event.getEntityLiving().getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof WarlockRobesItem)
                event.setAmount(event.getAmount() / 2);
        }
    }

    @SubscribeEvent
    public void onGetSpeedFactor(SpeedFactorEvent event) {
        if (event.getSpeedFactor() < 1.0f && event.getEntity() instanceof LivingEntity && ((LivingEntity)event.getEntity()).getItemBySlot(EquipmentSlot.FEET).getItem() instanceof WarlockRobesItem) {
            float diff = 1.0f - event.getSpeedFactor();
            event.setSpeedFactor(1.0f - diff / 2);
        }
    }
}
