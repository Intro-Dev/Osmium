package com.intro.common.config;

import com.google.gson.*;
import com.intro.client.render.Color;
import com.intro.client.util.EnumUtil;
import com.intro.common.config.options.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Type;

public class OptionDeserializer implements JsonDeserializer<Option> {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public Option deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject src = json.getAsJsonObject();
        ClassLoader cl = OptionDeserializer.class.getClassLoader();
        if(src.get("Type").getAsString().equals("BooleanOption")) {
            return new BooleanOption( src.get("Identifier").getAsString(), src.get("Value").getAsBoolean());
        }

        if(src.get("Type").getAsString().equals("EnumOption")) {
            try {
                // Loads enum, gets its value from the config file, and creates an EnumOption instance
                // throws an error according to intelij, but does it crash? no.
                return new EnumOption( src.get("Identifier").getAsString(), EnumUtil.loadEnumState(cl, src.get("EnumType").getAsString(), src.get("EnumValue").getAsString()));
            } catch (Exception e) {
                LOGGER.warn("Enum deserialization error!");
            }
        }
        if(src.get("Type").getAsString().equals("Vector2Option")) {
            return new Vector2Option( src.get("Identifier").getAsString(), src.get("x").getAsDouble(), src.get("y").getAsDouble());
        }
        if(src.get("Type").getAsString().equals("DoubleOption")) {
            return new DoubleOption( src.get("Identifier").getAsString(), src.get("Value").getAsDouble());
        }
        if(src.get("Type").getAsString().equals("ColorOption")) {
            return new ColorOption(src.get("Identifier").getAsString(), new Color(src.get("R").getAsInt(), src.get("G").getAsInt(), src.get("B").getAsInt(), src.get("A").getAsInt()));
        }
        // Options defaultOptions = new Options();
        // defaultOptions.setDefaults();
        // return defaultOptions.get(src.get("Identifier").getAsString());
        return null;
    }


}
