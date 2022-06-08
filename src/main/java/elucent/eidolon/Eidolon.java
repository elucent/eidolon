package elucent.eidolon;

import elucent.eidolon.capability.*;
import elucent.eidolon.codex.CodexChapters;
import elucent.eidolon.entity.*;
import elucent.eidolon.gui.SoulEnchanterScreen;
import elucent.eidolon.gui.WoodenBrewingStandScreen;
import elucent.eidolon.gui.WorktableScreen;
import elucent.eidolon.network.Networking;
import elucent.eidolon.proxy.ClientProxy;
import elucent.eidolon.proxy.ISidedProxy;
import elucent.eidolon.proxy.ServerProxy;
import elucent.eidolon.recipe.CrucibleRegistry;
import elucent.eidolon.recipe.WorktableRegistry;
import elucent.eidolon.ritual.RitualRegistry;
import elucent.eidolon.spell.AltarEntries;
import elucent.eidolon.tile.*;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
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
            WorktableRegistry.init();
            RitualRegistry.init();
            CodexChapters.init();
            Registry.addBrewingRecipes();
            AltarEntries.init();
        });
        event.enqueueWork(this::defineAttributes);

        CapabilityManager.INSTANCE.register(IReputation.class, new ReputationStorage(), ReputationImpl::new);
        CapabilityManager.INSTANCE.register(IKnowledge.class, new KnowledgeStorage(), KnowledgeImpl::new);

        EntitySpawnPlacementRegistry.register(Registry.ZOMBIE_BRUTE.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
            Monster::checkMonsterSpawnRules);
        EntitySpawnPlacementRegistry.register(Registry.WRAITH.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
            Monster::checkMonsterSpawnRules);
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientSetup(final FMLClientSetupEvent event){
        RenderingRegistry.registerEntityRenderingHandler(Registry.ZOMBIE_BRUTE.get(), (erm) -> new ZombieBruteRenderer(erm, new ZombieBruteModel(), 0.6f));
        RenderingRegistry.registerEntityRenderingHandler(Registry.WRAITH.get(), (erm) -> new WraithRenderer(erm, new WraithModel(), 0.6f));
        RenderingRegistry.registerEntityRenderingHandler(Registry.NECROMANCER.get(), (erm) -> new NecromancerRenderer(erm, new NecromancerModel(0), 0.6f));
        RenderingRegistry.registerEntityRenderingHandler(Registry.SOULFIRE_PROJECTILE.get(), (erm) -> new EmptyRenderer(erm));
        RenderingRegistry.registerEntityRenderingHandler(Registry.BONECHILL_PROJECTILE.get(), (erm) -> new EmptyRenderer(erm));
        RenderingRegistry.registerEntityRenderingHandler(Registry.NECROMANCER_SPELL.get(), (erm) -> new EmptyRenderer(erm));
        RenderingRegistry.registerEntityRenderingHandler(Registry.CHANT_CASTER.get(), (erm) -> new EmptyRenderer(erm));
        ClientRegistry.bindTileEntityRenderer(Registry.HAND_TILE_ENTITY, (trd) -> new HandTileRenderer(trd));
        ClientRegistry.bindTileEntityRenderer(Registry.BRAZIER_TILE_ENTITY, (trd) -> new BrazierTileRenderer(trd));
        ClientRegistry.bindTileEntityRenderer(Registry.NECROTIC_FOCUS_TILE_ENTITY, (trd) -> new NecroticFocusTileRenderer(trd));
        ClientRegistry.bindTileEntityRenderer(Registry.CRUCIBLE_TILE_ENTITY, (trd) -> new CrucibleTileRenderer(trd));
        ClientRegistry.bindTileEntityRenderer(Registry.SOUL_ENCHANTER_TILE_ENTITY, (trd) -> new SoulEnchanterTileRenderer(trd));
        ClientRegistry.bindTileEntityRenderer(Registry.GOBLET_TILE_ENTITY, (trd) -> new GobletTileRenderer(trd));

        ItemBlockRenderTypes.setRenderLayer(Registry.ENCHANTED_ASH.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(Registry.WOODEN_STAND.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(Registry.GOBLET.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(Registry.UNHOLY_EFFIGY.get(), RenderType.cutoutMipped());

        event.enqueueWork(() -> {
            ScreenManager.register(Registry.WORKTABLE_CONTAINER.get(), WorktableScreen::new);
            ScreenManager.register(Registry.SOUL_ENCHANTER_CONTAINER.get(), SoulEnchanterScreen::new);
            ScreenManager.register(Registry.WOODEN_STAND_CONTAINER.get(), WoodenBrewingStandScreen::new);
        });
    }

    public void defineAttributes() {
        DefaultAttributes.put(Registry.ZOMBIE_BRUTE.get(), ZombieBruteEntity.createAttributes());
        DefaultAttributes.put(Registry.WRAITH.get(), WraithEntity.createAttributes());
        DefaultAttributes.put(Registry.NECROMANCER.get(), NecromancerEntity.createAttributes());
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
