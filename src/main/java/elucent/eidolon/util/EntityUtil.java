package elucent.eidolon.util;

import elucent.eidolon.Eidolon;
import net.minecraft.entity.LivingEntity;

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
}
