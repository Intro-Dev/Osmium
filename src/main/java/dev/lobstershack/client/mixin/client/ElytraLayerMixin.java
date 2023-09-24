package dev.lobstershack.client.mixin.client;

import dev.lobstershack.client.OsmiumClient;
import dev.lobstershack.client.render.cosmetic.Cape;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.resources.PlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ElytraLayer.class)
public class ElytraLayerMixin {

    @Redirect(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;getSkin()Lnet/minecraft/client/resources/PlayerSkin;"))
    public PlayerSkin redirectCapeTexture(AbstractClientPlayer instance) {
        PlayerSkin originalSkin = instance.getSkin();
        Cape cape = OsmiumClient.cosmeticManager.getCapeFromEntityGotUUID(instance.getUUID());
        if(cape != null) {
            return new PlayerSkin(originalSkin.texture(), originalSkin.textureUrl(), cape.getFrameTexture(), cape.getFrameTexture(), originalSkin.model(), originalSkin.secure());
        }
        return originalSkin;
    }
}
