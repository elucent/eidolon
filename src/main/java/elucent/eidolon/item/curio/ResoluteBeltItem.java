package elucent.eidolon.item.curio;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.item.ItemBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Random;
import java.util.UUID;

public class ResoluteBeltItem extends ItemBase {
    UUID ATTR_ID = new UUID(3701779382882225399l, 5035874982077300549l);

    public ResoluteBeltItem(Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.addListener(ResoluteBeltItem::onHurt);
    }

    static Random random = new Random();

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (event.getSource().getTrueSource() instanceof LivingEntity && CuriosApi.getCuriosHelper().findEquippedCurio(Registry.RESOLUTE_BELT.get(), event.getEntityLiving()).isPresent()) {
            LivingEntity entity = (LivingEntity)event.getSource().getTrueSource();
            Vector3d diff = event.getEntityLiving().getPositionVec().subtract(entity.getPositionVec()).mul(1, 0, 1).normalize();
            entity.applyKnockback(0.8f, diff.x, diff.z);
            if (!entity.world.isRemote) entity.world.playSound(null, entity.getPosition(), SoundEvents.ENTITY_IRON_GOLEM_HURT, SoundCategory.PLAYERS, 1.0f, 1.9f + 0.2f * random.nextFloat());
        }
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT unused) {
        return new EidolonCurio(stack) {
            @Override
            public Multimap<Attribute, AttributeModifier> getAttributeModifiers(String identifier) {
                Multimap<Attribute, AttributeModifier> map = HashMultimap.create();
                map.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(ATTR_ID, Eidolon.MODID + ":resolute_belt", 1.0f, AttributeModifier.Operation.ADDITION));
                return map;
            }

            @Override
            public boolean canRightClickEquip() {
                return true;
            }
        };
    }
}
