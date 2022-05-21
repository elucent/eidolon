package elucent.eidolon.particle;

import java.awt.Color;

import com.mojang.blaze3d.vertex.VertexConsumer;

import elucent.eidolon.ClientConfig;
import elucent.eidolon.ClientEvents;
import elucent.eidolon.util.RenderUtil;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class GlowingSlashParticle extends TextureSheetParticle {
	SlashParticleData data;
    float[] hsv1 = new float[3], hsv2 = new float[3];
    public GlowingSlashParticle(ClientLevel world, SlashParticleData data, double x, double y, double z, double vx, double vy, double vz) {
        super(world, x, y, z, vx, vy, vz);
        this.setPos(x, y, z);
        this.data = data;
        this.xd = vx;
        this.yd = vy;
        this.zd = vz;
        this.setLifetime(data.lifetime);
        Vec3 dir = Vec3.directionFromRotation(Mth.RAD_TO_DEG * -data.pitch, Mth.RAD_TO_DEG * -data.yaw);
        xd += dir.x * (0.025f + world.random.nextFloat() * 0.025f);
        yd += dir.y * (0.025f + world.random.nextFloat() * 0.025f);
        zd += dir.z * (0.025f + world.random.nextFloat() * 0.025f);
        Color.RGBtoHSB((int)(255 * Math.min(1.0f, data.r1)), (int)(255 * Math.min(1.0f, data.g1)), (int)(255 * Math.min(1.0f, data.b1)), hsv1);
        Color.RGBtoHSB((int)(255 * Math.min(1.0f, data.r2)), (int)(255 * Math.min(1.0f, data.g2)), (int)(255 * Math.min(1.0f, data.b2)), hsv2);
        updateTraits();
    }

    protected float getCoeff() {
        return (float)this.age / this.lifetime;
    }

    protected float getCoeff(float pticks) {
        return ((float)this.age + pticks) / this.lifetime;
    }

    protected float getSqInvCoeff(float pticks) {
        float inv = (this.lifetime - (float)this.age + pticks) / this.lifetime;
        return inv * inv;
    }

    protected void updateTraits() {
        float coeff = getCoeff();
        float h = Mth.rotLerp(coeff, 360 * hsv1[0], 360 * hsv2[0]) / 360;
        float s = Mth.lerp(coeff, hsv1[1], hsv2[1]);
        float v = Mth.lerp(coeff, hsv1[2], hsv2[2]);
        int packed = Color.HSBtoRGB(h, s, v);
        float r = FastColor.ARGB32.red(packed) / 255.0f;
        float g = FastColor.ARGB32.green(packed) / 255.0f;
        float b = FastColor.ARGB32.blue(packed) / 255.0f;
        setColor(r, g, b);
        setAlpha(Mth.lerp(coeff, data.a1, data.a2));
    }

    @Override
    public void tick() {
        updateTraits();
        super.tick();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return GlowParticleRenderType.INSTANCE;
    }
    
    private void decoVert(VertexConsumer vc, float u, float v, float alpha, int lmap) {
    	vc.uv(u, v).color(rCol, gCol, bCol, alpha).uv2(lmap).endVertex();
    }

    @Override
    public void render(VertexConsumer vc, Camera info, float pticks) {
    	VertexConsumer b = ClientConfig.BETTER_LAYERING.get() ? ClientEvents.getDelayedRender().getBuffer(RenderUtil.DELAYED_PARTICLE) : vc;

        float x = (float)(Mth.lerp((double)pticks, this.xo, this.x) - info.getPosition().x());
        float y = (float)(Mth.lerp((double)pticks, this.yo, this.y) - info.getPosition().y());
        float z = (float)(Mth.lerp((double)pticks, this.zo, this.z) - info.getPosition().z());
    	float sy = Mth.sin(data.yaw), cy = Mth.cos(data.yaw), sp = Mth.sin(data.pitch), cp = Mth.cos(data.pitch);
    	float ox = x - sy * cp * data.rad, oy = y - sp * data.rad, oz = z - cy * cp * data.rad;

    	float right = data.yaw + Mth.PI / 2, up = data.pitch + Mth.PI / 2;
    	float sr = Mth.sin(right), cr = Mth.cos(right), su = Mth.sin(up), cu = Mth.cos(up);
    	float r = data.rad;
    	float xax = r * sr * cp, xay = 0, xaz = r * cr * cp;
    	float yax = r * sy * cu, yay = r * su, yaz = r * cy * cu;
    	float zax = r * sy * cp, zay = r * sp, zaz = r * cy * cp;
    	float cro = Mth.cos(data.roll), sro = Mth.sin(data.roll);
    	float nxax = xax * cro - yax * sro;
    	float nxay = xay * cro - yay * sro;
    	float nxaz = xaz * cro - yaz * sro;
    	yax = xax * sro + yax * cro;
    	yay = xay * sro + yay * cro;
    	yaz = xaz * sro + yaz * cro;
    	xax = nxax;
    	xay = nxay;
    	xaz = nxaz;
        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();
        int lmap = getLightColor(pticks);
		float w = data.width / 2;
		
		float hlangle = -data.angle / 2 + getSqInvCoeff(pticks) * data.angle;
		float hlwidth = 4 + 4 * getSqInvCoeff(pticks);
    	for (float i = 0; i < 18; i ++) {
    		float c1 = i / 18, c2 = (i + 1) / 18;
    		float basea = this.alpha - data.highlight;
    		float angle1 = -data.angle / 2 + c1 * data.angle, angle2 = -data.angle / 2 + c2 * data.angle;
    		float al1 = Mth.sin(c1 * Mth.PI), al2 = Mth.sin(c2 * Mth.PI);
    		float dangle1 = Mth.clamp((angle1 - hlangle) / data.angle, -1 / hlwidth, 1 / hlwidth), dangle2 = Mth.clamp((angle2 - hlangle) / data.angle, -1 / hlwidth, 1 / hlwidth);
    		float hl1 = (Mth.cos(hlwidth * Mth.PI * dangle1) + 1) / 2, hl2 = (Mth.cos(hlwidth * Mth.PI * dangle2) + 1) / 2;
    		al1 = (1 - data.highlight) * al1 + data.highlight * hl1;
    		al2 = (1 - data.highlight) * al2 + data.highlight * hl2;
    		al1 *= alpha;
    		al2 *= alpha;
    		float w1 = w * (0.25f + 0.75f * Mth.sin(c1 * Mth.PI)), w2 = w * (0.25f + 0.75f * Mth.sin(c2 * Mth.PI));
    		w1 += data.highlight * 0.25f * w * hl1;
    		w2 += data.highlight * 0.25f * w * hl2;
    		
    		float sa1 = Mth.sin(angle1), ca1 = Mth.cos(angle1), sa2 = Mth.sin(angle2), ca2 = Mth.cos(angle2);
    		float dx1 = sa1 * xax + ca1 * zax, dy1 = sa1 * xay + ca1 * zay, dz1 = sa1 * xaz + ca1 * zaz;
    		float dx2 = sa2 * xax + ca2 * zax, dy2 = sa2 * xay + ca2 * zay, dz2 = sa2 * xaz + ca2 * zaz;
    		
    		// horiz
	        decoVert(b.vertex(ox + dx2 * r + dx2 * w2, oy + dy2 * r + dy2 * w2, oz + dz2 * r + dz2 * w2), u1, v1, al2, lmap);
	        decoVert(b.vertex(ox + dx2 * r + dx2 * -w2, oy + dy2 * r + dy2 * -w2, oz + dz2 * r + dz2 * -w2), u1, v0, al2, lmap);
	        decoVert(b.vertex(ox + dx1 * r + dx1 * -w1, oy + dy1 * r + dy1 * -w1, oz + dz1 * r + dz1 * -w1), u0, v0, al1, lmap);
	        decoVert(b.vertex(ox + dx1 * r + dx1 * w1, oy + dy1 * r + dy1 * w1, oz + dz1 * r + dz1 * w1), u0, v1, al1, lmap);
	        
	        // horiz back
	        decoVert(b.vertex(ox + dx2 * r + dx2 * -w2, oy + dy2 * r + dy2 * -w2, oz + dz2 * r + dz2 * -w2), u1, v1, al2, lmap);
	        decoVert(b.vertex(ox + dx2 * r + dx2 * w2, oy + dy2 * r + dy2 * w2, oz + dz2 * r + dz2 * w2), u1, v0, al2, lmap);
	        decoVert(b.vertex(ox + dx1 * r + dx1 * w1, oy + dy1 * r + dy1 * w1, oz + dz1 * r + dz1 * w1), u0, v0, al1, lmap);
	        decoVert(b.vertex(ox + dx1 * r + dx1 * -w1, oy + dy1 * r + dy1 * -w1, oz + dz1 * r + dz1 * -w1), u0, v1, al1, lmap);
	        
	        // vert
	        decoVert(b.vertex(ox + dx2 * r + yax * w2, oy + dy2 * r + yay * w2, oz + dz2 * r + yaz * w2), u1, v1, al2, lmap);
	        decoVert(b.vertex(ox + dx2 * r + yax * -w2, oy + dy2 * r + yay * -w2, oz + dz2 * r + yaz * -w2), u1, v0, al2, lmap);
	        decoVert(b.vertex(ox + dx1 * r + yax * -w1, oy + dy1 * r + yay * -w1, oz + dz1 * r + yaz * -w1), u0, v0, al1, lmap);
	        decoVert(b.vertex(ox + dx1 * r + yax * w1, oy + dy1 * r + yay * w1, oz + dz1 * r + yaz * w1), u0, v1, al1, lmap);
	        
	        // vert back
	        decoVert(b.vertex(ox + dx2 * r + yax * -w2, oy + dy2 * r + yay * -w2, oz + dz2 * r + yaz * -w2), u1, v1, al2, lmap);
	        decoVert(b.vertex(ox + dx2 * r + yax * w2, oy + dy2 * r + yay * w2, oz + dz2 * r + yaz * w2), u1, v0, al2, lmap);
	        decoVert(b.vertex(ox + dx1 * r + yax * w1, oy + dy1 * r + yay * w1, oz + dz1 * r + yaz * w1), u0, v0, al1, lmap);
	        decoVert(b.vertex(ox + dx1 * r + yax * -w1, oy + dy1 * r + yay * -w1, oz + dz1 * r + yaz * -w1), u0, v1, al1, lmap);
    	}
    }
}
