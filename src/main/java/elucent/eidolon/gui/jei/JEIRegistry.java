package elucent.eidolon.gui.jei;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.recipe.recipes.register.RecipeTypes;
import elucent.eidolon.ritual.RitualRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.stream.Collectors;

@JeiPlugin
public class JEIRegistry implements IModPlugin {
    public static final RecipeType<RecipeWrappers.Worktable> WORKTABLE =
            new RecipeType<>(new ResourceLocation(Eidolon.MODID, "worktable"), RecipeWrappers.Worktable.class);

    public static final RecipeType<RecipeWrappers.Crucible> CRUCIBLE =
            new RecipeType<>(new ResourceLocation(Eidolon.MODID, "crucible"), RecipeWrappers.Crucible.class);

    public static final RecipeType<RecipeWrappers.RitualRecipe> RITUAL =
            new RecipeType<>(new ResourceLocation(Eidolon.MODID, "ritual"), RecipeWrappers.RitualRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Eidolon.MODID, "jei_plugin");
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(Registry.CRUCIBLE.get()), CRUCIBLE);
        registry.addRecipeCatalyst(new ItemStack(Registry.WORKTABLE.get()), WORKTABLE);
        registry.addRecipeCatalyst(new ItemStack(Registry.BRAZIER.get()), RITUAL);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();

        registry.addRecipeCategories(new CrucibleCategory(guiHelper));
        registry.addRecipeCategories(new WorktableCategory(guiHelper));
        registry.addRecipeCategories(new RitualCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        var level = Minecraft.getInstance().level;

        registry.addRecipes(CRUCIBLE, level.getRecipeManager().getAllRecipesFor(RecipeTypes.CRUCIBLE.get())
                .stream().map(c -> new RecipeWrappers.Crucible(c, null)).collect(Collectors.toList()));
        registry.addRecipes(WORKTABLE, level.getRecipeManager().getAllRecipesFor(RecipeTypes.WORKTABLE.get())
                .stream().map(c -> new RecipeWrappers.Worktable(c, null)).collect(Collectors.toList()));
        registry.addRecipes(RitualRegistry.getWrappedRecipes(), RITUAL.getUid());
    }
}
