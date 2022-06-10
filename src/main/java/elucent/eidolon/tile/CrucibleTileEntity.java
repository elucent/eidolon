package elucent.eidolon.tile;

import elucent.eidolon.Registry;
import elucent.eidolon.network.CrucibleFailPacket;
import elucent.eidolon.network.CrucibleSuccessPacket;
import elucent.eidolon.network.Networking;
import elucent.eidolon.particle.Particles;
import elucent.eidolon.recipe.CrucibleRecipe;
import elucent.eidolon.recipe.CrucibleRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class CrucibleTileEntity extends TileEntityBase implements TickBlockEntity {
    public boolean boiling = false;
    public boolean hasWater = false;
    public int stirTicks = 0;
    public int stirs = 0;
    public int stepCounter = 0;
    public List<CrucibleStep> steps = new ArrayList<>();
    public long seed = 0;
    public Random random = new Random();

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

        public CrucibleStep(CompoundTag nbt) {
            stirs = nbt.getInt("stirs");
            ListTag list = nbt.getList("contents", Tag.TAG_COMPOUND);
            for (Tag item : list) contents.add(ItemStack.of((CompoundTag)item));
        }

        public CompoundTag write() {
            CompoundTag nbt = new CompoundTag();
            nbt.putInt("stirs", stirs);
            ListTag list = new ListTag();
            for (ItemStack stack : contents) list.add(stack.save(new CompoundTag()));
            nbt.put("contents", list);
            return nbt;
        }
    }

    public static Predicate<?>[] HOT_BLOCKS = {
        (BlockState b) -> b.getBlock() == Blocks.MAGMA_BLOCK,
        (BlockState b) -> b.getBlock() == Blocks.FIRE,
        (BlockState b) -> b.getBlock() == Blocks.SOUL_FIRE,
        (BlockState b) -> b.getBlock() == Blocks.LAVA,
        (BlockState b) -> b.getBlock() == Blocks.CAMPFIRE && b.getValue(CampfireBlock.LIT),
        (BlockState b) -> b.getBlock() == Blocks.SOUL_CAMPFIRE && b.getValue(CampfireBlock.LIT)
    };

    public CrucibleTileEntity(BlockPos pos, BlockState state) {
        super(Registry.CRUCIBLE_TILE_ENTITY.get(), pos, state);
    }

    @Override
    public InteractionResult onActivated(BlockState state, BlockPos pos, Player player, InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND) {
            if (player.isShiftKeyDown() && player.getItemInHand(hand).isEmpty() && hasWater) {
                boiling = false;
                hasWater = false;
                stirs = 0;
                steps.clear();
                if (!level.isClientSide) {
                    sync();
                    level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
                }
                return InteractionResult.SUCCESS;
            }
            else if (player.getItemInHand(hand).isEmpty() && stirTicks == 0 && this.steps.size() > 0) {
                stirs ++;
                stirTicks = 20;
                if (!level.isClientSide) {
                    level.playSound(null, pos, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 1.0f, 1.0f);
                    sync();
                }
                return InteractionResult.SUCCESS;
            }
            if (player.getItemInHand(hand).getItem() == Items.WATER_BUCKET) {
                player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                hasWater = true;
                if (!level.isClientSide) {
                    sync();
                    level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.steps.clear();
        ListTag steps = tag.getList("steps", Tag.TAG_COMPOUND);
        for (Tag step : steps) this.steps.add(new CrucibleStep((CompoundTag)step));
        boiling = tag.getBoolean("boiling");
        hasWater = tag.getBoolean("hasWater");
        stirs = tag.getInt("stirs");
        stirTicks = tag.getInt("stirTicks");
        seed = steps.stream().map((step) -> step.hashCode()).reduce(0, (a, b) -> a ^ b);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ListTag steps = new ListTag();
        for (CrucibleStep step : this.steps) steps.add(step.write());
        tag.put("steps", steps);
        tag.putBoolean("boiling", boiling);
        tag.putBoolean("hasWater", hasWater);
        tag.putInt("stirs", stirs);
        tag.putInt("stirTicks", stirTicks);
    }

    @Override
    public void tick() {
        if (stirTicks > 0) stirTicks --;

        if (hasWater && level.getGameTime() % 200 == 0) {
            BlockState state = level.getBlockState(worldPosition.below());
            boolean isHeated = false;
            for (Predicate pred : HOT_BLOCKS) if (pred.test(state)) isHeated = true;
            if (boiling && !isHeated) {
                boiling = false;
                if (!level.isClientSide) sync();
            }
            else if (!boiling && isHeated) {
                boiling = true;
                if (!level.isClientSide) sync();
            }
        }

        float bubbleR = steps.size() > 0 ? Math.min(1.0f, getRed() * 1.25f) : 0.25f;
        float bubbleG = steps.size() > 0 ? Math.min(1.0f, getGreen() * 1.25f) : 0.5f;
        float bubbleB = steps.size() > 0 ? Math.min(1.0f, getBlue() * 1.25f) : 1.0f;
        float steamR = steps.size() > 0 ? Math.min(1.0f, 1 - (float)Math.pow(1 - getRed(), 2)) : 1.0f;
        float steamG = steps.size() > 0 ? Math.min(1.0f, 1 - (float)Math.pow(1 - getGreen(), 2)) : 1.0f;
        float steamB = steps.size() > 0 ? Math.min(1.0f, 1 - (float)Math.pow(1 - getBlue(), 2)) : 1.0f;

        if (level.isClientSide && hasWater && boiling) for (int i = 0; i < 2; i ++){
            Particles.create(Registry.BUBBLE_PARTICLE)
                .setScale(0.05f)
                .setLifetime(10)
                .addVelocity(0, 0.015625, 0)
                .setColor(bubbleR, bubbleG, bubbleB)
                .setAlpha(1.0f, 0.75f)
                .spawn(level, worldPosition.getX() + 0.125 + 0.75 * level.random.nextFloat(), worldPosition.getY() + 0.6875, worldPosition.getZ() + 0.125 + 0.75 * level.random.nextFloat());
            if (level.random.nextInt(8) == 0) Particles.create(Registry.STEAM_PARTICLE)
                .setAlpha(0.0625f, 0).setScale(0.375f, 0.125f).setLifetime(80)
                .randomOffset(0.375, 0.125).randomVelocity(0.0125f, 0.025f)
                .addVelocity(0, 0.05f, 0)
                .setColor(steamR, steamG, steamB)
                .spawn(level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.625, worldPosition.getZ() + 0.5);
        }

        if (!level.isClientSide && boiling && hasWater && level.getGameTime() % 8 == 0) {
            List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, new AABB(worldPosition).deflate(0.125));
            for (ItemEntity item : items) item.setPickUpDelay(20);
        }

        if (!level.isClientSide && stepCounter > 0) {
            -- stepCounter;
            if (stepCounter == 0) {
                List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, new AABB(worldPosition).deflate(0.125));
                List<ItemStack> contents = new ArrayList<>();
                for (ItemEntity item : items) {
                    for (int i = 0; i < item.getItem().getCount(); i ++) {
                        ItemStack stack = item.getItem().copy();
                        stack.setCount(1);
                        contents.add(stack);
                    }
                    item.remove(Entity.RemovalReason.DISCARDED);
                }
                if (stirs == 0 && contents.isEmpty()) { // no action done; end recipe
                    Networking.sendToTracking(level, worldPosition, new CrucibleFailPacket(worldPosition));
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
                        Networking.sendToTracking(level, worldPosition, new CrucibleSuccessPacket(worldPosition, steamR, steamG, steamB));
                        double angle = level.random.nextDouble() * Math.PI * 2;
                        ItemEntity entity = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.75, worldPosition.getZ() + 0.5, recipe.getResult().copy());
                        entity.setDeltaMovement(Math.sin(angle) * 0.125, 0.25, Math.cos(angle) * 0.125);
                        entity.setPickUpDelay(10);
                        level.addFreshEntity(entity);
                        contents.clear();
                        steps.clear();
                        hasWater = boiling = false;
                    }
                    else {
                        level.playSound(null, worldPosition, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0f, 1.0f); // try continue
                        stepCounter = 100;
                    }
                    stirs = 0;
                    sync();
                }
            }
        }

        if (!level.isClientSide && stepCounter == 0 && steps.size() == 0
            && hasWater && boiling && level.getGameTime() % 100 == 0) {
            List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, new AABB(worldPosition).deflate(0.125));
            if (items.size() > 0) stepCounter = 40;
        }
    }
}
