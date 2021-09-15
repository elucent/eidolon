package elucent.eidolon.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class ReversalPickItem extends PickaxeItem {
    public ReversalPickItem(Properties builderIn) {
        super(Tiers.MagicToolTier.INSTANCE, 1, -2.8F, builderIn);
        MinecraftForge.EVENT_BUS.addListener(ReversalPickItem::onStartBreak);
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

    @SubscribeEvent
    public static void onStartBreak(PlayerEvent.BreakSpeed event) {
        if (event.getPlayer().getHeldItemMainhand().getItem() instanceof ReversalPickItem) {
            float hardness = event.getState().getBlockHardness(event.getEntity().world, event.getPos());
            float adjHardness = 1 / (hardness / 1.5f + event.getState().getHarvestLevel());
            float newSpeed = MathHelper.sqrt(event.getOriginalSpeed() * 0.25f) * MathHelper.sqrt(hardness / adjHardness);
            event.setNewSpeed(newSpeed * newSpeed);
        }
    }
}
