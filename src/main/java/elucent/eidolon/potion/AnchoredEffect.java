package elucent.eidolon.potion;

import com.mojang.blaze3d.matrix.MatrixStack;
import elucent.eidolon.Eidolon;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeEffect;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AnchoredEffect extends Effect implements IForgeEffect {
    public AnchoredEffect() {
        super(EffectType.BENEFICIAL, ColorUtil.packColor(255, 154, 58, 232));
        MinecraftForge.EVENT_BUS.addListener(this::anchor);
    }

    @SubscribeEvent
    public void anchor(EnderTeleportEvent event) {
        LivingEntity e = event.getEntityLiving();
        if (e.isPotionActive(this)) {
            event.setCanceled(true);
        }
    }

    protected static final ResourceLocation EFFECT_TEXTURE = new ResourceLocation(Eidolon.MODID, "textures/mob_effect/anchored.png");

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderInventoryEffect(EffectInstance effect, DisplayEffectsScreen<?> gui, MatrixStack mStack, int x, int y, float z) {
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bindTexture(EFFECT_TEXTURE);
        gui.blit(mStack, x, y, 0, 0, 18, 18);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderHUDEffect(EffectInstance effect, AbstractGui gui, MatrixStack mStack, int x, int y, float z, float alpha) {
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bindTexture(EFFECT_TEXTURE);
        gui.blit(mStack, x, y, 0, 0, 18, 18);
    }
}
