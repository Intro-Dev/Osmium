package dev.lobstershack.client.config.options;

import com.google.gson.*;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

public class OptionSerializer implements JsonSerializer<Option<?>> {

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .excludeFieldsWithModifiers(Modifier.PRIVATE)
            .create();

    @Override
    public JsonElement serialize(Option<?> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject returnObject = new JsonObject();
        returnObject.addProperty("Identifier", src.getIdentifier());
        returnObject.addProperty("Value", GSON.toJson(src.get()));
        returnObject.addProperty("ValueType", src.get().getClass().getTypeName());
        return returnObject;
    }
}
