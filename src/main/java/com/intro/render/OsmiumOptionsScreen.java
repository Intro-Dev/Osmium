package com.intro.render;

import com.intro.config.BooleanOption;
import com.intro.config.EnumOption;
import com.intro.config.OptionUtil;
import com.intro.config.SneakMode;
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
        if(((BooleanOption) OptionUtil.Options.ToggleSprintEnabled.get()).variable) {
            ToggleSneakToggleWidget = new ButtonWidget(this.width / 2 - 75, this.height / 6 + 20, 150, 20, new TranslatableText("osmium.options.togglesprintenabled"), (buttonWidget) -> {
                ((BooleanOption) OptionUtil.Options.ToggleSprintEnabled.get()).variable = !((BooleanOption) OptionUtil.Options.ToggleSprintEnabled.get()).variable;
                if(((BooleanOption) OptionUtil.Options.ToggleSprintEnabled.get()).variable) {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.togglesprintenabled"));
                } else {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.togglesprintdisabled"));
                }
            });
        } else {
            ToggleSneakToggleWidget = new ButtonWidget(this.width / 2 - 75, this.height / 6 + 20, 150, 20, new TranslatableText("osmium.options.togglesprintdisabled"), (buttonWidget) -> {
                ((BooleanOption) OptionUtil.Options.ToggleSprintEnabled.get()).variable = !((BooleanOption) OptionUtil.Options.ToggleSprintEnabled.get()).variable;
                if(((BooleanOption) OptionUtil.Options.ToggleSprintEnabled.get()).variable) {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.togglesprintenabled"));
                } else {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.togglesprintdisabled"));
                }
            });
        }
        if(((BooleanOption) OptionUtil.Options.FullbrightEnabled.get()).variable) {
            FullbrightWidget = new ButtonWidget(this.width / 2 - 275, this.height / 6 + 20, 150, 20, new TranslatableText("osmium.options.fullbrightenabled"), (buttonWidget) -> {
                ((BooleanOption) OptionUtil.Options.FullbrightEnabled.get()).variable = !((BooleanOption) OptionUtil.Options.FullbrightEnabled.get()).variable;
                if(((BooleanOption) OptionUtil.Options.FullbrightEnabled.get()).variable) {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.fullbrightenabled"));
                } else {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.fullbrightdisabled"));
                }
            });
        } else {
            FullbrightWidget = new ButtonWidget(this.width / 2 - 275, this.height / 6 + 20, 150, 20, new TranslatableText("osmium.options.fullbrightdisabled"), (buttonWidget) -> {
                ((BooleanOption) OptionUtil.Options.FullbrightEnabled.get()).variable = !((BooleanOption) OptionUtil.Options.FullbrightEnabled.get()).variable;
                if(((BooleanOption) OptionUtil.Options.FullbrightEnabled.get()).variable) {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.fullbrightenabled"));
                } else {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.fullbrightdisabled"));
                }
            });
        }
        if(((BooleanOption) OptionUtil.Options.HurtbobbingEnabled.get()).variable) {
            HurtBobWidget = new ButtonWidget(this.width / 2 + 125, this.height / 6 + 20, 150, 20, new TranslatableText("osmium.options.hurtbobbingenabled"), (buttonWidget) -> {
                ((BooleanOption) OptionUtil.Options.HurtbobbingEnabled.get()).variable = !((BooleanOption) OptionUtil.Options.HurtbobbingEnabled.get()).variable;
                if(((BooleanOption) OptionUtil.Options.HurtbobbingEnabled.get()).variable) {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.hurtbobbingenabled"));
                } else {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.hurtbobbingdisabled"));
                }
            });
        } else {
            HurtBobWidget = new ButtonWidget(this.width / 2 + 125, this.height / 6 + 20, 150, 20, new TranslatableText("osmium.options.hurtbobbingdisabled"), (buttonWidget) -> {
                ((BooleanOption) OptionUtil.Options.HurtbobbingEnabled.get()).variable = !((BooleanOption) OptionUtil.Options.HurtbobbingEnabled.get()).variable;
                if(((BooleanOption) OptionUtil.Options.HurtbobbingEnabled.get()).variable) {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.hurtbobbingenabled"));
                } else {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.hurtbobbingdisabled"));
                }
            });
        }

        if(((BooleanOption) OptionUtil.Options.NoFireEnabled.get()).variable) {
            NoFireWidget = new ButtonWidget(this.width / 2 - 275, this.height / 6 + 60, 150, 20, new TranslatableText("osmium.options.nofireenabled"), (buttonWidget) -> {
                ((BooleanOption) OptionUtil.Options.NoFireEnabled.get()).variable = !((BooleanOption) OptionUtil.Options.NoFireEnabled.get()).variable;
                if(((BooleanOption) OptionUtil.Options.NoFireEnabled.get()).variable) {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.nofireenabled"));
                } else {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.nofiredisabled"));
                }
            });
        } else {
            NoFireWidget = new ButtonWidget(this.width / 2 - 275, this.height / 6 + 60, 150, 20, new TranslatableText("osmium.options.nofiredisabled"), (buttonWidget) -> {
                ((BooleanOption) OptionUtil.Options.NoFireEnabled.get()).variable = !((BooleanOption) OptionUtil.Options.NoFireEnabled.get()).variable;
                if(((BooleanOption) OptionUtil.Options.NoFireEnabled.get()).variable) {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.nofireenabled"));
                } else {
                    buttonWidget.setMessage(new TranslatableText("osmium.options.nofiredisabled"));
                }
            });
        }


        SmoothSneakWidget = new ButtonWidget(this.width / 2 + 125, this.height / 6 + 60, 150, 20, new TranslatableText("osmium.options.sneak" + ((EnumOption) OptionUtil.Options.SneakMode.get()).variable.toString().toLowerCase()), (buttonWidget) -> {
            ((EnumOption) OptionUtil.Options.SneakMode.get()).variable = ((SneakMode) ((EnumOption) OptionUtil.Options.SneakMode.get()).variable).next();
            mc.worldRenderer.reload();
            if(((EnumOption) OptionUtil.Options.SneakMode.get()).variable == SneakMode.VANILLA) {
                buttonWidget.setMessage(new TranslatableText("osmium.options.sneakvanilla"));
            } else if(((EnumOption) OptionUtil.Options.SneakMode.get()).variable == SneakMode.SMOOTH) {
                buttonWidget.setMessage(new TranslatableText("osmium.options.sneaksmooth"));
            } else if(((EnumOption) OptionUtil.Options.SneakMode.get()).variable == SneakMode.INSTANT) {
                buttonWidget.setMessage(new TranslatableText("osmium.options.sneakinstant"));
            }





        });


        OpenVideoOptions = new ButtonWidget(this.width / 2 - 75, this.height / 6 + 60, 150, 20, new TranslatableText("osmium.options.videooptions"), (buttonWidget) -> {
            mc.openScreen(new OsmiumVideoOptionsScreen(this));
        });

        BackButton = new ButtonWidget(this.width / 2 - 100, this.height / 6 + 200, 200, 20, new TranslatableText("osmium.options.videooptions.back"), (buttonWidget) -> {
            mc.openScreen(this.parent);
        });



        this.addDrawableChild(FullbrightWidget);
        this.addDrawableChild(ToggleSneakToggleWidget);
        this.addDrawableChild(HurtBobWidget);
        this.addDrawableChild(NoFireWidget);
        this.addDrawableChild(SmoothSneakWidget);
        this.addDrawableChild(OpenVideoOptions);
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
        drawCenteredText(matrices, mc.textRenderer, new TranslatableText("osmium.options.title"), this.width / 2, 15, 0xffffff);
        drawTextWithShadow(matrices, mc.textRenderer, new TranslatableText("osmium.version"), 20, this.height - 20, 0xffffff);
        super.render(matrices, mouseX, mouseY, delta);
    }



}
