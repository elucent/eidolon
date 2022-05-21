package elucent.eidolon.item;

import java.util.List;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemBase extends Item {
	String loreFormat = "" + ChatFormatting.DARK_PURPLE + ChatFormatting.ITALIC;
    String loreTag = null;

    public ItemBase(Properties properties) {
        super(properties);
    }

    public ItemBase setLore(String tag) {
        this.loreTag = tag;
        return this;
    }

    public ItemBase setLore(ChatFormatting format, String tag) {
    	this.loreFormat = "" + format;
        this.loreTag = tag;
        return this;
    }

    public ItemBase setLore(String format, String tag) {
    	this.loreFormat = format;
        this.loreTag = tag;
        return this;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (this.loreTag != null) {
            tooltip.add(new TextComponent(loreFormat + I18n.get(this.loreTag)));
        }
    }
}
