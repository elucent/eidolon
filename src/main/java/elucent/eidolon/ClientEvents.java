package elucent.eidolon;

import com.mojang.blaze3d.vertex.BufferBuilder;
import elucent.eidolon.util.RenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class ClientEvents {
    @OnlyIn(Dist.CLIENT)
    static MultiBufferSource.BufferSource DELAYED_RENDER = null;

    @OnlyIn(Dist.CLIENT)
    public static MultiBufferSource.BufferSource getDelayedRender() {
        if (DELAYED_RENDER == null) {
            Map<RenderType, BufferBuilder> buffers = new HashMap<>();
            for (RenderType type : new RenderType[]{
                RenderUtil.DELAYED_PARTICLE,
                RenderUtil.GLOWING_PARTICLE,
                RenderUtil.GLOWING_BLOCK_PARTICLE,
                RenderUtil.GLOWING,
                RenderUtil.GLOWING_SPRITE}) {
                buffers.put(type, new BufferBuilder(type.bufferSize()));
            }
            DELAYED_RENDER = MultiBufferSource.immediateWithBuffers(buffers, new BufferBuilder(256));
        }
        return DELAYED_RENDER;
    }

    @OnlyIn(Dist.CLIENT)
    static float clientTicks = 0;

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onRenderLast(RenderLevelLastEvent event) {
        if (ClientConfig.BETTER_LAYERING.get()) {
            var poseStack = event.getPoseStack();

            poseStack.pushPose();
            getDelayedRender().endBatch(RenderUtil.DELAYED_PARTICLE);
            getDelayedRender().endBatch(RenderUtil.GLOWING_PARTICLE);
            getDelayedRender().endBatch(RenderUtil.GLOWING_BLOCK_PARTICLE);
            poseStack.popPose();

            getDelayedRender().endBatch(RenderUtil.GLOWING_SPRITE);
            getDelayedRender().endBatch(RenderUtil.GLOWING);
        }
        clientTicks += event.getPartialTick();
    }

    @OnlyIn(Dist.CLIENT)
    public static float getClientTicks() {
        return clientTicks;
    }
}
