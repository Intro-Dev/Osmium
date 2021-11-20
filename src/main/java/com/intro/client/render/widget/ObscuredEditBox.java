package com.intro.client.render.widget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class ObscuredEditBox extends EditBox {

    public ObscuredEditBox(Font font, int i, int j, int k, int l, Component component) {
        super(font, i, j, k, l, component);
    }

    public ObscuredEditBox(Font font, int i, int j, int k, int l, @Nullable EditBox editBox, Component component) {
        super(font, i, j, k, l, editBox, component);
    }

}
