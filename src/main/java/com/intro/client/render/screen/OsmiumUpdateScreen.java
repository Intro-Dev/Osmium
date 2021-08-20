package com.intro.client.render.screen;

import com.intro.client.OsmiumClient;
import com.intro.client.render.Colors;
import com.intro.client.render.widget.ProgressBarWidget;
import com.intro.common.ModConstants;
import com.intro.common.util.Util;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.apache.logging.log4j.Level;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class OsmiumUpdateScreen extends Screen  {

    private final Screen parent;

    private final Minecraft mc = Minecraft.getInstance();

    private Button acceptButton;
    private ProgressBarWidget progressBar;
    private Button declineButton;
    private Button retryButton;

    private final String latestReleaseName = Util.getLatestReleaseName();
    private final String latestReleaseTag = Util.getLatestGithubReleaseTag();

    private Thread modDownloadThread = new Thread(new ModDownloader());

    private String errorText = "";

    public OsmiumUpdateScreen(Screen parent) {
        super(new TranslatableComponent("osmium.update_screen.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        acceptButton = new Button(this.width / 2 - 175, this.height / 6 + 200, 150, 20, new TranslatableComponent("osmium.download_update"), this::startDownload);
        declineButton = new Button(this.width / 2 + 25, this.height / 6 + 200, 150, 20, new TranslatableComponent("osmium.decline_update"), (button -> mc.setScreen(parent)));
        progressBar = new ProgressBarWidget(this.width / 2 - 100, this.height / 6 + 150, 200);
        progressBar.visible = false;
        retryButton = new Button(this.width / 2 - 100, this.height / 6 + 200, 200, 20, new TranslatableComponent("osmium.retry"), this::startDownload);
        retryButton.visible = false;

        this.addRenderableWidget(acceptButton);
        this.addRenderableWidget(declineButton);
        this.addRenderableWidget(progressBar);
        this.addRenderableWidget(retryButton);
    }

    private void startDownload(Button button) {
        this.acceptButton.visible = false;
        this.declineButton.visible = false;
        this.progressBar.visible = true;
        this.retryButton.visible = true;
        progressBar.setProgress(0d);
        if(modDownloadThread.isAlive()) {
            modDownloadThread.interrupt();
        }
        modDownloadThread = new Thread(new ModDownloader());
        modDownloadThread.start();
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredString(matrices, mc.font, new TranslatableComponent("osmium.update_available"), this.width / 2, this.height / 6, Colors.WHITE.getColor().getInt());
        drawCenteredString(matrices, mc.font, errorText, this.width / 2, this.height / 6 + 250, Colors.RED.getColor().getInt());
        drawCenteredString(matrices, mc.font, new TextComponent("Current version: " + ModConstants.UPDATE_STRING + "-" + ModConstants.MINECRAFT_VERSION_STRING + ", New version: " + latestReleaseTag), this.width / 2, this.height / 6 + 40, Colors.WHITE.getColor().getInt());

        super.render(matrices, mouseX, mouseY, delta);
    }


    private class ModDownloader implements Runnable {

        @Override
        public void run() {
            try {
                URL fileUrl = new URL(Util.getLatestReleaseDownloadString());
                long fileSize = getFileSize(fileUrl);
                HttpURLConnection connection = (HttpURLConnection) fileUrl.openConnection();

                BufferedInputStream stream = new BufferedInputStream(connection.getInputStream());
                FileOutputStream fileOutputStream = new FileOutputStream(FabricLoader.getInstance().getGameDir().getParent().toString() + "/mods/" + latestReleaseName);

                double bytesDownloaded = 0;

                // buffer data
                byte[] dataBuffer = new byte[1024];
                double bytesRead;

                errorText = "Downloading jar...";
                while((bytesRead = stream.read(dataBuffer, 0, 1024)) >= 0) {
                    bytesDownloaded += bytesRead;
                    fileOutputStream.write(dataBuffer, 0, (int) bytesRead);
                    final double currentProgress = (bytesDownloaded / fileSize);
                    progressBar.setProgress(currentProgress);
                }

                fileOutputStream.close();
                connection.disconnect();

                errorText = "Deleting old jar...";
                // try to delete old jar
                // hardcoded because fabric isn't exposing all the mod metadata needed for finding the jar name
                Files.delete(Path.of(FabricLoader.getInstance().getGameDir().getParent().toString() + "/mods/osmium-" + ModConstants.UPDATE_STRING + ".jar"));
                errorText = "Update successful! Exiting game...";
                mc.close();
            } catch (Exception e) {
                OsmiumClient.LOGGER.log(Level.ERROR, "Update Error: " + e + ", Try manually updating if this issue persists");
                errorText = "Update Error: " + e + ", Try manually updating if this issue persists";
            }
        }

        private long getFileSize(URL url) throws IOException {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            return connection.getContentLengthLong();
        }
    }

}
