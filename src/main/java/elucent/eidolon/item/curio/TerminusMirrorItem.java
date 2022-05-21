package elucent.eidolon.item.curio;

import elucent.eidolon.Registry;
import elucent.eidolon.entity.SpellProjectileEntity;
import elucent.eidolon.item.ItemBase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;

public class TerminusMirrorItem extends ItemBase {
    public TerminusMirrorItem(Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.addListener(TerminusMirrorItem::onDamage);
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

    @SubscribeEvent
    public static void onDamage(LivingAttackEvent event) {
        if (event.getEntityLiving() instanceof Player) {
            CuriosApi.getCuriosHelper().getEquippedCurios(event.getEntityLiving()).resolve().ifPresent((slots) -> {
                boolean hasVoid = false;
                int i;
                for (i = 0; i < slots.getSlots(); i ++) {
                    if (slots.getStackInSlot(i).getItem() == Registry.TERMINUS_MIRROR.get()) {
                        hasVoid = true;
                        break;
                    }
                }
                ItemStack stack = slots.getStackInSlot(i);
                if (hasVoid &&
                    (event.getSource().getDirectEntity() instanceof Projectile
                        || event.getSource().getDirectEntity() instanceof SpellProjectileEntity)) {
                    event.setCanceled(true);
                    if (!event.getEntity().getCommandSenderWorld().isClientSide)
                        event.getEntity().getCommandSenderWorld().playSound(null, event.getEntity().blockPosition(), SoundEvents.WITHER_HURT, SoundSource.PLAYERS, 1.0f, 0.75f);
                }
            });
        }
    }
}
