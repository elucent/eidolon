package elucent.eidolon.item;

import elucent.eidolon.network.LifestealEffectPacket;
import elucent.eidolon.network.Networking;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class SappingSwordItem extends SwordItem {
    public static class SappingSwordTier implements IItemTier {
        @Override
        public int getMaxUses() {
            return 507;
        }

        @Override
        public float getEfficiency() {
            return 4.0f;
        }

        @Override
        public float getAttackDamage() {
            return 1;
        }

        @Override
        public int getHarvestLevel() {
            return 1;
        }

        @Override
        public int getEnchantability() {
            return 20;
        }

        @Override
        public Ingredient getRepairMaterial() {
            return Ingredient.fromStacks(new ItemStack(Items.IRON_INGOT));
        }

        public static SappingSwordTier INSTANCE = new SappingSwordTier();
    }

    public SappingSwordItem(Properties builderIn) {
        super(SappingSwordTier.INSTANCE, 3, -2.4f, builderIn);
    }

    String loreTag = null;

    public Item setLore(String tag) {
        this.loreTag = tag;
        return this;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (this.loreTag != null) {
            tooltip.add(new StringTextComponent(""));
            tooltip.add(new StringTextComponent("" + TextFormatting.DARK_PURPLE + TextFormatting.ITALIC + I18n.format(this.loreTag)));
        }
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target.hurtResistantTime > 0) {
            target.hurtResistantTime = 0;
            float before = target.getHealth();
            target.attackEntityFrom(new EntityDamageSource("wither", attacker).setDamageBypassesArmor(), 2.0f);
            float healing = before - target.getHealth();
            if (healing > 0) {
                attacker.heal(healing);
                if (!attacker.world.isRemote) Networking.sendToTracking(attacker.world, attacker.getPosition(), new LifestealEffectPacket(target.getPosition(), attacker.getPosition(), 1.0f, 0.125f, 0.1875f));
            }
        }
        return super.hitEntity(stack, target, attacker);
    }
}
