package elucent.eidolon.recipe;

import com.google.gson.JsonObject;
import elucent.eidolon.recipe.recipeobj.RecipeObject;
import elucent.eidolon.recipe.recipeobj.RecipeObjectType;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class WorktableRecipe extends ForgeRegistryEntry<WorktableRecipe> implements ICraftingRecipe, IFinishedRecipe {
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

    public boolean matches(IInventory coreInv, IInventory extraInv) {
        if (coreInv.getSizeInventory() < 9 || extraInv.getSizeInventory() < 4) return false;
        for (int i = 0; i < core.CONTEXT.size(); i ++) {
            if (!matches(core.CONTEXT.get(i), coreInv.getStackInSlot(i))) return false;
        }
        for (int i = 0; i < extras.CONTEXT.size(); i ++) {
            if (!matches(extras.CONTEXT.get(i), extraInv.getStackInSlot(i))) return false;
        }
        return true;
    }

    public NonNullList<ItemStack> getRemainingItems(IInventory coreInv, IInventory extraInv) {
        ArrayList<ItemStack> result = new ArrayList<>();

        Consumer<ItemStack> action = itemStack -> result.add(itemStack.getContainerItem());

        RecipeUtils.forEach(coreInv, action);
        RecipeUtils.forEach(extraInv, action);

        return NonNullList.from(ItemStack.EMPTY, result.toArray(new ItemStack[0]));
    }

    @Override
    public ItemStack getRecipeOutput() {
        return result.copy();
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        return result.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    public ResourceLocation getId() {
        return getRegistryName();
    }

    @Override
    public void serialize(JsonObject json) {
        RecipeHandler.WORKTABLE_SERIALIZER.write(json, this);
    }

    @Override
    public ResourceLocation getID() {
        return getId();
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeHandler.WORKTABLE_SERIALIZER;
    }

    @Nullable
    @Override
    public JsonObject getAdvancementJson() {
        return null;
    }

    @Nullable
    @Override
    public ResourceLocation getAdvancementID() {
        return null;
    }

    @Override
    public IRecipeType<?> getType() {
        return IRecipeType.CRAFTING;
    }
}
