package com.intro.common.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.intro.common.ModConstants;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class Util {

    public static final Logger LOGGER = LogManager.getLogger();

    // cache gotten values to prevent rate limiting and to prevent lag due to downloading stuff
    private static String cachedLatestReleaseTag;
    private static String cachedLatestReleaseDownload;
    private static String cachedLatestReleaseName;

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
                    LOGGER.log(Level.INFO, "Could not retrieve github release data due to rate limiting. Unless your debugging you should probably not launch the game 60 times a day");
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
            e.printStackTrace();
            LOGGER.log(Level.WARN, "Failed to get latest version string!");
        }
        return "";
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
                    LOGGER.log(Level.INFO, "Could not retrieve github release data due to rate limiting. Unless your debugging you should probably not launch the game 60 times a day.");
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
            e.printStackTrace();
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
                    LOGGER.log(Level.INFO, "Could not retrieve github release data due to rate limiting. Unless your debugging you should probably not launch the game 60 times a day");
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
            e.printStackTrace();
            LOGGER.log(Level.WARN, "Failed to get latest version download url!");
        }
        return "";
    }
}
