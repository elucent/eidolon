package elucent.eidolon.item.curio;

import elucent.eidolon.Registry;
import elucent.eidolon.item.ItemBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;

public class GlassHandItem extends ItemBase {
    public GlassHandItem(Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.addListener(GlassHandItem::onHurt);
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (CuriosApi.getCuriosHelper().findEquippedCurio(Registry.GLASS_HAND.get(), event.getEntityLiving()).isPresent()) {
            event.setAmount(event.getAmount() * 5);
        }
        if (event.getSource().getEntity() instanceof LivingEntity &&
            CuriosApi.getCuriosHelper().findEquippedCurio(Registry.GLASS_HAND.get(), (LivingEntity)event.getSource().getEntity()).isPresent()) {
            event.setAmount(event.getAmount() * 2);
        }
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag unused) {
        return new EidolonCurio(stack) {
            @Override
            public boolean canRightClickEquip() {
                return true;
            }
        };
    }
}
