package elucent.eidolon.util.json.serializer;

import com.google.gson.*;
import net.minecraft.world.item.crafting.Ingredient;

import java.lang.reflect.Type;

/**
 * @author DustW
 **/
public class IngredientSerializer implements JsonSerializer<Ingredient>, JsonDeserializer<Ingredient> {
    @Override
    public Ingredient deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return Ingredient.fromJson(json);
        }
        catch (Exception e) {
            return Ingredient.EMPTY;
        }
    }

    @Override
    public JsonElement serialize(Ingredient src, Type typeOfSrc, JsonSerializationContext context) {
        return src.toJson();
    }
}
