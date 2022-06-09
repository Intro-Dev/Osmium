package com.intro.common.mixin.client;

import com.intro.client.render.cape.CosmeticManager;
import com.intro.common.api.OsmiumApi;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ReloadableResourceManager.class)
public class ReloadableResourceManagerMixin {

    @Inject(method = "createReload", at = @At("RETURN"))
    public void stitchTextures(Executor executor, Executor executor2, CompletableFuture<Unit> completableFuture, List<PackResources> list, CallbackInfoReturnable<ReloadInstance> cir) {
        CosmeticManager.loadCapes();

        try {
            OsmiumApi.getInstance().uploadCapeToServers(CosmeticManager.getPreLoadedPlayerCape());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
