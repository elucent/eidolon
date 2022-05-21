package elucent.eidolon.item;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import elucent.eidolon.Registry;
import elucent.eidolon.capability.IReputation;
import elucent.eidolon.capability.ISoul;
import elucent.eidolon.entity.SoulfireProjectileEntity;
import elucent.eidolon.network.MagicBurstEffectPacket;
import elucent.eidolon.network.Networking;
import elucent.eidolon.network.SoulUpdatePacket;
import elucent.eidolon.particle.Particles;
import elucent.eidolon.util.ColorUtil;
import elucent.eidolon.util.EntityUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.level.Level;

public class SummoningStaffItem extends ItemBase {
    public SummoningStaffItem(Properties builderIn) {
        super(builderIn);
    }
    
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
    	return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
       return 72000;
    }
    
    Random random = new Random();
    
    @Override
    public void onUsingTick(ItemStack stack, LivingEntity entity, int time) {
    	if (entity.level.isClientSide) {
    		HitResult hit = entity.pick(16, 0, false);
    		if (hit.getType() != Type.MISS) {
	    		Vec3 pos = hit.getLocation();
	    		time = 72000 - time;
	    		float alpha = Mth.clamp(time / 40.0f, 0, 1);
	    		float a = Mth.DEG_TO_RAD * (entity.level.getGameTime() % 360 + 12 * time);
	    		float r = 0.3f + 0.3f * alpha;
	    		float sa = r * Mth.sin(a), ca = r * Mth.cos(a);
	    		if (time == 40) {
	    			entity.playSound(SoundEvents.CROSSBOW_QUICK_CHARGE_3, 1, 1);
	    		}
	    		Particles.create(Registry.SMOKE_PARTICLE)
	    			.randomVelocity(0.025f * alpha, 0.0125f * alpha)
	        		.setColor(33.0f/255, 26.0f/255, 23.0f/255, 0.125f, 10.0f/255, 10.0f/255, 12.0f/255, 0)
	        		.setAlpha(0.25f * alpha, 0)
	        		.randomOffset(0.05f + 0.05f * alpha)
	        		.setScale(0.25f + 0.25f * alpha, alpha * 0.125f)
	        		.repeat(entity.level, pos.x + sa, pos.y, pos.z + ca, 2)
	        		.repeat(entity.level, pos.x - sa, pos.y, pos.z - ca, 2);
    		}
    	}
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int time) {
    	if ((72000 - time) >= 20 && hasCharges(stack)) {
    		ListTag charges = getCharges(stack);
    		int selected = getSelected(stack);
    		CompoundTag tag = charges.getCompound(selected);
    		HitResult hit = entity.pick(16, 0, false);
    		if (hit.getType() != Type.MISS) {
	    		Vec3 pos = hit.getLocation();
	    		Optional<EntityType<?>> etype = EntityType.by(tag);
	    		if (etype.isPresent() && !level.isClientSide) {
	    			tag.remove("UUID");
	    			Optional<Entity> e = etype.get().create(tag, level);
	    			if (e.isPresent()) {
	    				e.get().setPos(pos);
	    				EntityUtil.enthrall(entity, (LivingEntity)e.get());
	    				level.addFreshEntity(e.get());
	                    Networking.sendToTracking(entity.level, e.get().blockPosition(), new MagicBurstEffectPacket(e.get().getX(), e.get().getY() + e.get().getBbHeight() / 2, e.get().getZ(),
	                    		ColorUtil.packColor(255, 61, 70, 35), ColorUtil.packColor(255, 36, 24, 41)));
	    				level.playSound(null, e.get().blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.75f, 0.1f);
	    			}
	    		}
	
	    		entity.setItemInHand(entity.getUsedItemHand(), entity instanceof Player && ((Player)entity).getAbilities().instabuild ? stack : consumeCharge(stack, selected));
				entity.swing(entity.getUsedItemHand());
				entity.stopUsingItem();
    		}
    	}
    }
    
    public int getSelected(ItemStack stack) {
    	CompoundTag tag = stack.getOrCreateTag();
    	if (!tag.contains("selected")) tag.putInt("selected", 0);
    	else if (tag.getInt("selected") >= getCharges(stack).size()) tag.putInt("selected", 0);
    	return tag.getInt("selected");
    }
    
    public int changeSelection(ItemStack stack, int diff) {
    	if (!hasCharges(stack)) return 0;
    	CompoundTag tag = stack.getOrCreateTag();
    	int selected = getSelected(stack) + diff % getCharges(stack).size();
    	tag.putInt("selected", selected);
    	return selected;
    }
    
    public ItemStack addCharges(ItemStack stack, ListTag charges) {
    	CompoundTag tag = stack.getOrCreateTag();
    	if (!tag.contains("charges")) {
    		while (charges.size() > 100) charges.remove(charges.size() - 1);
    		tag.put("charges", charges);
    	}
    	else {
    		ListTag existing = tag.getList("charges", Tag.TAG_COMPOUND);
    		while (existing.size() + charges.size() > 100) charges.remove(charges.size() - 1);
    		if (charges.size() > 0) for (Tag t : charges) existing.add(t);
    		tag.put("charges", existing);
    	}
    	return stack;
    }
    
    public boolean hasCharges(ItemStack stack) {
    	CompoundTag tag = stack.getOrCreateTag();
    	ListTag list = tag.getList("charges", Tag.TAG_COMPOUND);
    	return tag.contains("charges") && tag.getList("charges", Tag.TAG_COMPOUND).size() > 0;
    }
    
    public ListTag getCharges(ItemStack stack) {
    	CompoundTag tag = stack.getOrCreateTag();
    	return tag.contains("charges") ? tag.getList("charges", Tag.TAG_COMPOUND) : new ListTag();
    }
    
    public ItemStack consumeCharge(ItemStack stack, int index) {
    	ListTag list = getCharges(stack);
    	if (list.size() > index) list.remove(index);
    	stack.getOrCreateTag().put("charges", list);
    	return stack;
    }
    
    public ItemStack addCharge(ItemStack stack, CompoundTag tag) {
    	ListTag list = getCharges(stack);
    	if (list.size() < 100) list.add(tag);
    	stack.getOrCreateTag().put("charges", list);
    	return stack;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (hasCharges(stack)) {
        	if (player.isCrouching()) {
        		changeSelection(stack, 1);
            	CompoundTag tag = getCharges(stack).getCompound(getSelected(stack));
            	ResourceLocation id = new ResourceLocation(tag.getString("id"));
            	String summonKey = "entity." + id.getNamespace() + "." + id.getPath();
        		player.setItemInHand(hand, stack);
        		if (!world.isClientSide) {
        			((ServerPlayer)player).connection.send(new ClientboundSetActionBarTextPacket(new TranslatableComponent("eidolon.tooltip.active_summon").append(
        				new TranslatableComponent(summonKey).withStyle(ChatFormatting.LIGHT_PURPLE)
			        )));
        			player.playNotifySound(SoundEvents.UI_BUTTON_CLICK, SoundSource.PLAYERS, 0.5f, 1.0f);
        		}
        		return InteractionResultHolder.fail(stack);
        	}
	        player.startUsingItem(hand);
	        return InteractionResultHolder.consume(stack);
        }
        else return InteractionResultHolder.fail(stack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        boolean charge = hasCharges(stack);
        int selected = getSelected(stack);
        String summonKey = "eidolon.tooltip.no_selected_summon";
        if (charge) {
        	CompoundTag tag = getCharges(stack).getCompound(selected);
            String ids = tag.getString("id");
        	ResourceLocation id = new ResourceLocation(tag.getString("id"));
        	summonKey = "entity." + id.getNamespace() + "." + id.getPath();
        }
        tooltip.add(new TranslatableComponent("eidolon.tooltip.active_summon").append(
        	new TranslatableComponent(summonKey).withStyle(charge ? ChatFormatting.LIGHT_PURPLE : ChatFormatting.DARK_PURPLE)
        ));
    }
}
