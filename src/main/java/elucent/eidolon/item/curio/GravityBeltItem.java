package elucent.eidolon.item.curio;

import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.item.ItemBase;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;

public class GravityBeltItem extends ItemBase {
    UUID ATTR_ID = new UUID(6937061617091731127l, 7120126291930051139l);
    public GravityBeltItem(Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.addListener(GravityBeltItem::onFall);
    }

    @SubscribeEvent
    public static void onFall(LivingFallEvent event) {
        if (CuriosApi.getCuriosHelper().findEquippedCurio(Registry.GRAVITY_BELT.get(), event.getEntityLiving()).isPresent()) {
            event.setDistance(event.getDistance() / 4);
        }
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT unused) {
        return new EidolonCurio(stack) {
            @Override
            public Multimap<Attribute, AttributeModifier> getAttributeModifiers(String identifier) {
                Multimap<Attribute, AttributeModifier> map = HashMultimap.create();
                map.put(ForgeMod.ENTITY_GRAVITY.get(), new AttributeModifier(ATTR_ID, Eidolon.MODID + ":gravity_belt", -0.60f, AttributeModifier.Operation.MULTIPLY_TOTAL));
                return map;
            }

            @Override
            public boolean canRightClickEquip() {
                return true;
            }
        };
    }
}
