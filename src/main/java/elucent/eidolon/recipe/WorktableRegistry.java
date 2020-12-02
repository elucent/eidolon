package elucent.eidolon.recipe;

import com.sun.corba.se.spi.orbutil.threadpool.Work;
import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.tile.CrucibleTileEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ForgeItemTagsProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorktableRegistry {
    static Map<ResourceLocation, WorktableRecipe> recipes = new HashMap<>();

    public static void register(WorktableRecipe recipe) {
        ResourceLocation loc = recipe.getRegistryName();
        assert loc != null;
        recipes.put(loc, recipe);
    }

    public static WorktableRecipe find(ResourceLocation loc) {
        return recipes.get(loc);
    }

    public static WorktableRecipe find(IInventory core, IInventory outer) {
        for (WorktableRecipe recipe : recipes.values()) if (recipe.matches(core, outer)) return recipe;
        return null;
    }

    public static void init() {
        register(new WorktableRecipe(new Object[]{
            Registry.PEWTER_INGOT.get(), Registry.PEWTER_INGOT.get(), ItemStack.EMPTY,
            ItemStack.EMPTY, Items.STICK, Registry.PEWTER_INGOT.get(),
            Items.STICK, ItemStack.EMPTY, ItemStack.EMPTY
        }, new Object[]{
            Registry.UNHOLY_SYMBOL.get(),
            Registry.SOUL_SHARD.get(),
            Registry.TATTERED_CLOTH.get(),
            Registry.SOUL_SHARD.get()
        }, new ItemStack(Registry.REAPER_SCYTHE.get())).setRegistryName(Eidolon.MODID, "reaper_scythe"));
        register(new WorktableRecipe(new Object[]{
            Registry.PEWTER_INGOT.get(), Registry.PEWTER_INGOT.get(), ItemStack.EMPTY,
            Registry.PEWTER_INGOT.get(), Items.STICK, ItemStack.EMPTY,
            ItemStack.EMPTY, Items.STICK, ItemStack.EMPTY
        }, new Object[]{
            Registry.UNHOLY_SYMBOL.get(),
            ItemStack.EMPTY,
            Registry.PEWTER_INLAY.get(),
            ItemStack.EMPTY
        }, new ItemStack(Registry.CLEAVING_AXE.get())).setRegistryName(Eidolon.MODID, "cleaving_axe"));
        register(new WorktableRecipe(new Object[]{
            ItemStack.EMPTY, Tags.Items.INGOTS_GOLD, Registry.SHADOW_GEM.get(),
            ItemStack.EMPTY, Items.STICK, Tags.Items.INGOTS_GOLD,
            Registry.GOLD_INLAY.get(), ItemStack.EMPTY, ItemStack.EMPTY
        }, new Object[]{
            Registry.LESSER_SOUL_GEM.get(),
            Items.BLAZE_POWDER,
            Items.BLAZE_POWDER,
            Items.BLAZE_POWDER
        }, new ItemStack(Registry.SOULFIRE_WAND.get())).setRegistryName(Eidolon.MODID, "soulfire_wand"));
        register(new WorktableRecipe(new Object[]{
            ItemStack.EMPTY, Registry.PEWTER_INGOT.get(), Registry.WRAITH_HEART.get(),
            ItemStack.EMPTY, Items.STICK, Registry.PEWTER_INGOT.get(),
            Registry.PEWTER_INLAY.get(), ItemStack.EMPTY, ItemStack.EMPTY
        }, new Object[]{
            Registry.LESSER_SOUL_GEM.get(),
            Registry.TATTERED_CLOTH.get(),
            Registry.TATTERED_CLOTH.get(),
            Registry.TATTERED_CLOTH.get()
        }, new ItemStack(Registry.BONECHILL_WAND.get())).setRegistryName(Eidolon.MODID, "bonechill_wand"));
    }
}
