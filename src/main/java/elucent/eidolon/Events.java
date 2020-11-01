package elucent.eidolon;

import com.mojang.blaze3d.systems.RenderSystem;
import elucent.eidolon.util.RenderUtil;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class Events {
    @SubscribeEvent
    public void onDeath(LivingDropsEvent event) {
        if (event.getSource().getDamageType() == Registry.RITUAL_DAMAGE.getDamageType())
            event.getDrops().clear();
    }

    @SubscribeEvent
    public void registerSpawns(BiomeLoadingEvent ev) {
        if (ev.getCategory() != Biome.Category.OCEAN && ev.getCategory() != Biome.Category.NETHER && ev.getCategory() != Biome.Category.THEEND) {
            ev.getSpawns().withSpawner(EntityClassification.MONSTER,
                new MobSpawnInfo.Spawners(Registry.WRAITH.get(), Config.WRAITH_SPAWN_WEIGHT.get(), 1, 2));
            ev.getSpawns().withSpawner(EntityClassification.MONSTER,
                new MobSpawnInfo.Spawners(Registry.ZOMBIE_BRUTE.get(), Config.ZOMBIE_BRUTE_SPAWN_WEIGHT.get(), 1, 2));
        }
    }

    @OnlyIn(Dist.CLIENT)
    static IRenderTypeBuffer.Impl DELAYED_RENDER = null;

    @OnlyIn(Dist.CLIENT)
    public static IRenderTypeBuffer.Impl getDelayedRender() {
        if (DELAYED_RENDER == null) {
            Map<RenderType, BufferBuilder> buffers = new HashMap<>();
            for (RenderType type : new RenderType[]{
                RenderUtil.DELAYED_PARTICLE,
                RenderUtil.GLOWING_PARTICLE,
                RenderUtil.GLOWING,
                RenderUtil.GLOWING_SPRITE}) {
                buffers.put(type, new BufferBuilder(type.getBufferSize()));
            }
            DELAYED_RENDER = IRenderTypeBuffer.getImpl(buffers, new BufferBuilder(256));
        }
        return DELAYED_RENDER;
    }

    @OnlyIn(Dist.CLIENT)
    static float clientTicks = 0;

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onRenderLast(RenderWorldLastEvent event) {
        if (ClientConfig.BETTER_LAYERING.get()) {
            RenderSystem.pushMatrix(); // this feels...cheaty
            RenderSystem.multMatrix(event.getMatrixStack().getLast().getMatrix());
            getDelayedRender().finish(RenderUtil.DELAYED_PARTICLE);
            getDelayedRender().finish(RenderUtil.GLOWING_PARTICLE);
            RenderSystem.popMatrix();

            getDelayedRender().finish(RenderUtil.GLOWING_SPRITE);
            getDelayedRender().finish(RenderUtil.GLOWING);
        }
        clientTicks += event.getPartialTicks();
    }

    @OnlyIn(Dist.CLIENT)
    public static float getClientTicks() {
        return clientTicks;
    }
}
