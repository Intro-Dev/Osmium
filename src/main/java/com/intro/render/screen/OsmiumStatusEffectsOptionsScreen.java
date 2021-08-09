package com.intro.render.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;

public class OsmiumStatusEffectsOptionsScreen extends Screen {

    private final Screen PARENT;

    private MinecraftClient mc = MinecraftClient.getInstance();


    public OsmiumStatusEffectsOptionsScreen(Screen parent) {
        super(new TranslatableText("osmium.statuseffectsoptionsscreen.title"));
        this.PARENT = parent;
    }
}
