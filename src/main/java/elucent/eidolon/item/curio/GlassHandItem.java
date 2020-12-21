package elucent.eidolon.item.curio;

import elucent.eidolon.Registry;
import elucent.eidolon.item.ItemBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effects;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.UUID;

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
        if (event.getSource().getTrueSource() instanceof LivingEntity &&
            CuriosApi.getCuriosHelper().findEquippedCurio(Registry.GLASS_HAND.get(), (LivingEntity)event.getSource().getTrueSource()).isPresent()) {
            event.setAmount(event.getAmount() * 2);
        }
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT unused) {
        return new EidolonCurio(stack) {
            @Override
            public boolean canRightClickEquip() {
                return true;
            }
        };
    }
}
