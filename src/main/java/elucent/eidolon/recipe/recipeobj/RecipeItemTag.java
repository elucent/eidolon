package elucent.eidolon.recipe.recipeobj;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.ItemTags;

public class RecipeItemTag extends RecipeObject<Tag.INamedTag<Item>> {
    public RecipeItemTag(Tag.INamedTag<Item> obj) {
        super(obj);
    }

    @Override
    public Ingredient getIngredient() {
        return Ingredient.of(obj);
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
    public Tag.INamedTag<Item> fromJson(JsonObject json) {
        ResourceLocation name = new ResourceLocation(json.get("tag").getAsString());
        return (Tag.INamedTag<Item>) ItemTags.getWrappers().stream().filter(tag -> tag.getName().equals(name)).toArray()[0];
    }

    @Override
    public CompoundTag toNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("tag", obj.getName().toString());
        return nbt;
    }

    @Override
    public Tag.INamedTag<Item> fromNBT(CompoundTag nbt) {
        return (Tag.INamedTag<Item>) ItemTags.getAllTags().getTag(new ResourceLocation(nbt.getString("tag")));
    }

    @Override
    public RecipeObjectType<Tag.INamedTag<Item>, ? extends RecipeObject<Tag.INamedTag<Item>>> getType() {
        return RecipeObjectRegisterHandler.TAG;
    }
}
