package com.intro.render;

import com.intro.OsmiumOptions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;


public class OsmiumOptionsScreen extends Screen {

    private Screen parent;

    public OsmiumOptionsScreen(Screen parent) {
        super(new TranslatableText("osmium.options.title"));
        this.parent = parent;
    }

    ButtonWidget ToggleSneakToggleWidget;
    ButtonWidget FullbrightWidget;
    ButtonWidget HurtBobWidget;
    ButtonWidget NoFireWidget;
    ButtonWidget NoSneakSquishWidget;
    ButtonWidget SmoothSneakWidget;
    ButtonWidget OpenVideoOptions;
    ButtonWidget BackButton;


    MinecraftClient mc = MinecraftClient.getInstance();

    /*
    Panel registration
    Very Innfecient space wise but I couldent bother working out a better solution
    TODO Make this good
     */


    @Override
    protected void init() {
        if(OsmiumOptions.ToggleSprintEnabled) {
            ToggleSneakToggleWidget = new ButtonWidget(this.width / 2 - 75, this.height / 6 + 20, 150, 20, new TranslatableText("osmium.options.togglesprintenabled"), (buttonWidget) -> {
                OsmiumOptions.ToggleSprintEnabled = !OsmiumOptions.ToggleSprintEnabled;
                if(OsmiumOptions.ToggleSprintEnabled) {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.togglesprintenabled"));
                } else {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.togglesprintdisabled"));
                }
            });
        } else {
            ToggleSneakToggleWidget = new ButtonWidget(this.width / 2 - 75, this.height / 6 + 20, 150, 20, new TranslatableText("osmium.options.togglesprintdisabled"), (buttonWidget) -> {
                OsmiumOptions.ToggleSprintEnabled = !OsmiumOptions.ToggleSprintEnabled;
                if(OsmiumOptions.ToggleSprintEnabled) {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.togglesprintenabled"));
                } else {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.togglesprintdisabled"));
                }
            });
        }
        if(OsmiumOptions.FullbrightEnabled) {
            FullbrightWidget = new ButtonWidget(this.width / 2 - 275, this.height / 6 + 20, 150, 20, new TranslatableText("osmium.options.fullbrightenabled"), (buttonWidget) -> {
                OsmiumOptions.FullbrightEnabled = !OsmiumOptions.FullbrightEnabled;
                if(OsmiumOptions.FullbrightEnabled) {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.fullbrightenabled"));
                } else {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.fullbrightdisabled"));
                }
            });
        } else {
            FullbrightWidget = new ButtonWidget(this.width / 2 - 275, this.height / 6 + 20, 150, 20, new TranslatableText("osmium.options.fullbrightdisabled"), (buttonWidget) -> {
                OsmiumOptions.FullbrightEnabled = !OsmiumOptions.FullbrightEnabled;
                if(OsmiumOptions.FullbrightEnabled) {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.fullbrightenabled"));
                } else {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.fullbrightdisabled"));
                }
            });
        }
        if(OsmiumOptions.HurtBobbingModEnabled) {
            HurtBobWidget = new ButtonWidget(this.width / 2 + 125, this.height / 6 + 20, 150, 20, new TranslatableText("osmium.options.hurtbobbingenabled"), (buttonWidget) -> {
                OsmiumOptions.HurtBobbingModEnabled = !OsmiumOptions.HurtBobbingModEnabled;
                if(OsmiumOptions.HurtBobbingModEnabled) {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.hurtbobbingenabled"));
                } else {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.hurtbobbingdisabled"));
                }
            });
        } else {
            HurtBobWidget = new ButtonWidget(this.width / 2 + 125, this.height / 6 + 20, 150, 20, new TranslatableText("osmium.options.hurtbobbingdisabled"), (buttonWidget) -> {
                OsmiumOptions.HurtBobbingModEnabled = !OsmiumOptions.HurtBobbingModEnabled;
                if(OsmiumOptions.HurtBobbingModEnabled) {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.hurtbobbingenabled"));
                } else {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.hurtbobbingdisabled"));
                }
            });
        }

        if(OsmiumOptions.NoFireEnabled) {
            NoFireWidget = new ButtonWidget(this.width / 2 - 275, this.height / 6 + 60, 150, 20, new TranslatableText("osmium.options.nofireenabled"), (buttonWidget) -> {
                OsmiumOptions.NoFireEnabled = !OsmiumOptions.NoFireEnabled;
                if(OsmiumOptions.NoFireEnabled) {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.nofireenabled"));
                } else {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.nofiredisabled"));
                }
            });
        } else {
            NoFireWidget = new ButtonWidget(this.width / 2 - 275, this.height / 6 + 60, 150, 20, new TranslatableText("osmium.options.nofiredisabled"), (buttonWidget) -> {
                OsmiumOptions.NoFireEnabled = !OsmiumOptions.NoFireEnabled;
                if(OsmiumOptions.NoFireEnabled) {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.nofireenabled"));
                } else {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.nofiredisabled"));
                }
            });
        }

        if(OsmiumOptions.SmoothSneak) {
            SmoothSneakWidget = new ButtonWidget(this.width / 2 + 125, this.height / 6 + 60, 150, 20, new TranslatableText("osmium.options.smoothsneakenabled"), (buttonWidget) -> {
                OsmiumOptions.SmoothSneak = !OsmiumOptions.SmoothSneak;
                if(OsmiumOptions.SmoothSneak) {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.smoothsneakenabled"));
                } else {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.smoothsneakdisabled"));
                }
            });
        } else {
            SmoothSneakWidget = new ButtonWidget(this.width / 2 + 125, this.height / 6 + 60, 150, 20, new TranslatableText("osmium.options.smoothsneakdisabled"), (buttonWidget) -> {
                OsmiumOptions.SmoothSneak = !OsmiumOptions.SmoothSneak;
                if(OsmiumOptions.SmoothSneak) {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.smoothsneakenabled"));
                } else {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.smoothsneakdisabled"));
                }
            });
        }

        if(OsmiumOptions.NoSquishySneak) {
            NoSneakSquishWidget = new ButtonWidget(this.width / 2 - 75, this.height / 6 + 60, 150, 20, new TranslatableText("osmium.options.nosquishysneakenabled"), (buttonWidget) -> {
                OsmiumOptions.NoSquishySneak = !OsmiumOptions.NoSquishySneak;
                if(OsmiumOptions.NoSquishySneak) {
                    System.out.println("got here 0");
                    buttonWidget.setMessage(new TranslatableText("osmium.options.nosquishysneakenabled"));
                } else {
                    System.out.println("got here 3");
                    buttonWidget.setMessage(new TranslatableText("osmium.options.nosquishysneakdisabled"));
                }
            });
        } else {
            NoSneakSquishWidget = new ButtonWidget(this.width / 2 - 75, this.height / 6 + 60, 150, 20, new TranslatableText("osmium.options.nosquishysneakdisabled"), (buttonWidget) -> {
                OsmiumOptions.NoSquishySneak = !OsmiumOptions.NoSquishySneak;
                if(OsmiumOptions.NoSquishySneak) {
                    System.out.println("got here 1");
                    buttonWidget.setMessage(new TranslatableText("osmium.options.nosquishysneakenabled"));
                } else {
                    System.out.println("got here 4");
                    buttonWidget.setMessage(new TranslatableText("osmium.options.nosquishysneakdisabled"));
                }
            });
        }

        OpenVideoOptions = new ButtonWidget(this.width / 2 - 275, this.height / 6 + 100, 150, 20, new TranslatableText("osmium.options.videooptions"), (buttonWidget) -> {
            mc.openScreen(new OsmiumVideoOptionsScreen(this));
        });

        BackButton = new ButtonWidget(this.width / 2 - 100, this.height / 6 + 200, 200, 20, new TranslatableText("osmium.options.videooptions.back"), (buttonWidget) -> {
            mc.openScreen(this.parent);
        });



        this.addButton(FullbrightWidget);
        this.addButton(ToggleSneakToggleWidget);
        this.addButton(HurtBobWidget);
        this.addButton(NoFireWidget);
        this.addButton(NoSneakSquishWidget);
        this.addButton(SmoothSneakWidget);
        this.addButton(OpenVideoOptions);
        this.addButton(BackButton);
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, mc.textRenderer, new TranslatableText("osmium.options.title"), this.width / 2, 20, 0xffffff);
        super.render(matrices, mouseX, mouseY, delta);
    }



}
