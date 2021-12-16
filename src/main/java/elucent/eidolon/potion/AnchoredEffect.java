package elucent.eidolon.potion;

import elucent.eidolon.Eidolon;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeMobEffect;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AnchoredEffect extends MobEffect implements IForgeMobEffect {
    public AnchoredEffect() {
        super(MobEffectCategory.BENEFICIAL, ColorUtil.packColor(255, 154, 58, 232));
        MinecraftForge.EVENT_BUS.addListener(this::anchor);
    }

    @SubscribeEvent
    public void anchor(EntityTeleportEvent event) {
        Entity e = event.getEntity();
        if (e instanceof LivingEntity && ((LivingEntity)e).hasEffect(this)) {
            event.setCanceled(true);
        }
    }

    protected static final ResourceLocation EFFECT_TEXTURE = new ResourceLocation(Eidolon.MODID, "textures/mob_effect/anchored.png");

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public void renderInventoryEffect(EffectInstance effect, DisplayEffectsScreen<?> gui, MatrixStack mStack, int x, int y, float z) {
//        Minecraft mc = Minecraft.getInstance();
//        mc.getTextureManager().bindTexture(EFFECT_TEXTURE);
//        AbstractGui.blit(mStack, x, y, 18, 18, 0, 0, 18, 18, 18, 18);
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public void renderHUDEffect(EffectInstance effect, AbstractGui gui, MatrixStack mStack, int x, int y, float z, float alpha) {
//        Minecraft mc = Minecraft.getInstance();
//        mc.getTextureManager().bindTexture(EFFECT_TEXTURE);
//        AbstractGui.blit(mStack, x, y, 18, 18, 0, 0, 18, 18, 18, 18);
//    }
}
