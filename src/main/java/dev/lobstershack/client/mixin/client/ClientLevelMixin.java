package dev.lobstershack.client.mixin.client;

import dev.lobstershack.client.event.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {


    @Inject(at = @At("HEAD"), method = "addEntity")
    public void addEntityPre(int id, Entity entity, CallbackInfo info){
        EventBuss.postEvent(new EventSpawnEntity(EventDirection.PRE, entity), EventType.EVENT_SPAWN_ENTITY);
    }

    @Inject(at = @At("TAIL"), method = "addEntity")
    public void addEntityPost(int id, Entity entity, CallbackInfo info){
        EventBuss.postEvent(new EventSpawnEntity(EventDirection.POST, entity), EventType.EVENT_SPAWN_ENTITY);
    }

    @Inject(at = @At("HEAD"), method = "addPlayer")
    public void addPlayerPre(int id, AbstractClientPlayer player, CallbackInfo info) {
        EventBuss.postEvent(new EventAddPlayer(player), EventType.EVENT_ADD_PLAYER);
    }






}
