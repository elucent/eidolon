package elucent.eidolon.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class ReaperScytheItem extends SwordItem {
    public ReaperScytheItem(Properties builderIn) {
        super(Tiers.PewterTier.INSTANCE, 5, -2.9f, builderIn);
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
}
