package com.intro.common.mixin.client;

import com.intro.client.OsmiumClient;
import com.intro.client.module.event.EventDirection;
import com.intro.client.module.event.EventTick;
import com.intro.client.module.event.EventType;
import com.intro.client.render.drawables.CpsDisplay;
import com.intro.client.render.screen.OsmiumUpdateScreen;
import com.intro.client.util.OptionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Files;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow @Final private static Logger LOGGER;

    @Inject(at = @At("HEAD"), method = "tick")
    public void preTick(CallbackInfo info) {
        OsmiumClient.EVENT_BUS.postEvent(new EventTick(EventDirection.PRE), EventType.EVENT_TICK);
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void postTick(CallbackInfo info) {
        OsmiumClient.EVENT_BUS.postEvent(new EventTick(EventDirection.POST), EventType.EVENT_TICK);
    }

    @Inject(at = @At("HEAD"), method = "close")
    public void close(CallbackInfo ci) {
        OptionUtil.save();
    }

    @Inject(method = "startAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;swing(Lnet/minecraft/world/InteractionHand;)V"))
    public void mouseClick(CallbackInfo ci) {
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
