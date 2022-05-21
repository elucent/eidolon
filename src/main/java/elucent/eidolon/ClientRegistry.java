package elucent.eidolon;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Random;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;

import elucent.eidolon.capability.ISoul;
import elucent.eidolon.entity.AngelArrowRenderer;
import elucent.eidolon.entity.ChantCasterRenderer;
import elucent.eidolon.entity.NecromancerModel;
import elucent.eidolon.entity.NecromancerRenderer;
import elucent.eidolon.entity.RavenModel;
import elucent.eidolon.entity.RavenRenderer;
import elucent.eidolon.entity.SlimySlugModel;
import elucent.eidolon.entity.SlimySlugRenderer;
import elucent.eidolon.entity.WraithModel;
import elucent.eidolon.entity.WraithRenderer;
import elucent.eidolon.entity.ZombieBruteModel;
import elucent.eidolon.entity.ZombieBruteRenderer;
import elucent.eidolon.item.IManaRelatedItem;
import elucent.eidolon.item.curio.RavenCloakRenderer;
import elucent.eidolon.item.model.BonelordArmorModel;
import elucent.eidolon.item.model.RavenCloakModel;
import elucent.eidolon.item.model.SilverArmorModel;
import elucent.eidolon.item.model.TopHatModel;
import elucent.eidolon.item.model.WarlockArmorModel;
import elucent.eidolon.reagent.Reagent;
import elucent.eidolon.reagent.ReagentRegistry;
import elucent.eidolon.ritual.AbsorptionRitual;
import elucent.eidolon.ritual.AllureRitual;
import elucent.eidolon.ritual.CrystalRitual;
import elucent.eidolon.ritual.DaylightRitual;
import elucent.eidolon.ritual.DeceitRitual;
import elucent.eidolon.ritual.MoonlightRitual;
import elucent.eidolon.ritual.PurifyRitual;
import elucent.eidolon.ritual.RechargingRitual;
import elucent.eidolon.ritual.RepellingRitual;
import elucent.eidolon.ritual.SanguineRitual;
import elucent.eidolon.ritual.SummonRitual;
import elucent.eidolon.spell.Rune;
import elucent.eidolon.spell.Runes;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.spell.Signs;
import elucent.eidolon.tile.CrucibleTileRenderer;
import elucent.eidolon.tile.SoulEnchanterTileRenderer;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

public class ClientRegistry {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event) {
        event.addSprite(AbsorptionRitual.SYMBOL);
        event.addSprite(CrystalRitual.SYMBOL);
        event.addSprite(SummonRitual.SYMBOL);
        event.addSprite(DeceitRitual.SYMBOL);
        event.addSprite(AllureRitual.SYMBOL);
        event.addSprite(DaylightRitual.SYMBOL);
        event.addSprite(MoonlightRitual.SYMBOL);
        event.addSprite(PurifyRitual.SYMBOL);
        event.addSprite(RepellingRitual.SYMBOL);
        event.addSprite(SanguineRitual.SYMBOL);
        event.addSprite(RechargingRitual.SYMBOL);
        event.addSprite(SoulEnchanterTileRenderer.BOOK_TEXTURE);

