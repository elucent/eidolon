package elucent.eidolon.item;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;

public class WandItem extends ItemBase {
    public WandItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getItemEnchantability() {
        return 20;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchant) {
        return super.canApplyAtEnchantingTable(stack, enchant)
            || enchant == Enchantments.UNBREAKING
            || enchant == Enchantments.MENDING;
    }
}
