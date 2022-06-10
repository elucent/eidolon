package elucent.eidolon.item;

import elucent.eidolon.util.BlockUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
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
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (this.loreTag != null) {
            tooltip.add(new TextComponent(""));
            tooltip.add(new TextComponent("" + ChatFormatting.DARK_PURPLE + ChatFormatting.ITALIC + I18n.get(this.loreTag)));
        }
    }

    @SubscribeEvent
    public static void onStartBreak(PlayerEvent.BreakSpeed event) {
        if (event.getPlayer().getMainHandItem().getItem() instanceof ReversalPickItem) {
            float hardness = event.getState().getDestroySpeed(event.getEntity().level, event.getPos());
            float adjHardness = 1 / (hardness / 1.5f + BlockUtil.getHarvestLevel(event.getState()));
            float newSpeed = Mth.sqrt(event.getOriginalSpeed() * 0.25f) * Mth.sqrt(hardness / adjHardness);
            event.setNewSpeed(newSpeed * newSpeed);
        }
    }
}
