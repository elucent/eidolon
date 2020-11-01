package elucent.eidolon.item.curio;

import elucent.eidolon.Registry;
import elucent.eidolon.item.ItemBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import javax.annotation.Nonnull;

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
                        || event.getSource().getImmediateSource() instanceof PotionEntity)) {
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
