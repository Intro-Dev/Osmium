package com.intro.render;

import com.intro.BlockEntityCullingMode;
import com.intro.OsmiumOptions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.options.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class OsmiumVideoOptionsScreen extends Screen {

    private Screen parent;
    private MinecraftClient mc = MinecraftClient.getInstance();

    private ButtonWidget BackButton;
    private ButtonWidget ToggleBECullingWidget;
    private ButtonWidget ToggleCapeWidget;

    public OsmiumVideoOptionsScreen(Screen parent) {
        super(new TranslatableText("osmium.options.videooptions.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        BackButton = new ButtonWidget(this.width / 2 - 100, this.height / 6 + 200, 200, 20, new TranslatableText("osmium.options.videooptions.back"), (buttonWidget) -> {
            mc.openScreen(this.parent);
        });

            ToggleBECullingWidget = new ButtonWidget(this.width / 2 - 275, this.height / 6 + 20, 150, 20, new TranslatableText("osmium.options.videooptions.beculling" + OsmiumOptions.BlockEntityCulling.toString().toLowerCase()), (buttonWidget) -> {
                OsmiumOptions.BlockEntityCulling = OsmiumOptions.BlockEntityCulling.next();
                mc.worldRenderer.reload();
                switch (OsmiumOptions.BlockEntityCulling) {
                    case DISABLED:
                        buttonWidget.setMessage(new TranslatableText("osmium.options.videooptions.becullingdisabled"));
                        break;
                    case LOW:
                        buttonWidget.setMessage(new TranslatableText("osmium.options.videooptions.becullinglow"));
                        break;
                    case MEDIUM:
                        buttonWidget.setMessage(new TranslatableText("osmium.options.videooptions.becullingmedium"));
                        break;
                    case HIGH:
                        buttonWidget.setMessage(new TranslatableText("osmium.options.videooptions.becullinghigh"));
                        break;
                    case EXTREME:
                        buttonWidget.setMessage(new TranslatableText("osmium.options.videooptions.becullingextreme"));
                        break;

                }
            });
        ToggleCapeWidget = new ButtonWidget(this.width / 2 - 75, this.height / 6 + 20, 150, 20, new TranslatableText("osmium.options.videooptions.cape" + OsmiumOptions.CustomCapeModes.toString().toLowerCase()), (buttonWidget) -> {
            OsmiumOptions.CustomCapeModes = OsmiumOptions.CustomCapeModes.next();
            switch (OsmiumOptions.CustomCapeModes) {
                case DISABLED:
                    buttonWidget.setMessage(new TranslatableText("osmium.options.videooptions.capedisabled"));
                    break;
                case OPTIFINE:
                    buttonWidget.setMessage(new TranslatableText("osmium.options.videooptions.capeoptifine"));
                    break;
                case ALL:
                    buttonWidget.setMessage(new TranslatableText("osmium.options.videooptions.capeall"));
                    break;


            }
        });

        this.addButton(BackButton);
        this.addButton(ToggleBECullingWidget);
        this.addButton(ToggleCapeWidget);
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, mc.textRenderer, new TranslatableText("osmium.options.videooptions.title"), this.width / 2, 20, 0xffffff);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
