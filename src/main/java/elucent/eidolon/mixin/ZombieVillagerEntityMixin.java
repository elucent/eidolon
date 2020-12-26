package elucent.eidolon.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraft.world.server.ServerWorld;

@Mixin(ZombieVillagerEntity.class)
public interface ZombieVillagerEntityMixin {
    @Invoker
    void callCureZombie(ServerWorld world);
}
