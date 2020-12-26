package elucent.eidolon;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import elucent.eidolon.block.BlockBase;
import elucent.eidolon.block.CandleBlock;
import elucent.eidolon.block.CandlestickBlock;
import elucent.eidolon.block.EnchantedAshBlock;
import elucent.eidolon.block.HorizontalBlockBase;
import elucent.eidolon.block.NecroticFocusBlock;
import elucent.eidolon.block.PlinthBlockBase;
import elucent.eidolon.block.SoulEnchanterBlock;
import elucent.eidolon.block.TableBlockBase;
import elucent.eidolon.block.WoodenStandBlock;
import elucent.eidolon.block.WorktableBlock;
import elucent.eidolon.entity.BonechillProjectileEntity;
import elucent.eidolon.entity.ChantCasterEntity;
import elucent.eidolon.entity.NecromancerEntity;
import elucent.eidolon.entity.NecromancerSpellEntity;
import elucent.eidolon.entity.SoulfireProjectileEntity;
import elucent.eidolon.entity.WraithEntity;
import elucent.eidolon.entity.ZombieBruteEntity;
import elucent.eidolon.gui.SoulEnchanterContainer;
import elucent.eidolon.gui.WoodenBrewingStandContainer;
import elucent.eidolon.gui.WorktableContainer;
import elucent.eidolon.item.BonechillWandItem;
import elucent.eidolon.item.CleavingAxeItem;
import elucent.eidolon.item.CodexItem;
import elucent.eidolon.item.ItemBase;
import elucent.eidolon.item.ReaperScytheItem;
import elucent.eidolon.item.ReversalPickItem;
import elucent.eidolon.item.SappingSwordItem;
import elucent.eidolon.item.SoulfireWandItem;
import elucent.eidolon.item.TopHatItem;
import elucent.eidolon.item.UnholySymbolItem;
import elucent.eidolon.item.WarlockRobesItem;
import elucent.eidolon.item.curio.BasicAmuletItem;
import elucent.eidolon.item.curio.BasicBeltItem;
import elucent.eidolon.item.curio.BasicRingItem;
import elucent.eidolon.item.curio.GlassHandItem;
import elucent.eidolon.item.curio.GravityBeltItem;
import elucent.eidolon.item.curio.MindShieldingPlateItem;
import elucent.eidolon.item.curio.PrestigiousPalmItem;
import elucent.eidolon.item.curio.ResoluteBeltItem;
import elucent.eidolon.item.curio.SanguineAmuletItem;
import elucent.eidolon.item.curio.VoidAmuletItem;
import elucent.eidolon.item.curio.WardedMailItem;
import elucent.eidolon.particle.BubbleParticleType;
import elucent.eidolon.particle.FlameParticleType;
import elucent.eidolon.particle.LineWispParticleType;
import elucent.eidolon.particle.SignParticleType;
import elucent.eidolon.particle.SmokeParticleType;
import elucent.eidolon.particle.SparkleParticleType;
import elucent.eidolon.particle.SteamParticleType;
import elucent.eidolon.particle.WispParticleType;
import elucent.eidolon.potion.AnchoredEffect;
import elucent.eidolon.potion.ChilledEffect;
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
import elucent.eidolon.tile.EffigyTileEntity;
import elucent.eidolon.tile.GobletTileEntity;
import elucent.eidolon.tile.HandTileEntity;
import elucent.eidolon.tile.NecroticFocusTileEntity;
import elucent.eidolon.tile.SoulEnchanterTileEntity;
import elucent.eidolon.tile.SoulEnchanterTileRenderer;
import elucent.eidolon.tile.WoodenStandTileEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.item.Rarity;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.particles.ParticleType;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class Registry {
    static Map<String, Block> BLOCK_MAP = new HashMap<>();
    static Map<String, Item> ITEM_MAP = new HashMap<>();
    static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Eidolon.MODID);
    static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Eidolon.MODID);
    static DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Eidolon.MODID);
    static DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Eidolon.MODID);
    static DeferredRegister<Effect> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, Eidolon.MODID);
    static DeferredRegister<Potion> POTION_TYPES = DeferredRegister.create(ForgeRegistries.POTION_TYPES, Eidolon.MODID);
    static DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Eidolon.MODID);
    static DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Eidolon.MODID);

    static Item.Properties itemProps() {
        return new Item.Properties().group(Eidolon.TAB);
    }

    static AbstractBlock.Properties blockProps(Material mat, MaterialColor color) {
        return AbstractBlock.Properties.create(mat, color);
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

    static <T extends Entity> RegistryObject<EntityType<T>> addEntity(String name, float width, float height, EntityType.IFactory<T> factory, EntityClassification kind) {
        EntityType<T> type = EntityType.Builder.<T>create(factory, kind)
            .setTrackingRange(64)
            .setUpdateInterval(1)
            .size(width, height)
            .build(Eidolon.MODID + ":" + name);
        return ENTITIES.register(name, () -> type);
    }

    static <T extends Entity> RegistryObject<EntityType<T>> addEntity(String name, int color1, int color2, float width, float height, EntityType.IFactory<T> factory, EntityClassification kind) {
        EntityType<T> type = EntityType.Builder.<T>create(factory, kind)
            .setTrackingRange(64)
            .setUpdateInterval(1)
            .size(width, height)
            .build(Eidolon.MODID + ":" + name);
        ITEMS.register("spawn_" + name, () -> new SpawnEggItem(type, color1, color2, itemProps().group(ItemGroup.MISC)));
        return ENTITIES.register(name, () -> type);
    }

    static RegistryObject<SoundEvent> addSound(String name) {
        SoundEvent event = new SoundEvent(new ResourceLocation(Eidolon.MODID, name));
        return SOUND_EVENTS.register(name, () -> event);
    }

    static <T extends Container> RegistryObject<ContainerType<T>> addContainer(String name, ContainerType.IFactory<T> factory) {
        return CONTAINERS.register(name, () -> new ContainerType<T>(factory));
    }

    static <T extends TileEntity> TileEntityType<T> addTileEntity(IForgeRegistry<TileEntityType<?>> registry, String name, Supplier<T> factory, Block... blocks) {
        TileEntityType<T> type = TileEntityType.Builder.<T>create(factory, blocks).build(null);
        type.setRegistryName(Eidolon.MODID, name);
        registry.register(type);
        for (Block block : blocks) if (block instanceof BlockBase) ((BlockBase)block).setTile(type);
        return type;
    }

    public static RegistryObject<SoundEvent>
        CAST_SOULFIRE_EVENT = addSound("cast_soulfire"),
        CAST_BONECHILL_EVENT = addSound("cast_bonechill"),
        SPLASH_SOULFIRE_EVENT = addSound("splash_soulfire"),
        SPLASH_BONECHILL_EVENT = addSound("splash_bonechill"),
        BRUTE_LIVING = addSound("brute_living"),
        BRUTE_HURT = addSound("brute_hurt"),
        BRUTE_DEATH = addSound("brute_death"),
		WRAITH_LIVING = addSound("wraith_living"),
        WRAITH_HURT = addSound("wraith_hurt"),
        WRAITH_DEATH = addSound("wraith_death"),
        SELECT_SIGN = addSound("select_sign"),
        CHANT_WORD = addSound("chant_word"),
        PAROUSIA = addSound("parousia");

    public static RegistryObject<Effect>
        CHILLED_EFFECT = POTIONS.register("chilled", () -> new ChilledEffect()),
        ANCHORED_EFFECT = POTIONS.register("anchored", () -> new AnchoredEffect());

    public static RegistryObject<Potion>
        CHILLED_POTION = POTION_TYPES.register("chilled", () -> new Potion(new EffectInstance(CHILLED_EFFECT.get(), 3600))),
        LONG_CHILLED_POTION = POTION_TYPES.register("long_chilled", () -> new Potion(new EffectInstance(CHILLED_EFFECT.get(), 9600))),
        ANCHORED_POTION = POTION_TYPES.register("anchored", () -> new Potion(new EffectInstance(ANCHORED_EFFECT.get(), 3600))),
        LONG_ANCHORED_POTION = POTION_TYPES.register("long_anchored", () -> new Potion(new EffectInstance(ANCHORED_EFFECT.get(), 9600)));

    public static RegistryObject<Item>
        LEAD_INGOT = addItem("lead_ingot"),
        LEAD_NUGGET = addItem("lead_nugget"),
        PEWTER_BLEND = addItem("pewter_blend"),
        PEWTER_INGOT = addItem("pewter_ingot"),
        PEWTER_NUGGET = addItem("pewter_nugget"),
        PEWTER_INLAY = addItem("pewter_inlay"),
        ARCANE_GOLD_INGOT = addItem("arcane_gold_ingot"),
        ARCANE_GOLD_NUGGET = addItem("arcane_gold_nugget"),
        SULFUR = addItem("sulfur"),
        GOLD_INLAY = addItem("gold_inlay"),
        ZOMBIE_HEART = addItem("zombie_heart", new ItemBase(itemProps().rarity(Rarity.UNCOMMON))
            .setLore("lore.eidolon.zombie_heart")),
        TATTERED_CLOTH = addItem("tattered_cloth"),
        WRAITH_HEART = addItem("wraith_heart", new ItemBase(itemProps()
            .rarity(Rarity.UNCOMMON)).setLore("lore.eidolon.wraith_heart")),
        TOP_HAT = addItem("top_hat", new TopHatItem(itemProps().maxStackSize(1).rarity(Rarity.EPIC)).setLore("lore.eidolon.top_hat")),
        BASIC_RING = addItem("basic_ring", new BasicRingItem(itemProps().maxStackSize(1))),
        BASIC_AMULET = addItem("basic_amulet", new BasicAmuletItem(itemProps().maxStackSize(1))),
        BASIC_BELT = addItem("basic_belt", new BasicBeltItem(itemProps().maxStackSize(1))),
        CODEX = addItem("codex", new CodexItem(itemProps().maxStackSize(1).rarity(Rarity.UNCOMMON)).setLore("lore.eidolon.codex")),
        SOUL_SHARD = addItem("soul_shard"),
        DEATH_ESSENCE = addItem("death_essence"),
        CRIMSON_ESSENCE = addItem("crimson_essence"),
        FUNGUS_SPROUTS = addItem("fungus_sprouts", itemProps().food(new Food.Builder().hunger(2).saturation(0.1f).build())),
        WARPED_SPROUTS = addItem("warped_sprouts", itemProps().food(new Food.Builder().hunger(4).saturation(0.6f).effect(() -> new EffectInstance(ANCHORED_EFFECT.get(), 900), 1).build())),
        ENDER_CALX = addItem("ender_calx"),
        TALLOW = addItem("tallow"),
        LESSER_SOUL_GEM = addItem("lesser_soul_gem"),
        UNHOLY_SYMBOL = addItem("unholy_symbol", new UnholySymbolItem(itemProps().rarity(Rarity.UNCOMMON).maxStackSize(1))),
        REAPER_SCYTHE = addItem("reaper_scythe", new ReaperScytheItem(itemProps().rarity(Rarity.UNCOMMON))
            .setLore("lore.eidolon.reaper_scythe")),
        CLEAVING_AXE = addItem("cleaving_axe", new CleavingAxeItem(itemProps().rarity(Rarity.UNCOMMON))
            .setLore("lore.eidolon.cleaving_axe")),
        SHADOW_GEM = addItem("shadow_gem"),
        WICKED_WEAVE = addItem("wicked_weave"),
        WARLOCK_HAT = addItem("warlock_hat", new WarlockRobesItem(EquipmentSlotType.HEAD, itemProps())),
        WARLOCK_CLOAK = addItem("warlock_cloak", new WarlockRobesItem(EquipmentSlotType.CHEST, itemProps())),
        WARLOCK_BOOTS = addItem("warlock_boots", new WarlockRobesItem(EquipmentSlotType.FEET, itemProps())),
        REVERSAL_PICK = addItem("reversal_pick", new ReversalPickItem(itemProps()
            .rarity(Rarity.UNCOMMON))),
        VOID_AMULET = addItem("void_amulet", new VoidAmuletItem(itemProps()
            .rarity(Rarity.UNCOMMON).maxStackSize(1)).setLore("lore.eidolon.void_amulet")),
        WARDED_MAIL = addItem("warded_mail", new WardedMailItem(itemProps()
            .rarity(Rarity.UNCOMMON).maxStackSize(1)).setLore("lore.eidolon.warded_mail")),
        SAPPING_SWORD = addItem("sapping_sword", new SappingSwordItem(itemProps()
            .rarity(Rarity.UNCOMMON).maxStackSize(1)).setLore("lore.eidolon.sapping_sword")),
        SANGUINE_AMULET = addItem("sanguine_amulet", new SanguineAmuletItem(itemProps()
            .rarity(Rarity.UNCOMMON).maxStackSize(1)).setLore("lore.eidolon.sanguine_amulet")),
        SOULFIRE_WAND = addItem("soulfire_wand", new SoulfireWandItem(itemProps()
            .rarity(Rarity.UNCOMMON).maxStackSize(1).maxDamage(253).setNoRepair())
            .setLore("lore.eidolon.soulfire_wand")),
        BONECHILL_WAND = addItem("bonechill_wand", new BonechillWandItem(itemProps()
            .rarity(Rarity.UNCOMMON).maxStackSize(1).maxDamage(253).setNoRepair())
            .setLore("lore.eidolon.bonechill_wand")),
        GRAVITY_BELT = addItem("gravity_belt", new GravityBeltItem(itemProps()
            .rarity(Rarity.UNCOMMON).maxStackSize(1)).setLore("lore.eidolon.gravity_belt")),
        RESOLUTE_BELT = addItem("resolute_belt", new ResoluteBeltItem(itemProps()
            .rarity(Rarity.UNCOMMON).maxStackSize(1)).setLore("lore.eidolon.resolute_belt")),
        PRESTIGIOUS_PALM = addItem("prestigious_palm", new PrestigiousPalmItem(itemProps()
            .rarity(Rarity.UNCOMMON).maxStackSize(1)).setLore("lore.eidolon.prestigious_palm")),
        MIND_SHIELDING_PLATE = addItem("mind_shielding_plate", new MindShieldingPlateItem(itemProps()
            .rarity(Rarity.UNCOMMON).maxStackSize(1)).setLore("lore.eidolon.mind_shielding_plate")),
        GLASS_HAND = addItem("glass_hand", new GlassHandItem(itemProps()
            .rarity(Rarity.RARE).maxStackSize(1)).setLore("lore.eidolon.glass_hand")),
        PAROUSIA_DISC = addItem("music_disc_parousia", new MusicDiscItem(9, () -> PAROUSIA.get(),
            itemProps().maxStackSize(1).group(ItemGroup.MISC).rarity(Rarity.RARE)));

    public static RegistryObject<Block>
        LEAD_ORE = addBlock("lead_ore", blockProps(Material.ROCK, MaterialColor.STONE)
            .hardnessAndResistance(2.8f, 3.0f)
            .harvestLevel(2).harvestTool(ToolType.PICKAXE)),
        LEAD_BLOCK = addBlock("lead_block", blockProps(Material.ROCK, MaterialColor.PURPLE_TERRACOTTA)
            .hardnessAndResistance(3.0f, 3.0f)
            .harvestLevel(2).harvestTool(ToolType.PICKAXE)),
        PEWTER_BLOCK = addBlock("pewter_block", blockProps(Material.ROCK, MaterialColor.LIGHT_GRAY)
            .hardnessAndResistance(4.0f, 4.0f)
            .harvestLevel(2).harvestTool(ToolType.PICKAXE)),
        ARCANE_GOLD_BLOCK = addBlock("arcane_gold_block", blockProps(Material.ROCK, MaterialColor.GOLD)
            .hardnessAndResistance(3.0f, 4.0f)
            .harvestLevel(2).harvestTool(ToolType.PICKAXE)),
        WOODEN_ALTAR = addBlock("wooden_altar", new TableBlockBase(blockProps(Material.WOOD, MaterialColor.WOOD)
            .hardnessAndResistance(1.6f, 3.0f)
            .harvestTool(ToolType.AXE))),
        STONE_ALTAR = addBlock("stone_altar", new TableBlockBase(blockProps(Material.ROCK, MaterialColor.STONE)
            .hardnessAndResistance(2.8f, 3.0f)
            .setRequiresTool().harvestTool(ToolType.PICKAXE).notSolid())
            .setMainShape(VoxelShapes.or(
                VoxelShapes.create(0, 0.375, 0, 1, 1, 1),
                VoxelShapes.create(0.0625, 0.125, 0.0625, 0.9375, 0.375, 0.9375)
            ))),
        CANDLE = addBlock("candle", new CandleBlock(blockProps(Material.MISCELLANEOUS, MaterialColor.WHITE_TERRACOTTA)
            .setLightLevel((state) -> 15).hardnessAndResistance(0.6f, 0.8f).notSolid())),
        CANDLESTICK = addBlock("candlestick", new CandlestickBlock(blockProps(Material.IRON, MaterialColor.GOLD)
            .setLightLevel((state) -> 15).hardnessAndResistance(1.2f, 2.0f).notSolid())),
        STRAW_EFFIGY = addBlock("straw_effigy", new HorizontalBlockBase(blockProps(Material.PLANTS, MaterialColor.YELLOW)
            .hardnessAndResistance(1.4f, 2.0f)
            .notSolid()).setShape(
                VoxelShapes.create(0.28125, 0, 0.28125, 0.71875, 1, 0.71875)
            )),
        GOBLET = addBlock("goblet", new BlockBase(blockProps(Material.IRON, MaterialColor.GOLD)
            .hardnessAndResistance(1.4f, 2.0f).setRequiresTool().harvestTool(ToolType.PICKAXE)
            .notSolid()).setShape(VoxelShapes.create(0.3125, 0, 0.3125, 0.6875, 0.5, 0.6875))),
        UNHOLY_EFFIGY = addBlock("unholy_effigy", new HorizontalBlockBase(blockProps(Material.ROCK, MaterialColor.STONE)
            .hardnessAndResistance(2.8f, 3.0f)
            .setRequiresTool().harvestTool(ToolType.PICKAXE)
            .notSolid()).setShape(
                VoxelShapes.create(0.25, 0, 0.25, 0.75, 1, 0.75)
            )),
        WORKTABLE = addBlock("worktable", new WorktableBlock(blockProps(Material.WOOD, MaterialColor.WOOD)
            .hardnessAndResistance(1.6f, 3.0f)
            .harvestTool(ToolType.AXE).notSolid()).setShape(VoxelShapes.or(
                VoxelShapes.create(0, 0, 0, 1, 0.25, 1),
                VoxelShapes.create(0.125, 0.25, 0.125, 0.875, 0.625, 0.875),
                VoxelShapes.create(0, 0.625, 0, 1, 1, 1)
            )
        )),
        PLINTH = addBlock("plinth", new PlinthBlockBase(blockProps(Material.ROCK, MaterialColor.STONE)
            .hardnessAndResistance(2.0f, 3.0f)
            .setRequiresTool().harvestTool(ToolType.PICKAXE).notSolid())
            .setShape(VoxelShapes.create(0.25, 0, 0.25, 0.75, 1, 0.75))),
        BRAZIER = addBlock("brazier", new BlockBase(blockProps(Material.WOOD, MaterialColor.IRON)
            .hardnessAndResistance(2.5f, 3.0f)
            .notSolid())
            .setShape(VoxelShapes.create(0.1875, 0, 0.1875, 0.8125, 0.75, 0.8125))),
        CRUCIBLE = addBlock("crucible", new BlockBase(blockProps(Material.IRON, MaterialColor.IRON)
            .hardnessAndResistance(4.0f, 3.0f)
            .setRequiresTool().harvestTool(ToolType.PICKAXE).notSolid())
            .setShape(VoxelShapes.or(
                VoxelShapes.create(0.0625, 0.875, 0.0625, 0.1875, 1, 0.9375),
                VoxelShapes.create(0.8125, 0.875, 0.0625, 0.9375, 1, 0.9375),
                VoxelShapes.create(0.0625, 0.875, 0.0625, 0.9375, 1, 0.1875),
                VoxelShapes.create(0.0625, 0.875, 0.8125, 0.9375, 1, 0.9375),
                VoxelShapes.create(0, 0.125, 0, 0.125, 0.875, 1),
                VoxelShapes.create(0.875, 0.125, 0, 1, 0.875, 1),
                VoxelShapes.create(0, 0.125, 0, 1, 0.875, 0.125),
                VoxelShapes.create(0, 0.125, 0.875, 1, 0.875, 1),
                VoxelShapes.create(0.0625, 0, 0.0625, 0.9375, 0.125, 0.9375)
            ))),
        STONE_HAND = addBlock("stone_hand", new HorizontalBlockBase(blockProps(Material.ROCK, MaterialColor.STONE)
            .hardnessAndResistance(2.0f, 3.0f)
            .setRequiresTool().harvestTool(ToolType.PICKAXE).notSolid())
            .setShape(VoxelShapes.create(0.25, 0, 0.25, 0.75, 0.75, 0.75))),
        ENCHANTED_ASH = addBlock("enchanted_ash", new EnchantedAshBlock(blockProps(Material.MISCELLANEOUS, MaterialColor.WHITE_TERRACOTTA)
            .hardnessAndResistance(0.0f, 0.75f).notSolid())
            .setShape(VoxelShapes.empty())),
        NECROTIC_FOCUS = addBlock("necrotic_focus", new NecroticFocusBlock(blockProps(Material.ROCK, MaterialColor.STONE)
            .hardnessAndResistance(2.8f, 3.0f)
            .setRequiresTool().harvestTool(ToolType.PICKAXE).notSolid())
            .setShape(VoxelShapes.create(0.25, 0, 0.25, 0.75, 0.75, 0.75))),
        SOUL_ENCHANTER = addBlock("soul_enchanter", new SoulEnchanterBlock(blockProps(Material.ROCK, MaterialColor.OBSIDIAN)
            .hardnessAndResistance(5.0f, 1200.0f)
            .harvestTool(ToolType.PICKAXE).setRequiresTool().notSolid())
            .setShape(VoxelShapes.create(0, 0, 0, 1, 0.75, 1))),
        WOODEN_STAND = addBlock("wooden_brewing_stand", new WoodenStandBlock(blockProps(Material.IRON, MaterialColor.WOOD)
            .hardnessAndResistance(2.0f, 3.0f)
            .harvestTool(ToolType.PICKAXE).notSolid()));

    public static RegistryObject<EntityType<ZombieBruteEntity>>
        ZOMBIE_BRUTE = addEntity("zombie_brute", 7969893, 44975, 1.2f, 2.5f, ZombieBruteEntity::new, EntityClassification.MONSTER);
    public static RegistryObject<EntityType<WraithEntity>>
        WRAITH = addEntity("wraith", 0x706e6b, 0xadacbd, 0.6f, 1.9f, WraithEntity::new, EntityClassification.MONSTER);
    public static RegistryObject<EntityType<SoulfireProjectileEntity>>
        SOULFIRE_PROJECTILE = addEntity("soulfire_projectile", 0.4f, 0.4f, SoulfireProjectileEntity::new, EntityClassification.MISC);
    public static RegistryObject<EntityType<BonechillProjectileEntity>>
        BONECHILL_PROJECTILE = addEntity("bonechill_projectile", 0.4f, 0.4f, BonechillProjectileEntity::new, EntityClassification.MISC);
    public static RegistryObject<EntityType<NecromancerSpellEntity>>
        NECROMANCER_SPELL = addEntity("necromancer_spell", 0.4f, 0.4f, NecromancerSpellEntity::new, EntityClassification.MISC);
    public static RegistryObject<EntityType<ChantCasterEntity>>
        CHANT_CASTER = addEntity("chant_caster", 0.1f, 0.1f, ChantCasterEntity::new, EntityClassification.MISC);
    public static RegistryObject<EntityType<NecromancerEntity>>
        NECROMANCER = addEntity("necromancer", 0x69255e, 0x9ce8ff, 0.6f, 1.9f, NecromancerEntity::new, EntityClassification.MONSTER);

    public static RegistryObject<ContainerType<WorktableContainer>>
        WORKTABLE_CONTAINER = addContainer("worktable", WorktableContainer::new);
    public static RegistryObject<ContainerType<SoulEnchanterContainer>>
        SOUL_ENCHANTER_CONTAINER = addContainer("soul_enchanter", SoulEnchanterContainer::new);
    public static RegistryObject<ContainerType<WoodenBrewingStandContainer>>
        WOODEN_STAND_CONTAINER = addContainer("wooden_brewing_stand", WoodenBrewingStandContainer::new);

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        POTIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
        POTION_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        PARTICLES.register(FMLJavaModLoadingContext.get().getModEventBus());
        SOUND_EVENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientInit() {
    }

    public static void addBrewingRecipes() {
        BrewingRecipeRegistry.addRecipe(
            Ingredient.fromStacks(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.WATER)),
            Ingredient.fromItems(Registry.FUNGUS_SPROUTS.get()),
            PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.AWKWARD)
        );

        BrewingRecipeRegistry.addRecipe(
            Ingredient.fromStacks(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.AWKWARD)),
            Ingredient.fromItems(Registry.WRAITH_HEART.get()),
            PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), CHILLED_POTION.get())
        );
        BrewingRecipeRegistry.addRecipe(
            Ingredient.fromStacks(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), CHILLED_POTION.get())),
            Ingredient.fromItems(Items.REDSTONE),
            PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), LONG_CHILLED_POTION.get())
        );

        BrewingRecipeRegistry.addRecipe(
            Ingredient.fromStacks(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.AWKWARD)),
            Ingredient.fromItems(Registry.WARPED_SPROUTS.get()),
            PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), ANCHORED_POTION.get())
        );
        BrewingRecipeRegistry.addRecipe(
            Ingredient.fromStacks(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), ANCHORED_POTION.get())),
            Ingredient.fromItems(Items.REDSTONE),
            PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), LONG_ANCHORED_POTION.get())
        );
    }

    public static TileEntityType<HandTileEntity> HAND_TILE_ENTITY;
    public static TileEntityType<BrazierTileEntity> BRAZIER_TILE_ENTITY;
    public static TileEntityType<NecroticFocusTileEntity> NECROTIC_FOCUS_TILE_ENTITY;
    public static TileEntityType<CrucibleTileEntity> CRUCIBLE_TILE_ENTITY;
    public static TileEntityType<EffigyTileEntity> EFFIGY_TILE_ENTITY;
    public static TileEntityType<SoulEnchanterTileEntity> SOUL_ENCHANTER_TILE_ENTITY;
    public static TileEntityType<WoodenStandTileEntity> WOODEN_STAND_TILE_ENTITY;
    public static TileEntityType<GobletTileEntity> GOBLET_TILE_ENTITY;

    @SubscribeEvent
    public void registerTiles(RegistryEvent.Register<TileEntityType<?>> evt) {
        HAND_TILE_ENTITY = addTileEntity(evt.getRegistry(), "hand_tile", HandTileEntity::new, STONE_HAND.get());
        BRAZIER_TILE_ENTITY = addTileEntity(evt.getRegistry(), "brazier_tile", BrazierTileEntity::new, BRAZIER.get());
        NECROTIC_FOCUS_TILE_ENTITY = addTileEntity(evt.getRegistry(), "necrotic_focus", NecroticFocusTileEntity::new, NECROTIC_FOCUS.get());
        CRUCIBLE_TILE_ENTITY = addTileEntity(evt.getRegistry(), "crucible", CrucibleTileEntity::new, CRUCIBLE.get());
        EFFIGY_TILE_ENTITY = addTileEntity(evt.getRegistry(), "effigy", EffigyTileEntity::new, STRAW_EFFIGY.get(), UNHOLY_EFFIGY.get());
        SOUL_ENCHANTER_TILE_ENTITY = addTileEntity(evt.getRegistry(), "soul_enchanter", SoulEnchanterTileEntity::new, SOUL_ENCHANTER.get());
        WOODEN_STAND_TILE_ENTITY = addTileEntity(evt.getRegistry(), "wooden_brewing_stand", WoodenStandTileEntity::new, WOODEN_STAND.get());
        GOBLET_TILE_ENTITY = addTileEntity(evt.getRegistry(), "goblet", GobletTileEntity::new, GOBLET.get());
    }

    public static DamageSource RITUAL_DAMAGE = (new DamageSource("ritual")).setMagicDamage().setDamageBypassesArmor();
    public static DamageSource FROST_DAMAGE = new DamageSource("frost");

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

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void registerFactories(ParticleFactoryRegisterEvent evt) {
        Minecraft.getInstance().particles.registerFactory(FLAME_PARTICLE.get(), FlameParticleType.Factory::new);
        Minecraft.getInstance().particles.registerFactory(SMOKE_PARTICLE.get(), SmokeParticleType.Factory::new);
        Minecraft.getInstance().particles.registerFactory(SPARKLE_PARTICLE.get(), SparkleParticleType.Factory::new);
        Minecraft.getInstance().particles.registerFactory(WISP_PARTICLE.get(), WispParticleType.Factory::new);
        Minecraft.getInstance().particles.registerFactory(BUBBLE_PARTICLE.get(), BubbleParticleType.Factory::new);
        Minecraft.getInstance().particles.registerFactory(STEAM_PARTICLE.get(), SteamParticleType.Factory::new);
        Minecraft.getInstance().particles.registerFactory(LINE_WISP_PARTICLE.get(), LineWispParticleType.Factory::new);
        Minecraft.getInstance().particles.registerFactory(SIGN_PARTICLE.get(), (sprite) -> new SignParticleType.Factory());
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event) {
        event.addSprite(CrystalRitual.SYMBOL);
        event.addSprite(SummonRitual.SYMBOL);
        event.addSprite(DeceitRitual.SYMBOL);
        event.addSprite(AllureRitual.SYMBOL);
        event.addSprite(DaylightRitual.SYMBOL);
        event.addSprite(MoonlightRitual.SYMBOL);
        event.addSprite(PurifyRitual.SYMBOL);
        event.addSprite(RepellingRitual.SYMBOL);
        event.addSprite(SanguineRitual.SYMBOL);
        event.addSprite(SoulEnchanterTileRenderer.BOOK_TEXTURE);

        for (Sign s : Signs.getSigns()) event.addSprite(s.getSprite());
    }
}
