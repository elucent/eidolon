package elucent.eidolon;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;

import elucent.eidolon.block.BlockBase;
import elucent.eidolon.block.BrazierBlock;
import elucent.eidolon.block.CandleBlock;
import elucent.eidolon.block.CandlestickBlock;
import elucent.eidolon.block.CisternBlock;
import elucent.eidolon.block.CrucibleBlock;
import elucent.eidolon.block.EffigyBlock;
import elucent.eidolon.block.EnchantedAshBlock;
import elucent.eidolon.block.GobletBlock;
import elucent.eidolon.block.HandBlock;
import elucent.eidolon.block.HerbBlockBase;
import elucent.eidolon.block.NecroticFocusBlock;
import elucent.eidolon.block.PillarBlockBase;
import elucent.eidolon.block.PipeBlock;
import elucent.eidolon.block.ResearchTableBlock;
import elucent.eidolon.block.SoulEnchanterBlock;
import elucent.eidolon.block.TableBlockBase;
import elucent.eidolon.block.TwoHighBlockBase;
import elucent.eidolon.block.WoodenStandBlock;
import elucent.eidolon.block.WorktableBlock;
import elucent.eidolon.capability.IKnowledge;
import elucent.eidolon.capability.IPlayerData;
import elucent.eidolon.capability.IReputation;
import elucent.eidolon.capability.ISoul;
import elucent.eidolon.capability.KnowledgeCommand;
import elucent.eidolon.entity.AngelArrowEntity;
import elucent.eidolon.entity.AngelArrowRenderer;
import elucent.eidolon.entity.BonechillProjectileEntity;
import elucent.eidolon.entity.ChantCasterEntity;
import elucent.eidolon.entity.NecromancerEntity;
import elucent.eidolon.entity.NecromancerModel;
import elucent.eidolon.entity.NecromancerRenderer;
import elucent.eidolon.entity.NecromancerSpellEntity;
import elucent.eidolon.entity.RavenEntity;
import elucent.eidolon.entity.RavenModel;
import elucent.eidolon.entity.RavenRenderer;
import elucent.eidolon.entity.SlimySlugEntity;
import elucent.eidolon.entity.SlimySlugModel;
import elucent.eidolon.entity.SlimySlugRenderer;
import elucent.eidolon.entity.SoulfireProjectileEntity;
import elucent.eidolon.entity.WraithEntity;
import elucent.eidolon.entity.WraithModel;
import elucent.eidolon.entity.WraithRenderer;
import elucent.eidolon.entity.ZombieBruteEntity;
import elucent.eidolon.entity.ZombieBruteModel;
import elucent.eidolon.entity.ZombieBruteRenderer;
import elucent.eidolon.gui.ResearchTableContainer;
import elucent.eidolon.gui.SoulEnchanterContainer;
import elucent.eidolon.gui.WoodenBrewingStandContainer;
import elucent.eidolon.gui.WorktableContainer;
import elucent.eidolon.item.AthameItem;
import elucent.eidolon.item.BonechillWandItem;
import elucent.eidolon.item.BonelordArmorItem;
import elucent.eidolon.item.CleavingAxeItem;
import elucent.eidolon.item.CodexItem;
import elucent.eidolon.item.CompletedResearchItem;
import elucent.eidolon.item.DeathbringerScytheItem;
import elucent.eidolon.item.ItemBase;
import elucent.eidolon.item.NotetakingToolsItem;
import elucent.eidolon.item.ReaperScytheItem;
import elucent.eidolon.item.ResearchNotesItem;
import elucent.eidolon.item.ReversalPickItem;
import elucent.eidolon.item.SappingSwordItem;
import elucent.eidolon.item.SilverArmorItem;
import elucent.eidolon.item.SoulfireWandItem;
import elucent.eidolon.item.SummoningStaffItem;
import elucent.eidolon.item.Tiers;
import elucent.eidolon.item.TongsItem;
import elucent.eidolon.item.TopHatItem;
import elucent.eidolon.item.UnholySymbolItem;
import elucent.eidolon.item.WarlockRobesItem;
import elucent.eidolon.item.curio.AngelSightItem;
import elucent.eidolon.item.curio.BasicAmuletItem;
import elucent.eidolon.item.curio.BasicBeltItem;
import elucent.eidolon.item.curio.BasicRingItem;
import elucent.eidolon.item.curio.EnervatingRingItem;
import elucent.eidolon.item.curio.GlassHandItem;
import elucent.eidolon.item.curio.GravityBeltItem;
import elucent.eidolon.item.curio.MindShieldingPlateItem;
import elucent.eidolon.item.curio.PrestigiousPalmItem;
import elucent.eidolon.item.curio.RavenCloakItem;
import elucent.eidolon.item.curio.ResoluteBeltItem;
import elucent.eidolon.item.curio.SanguineAmuletItem;
import elucent.eidolon.item.curio.SoulboneAmuletItem;
import elucent.eidolon.item.curio.TerminusMirrorItem;
import elucent.eidolon.item.curio.VoidAmuletItem;
import elucent.eidolon.item.curio.WardedMailItem;
import elucent.eidolon.item.model.SilverArmorModel;
import elucent.eidolon.item.model.TopHatModel;
import elucent.eidolon.item.model.WarlockArmorModel;
import elucent.eidolon.mixin.PotionBrewingMixin;
import elucent.eidolon.particle.BubbleParticleType;
import elucent.eidolon.particle.FlameParticleType;
import elucent.eidolon.particle.GlowingSlashParticleType;
import elucent.eidolon.particle.LineWispParticleType;
import elucent.eidolon.particle.RuneParticleType;
import elucent.eidolon.particle.SignParticleType;
import elucent.eidolon.particle.SlashParticleType;
import elucent.eidolon.particle.SmokeParticleType;
import elucent.eidolon.particle.SparkleParticleType;
import elucent.eidolon.particle.SteamParticleType;
import elucent.eidolon.particle.WispParticleType;
import elucent.eidolon.potion.AnchoredEffect;
import elucent.eidolon.potion.ChilledEffect;
import elucent.eidolon.potion.ReinforcedEffect;
import elucent.eidolon.potion.UndeathEffect;
import elucent.eidolon.potion.VulnerableEffect;
import elucent.eidolon.reagent.Reagent;
import elucent.eidolon.reagent.ReagentRegistry;
import elucent.eidolon.recipe.CrucibleRecipe;
import elucent.eidolon.recipe.WorktableRecipe;
import elucent.eidolon.research.Research;
import elucent.eidolon.ritual.AllureRitual;
import elucent.eidolon.ritual.CrystalRitual;
import elucent.eidolon.ritual.DaylightRitual;
import elucent.eidolon.ritual.DeceitRitual;
import elucent.eidolon.ritual.MoonlightRitual;
import elucent.eidolon.ritual.PurifyRitual;
import elucent.eidolon.ritual.RepellingRitual;
import elucent.eidolon.ritual.SanguineRitual;
import elucent.eidolon.ritual.SummonRitual;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.spell.Signs;
import elucent.eidolon.tile.BrazierTileEntity;
import elucent.eidolon.tile.CrucibleTileEntity;
import elucent.eidolon.tile.CrucibleTileRenderer;
import elucent.eidolon.tile.EffigyTileEntity;
import elucent.eidolon.tile.GobletTileEntity;
import elucent.eidolon.tile.HandTileEntity;
import elucent.eidolon.tile.NecroticFocusTileEntity;
import elucent.eidolon.tile.ResearchTableTileEntity;
import elucent.eidolon.tile.SoulEnchanterTileEntity;
import elucent.eidolon.tile.SoulEnchanterTileRenderer;
import elucent.eidolon.tile.WoodenStandTileEntity;
import elucent.eidolon.tile.reagent.CisternTileEntity;
import elucent.eidolon.tile.reagent.PipeTileEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.BlockEntitySupplier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

