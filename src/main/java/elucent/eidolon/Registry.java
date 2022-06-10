package elucent.eidolon;

import elucent.eidolon.block.CandleBlock;
import elucent.eidolon.block.*;
import elucent.eidolon.client.renderer.blockentity.SoulEnchanterTileRenderer;
import elucent.eidolon.entity.*;
import elucent.eidolon.gui.SoulEnchanterContainer;
import elucent.eidolon.gui.WoodenBrewingStandContainer;
import elucent.eidolon.gui.WorktableContainer;
import elucent.eidolon.item.*;
import elucent.eidolon.item.curio.*;
import elucent.eidolon.particle.*;
import elucent.eidolon.potion.AnchoredEffect;
import elucent.eidolon.potion.ChilledEffect;
import elucent.eidolon.ritual.*;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.spell.Signs;
import elucent.eidolon.tile.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Registry {
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

    static Item.Properties itemProps() {
        return new Item.Properties().tab(Eidolon.TAB);
    }

    static Block.Properties blockProps(Material mat, MaterialColor color) {
        return Block.Properties.of(mat, color);
    }

    static RegistryObject<Item> addItem(String name) {
        return ITEMS.register(name, () -> {
            Item i = new Item(itemProps());
            ITEM_MAP.put(name, i);
            return i;
        });
    }

    static RegistryObject<Item> addItem(String name, Item.Properties props) {
        return ITEMS.register(name, () -> {
            Item i = new Item(props);
            ITEM_MAP.put(name, i);
            return i;
        });
    }

    static RegistryObject<Item> addItem(String name, Supplier<Item> item) {
        return ITEMS.register(name, () -> {
            var result = item.get();
            ITEM_MAP.put(name, result);
            return result;
        });
    }

    static RegistryObject<Block> addBlock(String name, Block.Properties props) {
        var block = BLOCKS.register(name, () -> {
            Block b = new Block(props);
            BLOCK_MAP.put(name, b);
            return b;
        });
        ITEMS.register(name, () -> new BlockItem(block.get(), itemProps()));
        return block;
    }

    static RegistryObject<Block> addBlock(String name, Supplier<Block> block) {
        var aa = BLOCKS.register(name, () -> {
            var result = block.get();
            BLOCK_MAP.put(name, result);
            return result;
        });
        ITEMS.register(name, () -> new BlockItem(aa.get(), itemProps()));
        return aa;
    }

    public static class DecoBlockPack {
        DeferredRegister<Block> registry;
        String basename;
        Block.Properties props;
        RegistryObject<Block> full = null, slab = null, stair = null, wall = null, fence = null, fence_gate = null;

        public DecoBlockPack(DeferredRegister<Block> blocks, String basename, Block.Properties props) {
            this.registry = blocks;
            this.basename = basename;
            this.props = props;
            full = addBlock(basename, () -> new Block(props));
            slab = addBlock(basename + "_slab", () -> new SlabBlock(props));
            stair = addBlock(basename + "_stairs", () -> new StairBlock(() -> full.get().defaultBlockState(), props));
        }

        public DecoBlockPack addWall() {
            wall = addBlock(basename + "_wall", () -> new WallBlock(props));
            return this;
        }

        public DecoBlockPack addFence() {
            fence = addBlock(basename + "_fence", () -> new FenceBlock(props));
            fence = addBlock(basename + "_fence_gate", () -> new FenceGateBlock(props));
            return this;
        }

        public Block getBlock() { return full.get(); }
        public Block getSlab() { return slab.get(); }
        public Block getStairs() { return stair.get(); }
        public Block getWall() { return wall.get(); }
        public Block getFence() { return fence.get(); }
    }

    static <T extends Entity> RegistryObject<EntityType<T>> addEntity(String name, float width, float height, EntityType.EntityFactory<T> factory, MobCategory kind) {
        return ENTITIES.register(name, () -> EntityType.Builder.<T>of(factory, kind)
                .setTrackingRange(64)
                .setUpdateInterval(1)
                .sized(width, height)
                .build(Eidolon.MODID + ":" + name));
    }

    static <T extends Mob> RegistryObject<EntityType<T>> addEntity(String name, int color1, int color2, float width, float height, EntityType.EntityFactory<T> factory, MobCategory kind) {
        var type = ENTITIES.register(name, () ->
            EntityType.Builder.<T>of(factory, kind)
                    .setTrackingRange(64)
                    .setUpdateInterval(1)
                    .sized(width, height)
                    .build(Eidolon.MODID + ":" + name)
        );
        ITEMS.register("spawn_" + name, () -> new ForgeSpawnEggItem(type, color1, color2, itemProps().tab(CreativeModeTab.TAB_MISC)));
        return type;
    }

    static RegistryObject<SoundEvent> addSound(String name) {
        SoundEvent event = new SoundEvent(new ResourceLocation(Eidolon.MODID, name));
        return SOUND_EVENTS.register(name, () -> event);
    }

    static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> addContainer(String name, MenuType.MenuSupplier<T> factory) {
        return CONTAINERS.register(name, () -> new MenuType<T>(factory));
    }

    static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> addTileEntity(String name, BlockEntityType.BlockEntitySupplier<T> factory, Supplier<Block>... blocks) {
        return TILE_ENTITIES.register(name, () -> {
            var blockArray = new Block[blocks.length];
            for (int i = 0; i < blocks.length; i++) {
                blockArray[i] = blocks[i].get();
            }
            var result = BlockEntityType.Builder.of(factory, blockArray).build(null);
            for (var block : blockArray) {
                if (block instanceof BlockBase base) {
                    base.setTile(result);
                }
            }
            return result;
        });
    }

    public static RegistryObject<SoundEvent>
        CAST_SOULFIRE_EVENT = addSound("cast_soulfire"),
        CAST_BONECHILL_EVENT = addSound("cast_bonechill"),
        SPLASH_SOULFIRE_EVENT = addSound("splash_soulfire"),
        SPLASH_BONECHILL_EVENT = addSound("splash_bonechill"),
        SELECT_SIGN = addSound("select_sign"),
        CHANT_WORD = addSound("chant_word"),
        PAROUSIA = addSound("parousia");

    public static RegistryObject<MobEffect>
        CHILLED_EFFECT = POTIONS.register("chilled", () -> new ChilledEffect()),
        ANCHORED_EFFECT = POTIONS.register("anchored", () -> new AnchoredEffect());

    public static RegistryObject<Potion>
        CHILLED_POTION = POTION_TYPES.register("chilled", () -> new Potion(new MobEffectInstance(CHILLED_EFFECT.get(), 3600))),
        LONG_CHILLED_POTION = POTION_TYPES.register("long_chilled", () -> new Potion(new MobEffectInstance(CHILLED_EFFECT.get(), 9600))),
        ANCHORED_POTION = POTION_TYPES.register("anchored", () -> new Potion(new MobEffectInstance(ANCHORED_EFFECT.get(), 3600))),
        LONG_ANCHORED_POTION = POTION_TYPES.register("long_anchored", () -> new Potion(new MobEffectInstance(ANCHORED_EFFECT.get(), 9600)));

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
        ZOMBIE_HEART = addItem("zombie_heart", () -> new ItemBase(itemProps().rarity(Rarity.UNCOMMON))
            .setLore("lore.eidolon.zombie_heart")),
        TATTERED_CLOTH = addItem("tattered_cloth"),
        WRAITH_HEART = addItem("wraith_heart", () -> new ItemBase(itemProps()
            .rarity(Rarity.UNCOMMON)).setLore("lore.eidolon.wraith_heart")),
        TOP_HAT = addItem("top_hat", () -> new TopHatItem(itemProps().stacksTo(1).rarity(Rarity.EPIC)).setLore("lore.eidolon.top_hat")),
        BASIC_RING = addItem("basic_ring", () -> new BasicRingItem(itemProps().stacksTo(1))),
        BASIC_AMULET = addItem("basic_amulet", () -> new BasicAmuletItem(itemProps().stacksTo(1))),
        BASIC_BELT = addItem("basic_belt", () -> new BasicBeltItem(itemProps().stacksTo(1))),
        CODEX = addItem("codex", () -> new CodexItem(itemProps().stacksTo(1).rarity(Rarity.UNCOMMON)).setLore("lore.eidolon.codex")),
        SOUL_SHARD = addItem("soul_shard"),
        DEATH_ESSENCE = addItem("death_essence"),
        CRIMSON_ESSENCE = addItem("crimson_essence"),
        FUNGUS_SPROUTS = addItem("fungus_sprouts", itemProps().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.1f).build())),
        WARPED_SPROUTS = addItem("warped_sprouts", itemProps().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.6f).effect(() -> new MobEffectInstance(ANCHORED_EFFECT.get(), 900), 1).build())),
        ENDER_CALX = addItem("ender_calx"),
        TALLOW = addItem("tallow"),
        LESSER_SOUL_GEM = addItem("lesser_soul_gem"),
        UNHOLY_SYMBOL = addItem("unholy_symbol", () -> new UnholySymbolItem(itemProps().rarity(Rarity.UNCOMMON).stacksTo(1))),
        REAPER_SCYTHE = addItem("reaper_scythe", () -> new ReaperScytheItem(itemProps().rarity(Rarity.UNCOMMON))
            .setLore("lore.eidolon.reaper_scythe")),
        CLEAVING_AXE = addItem("cleaving_axe", () -> new CleavingAxeItem(itemProps().rarity(Rarity.UNCOMMON))
            .setLore("lore.eidolon.cleaving_axe")),
        SHADOW_GEM = addItem("shadow_gem"),
        WICKED_WEAVE = addItem("wicked_weave"),
        WARLOCK_HAT = addItem("warlock_hat", () -> new WarlockRobesItem(EquipmentSlot.HEAD, itemProps())),
        WARLOCK_CLOAK = addItem("warlock_cloak", () -> new WarlockRobesItem(EquipmentSlot.CHEST, itemProps())),
        WARLOCK_BOOTS = addItem("warlock_boots", () -> new WarlockRobesItem(EquipmentSlot.FEET, itemProps())),
        REVERSAL_PICK = addItem("reversal_pick", () -> new ReversalPickItem(itemProps()
            .rarity(Rarity.UNCOMMON))),
        VOID_AMULET = addItem("void_amulet", () -> new VoidAmuletItem(itemProps()
            .rarity(Rarity.UNCOMMON).stacksTo(1)).setLore("lore.eidolon.void_amulet")),
        WARDED_MAIL = addItem("warded_mail", () -> new WardedMailItem(itemProps()
            .rarity(Rarity.UNCOMMON).stacksTo(1)).setLore("lore.eidolon.warded_mail")),
        SAPPING_SWORD = addItem("sapping_sword", () -> new SappingSwordItem(itemProps()
            .rarity(Rarity.UNCOMMON).stacksTo(1)).setLore("lore.eidolon.sapping_sword")),
        SANGUINE_AMULET = addItem("sanguine_amulet", () -> new SanguineAmuletItem(itemProps()
            .rarity(Rarity.UNCOMMON).stacksTo(1)).setLore("lore.eidolon.sanguine_amulet")),
        SOULFIRE_WAND = addItem("soulfire_wand", () -> new SoulfireWandItem(itemProps()
            .rarity(Rarity.UNCOMMON).stacksTo(1).durability(253).setNoRepair())
            .setLore("lore.eidolon.soulfire_wand")),
        BONECHILL_WAND = addItem("bonechill_wand", () -> new BonechillWandItem(itemProps()
            .rarity(Rarity.UNCOMMON).stacksTo(1).durability(253).setNoRepair())
            .setLore("lore.eidolon.bonechill_wand")),
        GRAVITY_BELT = addItem("gravity_belt", () -> new GravityBeltItem(itemProps()
            .rarity(Rarity.UNCOMMON).stacksTo(1)).setLore("lore.eidolon.gravity_belt")),
        RESOLUTE_BELT = addItem("resolute_belt", () -> new ResoluteBeltItem(itemProps()
            .rarity(Rarity.UNCOMMON).stacksTo(1)).setLore("lore.eidolon.resolute_belt")),
        PRESTIGIOUS_PALM = addItem("prestigious_palm", () -> new PrestigiousPalmItem(itemProps()
            .rarity(Rarity.UNCOMMON).stacksTo(1)).setLore("lore.eidolon.prestigious_palm")),
        MIND_SHIELDING_PLATE = addItem("mind_shielding_plate", () -> new MindShieldingPlateItem(itemProps()
            .rarity(Rarity.UNCOMMON).stacksTo(1)).setLore("lore.eidolon.mind_shielding_plate")),
        GLASS_HAND = addItem("glass_hand", () -> new GlassHandItem(itemProps()
            .rarity(Rarity.RARE).stacksTo(1)).setLore("lore.eidolon.glass_hand")),
        PAROUSIA_DISC = addItem("music_disc_parousia", () -> new RecordItem(9, () -> PAROUSIA.get(),
            itemProps().stacksTo(1).tab(CreativeModeTab.TAB_MISC).rarity(Rarity.RARE)));

    public static RegistryObject<Block>
        LEAD_ORE = addBlock("lead_ore", blockProps(Material.STONE, MaterialColor.STONE)
            .sound(SoundType.STONE).strength(2.8f, 3.0f)
            //.harvestLevel(2).harvestTool(ToolType.PICKAXE)
    ),
        LEAD_BLOCK = addBlock("lead_block", blockProps(Material.STONE, MaterialColor.TERRACOTTA_PURPLE)
            .sound(SoundType.METAL).strength(3.0f, 3.0f)
            //.harvestLevel(2).harvestTool(ToolType.PICKAXE)
        ),
        PEWTER_BLOCK = addBlock("pewter_block", blockProps(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY)
            .sound(SoundType.METAL).strength(4.0f, 4.0f)
            //.harvestLevel(2).harvestTool(ToolType.PICKAXE)
        ),
        ARCANE_GOLD_BLOCK = addBlock("arcane_gold_block", blockProps(Material.STONE, MaterialColor.GOLD)
            .sound(SoundType.METAL).strength(3.0f, 4.0f)
            //.harvestLevel(2).harvestTool(ToolType.PICKAXE)
        ),
        WOODEN_ALTAR = addBlock("wooden_altar", () -> new TableBlockBase(blockProps(Material.WOOD, MaterialColor.WOOD)
            .sound(SoundType.WOOD).strength(1.6f, 3.0f)
            //.harvestTool(ToolType.AXE)
        )),
        STONE_ALTAR = addBlock("stone_altar", () -> new TableBlockBase(blockProps(Material.STONE, MaterialColor.STONE)
            .sound(SoundType.STONE).strength(2.8f, 3.0f)
            .requiresCorrectToolForDrops()
                //.harvestTool(ToolType.PICKAXE)
                .noOcclusion())
            .setMainShape(Shapes.or(
                Shapes.box(0, 0.375, 0, 1, 1, 1),
                Shapes.box(0.0625, 0.125, 0.0625, 0.9375, 0.375, 0.9375)
            ))),
        CANDLE = addBlock("candle", () -> new CandleBlock(blockProps(Material.DECORATION, MaterialColor.TERRACOTTA_WHITE)
            .sound(SoundType.STONE).lightLevel((state) -> 15).strength(0.6f, 0.8f).noOcclusion())),
        CANDLESTICK = addBlock("candlestick", () -> new CandlestickBlock(blockProps(Material.METAL, MaterialColor.GOLD)
            .sound(SoundType.STONE).lightLevel((state) -> 15).strength(1.2f, 2.0f).noOcclusion())),
        STRAW_EFFIGY = addBlock("straw_effigy", () -> new HorizontalWaterloggableBlock(blockProps(Material.PLANT, MaterialColor.COLOR_YELLOW)
            .sound(SoundType.WOOD).strength(1.4f, 2.0f)
            .noOcclusion()).setShape(
                Shapes.box(0.28125, 0, 0.28125, 0.71875, 1, 0.71875)
            )),
        GOBLET = addBlock("goblet", () -> new BlockBase(blockProps(Material.METAL, MaterialColor.GOLD)
            .sound(SoundType.METAL).strength(1.4f, 2.0f).requiresCorrectToolForDrops()
                //.harvestTool(ToolType.PICKAXE)
            .noOcclusion()).setShape(Shapes.box(0.3125, 0, 0.3125, 0.6875, 0.5, 0.6875))),
        UNHOLY_EFFIGY = addBlock("unholy_effigy", () -> new HorizontalWaterloggableBlock(blockProps(Material.STONE, MaterialColor.STONE)
            .sound(SoundType.STONE).strength(2.8f, 3.0f)
            .requiresCorrectToolForDrops()
                //.harvestTool(ToolType.PICKAXE)
            .noOcclusion()).setShape(
                Shapes.box(0.25, 0, 0.25, 0.75, 1, 0.75)
            )),
        WORKTABLE = addBlock("worktable", () -> new WorktableBlock(blockProps(Material.WOOD, MaterialColor.WOOD)
            .sound(SoundType.WOOD).strength(1.6f, 3.0f)
            //.harvestTool(ToolType.AXE)
                .noOcclusion()).setShape(Shapes.or(
                Shapes.box(0, 0, 0, 1, 0.25, 1),
                Shapes.box(0.125, 0.25, 0.125, 0.875, 0.625, 0.875),
                Shapes.box(0, 0.625, 0, 1, 1, 1)
            ))),
        PLINTH = addBlock("plinth", () -> new PlinthBlockBase(blockProps(Material.STONE, MaterialColor.STONE)
            .sound(SoundType.STONE).strength(2.0f, 3.0f)
            .requiresCorrectToolForDrops()
                //.harvestTool(ToolType.PICKAXE)
                .noOcclusion())
            .setShape(Shapes.box(0.25, 0, 0.25, 0.75, 1, 0.75))),
        BRAZIER = addBlock("brazier", () -> new BlockBase(blockProps(Material.WOOD, MaterialColor.METAL)
            .sound(SoundType.METAL).strength(2.5f, 3.0f)
            .noOcclusion())
            .setShape(Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.75, 0.8125))),
        CRUCIBLE = addBlock("crucible", () -> new BlockBase(blockProps(Material.METAL, MaterialColor.METAL)
            .sound(SoundType.METAL).strength(4.0f, 3.0f)
            .requiresCorrectToolForDrops()
                //.harvestTool(ToolType.PICKAXE)
                .noOcclusion())
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
        STONE_HAND = addBlock("stone_hand", () -> new HorizontalWaterloggableBlock(blockProps(Material.STONE, MaterialColor.STONE)
            .sound(SoundType.STONE).strength(2.0f, 3.0f)
            .requiresCorrectToolForDrops()
                //.harvestTool(ToolType.PICKAXE)
                .noOcclusion())
            .setShape(Shapes.box(0.25, 0, 0.25, 0.75, 0.75, 0.75))),
        ENCHANTED_ASH = addBlock("enchanted_ash", () -> new EnchantedAshBlock(blockProps(Material.DECORATION, MaterialColor.TERRACOTTA_WHITE)
            .sound(SoundType.STONE).strength(0.0f, 0.75f).noOcclusion())
            .setShape(Shapes.empty())),
        NECROTIC_FOCUS = addBlock("necrotic_focus", () -> new NecroticFocusBlock(blockProps(Material.STONE, MaterialColor.STONE)
            .sound(SoundType.STONE).strength(2.8f, 3.0f)
            .requiresCorrectToolForDrops()
                //.harvestTool(ToolType.PICKAXE)
                .noOcclusion())
            .setShape(Shapes.box(0.25, 0, 0.25, 0.75, 0.75, 0.75))),
        SOUL_ENCHANTER = addBlock("soul_enchanter", () -> new SoulEnchanterBlock(blockProps(Material.STONE, MaterialColor.PODZOL)
            .sound(SoundType.STONE).strength(5.0f, 1200.0f)
            //.harvestTool(ToolType.PICKAXE)
                .requiresCorrectToolForDrops().noOcclusion())
            .setShape(Shapes.box(0, 0, 0, 1, 0.75, 1))),
        WOODEN_STAND = addBlock("wooden_brewing_stand", () -> new WoodenStandBlock(blockProps(Material.METAL, MaterialColor.WOOD)
            .sound(SoundType.STONE).strength(2.0f, 3.0f)
            //.harvestTool(ToolType.PICKAXE)
                .noOcclusion()));
    public static DecoBlockPack
        SMOOTH_STONE_BRICK = new DecoBlockPack(BLOCKS, "smooth_stone_bricks", blockProps(Material.STONE, MaterialColor.STONE)
            .sound(SoundType.STONE).requiresCorrectToolForDrops()
            //.harvestTool(ToolType.PICKAXE)
            .strength(2.0f, 3.0f))
            .addWall(),
        SMOOTH_STONE_TILES = new DecoBlockPack(BLOCKS, "smooth_stone_tiles", blockProps(Material.STONE, MaterialColor.STONE)
            .sound(SoundType.STONE).requiresCorrectToolForDrops()
                //.harvestTool(ToolType.PICKAXE)
                .strength(2.0f, 3.0f)),
        POLISHED_PLANKS = new DecoBlockPack(BLOCKS, "polished_planks", blockProps(Material.WOOD, MaterialColor.WOOD)
            .sound(SoundType.WOOD)
                //.harvestTool(ToolType.AXE)
                .strength(1.6f, 3.0f))
            .addFence();
    public static RegistryObject<Block>
        POLISHED_WOOD_PILLAR = addBlock("polished_wood_pillar", () -> new RotatedPillarBlock(blockProps(Material.WOOD, MaterialColor.WOOD)
            //.harvestTool(ToolType.AXE)
            .strength(1.6f, 3.0f)));

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
    public static RegistryObject<EntityType<NecromancerEntity>>
        NECROMANCER = addEntity("necromancer", 0x69255e, 0x9ce8ff, 0.6f, 1.9f, NecromancerEntity::new, MobCategory.MONSTER);

    public static RegistryObject<MenuType<WorktableContainer>>
        WORKTABLE_CONTAINER = addContainer("worktable", WorktableContainer::new);
    public static RegistryObject<MenuType<SoulEnchanterContainer>>
        SOUL_ENCHANTER_CONTAINER = addContainer("soul_enchanter", SoulEnchanterContainer::new);
    public static RegistryObject<MenuType<WoodenBrewingStandContainer>>
        WOODEN_STAND_CONTAINER = addContainer("wooden_brewing_stand", WoodenBrewingStandContainer::new);

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        POTIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
        POTION_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        PARTICLES.register(FMLJavaModLoadingContext.get().getModEventBus());
        SOUND_EVENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static void addBrewingRecipes() {
        System.out.println("calling addBrewingRecipes");
        PotionBrewing.addMix(Potions.WATER, Registry.FUNGUS_SPROUTS.get(), Potions.AWKWARD);
        PotionBrewing.addMix(Potions.AWKWARD, Registry.WRAITH_HEART.get(), Registry.CHILLED_POTION.get());
        PotionBrewing.addMix(Registry.CHILLED_POTION.get(), Items.REDSTONE, Registry.LONG_CHILLED_POTION.get());
        PotionBrewing.addMix(Potions.AWKWARD, Registry.WARPED_SPROUTS.get(), Registry.ANCHORED_POTION.get());
        PotionBrewing.addMix(Registry.ANCHORED_POTION.get(), Items.REDSTONE, Registry.LONG_ANCHORED_POTION.get());
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientInit() {
    }

    public static RegistryObject<BlockEntityType<HandTileEntity>         >  HAND_TILE_ENTITY;
    public static RegistryObject<BlockEntityType<BrazierTileEntity>      >  BRAZIER_TILE_ENTITY;
    public static RegistryObject<BlockEntityType<NecroticFocusTileEntity>>  NECROTIC_FOCUS_TILE_ENTITY;
    public static RegistryObject<BlockEntityType<CrucibleTileEntity>     >  CRUCIBLE_TILE_ENTITY;
    public static RegistryObject<BlockEntityType<EffigyTileEntity>       >  EFFIGY_TILE_ENTITY;
    public static RegistryObject<BlockEntityType<SoulEnchanterTileEntity>>  SOUL_ENCHANTER_TILE_ENTITY;
    public static RegistryObject<BlockEntityType<WoodenStandTileEntity>  >  WOODEN_STAND_TILE_ENTITY;
    public static RegistryObject<BlockEntityType<GobletTileEntity>       >  GOBLET_TILE_ENTITY;

    static {
        HAND_TILE_ENTITY = addTileEntity("hand_tile", HandTileEntity::new, STONE_HAND);
        BRAZIER_TILE_ENTITY = addTileEntity("brazier_tile", BrazierTileEntity::new, BRAZIER);
        NECROTIC_FOCUS_TILE_ENTITY = addTileEntity("necrotic_focus", NecroticFocusTileEntity::new, NECROTIC_FOCUS);
        CRUCIBLE_TILE_ENTITY = addTileEntity( "crucible", CrucibleTileEntity::new, CRUCIBLE);
        EFFIGY_TILE_ENTITY = addTileEntity("effigy", EffigyTileEntity::new, STRAW_EFFIGY, UNHOLY_EFFIGY);
        SOUL_ENCHANTER_TILE_ENTITY = addTileEntity("soul_enchanter", SoulEnchanterTileEntity::new, SOUL_ENCHANTER);
        WOODEN_STAND_TILE_ENTITY = addTileEntity("wooden_brewing_stand", WoodenStandTileEntity::new, WOODEN_STAND);
        GOBLET_TILE_ENTITY = addTileEntity("goblet", GobletTileEntity::new, GOBLET);
    }

    public static DamageSource RITUAL_DAMAGE = new DamageSource("ritual").bypassArmor().bypassMagic();
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
        Minecraft.getInstance().particleEngine.register(FLAME_PARTICLE.get(), FlameParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(SMOKE_PARTICLE.get(), SmokeParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(SPARKLE_PARTICLE.get(), SparkleParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(WISP_PARTICLE.get(), WispParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(BUBBLE_PARTICLE.get(), BubbleParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(STEAM_PARTICLE.get(), SteamParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(LINE_WISP_PARTICLE.get(), LineWispParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(SIGN_PARTICLE.get(), (sprite) -> new SignParticleType.Factory());
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
