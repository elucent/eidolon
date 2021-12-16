package elucent.eidolon.item.curio;

import java.util.Random;

import elucent.eidolon.Registry;
import elucent.eidolon.entity.AngelArrowEntity;
import elucent.eidolon.item.ItemBase;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stats.Stats;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;

import net.minecraft.world.item.Item.Properties;

public class AngelSightItem extends ItemBase {
	static Random random = new Random();
	
    public AngelSightItem(Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.addListener(AngelSightItem::onLoose);
    }

    @SubscribeEvent
    public static void onLoose(ArrowLooseEvent event) {
        Player player = event.getPlayer();
        ItemStack stack = event.getBow(), ammo = player.getProjectile(stack);
        Level world = event.getWorld();

        if (!event.hasAmmo()) return;

        if (ammo.isEmpty()) {
            ammo = new ItemStack(Items.ARROW);
        }

        if (!CuriosApi.getCuriosHelper().findEquippedCurio(Registry.ANGELS_SIGHT.get(), event.getEntityLiving()).isPresent())
            return;

        float f = BowItem.getPowerForTime(event.getCharge());
        if (!((double)f < 0.1D)) {
            boolean flag1 = player.getAbilities().instabuild || (ammo.getItem() instanceof ArrowItem && ((ArrowItem) ammo.getItem()).isInfinite(ammo, stack, player));
            if (!world.isClientSide) {
                ArrowItem arrowitem = (ArrowItem) (ammo.getItem() instanceof ArrowItem ? ammo.getItem() : Items.ARROW);
                AbstractArrow innerarrow = arrowitem.createArrow(world, ammo, player);
                innerarrow = ((BowItem)stack.getItem()).customArrow(innerarrow);
                AngelArrowEntity abstractarrowentity = new AngelArrowEntity(world, player);
                abstractarrowentity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 3.0F, 1.0F);
                if (f == 1.0F) {
                    innerarrow.setCritArrow(true);
                    abstractarrowentity.setCritArrow(true);
                }

                int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
                if (j > 0) {
                    innerarrow.setBaseDamage(innerarrow.getBaseDamage() + (double) j * 0.5D + 0.5D);
                }

                int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
                if (k > 0) {
                    innerarrow.setKnockback(k);
                }

                if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
                    innerarrow.setSecondsOnFire(100);
                }
                abstractarrowentity.setArrow(innerarrow);

                stack.hurtAndBreak(1, player, (p) -> {
                    p.broadcastBreakEvent(player.getUsedItemHand());
                });
                if (flag1 || player.getAbilities().instabuild && (stack.getItem() == Items.SPECTRAL_ARROW || stack.getItem() == Items.TIPPED_ARROW)) {
                    abstractarrowentity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                }

                world.addFreshEntity(abstractarrowentity);
            }

            world.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
            if (!flag1 && !player.getAbilities().instabuild) {
                ammo.shrink(1);
                if (ammo.isEmpty()) {
                    player.getInventory().removeItem(ammo);
                }
            }

            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
        }
        event.setCanceled(true);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag unused) {
        return new EidolonCurio(stack) {
            @Override
            public boolean canRightClickEquip() {
                return true;
            }
        };
    }
}
