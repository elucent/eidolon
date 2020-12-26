package elucent.eidolon.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.codex.CruciblePage;
import elucent.eidolon.codex.Page;
import elucent.eidolon.gui.jei.RecipeWrappers;
import elucent.eidolon.tile.CrucibleTileEntity.CrucibleStep;
import elucent.eidolon.util.StackUtil;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;

public class CrucibleRegistry {
    static Map<ResourceLocation, Page> linkedPages = new HashMap<>();
    static Map<ResourceLocation, CrucibleRecipe> recipes = new HashMap<>();

    public static void register(CrucibleRecipe recipe) {
        ResourceLocation loc = recipe.getRegistryName();
        assert loc != null;
        recipes.put(loc, recipe);
    }

    public static void condense(List<ItemStack> stacks) {
        Iterator<ItemStack> iter = stacks.iterator();
        ItemStack last = ItemStack.EMPTY;
        while (iter.hasNext()) {
            ItemStack i = iter.next();
            if (!ItemStack.areItemsEqual(i, last) || !ItemStack.areItemStackTagsEqual(i, last) || last.getCount() + i.getCount() > last.getMaxStackSize()) {
                last = i;
            }
            else {
                last.grow(i.getCount());
                iter.remove();
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    static Page getDefaultPage(CrucibleRecipe recipe) {
        List<CruciblePage.CrucibleStep> steps = new ArrayList<>();
        for (CrucibleRecipe.Step step : recipe.getSteps()) {
            List<ItemStack> stacks = StackUtil.stacksFromObjects(step.matches);
            condense(stacks);
            steps.add(new CruciblePage.CrucibleStep(step.stirs, stacks.toArray(new ItemStack[stacks.size()])));
        }
        return new CruciblePage(recipe.result.copy(), steps.toArray(new CruciblePage.CrucibleStep[steps.size()]));
    }

    @OnlyIn(Dist.CLIENT)
    public static void linkPage(ResourceLocation recipe, Page page) {
        linkedPages.put(recipe, page);
    }

    @OnlyIn(Dist.CLIENT)
    public static List<RecipeWrappers.Crucible> getWrappedRecipes() {
        List<RecipeWrappers.Crucible> wrappers = new ArrayList<>();
        for (Map.Entry<ResourceLocation, CrucibleRecipe> entry : recipes.entrySet()) {
            Page page = linkedPages.getOrDefault(entry.getKey(), null);
            wrappers.add(new RecipeWrappers.Crucible(
                entry.getValue(),
                page != null ? page : getDefaultPage(entry.getValue())
            ));
        }
        return wrappers;
    }

    public static CrucibleRecipe find(ResourceLocation loc) {
        return recipes.get(loc);
    }

    public static CrucibleRecipe find(List<CrucibleStep> steps) {
        for (CrucibleRecipe recipe : recipes.values()) if (recipe.matches(steps)) return recipe;
        return null;
    }

    public static void init() {
        register(new CrucibleRecipe(new ItemStack(Registry.ARCANE_GOLD_INGOT.get(), 2)).setRegistryName(Eidolon.MODID, "arcane_gold")
            .addStep(Tags.Items.DUSTS_REDSTONE, Tags.Items.DUSTS_REDSTONE, Registry.SOUL_SHARD.get())
            .addStep(Tags.Items.INGOTS_GOLD, Tags.Items.INGOTS_GOLD));
        register(new CrucibleRecipe(new ItemStack(Registry.LESSER_SOUL_GEM.get())).setRegistryName(Eidolon.MODID, "lesser_soul_gem")
            .addStep(Tags.Items.DUSTS_REDSTONE, Tags.Items.DUSTS_REDSTONE, Tags.Items.GEMS_LAPIS, Tags.Items.GEMS_LAPIS)
            .addStirringStep(2, Registry.SOUL_SHARD.get(), Registry.SOUL_SHARD.get(), Registry.SOUL_SHARD.get(), Registry.SOUL_SHARD.get())
            .addStep(Tags.Items.GEMS_QUARTZ));
        register(new CrucibleRecipe(new ItemStack(Registry.SHADOW_GEM.get())).setRegistryName(Eidolon.MODID, "shadow_gem")
            .addStep(Items.COAL)
            .addStirringStep(1, Items.GHAST_TEAR, Registry.DEATH_ESSENCE.get())
            .addStirringStep(1, Registry.SOUL_SHARD.get(), Registry.SOUL_SHARD.get(), Registry.DEATH_ESSENCE.get())
            .addStep(Tags.Items.GEMS_DIAMOND));
        register(new CrucibleRecipe(new ItemStack(Registry.SULFUR.get(), 2)).setRegistryName(Eidolon.MODID, "sulfur")
            .addStep(Items.COAL, Registry.ENCHANTED_ASH.get()));
        register(new CrucibleRecipe(new ItemStack(Registry.ENDER_CALX.get(), 2)).setRegistryName(Eidolon.MODID, "ender_calx")
            .addStep(Tags.Items.ENDER_PEARLS, Registry.ENCHANTED_ASH.get()));
        register(new CrucibleRecipe(new ItemStack(Items.LEATHER, 1)).setRegistryName(Eidolon.MODID, "leather_from_flesh")
            .addStep(Registry.ENCHANTED_ASH.get(), Registry.ENCHANTED_ASH.get())
            .addStirringStep(2, Items.ROTTEN_FLESH));
        register(new CrucibleRecipe(new ItemStack(Items.ROTTEN_FLESH, 1)).setRegistryName(Eidolon.MODID, "rotten_beef")
            .addStep(Items.BEEF, Tags.Items.MUSHROOMS));
        register(new CrucibleRecipe(new ItemStack(Items.ROTTEN_FLESH, 1)).setRegistryName(Eidolon.MODID, "rotten_pork")
            .addStep(Items.PORKCHOP, Tags.Items.MUSHROOMS));
        register(new CrucibleRecipe(new ItemStack(Items.ROTTEN_FLESH, 1)).setRegistryName(Eidolon.MODID, "rotten_mutton")
            .addStep(Items.MUTTON, Tags.Items.MUSHROOMS));
        register(new CrucibleRecipe(new ItemStack(Items.ROTTEN_FLESH, 1)).setRegistryName(Eidolon.MODID, "rotten_chicken")
            .addStep(Items.CHICKEN, Tags.Items.MUSHROOMS));
        register(new CrucibleRecipe(new ItemStack(Items.ROTTEN_FLESH, 1)).setRegistryName(Eidolon.MODID, "rotten_rabbit")
            .addStep(Items.RABBIT, Tags.Items.MUSHROOMS));
        register(new CrucibleRecipe(new ItemStack(Items.GUNPOWDER, 4)).setRegistryName(Eidolon.MODID, "gunpowder")
            .addStep(Registry.SULFUR.get(), Items.BONE_MEAL)
            .addStirringStep(1, Items.CHARCOAL));
        register(new CrucibleRecipe(new ItemStack(Items.GOLDEN_APPLE, 1)).setRegistryName(Eidolon.MODID, "gilded_apple")
            .addStep(Tags.Items.INGOTS_GOLD, Tags.Items.INGOTS_GOLD)
            .addStirringStep(2, Registry.ENCHANTED_ASH.get())
            .addStep(Items.APPLE));
        register(new CrucibleRecipe(new ItemStack(Items.GOLDEN_CARROT, 1)).setRegistryName(Eidolon.MODID, "gilded_carrot")
            .addStep(Tags.Items.NUGGETS_GOLD, Tags.Items.NUGGETS_GOLD)
            .addStirringStep(2, Registry.ENCHANTED_ASH.get())
            .addStep(Items.CARROT));
        register(new CrucibleRecipe(new ItemStack(Items.GLISTERING_MELON_SLICE, 1)).setRegistryName(Eidolon.MODID, "gilded_melon")
            .addStep(Tags.Items.NUGGETS_GOLD, Tags.Items.NUGGETS_GOLD)
            .addStirringStep(2, Registry.ENCHANTED_ASH.get())
            .addStep(Items.MELON_SLICE));
        register(new CrucibleRecipe(new ItemStack(Registry.DEATH_ESSENCE.get(), 4)).setRegistryName(Eidolon.MODID, "death_essence")
            .addStep(Registry.ZOMBIE_HEART.get(), Items.ROTTEN_FLESH)
            .addStirringStep(2, Items.BONE_MEAL, Items.BONE_MEAL)
            .addStep(Items.CHARCOAL));
        register(new CrucibleRecipe(new ItemStack(Registry.CRIMSON_ESSENCE.get(), 4)).setRegistryName(Eidolon.MODID, "crimson_essence_fungus")
            .addStep(Items.CRIMSON_FUNGUS, Items.NETHER_WART)
            .addStirringStep(1, Registry.SULFUR.get()));
        register(new CrucibleRecipe(new ItemStack(Registry.CRIMSON_ESSENCE.get(), 2)).setRegistryName(Eidolon.MODID, "crimson_essence_roots")
            .addStep(Items.CRIMSON_ROOTS, Items.NETHER_WART)
            .addStirringStep(1, Registry.SULFUR.get()));
        register(new CrucibleRecipe(new ItemStack(Registry.CRIMSON_ESSENCE.get(), 2)).setRegistryName(Eidolon.MODID, "crimson_essence_vines")
            .addStep(Items.WEEPING_VINES, Items.NETHER_WART)
            .addStirringStep(1, Registry.SULFUR.get()));
        register(new CrucibleRecipe(new ItemStack(Registry.FUNGUS_SPROUTS.get(), 2)).setRegistryName(Eidolon.MODID, "fungus_sprouts")
            .addStep(Tags.Items.MUSHROOMS)
            .addStirringStep(2, Items.BONE_MEAL)
            .addStep(Items.WHEAT_SEEDS));
        register(new CrucibleRecipe(new ItemStack(Registry.WARPED_SPROUTS.get(), 2)).setRegistryName(Eidolon.MODID, "warped_sprouts")
            .addStep(Blocks.WARPED_FUNGUS)
            .addStirringStep(2, Registry.ENDER_CALX.get())
            .addStep(Items.NETHER_WART));
    }
}
