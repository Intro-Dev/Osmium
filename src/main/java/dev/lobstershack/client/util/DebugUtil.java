package dev.lobstershack.client.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DebugUtil {

    private static final Logger LOGGER = LogManager.getLogger("OsmiumDebug");
    private static boolean DEBUG = false;

    public static void enableDebugMode() {
        DEBUG = true;
    }

    public static boolean isDebug() {
        return DEBUG;
    }


    public static void logIfDebug(Object o, Level level) {
        if(DEBUG) {
            try {
                StackTraceElement callingStack = Thread.currentThread().getStackTrace()[2];
                int lastDotIndex = callingStack.getClassName().lastIndexOf(".");
                String className = callingStack.getClassName().substring(lastDotIndex + 1);
                LOGGER.log(level, className + "." + callingStack.getMethodName() +  "(): " + o);
            } catch (Exception e) {
                LOGGER.log(Level.WARN, "Congrats, the Osmium debugger just crashed. How did this happen?");
                LOGGER.log(Level.ERROR, e);
            }
        }
    }

}
