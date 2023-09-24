package dev.lobstershack.client.render.screen;

import dev.lobstershack.client.ModConstants;
import dev.lobstershack.client.OsmiumClient;
import dev.lobstershack.client.render.color.Colors;
import dev.lobstershack.client.render.widget.AbstractScalableButton;
import dev.lobstershack.client.render.widget.ProgressBarWidget;
import dev.lobstershack.client.util.Util;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.Level;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;

public class OsmiumUpdateScreen extends Screen  {

    private final Screen parent;

    private final Minecraft mc = Minecraft.getInstance();

    private Button acceptButton;
    private ProgressBarWidget progressBar;
    private Button declineButton;
    private Button retryButton;
    private Button continueButton;

    private final String latestReleaseName = Util.getLatestReleaseName();
    private final String latestReleaseTag = Util.getLatestGithubReleaseTag();

    private Thread modDownloadThread = new Thread(new ModDownloader());

    private String errorText = "";

    public static Path OLD_MOD_JAR_PATH;

    public OsmiumUpdateScreen(Screen parent) {
        super(Component.translatable("osmium.update_screen.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        acceptButton = new AbstractScalableButton(this.width / 2 - 175, this.height / 4 + 160, 150, 20, Component.translatable("osmium.download_update"), this::startDownload);
        declineButton = new AbstractScalableButton(this.width / 2 + 25, this.height / 4 + 160, 150, 20, Component.translatable("osmium.decline_update"), (button -> mc.setScreen(parent)));
        progressBar = new ProgressBarWidget(this.width / 2 - 100, this.height / 4 + 110, 200, 20, Component.empty());
        progressBar.visible = false;
        retryButton = new AbstractScalableButton(this.width / 2 - 100, this.height / 4 + 160, 200, 20, Component.translatable("osmium.retry"), this::startDownload);
        retryButton.visible = false;
        continueButton = new AbstractScalableButton(this.width / 2 - 100, this.height / 4 + 160, 200, 20, Component.translatable("osmium.decline_update"),button -> mc.setScreen(parent));
        continueButton.visible = false;


        this.addRenderableWidget(continueButton);
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
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(graphics, mouseX, mouseY, delta);
        graphics.drawCenteredString(mc.font, Component.translatable("osmium.update_available"), this.width / 2, this.height / 4, Colors.WHITE.getColor().getInt());
        graphics.drawCenteredString(mc.font, errorText, this.width / 2, this.height / 4 + 250, Colors.RED.getColor().getInt());
        graphics.drawCenteredString(mc.font, Component.literal("Current version: " + ModConstants.UPDATE_STRING + "-" + ModConstants.MINECRAFT_VERSION_STRING + ", New version: " + latestReleaseTag), this.width / 2, this.height / 4 + 50, Colors.WHITE.getColor().getInt());
        super.render(graphics, mouseX, mouseY, delta);
    }


    private class ModDownloader implements Runnable {

        @Override
        public void run() {
            try {
                Path oldJarPath = Util.getModJarPath("osmium", "Osmium");
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    try {
                        Util.forceDelete(oldJarPath.toFile());
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }));

                URL fileUrl = new URL(Util.getLatestReleaseDownloadString());
                long fileSize = getFileSize(fileUrl);
                HttpURLConnection connection = (HttpURLConnection) fileUrl.openConnection();

                BufferedInputStream stream = new BufferedInputStream(connection.getInputStream());
                FileOutputStream fileOutputStream = new FileOutputStream(FabricLoader.getInstance().getGameDir().toString() + "/mods/" + latestReleaseName);

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


                errorText = "Update successful! Update will be applied on game restart.";
                retryButton.visible = false;
                continueButton.visible = true;
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
