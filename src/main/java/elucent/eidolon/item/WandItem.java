package elucent.eidolon.item;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.Item.Properties;

public class WandItem extends ItemBase {
    public WandItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getEnchantmentValue() {
        return 20;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchant) {
        return super.canApplyAtEnchantingTable(stack, enchant)
            || enchant == Enchantments.UNBREAKING
            || enchant == Enchantments.MENDING;
    }
}
