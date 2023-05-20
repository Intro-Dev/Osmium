package dev.lobstershack.client.config;

import dev.lobstershack.client.config.options.Option;
import dev.lobstershack.client.config.options.legacy.*;
import dev.lobstershack.client.util.Util;

import java.util.ArrayList;
import java.util.List;

public class DataFixer {

    public static Option<?> fixLegacyOption(LegacyOption option) {
        if(option instanceof BooleanOption) {
            return new Option<>(option.identifier, ((BooleanOption) option).variable);
        }
        if (option instanceof StringOption) {
            return new Option<>(option.identifier, ((StringOption) option).variable);
        }
        if (option instanceof ColorOption) {
            return new Option<>(option.identifier, ((ColorOption) option).color);
        }
        if (option instanceof DoubleOption) {
            return new Option<>(option.identifier, ((DoubleOption) option).variable);
        }
        if (option instanceof ElementPositionOption) {
            return new Option<>(option.identifier, ((ElementPositionOption) option).elementPosition);
        }
        if (option instanceof EnumOption) {
            return new Option<>(option.identifier, ((EnumOption) option).variable);
        }

        Util.LOGGER.warn("Failed to fix up legacy option!");
        return null;
    }

    public static Option<?>[] fixLegacyOptions(LegacyOption[] options) {
        List<Option<?>> fixedOptions = new ArrayList<>();
        for (LegacyOption option : options) {
            fixedOptions.add(fixLegacyOption(option));
        }
        return fixedOptions.toArray(new Option<?>[]{});
    }

}
