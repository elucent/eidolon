package elucent.eidolon.item;

import elucent.eidolon.Registry;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class Tiers {
    public static class PewterTier implements Tier {
        @Override
        public int getUses() {
            return 325;
        }

        @Override
        public float getSpeed() {
            return 6.5f;
        }

        @Override
        public float getAttackDamageBonus() {
            return 2;
        }

        @Override
        public int getLevel() {
            return 2;
        }

        @Override
        public int getEnchantmentValue() {
            return 8;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(new ItemStack(Registry.PEWTER_INGOT.get()));
        }

        public static PewterTier INSTANCE = new PewterTier();
    }

    public static class MagicToolTier implements Tier {
        @Override
        public int getUses() {
            return 1170;
        }

        @Override
        public float getSpeed() {
            return 7.0f;
        }

        @Override
        public float getAttackDamageBonus() {
            return 3;
        }

        @Override
        public int getLevel() {
            return 3;
        }

        @Override
        public int getEnchantmentValue() {
            return 30;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.EMPTY;
        }

        public static MagicToolTier INSTANCE = new MagicToolTier();
    }

    public static class SilverTier implements Tier {
        @Override
        public int getUses() {
            return 193;
        }

        @Override
        public float getSpeed() {
            return 7.0f;
        }

        @Override
        public float getAttackDamageBonus() {
            return 2;
        }

        @Override
        public int getLevel() {
            return 2;
        }

        @Override
        public int getEnchantmentValue() {
            return 20;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(new ItemStack(Registry.SILVER_INGOT.get()));
        }

        public static SilverTier INSTANCE = new SilverTier();
    }

    public static class SanguineTier implements Tier {
        @Override
        public int getUses() {
            return 507;
        }

        @Override
        public float getSpeed() {
            return 8.0f;
        }

        @Override
        public float getAttackDamageBonus() {
            return 3;
        }

        @Override
        public int getLevel() {
            return 3;
        }

        @Override
        public int getEnchantmentValue() {
            return 20;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.EMPTY;
        }

        public static SanguineTier INSTANCE = new SanguineTier();
    }
}
