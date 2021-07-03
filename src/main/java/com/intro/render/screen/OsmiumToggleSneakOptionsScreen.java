package com.intro.render.screen;

import com.intro.config.BooleanOption;
import com.intro.config.OptionUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

public class OsmiumToggleSneakOptionsScreen extends Screen {

    private Screen parent;
    private MinecraftClient mc = MinecraftClient.getInstance();

    private ButtonWidget BackButton;
    private ButtonWidget ToggleSprintToggleWidget;
    private ButtonWidget ToggleSneakToggleWidget;


    public OsmiumToggleSneakOptionsScreen(Screen parent) {
        super(new TranslatableText("osmium.options.togglesneaksettings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        BackButton = new ButtonWidget(this.width / 2 - 100, this.height / 6 + 200, 200, 20, new TranslatableText("osmium.options.videooptions.back"), (buttonWidget) -> {
            mc.openScreen(this.parent);
        });

        if(((BooleanOption) OptionUtil.Options.ToggleSprintEnabled.get()).variable) {
            ToggleSprintToggleWidget = new ButtonWidget(this.width / 2 - 175, this.height / 6 + 20, 150, 20, new TranslatableText("osmium.options.togglesprintenabled"), (buttonWidget) -> {
                ((BooleanOption) OptionUtil.Options.ToggleSprintEnabled.get()).variable = !((BooleanOption) OptionUtil.Options.ToggleSprintEnabled.get()).variable;
                if(((BooleanOption) OptionUtil.Options.ToggleSprintEnabled.get()).variable) {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.togglesprintenabled"));
                } else {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.togglesprintdisabled"));
                }
            });
        } else {
            ToggleSprintToggleWidget = new ButtonWidget(this.width / 2 - 175, this.height / 6 + 20, 150, 20, new TranslatableText("osmium.options.togglesprintdisabled"), (buttonWidget) -> {
                ((BooleanOption) OptionUtil.Options.ToggleSprintEnabled.get()).variable = !((BooleanOption) OptionUtil.Options.ToggleSprintEnabled.get()).variable;
                if(((BooleanOption) OptionUtil.Options.ToggleSprintEnabled.get()).variable) {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.togglesprintenabled"));
                } else {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.togglesprintdisabled"));
                }
            });
        }

        if(((BooleanOption) OptionUtil.Options.ToggleSneakEnabled.get()).variable) {
            ToggleSneakToggleWidget = new ButtonWidget(this.width / 2 + 25, this.height / 6 + 20, 150, 20, new TranslatableText("osmium.options.togglesneakenabled"), (buttonWidget) -> {
                ((BooleanOption) OptionUtil.Options.ToggleSneakEnabled.get()).variable = !((BooleanOption) OptionUtil.Options.ToggleSneakEnabled.get()).variable;
                if(((BooleanOption) OptionUtil.Options.ToggleSneakEnabled.get()).variable) {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.togglesneakenabled"));
                } else {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.togglesneakdisabled"));
                }
            });
        } else {
            ToggleSneakToggleWidget = new ButtonWidget(this.width / 2 + 25, this.height / 6 + 20, 150, 20, new TranslatableText("osmium.options.togglesneakdisabled"), (buttonWidget) -> {
                ((BooleanOption) OptionUtil.Options.ToggleSneakEnabled.get()).variable = !((BooleanOption) OptionUtil.Options.ToggleSneakEnabled.get()).variable;
                if(((BooleanOption) OptionUtil.Options.ToggleSneakEnabled.get()).variable) {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.togglesneakenabled"));
                } else {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.togglesneakdisabled"));
                }
            });
        }

        this.addDrawableChild(ToggleSneakToggleWidget);
        this.addDrawableChild(ToggleSprintToggleWidget);
        this.addDrawableChild(BackButton);
    }

    @Override
    public void onClose() {
        super.onClose();
        OptionUtil.save();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, mc.textRenderer, new TranslatableText("osmium.options.togglesneaksettings"), this.width / 2, 15, 0xffffff);
        drawTextWithShadow(matrices, mc.textRenderer, new TranslatableText("osmium.version"), 20, this.height - 20, 0xffffff);

        super.render(matrices, mouseX, mouseY, delta);
    }
}
