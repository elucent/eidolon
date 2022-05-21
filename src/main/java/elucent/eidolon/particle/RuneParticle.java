package elucent.eidolon.particle;

import java.awt.Color;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import elucent.eidolon.ClientConfig;
import elucent.eidolon.ClientEvents;
import elucent.eidolon.Eidolon;
import elucent.eidolon.spell.Rune;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.util.RenderUtil;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class RuneParticle extends TextureSheetParticle {
    Rune rune;
    float[] hsv1 = new float[3], hsv2 = new float[3];
    public RuneParticle(ClientLevel world, RuneParticleData data, double x, double y, double z, double vx, double vy, double vz) {
        super(world, x, y, z, vx, vy, vz);
        this.setPos(x, y, z);
        this.rune = data.rune;
        this.xd = vx;
        this.yd = vy;
        this.zd = vz;
        this.setLifetime(20);
        this.gravity = -0.05f;
        Color.RGBtoHSB((int)(255 * Math.min(1.0f, data.r1)), (int)(255 * Math.min(1.0f, data.g1)), (int)(255 * Math.min(1.0f, data.b1)), hsv1);
        Color.RGBtoHSB((int)(255 * Math.min(1.0f, data.r2)), (int)(255 * Math.min(1.0f, data.g2)), (int)(255 * Math.min(1.0f, data.b2)), hsv2);
        if (hsv1[0] < 0.01) hsv1[0] = hsv2[0];
        if (hsv1[1] < 0.01) hsv1[1] = 0.01f;
        updateTraits();
    }

    protected float getCoeff() {
        float t = ((float)this.age + Minecraft.getInstance().getDeltaFrameTime()) / this.lifetime;
        t = Mth.clamp(t, 0.001f, 0.999f);
        float ic = (1 - t) * (1 - t);
        return 1 - (Mth.sin(Mth.PI * 4 * t) * ic * 0.5f - 0.7f * t + 0.7f);
    }

    protected void updateTraits() {
        float coeff = getCoeff();
        float t = ((float)this.age + Minecraft.getInstance().getDeltaFrameTime()) / this.lifetime;
        t = Mth.clamp(t, 0.001f, 0.999f);
        float ic = (1 - t) * (1 - t);
        quadSize = Mth.lerp(coeff, 0.125f, 0.0625f);
        this.oRoll = roll;
        this.roll = Mth.PI * Mth.sin(Mth.cos(Mth.PI * 4 * t) * ic) / 8;
        setAlpha(Mth.lerp(coeff * coeff, 0.5f, 0));
        
        float h = Mth.rotLerp(coeff, 360 * hsv1[0], 360 * hsv2[0]) / 360;
        float s = Mth.lerp(coeff, hsv1[1], hsv2[1]);
        float v = Mth.lerp(coeff, hsv1[2], hsv2[2]);
        int packed = Color.HSBtoRGB(h, s, v);
        float r = FastColor.ARGB32.red(packed) / 255.0f;
        float g = FastColor.ARGB32.green(packed) / 255.0f;
        float b = FastColor.ARGB32.blue(packed) / 255.0f;
        setColor(r, g, b);
    }

    @Override
    public void tick() {
        updateTraits();
        super.tick();
        xd *= 0.98;
        yd *= 0.98;
        zd *= 0.98;
    }

    @Override
    public void render(VertexConsumer b, Camera info, float pticks) {
    	b = ClientConfig.BETTER_LAYERING.get() ? ClientEvents.getDelayedRender().getBuffer(RenderUtil.GLOWING_BLOCK_PARTICLE) : b;
    	Vec3 vec3 = info.getPosition();
        float f = (float)(Mth.lerp((double)pticks, this.xo, this.x) - vec3.x());
        float f1 = (float)(Mth.lerp((double)pticks, this.yo, this.y) - vec3.y());
        float f2 = (float)(Mth.lerp((double)pticks, this.zo, this.z) - vec3.z());
        Quaternion quaternion;
        if (this.roll == 0.0F) {
           quaternion = info.rotation();
        } else {
           quaternion = new Quaternion(info.rotation());
           float f3 = Mth.lerp(pticks, this.oRoll, this.roll);
           quaternion.mul(Vector3f.ZP.rotation(f3));
        }

        Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
        vector3f1.transform(quaternion);
        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f4 = this.getQuadSize(pticks);

        for(int i = 0; i < 4; ++i) {
           Vector3f vector3f = avector3f[i];
           vector3f.transform(quaternion);
           vector3f.mul(f4);
           vector3f.add(f, f1, f2);
        }

        float f7 = this.getU0();
        float f8 = this.getU1();
        float f5 = this.getV0();
        float f6 = this.getV1();
        int j = this.getLightColor(pticks);
        
        
        Vector3f offX = avector3f[0].copy(), offY = avector3f[1].copy();
        offX.sub(avector3f[2]);
        offX.mul(0.5f);
        offY.sub(avector3f[3]);
        offY.mul(0.5f);

        TextureAtlasSprite aura = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation(Eidolon.MODID, "particle/aura"));
        
        for (int i = 0; i < 1; i ++) {
//        	float a = Mth.PI * i + Mth.PI * 2 * (age + pticks) / lifetime;
//        	float s = Mth.sin(a), c = Mth.cos(a);
//        	float dx = offX.x() * c + offY.x() * s;
//        	float dy = offX.y() * c + offY.y() * s;
//        	float dz = offX.z() * c + offY.z() * s;
//        	dx *= 0.25f;
//        	dy *= 0.25f;
//        	dz *= 0.25f;
            b.vertex((double)avector3f[0].x() + offX.x(), (double)avector3f[0].y() + offX.y(), (double)avector3f[0].z() + offX.z()).uv(aura.getU1(), aura.getV1()).color(this.rCol, this.gCol, this.bCol, this.alpha * 0.25f).uv2(j).endVertex();
            b.vertex((double)avector3f[1].x() + offY.x(), (double)avector3f[1].y() + offY.y(), (double)avector3f[1].z() + offY.z()).uv(aura.getU1(), aura.getV0()).color(this.rCol, this.gCol, this.bCol, this.alpha * 0.25f).uv2(j).endVertex();
            b.vertex((double)avector3f[2].x() - offX.x(), (double)avector3f[2].y() - offX.y(), (double)avector3f[2].z() - offX.z()).uv(aura.getU0(), aura.getV0()).color(this.rCol, this.gCol, this.bCol, this.alpha * 0.25f).uv2(j).endVertex();
            b.vertex((double)avector3f[3].x() - offY.x(), (double)avector3f[3].y() - offY.y(), (double)avector3f[3].z() - offY.z()).uv(aura.getU0(), aura.getV1()).color(this.rCol, this.gCol, this.bCol, this.alpha * 0.25f).uv2(j).endVertex();
        }
        
        b.vertex((double)avector3f[0].x(), (double)avector3f[0].y(), (double)avector3f[0].z()).uv(f8, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        b.vertex((double)avector3f[1].x(), (double)avector3f[1].y(), (double)avector3f[1].z()).uv(f8, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        b.vertex((double)avector3f[2].x(), (double)avector3f[2].y(), (double)avector3f[2].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        b.vertex((double)avector3f[3].x(), (double)avector3f[3].y(), (double)avector3f[3].z()).uv(f7, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return RuneParticleRenderType.INSTANCE;
    }
}
