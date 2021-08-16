package com.intro.server.api;

import com.intro.common.config.options.Option;

import java.util.ArrayList;

public class OptionApi {

    private static ArrayList<Option> serverSetOptions = new ArrayList<>();

    public static void addSetOption(Option option) {
        serverSetOptions.add(option);
    }

    public static ArrayList<Option> getServerSetOptions() {
        return serverSetOptions;
    }

}
