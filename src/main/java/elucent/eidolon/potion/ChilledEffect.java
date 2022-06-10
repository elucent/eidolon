package elucent.eidolon.potion;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.EffectRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeMobEffect;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;

import java.util.Random;
import java.util.function.Consumer;

public class ChilledEffect extends MobEffect implements IForgeMobEffect {
    static int packColor(int alpha, int red, int green, int blue) {
        return alpha << 24 | red << 16 | green << 8 | blue;
    }

    public ChilledEffect() {
        super(MobEffectCategory.HARMFUL, packColor(255, 147, 189, 245));
        MinecraftForge.EVENT_BUS.addListener(this::chill);
        DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> {
            MinecraftForge.EVENT_BUS.addListener(ChilledEffect::renderChill);
            return null;
        });
    }

    @SubscribeEvent
    public void chill(LivingHealEvent event) {
        LivingEntity e = event.getEntityLiving();
        if (e.hasEffect(this)) {
            event.setCanceled(true);
        }
    }

    protected static final ResourceLocation ICONS_TEXTURE = new ResourceLocation(Eidolon.MODID, "textures/gui/icons.png");
    protected static final ResourceLocation EFFECT_TEXTURE = new ResourceLocation(Eidolon.MODID, "textures/mob_effect/chilled.png");

    @Override
    public void initializeClient(Consumer<EffectRenderer> consumer) {
        consumer.accept(new EffectRenderer() {
            @Override
            public void renderInventoryEffect(MobEffectInstance effectInstance, EffectRenderingInventoryScreen<?> gui, PoseStack poseStack, int x, int y, float z) {
                Minecraft mc = Minecraft.getInstance();
                RenderSystem.setShaderTexture(0, EFFECT_TEXTURE);
                gui.blit(poseStack, x, y, 0, 0, 18, 18);
            }

            @Override
            public void renderHUDEffect(MobEffectInstance effectInstance, GuiComponent gui, PoseStack poseStack, int x, int y, float z, float alpha) {
                Minecraft mc = Minecraft.getInstance();
                RenderSystem.setShaderTexture(0, EFFECT_TEXTURE);
                gui.blit(poseStack, x, y, 0, 0, 18, 18);
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void renderChill(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (event.getType() == RenderGameOverlayEvent.ElementType.LAYER
            && player.hasEffect(Registry.CHILLED_EFFECT.get())) {

            PoseStack mStack = event.getMatrixStack();
            mStack.pushPose();
            mStack.translate(0, 0, 0.01);

            int health = Mth.ceil(player.getHealth());
            float absorb = Mth.ceil(player.getAbsorptionAmount());
            AttributeInstance attrMaxHealth = player.getAttribute(Attributes.MAX_HEALTH);
            float healthMax = (float)attrMaxHealth.getValue();

            int ticks = mc.gui.getGuiTicks();

            int regen = -1;
            if (player.hasEffect(MobEffects.REGENERATION)) regen = ticks % 25;

            Random rand = new Random();
            rand.setSeed(ticks * 312871L);

            int left = event.getWindow().getGuiScaledWidth() / 2 - 91;
            int top = event.getWindow().getGuiScaledHeight() - ((ForgeIngameGui) Minecraft.getInstance().gui).left_height;

            int healthRows = Mth.ceil((healthMax + absorb) / 2.0F / 10.0F);
            int rowHeight = Math.max(10 - (healthRows - 2), 3);

            RenderSystem.setShaderTexture(0, ICONS_TEXTURE);
            for (int i = Mth.ceil((healthMax) / 2.0F) - 1; i >= 0; -- i) {
                int row = Mth.ceil((float)(i + 1) / 10.0F) - 1;
                int x = left + i % 10 * 8;
                int y = top + rowHeight - 1; // we don't need to worry about rowHeight, the usual health rendering already added it to top

                if (health <= 4) y += rand.nextInt(2);
                if (i == regen) y -= 2;

                // todo alpha test?
                //RenderSystem.disableAlphaTest();
                RenderSystem.enableBlend();
                if (i * 2 + 1 < health)
                    mc.gui.blit(mStack, x, y, 0, 0, 9, 9);
                else if (i * 2 + 1 == health)
                    mc.gui.blit(mStack, x, y, 9, 0, 9, 9);
                RenderSystem.disableBlend();
                //RenderSystem.enableAlphaTest();
            }
            //RenderSystem.popAttributes();
            mStack.popPose();
        }
    }
}