public class Registry {
    public static Tags.IOptionalNamedTag<Item>
        INGOTS_LEAD = ItemTags.createOptional(new ResourceLocation("forge", "ingots/lead")),
        INGOTS_PEWTER = ItemTags.createOptional(new ResourceLocation("forge", "ingots/pewter")),
        INGOTS_ARCANE_GOLD = ItemTags.createOptional(new ResourceLocation("forge", "ingots/arcane_gold")),
        INGOTS_SILVER = ItemTags.createOptional(new ResourceLocation("forge", "ingots/silver")),
        GEMS_SHADOW = ItemTags.createOptional(new ResourceLocation("forge", "gems/shadow_gem"));

    static Map<String, Block> BLOCK_MAP = new HashMap<>();
    static Map<String, Item> ITEM_MAP = new HashMap<>();
    static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Eidolon.MODID);
    static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Eidolon.MODID);
    static DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Eidolon.MODID);
    static DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Eidolon.MODID);
    static DeferredRegister<MobEffect> POTIONS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Eidolon.MODID);
    static DeferredRegister<Potion> POTION_TYPES = DeferredRegister.create(ForgeRegistries.POTIONS, Eidolon.MODID);
    static DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Eidolon.MODID);
    static DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Eidolon.MODID);
    static DeferredRegister<RecipeSerializer<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Eidolon.MODID);
    static DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Eidolon.MODID);
    
    public static Tag<Item> ZOMBIE_FOOD_TAG = ItemTags.createOptional(new ResourceLocation(Eidolon.MODID, "zombie_food"));

    static Item.Properties itemProps() {
        return new Item.Properties().tab(Eidolon.TAB);
    }

    static BlockBehaviour.Properties blockProps(Material mat, MaterialColor color) {
        return BlockBehaviour.Properties.of(mat, color);
    }

    static RegistryObject<Item> addItem(String name) {
        Item i = new Item(itemProps());
        ITEM_MAP.put(name, i);
        return ITEMS.register(name, () -> i);
    }

    static RegistryObject<Item> addItem(String name, Item.Properties props) {
        Item i = new Item(props);
        ITEM_MAP.put(name, i);
        return ITEMS.register(name, () -> i);
    }

    static RegistryObject<Item> addItem(String name, Item item) {
        ITEM_MAP.put(name, item);
        return ITEMS.register(name, () -> item);
    }

    static RegistryObject<Item> addItem(String name, Supplier<Item> item) {
        return ITEMS.register(name, item);
    }

    static RegistryObject<Block> addBlock(String name, Block.Properties props) {
        Block b = new Block(props);
        BLOCK_MAP.put(name, b);
        ITEMS.register(name, () -> new BlockItem(b, itemProps()));
        return BLOCKS.register(name, () -> b);
    }

    static RegistryObject<Block> addBlock(String name, Block block) {
        BLOCK_MAP.put(name, block);
        ITEMS.register(name, () -> new BlockItem(block, itemProps()));
        return BLOCKS.register(name, () -> block);
    }

    public static class DecoBlockPack {
        DeferredRegister<Block> registry;
        String basename;
        BlockBehaviour.Properties props;
        RegistryObject<Block> full = null, slab = null, stair = null, wall = null, fence = null, fence_gate = null;

        public DecoBlockPack(DeferredRegister<Block> blocks, String basename, BlockBehaviour.Properties props) {
            this.registry = blocks;
            this.basename = basename;
            this.props = props;
            full = addBlock(basename, new Block(props));
            slab = addBlock(basename + "_slab", new SlabBlock(props));
            stair = addBlock(basename + "_stairs", new StairBlock(() -> full.get().defaultBlockState(), props));
        }

        public DecoBlockPack addWall() {
            wall = addBlock(basename + "_wall", new WallBlock(props));
            return this;
        }

        public DecoBlockPack addFence() {
            fence = addBlock(basename + "_fence", new FenceBlock(props));
            fence = addBlock(basename + "_fence_gate", new FenceGateBlock(props));
            return this;
        }

        public Block getBlock() { return full.get(); }
        public Block getSlab() { return slab.get(); }
        public Block getStairs() { return stair.get(); }
        public Block getWall() { return wall.get(); }
        public Block getFence() { return fence.get(); }
    }

    static <T extends Entity> RegistryObject<EntityType<T>> addEntity(String name, float width, float height, EntityType.EntityFactory<T> factory, MobCategory kind) {
        EntityType<T> type = EntityType.Builder.<T>of(factory, kind)
            .setTrackingRange(64)
            .setUpdateInterval(1)
            .sized(width, height)
            .build(Eidolon.MODID + ":" + name);
        return ENTITIES.register(name, () -> type);
    }

    static <T extends Mob> RegistryObject<EntityType<T>> addEntity(String name, int color1, int color2, float width, float height, EntityType.EntityFactory<T> factory, MobCategory kind) {
        EntityType<T> type = EntityType.Builder.<T>of(factory, kind)
            .setTrackingRange(64)
            .setUpdateInterval(1)
            .sized(width, height)
            .build(Eidolon.MODID + ":" + name);
        ITEMS.register("spawn_" + name, () -> new SpawnEggItem((EntityType<? extends T>) type, color1, color2, itemProps().tab(CreativeModeTab.TAB_MISC)));
        return ENTITIES.register(name, () -> type);
    }

    static RegistryObject<SoundEvent> addSound(String name) {
        SoundEvent event = new SoundEvent(new ResourceLocation(Eidolon.MODID, name));
        return SOUND_EVENTS.register(name, () -> event);
    }

    static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> addContainer(String name, MenuType.MenuSupplier<T> factory) {
        return CONTAINERS.register(name, () -> new MenuType<T>(factory));
    }

    static <T extends BlockEntity> BlockEntityType<T> addTileEntity(IForgeRegistry<BlockEntityType<?>> registry, String name, BlockEntitySupplier<T> factory, Block... blocks) {
        BlockEntityType<T> type = BlockEntityType.Builder.<T>of(factory, blocks).build(null);
        type.setRegistryName(Eidolon.MODID, name);
        registry.register(type);
        return type;
    }

    public static RegistryObject<SoundEvent>
        CAST_SOULFIRE_EVENT = addSound("cast_soulfire"),
        CAST_BONECHILL_EVENT = addSound("cast_bonechill"),
        SPLASH_SOULFIRE_EVENT = addSound("splash_soulfire"),
        SPLASH_BONECHILL_EVENT = addSound("splash_bonechill"),
        SELECT_RUNE = addSound("select_rune"),
        CHANT_WORD = addSound("chant_word"),
        PAROUSIA = addSound("parousia");

    public static RegistryObject<MobEffect>
        CHILLED_EFFECT = POTIONS.register("chilled", () -> new ChilledEffect()),
        ANCHORED_EFFECT = POTIONS.register("anchored", () -> new AnchoredEffect()),
        REINFORCED_EFFECT = POTIONS.register("reinforced", () -> new ReinforcedEffect()
        		.addAttributeModifier(Attributes.ARMOR, "483b6415-421e-45d1-ab28-d85d11a19c70", 0.25, Operation.MULTIPLY_TOTAL)),
        VULNERABLE_EFFECT = POTIONS.register("vulnerable", () -> new VulnerableEffect()
        		.addAttributeModifier(Attributes.ARMOR, "e5bae4de-2019-4316-b8cc-b4d879d676f9", -0.25, Operation.MULTIPLY_TOTAL)),
        UNDEATH_EFFECT = POTIONS.register("undeath", () -> new UndeathEffect());

    public static RegistryObject<Potion>
        CHILLED_POTION = POTION_TYPES.register("chilled", () -> new Potion(new MobEffectInstance(CHILLED_EFFECT.get(), 3600))),
        LONG_CHILLED_POTION = POTION_TYPES.register("long_chilled", () -> new Potion(new MobEffectInstance(CHILLED_EFFECT.get(), 9600))),
        ANCHORED_POTION = POTION_TYPES.register("anchored", () -> new Potion(new MobEffectInstance(ANCHORED_EFFECT.get(), 3600))),
        LONG_ANCHORED_POTION = POTION_TYPES.register("long_anchored", () -> new Potion(new MobEffectInstance(ANCHORED_EFFECT.get(), 9600))),
		REINFORCED_POTION = POTION_TYPES.register("reinforced", () -> new Potion(new MobEffectInstance(REINFORCED_EFFECT.get(), 3600))),
        LONG_REINFORCED_POTION = POTION_TYPES.register("long_reinforced", () -> new Potion(new MobEffectInstance(REINFORCED_EFFECT.get(), 9600))),
		STRONG_REINFORCED_POTION = POTION_TYPES.register("strong_reinforced", () -> new Potion(new MobEffectInstance(REINFORCED_EFFECT.get(), 1800, 1))),
		VULNERABLE_POTION = POTION_TYPES.register("vulnerable", () -> new Potion(new MobEffectInstance(VULNERABLE_EFFECT.get(), 3600))),
        LONG_VULNERABLE_POTION = POTION_TYPES.register("long_vulnerable", () -> new Potion(new MobEffectInstance(VULNERABLE_EFFECT.get(), 9600))),
        STRONG_VULNERABLE_POTION = POTION_TYPES.register("strong_vulnerable", () -> new Potion(new MobEffectInstance(VULNERABLE_EFFECT.get(), 1800, 1))),
        UNDEATH_POTION = POTION_TYPES.register("undeath", () -> new Potion(new MobEffectInstance(UNDEATH_EFFECT.get(), 3600))),
        LONG_UNDEATH_POTION = POTION_TYPES.register("long_undeath", () -> new Potion(new MobEffectInstance(UNDEATH_EFFECT.get(), 9600))),
		DECAY_POTION = POTION_TYPES.register("decay", () -> new Potion(new MobEffectInstance(MobEffects.WITHER, 900))),
        LONG_DECAY_POTION = POTION_TYPES.register("long_decay", () -> new Potion(new MobEffectInstance(MobEffects.WITHER, 1800))),
        STRONG_DECAY_POTION = POTION_TYPES.register("strong_decay", () -> new Potion(new MobEffectInstance(MobEffects.WITHER, 450, 1)));

    public static RegistryObject<Item>
        LEAD_INGOT = addItem("lead_ingot"),
        RAW_LEAD = addItem("raw_lead"),
        LEAD_NUGGET = addItem("lead_nugget"),
        SILVER_INGOT = addItem("silver_ingot"),
        RAW_SILVER = addItem("raw_silver"),
        SILVER_NUGGET = addItem("silver_nugget"),
        PEWTER_BLEND = addItem("pewter_blend"),
        PEWTER_INGOT = addItem("pewter_ingot"),
        PEWTER_NUGGET = addItem("pewter_nugget"),
        PEWTER_INLAY = addItem("pewter_inlay"),
        ARCANE_GOLD_INGOT = addItem("arcane_gold_ingot"),
        ARCANE_GOLD_NUGGET = addItem("arcane_gold_nugget"),
        ELDER_BRICK = addItem("elder_brick"),
        SULFUR = addItem("sulfur"),
        GOLD_INLAY = addItem("gold_inlay"),
        ZOMBIE_HEART = addItem("zombie_heart", new ItemBase(itemProps().rarity(Rarity.UNCOMMON).food(
        	new FoodProperties.Builder()
        		.nutrition(2).saturationMod(1.5f)
        		.effect(() -> new MobEffectInstance(MobEffects.HUNGER, 1800), 0.875f)
        		.effect(() -> new MobEffectInstance(MobEffects.POISON, 900, 1), 1.0f)
        		.build())).setLore("lore.eidolon.zombie_heart")),
        TATTERED_CLOTH = addItem("tattered_cloth"),
        WRAITH_HEART = addItem("wraith_heart", new ItemBase(itemProps()
            .rarity(Rarity.UNCOMMON)).setLore("lore.eidolon.wraith_heart")),
        TOP_HAT = addItem("top_hat", new TopHatItem(itemProps().stacksTo(1).rarity(Rarity.EPIC)).setLore("lore.eidolon.top_hat")),
        BASIC_RING = addItem("basic_ring", new BasicRingItem(itemProps().stacksTo(1))),
        BASIC_AMULET = addItem("basic_amulet", new BasicAmuletItem(itemProps().stacksTo(1))),
        BASIC_BELT = addItem("basic_belt", new BasicBeltItem(itemProps().stacksTo(1))),
        CODEX = addItem("codex", new CodexItem(itemProps().stacksTo(1).rarity(Rarity.UNCOMMON)).setLore("lore.eidolon.codex")),
        SOUL_SHARD = addItem("soul_shard"),
        DEATH_ESSENCE = addItem("death_essence"),
        CRIMSON_ESSENCE = addItem("crimson_essence"),
        FUNGUS_SPROUTS = addItem("fungus_sprouts", itemProps().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.1f).build())),
        WARPED_SPROUTS = addItem("warped_sprouts", itemProps().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.6f).effect(() -> new MobEffectInstance(ANCHORED_EFFECT.get(), 900), 1).build())),
        ENDER_CALX = addItem("ender_calx"),
        TALLOW = addItem("tallow"),
        LESSER_SOUL_GEM = addItem("lesser_soul_gem"),
        UNHOLY_SYMBOL = addItem("unholy_symbol", new UnholySymbolItem(itemProps().rarity(Rarity.UNCOMMON).stacksTo(1))),
        REAPER_SCYTHE = addItem("reaper_scythe", new ReaperScytheItem(itemProps().rarity(Rarity.UNCOMMON))
            .setLore("lore.eidolon.reaper_scythe")),
        CLEAVING_AXE = addItem("cleaving_axe", new CleavingAxeItem(itemProps().rarity(Rarity.UNCOMMON))
            .setLore("lore.eidolon.cleaving_axe")),
        SHADOW_GEM = addItem("shadow_gem"),
        WICKED_WEAVE = addItem("wicked_weave"),
        WARLOCK_HAT = addItem("warlock_hat", new WarlockRobesItem(EquipmentSlot.HEAD, itemProps())),
        WARLOCK_CLOAK = addItem("warlock_cloak", new WarlockRobesItem(EquipmentSlot.CHEST, itemProps())),
        WARLOCK_BOOTS = addItem("warlock_boots", new WarlockRobesItem(EquipmentSlot.FEET, itemProps())),
        SILVER_HELMET = addItem("silver_helmet", new SilverArmorItem(EquipmentSlot.HEAD, itemProps())),
        SILVER_CHESTPLATE = addItem("silver_chestplate", new SilverArmorItem(EquipmentSlot.CHEST, itemProps())),
        SILVER_LEGGINGS = addItem("silver_leggings", new SilverArmorItem(EquipmentSlot.LEGS, itemProps())),
        SILVER_BOOTS = addItem("silver_boots", new SilverArmorItem(EquipmentSlot.FEET, itemProps())),
        SILVER_SWORD = addItem("silver_sword", new SwordItem(Tiers.SilverTier.INSTANCE, 3, -2.4f, itemProps())),
        SILVER_PICKAXE = addItem("silver_pickaxe", new PickaxeItem(Tiers.SilverTier.INSTANCE, 1, -2.4f, itemProps())),
        SILVER_AXE = addItem("silver_axe", new AxeItem(Tiers.SilverTier.INSTANCE, 6, -2.4f, itemProps())),
        SILVER_SHOVEL = addItem("silver_shovel", new ShovelItem(Tiers.SilverTier.INSTANCE, 1.5f, -2.4f, itemProps())),
        SILVER_HOE = addItem("silver_hoe", new HoeItem(Tiers.SilverTier.INSTANCE, 0, -2.4f, itemProps())),
        ATHAME = addItem("athame", new AthameItem(itemProps().stacksTo(1))),
        REVERSAL_PICK = addItem("reversal_pick", new ReversalPickItem(itemProps()
            .rarity(Rarity.UNCOMMON))),
        VOID_AMULET = addItem("void_amulet", new VoidAmuletItem(itemProps()
            .rarity(Rarity.UNCOMMON).stacksTo(1)).setLore("lore.eidolon.void_amulet")),
        WARDED_MAIL = addItem("warded_mail", new WardedMailItem(itemProps()
            .rarity(Rarity.UNCOMMON).stacksTo(1)).setLore("lore.eidolon.warded_mail")),
        SAPPING_SWORD = addItem("sapping_sword", new SappingSwordItem(itemProps()
            .rarity(Rarity.UNCOMMON).stacksTo(1)).setLore("lore.eidolon.sapping_sword")),
        SANGUINE_AMULET = addItem("sanguine_amulet", new SanguineAmuletItem(itemProps()
            .rarity(Rarity.UNCOMMON).stacksTo(1)).setLore("lore.eidolon.sanguine_amulet")),
        ENERVATING_RING = addItem("enervating_ring", new EnervatingRingItem(itemProps()
            .rarity(Rarity.UNCOMMON).stacksTo(1)).setLore("lore.eidolon.enervating_ring")),
        SOULFIRE_WAND = addItem("soulfire_wand", new SoulfireWandItem(itemProps()
            .rarity(Rarity.UNCOMMON).stacksTo(1).durability(253).setNoRepair())
            .setLore("lore.eidolon.soulfire_wand")),
        BONECHILL_WAND = addItem("bonechill_wand", new BonechillWandItem(itemProps()
            .rarity(Rarity.UNCOMMON).stacksTo(1).durability(253).setNoRepair())
            .setLore("lore.eidolon.bonechill_wand")),
        GRAVITY_BELT = addItem("gravity_belt", new GravityBeltItem(itemProps()
            .rarity(Rarity.UNCOMMON).stacksTo(1)).setLore("lore.eidolon.gravity_belt")),
        RESOLUTE_BELT = addItem("resolute_belt", new ResoluteBeltItem(itemProps()
            .rarity(Rarity.UNCOMMON).stacksTo(1)).setLore("lore.eidolon.resolute_belt")),
        PRESTIGIOUS_PALM = addItem("prestigious_palm", new PrestigiousPalmItem(itemProps()
            .rarity(Rarity.UNCOMMON).stacksTo(1)).setLore("lore.eidolon.prestigious_palm")),
        MIND_SHIELDING_PLATE = addItem("mind_shielding_plate", new MindShieldingPlateItem(itemProps()
            .rarity(Rarity.UNCOMMON).stacksTo(1)).setLore("lore.eidolon.mind_shielding_plate")),
        GLASS_HAND = addItem("glass_hand", new GlassHandItem(itemProps()
            .rarity(Rarity.RARE).stacksTo(1)).setLore("lore.eidolon.glass_hand")),
        TERMINUS_MIRROR = addItem("terminus_mirror", new TerminusMirrorItem(itemProps()
            .rarity(Rarity.RARE).stacksTo(1)).setLore("lore.eidolon.terminus_mirror")),
        ANGELS_SIGHT = addItem("angels_sight", new AngelSightItem(itemProps()
            .rarity(Rarity.RARE).stacksTo(1)).setLore("lore.eidolon.angels_sight")),
        WITHERED_HEART = addItem("withered_heart", new ItemBase(itemProps().rarity(Rarity.RARE).food(
        	new FoodProperties.Builder()
        		.nutrition(2).saturationMod(1.5f)
        		.effect(() -> new MobEffectInstance(MobEffects.HUNGER, 1800), 0.875f)
        		.effect(() -> new MobEffectInstance(MobEffects.WITHER, 900, 1), 1.0f)
        		.build())).setLore("lore.eidolon.withered_heart")),
        IMBUED_BONES = addItem("imbued_bones", itemProps().rarity(Rarity.UNCOMMON)),
        SUMMONING_STAFF = addItem("summoning_staff", new SummoningStaffItem(itemProps().rarity(Rarity.RARE))),
        DEATHBRINGER_SCYTHE = addItem("deathbringer_scythe", new DeathbringerScytheItem(itemProps().rarity(Rarity.RARE))
        	.setLore("lore.eidolon.deathbringer_scythe")),
        SOULBONE_AMULET = addItem("soulbone_amulet", new SoulboneAmuletItem(itemProps()
            .rarity(Rarity.RARE).stacksTo(1)).setLore("lore.eidolon.soulbone_amulet")),
        BONELORD_HELM = addItem("bonelord_helm", new BonelordArmorItem(EquipmentSlot.HEAD, itemProps().rarity(Rarity.RARE))),
        BONELORD_CHESTPLATE = addItem("bonelord_chestplate", new BonelordArmorItem(EquipmentSlot.CHEST, itemProps().rarity(Rarity.RARE))),
        BONELORD_GREAVES = addItem("bonelord_greaves", new BonelordArmorItem(EquipmentSlot.LEGS, itemProps().rarity(Rarity.RARE))),
        PAROUSIA_DISC = addItem("music_disc_parousia", new RecordItem(9, () -> PAROUSIA.get(),
            itemProps().stacksTo(1).tab(CreativeModeTab.TAB_MISC).rarity(Rarity.RARE))),
        RAVEN_FEATHER = addItem("raven_feather"),
        RAVEN_CLOAK = addItem("raven_cloak", new RavenCloakItem(itemProps().rarity(Rarity.RARE))),
        ALCHEMISTS_TONGS = addItem("alchemists_tongs", new TongsItem(itemProps().stacksTo(1))),
        MERAMMER_RESIN = addItem("merammer_resin"),
        MAGIC_INK = addItem("magic_ink"),
        MAGICIANS_WAX = addItem("magicians_wax"),
        ARCANE_SEAL = addItem("arcane_seal"),
        PARCHMENT = addItem("parchment"),
        NOTETAKING_TOOLS = addItem("notetaking_tools", new NotetakingToolsItem(itemProps().stacksTo(16))),
        RESEARCH_NOTES = addItem("research_notes", new ResearchNotesItem(itemProps().rarity(Rarity.UNCOMMON).stacksTo(1))),
        COMPLETED_RESEARCH = addItem("completed_research", new CompletedResearchItem(itemProps().rarity(Rarity.UNCOMMON).stacksTo(1))),
        RED_CANDY = addItem("red_candy", new ItemBase(itemProps().rarity(Rarity.COMMON).food(
        	new FoodProperties.Builder()
        		.nutrition(2).saturationMod(2)
        		.build())).setLore(ChatFormatting.RED, "lore.eidolon.red_candy")),
        GRAPE_CANDY = addItem("grape_candy", new ItemBase(itemProps().rarity(Rarity.COMMON).food(
        	new FoodProperties.Builder()
        		.nutrition(2).saturationMod(2)
        		.build())).setLore(ChatFormatting.LIGHT_PURPLE, "lore.eidolon.grape_candy"));

    public static RegistryObject<Block>
        LEAD_ORE = addBlock("lead_ore", blockProps(Material.STONE, MaterialColor.STONE)
            .sound(SoundType.STONE).strength(2.8f, 3.0f).requiresCorrectToolForDrops()),
	    DEEP_LEAD_ORE = addBlock("deep_lead_ore", blockProps(Material.STONE, MaterialColor.DEEPSLATE)
	    	.sound(SoundType.DEEPSLATE).strength(3.2f, 3.0f).requiresCorrectToolForDrops()),
        LEAD_BLOCK = addBlock("lead_block", blockProps(Material.STONE, MaterialColor.TERRACOTTA_PURPLE)
            .sound(SoundType.METAL).strength(3.0f, 3.0f).requiresCorrectToolForDrops()),
        RAW_LEAD_BLOCK = addBlock("raw_lead_block", blockProps(Material.STONE, MaterialColor.TERRACOTTA_PURPLE)
        	.sound(SoundType.DEEPSLATE).strength(2.4f, 3.0f).requiresCorrectToolForDrops()),
        SILVER_ORE = addBlock("silver_ore", blockProps(Material.STONE, MaterialColor.STONE)
            .sound(SoundType.STONE).strength(3.2f, 3.0f).requiresCorrectToolForDrops()),
        DEEP_SILVER_ORE = addBlock("deep_silver_ore", blockProps(Material.STONE, MaterialColor.DEEPSLATE)
        	.sound(SoundType.DEEPSLATE).strength(3.6f, 3.0f).requiresCorrectToolForDrops()),
        SILVER_BLOCK = addBlock("silver_block", blockProps(Material.STONE, MaterialColor.COLOR_LIGHT_BLUE)
            .sound(SoundType.METAL).strength(3.0f, 3.0f).requiresCorrectToolForDrops()),
        RAW_SILVER_BLOCK = addBlock("raw_silver_block", blockProps(Material.STONE, MaterialColor.COLOR_LIGHT_BLUE)
        		.sound(SoundType.STONE).strength(2.4f, 3.0f).requiresCorrectToolForDrops()),
        PEWTER_BLOCK = addBlock("pewter_block", blockProps(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY)
            .sound(SoundType.METAL).strength(4.0f, 4.0f).requiresCorrectToolForDrops()),
        ARCANE_GOLD_BLOCK = addBlock("arcane_gold_block", blockProps(Material.STONE, MaterialColor.GOLD)
            .sound(SoundType.METAL).strength(3.0f, 4.0f).requiresCorrectToolForDrops()),
        SHADOW_GEM_BLOCK = addBlock("shadow_gem_block", blockProps(Material.STONE, MaterialColor.COLOR_PURPLE)
            .sound(SoundType.METAL).strength(3.0f, 4.0f).requiresCorrectToolForDrops()),
        WOODEN_ALTAR = addBlock("wooden_altar", new TableBlockBase(blockProps(Material.WOOD, MaterialColor.WOOD)
            .sound(SoundType.WOOD).strength(1.6f, 3.0f))),
        STONE_ALTAR = addBlock("stone_altar", new TableBlockBase(blockProps(Material.STONE, MaterialColor.STONE)
            .sound(SoundType.STONE).strength(2.8f, 3.0f)
            .requiresCorrectToolForDrops().noOcclusion())
            .setMainShape(Shapes.or(
                Shapes.box(0, 0.375, 0, 1, 1, 1),
                Shapes.box(0.0625, 0.125, 0.0625, 0.9375, 0.375, 0.9375)
            ))),
        CANDLE = addBlock("candle", new CandleBlock(blockProps(Material.DECORATION, MaterialColor.TERRACOTTA_WHITE)
            .sound(SoundType.STONE).lightLevel((state) -> 15).strength(0.6f, 0.8f).noOcclusion())),
        CANDLESTICK = addBlock("candlestick", new CandlestickBlock(blockProps(Material.METAL, MaterialColor.GOLD)
            .sound(SoundType.STONE).lightLevel((state) -> 15).strength(1.2f, 2.0f).noOcclusion())),
        MAGIC_CANDLE = addBlock("magic_candle", new CandleBlock(blockProps(Material.DECORATION, MaterialColor.TERRACOTTA_RED)
        	.sound(SoundType.STONE).lightLevel((state) -> 15).strength(0.6f, 0.8f).noOcclusion())),
        MAGIC_CANDLESTICK = addBlock("magic_candlestick", new CandlestickBlock(blockProps(Material.DECORATION, MaterialColor.GOLD)
        	.sound(SoundType.STONE).lightLevel((state) -> 15).strength(1.2f, 2.0f).noOcclusion())),
        STRAW_EFFIGY = addBlock("straw_effigy", new EffigyBlock(blockProps(Material.PLANT, MaterialColor.COLOR_YELLOW)
            .sound(SoundType.WOOD).strength(1.4f, 2.0f)
            .noOcclusion()).setShape(
                Shapes.box(0.28125, 0, 0.28125, 0.71875, 1, 0.71875)
            )),
        GOBLET = addBlock("goblet", new GobletBlock(blockProps(Material.METAL, MaterialColor.GOLD)
            .sound(SoundType.METAL).strength(1.4f, 2.0f).requiresCorrectToolForDrops()
            .noOcclusion()).setShape(Shapes.box(0.3125, 0, 0.3125, 0.6875, 0.5, 0.6875))),
        UNHOLY_EFFIGY = addBlock("unholy_effigy", new EffigyBlock(blockProps(Material.STONE, MaterialColor.STONE)
            .sound(SoundType.STONE).strength(2.8f, 3.0f)
            .requiresCorrectToolForDrops()
            .noOcclusion()).setShape(
                Shapes.box(0.25, 0, 0.25, 0.75, 1, 0.75)
            )),
        WORKTABLE = addBlock("worktable", new WorktableBlock(blockProps(Material.WOOD, MaterialColor.WOOD)
            .sound(SoundType.WOOD).strength(1.6f, 3.0f)
            .noOcclusion()).setShape(Shapes.or(
                Shapes.box(0, 0, 0, 1, 0.25, 1),
                Shapes.box(0.125, 0.25, 0.125, 0.875, 0.625, 0.875),
                Shapes.box(0, 0.625, 0, 1, 1, 1)
            ))),
        RESEARCH_TABLE = addBlock("research_table", new ResearchTableBlock(blockProps(Material.WOOD, MaterialColor.WOOD)
            .sound(SoundType.WOOD).strength(1.6f, 3.0f)
            .noOcclusion()).setShape(Shapes.or(
                Shapes.box(0, 0, 0, 1, 0.25, 1),
                Shapes.box(0.125, 0.25, 0.125, 0.875, 0.625, 0.875),
                Shapes.box(0, 0.625, 0, 1, 1, 1)
            ))),
        PLINTH = addBlock("plinth", new PillarBlockBase(blockProps(Material.STONE, MaterialColor.STONE)
            .sound(SoundType.STONE).strength(2.0f, 3.0f)
            .requiresCorrectToolForDrops().noOcclusion())
            .setShape(Shapes.box(0.25, 0, 0.25, 0.75, 1, 0.75))),
		OBELISK = addBlock("obelisk", new PillarBlockBase(blockProps(Material.STONE, MaterialColor.STONE)
            .sound(SoundType.STONE).strength(2.0f, 3.0f)
            .requiresCorrectToolForDrops().noOcclusion())
            .setShape(Shapes.box(0.125, 0, 0.125, 0.875, 1, 0.875))),
        BRAZIER = addBlock("brazier", new BrazierBlock(blockProps(Material.WOOD, MaterialColor.METAL)
            .sound(SoundType.METAL).strength(2.5f, 3.0f)
            .noOcclusion())
            .setShape(Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.75, 0.8125))),
        CRUCIBLE = addBlock("crucible", new CrucibleBlock(blockProps(Material.METAL, MaterialColor.METAL)
            .sound(SoundType.METAL).strength(4.0f, 3.0f)
            .requiresCorrectToolForDrops().noOcclusion())
            .setShape(Shapes.or(
                Shapes.box(0.0625, 0.875, 0.0625, 0.1875, 1, 0.9375),
                Shapes.box(0.8125, 0.875, 0.0625, 0.9375, 1, 0.9375),
                Shapes.box(0.0625, 0.875, 0.0625, 0.9375, 1, 0.1875),
                Shapes.box(0.0625, 0.875, 0.8125, 0.9375, 1, 0.9375),
                Shapes.box(0, 0.125, 0, 0.125, 0.875, 1),
                Shapes.box(0.875, 0.125, 0, 1, 0.875, 1),
                Shapes.box(0, 0.125, 0, 1, 0.875, 0.125),
                Shapes.box(0, 0.125, 0.875, 1, 0.875, 1),
                Shapes.box(0.0625, 0, 0.0625, 0.9375, 0.125, 0.9375)
            ))),
        STONE_HAND = addBlock("stone_hand", new HandBlock(blockProps(Material.STONE, MaterialColor.STONE)
            .sound(SoundType.STONE).strength(2.0f, 3.0f)
            .requiresCorrectToolForDrops().noOcclusion())
            .setShape(Shapes.box(0.25, 0, 0.25, 0.75, 0.75, 0.75))),
        ENCHANTED_ASH = addBlock("enchanted_ash", new EnchantedAshBlock(blockProps(Material.DECORATION, MaterialColor.TERRACOTTA_WHITE)
            .sound(SoundType.STONE).strength(0.0f, 0.75f).noOcclusion())
            .setShape(Shapes.empty())),
        NECROTIC_FOCUS = addBlock("necrotic_focus", new NecroticFocusBlock(blockProps(Material.STONE, MaterialColor.STONE)
            .sound(SoundType.STONE).strength(2.8f, 3.0f)
            .requiresCorrectToolForDrops().noOcclusion())
            .setShape(Shapes.box(0.25, 0, 0.25, 0.75, 0.75, 0.75))),
        PLANTER = addBlock("planter", new BlockBase(blockProps(Material.WOOD, MaterialColor.WOOD)
            .sound(SoundType.WOOD).strength(2.0f, 3.0f)
            .noOcclusion())
            .setShape(Shapes.or(
                Shapes.box(0, 0.25, 0, 1, 1, 1),
                Shapes.box(0.25, 0, 0.25, 0.75, 0.25, 0.75)))),
        MERAMMER_ROOT = addBlock("merammer_root", new HerbBlockBase(blockProps(Material.PLANT, MaterialColor.GRASS)
            .sound(SoundType.GRASS).noOcclusion())),
        AVENNIAN_SPRIG = addBlock("avennian_sprig", new HerbBlockBase(blockProps(Material.PLANT, MaterialColor.GRASS)
            .sound(SoundType.GRASS).noOcclusion())),
        OANNA_BLOOM = addBlock("oanna_bloom", new HerbBlockBase(blockProps(Material.PLANT, MaterialColor.GRASS)
            .sound(SoundType.GRASS).noOcclusion())),
        SILDRIAN_SEED = addBlock("sildrian_seed", new HerbBlockBase(blockProps(Material.PLANT, MaterialColor.GRASS)
            .sound(SoundType.GRASS).noOcclusion())),
        ILLWOOD_SAPLING = addBlock("illwood_sapling", new BushBlock(blockProps(Material.PLANT, MaterialColor.GRASS)
        	.sound(SoundType.GRASS).noOcclusion().noCollission())),
        ILLWOOD_LEAVES = addBlock("illwood_leaves", new BlockBase(blockProps(Material.PLANT, MaterialColor.GRASS)
        	.sound(SoundType.GRASS).noOcclusion())),
        ILLWOOD_LOG = addBlock("illwood_log", new RotatedPillarBlock(blockProps(Material.WOOD, MaterialColor.WOOD)
            .sound(SoundType.WOOD).strength(1.6f, 3.0f))),
        ILLWOOD_BARK = addBlock("illwood_bark", new RotatedPillarBlock(blockProps(Material.WOOD, MaterialColor.WOOD)
            .sound(SoundType.WOOD).strength(1.6f, 3.0f))),
        STRIPPED_ILLWOOD_LOG = addBlock("stripped_illwood_log", new RotatedPillarBlock(blockProps(Material.WOOD, MaterialColor.WOOD)
            .sound(SoundType.WOOD).strength(1.4f, 3.0f))),
		STRIPPED_ILLWOOD_BARK = addBlock("stripped_illwood_bark", new RotatedPillarBlock(blockProps(Material.WOOD, MaterialColor.WOOD)
            .sound(SoundType.WOOD).strength(1.4f, 3.0f))),
        SOUL_ENCHANTER = addBlock("soul_enchanter", new SoulEnchanterBlock(blockProps(Material.STONE, MaterialColor.PODZOL)
            .sound(SoundType.STONE).strength(5.0f, 1200.0f)
            .requiresCorrectToolForDrops().noOcclusion())
            .setShape(Shapes.box(0, 0, 0, 1, 0.75, 1))),
        WOODEN_STAND = addBlock("wooden_brewing_stand", new WoodenStandBlock(blockProps(Material.METAL, MaterialColor.WOOD)
            .sound(SoundType.STONE).strength(2.0f, 3.0f)
            .noOcclusion())),
        INCUBATOR = addBlock("incubator", new TwoHighBlockBase(blockProps(Material.METAL, MaterialColor.METAL)
            .sound(SoundType.GLASS).strength(2.0f, 3.0f)
            .noOcclusion()).setShape(Shapes.box(0.0625, 0, 0.0625, 0.9375, 1, 0.9375))),
        GLASS_TUBE = addBlock("glass_tube", new PipeBlock(blockProps(Material.GLASS, MaterialColor.COLOR_LIGHT_BLUE)
            .sound(SoundType.GLASS).strength(1.0f, 1.5f).noOcclusion())),
        CISTERN = addBlock("cistern", new CisternBlock(blockProps(Material.GLASS, MaterialColor.COLOR_LIGHT_BLUE)
            .sound(SoundType.GLASS).strength(1.5f, 1.5f).noOcclusion())
            .setShape(Shapes.box(0.0625, 0, 0.0625, 0.9375, 1, 0.9375)));
    public static DecoBlockPack
        SMOOTH_STONE_BRICK = new DecoBlockPack(BLOCKS, "smooth_stone_bricks", blockProps(Material.STONE, MaterialColor.STONE)
            .sound(SoundType.STONE).requiresCorrectToolForDrops().strength(2.0f, 3.0f))
            .addWall(),
        SMOOTH_STONE_TILES = new DecoBlockPack(BLOCKS, "smooth_stone_tiles", blockProps(Material.STONE, MaterialColor.STONE)
            .sound(SoundType.STONE).requiresCorrectToolForDrops().strength(2.0f, 3.0f)),
        SMOOTH_STONE_MASONRY = new DecoBlockPack(BLOCKS, "smooth_stone_masonry", blockProps(Material.STONE, MaterialColor.STONE)
            .sound(SoundType.STONE).requiresCorrectToolForDrops().strength(1.6f, 3.0f)),
        POLISHED_PLANKS = new DecoBlockPack(BLOCKS, "polished_planks", blockProps(Material.WOOD, MaterialColor.WOOD)
            .sound(SoundType.WOOD).strength(1.6f, 3.0f))
            .addFence(),
        ILLWOOD_PLANKS = new DecoBlockPack(BLOCKS, "illwood_planks", blockProps(Material.WOOD, MaterialColor.WOOD)
            .sound(SoundType.WOOD).strength(1.6f, 3.0f))
            .addFence(),
        ELDER_BRICKS = new DecoBlockPack(BLOCKS, "elder_bricks", blockProps(Material.STONE, MaterialColor.TERRACOTTA_ORANGE)
            .sound(SoundType.STONE).requiresCorrectToolForDrops().strength(3.0f, 3.0f))
            .addWall(),
        ELDER_MASONRY = new DecoBlockPack(BLOCKS, "elder_masonry", blockProps(Material.STONE, MaterialColor.TERRACOTTA_ORANGE)
            .sound(SoundType.STONE).requiresCorrectToolForDrops().strength(2.4f, 3.0f)),
        BONE_PILE = new DecoBlockPack(BLOCKS, "bone_pile", blockProps(Material.STONE, MaterialColor.QUARTZ)
            .sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops().strength(1.6f, 3.0f));
    public static RegistryObject<Block>
        POLISHED_WOOD_PILLAR = addBlock("polished_wood_pillar", new RotatedPillarBlock(blockProps(Material.WOOD, MaterialColor.WOOD)
            .strength(1.6f, 3.0f))),
        SMOOTH_STONE_ARCH = addBlock("smooth_stone_arch", new PillarBlockBase(blockProps(Material.STONE, MaterialColor.STONE)
            .sound(SoundType.STONE).strength(2.0f, 3.0f).requiresCorrectToolForDrops())),
        MOSSY_SMOOTH_STONE_BRICKS = addBlock("mossy_smooth_stone_bricks", blockProps(Material.STONE, MaterialColor.STONE)
            .sound(SoundType.STONE).strength(2.0f, 3.0f).requiresCorrectToolForDrops()),
        ELDER_BRICKS_EYE = addBlock("elder_bricks_eye", blockProps(Material.STONE, MaterialColor.TERRACOTTA_ORANGE)
            .sound(SoundType.STONE).strength(3.0f, 3.0f).requiresCorrectToolForDrops()),
		ELDER_PILLAR = addBlock("elder_pillar", new PillarBlockBase(blockProps(Material.STONE, MaterialColor.TERRACOTTA_ORANGE)
            .sound(SoundType.STONE).strength(3.0f, 3.0f)
            .requiresCorrectToolForDrops()));

    public static RegistryObject<EntityType<ZombieBruteEntity>>
        ZOMBIE_BRUTE = addEntity("zombie_brute", 7969893, 44975, 1.2f, 2.5f, ZombieBruteEntity::new, MobCategory.MONSTER);
    public static RegistryObject<EntityType<WraithEntity>>
        WRAITH = addEntity("wraith", 0x706e6b, 0xadacbd, 0.6f, 1.9f, WraithEntity::new, MobCategory.MONSTER);
    public static RegistryObject<EntityType<SoulfireProjectileEntity>>
        SOULFIRE_PROJECTILE = addEntity("soulfire_projectile", 0.4f, 0.4f, SoulfireProjectileEntity::new, MobCategory.MISC);
    public static RegistryObject<EntityType<BonechillProjectileEntity>>
        BONECHILL_PROJECTILE = addEntity("bonechill_projectile", 0.4f, 0.4f, BonechillProjectileEntity::new, MobCategory.MISC);
    public static RegistryObject<EntityType<NecromancerSpellEntity>>
        NECROMANCER_SPELL = addEntity("necromancer_spell", 0.4f, 0.4f, NecromancerSpellEntity::new, MobCategory.MISC);
    public static RegistryObject<EntityType<ChantCasterEntity>>
        CHANT_CASTER = addEntity("chant_caster", 0.1f, 0.1f, ChantCasterEntity::new, MobCategory.MISC);
    public static RegistryObject<EntityType<AngelArrowEntity>>
        ANGEL_ARROW = addEntity("angel_arrow", 0.5f, 0.5f, AngelArrowEntity::new, MobCategory.MISC);
    public static RegistryObject<EntityType<NecromancerEntity>>
        NECROMANCER = addEntity("necromancer", 0x69255e, 0x9ce8ff, 0.6f, 1.9f, NecromancerEntity::new, MobCategory.MONSTER);
    public static RegistryObject<EntityType<RavenEntity>>
        RAVEN = addEntity("raven", 0x1e1f24, 0x404f66, 0.375f, 0.5f, RavenEntity::new, MobCategory.CREATURE);
    public static RegistryObject<EntityType<SlimySlugEntity>>
    	SLIMY_SLUG = addEntity("slimy_slug", 0xdbe388, 0x5f9e42, 0.5f, 0.25f, SlimySlugEntity::new, MobCategory.CREATURE);

    public static RegistryObject<MenuType<WorktableContainer>>
        WORKTABLE_CONTAINER = addContainer("worktable", WorktableContainer::new);
    public static RegistryObject<MenuType<SoulEnchanterContainer>>
        SOUL_ENCHANTER_CONTAINER = addContainer("soul_enchanter", SoulEnchanterContainer::new);
    public static RegistryObject<MenuType<WoodenBrewingStandContainer>>
        WOODEN_STAND_CONTAINER = addContainer("wooden_brewing_stand", WoodenBrewingStandContainer::new);
    public static RegistryObject<MenuType<ResearchTableContainer>>
    	RESEARCH_TABLE_CONTAINER = addContainer("research_table", ResearchTableContainer::new);

    public static RegistryObject<RecipeSerializer<WorktableRecipe>>
        WORKTABLE_RECIPE = RECIPE_TYPES.register("worktable", () -> new WorktableRecipe.Serializer());
    public static RegistryObject<RecipeSerializer<CrucibleRecipe>>
        CRUCIBLE_RECIPE = RECIPE_TYPES.register("crucible", () -> new CrucibleRecipe.Serializer());
    
    public static RegistryObject<Attribute> 
    	MAX_SOUL_HEARTS = ATTRIBUTES.register("max_soul_hearts", () -> new RangedAttribute(Eidolon.MODID + ".max_soul_hearts", Config.MAX_ETHEREAL_HEALTH.get(), 0, 2000).setSyncable(true)),
    	PERSISTENT_SOUL_HEARTS = ATTRIBUTES.register("persistent_soul_hearts", () -> new RangedAttribute(Eidolon.MODID + ".persistent_soul_hearts", 0, 0, 2000).setSyncable(true));

    
    @SubscribeEvent
    public void addCustomAttributes(EntityAttributeModificationEvent event) {
    	for (EntityType<? extends LivingEntity> t : event.getTypes()) {
    		if (event.has(t, Attributes.MAX_HEALTH)) {
    			event.add(t, Registry.PERSISTENT_SOUL_HEARTS.get());
    			event.add(t, Registry.MAX_SOUL_HEARTS.get());
    		}
    	}
    }
    
    public static void init() {
        ATTRIBUTES.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        POTIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
        POTION_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        PARTICLES.register(FMLJavaModLoadingContext.get().getModEventBus());
        SOUND_EVENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        RECIPE_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static void addBrewingRecipes() {
        PotionBrewingMixin.callAddMix(Potions.WATER, Registry.FUNGUS_SPROUTS.get(), Potions.AWKWARD);
        PotionBrewingMixin.callAddMix(Potions.AWKWARD, Registry.WRAITH_HEART.get(), Registry.CHILLED_POTION.get());
        PotionBrewingMixin.callAddMix(Registry.CHILLED_POTION.get(), Items.REDSTONE, Registry.LONG_CHILLED_POTION.get());
        PotionBrewingMixin.callAddMix(Potions.AWKWARD, Registry.WARPED_SPROUTS.get(), Registry.ANCHORED_POTION.get());
        PotionBrewingMixin.callAddMix(Registry.ANCHORED_POTION.get(), Items.REDSTONE, Registry.LONG_ANCHORED_POTION.get());
        PotionBrewingMixin.callAddMix(Potions.AWKWARD, Items.NAUTILUS_SHELL, Registry.REINFORCED_POTION.get());
        PotionBrewingMixin.callAddMix(Registry.REINFORCED_POTION.get(), Items.REDSTONE, Registry.LONG_REINFORCED_POTION.get());
        PotionBrewingMixin.callAddMix(Registry.REINFORCED_POTION.get(), Items.GLOWSTONE, Registry.STRONG_REINFORCED_POTION.get());
        PotionBrewingMixin.callAddMix(Potions.AWKWARD, Registry.TATTERED_CLOTH.get(), Registry.VULNERABLE_POTION.get());
        PotionBrewingMixin.callAddMix(Registry.VULNERABLE_POTION.get(), Items.REDSTONE, Registry.LONG_VULNERABLE_POTION.get());
        PotionBrewingMixin.callAddMix(Registry.VULNERABLE_POTION.get(), Items.GLOWSTONE, Registry.STRONG_VULNERABLE_POTION.get());
        PotionBrewingMixin.callAddMix(Potions.AWKWARD, Registry.DEATH_ESSENCE.get(), Registry.UNDEATH_POTION.get());
        PotionBrewingMixin.callAddMix(Registry.UNDEATH_POTION.get(), Items.REDSTONE, Registry.LONG_UNDEATH_POTION.get());
        PotionBrewingMixin.callAddMix(Potions.AWKWARD, Registry.WITHERED_HEART.get(), Registry.DECAY_POTION.get());
        PotionBrewingMixin.callAddMix(Registry.DECAY_POTION.get(), Items.REDSTONE, Registry.LONG_DECAY_POTION.get());
        PotionBrewingMixin.callAddMix(Registry.DECAY_POTION.get(), Items.GLOWSTONE, Registry.STRONG_DECAY_POTION.get());
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientInit() {
    }

    public static BlockEntityType<HandTileEntity> HAND_TILE_ENTITY;
    public static BlockEntityType<BrazierTileEntity> BRAZIER_TILE_ENTITY;
    public static BlockEntityType<NecroticFocusTileEntity> NECROTIC_FOCUS_TILE_ENTITY;
    public static BlockEntityType<CrucibleTileEntity> CRUCIBLE_TILE_ENTITY;
    public static BlockEntityType<EffigyTileEntity> EFFIGY_TILE_ENTITY;
    public static BlockEntityType<SoulEnchanterTileEntity> SOUL_ENCHANTER_TILE_ENTITY;
    public static BlockEntityType<WoodenStandTileEntity> WOODEN_STAND_TILE_ENTITY;
    public static BlockEntityType<GobletTileEntity> GOBLET_TILE_ENTITY;
    public static BlockEntityType<CisternTileEntity> CISTERN_TILE_ENTITY;
    public static BlockEntityType<PipeTileEntity> PIPE_TILE_ENTITY;
    public static BlockEntityType<ResearchTableTileEntity> RESEARCH_TABLE_TILE_ENTITY;

    @SubscribeEvent
    public void registerTiles(RegistryEvent.Register<BlockEntityType<?>> evt) {
        HAND_TILE_ENTITY = addTileEntity(evt.getRegistry(), "hand_tile", HandTileEntity::new, STONE_HAND.get());
        BRAZIER_TILE_ENTITY = addTileEntity(evt.getRegistry(), "brazier_tile", BrazierTileEntity::new, BRAZIER.get());
        NECROTIC_FOCUS_TILE_ENTITY = addTileEntity(evt.getRegistry(), "necrotic_focus", NecroticFocusTileEntity::new, NECROTIC_FOCUS.get());
        CRUCIBLE_TILE_ENTITY = addTileEntity(evt.getRegistry(), "crucible", CrucibleTileEntity::new, CRUCIBLE.get());
        EFFIGY_TILE_ENTITY = addTileEntity(evt.getRegistry(), "effigy", EffigyTileEntity::new, STRAW_EFFIGY.get(), UNHOLY_EFFIGY.get());
        SOUL_ENCHANTER_TILE_ENTITY = addTileEntity(evt.getRegistry(), "soul_enchanter", SoulEnchanterTileEntity::new, SOUL_ENCHANTER.get());
        WOODEN_STAND_TILE_ENTITY = addTileEntity(evt.getRegistry(), "wooden_brewing_stand", WoodenStandTileEntity::new, WOODEN_STAND.get());
        GOBLET_TILE_ENTITY = addTileEntity(evt.getRegistry(), "goblet", GobletTileEntity::new, GOBLET.get());
        CISTERN_TILE_ENTITY = addTileEntity(evt.getRegistry(), "cistern", CisternTileEntity::new, CISTERN.get());
        PIPE_TILE_ENTITY = addTileEntity(evt.getRegistry(), "pipe", PipeTileEntity::new, GLASS_TUBE.get());
        RESEARCH_TABLE_TILE_ENTITY = addTileEntity(evt.getRegistry(), "research_table", ResearchTableTileEntity::new, RESEARCH_TABLE.get());
    }

    public static DamageSource RITUAL_DAMAGE = new DamageSource("ritual").bypassArmor().bypassMagic();
    public static DamageSource FROST_DAMAGE = new DamageSource("frost");
    
    public void registerCaps(RegisterCapabilitiesEvent event) {
    	event.register(IReputation.class);
        event.register(IKnowledge.class);
        event.register(ISoul.class);
        event.register(IPlayerData.class);
    }

    @SubscribeEvent
    public void defineAttributes(EntityAttributeCreationEvent event) {
    	event.put(Registry.ZOMBIE_BRUTE.get(), ZombieBruteEntity.createAttributes());
    	event.put(Registry.WRAITH.get(), WraithEntity.createAttributes());
    	event.put(Registry.NECROMANCER.get(), NecromancerEntity.createAttributes());
    	event.put(Registry.RAVEN.get(), RavenEntity.createAttributes());
    	event.put(Registry.SLIMY_SLUG.get(), SlimySlugEntity.createAttributes());
    }

    static DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Eidolon.MODID);

    public static RegistryObject<FlameParticleType>
        FLAME_PARTICLE = PARTICLES.register("flame_particle", FlameParticleType::new);
    public static RegistryObject<SmokeParticleType>
        SMOKE_PARTICLE = PARTICLES.register("smoke_particle", SmokeParticleType::new);
    public static RegistryObject<SparkleParticleType>
        SPARKLE_PARTICLE = PARTICLES.register("sparkle_particle", SparkleParticleType::new);
    public static RegistryObject<WispParticleType>
        WISP_PARTICLE = PARTICLES.register("wisp_particle", WispParticleType::new);
    public static RegistryObject<BubbleParticleType>
        BUBBLE_PARTICLE = PARTICLES.register("bubble_particle", BubbleParticleType::new);
    public static RegistryObject<LineWispParticleType>
        LINE_WISP_PARTICLE = PARTICLES.register("line_wisp_particle", LineWispParticleType::new);
    public static RegistryObject<SteamParticleType>
        STEAM_PARTICLE = PARTICLES.register("steam_particle", SteamParticleType::new);
    public static RegistryObject<SignParticleType>
        SIGN_PARTICLE = PARTICLES.register("sign_particle", SignParticleType::new);
    public static RegistryObject<SlashParticleType>
    	SLASH_PARTICLE = PARTICLES.register("slash_particle", SlashParticleType::new);
    public static RegistryObject<GlowingSlashParticleType>
		GLOWING_SLASH_PARTICLE = PARTICLES.register("glowing_slash_particle", GlowingSlashParticleType::new);
    public static RegistryObject<RuneParticleType>
    	RUNE_PARTICLE = PARTICLES.register("rune_particle", RuneParticleType::new);

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void registerFactories(ParticleFactoryRegisterEvent evt) {
        Minecraft.getInstance().particleEngine.register(FLAME_PARTICLE.get(), FlameParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(SMOKE_PARTICLE.get(), SmokeParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(SPARKLE_PARTICLE.get(), SparkleParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(WISP_PARTICLE.get(), WispParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(BUBBLE_PARTICLE.get(), BubbleParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(STEAM_PARTICLE.get(), SteamParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(LINE_WISP_PARTICLE.get(), LineWispParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(SIGN_PARTICLE.get(), (sprite) -> new SignParticleType.Factory());
        Minecraft.getInstance().particleEngine.register(SLASH_PARTICLE.get(), SlashParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(GLOWING_SLASH_PARTICLE.get(), GlowingSlashParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(RUNE_PARTICLE.get(), (sprite) -> new RuneParticleType.Factory());
    }
}
