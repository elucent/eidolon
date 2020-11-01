package elucent.eidolon;

import elucent.eidolon.entity.*;
import elucent.eidolon.network.Networking;
import elucent.eidolon.proxy.ClientProxy;
import elucent.eidolon.proxy.ISidedProxy;
import elucent.eidolon.proxy.ServerProxy;
import elucent.eidolon.recipe.CrucibleRegistry;
import elucent.eidolon.ritual.RitualRegistry;
import elucent.eidolon.tile.*;
import net.minecraft.block.BlockRenderType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeRegistry;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

import java.util.Random;

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
        MinecraftForge.EVENT_BUS.register(new Events());
        Registry registry = new Registry();
    }

    public void setup(final FMLCommonSetupEvent event) {
        WorldGen.init();
        Networking.init();
        CrucibleRegistry.init();
        RitualRegistry.init();
        event.enqueueWork(this::defineAttributes);

        EntitySpawnPlacementRegistry.register(Registry.ZOMBIE_BRUTE.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
            MonsterEntity::canMonsterSpawnInLight);
        EntitySpawnPlacementRegistry.register(Registry.WRAITH.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
            MonsterEntity::canMonsterSpawnInLight);
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientSetup(final FMLClientSetupEvent event){
        RenderingRegistry.registerEntityRenderingHandler(Registry.ZOMBIE_BRUTE.get(), (erm) -> new ZombieBruteRenderer(erm, new ZombieBruteModel(), 0.6f));
        RenderingRegistry.registerEntityRenderingHandler(Registry.WRAITH.get(), (erm) -> new WraithRenderer(erm, new WraithModel(), 0.6f));
        ClientRegistry.bindTileEntityRenderer(Registry.HAND_TILE_ENTITY, (trd) -> new HandTileRenderer(trd));
        ClientRegistry.bindTileEntityRenderer(Registry.BRAZIER_TILE_ENTITY, (trd) -> new BrazierTileRenderer(trd));
        ClientRegistry.bindTileEntityRenderer(Registry.CRAFTING_ALTAR_TILE_ENTITY, (trd) -> new CraftingAltarRenderer(trd));
        ClientRegistry.bindTileEntityRenderer(Registry.NECROTIC_FOCUS_TILE_ENTITY, (trd) -> new NecroticFocusTileRenderer(trd));
        ClientRegistry.bindTileEntityRenderer(Registry.CRUCIBLE_TILE_ENTITY, (trd) -> new CrucibleTileRenderer(trd));

        RenderTypeLookup.setRenderLayer(Registry.ENCHANTED_ASH.get(), RenderType.getCutoutMipped());
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
