package dev.lobstershack.client.render.screen.builder;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.lobstershack.client.config.options.Option;
import dev.lobstershack.client.render.widget.AbstractScalableButton;
import dev.lobstershack.client.render.widget.BooleanButtonWidget;
import dev.lobstershack.client.render.widget.DoubleSliderWidget;
import dev.lobstershack.client.render.widget.EnumSelectWidget;
import dev.lobstershack.client.util.DebugUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Level;

import java.util.*;

public class ScreenBuilderImpl implements ScreenBuilder {

    // list of all widgets
    // a gross hack having it as just Object but the alternative is creating like 5 different lists, and I don't really want to do that
    private final List<Object> widgets = new ArrayList<>();

    private final List<RenderConsumer> renderConsumers = new ArrayList<>();
    private final List<Runnable> initConsumers = new ArrayList<>();

    private final Map<Renderable, Set<WidgetConsumer>> initWidgetConsumers = new HashMap();

    private int widgetX = -275;
    private int placeInRow = 1;
    private int widgetY = 80;

    private Screen parent;

    private final Minecraft mc = Minecraft.getInstance();

    @Override
    public ScreenBuilder onRender(RenderConsumer consumer) {
        renderConsumers.add(consumer);
        return this;
    }

    @Override
    public <T extends GuiEventListener & Renderable & NarratableEntry> ScreenBuilder widget(T widget) {
        DebugUtil.logIfDebug("Adding widget to screen: " + widget.toString(), Level.INFO);
        widgets.add(widget);
        return this;
    }

    @Override
    public ScreenBuilder button(Option<?> option, String translationKey) {
        if(option.get() instanceof Boolean) {
            widget(new BooleanButtonWidget(mc.getWindow().getGuiScaledWidth() / 2 + widgetX, mc.getWindow().getScreenHeight() / 8 + widgetY, 150, 20, (Option<Boolean>) option, translationKey));
        }
        if (option.get() instanceof Enum<?>) {
            widget(new EnumSelectWidget(mc.getWindow().getGuiScaledWidth() / 2 + widgetX, mc.getWindow().getScreenHeight() / 8 + widgetY, 150, 20, (Option<Enum<?>>) option, translationKey));
        }
        incrementButton();
        return this;
    }

    @Override
    public ScreenBuilder button(int x, int y, int width, int height, Component text, Button.OnPress onPress) {
        widget(new AbstractScalableButton(x, y, width, height, text, onPress, 1f));
        return this;
    }

    @Override
    public ScreenBuilder button(int x, int y, int width, int height, Component text, Button.OnPress onPress, float scale) {
        widget(new AbstractScalableButton(x, y, width, height, text, onPress, scale));
        return this;
    }

    @Override
    public ScreenBuilder button(Option<?> option, String translationKey, WidgetConsumer afterInit) {
        if(option.get() instanceof Boolean) {
            BooleanButtonWidget widget = new BooleanButtonWidget(mc.getWindow().getGuiScaledWidth() / 2 + widgetX, mc.getWindow().getScreenHeight() / 8 + widgetY, 150, 20, (Option<Boolean>) option, translationKey);
            widget(widget);
            addWidgetConsumer(widget, afterInit);
        }
        if (option.get() instanceof Enum<?>) {
            EnumSelectWidget widget = new EnumSelectWidget(mc.getWindow().getGuiScaledWidth() / 2 + widgetX, mc.getWindow().getScreenHeight() / 8 + widgetY, 150, 20, (Option<Enum<?>>) option, translationKey);
            widget(widget);
            addWidgetConsumer(widget, afterInit);
        }
        incrementButton();
        return this;
    }

    @Override
    public ScreenBuilder button(Component text, Button.OnPress onPress) {
        widget(new AbstractScalableButton(mc.getWindow().getGuiScaledWidth() / 2 + widgetX, mc.getWindow().getScreenHeight() / 8 + widgetY, 150, 20, text, onPress));
        incrementButton();
        return this;
    }

