package elucent.eidolon.recipe.recipes.base;

import com.google.gson.annotations.Expose;
import elucent.eidolon.util.StackUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * @author DustW
 **/
public abstract class BaseRecipe<SELF extends BaseRecipe<SELF>> implements Recipe<CraftingContainer> {
    @Expose(deserialize = false, serialize = false)
    ResourceLocation id;
    @Expose
    public String type;

    public BaseRecipe() {
        type = getSerializer().getRegistryName().toString();
    }

    public abstract boolean matches(List<ItemStack> inputs);

    @Override
    public boolean matches(CraftingContainer pContainer, Level pLevel) {
        return matches(StackUtil.getStacks(pContainer));
    }

    @Override
    public ItemStack assemble(CraftingContainer pContainer) {
        return getResultItem();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    public SELF setID(ResourceLocation id) {
        this.id = id;
        return self();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    private SELF self() {
        return (SELF) this;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BaseRecipe recipe && recipe.id.equals(id);
    }
}
