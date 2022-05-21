package elucent.eidolon;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import elucent.eidolon.capability.IKnowledge;
import elucent.eidolon.capability.IPlayerData;
import elucent.eidolon.capability.IReputation;
import elucent.eidolon.capability.ISoul;
import elucent.eidolon.capability.KnowledgeCommand;
import elucent.eidolon.entity.ZombieBruteEntity;
import elucent.eidolon.entity.ai.PriestBarterGoal;
import elucent.eidolon.entity.ai.WitchBarterGoal;
import elucent.eidolon.event.StuckInBlockEvent;
import elucent.eidolon.item.BonelordArmorItem;
import elucent.eidolon.item.CleavingAxeItem;
import elucent.eidolon.item.CodexItem;
import elucent.eidolon.item.ReaperScytheItem;
import elucent.eidolon.item.WarlockRobesItem;
import elucent.eidolon.network.CrystallizeEffectPacket;
import elucent.eidolon.network.KnowledgeUpdatePacket;
import elucent.eidolon.network.Networking;
import elucent.eidolon.network.SoulUpdatePacket;
import elucent.eidolon.network.WingsDataUpdatePacket;
import elucent.eidolon.research.Research;
import elucent.eidolon.ritual.Ritual;
import elucent.eidolon.spell.Signs;
import elucent.eidolon.tile.GobletTileEntity;
import elucent.eidolon.util.EntityUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionAddedEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.registries.ForgeRegistries;

public class Events {
    @SubscribeEvent
    public void attachWorldCaps(AttachCapabilitiesEvent<Level> event) {
        if (event.getObject() instanceof Level) event.addCapability(new ResourceLocation(Eidolon.MODID, "reputation"), new IReputation.Provider());
    }

