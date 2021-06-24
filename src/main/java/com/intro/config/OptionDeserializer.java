package com.intro.config;

import com.google.gson.*;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.system.CallbackI;

import java.lang.reflect.Type;

@SuppressWarnings("unchecked")
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
                return new EnumOption( src.get("Identifier").getAsString(), loadEnum(cl, src.get("EnumType").getAsString(), src.get("EnumValue").getAsString()));
            } catch (Exception e) {
                LOGGER.warn("Enum deserialization error!");
            }
        }
        return null;
    }

    public static <T extends Enum<T>> T loadEnum(ClassLoader loader, String classBinaryName, String instanceName) throws ClassNotFoundException {
        @SuppressWarnings("unchecked")
        Class<T> eClass = (Class<T>)loader.loadClass(classBinaryName);
        return Enum.valueOf(eClass, instanceName);
    }
}
