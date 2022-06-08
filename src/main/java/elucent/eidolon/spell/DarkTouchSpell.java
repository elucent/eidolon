package elucent.eidolon.spell;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.capability.ReputationProvider;
import elucent.eidolon.deity.Deities;
import elucent.eidolon.network.MagicBurstEffectPacket;
import elucent.eidolon.network.Networking;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;

import java.util.List;

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
    public boolean canCast(Level world, BlockPos pos, Player player) {
        if (!world.getCapability(ReputationProvider.CAPABILITY).isPresent()) return false;
        if (world.getCapability(ReputationProvider.CAPABILITY).resolve().get().getReputation(player, Deities.DARK_DEITY.getId()) < 4.0) return false;

        HitResult ray = world.clip(new ClipContext(player.getEyePosition(0), player.getEyePosition(0).add(player.getLookAngle().scale(4)), ClipContext.BlockMode.OUTLINE, ClipContext.FluidMode.NONE, player));
        Vec3 v = ray.getType() == HitResult.Type.BLOCK ? ray.getLocation() : player.getEyePosition(0).add(player.getLookAngle().scale(4));
        List<ItemEntity> items = world.getEntitiesOfClass(ItemEntity.class, new AABB(v.x - 1.5, v.y - 1.5, v.z - 1.5, v.x + 1.5, v.y + 1.5, v.z + 1.5));
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
    public void cast(Level world, BlockPos pos, Player player) {
        HitResult ray = world.clip(new ClipContext(player.getEyePosition(0), player.getEyePosition(0).add(player.getLookAngle().scale(4)), ClipContext.BlockMode.OUTLINE, ClipContext.FluidMode.NONE, player));
        Vec3 v = ray.getType() == HitResult.Type.BLOCK ? ray.getLocation() : player.getEyePosition(0).add(player.getLookAngle().scale(4));
        List<ItemEntity> items = world.getEntitiesOfClass(ItemEntity.class, new AABB(v.x - 1.5, v.y - 1.5, v.z - 1.5, v.x + 1.5, v.y + 1.5, v.z + 1.5));
        if (items.size() == 1) {
            if (!world.isClientSide) {
                ItemStack stack = items.get(0).getItem();
                if (canTouch(stack)) {
                    items.get(0).setItem(touchResult(stack));
                    Vec3 p = items.get(0).position();
                    items.get(0).setDefaultPickUpDelay();
                    Networking.sendToTracking(world, items.get(0).blockPosition(), new MagicBurstEffectPacket(p.x, p.y, p.z, Signs.WICKED_SIGN.getColor(), Signs.BLOOD_SIGN.getColor()));
                }
            } else {
                world.playSound(player, player.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.NEUTRAL, 1.0F, 0.6F + world.random.nextFloat() * 0.2F);
            }
        }
    }
}
