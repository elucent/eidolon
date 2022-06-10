package elucent.eidolon.client.models;

import elucent.eidolon.Eidolon;
import elucent.eidolon.client.models.armor.TopHatModel;
import elucent.eidolon.client.models.armor.WarlockArmorModel;
import elucent.eidolon.client.models.entity.NecromancerModel;
import elucent.eidolon.client.models.entity.WraithModel;
import elucent.eidolon.client.models.entity.ZombieBruteModel;
import elucent.eidolon.client.renderer.blockentity.CrucibleTileRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModelRegistry {

    public static final ModelLayerLocation WRAITH = model("wraith");
    public static final ModelLayerLocation ZOMBIE_BURTE = model("zombie_brute");
    public static final ModelLayerLocation NECROMANCER = model("necromancer");
    public static final ModelLayerLocation CRUCIBLE = model("crucible");

    public static final ModelLayerLocation TOP_HAT = model("top_hat");

    public static final ModelLayerLocation WARLOCK_CHEST = model("warlock_chest");
    public static final ModelLayerLocation WARLOCK_HEAD = model("warlock_head");
    public static final ModelLayerLocation WARLOCK_FEET = model("warlock_feet");

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterRenderers event) {
        ForgeHooksClient.registerLayerDefinition(WRAITH, WraithModel::createBodyLayer);
        ForgeHooksClient.registerLayerDefinition(ZOMBIE_BURTE, ZombieBruteModel::createBodyLayer);
        ForgeHooksClient.registerLayerDefinition(NECROMANCER, NecromancerModel::createBodyLayer);

        ForgeHooksClient.registerLayerDefinition(CRUCIBLE, CrucibleTileRenderer::createBodyLayer);

        ForgeHooksClient.registerLayerDefinition(TOP_HAT, TopHatModel::createBodyLayer);

        ForgeHooksClient.registerLayerDefinition(WARLOCK_CHEST, () -> WarlockArmorModel.createBodyLayer(EquipmentSlot.CHEST));
        ForgeHooksClient.registerLayerDefinition(WARLOCK_HEAD, () -> WarlockArmorModel.createBodyLayer(EquipmentSlot.HEAD));
        ForgeHooksClient.registerLayerDefinition(WARLOCK_FEET, () -> WarlockArmorModel.createBodyLayer(EquipmentSlot.FEET));
    }

    private static ModelLayerLocation model(String pPath){
        return new ModelLayerLocation(new ResourceLocation(Eidolon.MODID, pPath), "main");
    }
}
