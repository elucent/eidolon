package elucent.eidolon.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import elucent.eidolon.Registry;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.material.Material;
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

public class AthameItem extends SwordItem {
	private Random random = new Random();
	
    public AthameItem(Properties builderIn) {
        super(Tiers.PewterTier.INSTANCE, 1, -1.6f, builderIn);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onLooting(LootingLevelEvent event) {
        if (event.getEntityLiving().getMainHandItem().getItem() instanceof AthameItem)
            event.setLootingLevel(event.getLootingLevel() * 2 + 1);
    }

    @SubscribeEvent
    public void onHurt(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity
            && ((LivingEntity)event.getSource().getEntity()).getMainHandItem().getItem() instanceof AthameItem
            && (event.getEntity() instanceof EnderMan || event.getEntity() instanceof Endermite || event.getEntity() instanceof EnderDragon)) {
            event.setAmount(event.getAmount() * 4);
        }
    }

    String loreTag = null;

    public Item setLore(String tag) {
        this.loreTag = tag;
        return this;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (this.loreTag != null) {
            tooltip.add(new TextComponent(""));
            tooltip.add(new TextComponent("" + ChatFormatting.DARK_PURPLE + ChatFormatting.ITALIC + I18n.get(this.loreTag)));
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        BlockState state = ctx.getLevel().getBlockState(ctx.getClickedPos());
        float hardness = state.getDestroySpeed(ctx.getLevel(), ctx.getClickedPos());
        if ((state.getMaterial() == Material.PLANT || state.getMaterial() == Material.REPLACEABLE_PLANT || state.getMaterial() == Material.LEAVES || state.getMaterial() == Material.WATER_PLANT || state.getMaterial() == Material.REPLACEABLE_FIREPROOF_PLANT)
            && hardness < 5.0f && hardness >= 0) {
            if (!ctx.getLevel().isClientSide) {
                Vec3 hit = ctx.getClickLocation();
                ((ServerLevel)ctx.getLevel()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), hit.x, hit.y, hit.z, 3, ((double)random.nextFloat() - 0.5D) * 0.08D, ((double)random.nextFloat() - 0.5D) * 0.08D, ((double)random.nextFloat() - 0.5D) * 0.08D, (double)0.05F);
                ctx.getLevel().playSound(null, ctx.getClickedPos(), SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS, 0.5f, 0.9f + random.nextFloat() * 0.2f);
                if (random.nextInt(5) == 0) {
                    if (state.getBlock() instanceof DoublePlantBlock && state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER)
                        ctx.getLevel().destroyBlock(ctx.getClickedPos().below(), false);
                    else ctx.getLevel().destroyBlock(ctx.getClickedPos(), false);
                    if (random.nextInt(10) == 0) {
                        ItemStack drop = getHarvestable(state);
                        if (!drop.isEmpty() && !ctx.getLevel().isClientSide) {
                            ctx.getLevel().playSound(null, ctx.getClickedPos(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.5f, 0.9f + random.nextFloat() * 0.2f);
                            ctx.getLevel().addFreshEntity(new ItemEntity(ctx.getLevel(), ctx.getClickedPos().getX() + 0.5, ctx.getClickedPos().getY() + 0.5, ctx.getClickedPos().getZ() + 0.5, drop.copy()));
                        }
                        if (!ctx.getPlayer().isCreative()) ctx.getItemInHand().hurtAndBreak(1, ctx.getPlayer(), (player) -> {
                            player.broadcastBreakEvent(ctx.getHand());
                        });
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }
        return super.useOn(ctx);
    }

    public static Map<ResourceLocation, ItemStack> harvestables = new HashMap<>();

    public static void initHarvestables() {
        harvestables.put(Blocks.JUNGLE_LEAVES.getRegistryName(), new ItemStack(Registry.SILDRIAN_SEED.get()));
        harvestables.put(Blocks.LILY_PAD.getRegistryName(), new ItemStack(Registry.OANNA_BLOOM.get()));
        harvestables.put(Blocks.OXEYE_DAISY.getRegistryName(), new ItemStack(Registry.MERAMMER_ROOT.get()));
        harvestables.put(Blocks.FERN.getRegistryName(), new ItemStack(Registry.AVENNIAN_SPRIG.get()));
    }

    public static ItemStack getHarvestable(BlockState state) {
        return harvestables.getOrDefault(state.getBlock().getRegistryName(), ItemStack.EMPTY);
    }
}
