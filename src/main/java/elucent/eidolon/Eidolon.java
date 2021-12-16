package elucent.eidolon;

import elucent.eidolon.capability.IKnowledge;
import elucent.eidolon.capability.IReputation;
import elucent.eidolon.capability.KnowledgeImpl;
import elucent.eidolon.capability.ReputationImpl;
import elucent.eidolon.codex.CodexChapters;
import elucent.eidolon.deity.RegisterDeitiesEvent;
import elucent.eidolon.entity.AngelArrowRenderer;
import elucent.eidolon.entity.NecromancerEntity;
import elucent.eidolon.entity.NecromancerModel;
import elucent.eidolon.entity.NecromancerRenderer;
import elucent.eidolon.entity.RavenEntity;
import elucent.eidolon.entity.RavenModel;
import elucent.eidolon.entity.RavenRenderer;
import elucent.eidolon.entity.WraithEntity;
import elucent.eidolon.entity.WraithModel;
import elucent.eidolon.entity.WraithRenderer;
import elucent.eidolon.entity.ZombieBruteEntity;
import elucent.eidolon.entity.ZombieBruteModel;
import elucent.eidolon.entity.ZombieBruteRenderer;
import elucent.eidolon.gui.SoulEnchanterScreen;
import elucent.eidolon.gui.WoodenBrewingStandScreen;
import elucent.eidolon.gui.WorktableScreen;
import elucent.eidolon.item.AthameItem;
import elucent.eidolon.network.Networking;
import elucent.eidolon.proxy.ClientProxy;
import elucent.eidolon.proxy.ISidedProxy;
import elucent.eidolon.proxy.ServerProxy;
import elucent.eidolon.reagent.RegisterReagentsEvent;
import elucent.eidolon.recipe.CrucibleRegistry;
import elucent.eidolon.ritual.RitualRegistry;
import elucent.eidolon.spell.AltarEntries;
import elucent.eidolon.tile.BrazierTileRenderer;
import elucent.eidolon.tile.CrucibleTileRenderer;
import elucent.eidolon.tile.GobletTileRenderer;
import elucent.eidolon.tile.HandTileRenderer;
import elucent.eidolon.tile.NecroticFocusTileRenderer;
import elucent.eidolon.tile.SoulEnchanterTileRenderer;
import elucent.eidolon.tile.reagent.CisternTileRenderer;
import elucent.eidolon.tile.reagent.PipeTileRenderer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

@Mod(Eidolon.MODID)
public class Eidolon {
    public static ISidedProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    public static final String MODID = "eidolon";

    public static final CreativeModeTab TAB = new CreativeModeTab(MODID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Registry.SHADOW_GEM.get(), 1);
        }
    };

    public Eidolon() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::sendImc);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        FMLJavaModLoadingContext.get().getModEventBus().register(new Registry());
        Registry.init();
        proxy.init();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new WorldGen());
        WorldGen.preInit();
        MinecraftForge.EVENT_BUS.register(new Events());
        DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> {
            MinecraftForge.EVENT_BUS.register(new ClientEvents());
            return new Object();
        });
    }

    public void setup(final FMLCommonSetupEvent event) {
        Networking.init();
        WorldGen.init();
        event.enqueueWork(() -> {
            CrucibleRegistry.init();
            RitualRegistry.init();
            CodexChapters.init();
            Registry.addBrewingRecipes();
            AltarEntries.init();
            MinecraftForge.EVENT_BUS.post(new RegisterDeitiesEvent());
            MinecraftForge.EVENT_BUS.post(new RegisterReagentsEvent());
            AthameItem.initHarvestables();
        });

        SpawnPlacements.register(Registry.ZOMBIE_BRUTE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            Monster::checkMonsterSpawnRules);
        SpawnPlacements.register(Registry.WRAITH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            Monster::checkMonsterSpawnRules);
        SpawnPlacements.register(Registry.RAVEN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            Animal::checkAnimalSpawnRules);
        SpawnPlacements.register(Registry.SLIMY_SLUG.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        	(e, w, t, pos, rand) -> pos.getY() < w.getHeight(Types.WORLD_SURFACE, pos.getX(), pos.getZ()));
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientSetup(final FMLClientSetupEvent event){
        BlockEntityRenderers.register(Registry.HAND_TILE_ENTITY, (trd) -> new HandTileRenderer());
        BlockEntityRenderers.register(Registry.BRAZIER_TILE_ENTITY, (trd) -> new BrazierTileRenderer());
        BlockEntityRenderers.register(Registry.NECROTIC_FOCUS_TILE_ENTITY, (trd) -> new NecroticFocusTileRenderer());
        BlockEntityRenderers.register(Registry.CRUCIBLE_TILE_ENTITY, (trd) -> new CrucibleTileRenderer());
        BlockEntityRenderers.register(Registry.SOUL_ENCHANTER_TILE_ENTITY, (trd) -> new SoulEnchanterTileRenderer());
        BlockEntityRenderers.register(Registry.GOBLET_TILE_ENTITY, (trd) -> new GobletTileRenderer());
        BlockEntityRenderers.register(Registry.CISTERN_TILE_ENTITY, (trd) -> new CisternTileRenderer());
        BlockEntityRenderers.register(Registry.PIPE_TILE_ENTITY, (trd) -> new PipeTileRenderer());

        ItemBlockRenderTypes.setRenderLayer(Registry.ENCHANTED_ASH.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(Registry.WOODEN_STAND.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(Registry.GOBLET.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(Registry.UNHOLY_EFFIGY.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(Registry.INCUBATOR.get(), (t) -> t == RenderType.solid() || t == RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(Registry.GLASS_TUBE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(Registry.CISTERN.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(Registry.MERAMMER_ROOT.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(Registry.SILDRIAN_SEED.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(Registry.OANNA_BLOOM.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(Registry.AVENNIAN_SPRIG.get(), RenderType.cutoutMipped());

        event.enqueueWork(() -> {
            MenuScreens.register(Registry.WORKTABLE_CONTAINER.get(), WorktableScreen::new);
            MenuScreens.register(Registry.SOUL_ENCHANTER_CONTAINER.get(), SoulEnchanterScreen::new);
            MenuScreens.register(Registry.WOODEN_STAND_CONTAINER.get(), WoodenBrewingStandScreen::new);
        });
    }

    public void sendImc(InterModEnqueueEvent evt) {
        InterModComms.sendTo("consecration", "holy_material", () -> "silver");
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.CHARM.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.RING.getMessageBuilder().size(2).build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BELT.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BODY.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.HEAD.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.NECKLACE.getMessageBuilder().build());
    }
}
