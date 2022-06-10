package elucent.eidolon.recipe;

import elucent.eidolon.recipe.recipeobj.RecipeObject;
import elucent.eidolon.recipe.recipeobj.RecipeObjectType;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class WorktableRecipe extends ForgeRegistryEntry<WorktableRecipe> implements CraftingRecipe {
    RecipeCore core;
    RecipeExtras extras;
    ItemStack result;

    public static class RecipeCore {
        List<RecipeObject<?>> CONTEXT;

        public RecipeCore(
                RecipeObject<?> a, RecipeObject<?> b, RecipeObject<?> c,
                RecipeObject<?> d, RecipeObject<?> e, RecipeObject<?> f,
                RecipeObject<?> g, RecipeObject<?> h, RecipeObject<?> i
        ) {
            CONTEXT = Arrays.asList(a, b, c, d, e, f, g, h, i);
        }

        public RecipeCore(
                Object a, Object b, Object c,
                Object d, Object e, Object f,
                Object g, Object h, Object i
        ) {
            this(
                    RecipeObjectType.of(a), RecipeObjectType.of(b), RecipeObjectType.of(c),
                    RecipeObjectType.of(d), RecipeObjectType.of(e), RecipeObjectType.of(f),
                    RecipeObjectType.of(g), RecipeObjectType.of(h), RecipeObjectType.of(i));
        }
    }

    public static class RecipeExtras {
        List<RecipeObject<?>> CONTEXT;

        public RecipeExtras(
                RecipeObject<?> a, RecipeObject<?> b,
                RecipeObject<?> c, RecipeObject<?> d
        ) {
            CONTEXT = Arrays.asList(a, b, c, d);
        }

        public RecipeExtras(
                Object a, Object b,
                Object c, Object d
        ) {
            this(
                    RecipeObjectType.of(a), RecipeObjectType.of(b),
                    RecipeObjectType.of(c), RecipeObjectType.of(d)
            );
        }
    }

    public WorktableRecipe(WorktableRecipe.RecipeCore core, WorktableRecipe.RecipeExtras extras, ItemStack result) {
        this.core = core;
        this.extras = extras;
        this.result = result;
    }

    public List<RecipeObject<?>> getCore() {
        return core.CONTEXT;
    }

    public List<RecipeObject<?>> getOuter() {
        return extras.CONTEXT;
    }

    static boolean matches(RecipeObject<?> match, ItemStack sacrifice) {
        return match.matches(sacrifice);
    }

    public boolean matches(Container coreInv, Container extraInv) {
        if (coreInv.getContainerSize() < 9 || extraInv.getContainerSize() < 4) return false;
        for (int i = 0; i < core.CONTEXT.size(); i ++) {
            if (!matches(core.CONTEXT.get(i), coreInv.getItem(i))) return false;
        }
        for (int i = 0; i < extras.CONTEXT.size(); i ++) {
            if (!matches(extras.CONTEXT.get(i), extraInv.getItem(i))) return false;
        }
        return true;
    }

    public NonNullList<ItemStack> getRemainingItems(Container coreInv, Container extraInv) {
        ArrayList<ItemStack> result = new ArrayList<>();

        Consumer<ItemStack> action = itemStack -> result.add(itemStack.getContainerItem());

        RecipeUtils.forEach(coreInv, action);
        RecipeUtils.forEach(extraInv, action);

        return NonNullList.of(ItemStack.EMPTY, result.toArray(new ItemStack[0]));
    }

    @Override
    public ItemStack getResultItem() {
        return result.copy();
    }

    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {
        return false;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ResourceLocation getId() {
        return getRegistryName();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeHandler.WORKTABLE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }
}
