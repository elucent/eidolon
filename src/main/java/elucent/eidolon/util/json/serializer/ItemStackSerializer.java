package elucent.eidolon.util.json.serializer;

import com.google.gson.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;

import java.lang.reflect.Type;

/**
 * @author DustW
 **/
public class ItemStackSerializer implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {

    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return CraftingHelper.getItemStack(json.getAsJsonObject(), true);
    }

    @Override
    public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        CompoundTag allTag = src.serializeNBT();

        String item = src.getItem().getRegistryName().toString();
        result.addProperty("item", item);

        var tag = allTag.getCompound("tag");
        if (allTag.contains("ForgeCaps")) {
            var fCap = allTag.getCompound("ForgeCaps");
            tag.put("ForgeCaps", fCap);
        }
        JsonPrimitive nbt = new JsonPrimitive(tag.toString());
        result.add("nbt", nbt);

        result.addProperty("count", src.getCount());

        return result;
    }
}
