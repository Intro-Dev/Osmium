package com.intro.render.screen;

import com.intro.config.BooleanOption;
import com.intro.config.EnumOption;
import com.intro.config.OptionUtil;
import com.intro.config.SneakMode;
import com.intro.render.widget.BooleanButtonWidget;
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
    ButtonWidget OpenGuiEditing;
    ButtonWidget FpsWidget;


    MinecraftClient mc = MinecraftClient.getInstance();

    /*
    Made this good
     */


    @Override
    protected void init() {





        FullbrightWidget = new BooleanButtonWidget(this.width / 2 - 275, this.height / 6 + 20, 150, 20, (BooleanOption) OptionUtil.Options.FullbrightEnabled.get(), "osmium.options.fullbright");
        HurtBobWidget = new BooleanButtonWidget(this.width / 2 + 125, this.height / 6 + 20, 150, 20,((BooleanOption) OptionUtil.Options.HurtbobbingEnabled.get()), "osmium.options.hurtbobbing");
        NoFireWidget = new BooleanButtonWidget(this.width / 2 - 275, this.height / 6 + 60, 150, 20, ((BooleanOption) OptionUtil.Options.NoFireEnabled.get()), "osmium.options.nofire");
        FpsWidget = new BooleanButtonWidget(this.width / 2 - 75, this.height / 6 + 100, 150, 20, ((BooleanOption) OptionUtil.Options.FpsEnabled.get()), "osmium.options.fps");



        SmoothSneakWidget = new ButtonWidget(this.width / 2 + 125, this.height / 6 + 60, 150, 20, new TranslatableText("osmium.options.sneak" + ((EnumOption) OptionUtil.Options.SneakMode.get()).variable.toString().toLowerCase()), (buttonWidget) -> {
            ((EnumOption) OptionUtil.Options.SneakMode.get()).variable = ((SneakMode) ((EnumOption) OptionUtil.Options.SneakMode.get()).variable).next();
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

        OpenGuiEditing = new ButtonWidget(this.width / 2 - 275, this.height / 6 + 100, 150, 20, new TranslatableText("osmium.guiedit.title"), (buttonWidget) -> {
            mc.openScreen(new OsmiumGuiEditScreen(this));
        });

        ToggleSneakToggleWidget = new ButtonWidget(this.width / 2 - 75, this.height / 6 + 20, 150, 20, new TranslatableText("osmium.options.togglesneaksettings"), (buttonWidget) -> {
            mc.openScreen(new OsmiumToggleSneakOptionsScreen(this));
        });




        this.addDrawableChild(FullbrightWidget);
        this.addDrawableChild(ToggleSneakToggleWidget);
        this.addDrawableChild(HurtBobWidget);
        this.addDrawableChild(NoFireWidget);
        this.addDrawableChild(SmoothSneakWidget);
        this.addDrawableChild(OpenVideoOptions);
        this.addDrawableChild(BackButton);
        this.addDrawableChild(OpenGuiEditing);
        this.addDrawableChild(FpsWidget);
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
