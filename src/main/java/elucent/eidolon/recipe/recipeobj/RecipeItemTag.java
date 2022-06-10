package elucent.eidolon.recipe.recipeobj;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class RecipeItemTag extends RecipeObject<TagKey<Item>> {
    public RecipeItemTag(TagKey<Item> obj) {
        super(obj);
    }

    @Override
    public Ingredient getIngredient() {
        return Ingredient.of(obj);
    }

    @Override
    public boolean matches(ItemStack itemStack) {
        return itemStack.is(obj);
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("tag", obj.location().toString());
        return json;
    }

    @Override
    public TagKey<Item> fromJson(JsonObject json) {
        ResourceLocation name = new ResourceLocation(json.get("tag").getAsString());
        return ItemTags.create(name);
    }

    @Override
    public CompoundTag toNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("tag", obj.location().toString());
        return nbt;
    }

    @Override
    public TagKey<Item> fromNBT(CompoundTag nbt) {
        return ItemTags.create(new ResourceLocation(nbt.getString("tag")));
    }

    @Override
    public RecipeObjectType<TagKey<Item>, ? extends RecipeObject<TagKey<Item>>> getType() {
        return RecipeObjectRegisterHandler.TAG;
    }
}
