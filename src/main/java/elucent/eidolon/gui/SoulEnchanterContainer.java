package elucent.eidolon.gui;

import com.google.common.collect.Lists;
import elucent.eidolon.Registry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class SoulEnchanterContainer extends AbstractContainerMenu {
    private final Container tableInventory = new SimpleContainer(2) {
        @Override
        public void setChanged() {
            super.setChanged();
            SoulEnchanterContainer.this.slotsChanged(this);
        }
    };

    private final ContainerLevelAccess worldPosCallable;
    private final Random rand = new Random();
    private final DataSlot xpSeed = DataSlot.standalone();
    public final int[] enchantClue = new int[]{-1, -1, -1};
    public final int[] worldClue = new int[]{-1, -1, -1};

    public SoulEnchanterContainer(int id, Inventory playerInventory) {
        this(id, playerInventory, ContainerLevelAccess.NULL);
    }

    public SoulEnchanterContainer(int id, Inventory playerInventory, ContainerLevelAccess worldPosCallable) {
        super(Registry.SOUL_ENCHANTER_CONTAINER.get(), id);
        this.worldPosCallable = worldPosCallable;
        this.addSlot(new Slot(this.tableInventory, 0, 15, 47) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return true;
            }
            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });
        this.addSlot(new Slot(this.tableInventory, 1, 35, 47) {
            @Override
            public boolean mayPlace(ItemStack stack) {
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

        this.addDataSlot(this.xpSeed).set(playerInventory.player.getEnchantmentSeed());
        this.addDataSlot(DataSlot.shared(this.enchantClue, 0));
        this.addDataSlot(DataSlot.shared(this.enchantClue, 1));
        this.addDataSlot(DataSlot.shared(this.enchantClue, 2));
        this.addDataSlot(DataSlot.shared(this.worldClue, 0));
        this.addDataSlot(DataSlot.shared(this.worldClue, 1));
        this.addDataSlot(DataSlot.shared(this.worldClue, 2));
    }

    private float getPower(Level world, BlockPos pos) {
        return world.getBlockState(pos).getEnchantPowerBonus(world, pos);
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    public void slotsChanged(Container inventoryIn) {
        if (inventoryIn == this.tableInventory) {
            ItemStack itemstack = inventoryIn.getItem(0);
            if (!itemstack.isEmpty() && (itemstack.isEnchantable() || itemstack.isEnchanted() || itemstack.getItem() == Items.ENCHANTED_BOOK)) {
                this.worldPosCallable.execute((world, pos) -> {
                    int power = 0;

                    for(int k = -1; k <= 1; ++k) {
                        for(int l = -1; l <= 1; ++l) {
                            if ((k != 0 || l != 0) && world.isEmptyBlock(pos.offset(l, 0, k)) && world.isEmptyBlock(pos.offset(l, 1, k))) {
                                power += getPower(world, pos.offset(l * 2, 0, k * 2));
                                power += getPower(world, pos.offset(l * 2, 1, k * 2));

                                if (l != 0 && k != 0) {
                                    power += getPower(world, pos.offset(l * 2, 0, k));
                                    power += getPower(world, pos.offset(l * 2, 1, k));
                                    power += getPower(world, pos.offset(l, 0, k * 2));
                                    power += getPower(world, pos.offset(l, 1, k * 2));
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
                        List<EnchantmentInstance> list = getEnchantmentList(itemstack, j1);
                        if (list != null && !list.isEmpty()) {
                            EnchantmentInstance enchantmentdata = list.get(rand.nextInt(list.size()));
                            enchantClue[j1] = net.minecraft.core.Registry.ENCHANTMENT.getId(enchantmentdata.enchantment);
                            worldClue[j1] = enchantmentdata.level;
                        }
                    }

                    this.broadcastChanges();
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
    public boolean clickMenuButton(Player playerIn, int id) {
        ItemStack itemstack = this.tableInventory.getItem(0);
        ItemStack itemstack1 = this.tableInventory.getItem(1);
        int i = id + 1;
        if ((itemstack1.isEmpty() || itemstack1.getCount() < 1) && !playerIn.getAbilities().instabuild) {
            return false;
        } else if (itemstack.isEmpty() || playerIn.experienceLevel < this.worldClue[id] && !playerIn.getAbilities().instabuild) {
            return false;
        } else {
            this.worldPosCallable.execute((p_217003_6_, p_217003_7_) -> {
                ItemStack itemstack2 = itemstack;
                List<EnchantmentInstance> list = this.getEnchantmentList(itemstack, id);
                if (!list.isEmpty()) {
                    playerIn.onEnchantmentPerformed(itemstack, worldClue[id]);
                    boolean flag = itemstack.getItem() == Items.BOOK;
                    if (flag) {
                        itemstack2 = new ItemStack(Items.ENCHANTED_BOOK);
                        CompoundTag compoundnbt = itemstack.getTag();
                        if (compoundnbt != null) {
                            itemstack2.setTag(compoundnbt.copy());
                        }

                        this.tableInventory.setItem(0, itemstack2);
                    }

                    Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(itemstack2);
                    if (enchants.size() > 0) {
                        for (int j = 0; j < list.size(); ++ j) {
                            EnchantmentInstance data = list.get(j);
                            if (enchants.containsKey(data.enchantment)) enchants.replace(data.enchantment, data.level);
                            else enchants.put(data.enchantment, data.level);
                        }
                        EnchantmentHelper.setEnchantments(enchants, itemstack2);
                    }
                    else for(int j = 0; j < list.size(); ++j) {
                        EnchantmentInstance data = list.get(j);
                        if (flag) {
                            EnchantedBookItem.addEnchantment(itemstack2, data);
                        } else {
                            itemstack2.enchant(data.enchantment, data.level);
                        }
                    }

                    if (!playerIn.getAbilities().instabuild) {
                        itemstack1.shrink(1);
                        if (itemstack1.isEmpty()) {
                            this.tableInventory.setItem(1, ItemStack.EMPTY);
                        }
                    }

                    playerIn.awardStat(Stats.ENCHANT_ITEM);
                    if (playerIn instanceof ServerPlayer) {
                        CriteriaTriggers.ENCHANTED_ITEM.trigger((ServerPlayer)playerIn, itemstack2, i);
                    }

                    this.tableInventory.setChanged();
                    this.xpSeed.set(playerIn.getEnchantmentSeed());
                    this.slotsChanged(this.tableInventory);
                    p_217003_6_.playSound((Player)null, p_217003_7_, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, p_217003_6_.random.nextFloat() * 0.1F + 0.7F);
                }
            });
            return true;
        }
    }

    public static int getEnchantmentLevel(Enchantment enchID, ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        } else {
            ResourceLocation resourcelocation = net.minecraft.core.Registry.ENCHANTMENT.getKey(enchID);
            ListTag listnbt = stack.getItem() == Items.ENCHANTED_BOOK ? EnchantedBookItem.getEnchantments(stack) : stack.getEnchantmentTags();

            for(int i = 0; i < listnbt.size(); ++i) {
                CompoundTag compoundnbt = listnbt.getCompound(i);
                ResourceLocation resourcelocation1 = ResourceLocation.tryParse(compoundnbt.getString("id"));
                if (resourcelocation1 != null && resourcelocation1.equals(resourcelocation)) {
                    return Mth.clamp(compoundnbt.getInt("lvl"), 0, 255);
                }
            }

            return 0;
        }
    }

    private List<EnchantmentInstance> getEnchantmentList(ItemStack stack, int enchantSlot) {
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
            return !canApply || ench.isTreasureOnly() || existing.containsKey(ench) && existing.get(ench) >= ench.getMaxLevel();
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

        List<EnchantmentInstance> enchants = new ArrayList<>();
        if (valid.isEmpty()) return enchants;
        // System.out.println("" + enchantSlot + ": " + valid.stream().reduce("", (a, b) -> "" + a + ", " + b, (a, b) -> "" + a + ", " + b));
        for (int i = 0; i < enchantSlot; i ++) rand.nextInt(valid.size());
        Enchantment enchant = valid.get(this.rand.nextInt(valid.size()));
        int level = getEnchantmentLevel(enchant, stack);
        if (level > 0) enchants.add(new EnchantmentInstance(enchant, level + 1));
        else enchants.add(new EnchantmentInstance(enchant, 1));

        return enchants;
    }

    @OnlyIn(Dist.CLIENT)
    public int getSoulShardAmount() {
        ItemStack itemstack = this.tableInventory.getItem(1);
        return itemstack.isEmpty() ? 0 : itemstack.getCount();
    }

    @OnlyIn(Dist.CLIENT)
    public int getXPSeed() {
        return this.xpSeed.get();
    }

    /**
     * Called when the container is closed.
     */
    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        this.worldPosCallable.execute((world, pos) -> {
            this.clearContainer(playerIn, this.tableInventory);
        });
    }

    /**
     * Determines whether supplied player can use this container
     */
    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(this.worldPosCallable, playerIn, Registry.SOUL_ENCHANTER.get());
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index == 0) {
                if (!this.moveItemStackTo(itemstack1, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index == 1) {
                if (!this.moveItemStackTo(itemstack1, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (itemstack1.getItem() == Registry.SOUL_SHARD.get()) {
                if (!this.moveItemStackTo(itemstack1, 1, 2, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (this.slots.get(0).hasItem() || !this.slots.get(0).mayPlace(itemstack1)) {
                    return ItemStack.EMPTY;
                }

                ItemStack itemstack2 = itemstack1.copy();
                itemstack2.setCount(1);
                itemstack1.shrink(1);
                this.slots.get(0).set(itemstack2);
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }
}
