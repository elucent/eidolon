package elucent.eidolon.event;

import net.minecraft.world.entity.Entity;

public class SpeedFactorEvent extends net.minecraftforge.eventbus.api.Event {
    Entity entity;
    float speedFactor;

    public SpeedFactorEvent(Entity entity, float speedFactor) {
        this.entity = entity;
        this.speedFactor = speedFactor;
    }

    public Entity getEntity() {
        return entity;
    }

    public float getSpeedFactor() {
        return speedFactor;
    }

    public void setSpeedFactor(float factor) {
        speedFactor = factor;
    }
}
