package elucent.eidolon.recipe;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.codex.Page;
import elucent.eidolon.codex.WorktablePage;
import elucent.eidolon.gui.jei.RecipeWrappers;
import elucent.eidolon.util.StackUtil;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorktableRegistry {
    public static Map<ResourceLocation, WorktableRecipe> recipes = new HashMap<>();

    public static void registerAll(WorktableRecipe... recipes) {
        for (WorktableRecipe recipe : recipes) {
            register(recipe);
        }
    }

    public static void register(WorktableRecipe recipe) {
        ResourceLocation loc = recipe.getRegistryName();
        assert loc != null;
        recipes.put(loc, recipe);
    }

    public static Page getDefaultPage(WorktableRecipe recipe) {
        List<ItemStack> stacks = new ArrayList<>();
        for (Object o : recipe.getCore()) stacks.add(StackUtil.stackFromObject(o));
        for (Object o : recipe.getOuter()) stacks.add(StackUtil.stackFromObject(o));
        return new WorktablePage(recipe.result.copy(), stacks.toArray(new ItemStack[0]));
    }

    public static List<RecipeWrappers.Worktable> getWrappedRecipes() {
        List<RecipeWrappers.Worktable> wrappers = new ArrayList<>();
        for (Map.Entry<ResourceLocation, WorktableRecipe> entry : recipes.entrySet()) {
            Page page = null; // linkedPages.getOrDefault(entry.getKey(), null);
            wrappers.add(new RecipeWrappers.Worktable(
                entry.getValue(),
                page
            ));
        }
        return wrappers;
    }

    public static WorktableRecipe find(World world, ResourceLocation loc) {
        IRecipe<?> recipe = world.getRecipeManager().getRecipe(loc).get();
        return recipe instanceof WorktableRecipe ? ((WorktableRecipe) recipe) : null;
    }

    public static WorktableRecipe find(World world, IInventory core, IInventory outer) {
        for (ICraftingRecipe recipe : world.getRecipeManager().getRecipesForType(IRecipeType.CRAFTING))
            if (recipe instanceof WorktableRecipe && ((WorktableRecipe) recipe).matches(core, outer))
                return (WorktableRecipe) recipe;
        return null;
    }

    public static void init() {
        registerAll(
                new WorktableRecipe(new WorktableRecipe.RecipeCore(
                        ItemStack.EMPTY, Registry.PEWTER_INGOT.get(), ItemStack.EMPTY,
                        Registry.PEWTER_INLAY.get(), Registry.BASIC_AMULET.get(), Registry.PEWTER_INLAY.get(),
                        ItemStack.EMPTY, Blocks.OBSIDIAN, ItemStack.EMPTY
                ), new WorktableRecipe.RecipeExtras(
                        Registry.SOUL_SHARD.get(),
                        ItemStack.EMPTY,
                        Registry.SOUL_SHARD.get(),
                        ItemStack.EMPTY
                ), new ItemStack(Registry.VOID_AMULET.get())).setRegistryName(Eidolon.MODID, "void_amulet"),

                new WorktableRecipe(new WorktableRecipe.RecipeCore(
                        ItemStack.EMPTY, Registry.LESSER_SOUL_GEM.get(), ItemStack.EMPTY,
                        Registry.ENCHANTED_ASH.get(), Items.IRON_CHESTPLATE, Registry.ENCHANTED_ASH.get(),
                        ItemStack.EMPTY, Registry.ENCHANTED_ASH.get(), ItemStack.EMPTY
                ), new WorktableRecipe.RecipeExtras(
                        Registry.PEWTER_INLAY.get(),
                        Registry.PEWTER_INLAY.get(),
                        Registry.PEWTER_INLAY.get(),
                        Registry.PEWTER_INLAY.get()
                ), new ItemStack(Registry.WARDED_MAIL.get())).setRegistryName(Eidolon.MODID, "warded_mail"),

                new WorktableRecipe(new WorktableRecipe.RecipeCore(
                        Registry.PEWTER_INGOT.get(), Registry.PEWTER_INGOT.get(), ItemStack.EMPTY,
                        ItemStack.EMPTY, Items.STICK, Registry.PEWTER_INGOT.get(),
                        Items.STICK, ItemStack.EMPTY, ItemStack.EMPTY
                ), new WorktableRecipe.RecipeExtras(
                        Registry.UNHOLY_SYMBOL.get(),
                        Registry.SOUL_SHARD.get(),
                        Registry.TATTERED_CLOTH.get(),
                        Registry.SOUL_SHARD.get()
                ), new ItemStack(Registry.REAPER_SCYTHE.get())).setRegistryName(Eidolon.MODID, "reaper_scythe"),

                new WorktableRecipe(new WorktableRecipe.RecipeCore(
                        Registry.PEWTER_INGOT.get(), Registry.PEWTER_INGOT.get(), ItemStack.EMPTY,
                        Registry.PEWTER_INGOT.get(), Items.STICK, ItemStack.EMPTY,
                        ItemStack.EMPTY, Items.STICK, ItemStack.EMPTY
                ), new WorktableRecipe.RecipeExtras(
                        Registry.UNHOLY_SYMBOL.get(),
                        ItemStack.EMPTY,
                        Registry.PEWTER_INLAY.get(),
                        ItemStack.EMPTY
                ), new ItemStack(Registry.CLEAVING_AXE.get())).setRegistryName(Eidolon.MODID, "cleaving_axe"),

                new WorktableRecipe(new WorktableRecipe.RecipeCore(
                        ItemStack.EMPTY, Items.BOOK, ItemStack.EMPTY,
                        Registry.ARCANE_GOLD_INGOT.get(), Blocks.OBSIDIAN, Registry.ARCANE_GOLD_INGOT.get(),
                        Blocks.OBSIDIAN, Blocks.OBSIDIAN, Blocks.OBSIDIAN
                ), new WorktableRecipe.RecipeExtras(
                        Tags.Items.GEMS_DIAMOND,
                        Registry.GOLD_INLAY.get(),
                        Tags.Items.GEMS_DIAMOND,
                        Registry.GOLD_INLAY.get()
                ), new ItemStack(Registry.SOUL_ENCHANTER.get())).setRegistryName(Eidolon.MODID, "soul_enchanter"),

                new WorktableRecipe(new WorktableRecipe.RecipeCore(
                        Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN, Blocks.OBSIDIAN,
                        ItemStack.EMPTY, Registry.PEWTER_INGOT.get(), ItemStack.EMPTY,
                        ItemStack.EMPTY, Registry.PEWTER_INLAY.get(), ItemStack.EMPTY
                ), new WorktableRecipe.RecipeExtras(
                        Items.ENDER_PEARL,
                        Registry.SOUL_SHARD.get(),
                        Registry.LESSER_SOUL_GEM.get(),
                        Registry.SOUL_SHARD.get()
                ), new ItemStack(Registry.REVERSAL_PICK.get())).setRegistryName(Eidolon.MODID, "reversal_pick"),

                new WorktableRecipe(new WorktableRecipe.RecipeCore(
                        ItemStack.EMPTY, Registry.ARCANE_GOLD_INGOT.get(), Registry.SHADOW_GEM.get(),
                        ItemStack.EMPTY, Items.STICK, Registry.ARCANE_GOLD_INGOT.get(),
                        Registry.GOLD_INLAY.get(), ItemStack.EMPTY, ItemStack.EMPTY
                ), new WorktableRecipe.RecipeExtras(
                        Registry.LESSER_SOUL_GEM.get(),
                        Items.BLAZE_POWDER,
                        Items.BLAZE_POWDER,
                        Items.BLAZE_POWDER
                ), new ItemStack(Registry.SOULFIRE_WAND.get())).setRegistryName(Eidolon.MODID, "soulfire_wand"),

                new WorktableRecipe(new WorktableRecipe.RecipeCore(
                        ItemStack.EMPTY, Registry.PEWTER_INGOT.get(), Registry.WRAITH_HEART.get(),
                        ItemStack.EMPTY, Items.STICK, Registry.PEWTER_INGOT.get(),
                        Registry.PEWTER_INLAY.get(), ItemStack.EMPTY, ItemStack.EMPTY
                ), new WorktableRecipe.RecipeExtras(
                        Registry.LESSER_SOUL_GEM.get(),
                        Items.BONE_MEAL,
                        Items.BONE_MEAL,
                        Items.BONE_MEAL
                ), new ItemStack(Registry.BONECHILL_WAND.get())).setRegistryName(Eidolon.MODID, "bonechill_wand"),

                new WorktableRecipe(new WorktableRecipe.RecipeCore(
                        Blocks.SMOOTH_STONE_SLAB, Blocks.SMOOTH_STONE_SLAB, Blocks.SMOOTH_STONE_SLAB,
                        Tags.Items.STONE, Tags.Items.STONE, Tags.Items.STONE,
                        Tags.Items.STONE, Registry.PEWTER_INLAY.get(), Tags.Items.STONE
                ), new WorktableRecipe.RecipeExtras(
                        Registry.SOUL_SHARD.get(),
                        ItemStack.EMPTY,
                        ItemStack.EMPTY,
                        ItemStack.EMPTY
                ), new ItemStack(Registry.STONE_ALTAR.get(), 3)).setRegistryName(Eidolon.MODID, "stone_altar"),

                new WorktableRecipe(new WorktableRecipe.RecipeCore(
                        ItemStack.EMPTY, Blocks.SMOOTH_STONE, ItemStack.EMPTY,
                        Tags.Items.STONE, Tags.Items.STONE, Tags.Items.STONE,
                        ItemStack.EMPTY, Tags.Items.STONE, ItemStack.EMPTY
                ), new WorktableRecipe.RecipeExtras(
                        Registry.UNHOLY_SYMBOL.get(),
                        ItemStack.EMPTY,
                        Registry.GOLD_INLAY.get(),
                        ItemStack.EMPTY
                ), new ItemStack(Registry.UNHOLY_EFFIGY.get())).setRegistryName(Eidolon.MODID, "unholy_effigy"),

                new WorktableRecipe(new WorktableRecipe.RecipeCore(
                        Blocks.WHITE_WOOL, Blocks.WHITE_WOOL, Blocks.WHITE_WOOL,
                        Blocks.WHITE_WOOL, Registry.SHADOW_GEM.get(), Blocks.WHITE_WOOL,
                        Blocks.WHITE_WOOL, Blocks.WHITE_WOOL, Blocks.WHITE_WOOL
                ), new WorktableRecipe.RecipeExtras(
                        Registry.UNHOLY_SYMBOL.get(),
                        ItemStack.EMPTY,
                        Tags.Items.DYES_BLUE,
                        ItemStack.EMPTY
                ), new ItemStack(Registry.WICKED_WEAVE.get(), 8)).setRegistryName(Eidolon.MODID, "wicked_weave"),

                new WorktableRecipe(new WorktableRecipe.RecipeCore(
                        ItemStack.EMPTY, Registry.WICKED_WEAVE.get(), ItemStack.EMPTY,
                        ItemStack.EMPTY, Registry.WICKED_WEAVE.get(), ItemStack.EMPTY,
                        Registry.WICKED_WEAVE.get(), ItemStack.EMPTY, Registry.WICKED_WEAVE.get()
                ), new WorktableRecipe.RecipeExtras(
                        Registry.SOUL_SHARD.get(),
                        ItemStack.EMPTY,
                        Registry.SOUL_SHARD.get(),
                        ItemStack.EMPTY
                ), new ItemStack(Registry.WARLOCK_HAT.get())).setRegistryName(Eidolon.MODID, "warlock_hat"),

                new WorktableRecipe(new WorktableRecipe.RecipeCore(
                        Registry.WICKED_WEAVE.get(), Registry.WICKED_WEAVE.get(), Registry.WICKED_WEAVE.get(),
                        Registry.WICKED_WEAVE.get(), Registry.WICKED_WEAVE.get(), Registry.WICKED_WEAVE.get(),
                        Registry.WICKED_WEAVE.get(), ItemStack.EMPTY, Registry.WICKED_WEAVE.get()
                ), new WorktableRecipe.RecipeExtras(
                        Registry.SOUL_SHARD.get(),
                        ItemStack.EMPTY,
                        Registry.SOUL_SHARD.get(),
                        ItemStack.EMPTY
                ), new ItemStack(Registry.WARLOCK_CLOAK.get())).setRegistryName(Eidolon.MODID, "warlock_cloak"),

                new WorktableRecipe(new WorktableRecipe.RecipeCore(
                        ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY,
                        Registry.WICKED_WEAVE.get(), ItemStack.EMPTY, Registry.WICKED_WEAVE.get(),
                        Registry.WICKED_WEAVE.get(), ItemStack.EMPTY, Registry.WICKED_WEAVE.get()
                ), new WorktableRecipe.RecipeExtras(
                        Registry.SOUL_SHARD.get(),
                        ItemStack.EMPTY,
                        Registry.SOUL_SHARD.get(),
                        ItemStack.EMPTY
                ), new ItemStack(Registry.WARLOCK_BOOTS.get())).setRegistryName(Eidolon.MODID, "warlock_boots"),

                new WorktableRecipe(new WorktableRecipe.RecipeCore(
                        ItemStack.EMPTY, Tags.Items.ENDER_PEARLS, ItemStack.EMPTY,
                        Tags.Items.FEATHERS, Registry.BASIC_BELT.get(), Tags.Items.FEATHERS,
                        ItemStack.EMPTY, Registry.LESSER_SOUL_GEM.get(), ItemStack.EMPTY
                ), new WorktableRecipe.RecipeExtras(
                        Registry.ENDER_CALX.get(),
                        Registry.PEWTER_INLAY.get(),
                        Registry.ENDER_CALX.get(),
                        Registry.PEWTER_INLAY.get()
                ), new ItemStack(Registry.GRAVITY_BELT.get())).setRegistryName(Eidolon.MODID, "gravity_belt"),

                new WorktableRecipe(new WorktableRecipe.RecipeCore(
                        ItemStack.EMPTY, Registry.GOLD_INLAY.get(), ItemStack.EMPTY,
                        Registry.ARCANE_GOLD_INGOT.get(), Registry.BASIC_BELT.get(), Registry.ARCANE_GOLD_INGOT.get(),
                        ItemStack.EMPTY, Tags.Items.GEMS_DIAMOND, ItemStack.EMPTY
                ), new WorktableRecipe.RecipeExtras(
                        Items.LEATHER,
                        Registry.SOUL_SHARD.get(),
                        Registry.ENCHANTED_ASH.get(),
                        Registry.SOUL_SHARD.get()
                ), new ItemStack(Registry.RESOLUTE_BELT.get())).setRegistryName(Eidolon.MODID, "resolute_belt"),

                new WorktableRecipe(new WorktableRecipe.RecipeCore(
                        ItemStack.EMPTY, Registry.WICKED_WEAVE.get(), ItemStack.EMPTY,
                        Registry.WICKED_WEAVE.get(), Registry.WICKED_WEAVE.get(), Registry.WICKED_WEAVE.get(),
                        ItemStack.EMPTY, Registry.LESSER_SOUL_GEM.get(), ItemStack.EMPTY
                ), new WorktableRecipe.RecipeExtras(
                        Registry.WARPED_SPROUTS.get(),
                        Registry.ENDER_CALX.get(),
                        Registry.SOUL_SHARD.get(),
                        Registry.ENDER_CALX.get()
                ), new ItemStack(Registry.PRESTIGIOUS_PALM.get())).setRegistryName(Eidolon.MODID, "prestigious_palm"),

                new WorktableRecipe(new WorktableRecipe.RecipeCore(
                        Registry.LEAD_INGOT.get(), Registry.LEAD_INGOT.get(), Registry.LEAD_INGOT.get(),
                        Registry.LEAD_INGOT.get(), Registry.LEAD_INGOT.get(), Registry.LEAD_INGOT.get(),
                        Items.LEATHER, Registry.SOUL_SHARD.get(), Items.LEATHER
                ), new WorktableRecipe.RecipeExtras(
                        Tags.Items.STORAGE_BLOCKS_LAPIS,
                        ItemStack.EMPTY,
                        Tags.Items.GEMS_QUARTZ,
                        ItemStack.EMPTY
                ), new ItemStack(Registry.MIND_SHIELDING_PLATE.get())).setRegistryName(Eidolon.MODID, "mind_shielding_plate"),

                new WorktableRecipe(new WorktableRecipe.RecipeCore(
                        ItemStack.EMPTY, Tags.Items.STORAGE_BLOCKS_DIAMOND, ItemStack.EMPTY,
                        ItemStack.EMPTY, Registry.BASIC_AMULET.get(), ItemStack.EMPTY,
                        ItemStack.EMPTY, Blocks.GLASS, ItemStack.EMPTY
                ), new WorktableRecipe.RecipeExtras(
                        Registry.ZOMBIE_HEART.get(),
                        Registry.LESSER_SOUL_GEM.get(),
                        Registry.WRAITH_HEART.get(),
                        Registry.LESSER_SOUL_GEM.get()
                ), new ItemStack(Registry.GLASS_HAND.get())).setRegistryName(Eidolon.MODID, "glass_hand")
        );
    }
}
