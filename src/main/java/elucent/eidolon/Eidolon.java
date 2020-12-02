package elucent.eidolon;

import elucent.eidolon.capability.*;
import elucent.eidolon.codex.CodexChapters;
import elucent.eidolon.entity.*;
import elucent.eidolon.gui.WorktableScreen;
import elucent.eidolon.network.Networking;
import elucent.eidolon.proxy.ClientProxy;
import elucent.eidolon.proxy.ISidedProxy;
import elucent.eidolon.proxy.ServerProxy;
import elucent.eidolon.recipe.CrucibleRegistry;
import elucent.eidolon.recipe.WorktableRegistry;
import elucent.eidolon.ritual.RitualRegistry;
import elucent.eidolon.tile.*;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
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

    public static final ItemGroup TAB = new ItemGroup(MODID) {
        @Override
        public ItemStack createIcon() {
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
        MinecraftForge.EVENT_BUS.register(new Events());
        Registry registry = new Registry();
    }

    public void setup(final FMLCommonSetupEvent event) {
        CodexChapters.init();
        Networking.init();
        WorldGen.init();
        event.enqueueWork(() -> {
            CrucibleRegistry.init();
            WorktableRegistry.init();
            RitualRegistry.init();
        });
        event.enqueueWork(this::defineAttributes);

        CapabilityManager.INSTANCE.register(IReputation.class, new ReputationStorage(), ReputationImpl::new);
        CapabilityManager.INSTANCE.register(IKnowledge.class, new KnowledgeStorage(), KnowledgeImpl::new);

        EntitySpawnPlacementRegistry.register(Registry.ZOMBIE_BRUTE.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
            MonsterEntity::canMonsterSpawnInLight);
        EntitySpawnPlacementRegistry.register(Registry.WRAITH.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
            MonsterEntity::canMonsterSpawnInLight);
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientSetup(final FMLClientSetupEvent event){
        RenderingRegistry.registerEntityRenderingHandler(Registry.ZOMBIE_BRUTE.get(), (erm) -> new ZombieBruteRenderer(erm, new ZombieBruteModel(), 0.6f));
        RenderingRegistry.registerEntityRenderingHandler(Registry.WRAITH.get(), (erm) -> new WraithRenderer(erm, new WraithModel(), 0.6f));
        RenderingRegistry.registerEntityRenderingHandler(Registry.SOULFIRE_PROJECTILE.get(), (erm) -> new EmptyRenderer(erm));
        RenderingRegistry.registerEntityRenderingHandler(Registry.BONECHILL_PROJECTILE.get(), (erm) -> new EmptyRenderer(erm));
        RenderingRegistry.registerEntityRenderingHandler(Registry.CHANT_CASTER.get(), (erm) -> new EmptyRenderer(erm));
        ClientRegistry.bindTileEntityRenderer(Registry.HAND_TILE_ENTITY, (trd) -> new HandTileRenderer(trd));
        ClientRegistry.bindTileEntityRenderer(Registry.BRAZIER_TILE_ENTITY, (trd) -> new BrazierTileRenderer(trd));
        ClientRegistry.bindTileEntityRenderer(Registry.NECROTIC_FOCUS_TILE_ENTITY, (trd) -> new NecroticFocusTileRenderer(trd));
        ClientRegistry.bindTileEntityRenderer(Registry.CRUCIBLE_TILE_ENTITY, (trd) -> new CrucibleTileRenderer(trd));
        ClientRegistry.bindTileEntityRenderer(Registry.PODIUM_TILE_ENTITY, (trd) -> new PodiumTileRenderer(trd));
        ClientRegistry.bindTileEntityRenderer(Registry.OFFERTORY_PLATE_TILE_ENTITY, (trd) -> new OffertoryPlateTileRenderer(trd));

        RenderTypeLookup.setRenderLayer(Registry.ENCHANTED_ASH.get(), RenderType.getCutoutMipped());

        event.enqueueWork(() -> {
            ScreenManager.registerFactory(Registry.WORKTABLE_CONTAINER.get(), WorktableScreen::new);
        });
    }

    public void defineAttributes() {
        GlobalEntityTypeAttributes.put(Registry.ZOMBIE_BRUTE.get(), ZombieBruteEntity.createAttributes());
        GlobalEntityTypeAttributes.put(Registry.WRAITH.get(), WraithEntity.createAttributes());
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