        event.addSprite(new ResourceLocation(Eidolon.MODID, "particle/aura"));
        event.addSprite(new ResourceLocation(Eidolon.MODID, "particle/beam"));
        event.addSprite(new ResourceLocation(Eidolon.MODID, "particle/ring"));
        for (Reagent r : ReagentRegistry.getReagents()) event.addSprite(r.getTexture());
        for (Sign s : Signs.getSigns()) event.addSprite(s.getSprite());
        for (Rune r : Runes.getRunes()) event.addSprite(r.getSprite());
    }
    
    public static ModelLayerLocation 
    	SILVER_ARMOR_LAYER = new ModelLayerLocation(new ResourceLocation(Eidolon.MODID, "silver_armor"), "main"),
    	WARLOCK_ARMOR_LAYER = new ModelLayerLocation(new ResourceLocation(Eidolon.MODID, "warlock_armor"), "main"),
    	BONELORD_ARMOR_LAYER = new ModelLayerLocation(new ResourceLocation(Eidolon.MODID, "bonelord_armor"), "main"),
    	TOP_HAT_LAYER = new ModelLayerLocation(new ResourceLocation(Eidolon.MODID, "top_hat"), "main"),
    	RAVEN_LAYER = new ModelLayerLocation(new ResourceLocation(Eidolon.MODID, "raven"), "main"),
    	NECROMANCER_LAYER = new ModelLayerLocation(new ResourceLocation(Eidolon.MODID, "necromancer"), "main"),
    	WRAITH_LAYER = new ModelLayerLocation(new ResourceLocation(Eidolon.MODID, "wraith"), "main"),
    	ZOMBIE_BRUTE_LAYER = new ModelLayerLocation(new ResourceLocation(Eidolon.MODID, "zombie_brute"), "main"),
    	SLUG_LAYER = new ModelLayerLocation(new ResourceLocation(Eidolon.MODID, "slimy_slug"), "main"),
    	CRUCIBLE_STIRRER_LAYER = new ModelLayerLocation(new ResourceLocation(Eidolon.MODID, "crucible_stirrer"), "main"),
    	RAVEN_CLOAK_LAYER = new ModelLayerLocation(new ResourceLocation(Eidolon.MODID, "raven_cloak"), "main");

    public static WarlockArmorModel WARLOCK_ARMOR_MODEL = null;
    public static BonelordArmorModel BONELORD_ARMOR_MODEL = null;
    public static TopHatModel TOP_HAT_MODEL = null;
    public static SilverArmorModel SILVER_ARMOR_MODEL = null;
    public static ZombieBruteModel ZOMBIE_BRUTE_MODEL = null;
    public static WraithModel WRAITH_MODEL = null;
    public static RavenModel RAVEN_MODEL = null;
    public static NecromancerModel NECROMANCER_MODEL = null;
    public static SlimySlugModel SLUG_MODEL = null;

    @SubscribeEvent
    public void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
    	event.registerLayerDefinition(WARLOCK_ARMOR_LAYER, WarlockArmorModel::createBodyLayer);
    	event.registerLayerDefinition(BONELORD_ARMOR_LAYER, BonelordArmorModel::createBodyLayer);
    	event.registerLayerDefinition(TOP_HAT_LAYER, TopHatModel::createBodyLayer);
    	event.registerLayerDefinition(SILVER_ARMOR_LAYER, SilverArmorModel::createBodyLayer);
    	event.registerLayerDefinition(RAVEN_CLOAK_LAYER, RavenCloakModel::createBodyLayer);

    	event.registerLayerDefinition(RAVEN_LAYER, RavenModel::createBodyLayer);
    	event.registerLayerDefinition(ZOMBIE_BRUTE_LAYER, ZombieBruteModel::createBodyLayer);
    	event.registerLayerDefinition(WRAITH_LAYER, WraithModel::createBodyLayer);
    	event.registerLayerDefinition(NECROMANCER_LAYER, NecromancerModel::createBodyLayer);
    	event.registerLayerDefinition(SLUG_LAYER, SlimySlugModel::createBodyLayer);

    	event.registerLayerDefinition(CRUCIBLE_STIRRER_LAYER, CrucibleTileRenderer::createModelLayer);
    }

    @SubscribeEvent
    public void onRegisterLayers(EntityRenderersEvent.AddLayers event) {
    	WARLOCK_ARMOR_MODEL = new WarlockArmorModel(event.getEntityModels().bakeLayer(WARLOCK_ARMOR_LAYER));
    	BONELORD_ARMOR_MODEL = new BonelordArmorModel(event.getEntityModels().bakeLayer(BONELORD_ARMOR_LAYER));
    	TOP_HAT_MODEL = new TopHatModel(event.getEntityModels().bakeLayer(TOP_HAT_LAYER));
    	SILVER_ARMOR_MODEL = new SilverArmorModel(event.getEntityModels().bakeLayer(SILVER_ARMOR_LAYER));
    	
    	RAVEN_MODEL = new RavenModel(event.getEntityModels().bakeLayer(RAVEN_LAYER));
    	ZOMBIE_BRUTE_MODEL = new ZombieBruteModel(event.getEntityModels().bakeLayer(ZOMBIE_BRUTE_LAYER));
    	WRAITH_MODEL = new WraithModel(event.getEntityModels().bakeLayer(WRAITH_LAYER));
    	NECROMANCER_MODEL = new NecromancerModel(event.getEntityModels().bakeLayer(NECROMANCER_LAYER));
    	SLUG_MODEL = new SlimySlugModel(event.getEntityModels().bakeLayer(SLUG_LAYER));
    }
    
    @SubscribeEvent
    public void onRegisterEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
    	EntityRenderers.register(Registry.ZOMBIE_BRUTE.get(), ZombieBruteRenderer::new);
    	EntityRenderers.register(Registry.WRAITH.get(), WraithRenderer::new);
    	EntityRenderers.register(Registry.NECROMANCER.get(), NecromancerRenderer::new);
    	EntityRenderers.register(Registry.SOULFIRE_PROJECTILE.get(), NoopRenderer::new);
    	EntityRenderers.register(Registry.BONECHILL_PROJECTILE.get(), NoopRenderer::new);
    	EntityRenderers.register(Registry.NECROMANCER_SPELL.get(), NoopRenderer::new);
    	EntityRenderers.register(Registry.CHANT_CASTER.get(), ChantCasterRenderer::new);
    	EntityRenderers.register(Registry.RAVEN.get(), RavenRenderer::new);
    	EntityRenderers.register(Registry.ANGEL_ARROW.get(), AngelArrowRenderer::new);
    	EntityRenderers.register(Registry.SLIMY_SLUG.get(), SlimySlugRenderer::new);
    }
    
    public static ShaderInstance GLOWING_SHADER, GLOWING_SPRITE_SHADER, GLOWING_PARTICLE_SHADER, VAPOR_SHADER, GLOWING_ENTITY_SHADER, SPRITE_PARTICLE_SHADER;

    public static ShaderInstance getGlowingShader() { return GLOWING_SHADER; }
    public static ShaderInstance getGlowingSpriteShader() { return GLOWING_SPRITE_SHADER; }
    public static ShaderInstance getGlowingParticleShader() { return GLOWING_PARTICLE_SHADER; }
    public static ShaderInstance getGlowingEntityShader() { return GLOWING_ENTITY_SHADER; }
    public static ShaderInstance getVaporShader() { return VAPOR_SHADER; }
    public static ShaderInstance getSpriteParticleShader() { return SPRITE_PARTICLE_SHADER; }

    @SubscribeEvent
    public void shaderRegistry(RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(event.getResourceManager(), new ResourceLocation("eidolon:glowing"), DefaultVertexFormat.POSITION_COLOR), 
        	shader -> { GLOWING_SHADER = shader; });
        event.registerShader(new ShaderInstance(event.getResourceManager(), new ResourceLocation("eidolon:glowing_sprite"), DefaultVertexFormat.POSITION_TEX_COLOR), 
        	shader -> { GLOWING_SPRITE_SHADER = shader; });
        event.registerShader(new ShaderInstance(event.getResourceManager(), new ResourceLocation("eidolon:glowing_particle"), DefaultVertexFormat.PARTICLE), 
        	shader -> { GLOWING_PARTICLE_SHADER = shader; });
        event.registerShader(new ShaderInstance(event.getResourceManager(), new ResourceLocation("eidolon:glowing_entity"), DefaultVertexFormat.NEW_ENTITY), 
        	shader -> { GLOWING_ENTITY_SHADER = shader; });
        event.registerShader(new ShaderInstance(event.getResourceManager(), new ResourceLocation("eidolon:vapor"), DefaultVertexFormat.BLOCK), 
        	shader -> { VAPOR_SHADER = shader; });
        event.registerShader(new ShaderInstance(event.getResourceManager(), new ResourceLocation("eidolon:sprite_particle"), DefaultVertexFormat.PARTICLE), 
            	shader -> { SPRITE_PARTICLE_SHADER = shader; });
    }
    
    public static void initCurios() {
    	CuriosRendererRegistry.register(Registry.RAVEN_CLOAK.get(), RavenCloakRenderer::new);
    }

    protected static final ResourceLocation ICONS_TEXTURE = new ResourceLocation(Eidolon.MODID, "textures/gui/icons.png");
    protected static final ResourceLocation MANA_BAR_TEXTURE = new ResourceLocation(Eidolon.MODID, "textures/gui/mana_bar.png");
    
    public static class EidolonManaBar implements IIngameOverlay {
    	int xPos() {
    		String origin = ClientConfig.MANA_BAR_POSITION.get();
    		if (origin.equals(ClientConfig.Positions.BOTTOM_LEFT)
    			|| origin.equals(ClientConfig.Positions.LEFT)
    			|| origin.equals(ClientConfig.Positions.TOP_LEFT))
    			return -1;
    		if (origin.equals(ClientConfig.Positions.BOTTOM_RIGHT)
    			|| origin.equals(ClientConfig.Positions.RIGHT)
    			|| origin.equals(ClientConfig.Positions.TOP_RIGHT))
    			return 1;
    		return 0;
    	}
    	
    	int yPos() {
    		String origin = ClientConfig.MANA_BAR_POSITION.get();
    		if (origin.equals(ClientConfig.Positions.TOP_LEFT)
    			|| origin.equals(ClientConfig.Positions.TOP)
    			|| origin.equals(ClientConfig.Positions.TOP_LEFT))
    			return -1;
    		if (origin.equals(ClientConfig.Positions.BOTTOM_LEFT)
    			|| origin.equals(ClientConfig.Positions.BOTTOM_RIGHT))
    			return 1;
    		return 0;
    	}
    	
    	boolean horiz() {
    		String orient = ClientConfig.MANA_BAR_ORIENTATION.get();
    		String origin = ClientConfig.MANA_BAR_POSITION.get();
    		if (orient.equals(ClientConfig.Orientations.HORIZONTAL)) return true;
    		else if (orient.equals(ClientConfig.Orientations.VERTICAL)) return false;
    		else if (origin.equals(ClientConfig.Positions.LEFT)
    				 || origin.equals(ClientConfig.Positions.RIGHT)) return false;
    		else return true;
    	}
    	
		@Override
		public void render(ForgeIngameGui gui, PoseStack mStack, float partialTicks, int width, int height) {
	        Minecraft mc = Minecraft.getInstance();
	        LocalPlayer player = mc.player;
            
            int xp = xPos(), yp = yPos();
            boolean isHoriz = horiz();
            
            int w = isHoriz ? 120 : 28, h = isHoriz ? 28 : 120;

            int ox = width / 2 - w / 2;
            int oy = height / 2 - h / 2;
            if (isHoriz) {
	            if (yp == -1) oy = 4;
	            else if (yp == 1) oy = height + 4 - h;
	            if (xp == -1) ox = 8;
	            else if (xp == 1) ox = width - 4 - w;
            }
            else {
	            if (yp == -1) oy = -8;
	            else if (yp == 1) oy = height - 20 - h;
	            if (xp == -1) ox = 4;
	            else if (xp == 1) ox = width + 4 - w;
            }
            
            final int barlength = 114;
            float magic = 0, maxMagic = 0;
            try {
            	ISoul soul = player.getCapability(ISoul.INSTANCE).resolve().get();
            	magic = soul.getMagic();
            	maxMagic = soul.getMaxMagic();
            } catch (NoSuchElementException e) {
            	//
            }
            if (maxMagic == 0) return;
	        if (!(player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof IManaRelatedItem)
	        	&& !(player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof IManaRelatedItem))
	        	return;
                        
            int length = Mth.ceil(barlength * magic / maxMagic);
            
            int iconU = 48, iconV = 48;

            mStack.pushPose();
            mStack.translate(0, 0, 0.01);
            RenderSystem.setShaderTexture(0, MANA_BAR_TEXTURE);
            if (isHoriz) {
            	ox -= 4;
            	gui.blit(mStack, ox, oy, 2, length == 0 ? 6 : 38, 6, 20);
            	if (xp > 0) {
            		gui.blit(mStack, ox - 23, oy - 2, 0, 64, 24, 24);
            		gui.blit(mStack, ox - 18, oy + 4, iconU, iconV, 12, 12);
            	}
            	ox += 6;
            	
            	int firstSegment = Math.min(8, length);
            	length -= firstSegment;
            	gui.blit(mStack, ox, oy, 8, 38, firstSegment, 20);
            	ox += firstSegment;
            	if (firstSegment < 8) {
            		gui.blit(mStack, ox, oy, 8 + firstSegment, 6, 8 - firstSegment, 20);
            		ox += 8 - firstSegment;
            	}
            	
            	for (int i = 0; i < 6; i ++) {
            		int segment = Math.min(16, length);
            		length -= segment;
                	gui.blit(mStack, ox, oy, 16, 38, segment, 20);
                	ox += segment;
                	if (segment < 16) {
                		gui.blit(mStack, ox, oy, 16 + segment, 6, 16 - segment, 20);
                    	ox += 16 - segment;
                	}
            	}
            	
            	int lastSegment = Math.min(8, length);
            	length -= lastSegment;
            	gui.blit(mStack, ox, oy, 32, 38, lastSegment, 20);
            	ox += lastSegment;
            	if (lastSegment < 8) {
            		gui.blit(mStack, ox, oy, 32 + lastSegment, 6, 8 - lastSegment, 20);
            		ox += 8 - lastSegment;
            	}
            	
                gui.blit(mStack, ox, oy, 40, Mth.ceil(barlength * magic / maxMagic) == barlength ? 6 : 38, 7, 20);
            	if (xp <= 0) {
            		gui.blit(mStack, ox + 5, oy - 2, 32, 64, 24, 24);
            		gui.blit(mStack, ox + 12, oy + 4, iconU, iconV, 12, 12);
            	}
            }
            else {
            	oy += 16;
            	oy += barlength;
            	gui.blit(mStack, ox, oy, length == 0 ? 54 : 86, 40, 20, 6);
            	if (yp < 0) {
            		gui.blit(mStack, ox - 2, oy + 5, 32, 96, 24, 24);
        			gui.blit(mStack, ox + 4, oy + 12, iconU, iconV, 12, 12);
            	}
            	
            	int firstSegment = Math.min(8, length);
            	length -= firstSegment;
            	oy -= firstSegment;
            	gui.blit(mStack, ox, oy, 86, 32, 20, firstSegment);
            	if (firstSegment < 8) {
            		oy -= 8 - firstSegment;
                	gui.blit(mStack, ox, oy, 54, 32 + firstSegment, 20, 8 - firstSegment);
            	}
            	
            	for (int i = 0; i < 6; i ++) {
            		int segment = Math.min(16, length);
            		length -= segment;
            		oy -= segment;
                	gui.blit(mStack, ox, oy, 86, 16, 20, segment);
                	if (segment < 16) {
                		oy -= 16 - segment;
                    	gui.blit(mStack, ox, oy, 54, 16 + segment, 20, 16 - segment);
                    }
            	}
            	
            	int lastSegment = Math.min(8, length);
            	length -= lastSegment;
            	oy -= lastSegment;
            	gui.blit(mStack, ox, oy, 86, 8, 20, lastSegment);
            	if (lastSegment < 8) {
            		oy -= 8 - lastSegment;
                	gui.blit(mStack, ox, oy, 54, 8 + lastSegment, 20, 8 - lastSegment);
            	}

            	oy -= 6;
                gui.blit(mStack, ox, oy, Mth.ceil(barlength * magic / maxMagic) == barlength ? 54 : 86, 2, 20, 6);
            	if (yp >= 0) {
            		gui.blit(mStack, ox - 2, oy - 23, 0, 96, 24, 24);
            		gui.blit(mStack, ox + 4, oy - 18, iconU, iconV, 12, 12);
            	}
            }
            
            mStack.popPose();
		}
    }
    
    public static class EidolonHearts implements IIngameOverlay {
    	float lastEtherealHealth = 0;
    	long healthBlinkTime = 0;
    	long lastHealthTime = 0;
    	
		@Override
		public void render(ForgeIngameGui gui, PoseStack mStack, float partialTicks, int width, int height) {
			if (!gui.shouldDrawSurvivalElements()) return;
	        Minecraft mc = Minecraft.getInstance();
	        LocalPlayer player = mc.player;
            mStack.pushPose();
            mStack.translate(0, 0, 0.01);

            int health = Mth.ceil(player.getHealth());
            float absorb = Mth.ceil(player.getAbsorptionAmount());
            AttributeInstance attrMaxHealth = player.getAttribute(Attributes.MAX_HEALTH);
            float healthMax = (float)attrMaxHealth.getValue();
            
            float etherealHealth = 0, etherealMax = 0;
            try {
            	ISoul cap = player.getCapability(ISoul.INSTANCE).resolve().get();
            	etherealHealth = cap.getEtherealHealth();
            	etherealMax = cap.getMaxEtherealHealth();
            } catch (NoSuchElementException e) {
            	// ignore empty optional
            }

            int ticks = gui.getGuiTicks();
            boolean highlight = healthBlinkTime > (long)ticks && (healthBlinkTime - (long)ticks) / 3L % 2L == 1L;

            if (etherealHealth < this.lastEtherealHealth && player.invulnerableTime > 0) {
                this.lastHealthTime = Util.getMillis();
                this.healthBlinkTime = (long)(ticks + 20);
            }
            else if (etherealHealth > this.lastEtherealHealth) {
                this.lastHealthTime = Util.getMillis();
                this.healthBlinkTime = (long)(ticks + 10);
            }
            if (Util.getMillis() - this.lastHealthTime > 1000L) {
                lastEtherealHealth = health;
                lastHealthTime = Util.getMillis();
            }

            lastEtherealHealth = etherealHealth;

            float f = Math.max((float)player.getAttributeValue(Attributes.MAX_HEALTH), (float)Math.max(health, health));
            int regen = -1;
            if (player.hasEffect(MobEffects.REGENERATION)) regen = ticks % Mth.ceil(f + 5.0F);

            Random rand = new Random();
            rand.setSeed((long)(ticks * 312871));

            int absorptionHearts = Mth.ceil(absorb / 2.0f) - 1;
            int hearts = Mth.ceil(healthMax / 2.0f) - 1;
            int ethHearts = (int)Mth.ceil(etherealMax / 2.0f);
            int healthRows = Mth.ceil((healthMax + absorb) / 2.0F / 10.0F);
            int totalHealthRows = Mth.ceil((healthMax + absorb + etherealMax) / 2.0F / 10.0F);
            int rowHeight = Math.max(10 - (healthRows - 2), 3);
            int extraHealthRows = totalHealthRows - healthRows;
            int extraRowHeight = Mth.clamp(10 - (healthRows - 2), 3, 10);

            int left = width / 2 - 91;
            int top = height - ((ForgeIngameGui)Minecraft.getInstance().gui).left_height + healthRows * rowHeight;
            if (rowHeight != 10) top += 10 - rowHeight;
            
            gui.left_height += extraHealthRows * extraRowHeight;

            RenderSystem.setShaderTexture(0, ICONS_TEXTURE);
            for (int i = absorptionHearts + hearts + ethHearts; i > absorptionHearts + hearts; -- i) {
                int row = (i + 1) / 10;
                int heart = (i + 1) % 10;
                int x = left + heart * 8;
                int y = top - extraRowHeight * Math.max(0, row - healthRows + 1) - rowHeight * Math.min(row, healthRows - 1);
                mc.gui.blit(mStack, x, y, highlight ? 9 : 0, 18, 9, 9);
            }
            for (int i = absorptionHearts + hearts + ethHearts; i > absorptionHearts + hearts; -- i) {
                int row = (i + 1) / 10;
                int heart = (i + 1) % 10;
                int x = left + heart * 8;
                int y = top - extraRowHeight * Math.max(0, row - healthRows + 1) - rowHeight * Math.min(row, healthRows - 1);
                int i2 = i - (Mth.ceil((healthMax + absorb) / 2.0f) - 1);
                if (i2 * 2 + 1 < etherealHealth)
                    mc.gui.blit(mStack, x, y, 0, 9, 9, 9);
                else if (i2 * 2 + 1 == etherealHealth)
                    mc.gui.blit(mStack, x, y, 9, 9, 9, 9);
            }
            for (int i = Mth.ceil((healthMax + absorb) / 2.0F) - 1; i >= 0; -- i) {
                int row = i / 10;
                int heart = i % 10;
                int x = left + heart * 8;
                int y = top - row * rowHeight;

                if (health <= 4) y += rand.nextInt(2);
                if (i == regen) y -= 2;

                RenderSystem.enableBlend();
                if (player.hasEffect(Registry.CHILLED_EFFECT.get()) && i <= Mth.ceil(healthMax / 2.0f) - 1) {
	                if (i * 2 + 1 < health)
	                    mc.gui.blit(mStack, x, y, 0, 0, 9, 9);
	                else if (i * 2 + 1 == health)
	                    mc.gui.blit(mStack, x, y, 9, 0, 9, 9);
                }
                RenderSystem.disableBlend();
            }
            mStack.popPose();
		}
    }
}
