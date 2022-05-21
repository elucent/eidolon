package elucent.eidolon.item;

import elucent.eidolon.Eidolon;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.spell.Signs;
import elucent.eidolon.util.KnowledgeUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;

public class CodexItem extends ItemBase implements IManaRelatedItem {
    public CodexItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
        if (world.isClientSide) {
            entity.playNotifySound(SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS, 1.0f, 1.0f);
            Eidolon.proxy.openCodexGui();
        }
        return InteractionResultHolder.pass(entity.getItemInHand(hand));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (!world.isClientSide && stack.hasTag() && stack.getTag().contains("sign")) {
            ResourceLocation loc = new ResourceLocation(stack.getTag().getString("sign"));
            stack.getTag().remove("sign");
            Sign sign = Signs.find(loc);
            if (sign != null) KnowledgeUtil.grantSign(entity, sign);
        }
    }

    public static ItemStack withSign(ItemStack stack, Sign sign) {
        ItemStack newStack = stack.copy();
        newStack.getOrCreateTag().putString("sign", sign.getRegistryName().toString());
        return newStack;
    }
}
