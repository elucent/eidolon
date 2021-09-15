package elucent.eidolon.item.curio;

import elucent.eidolon.Registry;
import elucent.eidolon.item.ItemBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;

public class WardedMailItem extends ItemBase {
    public WardedMailItem(Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.addListener(WardedMailItem::onDamage);
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

    @SubscribeEvent
    public static void onDamage(LivingAttackEvent event) {
        if (event.getSource().isMagicDamage()) {
            CuriosApi.getCuriosHelper().getEquippedCurios(event.getEntityLiving()).resolve().ifPresent((slots) -> {
                boolean hasMail = false;
                for (int i = 0; i < slots.getSlots(); i ++) {
                    if (slots.getStackInSlot(i).getItem() == Registry.WARDED_MAIL.get()) {
                        event.setCanceled(true);
                        event.getEntityLiving().attackEntityFrom(new DamageSource(event.getSource().getDamageType()), event.getAmount());
                        return;
                    }
                }
            });
        }
    }
}
