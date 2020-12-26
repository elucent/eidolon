package elucent.eidolon.tile;

import elucent.eidolon.Registry;
import elucent.eidolon.ritual.IRitualItemProvider;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class HandTileEntity extends TileEntityBase implements IRitualItemProvider {
    ItemStack stack = ItemStack.EMPTY;

    public HandTileEntity() {
        this(Registry.HAND_TILE_ENTITY);
    }

    public HandTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void onDestroyed(BlockState state, BlockPos pos) {
        if (!stack.isEmpty()) InventoryHelper.spawnItemStack(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
    }

    @Override
    public ActionResultType onActivated(BlockState state, BlockPos pos, PlayerEntity player, Hand hand) {
        if (hand == Hand.MAIN_HAND) {
            if (player.getHeldItem(hand).isEmpty() && !stack.isEmpty()) {
                player.addItemStackToInventory(stack);
                stack = ItemStack.EMPTY;
                if (!world.isRemote) sync();
                return ActionResultType.SUCCESS;
            }
            else if (!player.getHeldItem(hand).isEmpty() && stack.isEmpty()) {
                stack = player.getHeldItem(hand).copy();
                stack.setCount(1);
                player.getHeldItem(hand).shrink(1);
                if (player.getHeldItem(hand).isEmpty()) player.setHeldItem(hand, ItemStack.EMPTY);
                if (!world.isRemote) sync();
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        stack = ItemStack.read(tag.getCompound("stack"));
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        tag.put("stack", stack.write(new CompoundNBT()));
        return tag;
    }

    @Override
    public ItemStack provide() {
        return stack.copy();
    }

    @Override
    public void take() {
        stack = ItemStack.EMPTY;
        if (!world.isRemote) sync();
    }
}
