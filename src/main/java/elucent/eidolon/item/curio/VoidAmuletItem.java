package elucent.eidolon.item.curio;

import elucent.eidolon.Registry;
import elucent.eidolon.entity.SpellProjectileEntity;
import elucent.eidolon.item.ItemBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;

public class VoidAmuletItem extends ItemBase {
    public VoidAmuletItem(Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.addListener(VoidAmuletItem::onDamage);
    }

    static int getCooldown(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("cooldown")) {
            return stack.getTag().getInt("cooldown");
        }
        return 0;
    }

    static void setCooldown(ItemStack stack, int cooldown) {
        stack.getOrCreateTag().putInt("cooldown", cooldown);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag unused) {
        return new EidolonCurio(stack) {
            @Override
            public void curioTick(String type, int index, LivingEntity entity) {
                if (!entity.level.isClientSide) {
                    if (getCooldown(stack) > 0) setCooldown(stack, getCooldown(stack) - 1);
                }
            }

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
                    if (slots.getStackInSlot(i).getItem() == Registry.VOID_AMULET.get()) {
                        if (getCooldown(slots.getStackInSlot(i)) == 0) {
                            hasVoid = true;
                            break;
                        }
                    }
                }
                ItemStack stack = slots.getStackInSlot(i);
                if (hasVoid &&
                    (event.getSource().getDirectEntity() instanceof Fireball
                        || event.getSource().getDirectEntity() instanceof LlamaSpit
                        || event.getSource().getDirectEntity() instanceof SmallFireball
                        || event.getSource().getDirectEntity() instanceof ShulkerBullet
                        || event.getSource().getDirectEntity() instanceof ThrownPotion
                        || event.getSource().getDirectEntity() instanceof SpellProjectileEntity)) {
                    event.setCanceled(true);
                    if (!event.getEntity().getCommandSenderWorld().isClientSide) {
                        event.getEntity().getCommandSenderWorld().playSound(null, event.getEntity().blockPosition(), SoundEvents.WITHER_HURT, SoundSource.PLAYERS, 1.0f, 0.75f);
                        setCooldown(stack, 200);
                    }
                }
            });
        }
    }
}
