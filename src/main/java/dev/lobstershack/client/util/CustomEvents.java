package dev.lobstershack.client.util;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.multiplayer.PlayerInfo;

public class CustomEvents {

    public static final Event<CustomEvents.PlayerJoin> PLAYER_JOIN = EventFactory.createArrayBacked(PlayerJoin.class, callbacks -> (playerInfo) -> {
        for (PlayerJoin callback : callbacks) {
            callback.onPlayerJoin(playerInfo);
        }
    });

    public static final Event<CustomEvents.PlayerRemove> PLAYER_REMOVE = EventFactory.createArrayBacked(PlayerRemove.class, callbacks -> (playerInfo) -> {
        for (PlayerRemove callback : callbacks) {
            callback.onPlayerRemove(playerInfo);
        }
    });

    @FunctionalInterface
    public interface PlayerJoin {
        void onPlayerJoin(PlayerInfo playerInfo);
    }

    @FunctionalInterface
    public interface PlayerRemove {
        void onPlayerRemove(PlayerInfo playerInfo);
    }

}
