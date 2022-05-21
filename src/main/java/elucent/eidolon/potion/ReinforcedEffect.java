package elucent.eidolon.potion;

import elucent.eidolon.Eidolon;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeMobEffect;

public class ReinforcedEffect extends MobEffect implements IForgeMobEffect {
    public ReinforcedEffect() {
        super(MobEffectCategory.BENEFICIAL, ColorUtil.packColor(255, 250, 214, 74));
    }
    
    protected static final ResourceLocation EFFECT_TEXTURE = new ResourceLocation(Eidolon.MODID, "textures/mob_effect/reinforced.png");

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
