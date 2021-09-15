package elucent.eidolon.spell;

import elucent.eidolon.util.ColorUtil;
import net.minecraft.util.ResourceLocation;

public class Sign {
    ResourceLocation key, sprite;
    int color;
    public Sign(ResourceLocation key, ResourceLocation sprite, int color) {
        this.key = key;
        this.sprite = sprite;
        this.color = color;
    }

    public ResourceLocation getRegistryName() {
        return key;
    }

    public ResourceLocation getSprite() {
        return sprite;
    }

    public int getColor() {
        return color;
    }

    public float getRed() {
        return ColorUtil.getRed(color) / 255.0f;
    }

    public float getGreen() {
        return ColorUtil.getGreen(color) / 255.0f;
    }

    public float getBlue() {
        return ColorUtil.getBlue(color) / 255.0f;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Sign && ((Sign)other).key.equals(key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
