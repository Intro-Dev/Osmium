package com.intro.module;

import com.intro.Osmium;
import com.intro.module.event.Event;
import net.minecraft.client.MinecraftClient;

public abstract class Module {

    public Module(String name) {
        this.name = name;
        Osmium.modules.add(this);
    }

    public String name;

    public MinecraftClient mc = MinecraftClient.getInstance();

    public abstract void OnEvent(Event event);
}
