package elucent.eidolon.spell;

import net.minecraft.util.ResourceLocation;

import java.util.function.Consumer;

public class AltarEntry {
    double capacity = 0, power = 0;
    ResourceLocation key = null;
    Consumer<AltarInfo> callback = null;

    AltarEntry(ResourceLocation key) {
        this.key = key;
    }

    AltarEntry setCapacity(double capacity) {
        this.capacity = capacity;
        return this;
    }

    AltarEntry setPower(double power) {
        this.power = power;
        return this;
    }

    AltarEntry setCallback(Consumer<AltarInfo> callback) {
        this.callback = callback;
        return this;
    }

    AltarEntry setKey(ResourceLocation key) {
        this.key = key;
        return this;
    }

    void apply(AltarInfo info) {
        info.attributes.computeIfAbsent(key, (k) -> new AltarInfo.AltarAttributes());
        if (capacity > 0) info.increaseCapacity(key, capacity);
        if (power > 0) info.increasePower(key, power);
        if (callback != null) callback.accept(info);
    }
}