    @SubscribeEvent
    public void attachEntityCaps(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
        	event.addCapability(new ResourceLocation(Eidolon.MODID, "knowledge"), new IKnowledge.Provider());
        	event.addCapability(new ResourceLocation(Eidolon.MODID, "player_data"), new IPlayerData.Provider());
        }
        if (event.getObject() instanceof LivingEntity) event.addCapability(new ResourceLocation(Eidolon.MODID, "soul"), new ISoul.Provider());
    }

    @SubscribeEvent
    public void onClone(PlayerEvent.Clone event) {
        Capability<IKnowledge> KNOWLEDGE = IKnowledge.INSTANCE;
        Capability<ISoul> SOUL = ISoul.INSTANCE;
        Capability<IPlayerData> PDATA = IPlayerData.INSTANCE;
    	event.getOriginal().reviveCaps();
        event.getPlayer().getCapability(KNOWLEDGE).ifPresent((k) -> {
        	event.getOriginal().getCapability(KNOWLEDGE).ifPresent((o) -> {
            	((INBTSerializable<CompoundTag>)k).deserializeNBT(((INBTSerializable<CompoundTag>)o).serializeNBT());
        	});
        });
        event.getPlayer().getCapability(SOUL).ifPresent((k) -> {
        	event.getOriginal().getCapability(SOUL).ifPresent((o) -> {
            	((INBTSerializable<CompoundTag>)k).deserializeNBT(((INBTSerializable<CompoundTag>)o).serializeNBT());
        	});
        });
        event.getPlayer().getCapability(PDATA).ifPresent((k) -> {
        	event.getOriginal().getCapability(PDATA).ifPresent((o) -> {
            	((INBTSerializable<CompoundTag>)k).deserializeNBT(((INBTSerializable<CompoundTag>)o).serializeNBT());
        	});
        });
    	event.getOriginal().invalidateCaps();
        if (!event.getPlayer().level.isClientSide) {
            Networking.sendTo(event.getPlayer(), new KnowledgeUpdatePacket(event.getPlayer(), false));
            Networking.sendTo(event.getPlayer(), new SoulUpdatePacket(event.getPlayer()));
        }
    }
    
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
    	KnowledgeCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void onTarget(LivingSetAttackTargetEvent event) {
        if (EntityUtil.isEnthralledBy(event.getEntityLiving(), event.getTarget()))
            ((Mob)event.getEntityLiving()).setTarget(null);
    }

    @SubscribeEvent
    public void onTarget(LivingUpdateEvent event) {
    	Level level = event.getEntity().getLevel();
    	LivingEntity e = event.getEntityLiving();
        if (e.hasEffect(Registry.UNDEATH_EFFECT.get()) && level.isDay() && !level.isClientSide) {
            float f = e.getBrightness();
            BlockPos blockpos = e.getVehicle() instanceof Boat ? (new BlockPos(e.getX(), (double) Math.round(e.getY()), e.getZ())).above() : new BlockPos(e.getX(), (double) Math.round(e.getY()), e.getZ());
            if (f > 0.5F && e.getRandom().nextFloat() * 30.0F < (f - 0.4F) * 2.0F && level.canSeeSky(blockpos)) {
                e.setSecondsOnFire(8);
            }
        }
        boolean hasBoneArmor = false;
        for (ItemStack s : e.getArmorSlots()) {
        	if (s.getItem() instanceof BonelordArmorItem) hasBoneArmor = true;
        }
        if (hasBoneArmor && event.getEntityLiving().getHealth() >= event.getEntityLiving().getMaxHealth() * 0.999 && event.getEntity().tickCount % 80 == 0) 
        	event.getEntityLiving().getCapability(ISoul.INSTANCE).ifPresent(s -> s.healEtherealHealth(1, ISoul.getPersistentHealth(event.getEntityLiving())));
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
        
        if (entity instanceof ZombieBruteEntity z && entity.hasEffect(MobEffects.WITHER) && !entity.level.isClientSide) {
        	for (ItemEntity item : event.getDrops()) if (item.getItem().is(Registry.ZOMBIE_HEART.get())) {
        		item.setItem(new ItemStack(Registry.WITHERED_HEART.get(), item.getItem().getCount()));
        	}
        }

        if (event.getSource().getEntity() != null && event.getSource().getEntity() instanceof LivingEntity) {
            LivingEntity source = (LivingEntity) event.getSource().getEntity();
            ItemStack held = source.getMainHandItem();
            if (!entity.level.isClientSide && (held.getItem() instanceof ReaperScytheItem || event.getSource() == Registry.RITUAL_DAMAGE) 
            		&& entity.isInvertedHealAndHarm()) {
            	if (!(entity instanceof Player)) event.getDrops().clear();
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
                    for (ItemEntity e : event.getDrops()) {
                    	if (e.getItem().is(head.getItem())) doDrop = false; // No duplicate heads.
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
        if (BiomeDictionary.hasType(key, BiomeDictionary.Type.OVERWORLD) && BiomeDictionary.hasType(key, BiomeDictionary.Type.FOREST)) {
            ev.getSpawns().addSpawn(MobCategory.CREATURE,
                new MobSpawnSettings.SpawnerData(Registry.RAVEN.get(), Config.RAVEN_SPAWN_WEIGHT.get(), 2, 5));
        }
        if (key.equals(Biomes.OLD_GROWTH_PINE_TAIGA) || key.equals(Biomes.OLD_GROWTH_SPRUCE_TAIGA) || key.equals(Biomes.FLOWER_FOREST))
        	ev.getSpawns().addSpawn(MobCategory.AMBIENT,
                new MobSpawnSettings.SpawnerData(Registry.SLIMY_SLUG.get(), Config.ABOVEGROUND_SLUG_WEIGHT.get(), 2, 5));
        if (key.equals(Biomes.LUSH_CAVES))
        	ev.getSpawns().addSpawn(MobCategory.AMBIENT,
                new MobSpawnSettings.SpawnerData(Registry.SLIMY_SLUG.get(), Config.UNDERGROUND_SLUG_WEIGHT.get(), 2, 5));
    }

    @SubscribeEvent
    public void registerCustomAI(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof LivingEntity && !event.getWorld().isClientSide) {
            if (event.getEntity() instanceof Player) {
                Networking.sendTo((Player)event.getEntity(), new KnowledgeUpdatePacket((Player)event.getEntity(), false));
                Networking.sendTo((Player)event.getEntity(), new SoulUpdatePacket((Player)event.getEntity()));
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
    public void onPlayerTick(PlayerTickEvent event) {
    	if (event.phase == TickEvent.Phase.END) event.player.getCapability(IPlayerData.INSTANCE).ifPresent((d) -> {
    		if (!d.getWingsItem(event.player).isEmpty()) {
    			if (event.player.isCrouching() && event.player.getDeltaMovement().y < -0.1) {
    				d.startFlying(event.player);
    				event.player.setDeltaMovement(event.player.getDeltaMovement().x, -0.1, event.player.getDeltaMovement().z);
    			}
				if (d.isFlying(event.player)) event.player.resetFallDistance();
				
    			if (d.isDashing(event.player)) d.doDashTick(event.player);
				
    			if (event.player.isOnGround()) {
    				d.rechargeWings(event.player);
    				d.stopFlying(event.player);
    			}
    			if (!event.player.level.isClientSide) {
    				Networking.sendToTracking(event.player.level, event.player.blockPosition(), new WingsDataUpdatePacket(event.player));
    			}
    		}
    	});
    }

    @SubscribeEvent
    public void onApplyPotion(PotionEvent.PotionApplicableEvent event) {
        if (event.getPotionEffect().getEffect() == MobEffects.MOVEMENT_SLOWDOWN && event.getEntityLiving().getItemBySlot(EquipmentSlot.FEET).getItem() instanceof WarlockRobesItem) {
            event.setResult(Event.Result.DENY);
        }
    }
    
    @SubscribeEvent
    public void onLivingUse(LivingEntityUseItemEvent event) {
    	if (event.getEntityLiving().hasEffect(Registry.UNDEATH_EFFECT.get())) {
    		if (!Registry.ZOMBIE_FOOD_TAG.contains(event.getItem().getItem())) 
    			event.setCanceled(true);
    	}
    }
    
    @SubscribeEvent
    public void onPotionApplicable(PotionAddedEvent event) {
    	if (event.getEntityLiving().hasEffect(MobEffects.HUNGER) && event.getPotionEffect().getEffect() == Registry.UNDEATH_EFFECT.get()) {
    		event.getEntityLiving().removeEffect(MobEffects.HUNGER);
    	}
    }
    
    @SubscribeEvent
    public void onPotionApplicable(PotionApplicableEvent event) {
    	if (event.getEntityLiving().hasEffect(Registry.UNDEATH_EFFECT.get()) && event.getPotionEffect().getEffect() == MobEffects.HUNGER) {
    		event.setResult(Result.DENY);
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
        
        event.getEntityLiving().getCapability(ISoul.INSTANCE).ifPresent(s -> {
            if (s.hasEtherealHealth()) {
            	s.hurtEtherealHealth(event.getAmount(), ISoul.getPersistentHealth(event.getEntityLiving()));
            	event.setAmount(0);
            	Networking.sendToTracking(event.getEntity().level, event.getEntity().getOnPos(), new SoulUpdatePacket((Player)event.getEntityLiving()));
            }
        });
    }

    @SubscribeEvent
    public void onGetSpeedFactor(StuckInBlockEvent event) {
        if (event.getStuckMultiplier().length() < 1.0f && event.getEntity() instanceof LivingEntity && ((LivingEntity)event.getEntity()).getItemBySlot(EquipmentSlot.FEET).getItem() instanceof WarlockRobesItem) {
            Vec3 diff = new Vec3(1, 1, 1).subtract(event.getStuckMultiplier()).scale(0.5);
            event.setStuckMultiplier(new Vec3(1, 1, 1).subtract(diff));
        }
    }
}
