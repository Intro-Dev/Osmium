package com.intro.common.config.options.legacy;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class LegacyOptionSerializer implements JsonSerializer<LegacyOption> {

    @Override
    public JsonElement serialize(LegacyOption src, Type typeOfSrc, JsonSerializationContext context) {
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
        if(src.type.equals("ElementPositionOption")) {
            ElementPositionOption option = (ElementPositionOption) src;
            JsonObject returnObject = new JsonObject();
            returnObject.addProperty("Identifier", option.identifier);
            returnObject.addProperty("x", option.elementPosition.x);
            returnObject.addProperty("y", option.elementPosition.y);
            returnObject.addProperty("scale", option.elementPosition.scale);
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
        if(src.type.equals("ColorOption")) {
            ColorOption option = (ColorOption) src;
            JsonObject returnObject = new JsonObject();
            returnObject.addProperty("Identifier", option.identifier);
            returnObject.addProperty("R", option.color.getR());
            returnObject.addProperty("G", option.color.getG());
            returnObject.addProperty("B", option.color.getB());
            returnObject.addProperty("A", option.color.getA());
            returnObject.addProperty("Type", option.type);
            return returnObject;
        }
        if(src.type.equals("StringOption")) {
            StringOption option = (StringOption) src;
            JsonObject returnObject = new JsonObject();
            returnObject.addProperty("Identifier", option.identifier);
            returnObject.addProperty("Value", option.variable);
            returnObject.addProperty("Type", option.type);
            return returnObject;
        }

        return null;
    }
}
