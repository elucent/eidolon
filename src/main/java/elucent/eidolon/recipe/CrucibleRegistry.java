package elucent.eidolon.recipe;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.ritual.Ritual;
import elucent.eidolon.tile.CrucibleTileEntity;
import elucent.eidolon.tile.CrucibleTileEntity.CrucibleStep;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrucibleRegistry {
    static Map<ResourceLocation, CrucibleRecipe> recipes = new HashMap<>();

    public static void register(CrucibleRecipe recipe) {
        ResourceLocation loc = recipe.getRegistryName();
        assert loc != null;
        recipes.put(loc, recipe);
    }

    public static CrucibleRecipe find(ResourceLocation loc) {
        return recipes.get(loc);
    }

    public static CrucibleRecipe find(List<CrucibleStep> steps) {
        for (CrucibleRecipe recipe : recipes.values()) if (recipe.matches(steps)) return recipe;
        return null;
    }

    public static void init() {
        register(new CrucibleRecipe(new ItemStack(Registry.LESSER_SOUL_GEM.get())).setRegistryName(Eidolon.MODID, "lesser_soul_gem")
            .addStep(Tags.Items.DUSTS_REDSTONE, Tags.Items.DUSTS_REDSTONE)
            .addStep(Tags.Items.GEMS_LAPIS, Tags.Items.GEMS_LAPIS)
            .addStep(2)
            .addStep(Registry.SOUL_SHARD.get(), Registry.SOUL_SHARD.get(), Registry.SOUL_SHARD.get(), Registry.SOUL_SHARD.get())
            .addStep(1)
            .addStep(Tags.Items.GEMS_QUARTZ));
        register(new CrucibleRecipe(new ItemStack(Registry.SHADOW_GEM.get())).setRegistryName(Eidolon.MODID, "shadow_gem")
            .addStep(Tags.Items.GEMS_DIAMOND)
            .addStep(Items.CRYING_OBSIDIAN)
            .addStep(Registry.SOUL_SHARD.get(), Registry.SOUL_SHARD.get())
            .addStep(2)
            .addStep(Tags.Items.DUSTS_REDSTONE, Tags.Items.DUSTS_REDSTONE)
            .addStep(1));
    }
}
