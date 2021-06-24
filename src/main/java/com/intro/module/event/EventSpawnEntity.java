package com.intro.module.event;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;

public class EventSpawnEntity extends Event {
    public Entity entity;
    public EventSpawnEntity(EventDirection direction, Entity entity) {
        super(direction, false);
        this.entity = entity;
    }
}
