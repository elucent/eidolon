package elucent.eidolon.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class ItemBase extends Item {
    String loreTag = null;

    public ItemBase(Properties properties) {
        super(properties);
    }

    public ItemBase setLore(String tag) {
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
}
