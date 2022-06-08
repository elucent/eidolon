package elucent.eidolon.tile;

import elucent.eidolon.Registry;
import elucent.eidolon.ritual.IRitualItemProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.inventory.InventoryHelper;

public class HandTileEntity extends TileEntityBase implements IRitualItemProvider {
    ItemStack stack = ItemStack.EMPTY;

    public HandTileEntity() {
        this(Registry.HAND_TILE_ENTITY);
    }

    public HandTileEntity(BlockEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void onDestroyed(BlockState state, BlockPos pos) {
        if (!stack.isEmpty()) InventoryHelper.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
    }

    @Override
    public InteractionResult onActivated(BlockState state, BlockPos pos, Player player, InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND) {
            if (player.getItemInHand(hand).isEmpty() && !stack.isEmpty()) {
                player.addItem(stack);
                stack = ItemStack.EMPTY;
                if (!level.isClientSide) sync();
                return InteractionResult.SUCCESS;
            }
            else if (!player.getItemInHand(hand).isEmpty() && stack.isEmpty()) {
                stack = player.getItemInHand(hand).copy();
                stack.setCount(1);
                player.getItemInHand(hand).shrink(1);
                if (player.getItemInHand(hand).isEmpty()) player.setItemInHand(hand, ItemStack.EMPTY);
                if (!level.isClientSide) sync();
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void load(BlockState state, CompoundTag tag) {
        super.load(state, tag);
        stack = ItemStack.of(tag.getCompound("stack"));
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag = super.save(tag);
        tag.put("stack", stack.save(new CompoundTag()));
        return tag;
    }

    @Override
    public ItemStack provide() {
        return stack.copy();
    }

    @Override
    public void take() {
        stack = ItemStack.EMPTY;
        if (!level.isClientSide) sync();
    }
}
