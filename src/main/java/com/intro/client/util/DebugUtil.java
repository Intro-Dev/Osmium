package com.intro.client.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DebugUtil {

    private static final Logger LOGGER = LogManager.getLogger("OsmiumDebug");

    public static boolean DEBUG = false;
    public static void logIfDebug(Object o, Level level) {
        if(DEBUG) LOGGER.log(level, o);
    }

}
