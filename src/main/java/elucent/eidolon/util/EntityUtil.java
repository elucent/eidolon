package elucent.eidolon.util;

import elucent.eidolon.Eidolon;
import elucent.eidolon.ForeignItems;
import elucent.eidolon.item.WarlockRobesItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;

public class EntityUtil {
    public static final String THRALL_KEY = Eidolon.MODID + ":thrall";

    public static void enthrall(LivingEntity caster, LivingEntity thrall) {
        thrall.getPersistentData().putUniqueId(THRALL_KEY, caster.getUniqueID());
    }

    public static boolean isEnthralled(LivingEntity entity) {
        return entity.getPersistentData().contains(THRALL_KEY);
    }

    public static boolean isEnthralledBy(LivingEntity entity, LivingEntity owner) {
        return entity != null && owner != null && isEnthralled(entity) && entity.getPersistentData().getUniqueId(THRALL_KEY).equals(owner.getUniqueID());
    }
    
    public static float getWardingMod(LivingEntity entity) {
    	float modifier = 1F;
    	final Item head = entity.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem();
    	final Item chest = entity.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem();
    	final Item legs = entity.getItemStackFromSlot(EquipmentSlotType.LEGS).getItem();
    	final Item feet = entity.getItemStackFromSlot(EquipmentSlotType.FEET).getItem();
    	
    	if (head instanceof WarlockRobesItem) {
    		modifier -= 0.12F;
    	}
    	
    	if (chest instanceof WarlockRobesItem) {
    		modifier -= 0.24F;
    	}
    	
    	if (feet instanceof WarlockRobesItem) {
    		modifier -= 0.08F;
    	}
    	
    	
    	return modifier;
    }
}
