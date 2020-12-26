package elucent.eidolon.item.curio;

import elucent.eidolon.Registry;
import elucent.eidolon.entity.SpellProjectileEntity;
import elucent.eidolon.item.ItemBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.LlamaSpitEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
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
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT unused) {
        return new EidolonCurio(stack) {
            @Override
            public void curioTick(String type, int index, LivingEntity entity) {
                if (!entity.world.isRemote) {
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
        if (event.getEntityLiving() instanceof PlayerEntity) {
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
                    (event.getSource().getImmediateSource() instanceof FireballEntity
                        || event.getSource().getImmediateSource() instanceof LlamaSpitEntity
                        || event.getSource().getImmediateSource() instanceof SmallFireballEntity
                        || event.getSource().getImmediateSource() instanceof ShulkerBulletEntity
                        || event.getSource().getImmediateSource() instanceof PotionEntity
                        || event.getSource().getImmediateSource() instanceof SpellProjectileEntity)) {
                    event.setCanceled(true);
                    if (!event.getEntity().getEntityWorld().isRemote) {
                        event.getEntity().getEntityWorld().playSound(null, event.getEntity().getPosition(), SoundEvents.ENTITY_WITHER_HURT, SoundCategory.PLAYERS, 1.0f, 0.75f);
                        setCooldown(stack, 200);
                    }
                }
            });
        }
    }
}
