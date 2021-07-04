package com.intro.config;

import com.google.gson.*;

import java.lang.reflect.Type;

public class OptionSerializer implements JsonSerializer<Option> {




    public Option deserialize(JsonArray src) {

        return null;
    }

    @Override
    public JsonElement serialize(Option src, Type typeOfSrc, JsonSerializationContext context) {
        if(src.type.equals("BooleanOption")) {
            BooleanOption option = (BooleanOption) src;
            JsonObject returnObject = new JsonObject();
            returnObject.addProperty("Identifier", option.identifier);
            returnObject.addProperty("Value", option.variable);
            returnObject.addProperty("Type", option.type);
            return returnObject;
        }
        if(src.type.equals("EnumOption")) {
            EnumOption option = (EnumOption) src;
            JsonObject returnObject = new JsonObject();
            returnObject.addProperty("Identifier", option.identifier);
            returnObject.addProperty("EnumValue", option.variable.toString());
            returnObject.addProperty("EnumType", option.variable.getClass().getTypeName());
            returnObject.addProperty("Type", option.type);
            return returnObject;
        }
        if(src.type.equals("Vector2Option")) {
            Vector2Option option = (Vector2Option) src;
            JsonObject returnObject = new JsonObject();
            returnObject.addProperty("Identifier", option.identifier);
            returnObject.addProperty("x", option.x);
            returnObject.addProperty("y", option.y);
            returnObject.addProperty("Type", option.type);
            return returnObject;
        }
        if(src.type.equals("DoubleOption")) {
            DoubleOption option = (DoubleOption) src;
            JsonObject returnObject = new JsonObject();
            returnObject.addProperty("Identifier", option.identifier);
            returnObject.addProperty("Value", option.variable);
            returnObject.addProperty("Type", option.type);
            return returnObject;
        }
        return null;
    }
}
