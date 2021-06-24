package com.intro.mixin;

import com.intro.Osmium;
import com.intro.module.event.EventDirection;
import com.intro.module.event.EventPlayerJoin;
import com.intro.module.event.EventSpawnEntity;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin  {

    //public ClientWorldMixin(ClientPlayNetworkHandler networkHandler, Properties properties, RegistryKey<World> registryRef, DimensionType dimensionType, int loadDistance, Supplier<Profiler> profiler, WorldRenderer worldRenderer, boolean debugWorld, long seed) {
     //   super(networkHandler, properties, registryRef, dimensionType, loadDistance, profiler, worldRenderer, debugWorld, seed);
    //}

    @Inject(at = @At("HEAD"), method = "addEntity")
    public void addEntityPre(int id, Entity entity, CallbackInfo info){
        Osmium.EVENT_BUS.PostEvent(new EventSpawnEntity(EventDirection.PRE, entity));
    }

    @Inject(at = @At("TAIL"), method = "addEntity")
    public void addEntityPost(int id, Entity entity, CallbackInfo info){
        Osmium.EVENT_BUS.PostEvent(new EventSpawnEntity(EventDirection.POST, entity));
    }

    @Inject(at = @At("HEAD"), method = "addPlayer")
    public void addPlayerPre(int id, AbstractClientPlayerEntity player, CallbackInfo info) {
        Osmium.EVENT_BUS.PostEvent(new EventPlayerJoin(player));
    }

}
