package elucent.eidolon.item;

import elucent.eidolon.Registry;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class Tiers {
    public static class PewterTier implements IItemTier {
        @Override
        public int getMaxUses() {
            return 325;
        }

        @Override
        public float getEfficiency() {
            return 6.5f;
        }

        @Override
        public float getAttackDamage() {
            return 2;
        }

        @Override
        public int getHarvestLevel() {
            return 2;
        }

        @Override
        public int getEnchantability() {
            return 8;
        }

        @Override
        public Ingredient getRepairMaterial() {
            return Ingredient.fromStacks(new ItemStack(Registry.PEWTER_INGOT.get()));
        }

        public static PewterTier INSTANCE = new PewterTier();
    }

    public static class SanguineTier implements IItemTier {
        @Override
        public int getMaxUses() {
            return 507;
        }

        @Override
        public float getEfficiency() {
            return 8.0f;
        }

        @Override
        public float getAttackDamage() {
            return 3;
        }

        @Override
        public int getHarvestLevel() {
            return 3;
        }

        @Override
        public int getEnchantability() {
            return 20;
        }

        @Override
        public Ingredient getRepairMaterial() {
            return Ingredient.EMPTY;
        }

        public static SanguineTier INSTANCE = new SanguineTier();
    }
}
