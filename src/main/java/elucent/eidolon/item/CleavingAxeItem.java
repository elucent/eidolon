package elucent.eidolon.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

import net.minecraft.world.item.Item.Properties;

public class CleavingAxeItem extends AxeItem {
    public CleavingAxeItem(Properties builderIn) {
        super(Tiers.PewterTier.INSTANCE, 7, -3.2f, builderIn);
    }

    String loreTag = null;

    public Item setLore(String tag) {
        this.loreTag = tag;
        return this;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, Level worldIn, List<TextComponent> tooltip, TooltipFlag flagIn) {
        if (this.loreTag != null) {
            tooltip.add(new TextComponent(""));
            tooltip.add(new TextComponent("" + ChatFormatting.DARK_PURPLE + ChatFormatting.ITALIC + I18n.get(this.loreTag)));
        }
    }
}
