package elucent.eidolon.entity;

import java.util.Iterator;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import elucent.eidolon.ClientConfig;
import elucent.eidolon.ClientEvents;
import elucent.eidolon.Eidolon;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.spell.SignSequence;
import elucent.eidolon.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class ChantCasterRenderer extends EntityRenderer<ChantCasterEntity> {
    public ChantCasterRenderer(Context erm) {
        super(erm);
    }

    @Override
    public void render(ChantCasterEntity entity, float yaw, float pticks, PoseStack mStack, MultiBufferSource mb, int lmap) {
    	mStack.pushPose();
        VertexConsumer b = ClientEvents.getDelayedRender().getBuffer(RenderUtil.GLOWING_SPRITE);
        VertexConsumer sb = ClientEvents.getDelayedRender().getBuffer(RenderUtil.GLOWING_BLOCK_PARTICLE);
        
        Minecraft mc = Minecraft.getInstance();
        double ex = Mth.lerp(pticks, entity.xOld, entity.getX());
        double ey = Mth.lerp(pticks, entity.yOld, entity.getY());
        double ez = Mth.lerp(pticks, entity.zOld, entity.getZ());
        double px = Mth.lerp(pticks, mc.player.xOld, mc.player.getX());
        double py = Mth.lerp(pticks, mc.player.yOld, mc.player.getY());
        double pz = Mth.lerp(pticks, mc.player.zOld, mc.player.getZ());
//        mStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
//        mStack.translate(-px, -py, -pz);
        TextureAtlasSprite beam = mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation(Eidolon.MODID, "particle/beam"));
        TextureAtlasSprite ring = mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation(Eidolon.MODID, "particle/ring"));

        Vec3 look = entity.look;
        yaw = (float) Mth.atan2(look.x, look.z);
        Vec3 left = new Vec3(Math.cos(yaw), 0, -Math.sin(yaw));
        Vec3 up = left.cross(look);
        
        SignSequence spell = SignSequence.deserializeNbt(entity.getEntityData().get(entity.SIGNS));
        int i = 0;
        int sz = Math.max(0, spell.seq.size() - 1);
        float r = Mth.sqrt(sz) / 4f;
        if (sz > 0) r = Math.max(0.3f, r);
        Vec3 center = look.add(0, 0.5f, 0);
        int nreps = entity.deathTimer > 0 && entity.getEntityData().get(entity.SUCCEEDED) ? 3 : 1;
        float alphaMod = 1;
        if (entity.deathTimer > 0) {
        	alphaMod = entity.deathTimer / 20.0f;
        	alphaMod *= alphaMod;
        	if (!entity.getEntityData().get(entity.SUCCEEDED)) {
        		alphaMod *= alphaMod;
        		center = center.add(look.scale(1 - alphaMod));
    			r += 0.25f - 0.25f * alphaMod;
    	    }
    	}
        for (int k = 0; k < nreps; k ++) {
	        for (Sign s : spell.seq) {
	        	float a = -Mth.PI / 2 - i * 2 * Mth.PI / spell.seq.size();
	        	float sa = Mth.sin(a), ca = Mth.cos(a);
	        	
	        	Vec3 od = center.add(left.scale(r * ca)).add(up.scale(r * sa));
	        	Vec3 dxd = left.scale(0.175), dyd = up.scale(0.175);
	        	Vector3f o = new Vector3f((float)od.x, (float)od.y, (float)od.z);
	        	Vector3f dx = new Vector3f((float)dxd.x, (float)dxd.y, (float)dxd.z);
	        	Vector3f dy = new Vector3f((float)dyd.x, (float)dyd.y, (float)dyd.z);
	        	
	        	TextureAtlasSprite spr = mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(s.getSprite());
	        	
	        	float brightMod = Mth.clamp(Mth.sin(a + Mth.TWO_PI * entity.tickCount / 20), 0, 1);
	        	brightMod *= brightMod;
	        	brightMod = 0.6f + 0.4f * brightMod;
	        	
	        	for (int j = 0; j < 2; j ++) {
		        	sb.vertex(mStack.last().pose(), o.x() - dx.x() + dy.x(), o.y() - dx.y() + dy.y(), o.z() - dx.z() + dy.z()).uv(spr.getU1(), spr.getV1()).color(s.getRed(), s.getGreen(), s.getBlue(), brightMod * alphaMod).uv2(0).endVertex();
		        	sb.vertex(mStack.last().pose(), o.x() - dx.x() - dy.x(), o.y() - dx.y() - dy.y(), o.z() - dx.z() - dy.z()).uv(spr.getU1(), spr.getV0()).color(s.getRed(), s.getGreen(), s.getBlue(), brightMod * alphaMod).uv2(0).endVertex();
		        	sb.vertex(mStack.last().pose(), o.x() + dx.x() - dy.x(), o.y() + dx.y() - dy.y(), o.z() + dx.z() - dy.z()).uv(spr.getU0(), spr.getV0()).color(s.getRed(), s.getGreen(), s.getBlue(), brightMod * alphaMod).uv2(0).endVertex();
		        	sb.vertex(mStack.last().pose(), o.x() + dx.x() + dy.x(), o.y() + dx.y() + dy.y(), o.z() + dx.z() + dy.z()).uv(spr.getU0(), spr.getV1()).color(s.getRed(), s.getGreen(), s.getBlue(), brightMod * alphaMod).uv2(0).endVertex();
		
		        	sb.vertex(mStack.last().pose(), o.x() + dx.x() + dy.x(), o.y() + dx.y() + dy.y(), o.z() + dx.z() + dy.z()).uv(spr.getU1(), spr.getV1()).color(s.getRed(), s.getGreen(), s.getBlue(), brightMod * alphaMod).uv2(0).endVertex();
		        	sb.vertex(mStack.last().pose(), o.x() + dx.x() - dy.x(), o.y() + dx.y() - dy.y(), o.z() + dx.z() - dy.z()).uv(spr.getU1(), spr.getV0()).color(s.getRed(), s.getGreen(), s.getBlue(), brightMod * alphaMod).uv2(0).endVertex();
		        	sb.vertex(mStack.last().pose(), o.x() - dx.x() - dy.x(), o.y() - dx.y() - dy.y(), o.z() - dx.z() - dy.z()).uv(spr.getU0(), spr.getV0()).color(s.getRed(), s.getGreen(), s.getBlue(), brightMod * alphaMod).uv2(0).endVertex();
		        	sb.vertex(mStack.last().pose(), o.x() - dx.x() + dy.x(), o.y() - dx.y() + dy.y(), o.z() - dx.z() + dy.z()).uv(spr.getU0(), spr.getV1()).color(s.getRed(), s.getGreen(), s.getBlue(), brightMod * alphaMod).uv2(0).endVertex();
		        }
	        	
	        	dx.mul(1.75f);
	        	dy.mul(1.75f);
	        	sb.vertex(mStack.last().pose(), o.x() - dx.x() + dy.x(), o.y() - dx.y() + dy.y(), o.z() - dx.z() + dy.z()).uv(ring.getU1(), ring.getV1()).color(s.getRed(), s.getGreen(), s.getBlue(), alphaMod * 0.5f).uv2(0).endVertex();
	        	sb.vertex(mStack.last().pose(), o.x() - dx.x() - dy.x(), o.y() - dx.y() - dy.y(), o.z() - dx.z() - dy.z()).uv(ring.getU1(), ring.getV0()).color(s.getRed(), s.getGreen(), s.getBlue(), alphaMod * 0.5f).uv2(0).endVertex();
	        	sb.vertex(mStack.last().pose(), o.x() + dx.x() - dy.x(), o.y() + dx.y() - dy.y(), o.z() + dx.z() - dy.z()).uv(ring.getU0(), ring.getV0()).color(s.getRed(), s.getGreen(), s.getBlue(), alphaMod * 0.5f).uv2(0).endVertex();
	        	sb.vertex(mStack.last().pose(), o.x() + dx.x() + dy.x(), o.y() + dx.y() + dy.y(), o.z() + dx.z() + dy.z()).uv(ring.getU0(), ring.getV1()).color(s.getRed(), s.getGreen(), s.getBlue(), alphaMod * 0.5f).uv2(0).endVertex();
	        	
	        	sb.vertex(mStack.last().pose(), o.x() + dx.x() + dy.x(), o.y() + dx.y() + dy.y(), o.z() + dx.z() + dy.z()).uv(ring.getU1(), ring.getV1()).color(s.getRed(), s.getGreen(), s.getBlue(), alphaMod * 0.5f).uv2(0).endVertex();
	        	sb.vertex(mStack.last().pose(), o.x() + dx.x() - dy.x(), o.y() + dx.y() - dy.y(), o.z() + dx.z() - dy.z()).uv(ring.getU1(), ring.getV0()).color(s.getRed(), s.getGreen(), s.getBlue(), alphaMod * 0.5f).uv2(0).endVertex();
	        	sb.vertex(mStack.last().pose(), o.x() - dx.x() - dy.x(), o.y() - dx.y() - dy.y(), o.z() - dx.z() - dy.z()).uv(ring.getU0(), ring.getV0()).color(s.getRed(), s.getGreen(), s.getBlue(), alphaMod * 0.5f).uv2(0).endVertex();
	        	sb.vertex(mStack.last().pose(), o.x() - dx.x() + dy.x(), o.y() - dx.y() + dy.y(), o.z() - dx.z() + dy.z()).uv(ring.getU0(), ring.getV1()).color(s.getRed(), s.getGreen(), s.getBlue(), alphaMod * 0.5f).uv2(0).endVertex();
	        	
	        	i ++;
	        }
	        i = 0;
	        if (!spell.seq.isEmpty()) {
	        	Iterator<Sign> iter = spell.seq.iterator();
	        	Sign cur = null, next = spell.seq.getLast();
	        	float rr = r + 0.1375f;
	        	float rr2 = rr + 0.2f;
	        	float rs = r - 0.3f;
	        	float rs2 = rs + 0.2f;
	        	int steps = Math.max(24, spell.seq.size() * 4);
	        	if (steps % spell.seq.size() != 0) steps += spell.seq.size() - steps % spell.seq.size();
	        	int periodicity = Math.max(4, steps / spell.seq.size());
		        for (i = 0; i < steps; i ++) {
		        	if (i % periodicity == 0) {
		        		cur = next;
		        		next = iter.next();
		        		if (!iter.hasNext()) iter = spell.seq.iterator();
		        	}
		        	float a1 = -Mth.PI / 2 - (i - periodicity) * Mth.TWO_PI / steps, a2 = -Mth.PI / 2 - (i - periodicity + 1) * Mth.TWO_PI / steps;
		        	float sa1 = Mth.sin(a1), ca1 = Mth.cos(a1);
		        	float sa2 = Mth.sin(a2), ca2 = Mth.cos(a2);
		        	
		        	float r1 = Mth.lerp((i % periodicity) / (float)periodicity, cur.getRed(), next.getRed());
		        	float r2 = Mth.lerp((i % periodicity + 1) / (float)periodicity, cur.getRed(), next.getRed());
		        	float g1 = Mth.lerp((i % periodicity) / (float)periodicity, cur.getGreen(), next.getGreen());
		        	float g2 = Mth.lerp((i % periodicity + 1) / (float)periodicity, cur.getGreen(), next.getGreen());
		        	float b1 = Mth.lerp((i % periodicity) / (float)periodicity, cur.getBlue(), next.getBlue());
		        	float b2 = Mth.lerp((i % periodicity + 1) / (float)periodicity, cur.getBlue(), next.getBlue());
		        	
		        	Vec3 id1 = center.add(left.scale(rr * ca1)).add(up.scale(rr * sa1));
		        	Vec3 id2 = center.add(left.scale(rr * ca2)).add(up.scale(rr * sa2));
		        	Vec3 od1 = center.add(left.scale(rr2 * ca1)).add(up.scale(rr2 * sa1));
		        	Vec3 od2 = center.add(left.scale(rr2 * ca2)).add(up.scale(rr2 * sa2));
		        	Vector3f i1 = new Vector3f((float)id1.x, (float)id1.y, (float)id1.z);
		        	Vector3f i2 = new Vector3f((float)id2.x, (float)id2.y, (float)id2.z);
		        	Vector3f o1 = new Vector3f((float)od1.x, (float)od1.y, (float)od1.z);
		        	Vector3f o2 = new Vector3f((float)od2.x, (float)od2.y, (float)od2.z);
		        	
		        	sb.vertex(mStack.last().pose(), o1.x(), o1.y(), o1.z()).uv(beam.getU1(), beam.getV1()).color(r1, g1, b1, alphaMod * 0.5f).uv2(0).endVertex();
		        	sb.vertex(mStack.last().pose(), o2.x(), o2.y(), o2.z()).uv(beam.getU0(), beam.getV1()).color(r2, g2, b2, alphaMod * 0.5f).uv2(0).endVertex();
		        	sb.vertex(mStack.last().pose(), i2.x(), i2.y(), i2.z()).uv(beam.getU0(), beam.getV0()).color(r2, g2, b2, alphaMod * 0.5f).uv2(0).endVertex();
		        	sb.vertex(mStack.last().pose(), i1.x(), i1.y(), i1.z()).uv(beam.getU1(), beam.getV0()).color(r1, g1, b1, alphaMod * 0.5f).uv2(0).endVertex();
		        	
		        	sb.vertex(mStack.last().pose(), o2.x(), o2.y(), o2.z()).uv(beam.getU1(), beam.getV1()).color(r2, g2, b2, alphaMod * 0.5f).uv2(0).endVertex();
		        	sb.vertex(mStack.last().pose(), o1.x(), o1.y(), o1.z()).uv(beam.getU0(), beam.getV1()).color(r1, g1, b1, alphaMod * 0.5f).uv2(0).endVertex();
		        	sb.vertex(mStack.last().pose(), i1.x(), i1.y(), i1.z()).uv(beam.getU0(), beam.getV0()).color(r1, g1, b1, alphaMod * 0.5f).uv2(0).endVertex();
		        	sb.vertex(mStack.last().pose(), i2.x(), i2.y(), i2.z()).uv(beam.getU1(), beam.getV0()).color(r2, g2, b2, alphaMod * 0.5f).uv2(0).endVertex();
		        	
		        	id1 = center.add(left.scale(rs * ca1)).add(up.scale(rs * sa1));
		        	id2 = center.add(left.scale(rs * ca2)).add(up.scale(rs * sa2));
		        	od1 = center.add(left.scale(rs2 * ca1)).add(up.scale(rs2 * sa1));
		        	od2 = center.add(left.scale(rs2 * ca2)).add(up.scale(rs2 * sa2));
		        	i1 = new Vector3f((float)id1.x, (float)id1.y, (float)id1.z);
		        	i2 = new Vector3f((float)id2.x, (float)id2.y, (float)id2.z);
		        	o1 = new Vector3f((float)od1.x, (float)od1.y, (float)od1.z);
		        	o2 = new Vector3f((float)od2.x, (float)od2.y, (float)od2.z);
		        	
		        	sb.vertex(mStack.last().pose(), o1.x(), o1.y(), o1.z()).uv(beam.getU1(), beam.getV1()).color(r1, g1, b1, alphaMod * 0.5f).uv2(0).endVertex();
		        	sb.vertex(mStack.last().pose(), o2.x(), o2.y(), o2.z()).uv(beam.getU0(), beam.getV1()).color(r2, g2, b2, alphaMod * 0.5f).uv2(0).endVertex();
		        	sb.vertex(mStack.last().pose(), i2.x(), i2.y(), i2.z()).uv(beam.getU0(), beam.getV0()).color(r2, g2, b2, alphaMod * 0.5f).uv2(0).endVertex();
		        	sb.vertex(mStack.last().pose(), i1.x(), i1.y(), i1.z()).uv(beam.getU1(), beam.getV0()).color(r1, g1, b1, alphaMod * 0.5f).uv2(0).endVertex();
		        	
		        	sb.vertex(mStack.last().pose(), o2.x(), o2.y(), o2.z()).uv(beam.getU1(), beam.getV1()).color(r2, g2, b2, alphaMod * 0.5f).uv2(0).endVertex();
		        	sb.vertex(mStack.last().pose(), o1.x(), o1.y(), o1.z()).uv(beam.getU0(), beam.getV1()).color(r1, g1, b1, alphaMod * 0.5f).uv2(0).endVertex();
		        	sb.vertex(mStack.last().pose(), i1.x(), i1.y(), i1.z()).uv(beam.getU0(), beam.getV0()).color(r1, g1, b1, alphaMod * 0.5f).uv2(0).endVertex();
		        	sb.vertex(mStack.last().pose(), i2.x(), i2.y(), i2.z()).uv(beam.getU1(), beam.getV0()).color(r2, g2, b2, alphaMod * 0.5f).uv2(0).endVertex();
		        }
	        }
        }
        mStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(ChantCasterEntity entity) {
        return new ResourceLocation(Eidolon.MODID, "textures/particle/beam.png");
    }
}
