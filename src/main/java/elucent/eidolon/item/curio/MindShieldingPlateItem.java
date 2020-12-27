package elucent.eidolon.item.curio;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import elucent.eidolon.Registry;
import elucent.eidolon.item.ItemBase;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effects;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
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
    }

    @SubscribeEvent
    public static void onPotion(PotionEvent.PotionApplicableEvent event) {
        if (event.getPotionEffect().getPotion() == Effects.NAUSEA && CuriosApi.getCuriosHelper().findEquippedCurio(Registry.MIND_SHIELDING_PLATE.get(), event.getEntityLiving()).isPresent()) {
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        if (event.getOriginal().experienceLevel > 0 && CuriosApi.getCuriosHelper().findEquippedCurio(Registry.MIND_SHIELDING_PLATE.get(), event.getEntityLiving()).isPresent()) {
            event.getPlayer().experienceLevel = event.getOriginal().experienceLevel * 3 / 4;
            event.getPlayer().experience = event.getOriginal().experience * 3 / 4;
        }
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT unused) {
        return new EidolonCurio(stack) {
            @Override
            public boolean canRightClickEquip() {
                return true;
            }
        };
    }
}
