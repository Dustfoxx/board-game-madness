package Model;

import com.google.gson.*;
import java.lang.reflect.Type;

public class GeneralAdapter<T> implements JsonDeserializer<T>, JsonSerializer<T> {

    private final Gson gson;

    public GeneralAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String typeName = jsonObject.get("type").getAsString(); // Always use "type"
        try {
            Class<?> subtype = Class.forName(typeName);
            return context.deserialize(jsonObject, subtype);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unknown type: " + typeName, e);
        }
    }

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = gson.toJsonTree(src).getAsJsonObject();
        jsonObject.addProperty("type", src.getClass().getName()); // Always add "type"
        return jsonObject;
    }
}
