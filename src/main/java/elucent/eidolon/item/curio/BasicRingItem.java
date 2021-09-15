package elucent.eidolon.item.curio;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import elucent.eidolon.Eidolon;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.UUID;

public class BasicRingItem extends Item {
    UUID ATTR_ID = new UUID(7207179027447911419l, 1628308750126455317l);
    public BasicRingItem(Properties properties) {
        super(properties);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT unused) {
        return new EidolonCurio(stack) {
            @Override
            public Multimap<Attribute, AttributeModifier> getAttributeModifiers(String identifier) {
                Multimap<Attribute, AttributeModifier> map = HashMultimap.create();
                map.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(ATTR_ID, Eidolon.MODID + ":basic_ring", 0.5f, AttributeModifier.Operation.ADDITION));
                return map;
            }

            @Override
            public boolean canRightClickEquip() {
                return true;
            }
        };
    }
}
