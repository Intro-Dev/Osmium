package com.intro.common.mixin.client;

import com.intro.client.render.cape.CosmeticManager;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.server.packs.resources.SimpleReloadableResourceManager;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(SimpleReloadableResourceManager.class)
public class SimpleReloadableResourceManagerMixin {

    @Inject(method = "createReload", at = @At("RETURN"))
    public void stitchTextures(Executor executor, Executor executor2, CompletableFuture<Unit> completableFuture, List<PackResources> list, CallbackInfoReturnable<ReloadInstance> cir) {
        CosmeticManager.genCapes();
    }
}
