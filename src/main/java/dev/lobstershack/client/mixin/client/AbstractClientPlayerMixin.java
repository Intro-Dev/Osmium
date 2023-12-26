package dev.lobstershack.client.mixin.client;

import dev.lobstershack.client.OsmiumClient;
import dev.lobstershack.client.render.cosmetic.Cape;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin {

    @Shadow abstract protected PlayerInfo getPlayerInfo();


    @Inject(method = "getSkin", at = @At(value = "RETURN"), cancellable = true)
    public void supplyCustomCape(CallbackInfoReturnable<PlayerSkin> cir) {
        PlayerSkin originalSkin = cir.getReturnValue();
        Cape cape = OsmiumClient.cosmeticManager.getCapeFromEntityGotUUID(getPlayerInfo().getProfile().getId());
        if(cape != null) {
            cir.setReturnValue(new PlayerSkin(originalSkin.texture(), originalSkin.textureUrl(), cape.getFrameTexture(), cape.getFrameTexture(), originalSkin.model(), originalSkin.secure()));
            return;
        }
        cir.setReturnValue(originalSkin);
    }

}
