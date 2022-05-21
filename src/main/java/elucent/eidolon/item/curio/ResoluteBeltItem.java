package elucent.eidolon.item.curio;

import java.util.Random;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.item.ItemBase;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;

public class ResoluteBeltItem extends ItemBase {
    UUID ATTR_ID = new UUID(3701779382882225399l, 5035874982077300549l);

    public ResoluteBeltItem(Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.addListener(ResoluteBeltItem::onHurt);
    }

    static Random random = new Random();

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity && CuriosApi.getCuriosHelper().findEquippedCurio(Registry.RESOLUTE_BELT.get(), event.getEntityLiving()).isPresent()) {
            LivingEntity entity = (LivingEntity)event.getSource().getEntity();
            Vec3 diff = event.getEntityLiving().position().subtract(entity.position()).multiply(1, 0, 1).normalize();
            entity.knockback(0.8f, diff.x, diff.z);
            if (!entity.level.isClientSide) entity.level.playSound(null, entity.blockPosition(), SoundEvents.IRON_GOLEM_HURT, SoundSource.PLAYERS, 1.0f, 1.9f + 0.2f * random.nextFloat());
        }
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag unused) {
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
