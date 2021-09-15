package elucent.eidolon.deity;

import elucent.eidolon.capability.IReputation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public abstract class Deity {
    ResourceLocation id;
    int red, green, blue;

    public Deity(ResourceLocation id, int red, int green, int blue) {
        this.id = id;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public float getRed() {
        return red / 255.0f;
    }

    public float getGreen() {
        return green / 255.0f;
    }

    public float getBlue() {
        return blue / 255.0f;
    }

    public ResourceLocation getId() {
        return id;
    }

    public abstract void onReputationUnlock(PlayerEntity player, IReputation rep, ResourceLocation lock);
    public abstract void onReputationChange(PlayerEntity player, IReputation rep, double prev, double current);
}
