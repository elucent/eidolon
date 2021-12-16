package elucent.eidolon.item.curio;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import com.mojang.math.Matrix4f;

import elucent.eidolon.Registry;
import elucent.eidolon.item.ItemBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;

import net.minecraft.world.item.Item.Properties;

public class SanguineAmuletItem extends ItemBase {
    public SanguineAmuletItem(Properties properties) {
        super(properties);
        DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> {
        	MinecraftForgeClient.registerTooltipComponentFactory(SanguineAmuletTooltipInfo.class, SanguineAmuletTooltipComponent::new);
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
        int newCharge = Mth.clamp(getCharge(stack) + diff, 0, 40);
        stack.getOrCreateTag().putInt("charge", newCharge);
    }

    static void setCharge(ItemStack stack, int charge) {
        int newCharge = Mth.clamp(charge, 0, 40);
        stack.getOrCreateTag().putInt("charge", newCharge);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag unused) {
        return new EidolonCurio(stack) {
            @Override
            public void curioTick(String type, int index, LivingEntity entity) {
                if (!entity.level.isClientSide) {
                    if (entity.tickCount % 80 == 0 &&
                        entity.getHealth() >= entity.getMaxHealth() - 0.0001 &&
                        entity instanceof Player && ((Player) entity).getFoodData().getFoodLevel() >= 18 &&
                        getCharge(stack) < 40) {
                        Player player = (Player) entity;
                        float f = player.getFoodData().getSaturationLevel() > 0 ?
                            Math.min(4 * player.getFoodData().getSaturationLevel(), 16.0F) : 4.0f;
                        player.causeFoodExhaustion(f);
                        addCharge(stack, 1);
                        ThrownEnderpearl e;
                    }
                    if (entity.tickCount % 10 == 0 &&
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
            public CompoundTag writeSyncData() {
                CompoundTag nbt = new CompoundTag();
                nbt.putInt("charge", getCharge(stack));
                return nbt;
            }

            public void readSyncData(CompoundTag compound) {
                setCharge(stack, compound.getInt("charge"));
            }

            @Override
            public boolean canRightClickEquip() {
                return true;
            }
        };
    }
    
    public static class SanguineAmuletTooltipInfo implements TooltipComponent {
    	ItemStack stack;
    	int maxWidth;
    	
    	public SanguineAmuletTooltipInfo(ItemStack stack, int maxWidth) {
    		this.stack = stack;
    		this.maxWidth = maxWidth;
    	}
    }
    
    public static class SanguineAmuletTooltipComponent implements ClientTooltipComponent {
    	ItemStack stack;
    	int maxWidth;
    	public SanguineAmuletTooltipComponent(SanguineAmuletTooltipInfo info) {
    		this.stack = info.stack;
    		this.maxWidth = info.maxWidth;
    	}
    	
		@Override
		public int getHeight() {
            int charge = getCharge(stack);
            int rows = (charge + 19) / 20;
            return 8 + 12 * rows;
		}

		@Override
		public int getWidth(Font font) {
			return maxWidth;
		}

		@Override
	    public void renderImage(Font font, int x, int y, PoseStack poseStack, ItemRenderer itemRender, int p_194053_) {
            Minecraft mc = Minecraft.getInstance();
            RenderSystem.setShaderTexture(0, new ResourceLocation("minecraft", "textures/gui/icons.png"));
            int charge = getCharge(stack);
            int rows = (charge + 19) / 20;
            for (int i = 0; i < charge; i += 20) {
                for (int j = 0; j < Mth.clamp(charge - i, 0, 20); j += 2) {
                    if (charge - (i + j) == 1) {
                        GuiComponent.blit(poseStack, x - 1 + j / 2 * 8, y + (i / 20) * 9 + 2, 61, 0, 9, 9, 256, 256);
                    } else
                        GuiComponent.blit(poseStack, x - 1 + j / 2 * 8, y + (i / 20) * 9 + 2, 52, 0, 9, 9, 256, 256);
                }
            }
	    }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void renderTooltip(RenderTooltipEvent.GatherComponents event) {
        ItemStack stack = event.getItemStack();
        if (stack.getItem() == Registry.SANGUINE_AMULET.get()) {
        	event.getTooltipElements().add(Either.right(new SanguineAmuletTooltipInfo(stack, event.getMaxWidth())));
        }
    }
}
