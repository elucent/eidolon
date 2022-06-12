package elucent.eidolon.recipe;

import elucent.eidolon.Registry;
import elucent.eidolon.codex.CruciblePage;
import elucent.eidolon.codex.Page;
import elucent.eidolon.gui.jei.RecipeWrappers;
import elucent.eidolon.recipe.recipes.recipe.CrucibleRecipe;
import elucent.eidolon.recipe.recipes.register.RecipeTypes;
import elucent.eidolon.tile.CrucibleTileEntity.CrucibleStep;
import elucent.eidolon.util.StackUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import java.util.*;
import java.util.stream.Collectors;

public class CrucibleRegistry {
    static Map<ResourceLocation, Page> linkedPages = new HashMap<>();

    public static void condense(List<ItemStack> stacks) {
        Iterator<ItemStack> iter = stacks.iterator();
        ItemStack last = ItemStack.EMPTY;
        while (iter.hasNext()) {
            ItemStack i = iter.next();
            if (!ItemStack.isSame(i, last) || !ItemStack.tagMatches(i, last) || last.getCount() + i.getCount() > last.getMaxStackSize()) {
                last = i;
            }
            else {
                last.grow(i.getCount());
                iter.remove();
            }
        }
    }

    public static Page getDefaultPage(CrucibleRecipe recipe) {
        List<CruciblePage.CrucibleStep> steps = new ArrayList<>();
        for (CrucibleRecipe.Step step : recipe.getSteps()) {
            List<ItemStack> stacks = StackUtil.stacksFromObjects(step.matches.stream().map(i -> i.getItems()[0]).collect(Collectors.toList()));
            condense(stacks);
            steps.add(new CruciblePage.CrucibleStep(step.stirs, stacks.toArray(new ItemStack[stacks.size()])));
        }
        return new CruciblePage(recipe.result.copy(), steps.toArray(new CruciblePage.CrucibleStep[steps.size()]));
    }

    public static void linkPage(ResourceLocation recipe, Page page) {
        linkedPages.put(recipe, page);
    }

    public static List<RecipeWrappers.Crucible> getWrappedRecipes() {
        List<RecipeWrappers.Crucible> wrappers = new ArrayList<>();
        // for (Map.Entry<ResourceLocation, CrucibleRecipe> entry : recipes.entrySet()) {
        //     Page page = linkedPages.getOrDefault(entry.getKey(), null);
        //     wrappers.add(new RecipeWrappers.Crucible(
        //         entry.getValue(),
        //         page
        //     ));
        // }
        return wrappers;
    }

    public static CrucibleRecipe find(Level level, ResourceLocation loc) {
        return (CrucibleRecipe) level.getRecipeManager().byKey(loc).get();
    }

    public static CrucibleRecipe find(Level level, List<CrucibleStep> steps) {
        return level.getRecipeManager().getAllRecipesFor(RecipeTypes.CRUCIBLE.get())
                .stream().filter(r -> r.matches(steps, true)).findFirst().get();
    }

