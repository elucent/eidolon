package elucent.eidolon.item.curio;

import elucent.eidolon.Registry;
import elucent.eidolon.item.ItemBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nonnull;

public class SanguineAmuletItem extends ItemBase {
    public SanguineAmuletItem(Properties properties) {
        super(properties);
        DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> {
            MinecraftForge.EVENT_BUS.addListener(SanguineAmuletItem::addTooltip);
            MinecraftForge.EVENT_BUS.addListener(SanguineAmuletItem::renderTooltip);
            return null;
        });
    }

    static int getCharge(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("charge")) {
            return stack.getTag().getInt("charge");
        }
        return 0;
    }

    static void addCharge(ItemStack stack, int diff) {
        int newCharge = MathHelper.clamp(getCharge(stack) + diff, 0, 40);
        stack.getOrCreateTag().putInt("charge", newCharge);
    }

    static void setCharge(ItemStack stack, int charge) {
        int newCharge = MathHelper.clamp(charge, 0, 40);
        stack.getOrCreateTag().putInt("charge", newCharge);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT unused) {
        return new EidolonCurio(stack) {
            @Override
            public void curioTick(String type, int index, LivingEntity entity) {
                if (!entity.world.isRemote) {
                    if (entity.ticksExisted % 80 == 0 &&
                        entity.getHealth() == entity.getMaxHealth() &&
                        entity instanceof PlayerEntity && ((PlayerEntity) entity).getFoodStats().getFoodLevel() >= 18 &&
                        getCharge(stack) < 40) {
                        PlayerEntity player = (PlayerEntity) entity;
                        float f = player.getFoodStats().getSaturationLevel() > 0 ?
                            Math.min(4 * player.getFoodStats().getSaturationLevel(), 16.0F) : 4.0f;
                        player.addExhaustion(f);
                        addCharge(stack, 1);
                        EnderPearlEntity e;
                    }
                    if (entity.ticksExisted % 10 == 0 &&
                        getCharge(stack) > 0 && entity.getHealth() < entity.getMaxHealth()) {
                        int taken = (int) Math.min(1, entity.getMaxHealth() - entity.getHealth());
                        addCharge(stack, -taken);
                        entity.heal(taken);
                    }
                }
            }

            @Override
            public boolean canSync(String identifier, int index, LivingEntity livingEntity) {
                return true;
            }

            @Nonnull
            public CompoundNBT writeSyncData() {
                CompoundNBT nbt = new CompoundNBT();
                nbt.putInt("charge", getCharge(stack));
                return nbt;
            }

            public void readSyncData(CompoundNBT compound) {
                setCharge(stack, compound.getInt("charge"));
            }

            @Override
            public boolean canRightClickEquip() {
                return true;
            }
        };
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void addTooltip(ItemTooltipEvent event) {
        if (event.getItemStack().getItem() == Registry.SANGUINE_AMULET.get()) {
            int charge = getCharge(event.getItemStack());
            if (charge > 0) event.getToolTip().add(new StringTextComponent(" "));
            for (int i = 0; i < charge; i += 20) {
                event.getToolTip().add(new StringTextComponent(" "));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void renderTooltip(RenderTooltipEvent.PostText event) {
        ItemStack stack = event.getStack();
        if (stack.getItem() == Registry.SANGUINE_AMULET.get()) {
            Minecraft mc = Minecraft.getInstance();
            mc.getTextureManager().bindTexture(new ResourceLocation("minecraft", "textures/gui/icons.png"));
            int charge = getCharge(event.getStack());
            int rows = (charge + 19) / 20;
            for (int i = 0; i < charge; i += 20) {
                for (int j = 0; j < MathHelper.clamp(charge - i, 0, 20); j += 2) {
                    if (charge - (i + j) == 1) {
                        AbstractGui.blit(event.getMatrixStack(), event.getX() - 1 + j / 2 * 8, event.getY() + (event.getLines().size() - rows) * (event.getFontRenderer().FONT_HEIGHT + 1) + (i / 20) * 9 + 2, 61, 0, 9, 9, 256, 256);
                    } else
                        AbstractGui.blit(event.getMatrixStack(), event.getX() - 1 + j / 2 * 8, event.getY() + (event.getLines().size() - rows) * (event.getFontRenderer().FONT_HEIGHT + 1) + (i / 20) * 9 + 2, 52, 0, 9, 9, 256, 256);
                }
            }
        }
    }
}
