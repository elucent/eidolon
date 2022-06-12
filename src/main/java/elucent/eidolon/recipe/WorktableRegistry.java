package elucent.eidolon.recipe;

import elucent.eidolon.Registry;
import elucent.eidolon.codex.Page;
import elucent.eidolon.codex.WorktablePage;
import elucent.eidolon.gui.jei.RecipeWrappers;
import elucent.eidolon.recipe.recipes.recipe.WorktableRecipe;
import elucent.eidolon.recipe.recipes.register.RecipeTypes;
import elucent.eidolon.util.StackUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorktableRegistry {
    public static Page getDefaultPage(WorktableRecipe recipe) {
        List<ItemStack> stacks = new ArrayList<>();
        for (Object o : recipe.getCore()) stacks.add(StackUtil.stackFromObject(o));
        for (Object o : recipe.getOuter()) stacks.add(StackUtil.stackFromObject(o));
        return new WorktablePage(recipe.getResultItem().copy(), stacks.toArray(new ItemStack[0]));
    }

    public static List<RecipeWrappers.Worktable> getWrappedRecipes() {
        List<RecipeWrappers.Worktable> wrappers = new ArrayList<>();
        //for (Map.Entry<ResourceLocation, WorktableRecipe> entry : recipes.entrySet()) {
        //    Page page = null; // linkedPages.getOrDefault(entry.getKey(), null);
        //    wrappers.add(new RecipeWrappers.Worktable(
        //        entry.getValue(),
        //        page
        //    ));
        //}
        return wrappers;
    }

    public static WorktableRecipe find(Level world, ResourceLocation loc) {
        Recipe<?> recipe = world.getRecipeManager().byKey(loc).get();
        return recipe instanceof WorktableRecipe ? ((WorktableRecipe) recipe) : null;
    }

    public static WorktableRecipe find(Level world, Container core, Container outer) {
        for (CraftingRecipe recipe : world.getRecipeManager().getAllRecipesFor(RecipeTypes.WORKTABLE.get()))
            if (recipe instanceof WorktableRecipe && ((WorktableRecipe) recipe).matches(core, outer))
                return (WorktableRecipe) recipe;
        return null;
    }

    public static List<WorktableRecipe> built() {
        return new ArrayList<>(Arrays.asList(
                new WorktableRecipe(Arrays.asList(
                        ItemStack.EMPTY, Registry.PEWTER_INGOT.get(), ItemStack.EMPTY,
                        Registry.PEWTER_INLAY.get(), Registry.BASIC_AMULET.get(), Registry.PEWTER_INLAY.get(),
                        ItemStack.EMPTY, Blocks.OBSIDIAN, ItemStack.EMPTY
                ), Arrays.asList(
                        Registry.SOUL_SHARD.get(),
                        ItemStack.EMPTY,
                        Registry.SOUL_SHARD.get(),
                        ItemStack.EMPTY
                ), new ItemStack(Registry.VOID_AMULET.get())),

                new WorktableRecipe(Arrays.asList(
                        ItemStack.EMPTY, Registry.LESSER_SOUL_GEM.get(), ItemStack.EMPTY,
                        Registry.ENCHANTED_ASH.get(), Items.IRON_CHESTPLATE, Registry.ENCHANTED_ASH.get(),
                        ItemStack.EMPTY, Registry.ENCHANTED_ASH.get(), ItemStack.EMPTY
                ), Arrays.asList(
                        Registry.PEWTER_INLAY.get(),
                        Registry.PEWTER_INLAY.get(),
                        Registry.PEWTER_INLAY.get(),
                        Registry.PEWTER_INLAY.get()
                ), new ItemStack(Registry.WARDED_MAIL.get())),

                new WorktableRecipe(Arrays.asList(
                        Registry.PEWTER_INGOT.get(), Registry.PEWTER_INGOT.get(), ItemStack.EMPTY,
                        ItemStack.EMPTY, Items.STICK, Registry.PEWTER_INGOT.get(),
                        Items.STICK, ItemStack.EMPTY, ItemStack.EMPTY
                ), Arrays.asList(
                        Registry.UNHOLY_SYMBOL.get(),
                        Registry.SOUL_SHARD.get(),
                        Registry.TATTERED_CLOTH.get(),
                        Registry.SOUL_SHARD.get()
                ), new ItemStack(Registry.REAPER_SCYTHE.get())),

                new WorktableRecipe(Arrays.asList(
                        Registry.PEWTER_INGOT.get(), Registry.PEWTER_INGOT.get(), ItemStack.EMPTY,
                        Registry.PEWTER_INGOT.get(), Items.STICK, ItemStack.EMPTY,
                        ItemStack.EMPTY, Items.STICK, ItemStack.EMPTY
                ), Arrays.asList(
                        Registry.UNHOLY_SYMBOL.get(),
                        ItemStack.EMPTY,
                        Registry.PEWTER_INLAY.get(),
                        ItemStack.EMPTY
                ), new ItemStack(Registry.CLEAVING_AXE.get())),

                new WorktableRecipe(Arrays.asList(
                        ItemStack.EMPTY, Items.BOOK, ItemStack.EMPTY,
                        Registry.ARCANE_GOLD_INGOT.get(), Blocks.OBSIDIAN, Registry.ARCANE_GOLD_INGOT.get(),
                        Blocks.OBSIDIAN, Blocks.OBSIDIAN, Blocks.OBSIDIAN
                ), Arrays.asList(
                        Tags.Items.GEMS_DIAMOND,
                        Registry.GOLD_INLAY.get(),
                        Tags.Items.GEMS_DIAMOND,
                        Registry.GOLD_INLAY.get()
                ), new ItemStack(Registry.SOUL_ENCHANTER.get())),

                new WorktableRecipe(Arrays.asList(
                        Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN, Blocks.OBSIDIAN,
                        ItemStack.EMPTY, Registry.PEWTER_INGOT.get(), ItemStack.EMPTY,
                        ItemStack.EMPTY, Registry.PEWTER_INLAY.get(), ItemStack.EMPTY
                ), Arrays.asList(
                        Items.ENDER_PEARL,
                        Registry.SOUL_SHARD.get(),
                        Registry.LESSER_SOUL_GEM.get(),
                        Registry.SOUL_SHARD.get()
                ), new ItemStack(Registry.REVERSAL_PICK.get())),

                new WorktableRecipe(Arrays.asList(
                        ItemStack.EMPTY, Registry.ARCANE_GOLD_INGOT.get(), Registry.SHADOW_GEM.get(),
                        ItemStack.EMPTY, Items.STICK, Registry.ARCANE_GOLD_INGOT.get(),
                        Registry.GOLD_INLAY.get(), ItemStack.EMPTY, ItemStack.EMPTY
                ), Arrays.asList(
                        Registry.LESSER_SOUL_GEM.get(),
                        Items.BLAZE_POWDER,
                        Items.BLAZE_POWDER,
                        Items.BLAZE_POWDER
                ), new ItemStack(Registry.SOULFIRE_WAND.get())),

                new WorktableRecipe(Arrays.asList(
                        ItemStack.EMPTY, Registry.PEWTER_INGOT.get(), Registry.WRAITH_HEART.get(),
                        ItemStack.EMPTY, Items.STICK, Registry.PEWTER_INGOT.get(),
                        Registry.PEWTER_INLAY.get(), ItemStack.EMPTY, ItemStack.EMPTY
                ), Arrays.asList(
                        Registry.LESSER_SOUL_GEM.get(),
                        Items.BONE_MEAL,
                        Items.BONE_MEAL,
                        Items.BONE_MEAL
                ), new ItemStack(Registry.BONECHILL_WAND.get())),

                new WorktableRecipe(Arrays.asList(
                        Blocks.SMOOTH_STONE_SLAB, Blocks.SMOOTH_STONE_SLAB, Blocks.SMOOTH_STONE_SLAB,
                        Tags.Items.STONE, Tags.Items.STONE, Tags.Items.STONE,
                        Tags.Items.STONE, Registry.PEWTER_INLAY.get(), Tags.Items.STONE
                ), Arrays.asList(
                        Registry.SOUL_SHARD.get(),
                        ItemStack.EMPTY,
                        ItemStack.EMPTY,
                        ItemStack.EMPTY
                ), new ItemStack(Registry.STONE_ALTAR.get(), 3)),

                new WorktableRecipe(Arrays.asList(
                        ItemStack.EMPTY, Blocks.SMOOTH_STONE, ItemStack.EMPTY,
                        Tags.Items.STONE, Tags.Items.STONE, Tags.Items.STONE,
                        ItemStack.EMPTY, Tags.Items.STONE, ItemStack.EMPTY
                ), Arrays.asList(
                        Registry.UNHOLY_SYMBOL.get(),
                        ItemStack.EMPTY,
                        Registry.GOLD_INLAY.get(),
                        ItemStack.EMPTY
                ), new ItemStack(Registry.UNHOLY_EFFIGY.get())),

                new WorktableRecipe(Arrays.asList(
                        Blocks.WHITE_WOOL, Blocks.WHITE_WOOL, Blocks.WHITE_WOOL,
                        Blocks.WHITE_WOOL, Registry.SHADOW_GEM.get(), Blocks.WHITE_WOOL,
                        Blocks.WHITE_WOOL, Blocks.WHITE_WOOL, Blocks.WHITE_WOOL
                ), Arrays.asList(
                        Registry.UNHOLY_SYMBOL.get(),
                        ItemStack.EMPTY,
                        Tags.Items.DYES_BLUE,
                        ItemStack.EMPTY
                ), new ItemStack(Registry.WICKED_WEAVE.get(), 8)),

                new WorktableRecipe(Arrays.asList(
                        ItemStack.EMPTY, Registry.WICKED_WEAVE.get(), ItemStack.EMPTY,
                        ItemStack.EMPTY, Registry.WICKED_WEAVE.get(), ItemStack.EMPTY,
                        Registry.WICKED_WEAVE.get(), ItemStack.EMPTY, Registry.WICKED_WEAVE.get()
                ), Arrays.asList(
                        Registry.SOUL_SHARD.get(),
                        ItemStack.EMPTY,
                        Registry.SOUL_SHARD.get(),
                        ItemStack.EMPTY
                ), new ItemStack(Registry.WARLOCK_HAT.get())),

                new WorktableRecipe(Arrays.asList(
                        Registry.WICKED_WEAVE.get(), Registry.WICKED_WEAVE.get(), Registry.WICKED_WEAVE.get(),
                        Registry.WICKED_WEAVE.get(), Registry.WICKED_WEAVE.get(), Registry.WICKED_WEAVE.get(),
                        Registry.WICKED_WEAVE.get(), ItemStack.EMPTY, Registry.WICKED_WEAVE.get()
                ), Arrays.asList(
                        Registry.SOUL_SHARD.get(),
                        ItemStack.EMPTY,
                        Registry.SOUL_SHARD.get(),
                        ItemStack.EMPTY
                ), new ItemStack(Registry.WARLOCK_CLOAK.get())),

                new WorktableRecipe(Arrays.asList(
                        ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY,
                        Registry.WICKED_WEAVE.get(), ItemStack.EMPTY, Registry.WICKED_WEAVE.get(),
                        Registry.WICKED_WEAVE.get(), ItemStack.EMPTY, Registry.WICKED_WEAVE.get()
                ), Arrays.asList(
                        Registry.SOUL_SHARD.get(),
                        ItemStack.EMPTY,
                        Registry.SOUL_SHARD.get(),
                        ItemStack.EMPTY
                ), new ItemStack(Registry.WARLOCK_BOOTS.get())),

                new WorktableRecipe(Arrays.asList(
                        ItemStack.EMPTY, Tags.Items.ENDER_PEARLS, ItemStack.EMPTY,
                        Tags.Items.FEATHERS, Registry.BASIC_BELT.get(), Tags.Items.FEATHERS,
                        ItemStack.EMPTY, Registry.LESSER_SOUL_GEM.get(), ItemStack.EMPTY
                ), Arrays.asList(
                        Registry.ENDER_CALX.get(),
                        Registry.PEWTER_INLAY.get(),
                        Registry.ENDER_CALX.get(),
                        Registry.PEWTER_INLAY.get()
                ), new ItemStack(Registry.GRAVITY_BELT.get())),

                new WorktableRecipe(Arrays.asList(
                        ItemStack.EMPTY, Registry.GOLD_INLAY.get(), ItemStack.EMPTY,
                        Registry.ARCANE_GOLD_INGOT.get(), Registry.BASIC_BELT.get(), Registry.ARCANE_GOLD_INGOT.get(),
                        ItemStack.EMPTY, Tags.Items.GEMS_DIAMOND, ItemStack.EMPTY
                ), Arrays.asList(
                        Items.LEATHER,
                        Registry.SOUL_SHARD.get(),
                        Registry.ENCHANTED_ASH.get(),
                        Registry.SOUL_SHARD.get()
                ), new ItemStack(Registry.RESOLUTE_BELT.get())),

                new WorktableRecipe(Arrays.asList(
                        ItemStack.EMPTY, Registry.WICKED_WEAVE.get(), ItemStack.EMPTY,
                        Registry.WICKED_WEAVE.get(), Registry.WICKED_WEAVE.get(), Registry.WICKED_WEAVE.get(),
                        ItemStack.EMPTY, Registry.LESSER_SOUL_GEM.get(), ItemStack.EMPTY
                ), Arrays.asList(
                        Registry.WARPED_SPROUTS.get(),
                        Registry.ENDER_CALX.get(),
                        Registry.SOUL_SHARD.get(),
                        Registry.ENDER_CALX.get()
                ), new ItemStack(Registry.PRESTIGIOUS_PALM.get())),

                new WorktableRecipe(Arrays.asList(
                        Registry.LEAD_INGOT.get(), Registry.LEAD_INGOT.get(), Registry.LEAD_INGOT.get(),
                        Registry.LEAD_INGOT.get(), Registry.LEAD_INGOT.get(), Registry.LEAD_INGOT.get(),
                        Items.LEATHER, Registry.SOUL_SHARD.get(), Items.LEATHER
                ), Arrays.asList(
                        Tags.Items.STORAGE_BLOCKS_LAPIS,
                        ItemStack.EMPTY,
                        Tags.Items.GEMS_QUARTZ,
                        ItemStack.EMPTY
                ), new ItemStack(Registry.MIND_SHIELDING_PLATE.get())),

                new WorktableRecipe(Arrays.asList(
                        ItemStack.EMPTY, Tags.Items.STORAGE_BLOCKS_DIAMOND, ItemStack.EMPTY,
                        ItemStack.EMPTY, Registry.BASIC_AMULET.get(), ItemStack.EMPTY,
                        ItemStack.EMPTY, Blocks.GLASS, ItemStack.EMPTY
                ), Arrays.asList(
                        Registry.ZOMBIE_HEART.get(),
                        Registry.LESSER_SOUL_GEM.get(),
                        Registry.WRAITH_HEART.get(),
                        Registry.LESSER_SOUL_GEM.get()
                ), new ItemStack(Registry.GLASS_HAND.get()))
        ));
    }
}
