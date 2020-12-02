package elucent.eidolon.item;

import elucent.eidolon.codex.CodexGui;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.spell.SignUtil;
import elucent.eidolon.spell.Signs;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class CodexItem extends ItemBase {
    public CodexItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity entity, Hand hand) {
        if (world.isRemote) {
            entity.playSound(SoundEvents.ITEM_BOOK_PAGE_TURN, SoundCategory.PLAYERS, 1.0f, 1.0f);
            Minecraft.getInstance().displayGuiScreen(CodexGui.getInstance());
        }
        return ActionResult.resultPass(entity.getHeldItem(hand));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (!world.isRemote && stack.hasTag() && stack.getTag().contains("sign")) {
            ResourceLocation loc = new ResourceLocation(stack.getTag().getString("sign"));
            stack.getTag().remove("sign");
            Sign sign = Signs.find(loc);
            if (sign != null) SignUtil.grantSign(entity, sign);
        }
    }

    public static ItemStack withSign(ItemStack stack, Sign sign) {
        ItemStack newStack = stack.copy();
        newStack.getOrCreateTag().putString("sign", sign.getRegistryName().toString());
        return newStack;
    }
}
