package dev.lobstershack.client.config.options;

import com.google.gson.*;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

public class OptionDeserializer implements JsonDeserializer<Option<?>> {

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .excludeFieldsWithModifiers(Modifier.PRIVATE)
            .create();

    @Override
    public Option<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return Option.unboundOptionOf(json.getAsJsonObject().get("Identifier").getAsString(), GSON.fromJson(json.getAsJsonObject().get("Value").getAsString(), Class.forName(json.getAsJsonObject().get("ValueType").getAsString())));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}
