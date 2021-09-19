package elucent.eidolon.recipe.recipeobj;

import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class RecipeItemTag extends RecipeObject<ITag.INamedTag<Item>> {
    public RecipeItemTag(ITag.INamedTag<Item> obj) {
        super(obj);
    }

    @Override
    public Ingredient getIngredient() {
        return Ingredient.fromTag(obj);
    }

    @Override
    public boolean matches(ItemStack itemStack) {
        return this.obj.contains(itemStack.getItem());
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("tag", obj.getName().toString());
        return json;
    }

    @Override
    public ITag.INamedTag<Item> fromJson(JsonObject json) {
        ResourceLocation name = new ResourceLocation(json.get("tag").getAsString());
        return (ITag.INamedTag<Item>) ItemTags.getAllTags().stream().filter(tag -> tag.getName().equals(name)).toArray()[0];
    }

    @Override
    public CompoundNBT toNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("tag", obj.getName().toString());
        return nbt;
    }

    @Override
    public ITag.INamedTag<Item> fromNBT(CompoundNBT nbt) {
        return (ITag.INamedTag<Item>) ItemTags.getCollection().get(new ResourceLocation(nbt.getString("tag")));
    }

    @Override
    public RecipeObjectType<ITag.INamedTag<Item>, ? extends RecipeObject<ITag.INamedTag<Item>>> getType() {
        return RecipeObjectRegisterHandler.TAG;
    }
}
