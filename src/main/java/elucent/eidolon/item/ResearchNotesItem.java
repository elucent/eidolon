package elucent.eidolon.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import elucent.eidolon.Registry;
import elucent.eidolon.research.Research;
import elucent.eidolon.research.Researches;
import elucent.eidolon.util.KnowledgeUtil;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.SwordItem;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.InteractionResult;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

public class ResearchNotesItem extends ItemBase {
	private Random random = new Random();
	
    public ResearchNotesItem(Properties builderIn) {
        super(builderIn);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    	if (!stack.hasTag() || !stack.getTag().contains("research")) return;
        Research r = Researches.find(new ResourceLocation(stack.getTag().getString("research")));
        if (r == null) return;
        int done = stack.getTag().getInt("stepsDone");
        StringBuilder stars = new StringBuilder();
        stars.append(ChatFormatting.GOLD);
        for (int i = 0; i < r.getStars(); i ++) {
        	if (i == done) stars.append(ChatFormatting.GRAY);
        	if (i < done) stars.append("\u2605");
        	else stars.append("\u2606");
        }
        tooltip.add(new TextComponent(stars.toString()));
        boolean known = done >= r.getStars() || KnowledgeUtil.knowsResearch(Minecraft.getInstance().player, r.getRegistryName());
        String name = known ? r.getName() : "???";
        tooltip.add(new TextComponent("" + (known ? ChatFormatting.GRAY : ChatFormatting.DARK_GRAY) + ChatFormatting.ITALIC + name));
    }
}
