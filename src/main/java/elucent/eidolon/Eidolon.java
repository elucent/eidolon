package elucent.eidolon;

import elucent.eidolon.client.models.ModelRegistry;
import elucent.eidolon.client.models.entity.NecromancerModel;
import elucent.eidolon.client.models.entity.WraithModel;
import elucent.eidolon.client.models.entity.ZombieBruteModel;
import elucent.eidolon.client.renderer.blockentity.*;
import elucent.eidolon.client.renderer.entity.NecromancerRenderer;
import elucent.eidolon.client.renderer.entity.WraithRenderer;
import elucent.eidolon.client.renderer.entity.ZombieBruteRenderer;
import elucent.eidolon.codex.CodexChapters;
import elucent.eidolon.entity.EmptyRenderer;
import elucent.eidolon.entity.NecromancerEntity;
import elucent.eidolon.entity.WraithEntity;
import elucent.eidolon.entity.ZombieBruteEntity;
import elucent.eidolon.gui.SoulEnchanterScreen;
import elucent.eidolon.gui.WoodenBrewingStandScreen;
import elucent.eidolon.gui.WorktableScreen;
import elucent.eidolon.network.Networking;
import elucent.eidolon.proxy.ClientProxy;
import elucent.eidolon.proxy.ISidedProxy;
import elucent.eidolon.proxy.ServerProxy;
import elucent.eidolon.recipe.recipes.register.RecipeManager;
import elucent.eidolon.ritual.RitualRegistry;
import elucent.eidolon.spell.AltarEntries;
import elucent.eidolon.world.worldgen.WorldGen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
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
        MinecraftForge.EVENT_BUS.register(new WorldGen());
        WorldGen.preInit();

        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        WorldGen.register(bus);
        RecipeManager.register(bus);

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
            RitualRegistry.init();
            CodexChapters.init();
            Registry.addBrewingRecipes();
            AltarEntries.init();
        });
        //event.enqueueWork(this::defineAttributes);

        // CapabilityManager.INSTANCE.register(IReputation.class, new ReputationStorage(), ReputationImpl::new);
        // CapabilityManager.INSTANCE.register(IKnowledge.class, new KnowledgeStorage(), KnowledgeImpl::new);

        SpawnPlacements.register(Registry.ZOMBIE_BRUTE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            Monster::checkMonsterSpawnRules);
        SpawnPlacements.register(Registry.WRAITH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            Monster::checkMonsterSpawnRules);
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientSetup(final FMLClientSetupEvent event){
        EntityRenderers.register(Registry.ZOMBIE_BRUTE.get(), (erm) -> new ZombieBruteRenderer(erm, new ZombieBruteModel(erm.bakeLayer(ModelRegistry.ZOMBIE_BURTE)), 0.6f));
        EntityRenderers.register(Registry.WRAITH.get(), (erm) -> new WraithRenderer(erm, new WraithModel(erm.bakeLayer(ModelRegistry.WRAITH)), 0.6f));
        EntityRenderers.register(Registry.NECROMANCER.get(), (erm) -> new NecromancerRenderer(erm, new NecromancerModel(erm.bakeLayer(ModelRegistry.NECROMANCER)), 0.6f));
        EntityRenderers.register(Registry.SOULFIRE_PROJECTILE.get(), EmptyRenderer::new);
        EntityRenderers.register(Registry.BONECHILL_PROJECTILE.get(), EmptyRenderer::new);
        EntityRenderers.register(Registry.NECROMANCER_SPELL.get(), EmptyRenderer::new);
        EntityRenderers.register(Registry.CHANT_CASTER.get(), EmptyRenderer::new);
        BlockEntityRenderers.register(Registry.HAND_TILE_ENTITY.get(), (trd) -> new HandTileRenderer(trd.getBlockEntityRenderDispatcher()));
        BlockEntityRenderers.register(Registry.BRAZIER_TILE_ENTITY.get(), (trd) -> new BrazierTileRenderer(trd.getBlockEntityRenderDispatcher()));
        BlockEntityRenderers.register(Registry.NECROTIC_FOCUS_TILE_ENTITY.get(), (trd) -> new NecroticFocusTileRenderer(trd.getBlockEntityRenderDispatcher()));
        BlockEntityRenderers.register(Registry.CRUCIBLE_TILE_ENTITY.get(), (trd) -> new CrucibleTileRenderer(trd.getBlockEntityRenderDispatcher()));
        BlockEntityRenderers.register(Registry.SOUL_ENCHANTER_TILE_ENTITY.get(), (trd) -> new SoulEnchanterTileRenderer(trd.getBlockEntityRenderDispatcher()));
        BlockEntityRenderers.register(Registry.GOBLET_TILE_ENTITY.get(), (trd) -> new GobletTileRenderer(trd.getBlockEntityRenderDispatcher()));

        ItemBlockRenderTypes.setRenderLayer(Registry.ENCHANTED_ASH.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(Registry.WOODEN_STAND.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(Registry.GOBLET.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(Registry.UNHOLY_EFFIGY.get(), RenderType.cutoutMipped());

        event.enqueueWork(() -> {
            MenuScreens.register(Registry.WORKTABLE_CONTAINER.get(), WorktableScreen::new);
            MenuScreens.register(Registry.SOUL_ENCHANTER_CONTAINER.get(), SoulEnchanterScreen::new);
            MenuScreens.register(Registry.WOODEN_STAND_CONTAINER.get(), WoodenBrewingStandScreen::new);
        });
    }

    @SubscribeEvent
    public void defineAttributes(EntityAttributeCreationEvent event) {
        event.put(Registry.ZOMBIE_BRUTE.get(), ZombieBruteEntity.createAttributes());
        event.put(Registry.WRAITH.get(), WraithEntity.createAttributes());
        event.put(Registry.NECROMANCER.get(), NecromancerEntity.createAttributes());
    }

    public void sendImc(InterModEnqueueEvent evt) {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.CHARM.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.RING.getMessageBuilder().size(2).build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BELT.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BODY.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.HEAD.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.NECKLACE.getMessageBuilder().build());
    }
}
