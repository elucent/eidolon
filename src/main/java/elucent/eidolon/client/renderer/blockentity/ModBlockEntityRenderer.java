package elucent.eidolon.client.renderer.blockentity;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * @author DustW
 **/
public abstract class ModBlockEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
    protected BlockEntityRenderDispatcher rendererDispatcherIn;
    public ModBlockEntityRenderer(BlockEntityRenderDispatcher rendererDispatcherIn) {
        this.rendererDispatcherIn = rendererDispatcherIn;
    }
}