    public static List<CrucibleRecipe> built() {
        List<CrucibleRecipe> recipes = new ArrayList<>();

        recipes.add(new CrucibleRecipe(new ItemStack(Registry.ARCANE_GOLD_INGOT.get(), 2))
            .addStep(Tags.Items.DUSTS_REDSTONE, Tags.Items.DUSTS_REDSTONE, Registry.SOUL_SHARD.get())
            .addStep(Tags.Items.INGOTS_GOLD, Tags.Items.INGOTS_GOLD));
        recipes.add(new CrucibleRecipe(new ItemStack(Registry.LESSER_SOUL_GEM.get()))
            .addStep(Tags.Items.DUSTS_REDSTONE, Tags.Items.DUSTS_REDSTONE, Tags.Items.GEMS_LAPIS, Tags.Items.GEMS_LAPIS)
            .addStirringStep(2, Registry.SOUL_SHARD.get(), Registry.SOUL_SHARD.get(), Registry.SOUL_SHARD.get(), Registry.SOUL_SHARD.get())
            .addStep(Tags.Items.GEMS_QUARTZ));
        recipes.add(new CrucibleRecipe(new ItemStack(Registry.SHADOW_GEM.get()))
            .addStep(Items.COAL)
            .addStirringStep(1, Items.GHAST_TEAR, Registry.DEATH_ESSENCE.get())
            .addStirringStep(1, Registry.SOUL_SHARD.get(), Registry.SOUL_SHARD.get(), Registry.DEATH_ESSENCE.get())
            .addStep(Tags.Items.GEMS_DIAMOND));
        recipes.add(new CrucibleRecipe(new ItemStack(Registry.SULFUR.get(), 2))
            .addStep(Items.COAL, Registry.ENCHANTED_ASH.get()));
        recipes.add(new CrucibleRecipe(new ItemStack(Registry.ENDER_CALX.get(), 2))
            .addStep(Tags.Items.ENDER_PEARLS, Registry.ENCHANTED_ASH.get()));
        recipes.add(new CrucibleRecipe(new ItemStack(Items.LEATHER, 1))
            .addStep(Registry.ENCHANTED_ASH.get(), Registry.ENCHANTED_ASH.get())
            .addStirringStep(2, Items.ROTTEN_FLESH));
        recipes.add(new CrucibleRecipe(new ItemStack(Items.ROTTEN_FLESH, 1))
            .addStep(Items.BEEF, Tags.Items.MUSHROOMS));
        recipes.add(new CrucibleRecipe(new ItemStack(Items.ROTTEN_FLESH, 1))
            .addStep(Items.PORKCHOP, Tags.Items.MUSHROOMS));
        recipes.add(new CrucibleRecipe(new ItemStack(Items.ROTTEN_FLESH, 1))
            .addStep(Items.MUTTON, Tags.Items.MUSHROOMS));
        recipes.add(new CrucibleRecipe(new ItemStack(Items.ROTTEN_FLESH, 1))
            .addStep(Items.CHICKEN, Tags.Items.MUSHROOMS));
        recipes.add(new CrucibleRecipe(new ItemStack(Items.ROTTEN_FLESH, 1))
            .addStep(Items.RABBIT, Tags.Items.MUSHROOMS));
        recipes.add(new CrucibleRecipe(new ItemStack(Items.GUNPOWDER, 4))
            .addStep(Registry.SULFUR.get(), Items.BONE_MEAL)
            .addStirringStep(1, Items.CHARCOAL));
        recipes.add(new CrucibleRecipe(new ItemStack(Items.GOLDEN_APPLE, 1))
            .addStep(Tags.Items.INGOTS_GOLD, Tags.Items.INGOTS_GOLD)
            .addStirringStep(2, Registry.ENCHANTED_ASH.get())
            .addStep(Items.APPLE));
        recipes.add(new CrucibleRecipe(new ItemStack(Items.GOLDEN_CARROT, 1))
            .addStep(Tags.Items.NUGGETS_GOLD, Tags.Items.NUGGETS_GOLD)
            .addStirringStep(2, Registry.ENCHANTED_ASH.get())
            .addStep(Items.CARROT));
        recipes.add(new CrucibleRecipe(new ItemStack(Items.GLISTERING_MELON_SLICE, 1))
            .addStep(Tags.Items.NUGGETS_GOLD, Tags.Items.NUGGETS_GOLD)
            .addStirringStep(2, Registry.ENCHANTED_ASH.get())
            .addStep(Items.MELON_SLICE));
        recipes.add(new CrucibleRecipe(new ItemStack(Registry.DEATH_ESSENCE.get(), 4))
            .addStep(Registry.ZOMBIE_HEART.get(), Items.ROTTEN_FLESH)
            .addStirringStep(2, Items.BONE_MEAL, Items.BONE_MEAL)
            .addStep(Items.CHARCOAL));
        recipes.add(new CrucibleRecipe(new ItemStack(Registry.CRIMSON_ESSENCE.get(), 4))
            .addStep(Items.CRIMSON_FUNGUS, Items.NETHER_WART)
            .addStirringStep(1, Registry.SULFUR.get()));
        recipes.add(new CrucibleRecipe(new ItemStack(Registry.CRIMSON_ESSENCE.get(), 2))
            .addStep(Items.CRIMSON_ROOTS, Items.NETHER_WART)
            .addStirringStep(1, Registry.SULFUR.get()));
        recipes.add(new CrucibleRecipe(new ItemStack(Registry.CRIMSON_ESSENCE.get(), 2))
            .addStep(Items.WEEPING_VINES, Items.NETHER_WART)
            .addStirringStep(1, Registry.SULFUR.get()));
        recipes.add(new CrucibleRecipe(new ItemStack(Registry.FUNGUS_SPROUTS.get(), 2))
            .addStep(Tags.Items.MUSHROOMS)
            .addStirringStep(2, Items.BONE_MEAL)
            .addStep(Items.WHEAT_SEEDS));
        recipes.add(new CrucibleRecipe(new ItemStack(Registry.WARPED_SPROUTS.get(), 2))
            .addStep(Blocks.WARPED_FUNGUS)
            .addStirringStep(2, Registry.ENDER_CALX.get())
            .addStep(Items.NETHER_WART));
        recipes.add(new CrucibleRecipe(new ItemStack(Registry.POLISHED_PLANKS.getBlock(), 32))
            .addStep(ItemTags.PLANKS, ItemTags.PLANKS, ItemTags.PLANKS, ItemTags.PLANKS,
                ItemTags.PLANKS, ItemTags.PLANKS, ItemTags.PLANKS, ItemTags.PLANKS,
                ItemTags.PLANKS, ItemTags.PLANKS, ItemTags.PLANKS, ItemTags.PLANKS,
                ItemTags.PLANKS, ItemTags.PLANKS, ItemTags.PLANKS, ItemTags.PLANKS,
                ItemTags.PLANKS, ItemTags.PLANKS, ItemTags.PLANKS, ItemTags.PLANKS,
                ItemTags.PLANKS, ItemTags.PLANKS, ItemTags.PLANKS, ItemTags.PLANKS,
                ItemTags.PLANKS, ItemTags.PLANKS, ItemTags.PLANKS, ItemTags.PLANKS,
                ItemTags.PLANKS, ItemTags.PLANKS, ItemTags.PLANKS, ItemTags.PLANKS)
            .addStirringStep(1, Registry.SOUL_SHARD.get(), Registry.ENCHANTED_ASH.get()));

        return recipes;
    }
}
