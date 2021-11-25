package com.intro.common.config;

import com.intro.common.config.options.Option;
import com.intro.common.config.options.legacy.*;
import com.intro.common.util.Util;

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
        return fixedOptions.toArray(new Option<?>[] {});
    }

}
