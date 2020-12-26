package elucent.eidolon.gui.jei;

import elucent.eidolon.Eidolon;
import elucent.eidolon.recipe.CrucibleRegistry;
import elucent.eidolon.recipe.WorktableRegistry;
import elucent.eidolon.ritual.RitualRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class JEIRegistry implements IModPlugin {
    public static IRecipeCategory CRUCIBLE_CATEGORY, WORKTABLE_CATEGORY, RITUAL_CATEGORY;

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Eidolon.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();

        registry.addRecipeCategories(CRUCIBLE_CATEGORY = new CrucibleCategory(guiHelper));
        registry.addRecipeCategories(WORKTABLE_CATEGORY = new WorktableCategory(guiHelper));
        registry.addRecipeCategories(RITUAL_CATEGORY = new RitualCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        registry.addRecipes(CrucibleRegistry.getWrappedRecipes(), CRUCIBLE_CATEGORY.getUid());
        registry.addRecipes(WorktableRegistry.getWrappedRecipes(), WORKTABLE_CATEGORY.getUid());
        registry.addRecipes(RitualRegistry.getWrappedRecipes(), RITUAL_CATEGORY.getUid());
    }
}
