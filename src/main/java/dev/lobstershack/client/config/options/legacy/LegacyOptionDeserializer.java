package dev.lobstershack.client.config.options.legacy;

import com.google.gson.*;
import dev.lobstershack.client.render.color.Color;
import dev.lobstershack.client.util.EnumUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Type;

public class LegacyOptionDeserializer implements JsonDeserializer<LegacyOption> {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public LegacyOption deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject src = json.getAsJsonObject();
        ClassLoader cl = LegacyOptionDeserializer.class.getClassLoader();
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
        if(src.get("Type").getAsString().equals("ElementPositionOption")) {
            return new ElementPositionOption( src.get("Identifier").getAsString(), src.get("x").getAsDouble(), src.get("y").getAsDouble(), src.get("scale").getAsFloat());
        }
        if(src.get("Type").getAsString().equals("DoubleOption")) {
            return new DoubleOption( src.get("Identifier").getAsString(), src.get("Value").getAsDouble());
        }
        if(src.get("Type").getAsString().equals("ColorOption")) {
            return new ColorOption(src.get("Identifier").getAsString(), new Color(src.get("R").getAsInt(), src.get("G").getAsInt(), src.get("B").getAsInt(), src.get("A").getAsInt()));
        }
        // for legacy Vector2d options
        if(src.get("Type").getAsString().equals("Vector2Option")) {
            return new ElementPositionOption( src.get("Identifier").getAsString(), src.get("x").getAsDouble(), src.get("y").getAsDouble());
        }
        if(src.get("Type").getAsString().equals("StringOption")) {
            return new StringOption(src.get("Identifier").getAsString(), src.get("Value").getAsString());
        }
        return null;
    }


}
