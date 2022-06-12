package elucent.eidolon.recipe.recipes.object;

import com.google.gson.annotations.Expose;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

/**
 * @author DustW
 **/
public class EffectStack {
    @Expose
    public String id;
    @Expose
    public int lvl;
    @Expose
    public int duration;

    private Supplier<MobEffectInstance> cache;

    public MobEffectInstance get() {
        if (cache == null) {
            var effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(id));

            if (effect != null) {
                cache = () -> new MobEffectInstance(effect, duration, lvl - 1);
            }
        }

        return cache.get();
    }
}