    @Override
    public ScreenBuilder button(Component text, Button.OnPress onPress, WidgetConsumer afterInit) {
        AbstractScalableButton button = new AbstractScalableButton(mc.getWindow().getGuiScaledWidth() / 2 + widgetX, mc.getWindow().getScreenHeight() / 8 + widgetY, 150, 20, text, onPress);
        widget(button);
        incrementButton();
        addWidgetConsumer(button, afterInit);
        return this;
    }

    @Override
    public ScreenBuilder addBackButton(Screen parent) {
        this.parent = parent;
        return this;
    }

    @Override
    public ScreenBuilder onInit(Runnable runnable) {
        initConsumers.add(runnable);
        return this;
    }

    @Override
    public ScreenBuilder slider(Option<Double> option, String translationKey, double minVal, double maxVal, double roundTo) {
        widget(new DoubleSliderWidget(mc, mc.getWindow().getGuiScaledWidth() / 2 + widgetX, mc.getWindow().getScreenHeight() / 8 + widgetY, 150, 20, option, translationKey, minVal, maxVal, roundTo));
        incrementButton();
        return this;
    }

    private void incrementButton() {
        widgetX += 200;
        placeInRow++;
        if(placeInRow > 3) {
            placeInRow = 1;
            widgetY += 40;
            widgetX = -275;
        }
    }

    private void addWidgetConsumer(Renderable widget, WidgetConsumer consumer) {
        initWidgetConsumers.computeIfAbsent(widget, k -> new HashSet<>());
        initWidgetConsumers.get(widget).add(consumer);
    }

    @Override
    public Screen build(Component title) {
        return new Screen(title) {

            private int globalOffset = 0;
            private int logoOffset = 0;
            private boolean shouldRenderLogo = true;
            private int finalOffset = 0;
            private final ResourceLocation LOGO_TEXTURE = new ResourceLocation("osmium", "icon.png");

            @Override
            public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
                PoseStack stack = graphics.pose();
                renderBackground(graphics);
                RenderSystem.enableBlend();
                stack.pushPose();
                // scale image down to a good size
                stack.scale(0.5f, 0.5f, 0.5f);
                // account for scaling difference
                // its width / 2 - 128 because we are scaling by 0.5, and 128 is the scaled dimensions of the image
                stack.translate(this.width / 2f - 128, finalOffset,0);
                if(shouldRenderLogo)
                    graphics.blit(LOGO_TEXTURE, this.width / 2, this.height / 8 + globalOffset + logoOffset, 0, 0, 256, 256);
                stack.popPose();

                stack.pushPose();
                stack.translate(0, finalOffset,0);
                graphics.drawCenteredString(mc.font, Component.translatable("osmium.version"), this.width / 2, this.height / 8 + 100 + globalOffset + (logoOffset / 4), 0xffffff);
                stack.popPose();
                renderConsumers.forEach(consumer -> consumer.onRender(stack, partialTicks));
                super.render(graphics, mouseX, mouseY, partialTicks);
            }

            @Override
            protected void init() {

                if(mc.options.guiScale().get() > 2) {
                    logoOffset = -40;
                }
                if(mc.options.guiScale().get() > 4) {
                    shouldRenderLogo = false;
                    logoOffset = -80;
                    globalOffset = -64;
                }

                if(mc.options.guiScale().get() == 0) {
                    finalOffset = 57;
                }  else {
                    finalOffset = 57 / mc.options.guiScale().get();
                }


                widgets.forEach((w) -> {
                    add(w);
                    initWidgetConsumers.computeIfAbsent((Renderable) w, k -> new HashSet<>());
                    initWidgetConsumers.get((Renderable) w).forEach(consumer -> consumer.onTick((Renderable) w));
                });
                initConsumers.forEach(Runnable::run);
                if(parent != null) {
                    addRenderableWidget(new AbstractScalableButton(this.width / 2 - 100, this.height / 4 + 225, 200, 20, Component.translatable("osmium.options.video_options.back"), (Button) -> mc.setScreen(parent)));
                }
                super.init();
            }

            @SuppressWarnings("uncheckedcast")
            private <T extends GuiEventListener & Renderable & NarratableEntry> void add(Object widget) {
                addRenderableWidget((T) widget);
            }

            @Override
            protected void rebuildWidgets() {
                init();
                super.rebuildWidgets();
            }
        };
    }
}
