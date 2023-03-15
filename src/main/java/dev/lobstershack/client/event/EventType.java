package dev.lobstershack.client.event;

public enum EventType {
    EVENT_ADD_PLAYER(0),
    EVENT_JOIN_WORLD(1),
    EVENT_REMOVE_PLAYER(2),
    EVENT_RENDER(3),
    EVENT_RENDER_POST_TICK(4),
    EVENT_SETTINGS_CHANGE(5),
    EVENT_SPAWN_ENTITY(6),
    EVENT_START_GAME(7),
    EVENT_TICK(8),
    EVENT_RECEIVE_CHAT_MESSAGE(9);

    private final int value;

    public int getIntVal() {
        return value;
    }

    EventType(int value) {
        this.value = value;
    }
}
