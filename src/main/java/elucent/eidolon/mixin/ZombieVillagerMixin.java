package elucent.eidolon.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.server.level.ServerLevel;

@Mixin(ZombieVillager.class)
public interface ZombieVillagerMixin {
    @Invoker
    void callFinishConversion(ServerLevel world);
}
