package elucent.eidolon;

import elucent.eidolon.block.*;
import elucent.eidolon.entity.*;
import elucent.eidolon.gui.WorktableContainer;
import elucent.eidolon.item.*;
import elucent.eidolon.item.curio.*;
import elucent.eidolon.particle.*;
import elucent.eidolon.potion.ChilledEffect;
import elucent.eidolon.ritual.*;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.spell.Signs;
import elucent.eidolon.tile.*;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.*;
import net.minecraft.particles.ParticleType;
import net.minecraft.potion.Effect;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.TicketManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Registry {
    static Map<String, Block> BLOCK_MAP = new HashMap<>();
    static Map<String, Item> ITEM_MAP = new HashMap<>();
    static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Eidolon.MODID);
    static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Eidolon.MODID);
    static DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Eidolon.MODID);
    static DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Eidolon.MODID);
    static DeferredRegister<Effect> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, Eidolon.MODID);
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
        SoundEvent event = new SoundEvent(new ResourceLocation(Eidolon.MODID, name));
        return CONTAINERS.register(name, () -> new ContainerType<T>(factory));
    }

    static <T extends TileEntityBase> TileEntityType<T> addTileEntity(IForgeRegistry<TileEntityType<?>> registry, String name, Supplier<T> factory, Block... blocks) {
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
        SELECT_SIGN = addSound("select_sign"),
        CHANT_WORD = addSound("chant_word"),
        PAROUSIA = addSound("parousia");

    public static RegistryObject<Item>
        LEAD_INGOT = addItem("lead_ingot"),
        LEAD_NUGGET = addItem("lead_nugget"),
        PEWTER_BLEND = addItem("pewter_blend"),
        PEWTER_INGOT = addItem("pewter_ingot"),
        PEWTER_NUGGET = addItem("pewter_nugget"),
        PEWTER_INLAY = addItem("pewter_inlay"),
        GOLD_INLAY = addItem("gold_inlay"),
        ZOMBIE_HEART = addItem("zombie_heart", new ItemBase(itemProps().rarity(Rarity.UNCOMMON))
            .setLore("lore.eidolon.zombie_heart")),
        TATTERED_CLOTH = addItem("tattered_cloth"),
        WRAITH_HEART = addItem("wraith_heart", new ItemBase(itemProps()
            .rarity(Rarity.UNCOMMON)).setLore("lore.eidolon.wraith_heart")),
        BASIC_RING = addItem("basic_ring", new BasicRingItem(itemProps().maxStackSize(1))),
        BASIC_AMULET = addItem("basic_amulet", new BasicAmuletItem(itemProps().maxStackSize(1))),
        CODEX = addItem("codex", new CodexItem(itemProps().maxStackSize(1).rarity(Rarity.UNCOMMON)).setLore("lore.eidolon.codex")),
        SOUL_SHARD = addItem("soul_shard"),
        LESSER_SOUL_GEM = addItem("lesser_soul_gem"),
        UNHOLY_SYMBOL = addItem("unholy_symbol", new UnholySymbolItem(itemProps().rarity(Rarity.UNCOMMON))),
        REAPER_SCYTHE = addItem("reaper_scythe", new ReaperScytheItem(itemProps().rarity(Rarity.UNCOMMON))
            .setLore("lore.eidolon.reaper_scythe")),
        CLEAVING_AXE = addItem("cleaving_axe", new CleavingAxeItem(itemProps().rarity(Rarity.UNCOMMON))
            .setLore("lore.eidolon.cleaving_axe")),
        SHADOW_GEM = addItem("shadow_gem"),
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
        WOODEN_ALTAR = addBlock("wooden_altar", new TableBlockBase(blockProps(Material.WOOD, MaterialColor.WOOD)
            .hardnessAndResistance(2.8f, 3.0f)
            .harvestTool(ToolType.AXE))),
        STRAW_EFFIGY = addBlock("straw_effigy", new HorizontalBlockBase(blockProps(Material.PLANTS, MaterialColor.YELLOW)
            .hardnessAndResistance(1.4f, 2.0f)
            .notSolid()).setShape(
                VoxelShapes.create(0.28125, 0, 0.28125, 0.71875, 1, 0.71875)
            )),
        WORKTABLE = addBlock("worktable", new WorktableBlock(blockProps(Material.WOOD, MaterialColor.WOOD)
            .hardnessAndResistance(2.8f, 3.0f)
            .harvestTool(ToolType.AXE))),
        WOODEN_PODIUM = addBlock("wooden_podium", new HorizontalBlockBase(blockProps(Material.WOOD, MaterialColor.WOOD)
            .hardnessAndResistance(2.8f, 3.0f)
            .harvestTool(ToolType.AXE).notSolid()).setShape(
                VoxelShapes.create(0.125, 0, 0.125, 0.875, 0.375, 0.875)
            )),
        OFFERTORY_PLATE = addBlock("offertory_plate", new BlockBase(blockProps(Material.ROCK, MaterialColor.WHITE_TERRACOTTA)
            .hardnessAndResistance(1.4f, 2.0f))
            .setShape(VoxelShapes.create(0.1875, 0, 0.1875, 0.8125, 0.125, 0.8125)
            )),
        PLINTH = addBlock("plinth", new BlockBase(blockProps(Material.ROCK, MaterialColor.STONE)
            .hardnessAndResistance(2.0f, 3.0f)
            .harvestTool(ToolType.PICKAXE).notSolid())
            .setShape(VoxelShapes.create(0.25, 0, 0.25, 0.75, 1, 0.75))),
        BRAZIER = addBlock("brazier", new BlockBase(blockProps(Material.WOOD, MaterialColor.IRON)
            .hardnessAndResistance(2.5f, 3.0f)
            .notSolid())
            .setShape(VoxelShapes.create(0.1875, 0, 0.1875, 0.8125, 0.75, 0.8125))),
        CRUCIBLE = addBlock("crucible", new BlockBase(blockProps(Material.IRON, MaterialColor.IRON)
            .hardnessAndResistance(4.0f, 3.0f)
            .harvestLevel(0).harvestTool(ToolType.PICKAXE).notSolid())
            .setShape(VoxelShapes.combine(
                VoxelShapes.combine(
                    VoxelShapes.combine(
                        VoxelShapes.combine(
                            VoxelShapes.create(0.0625, 0.875, 0.0625, 0.1875, 1, 0.9375),
                            VoxelShapes.create(0.8125, 0.875, 0.0625, 0.9375, 1, 0.9375),
                            IBooleanFunction.OR),
                        VoxelShapes.combine(
                            VoxelShapes.create(0.0625, 0.875, 0.0625, 0.9375, 1, 0.1875),
                            VoxelShapes.create(0.0625, 0.875, 0.8125, 0.9375, 1, 0.9375),
                            IBooleanFunction.OR),
                        IBooleanFunction.OR),
                    VoxelShapes.combine(
                        VoxelShapes.combine(
                            VoxelShapes.create(0, 0.125, 0, 0.125, 0.875, 1),
                            VoxelShapes.create(0.875, 0.125, 0, 1, 0.875, 1),
                            IBooleanFunction.OR),
                        VoxelShapes.combine(
                            VoxelShapes.create(0, 0.125, 0, 1, 0.875, 0.125),
                            VoxelShapes.create(0, 0.125, 0.875, 1, 0.875, 1),
                            IBooleanFunction.OR),
                        IBooleanFunction.OR),
                    IBooleanFunction.OR),
                VoxelShapes.create(0.0625, 0, 0.0625, 0.9375, 0.125, 0.9375),
                IBooleanFunction.OR))),
        STONE_HAND = addBlock("stone_hand", new HorizontalBlockBase(blockProps(Material.ROCK, MaterialColor.STONE)
            .hardnessAndResistance(2.0f, 3.0f)
            .harvestTool(ToolType.PICKAXE).notSolid())
            .setShape(VoxelShapes.create(0.25, 0, 0.25, 0.75, 0.75, 0.75))),
        ENCHANTED_ASH = addBlock("enchanted_ash", new EnchantedAshBlock(blockProps(Material.MISCELLANEOUS, MaterialColor.WHITE_TERRACOTTA)
            .hardnessAndResistance(0.0f, 0.75f).notSolid())
            .setShape(VoxelShapes.empty())),
        NECROTIC_FOCUS = addBlock("necrotic_focus", new NecroticFocusBlock(blockProps(Material.ROCK, MaterialColor.STONE)
            .hardnessAndResistance(2.8f, 3.0f)
            .harvestTool(ToolType.PICKAXE).notSolid())
            .setShape(VoxelShapes.create(0.25, 0, 0.25, 0.75, 0.75, 0.75)));

    public static RegistryObject<EntityType<ZombieBruteEntity>>
        ZOMBIE_BRUTE = addEntity("zombie_brute", 7969893, 44975, 1.2f, 2.5f, ZombieBruteEntity::new, EntityClassification.MONSTER);
    public static RegistryObject<EntityType<WraithEntity>>
        WRAITH = addEntity("wraith", 0x706e6b, 0xadacbd, 0.6f, 1.9f, WraithEntity::new, EntityClassification.MONSTER);
    public static RegistryObject<EntityType<SoulfireProjectileEntity>>
        SOULFIRE_PROJECTILE = addEntity("soulfire_projectile", 0.4f, 0.4f, SoulfireProjectileEntity::new, EntityClassification.MISC);
    public static RegistryObject<EntityType<BonechillProjectileEntity>>
        BONECHILL_PROJECTILE = addEntity("bonechill_projectile", 0.4f, 0.4f, BonechillProjectileEntity::new, EntityClassification.MISC);
    public static RegistryObject<EntityType<ChantCasterEntity>>
        CHANT_CASTER = addEntity("chant_caster", 0.1f, 0.1f, ChantCasterEntity::new, EntityClassification.MISC);

    public static RegistryObject<Effect>
        CHILLED_EFFECT = POTIONS.register("chilled", () -> new ChilledEffect());

    public static RegistryObject<ContainerType<WorktableContainer>>
        WORKTABLE_CONTAINER = addContainer("worktable", WorktableContainer::new);

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        POTIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        PARTICLES.register(FMLJavaModLoadingContext.get().getModEventBus());
        SOUND_EVENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientInit() {
    }

    public static TileEntityType<HandTileEntity> HAND_TILE_ENTITY;
    public static TileEntityType<BrazierTileEntity> BRAZIER_TILE_ENTITY;
    public static TileEntityType<NecroticFocusTileEntity> NECROTIC_FOCUS_TILE_ENTITY;
    public static TileEntityType<CrucibleTileEntity> CRUCIBLE_TILE_ENTITY;
    public static TileEntityType<PodiumTileEntity> PODIUM_TILE_ENTITY;
    public static TileEntityType<OffertoryPlateTileEntity> OFFERTORY_PLATE_TILE_ENTITY;
    public static TileEntityType<EffigyTileEntity> EFFIGY_TILE_ENTITY;

    @SubscribeEvent
    public void registerTiles(RegistryEvent.Register<TileEntityType<?>> evt) {
        HAND_TILE_ENTITY = addTileEntity(evt.getRegistry(), "hand_tile", HandTileEntity::new, STONE_HAND.get());
        BRAZIER_TILE_ENTITY = addTileEntity(evt.getRegistry(), "brazier_tile", BrazierTileEntity::new, BRAZIER.get());
        NECROTIC_FOCUS_TILE_ENTITY = addTileEntity(evt.getRegistry(), "necrotic_focus", NecroticFocusTileEntity::new, NECROTIC_FOCUS.get());
        CRUCIBLE_TILE_ENTITY = addTileEntity(evt.getRegistry(), "crucible", CrucibleTileEntity::new, CRUCIBLE.get());
        PODIUM_TILE_ENTITY = addTileEntity(evt.getRegistry(), "podium", PodiumTileEntity::new, WOODEN_PODIUM.get());
        OFFERTORY_PLATE_TILE_ENTITY = addTileEntity(evt.getRegistry(), "offertory_plate", OffertoryPlateTileEntity::new, OFFERTORY_PLATE.get());
        EFFIGY_TILE_ENTITY = addTileEntity(evt.getRegistry(), "effigy", EffigyTileEntity::new, STRAW_EFFIGY.get());
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
        event.addSprite(InfuseRitual.SYMBOL);
        event.addSprite(SanguineRitual.SYMBOL);
        event.addSprite(PodiumTileRenderer.BOOK_TEXTURE);

        for (Sign s : Signs.getSigns()) event.addSprite(s.getSprite());
    }
}
