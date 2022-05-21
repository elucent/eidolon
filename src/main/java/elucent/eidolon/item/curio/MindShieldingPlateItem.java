package elucent.eidolon.item.curio;

import elucent.eidolon.Registry;
import elucent.eidolon.item.ItemBase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;

public class MindShieldingPlateItem extends ItemBase {
    public MindShieldingPlateItem(Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.addListener(MindShieldingPlateItem::onPotion);
        MinecraftForge.EVENT_BUS.addListener(MindShieldingPlateItem::onClone);
        MinecraftForge.EVENT_BUS.addListener(MindShieldingPlateItem::onDropXP);
    }

    @SubscribeEvent
    public static void onPotion(PotionEvent.PotionApplicableEvent event) {
        if (event.getPotionEffect().getEffect() == MobEffects.CONFUSION && CuriosApi.getCuriosHelper().findEquippedCurio(Registry.MIND_SHIELDING_PLATE.get(), event.getEntityLiving()).isPresent()) {
            event.setResult(Event.Result.DENY);
        }
    }

    static final int LEVEL_FLAG = 1 << 30;

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        if ((event.getOriginal().experienceLevel & LEVEL_FLAG) != 0) {
            event.getPlayer().experienceLevel = event.getOriginal().experienceLevel & ~LEVEL_FLAG;
            event.getPlayer().experienceProgress = event.getOriginal().experienceProgress;
        }
    }

    @SubscribeEvent
    public static void onDropXP(LivingExperienceDropEvent event) {
        if (event.getEntity() instanceof Player && CuriosApi.getCuriosHelper().findEquippedCurio(Registry.MIND_SHIELDING_PLATE.get(), event.getEntityLiving()).isPresent()) {
            ((Player)event.getEntity()).experienceLevel |= LEVEL_FLAG;
            event.setCanceled(true);
        }
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag unused) {
        return new EidolonCurio(stack) {
            @Override
            public boolean canRightClickEquip() {
                return true;
            }
        };
    }
}
