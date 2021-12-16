package elucent.eidolon.reagent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class Reagent {
    ResourceLocation name, texture;
    boolean gas;

    public Reagent(ResourceLocation name, ResourceLocation texture, boolean isGas) {
        this.name = name;
        this.texture = texture;
        this.gas = isGas;
    }

    public ResourceLocation getRegistryName() {
        return name;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    @OnlyIn(Dist.CLIENT)
    public TextureAtlasSprite getSprite() {
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);
    }

    public boolean isGas() {
        return gas;
    }

    public abstract void worldEffect(Level world, BlockPos pos, int amount);
}
