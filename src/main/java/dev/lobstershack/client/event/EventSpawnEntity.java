package dev.lobstershack.client.event;

import net.minecraft.world.entity.Entity;

public class EventSpawnEntity extends Event {

    public final Entity entity;

    public EventSpawnEntity(EventDirection direction, Entity entity) {
        super(direction);
        this.entity = entity;
    }
}
