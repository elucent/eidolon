package elucent.eidolon.tile;

import elucent.eidolon.Registry;
import elucent.eidolon.network.CrucibleFailPacket;
import elucent.eidolon.network.CrucibleSuccessPacket;
import elucent.eidolon.network.Networking;
import elucent.eidolon.particle.Particles;
import elucent.eidolon.recipe.CrucibleRecipe;
import elucent.eidolon.recipe.CrucibleRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class CrucibleTileEntity extends TileEntityBase implements ITickableTileEntity {
    boolean boiling = false;
    boolean hasWater = false;
    int stirTicks = 0;
    int stirs = 0;
    int stepCounter = 0;
    List<CrucibleStep> steps = new ArrayList<>();
    long seed = 0;
    Random random = new Random();

    public float getRed() {
        random.setSeed(seed);
        return random.nextFloat();
    }

    public float getGreen() {
        random.setSeed(seed * 2);
        return random.nextFloat();
    }

    public float getBlue() {
        random.setSeed(seed * 3);
        return random.nextFloat();
    }

    public static class CrucibleStep {
        int stirs;
        List<ItemStack> contents = new ArrayList<>();

        public CrucibleStep(int stirs, List<ItemStack> contents) {
            this.stirs = stirs;
            this.contents.addAll(contents);
        }

        public int getStirs() {
            return stirs;
        }

        public List<ItemStack> getContents() {
            return contents;
        }

        public CrucibleStep(CompoundNBT nbt) {
            stirs = nbt.getInt("stirs");
            ListNBT list = nbt.getList("contents", Constants.NBT.TAG_COMPOUND);
            for (INBT item : list) contents.add(ItemStack.read((CompoundNBT)item));
        }

        public CompoundNBT write() {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("stirs", stirs);
            ListNBT list = new ListNBT();
            for (ItemStack stack : contents) list.add(stack.write(new CompoundNBT()));
            nbt.put("contents", list);
            return nbt;
        }
    }

    public static Predicate<?>[] HOT_BLOCKS = {
        (BlockState b) -> b.getBlock() == Blocks.MAGMA_BLOCK,
        (BlockState b) -> b.getBlock() == Blocks.FIRE,
        (BlockState b) -> b.getBlock() == Blocks.SOUL_FIRE,
        (BlockState b) -> b.getBlock() == Blocks.LAVA,
        (BlockState b) -> b.getBlock() == Blocks.CAMPFIRE && b.get(CampfireBlock.LIT),
        (BlockState b) -> b.getBlock() == Blocks.SOUL_CAMPFIRE && b.get(CampfireBlock.LIT)
    };

    public CrucibleTileEntity() {
        this(Registry.CRUCIBLE_TILE_ENTITY);
    }

    public CrucibleTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public ActionResultType onActivated(BlockState state, BlockPos pos, PlayerEntity player, Hand hand) {
        if (hand == Hand.MAIN_HAND) {
            if (player.isSneaking() && player.getHeldItem(hand).isEmpty() && hasWater) {
                boiling = false;
                hasWater = false;
                stirs = 0;
                steps.clear();
                if (!world.isRemote) {
                    sync();
                    world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
                }
                return ActionResultType.SUCCESS;
            }
            else if (player.getHeldItem(hand).isEmpty() && stirTicks == 0 && this.steps.size() > 0) {
                stirs ++;
                stirTicks = 20;
                if (!world.isRemote) {
                    world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    sync();
                }
                return ActionResultType.SUCCESS;
            }
            if (player.getHeldItem(hand).getItem() == Items.WATER_BUCKET) {
                player.setHeldItem(hand, new ItemStack(Items.BUCKET));
                hasWater = true;
                if (!world.isRemote) {
                    sync();
                    world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        this.steps.clear();
        ListNBT steps = tag.getList("steps", Constants.NBT.TAG_COMPOUND);
        for (INBT step : steps) this.steps.add(new CrucibleStep((CompoundNBT)step));
        boiling = tag.getBoolean("boiling");
        hasWater = tag.getBoolean("hasWater");
        stirs = tag.getInt("stirs");
        stirTicks = tag.getInt("stirTicks");
        seed = steps.stream().map((step) -> step.hashCode()).reduce(0, (a, b) -> a ^ b);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        ListNBT steps = new ListNBT();
        for (CrucibleStep step : this.steps) steps.add(step.write());
        tag.put("steps", steps);
        tag.putBoolean("boiling", boiling);
        tag.putBoolean("hasWater", hasWater);
        tag.putInt("stirs", stirs);
        tag.putInt("stirTicks", stirTicks);
        return tag;
    }

    @Override
    public void tick() {
        if (stirTicks > 0) stirTicks --;

        if (hasWater && world.getGameTime() % 200 == 0) {
            BlockState state = world.getBlockState(pos.down());
            boolean isHeated = false;
            for (Predicate pred : HOT_BLOCKS) if (pred.test(state)) isHeated = true;
            if (boiling && !isHeated) {
                boiling = false;
                if (!world.isRemote) sync();
            }
            else if (!boiling && isHeated) {
                boiling = true;
                if (!world.isRemote) sync();
            }
        }

        float bubbleR = steps.size() > 0 ? Math.min(1.0f, getRed() * 1.25f) : 0.25f;
        float bubbleG = steps.size() > 0 ? Math.min(1.0f, getGreen() * 1.25f) : 0.5f;
        float bubbleB = steps.size() > 0 ? Math.min(1.0f, getBlue() * 1.25f) : 1.0f;
        float steamR = steps.size() > 0 ? Math.min(1.0f, 1 - (float)Math.pow(1 - getRed(), 2)) : 1.0f;
        float steamG = steps.size() > 0 ? Math.min(1.0f, 1 - (float)Math.pow(1 - getGreen(), 2)) : 1.0f;
        float steamB = steps.size() > 0 ? Math.min(1.0f, 1 - (float)Math.pow(1 - getBlue(), 2)) : 1.0f;

        if (world.isRemote && hasWater && boiling) for (int i = 0; i < 2; i ++){
            Particles.create(Registry.BUBBLE_PARTICLE)
                .setScale(0.05f)
                .setLifetime(10)
                .addVelocity(0, 0.015625, 0)
                .setColor(bubbleR, bubbleG, bubbleB)
                .setAlpha(1.0f, 0.75f)
                .spawn(world, pos.getX() + 0.125 + 0.75 * world.rand.nextFloat(), pos.getY() + 0.6875, pos.getZ() + 0.125 + 0.75 * world.rand.nextFloat());
            if (world.rand.nextInt(8) == 0) Particles.create(Registry.STEAM_PARTICLE)
                .setAlpha(0.0625f, 0).setScale(0.375f, 0.125f).setLifetime(80)
                .randomOffset(0.375, 0.125).randomVelocity(0.0125f, 0.025f)
                .addVelocity(0, 0.05f, 0)
                .setColor(steamR, steamG, steamB)
                .spawn(world, pos.getX() + 0.5, pos.getY() + 0.625, pos.getZ() + 0.5);
        }

        if (!world.isRemote && boiling && hasWater && world.getGameTime() % 8 == 0) {
            List<ItemEntity> items = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos).shrink(0.125));
            for (ItemEntity item : items) item.setPickupDelay(20);
        }

        if (!world.isRemote && stepCounter > 0) {
            -- stepCounter;
            if (stepCounter == 0) {
                List<ItemEntity> items = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos).shrink(0.125));
                List<ItemStack> contents = new ArrayList<>();
                for (ItemEntity item : items) {
                    for (int i = 0; i < item.getItem().getCount(); i ++) {
                        ItemStack stack = item.getItem().copy();
                        stack.setCount(1);
                        contents.add(stack);
                    }
                    item.remove();
                }
                if (stirs == 0 && contents.isEmpty()) { // no action done; end recipe
                    Networking.sendToTracking(world, pos, new CrucibleFailPacket(pos));
                    contents.clear();
                    steps.clear();
                    stirs = 0;
                    hasWater = boiling = false;
                    sync();
                }
                else {
                    CrucibleStep step = new CrucibleStep(stirs, contents);
                    steps.add(step);

                    CrucibleRecipe recipe = CrucibleRegistry.find(steps);
                    if (recipe != null) { // if recipe found
                        Networking.sendToTracking(world, pos, new CrucibleSuccessPacket(pos, steamR, steamG, steamB));
                        double angle = world.rand.nextDouble() * Math.PI * 2;
                        ItemEntity entity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.75, pos.getZ() + 0.5, recipe.getResult().copy());
                        entity.setMotion(Math.sin(angle) * 0.125, 0.25, Math.cos(angle) * 0.125);
                        entity.setPickupDelay(10);
                        world.addEntity(entity);
                        contents.clear();
                        steps.clear();
                        hasWater = boiling = false;
                    }
                    else {
                        world.playSound(null, pos, SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.BLOCKS, 1.0f, 1.0f); // try continue
                        stepCounter = 100;
                    }
                    stirs = 0;
                    sync();
                }
            }
        }

        if (!world.isRemote && stepCounter == 0 && steps.size() == 0
            && hasWater && boiling && world.getGameTime() % 100 == 0) {
            List<ItemEntity> items = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos).shrink(0.125));
            if (items.size() > 0) stepCounter = 40;
        }
    }
}
