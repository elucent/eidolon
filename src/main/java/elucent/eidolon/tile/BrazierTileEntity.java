package elucent.eidolon.tile;

import elucent.eidolon.Registry;
import elucent.eidolon.network.*;
import elucent.eidolon.particle.Particles;
import elucent.eidolon.ritual.Ritual;
import elucent.eidolon.ritual.Ritual.RitualResult;
import elucent.eidolon.ritual.Ritual.SetupResult;
import elucent.eidolon.ritual.RitualRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class BrazierTileEntity extends TileEntityBase implements ITickableTileEntity {
    ItemStack stack = ItemStack.EMPTY;
    boolean burning = false;
    int findingCounter = 0;
    int stepCounter = 0;
    Ritual ritual = null;
    int step = 0;
    boolean ritualDone = false;

    public BrazierTileEntity() {
        this(Registry.BRAZIER_TILE_ENTITY);
    }

    public BrazierTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void onDestroyed(BlockState state, BlockPos pos) {
        super.onDestroyed(state, pos);
        if (!stack.isEmpty()) InventoryHelper.spawnItemStack(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
    }

    @Override
    public ActionResultType onActivated(BlockState state, BlockPos pos, PlayerEntity player, Hand hand) {
        if (hand == Hand.MAIN_HAND) {
            if (burning && player.isSneaking() && player.getHeldItem(hand).isEmpty()) {
                extinguish();
                return ActionResultType.SUCCESS;
            }
            else if (!burning && player.getHeldItem(hand).isEmpty() && !stack.isEmpty()) {
                player.addItemStackToInventory(stack);
                stack = ItemStack.EMPTY;
                if (!world.isRemote) sync();
                return ActionResultType.SUCCESS;
            }
            else if (!burning && !stack.isEmpty()
                && player.getHeldItem(hand).getItem() == Items.FLINT_AND_STEEL) {
                player.getHeldItem(hand).damageItem(1, player, (p) -> {
                    p.sendBreakAnimation(hand);
                });
                startBurning();
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
        burning = tag.getBoolean("burning");
        ritual = tag.contains("ritual") ? RitualRegistry.find(new ResourceLocation(tag.getString("ritual"))) : null;
        step = tag.getInt("step");
        ritualDone = tag.getBoolean("ritualDone");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        tag.put("stack", stack.write(new CompoundNBT()));
        tag.putBoolean("burning", burning);
        if (ritual != null) tag.putString("ritual", ritual.getRegistryName().toString());
        tag.putInt("step", step);
        tag.putBoolean("ritualDone", ritualDone);
        return tag;
    }

    protected void complete() {
        burning = false;
        stepCounter = 0;
        findingCounter = 0;
        if (!world.isRemote) {
            if (ritual != null)
                Networking.sendToTracking(world, pos.up(2), new RitualCompletePacket(pos.up(2), ritual.getRed(), ritual.getGreen(), ritual.getBlue()));
            ritual = null;
            Networking.sendToTracking(world, pos, new ExtinguishEffectPacket(pos));
            sync();
        }
        ritual = null;
    }

    protected void extinguish() {
        burning = false;
        stepCounter = 0;
        findingCounter = 0;
        if (!world.isRemote) {
            if (ritual != null)
                Networking.sendToTracking(world, pos.up(2), new FlameEffectPacket(pos.up(2), ritual.getRed(), ritual.getGreen(), ritual.getBlue()));
            ritual = null;
            Networking.sendToTracking(world, pos, new ExtinguishEffectPacket(pos));
            sync();
        }
        ritual = null;
    }

    protected void startBurning() {
        burning = true;
        findingCounter = 0;
        if (!world.isRemote) {
            Networking.sendToTracking(world, pos, new IgniteEffectPacket(pos, 1.0f, 0.5f, 0.25f));
            sync();
        }
    }

    protected void setRitual(Ritual ritual) {
        this.ritual = ritual;
        if (ritual == null) extinguish();
        else {
            stepCounter = 0;
            step = 0;
            ritualDone = false;
            if (!world.isRemote) {
                Networking.sendToTracking(world, pos.up(2), new FlameEffectPacket(pos.up(2), ritual.getRed(), ritual.getGreen(), ritual.getBlue()));
                sync();
            }
        }
    }

    @Override
    public void tick() {
        if (burning && findingCounter < 80 && ritual == null) {
            float progress = (findingCounter - 40) / 40.0f;
            if (progress >= 0) for (int i = 0; i < 8; i ++) {
                float angle = progress * (float)Math.PI / 4 + i * (float)Math.PI / 4;
                float radius = 0.625f * MathHelper.sin(4 * angle);
                angle += (float)Math.PI / 4;
                float x = getPos().getX() + 0.5f + MathHelper.sin(angle) * radius;
                float y = getPos().getY() + 0.875f;
                float z = getPos().getZ() + 0.5f + MathHelper.cos(angle) * radius;
                Particles.create(Registry.WISP_PARTICLE)
                    .setAlpha(0.25f * progress, 0).setScale(0.125f, 0.0625f).setLifetime(20)
                    .setColor(1.0f, 0.5f, 0.25f, 1.0f, 0.25f, 0.375f)
                    .spawn(world, x, y, z);
            }
            findingCounter ++;
            if (findingCounter == 80) {
                Ritual r = RitualRegistry.find(world, pos, stack);
                stack = ItemStack.EMPTY;
                findingCounter = 81;
                setRitual(r);
            }
        }
        if (burning && ritual != null && !ritualDone) {
            stepCounter ++;
            if (stepCounter == 40) {
                SetupResult result = ritual.setup(world, pos, step);
                if (result == SetupResult.SUCCEED) {
                    ritualDone = true;
                    if (!world.isRemote) sync();
                    if (ritual.start(world, pos) == RitualResult.TERMINATE) complete();
                }
                else if (result == SetupResult.FAIL && !world.isRemote) extinguish();
                else if (!world.isRemote) {
                    stepCounter = 0;
                    step ++;
                    sync();
                }
            }
        }
        if (burning && ritual != null && ritualDone) {
            if (ritual.tick(world, pos) == RitualResult.TERMINATE) complete();
        }
        if (world.isRemote && burning) {
            float x = getPos().getX() + 0.5f, y = getPos().getY() + 1, z = getPos().getZ() + 0.5f;
            float r = ritual == null ? 1.0f : ritual.getRed();
            float g = ritual == null ? 0.5f : ritual.getGreen();
            float b = ritual == null ? 0.25f : ritual.getBlue();
            Particles.create(Registry.FLAME_PARTICLE)
                .setAlpha(0.5f, 0).setScale(0.3125f, 0.125f).setLifetime(20)
                .randomOffset(0.25, 0.125).randomVelocity(0.00625f, 0.01875f)
                .addVelocity(0, 0.00625f, 0)
                .setColor(r, g, b, r, g * 0.5f, b * 1.5f)
                .spawn(world, x, y, z);
            if (world.rand.nextInt(5) == 0) Particles.create(Registry.SMOKE_PARTICLE)
                .setAlpha(0.125f, 0).setScale(0.375f, 0.125f).setLifetime(80)
                .randomOffset(0.25, 0.125).randomVelocity(0.025f, 0.025f)
                .addVelocity(0, 0.1f, 0)
                .setColor(0.5f, 0.5f, 0.5f, 0.25f, 0.25f, 0.25f)
                .spawn(world, x, y + 0.125, z);
            if (world.rand.nextInt(40) == 0) Particles.create(Registry.SPARKLE_PARTICLE)
                .setAlpha(1, 0).setScale(0.0625f, 0).setLifetime(40)
                .randomOffset(0.0625, 0).randomVelocity(0.125f, 0)
                .addVelocity(0, 0.125f, 0)
                .setColor(r, g * 1.5f, b * 2, r, g, b)
                .enableGravity().setSpin(0.4f)
                .spawn(world, x, y, z);
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 4, pos.getZ() + 1);
    }
}
