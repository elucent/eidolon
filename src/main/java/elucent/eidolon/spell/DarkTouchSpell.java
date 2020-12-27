package elucent.eidolon.spell;

import java.util.List;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.capability.ReputationProvider;
import elucent.eidolon.deity.Deities;
import elucent.eidolon.network.MagicBurstEffectPacket;
import elucent.eidolon.network.Networking;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class DarkTouchSpell extends StaticSpell {
    public static final String NECROTIC_KEY = new ResourceLocation(Eidolon.MODID, "necrotic").toString();

    public DarkTouchSpell(ResourceLocation name, Sign... signs) {
        super(name, signs);

//        MinecraftForge.EVENT_BUS.addListener(DarkTouchSpell::onHurt);
//        DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> {
//            MinecraftForge.EVENT_BUS.addListener(DarkTouchSpell::tooltip);
//            return new Object();
//        });
    }

//    @SubscribeEvent
//    public static void onHurt(LivingHurtEvent event) {
//        if (event.getSource().getDamageType() != DamageSource.WITHER.getDamageType()
//            && event.getSource().getTrueSource() instanceof LivingEntity
//            && ((LivingEntity)event.getSource().getTrueSource()).getHeldItemMainhand().hasTag()
//            && ((LivingEntity)event.getSource().getTrueSource()).getHeldItemMainhand().getTag().contains(NECROTIC_KEY)) {
//            float amount = Math.min(1, event.getAmount());
//            event.setAmount(event.getAmount() - amount);
//            if (event.getAmount() <= 0) event.setCanceled(true);
//            int prevHurtResist = event.getEntityLiving().hurtResistantTime;
//            if (event.getEntityLiving().attackEntityFrom(new EntityDamageSource(DamageSource.WITHER.getDamageType(), event.getSource().getTrueSource()), amount)) {
//                if (event.getEntityLiving().getHealth() <= 0) event.setCanceled(true);
//                else event.getEntityLiving().hurtResistantTime = prevHurtResist;
//            }
//        }
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    @SubscribeEvent
//    public static void tooltip(ItemTooltipEvent event) {
//        if (event.getItemStack().hasTag() && event.getItemStack().getTag().contains(NECROTIC_KEY)) {
//            event.getToolTip().add(new TranslationTextComponent("eidolon.tooltip.necrotic").mergeStyle(TextFormatting.DARK_BLUE));
//        }
//    }

    @Override
    public boolean canCast(World world, BlockPos pos, PlayerEntity player) {
        if (!world.getCapability(ReputationProvider.CAPABILITY).isPresent()) return false;
        if (world.getCapability(ReputationProvider.CAPABILITY).resolve().get().getReputation(player, Deities.DARK_DEITY.getId()) < 4.0) return false;

        RayTraceResult ray = world.rayTraceBlocks(new RayTraceContext(player.getEyePosition(0), player.getEyePosition(0).add(player.getLookVec().scale(4)), RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, player));
        Vector3d v = ray.getType() == RayTraceResult.Type.BLOCK ? ray.getHitVec() : player.getEyePosition(0).add(player.getLookVec().scale(4));
        List<ItemEntity> items = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(v.x - 1.5, v.y - 1.5, v.z - 1.5, v.x + 1.5, v.y + 1.5, v.z + 1.5));
        if (items.size() != 1) return false;
        ItemStack stack = items.get(0).getItem();
        return stack.getCount() == 1 && canTouch(stack);
    }

    boolean canTouch(ItemStack stack) {
        return stack.getItem() == Registry.PEWTER_INLAY.get()             // is pewter
               || stack.getItem() == Items.BLACK_WOOL
               || (stack.getItem() instanceof MusicDiscItem && stack.getItem() != Registry.PAROUSIA_DISC.get());
            // || (stack.isDamageable() && stack.getMaxStackSize() == 1); // is tool
    }

    ItemStack touchResult(ItemStack stack) { // assumes canTouch is true
        if (stack.getItem() == Registry.PEWTER_INLAY.get())
            return new ItemStack(Registry.UNHOLY_SYMBOL.get());
        else if (stack.getItem() == Items.BLACK_WOOL)
            return new ItemStack(Registry.TOP_HAT.get());
        else if (stack.getItem() instanceof MusicDiscItem && stack.getItem() != Registry.PAROUSIA_DISC.get())
            return new ItemStack(Registry.PAROUSIA_DISC.get());
//        else {
//            stack.getOrCreateTag().putBoolean(NECROTIC_KEY, true);
//            return stack;
//        }
        else return stack;
    }

    @Override
    public void cast(World world, BlockPos pos, PlayerEntity player) {
        RayTraceResult ray = world.rayTraceBlocks(new RayTraceContext(player.getEyePosition(0), player.getEyePosition(0).add(player.getLookVec().scale(4)), RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, player));
        Vector3d v = ray.getType() == RayTraceResult.Type.BLOCK ? ray.getHitVec() : player.getEyePosition(0).add(player.getLookVec().scale(4));
        List<ItemEntity> items = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(v.x - 1.5, v.y - 1.5, v.z - 1.5, v.x + 1.5, v.y + 1.5, v.z + 1.5));
        if (items.size() == 1) {
            if (!world.isRemote) {
                ItemStack stack = items.get(0).getItem();
                if (canTouch(stack)) {
                    items.get(0).setItem(touchResult(stack));
                    Vector3d p = items.get(0).getPositionVec();
                    items.get(0).setDefaultPickupDelay();
                    Networking.sendToTracking(world, items.get(0).getPosition(), new MagicBurstEffectPacket(p.x, p.y, p.z, Signs.WICKED_SIGN.getColor(), Signs.BLOOD_SIGN.getColor()));
                }
            } else {
                world.playSound(player, player.getPosition(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.NEUTRAL, 1.0F, 0.6F + world.rand.nextFloat() * 0.2F);
            }
        }
    }
}
