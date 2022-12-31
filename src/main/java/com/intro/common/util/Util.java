package com.intro.common.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.intro.common.ModConstants;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Util {

    public static final Logger LOGGER = LogManager.getLogger("OsmiumGeneric");

    // cache gotten values to prevent rate limiting and to prevent lag due to downloading stuff
    private static String cachedLatestReleaseTag;
    private static String cachedLatestReleaseDownload;
    private static String cachedLatestReleaseName;
    // starts at 1
    private static final int EASTER_EGG_COUNT = 12;


    // cache values at startup
    static {
        getLatestGithubReleaseTag();
        getLatestReleaseName();
        getLatestReleaseDownloadString();
    }


    public static String getLatestGithubReleaseTag() {
        try {
            if(cachedLatestReleaseTag != null) {
                return cachedLatestReleaseTag;
            }
            HttpURLConnection connection = (HttpURLConnection) new URL("https://api.github.com/repos/Intro-Dev/Osmium/releases").openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            switch (connection.getResponseCode()) {
                case 403 -> {
                    LOGGER.log(Level.INFO, "Could not retrieve github release data due to rate limiting. Unless your debugging you should probably not launch the game 60 times an hour");
                    return ModConstants.FULL_VERSION_STRING;
                }
                case 404 -> {
                    LOGGER.log(Level.INFO, "Could not retrieve github release data due to unknown url.");
                    return ModConstants.FULL_VERSION_STRING;
                }
                case 200 -> {
                }
                default -> {
                    LOGGER.log(Level.DEBUG, "Could not retrieve github release data for an unknown reason.");
                    return ModConstants.FULL_VERSION_STRING;
                }
            }

            JsonReader reader = new JsonReader(new InputStreamReader(connection.getInputStream()));
            JsonParser parser = new JsonParser();
            JsonElement rootElement = parser.parse(reader);
            JsonArray responseJson = rootElement.getAsJsonArray();
            // get tag
            String tag = responseJson.get(0).getAsJsonObject().get("tag_name").getAsString();
            cachedLatestReleaseTag = tag;
            return tag;
        } catch (Exception e) {
            LOGGER.log(Level.WARN, "Failed to get latest version string!");
            return ModConstants.FULL_VERSION_STRING;
        }
    }

    public static Component generateRandomEasterEggMessage() {
        return Component.translatable("osmium.easter_eggs.random_" + Mth.randomBetween(RandomSource.create(), 1, EASTER_EGG_COUNT));
    }

    public static boolean isRunningLatestVersion() {
        String[] releaseStrings = getLatestGithubReleaseTag().split("-");
        // release strings on GitHub have to be structured like this or else this check breaks
        // 1.0.0-1.17.1
        return !(!Objects.equals(releaseStrings[0], ModConstants.UPDATE_STRING) && releaseStrings[1].equals(ModConstants.MINECRAFT_VERSION_STRING));
    }

    public static String getLatestReleaseDownloadString() {
        try {
            if(cachedLatestReleaseDownload != null) {
                return cachedLatestReleaseDownload;
            }
            HttpURLConnection connection = (HttpURLConnection) new URL("https://api.github.com/repos/Intro-Dev/Osmium/releases/latest").openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            switch (connection.getResponseCode()) {
                case 403 -> {
                    LOGGER.log(Level.INFO, "Could not retrieve github release data due to rate limiting. Unless your debugging you should probably not launch the game 60 times an hour");
                    return "";
                }
                case 404 -> {
                    LOGGER.log(Level.INFO, "Could not retrieve github release data due to unknown url.");
                    return "";
                }
                case 200 -> {
                }
                default -> {
                    LOGGER.log(Level.DEBUG, "Could not retrieve github release data for an unknown reason.");
                    return "";
                }
            }

            JsonReader reader = new JsonReader(new InputStreamReader(connection.getInputStream()));
            JsonParser parser = new JsonParser();
            JsonElement rootElement = parser.parse(reader);
            JsonObject responseJson = rootElement.getAsJsonObject();
            // get tag
            String download = responseJson.get("assets").getAsJsonArray().get(0).getAsJsonObject().get("browser_download_url").getAsString();
            cachedLatestReleaseDownload = download;
            return download;
        } catch (Exception e) {
            LOGGER.log(Level.WARN, "Failed to get latest version download url!");
        }
        return "";
    }

    public static String getLatestReleaseName() {
        try {
            if(cachedLatestReleaseName != null) {
                return cachedLatestReleaseName;
            }
            HttpURLConnection connection = (HttpURLConnection) new URL("https://api.github.com/repos/Intro-Dev/Osmium/releases/latest").openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            switch (connection.getResponseCode()) {
                case 403 -> {
                    LOGGER.log(Level.INFO, "Could not retrieve github release data due to rate limiting. Unless your debugging you should probably not launch the game 60 times an hour");
                    return "";
                }
                case 404 -> {
                    LOGGER.log(Level.INFO, "Could not retrieve github release data due to unknown url.");
                    return "";
                }
                case 200 -> {
                }
                default -> {
                    LOGGER.log(Level.DEBUG, "Could not retrieve github release data for an unknown reason.");
                    return "";
                }
            }

            JsonReader reader = new JsonReader(new InputStreamReader(connection.getInputStream()));
            JsonParser parser = new JsonParser();
            JsonElement rootElement = parser.parse(reader);
            JsonObject responseJson = rootElement.getAsJsonObject();
            // get name
            String name = responseJson.get("assets").getAsJsonArray().get(0).getAsJsonObject().get("name").getAsString();
            cachedLatestReleaseName = name;
            return name;
        } catch (Exception e) {
            LOGGER.log(Level.WARN, "Failed to get latest version download url!");
        }
        return "";
    }

    /**
     * Adapted from ModManager by DeathsGun under Apache 2.0 license
     * ModManager GitHub https://github.com/DeathsGun/ModManager
     *
     * @author DeathsGun
     */
    public static Path getModJarPath(String modId, String name) throws IOException {
        List<Path> jars = Files.list(FabricLoader.getInstance().getGameDir().resolve("mods")).filter(file -> file.toFile().getName().endsWith(".jar")).toList();
        for(Path jarPath : jars) {
            ZipFile zipFile = new ZipFile(jarPath.toFile());
            ZipEntry entry = zipFile.getEntry("fabric.mod.json");
            JsonReader reader = new JsonReader(new InputStreamReader(zipFile.getInputStream(entry)));
            JsonParser parser = new JsonParser();
            JsonElement rootElement = parser.parse(reader);
            JsonObject jsonContent = rootElement.getAsJsonObject();
            zipFile.close();
            if (jsonContent.get("id").getAsString().equals(modId) || jsonContent.get("name").getAsString().equals(name)) {
                return jarPath;
            }
        }
        return null;
    }

    public static Path getCosmeticPacksPath() {
       return FabricLoader.getInstance().getGameDir().resolve("cosmetics");
    }

    public static String getZipFileSystemPrefix(ZipFile file) {
        File path = new File(file.getName());
        return path.getName().replaceAll("\\.zip", "");
    }

    // just tells the os to delete the file
    // only for windows because windows is the only platform that cares about concurrent file usage enough to block Files.delete()
    public static void forceDelete(File file) throws IOException, InterruptedException {
        if (net.minecraft.Util.getPlatform() == net.minecraft.Util.OS.WINDOWS) {
            new ProcessBuilder("cmd", "/c", "del /f " + '"' + file.getAbsolutePath() + '"').start().waitFor(200, TimeUnit.MILLISECONDS);
        } else {
            Files.deleteIfExists(file.toPath());
        }
    }

    public static <K, V> Map<K, V> mutableMapOf(K[] keys, V[] values) {
        HashMap<K, V> map = new HashMap<>();

        if(keys.length != values.length) throw new IllegalArgumentException("Keys and values must be of same length!");

        for(int i = 0; i < keys.length; i++) {
            map.put(keys[i], values[i]);
        }
        return map;
    }

}
