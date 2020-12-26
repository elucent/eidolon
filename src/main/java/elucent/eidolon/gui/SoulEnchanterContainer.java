package elucent.eidolon.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Lists;

import elucent.eidolon.Registry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

public class SoulEnchanterContainer extends Container {
    private final IInventory tableInventory = new Inventory(2) {
        public void markDirty() {
            super.markDirty();
            SoulEnchanterContainer.this.onCraftMatrixChanged(this);
        }
    };

    private final IWorldPosCallable worldPosCallable;
    private final Random rand = new Random();
    private final IntReferenceHolder xpSeed = IntReferenceHolder.single();
    public final int[] enchantClue = new int[]{-1, -1, -1};
    public final int[] worldClue = new int[]{-1, -1, -1};

    public SoulEnchanterContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, IWorldPosCallable.DUMMY);
    }

    public SoulEnchanterContainer(int id, PlayerInventory playerInventory, IWorldPosCallable worldPosCallable) {
        super(Registry.SOUL_ENCHANTER_CONTAINER.get(), id);
        this.worldPosCallable = worldPosCallable;
        this.addSlot(new Slot(this.tableInventory, 0, 15, 47) {
            public boolean isItemValid(ItemStack stack) {
                return true;
            }
            public int getSlotStackLimit() {
                return 1;
            }
        });
        this.addSlot(new Slot(this.tableInventory, 1, 35, 47) {
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() == Registry.SOUL_SHARD.get();
            }
        });

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

        this.trackInt(this.xpSeed).set(playerInventory.player.getXPSeed());
        this.trackInt(IntReferenceHolder.create(this.enchantClue, 0));
        this.trackInt(IntReferenceHolder.create(this.enchantClue, 1));
        this.trackInt(IntReferenceHolder.create(this.enchantClue, 2));
        this.trackInt(IntReferenceHolder.create(this.worldClue, 0));
        this.trackInt(IntReferenceHolder.create(this.worldClue, 1));
        this.trackInt(IntReferenceHolder.create(this.worldClue, 2));
    }

    private float getPower(World world, BlockPos pos) {
        return world.getBlockState(pos).getEnchantPowerBonus(world, pos);
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        if (inventoryIn == this.tableInventory) {
            ItemStack itemstack = inventoryIn.getStackInSlot(0);
            if (!itemstack.isEmpty() && (itemstack.isEnchantable() || itemstack.isEnchanted() || itemstack.getItem() == Items.ENCHANTED_BOOK)) {
                this.worldPosCallable.consume((world, pos) -> {
                    int power = 0;

                    for(int k = -1; k <= 1; ++k) {
                        for(int l = -1; l <= 1; ++l) {
                            if ((k != 0 || l != 0) && world.isAirBlock(pos.add(l, 0, k)) && world.isAirBlock(pos.add(l, 1, k))) {
                                power += getPower(world, pos.add(l * 2, 0, k * 2));
                                power += getPower(world, pos.add(l * 2, 1, k * 2));

                                if (l != 0 && k != 0) {
                                    power += getPower(world, pos.add(l * 2, 0, k));
                                    power += getPower(world, pos.add(l * 2, 1, k));
                                    power += getPower(world, pos.add(l, 0, k * 2));
                                    power += getPower(world, pos.add(l, 1, k * 2));
                                }
                            }
                        }
                    }

                    this.rand.setSeed((long)xpSeed.get());

                    for(int i1 = 0; i1 < 3; ++i1) {
                        enchantClue[i1] = -1;
                        worldClue[i1] = -1;
                    }

                    for(int j1 = 0; j1 < 3; ++j1) {
                        List<EnchantmentData> list = getEnchantmentList(itemstack, j1);
                        if (list != null && !list.isEmpty()) {
                            EnchantmentData enchantmentdata = list.get(rand.nextInt(list.size()));
                            enchantClue[j1] = net.minecraft.util.registry.Registry.ENCHANTMENT.getId(enchantmentdata.enchantment);
                            worldClue[j1] = enchantmentdata.enchantmentLevel;
                        }
                    }

                    this.detectAndSendChanges();
                });
            } else {
                for(int i = 0; i < 3; ++i) {
                    this.enchantClue[i] = -1;
                    this.worldClue[i] = -1;
                }
            }
        }

    }

    /**
     * Handles the given Button-click on the server, currently only used by enchanting. Name is for legacy.
     */
    public boolean enchantItem(PlayerEntity playerIn, int id) {
        ItemStack itemstack = this.tableInventory.getStackInSlot(0);
        ItemStack itemstack1 = this.tableInventory.getStackInSlot(1);
        int i = id + 1;
        if ((itemstack1.isEmpty() || itemstack1.getCount() < worldClue[id]) && !playerIn.abilities.isCreativeMode) {
            return false;
        } else if (itemstack.isEmpty() || playerIn.experienceLevel < this.worldClue[id] && !playerIn.abilities.isCreativeMode) {
            return false;
        } else {
            this.worldPosCallable.consume((p_217003_6_, p_217003_7_) -> {
                ItemStack itemstack2 = itemstack;
                List<EnchantmentData> list = this.getEnchantmentList(itemstack, id);
                if (!list.isEmpty()) {
                    playerIn.onEnchant(itemstack, worldClue[id]);
                    boolean flag = itemstack.getItem() == Items.BOOK;
                    if (flag) {
                        itemstack2 = new ItemStack(Items.ENCHANTED_BOOK);
                        CompoundNBT compoundnbt = itemstack.getTag();
                        if (compoundnbt != null) {
                            itemstack2.setTag(compoundnbt.copy());
                        }

                        this.tableInventory.setInventorySlotContents(0, itemstack2);
                    }

                    Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(itemstack2);
                    if (enchants.size() > 0) {
                        for (int j = 0; j < list.size(); ++ j) {
                            EnchantmentData data = list.get(j);
                            if (enchants.containsKey(data.enchantment)) enchants.replace(data.enchantment, data.enchantmentLevel);
                            else enchants.put(data.enchantment, data.enchantmentLevel);
                        }
                        EnchantmentHelper.setEnchantments(enchants, itemstack2);
                    }
                    else for(int j = 0; j < list.size(); ++j) {
                        EnchantmentData data = list.get(j);
                        if (flag) {
                            EnchantedBookItem.addEnchantment(itemstack2, data);
                        } else {
                            itemstack2.addEnchantment(data.enchantment, data.enchantmentLevel);
                        }
                    }

                    if (!playerIn.abilities.isCreativeMode) {
                        itemstack1.shrink(1);
                        if (itemstack1.isEmpty()) {
                            this.tableInventory.setInventorySlotContents(1, ItemStack.EMPTY);
                        }
                    }

                    playerIn.addStat(Stats.ENCHANT_ITEM);
                    if (playerIn instanceof ServerPlayerEntity) {
                        CriteriaTriggers.ENCHANTED_ITEM.trigger((ServerPlayerEntity)playerIn, itemstack2, i);
                    }

                    this.tableInventory.markDirty();
                    this.xpSeed.set(playerIn.getXPSeed());
                    this.onCraftMatrixChanged(this.tableInventory);
                    p_217003_6_.playSound((PlayerEntity)null, p_217003_7_, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, p_217003_6_.rand.nextFloat() * 0.1F + 0.7F);
                }
            });
            return true;
        }
    }

    public static int getEnchantmentLevel(Enchantment enchID, ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        } else {
            ResourceLocation resourcelocation = net.minecraft.util.registry.Registry.ENCHANTMENT.getKey(enchID);
            ListNBT listnbt = stack.getItem() == Items.ENCHANTED_BOOK ? EnchantedBookItem.getEnchantments(stack) : stack.getEnchantmentTagList();

            for(int i = 0; i < listnbt.size(); ++i) {
                CompoundNBT compoundnbt = listnbt.getCompound(i);
                ResourceLocation resourcelocation1 = ResourceLocation.tryCreate(compoundnbt.getString("id"));
                if (resourcelocation1 != null && resourcelocation1.equals(resourcelocation)) {
                    return MathHelper.clamp(compoundnbt.getInt("lvl"), 0, 255);
                }
            }

            return 0;
        }
    }

    private List<EnchantmentData> getEnchantmentList(ItemStack stack, int enchantSlot) {
        this.rand.setSeed((long)(this.xpSeed.get() + enchantSlot));
        ItemStack test = stack.copy();
        EnchantmentHelper.setEnchantments(new HashMap<>(), test);
        if (test.getItem() == Items.ENCHANTED_BOOK) test = new ItemStack(Items.BOOK);
        final ItemStack finalTest = test;

        Map<Enchantment, Integer> existing = EnchantmentHelper.getEnchantments(stack);
        List<Enchantment> valid = Lists.newArrayList(ForgeRegistries.ENCHANTMENTS.getValues());
        valid.removeIf((ench) -> {
            boolean canApply = ench.canApplyAtEnchantingTable(finalTest) ||
                finalTest.getItem() == Items.BOOK && ench.isAllowedOnBooks();
            return !canApply || ench.isTreasureEnchantment() || existing.containsKey(ench) && existing.get(ench) >= ench.getMaxLevel();
        });

        for (Map.Entry<Enchantment, Integer> e : existing.entrySet()) {
            Iterator<Enchantment> iterator = valid.iterator();

            while(iterator.hasNext()) {
                Enchantment next = iterator.next();
                if (!e.getKey().isCompatibleWith(next) && e.getKey() != next) {
                    iterator.remove();
                }
            }
        }

        List<EnchantmentData> enchants = new ArrayList<>();
        if (valid.isEmpty()) return enchants;
        // System.out.println("" + enchantSlot + ": " + valid.stream().reduce("", (a, b) -> "" + a + ", " + b, (a, b) -> "" + a + ", " + b));
        for (int i = 0; i < enchantSlot; i ++) rand.nextInt(valid.size());
        Enchantment enchant = valid.get(this.rand.nextInt(valid.size()));
        int level = getEnchantmentLevel(enchant, stack);
        if (level > 0) enchants.add(new EnchantmentData(enchant, level + 1));
        else enchants.add(new EnchantmentData(enchant, 1));

        return enchants;
    }

    @OnlyIn(Dist.CLIENT)
    public int getSoulShardAmount() {
        ItemStack itemstack = this.tableInventory.getStackInSlot(1);
        return itemstack.isEmpty() ? 0 : itemstack.getCount();
    }

    @OnlyIn(Dist.CLIENT)
    public int getXPSeed() {
        return this.xpSeed.get();
    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        this.worldPosCallable.consume((world, pos) -> {
            this.clearContainer(playerIn, playerIn.world, this.tableInventory);
        });
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(this.worldPosCallable, playerIn, Registry.SOUL_ENCHANTER.get());
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index == 0) {
                if (!this.mergeItemStack(itemstack1, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index == 1) {
                if (!this.mergeItemStack(itemstack1, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (itemstack1.getItem() == Registry.SOUL_SHARD.get()) {
                if (!this.mergeItemStack(itemstack1, 1, 2, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (this.inventorySlots.get(0).getHasStack() || !this.inventorySlots.get(0).isItemValid(itemstack1)) {
                    return ItemStack.EMPTY;
                }

                ItemStack itemstack2 = itemstack1.copy();
                itemstack2.setCount(1);
                itemstack1.shrink(1);
                this.inventorySlots.get(0).putStack(itemstack2);
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }
}
