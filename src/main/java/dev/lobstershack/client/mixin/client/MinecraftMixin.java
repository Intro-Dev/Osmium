package dev.lobstershack.client.mixin.client;

import dev.lobstershack.client.OsmiumClient;
import dev.lobstershack.client.config.Options;
import dev.lobstershack.client.render.screen.OsmiumUpdateScreen;
import dev.lobstershack.client.render.widget.drawable.CpsDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.file.Files;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(at = @At("HEAD"), method = "close")
    public void close(CallbackInfo ci) {
        Options.save();
    }

    @Inject(method = "startAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;swing(Lnet/minecraft/world/InteractionHand;)V"))
    public void mouseClick(CallbackInfoReturnable<Boolean> cir) {
        CpsDisplay.getInstance().onClick();
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/MobEffectTextureManager;<init>(Lnet/minecraft/client/renderer/texture/TextureManager;)V"))
    public void init(GameConfig gameConfig, CallbackInfo ci) {
    }

    @Inject(at = @At("RETURN"), method = "close")
    public void closeReturn(CallbackInfo ci) {
        try {
            if(OsmiumUpdateScreen.OLD_MOD_JAR_PATH != null) {
                Files.deleteIfExists(OsmiumUpdateScreen.OLD_MOD_JAR_PATH);
            }
        } catch (Exception e) {
            OsmiumClient.LOGGER.log(Level.ERROR, "Failed to delete old mod jar!", e);
        }
    }







}
