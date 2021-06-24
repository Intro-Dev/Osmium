package com.intro.module;

import com.intro.Osmium;
import com.intro.render.OsmiumOptionsScreen;
import com.intro.module.event.Event;
import com.intro.module.event.EventTick;
import net.minecraft.client.MinecraftClient;

public class Gui extends Module{

    //Text text;
    public boolean GuiOpen = false;
   //private ArrayList<Text> guiText = new ArrayList<Text>();



    public Gui() {
        super("Gui");
        //text = new Text(200, 100, "", 0xffffff);
    }

    public void OnEvent(Event event) {
        if(mc.player != null) {
            if(event instanceof EventTick) {
                if(Osmium.menuKey.wasPressed()) {
                    if(mc.currentScreen instanceof OsmiumOptionsScreen) {
                        mc.currentScreen.onClose();
                    } else {
                        mc.openScreen(new OsmiumOptionsScreen(null));
                    }
                }
            }
        }
    }
}
