package elucent.eidolon.potion;

import elucent.eidolon.Eidolon;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeMobEffect;

public class UndeathEffect extends MobEffect implements IForgeMobEffect {
    public UndeathEffect() {
        super(MobEffectCategory.HARMFUL, ColorUtil.packColor(255, 51, 39, 42));
    }
    
    protected static final ResourceLocation EFFECT_TEXTURE = new ResourceLocation(Eidolon.MODID, "textures/mob_effect/undeath.png");

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
