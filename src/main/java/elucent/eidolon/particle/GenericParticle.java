package elucent.eidolon.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

import java.awt.*;

public class GenericParticle extends SpriteTexturedParticle {
    GenericParticleData data;
    float[] hsv1 = new float[3], hsv2 = new float[3];
    public GenericParticle(ClientLevel world, GenericParticleData data, double x, double y, double z, double vx, double vy, double vz) {
        super(world, x, y, z, vx, vy, vz);
        this.setPos(x, y, z);
        this.data = data;
        this.xd = vx;
        this.yd = vy;
        this.zd = vz;
        this.setLifetime(data.lifetime);
        this.gravity = data.gravity ? 1 : 0;
        Color.RGBtoHSB((int)(255 * Math.min(1.0f, data.r1)), (int)(255 * Math.min(1.0f, data.g1)), (int)(255 * Math.min(1.0f, data.b1)), hsv1);
        Color.RGBtoHSB((int)(255 * Math.min(1.0f, data.r2)), (int)(255 * Math.min(1.0f, data.g2)), (int)(255 * Math.min(1.0f, data.b2)), hsv2);
        updateTraits();
    }

    protected float getCoeff() {
        return (float)this.age / this.lifetime;
    }

    protected void updateTraits() {
        float coeff = getCoeff();
        quadSize = Mth.lerp(coeff, data.scale1, data.scale2);
        float h = Mth.rotLerp(coeff, 360 * hsv1[0], 360 * hsv2[0]) / 360;
        float s = Mth.lerp(coeff, hsv1[1], hsv2[1]);
        float v = Mth.lerp(coeff, hsv1[2], hsv2[2]);
        int packed = Color.HSBtoRGB(h, s, v);
        float r = FastColor.PackedColor.red(packed) / 255.0f;
        float g = FastColor.PackedColor.green(packed) / 255.0f;
        float b = FastColor.PackedColor.blue(packed) / 255.0f;
        setColor(r, g, b);
        setAlpha(Mth.lerp(coeff, data.a1, data.a2));
        oRoll = roll;
        roll += data.spin;
    }

    @Override
    public void tick() {
        updateTraits();
        super.tick();
    }

    @Override
    public IParticleRenderType getRenderType() {
        return SpriteParticleRenderType.INSTANCE;
    }
}
