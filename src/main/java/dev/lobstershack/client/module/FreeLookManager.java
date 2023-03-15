package dev.lobstershack.client.module;

import dev.lobstershack.client.OsmiumClient;
import dev.lobstershack.client.util.DebugUtil;
import dev.lobstershack.common.config.Options;
import dev.lobstershack.common.mixin.client.CameraInvoker;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.apache.logging.log4j.Level;
import org.joml.Vector2f;

/**
 Holds values and consolidates functions between FreeLook related mixins
 @see dev.lobstershack.common.mixin.client.MouseHandlerMixin
 @see dev.lobstershack.common.mixin.client.CameraMixin
 **/
public class FreeLookManager {

    private static FreeLookManager INSTANCE;

    private boolean inFreeLookMode = false;

    private float freeLookXRot = 0f;
    private float freeLookYRot = 0f;

    public Vector2f processCameraTick(float yRot, float xRot) {
        if(OsmiumClient.freeLookKey.isDown() && !inFreeLookMode) {
            startFreeLook();
        }
        if(inFreeLookMode && !OsmiumClient.freeLookKey.isDown()) {
            stopFreeLook();
        }
        if(inFreeLookMode && Options.FreeLookEnabled.get()) {
            Minecraft.getInstance().options.setCameraType(CameraType.THIRD_PERSON_BACK);
            ((CameraInvoker) Minecraft.getInstance().gameRenderer.getMainCamera()).invokeSetRotation(freeLookYRot, freeLookXRot);
            return new Vector2f(freeLookXRot, freeLookYRot);
        }
        return new Vector2f(xRot, yRot);
    }

    public void stopCameraEntityRotationUpdate(LocalPlayer entity, double deltaYRot, double deltaXRot) {
        if(!(Options.FreeLookEnabled.get() && inFreeLookMode))  {
            entity.turn(deltaYRot, deltaXRot);
        } else {
            freeLookXRot += ((float) deltaXRot * Minecraft.getInstance().options.sensitivity().get());
            freeLookYRot += ((float) deltaYRot * Minecraft.getInstance().options.sensitivity().get());
        }
    }


    private void startFreeLook() {
        inFreeLookMode = true;
        freeLookXRot = Minecraft.getInstance().getCameraEntity().xRotO;
        freeLookYRot = Minecraft.getInstance().getCameraEntity().yRotO;
        DebugUtil.logIfDebug("Entered Free Look", Level.INFO);
    }

    private void stopFreeLook() {
        inFreeLookMode = false;
        Minecraft.getInstance().options.setCameraType(CameraType.FIRST_PERSON);
        DebugUtil.logIfDebug("Left Free Look", Level.INFO);
    }

    public static FreeLookManager getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new FreeLookManager();
        }
        return INSTANCE;
    }
}
