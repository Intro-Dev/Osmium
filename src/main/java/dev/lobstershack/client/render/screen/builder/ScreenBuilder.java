package dev.lobstershack.client.render.screen.builder;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.lobstershack.common.config.options.Option;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * A class for creating generic ui screens
 * Can be implemented to create various themes
 *
 * @author Intro
 * @since 1.3
 */
public interface ScreenBuilder {

    /**
     * @return Returns a new instance of the set ScreenBuilder
     */
    static ScreenBuilder newInstance() {
        return new ScreenBuilderImpl();
    }

    /**
     * Adds a method to be executed on render
     * @param consumer The method to be executed
     * @return Returns the current ScreenBuilder
     */
    ScreenBuilder onRender(RenderConsumer consumer);

    /**
     * Adds a widget to be added to the screen
     * @param widget The widget to be added
     * @return Returns the current ScreenBuilder
     */
    <T extends GuiEventListener & Renderable & NarratableEntry> ScreenBuilder widget(T widget);

    /**
     * Adds a generic option button. Automatically offsets the buttons each time a new one is added.
     * Only works with Enum and Boolean options
     * @param option The option to be used
     * @param translationKey The translation key of the option
     * @return Returns the current ScreenBuilder
     */
    ScreenBuilder button(Option<?> option, String translationKey);

    /**
     * Adds a button with custom function and position
     * @param x Button x
     * @param y Button y
     * @param width Button width
     * @param height Button height
     * @param text Button text
     * @param onPress Button function
     * @return Returns the current ScreenBuilder
     */
    ScreenBuilder button(int x, int y, int width, int height, Component text, Button.OnPress onPress);

    /**
     * Adds a button with custom function, position, and scale
     * @param x Button x
     * @param y Button y
     * @param width Button width
     * @param height Button height
     * @param text Button text
     * @param onPress Button function
     * @param scale Button scale
     * @return Returns the current ScreenBuilder
     */
    ScreenBuilder button(int x, int y, int width, int height, Component text, Button.OnPress onPress, float scale);

    /**
     * Adds a generic option button. Automatically offsets the buttons each time a new one is added.
     * Only works with Enum and Boolean options
     * @param option The option to be used
     * @param translationKey The translation key of the option
     * @return Returns the current ScreenBuilder
     */
    ScreenBuilder button(Option<?> option, String translationKey, WidgetConsumer afterInit);

    /**
     * Adds a button with custom function
     * @param text Button text
     * @param onPress Button function
     * @return Returns the current ScreenBuilder
     */
    ScreenBuilder button(Component text, Button.OnPress onPress);

    /**
     * Adds a back button
     * @param parent The screen to return to
     * @return Returns the current ScreenBuilder
     */
    ScreenBuilder addBackButton(Screen parent);

    /**
     * Adds a listener that will run on screen init
     * @param runnable Listener function
     * @return Returns the current ScreenBuilder
     */
    ScreenBuilder onInit(Runnable runnable);

    /**
     * Adds a double option slider
     * @param option The option to be used
     * @param translationKey The translation key of the option
     * @param minVal The minimum value of the slider
     * @param maxVal The maximum value of the slider
     * @param roundTo The place the slider will round too
     * @return Returns the current ScreenBuilder
     */
    ScreenBuilder slider(Option<Double> option, String translationKey, double minVal, double maxVal, double roundTo);

    /**
     * Builds the given data into a screen
     * @param title The title of the built screen
     * @return The built screen
     */
    Screen build(Component title);

    interface RenderConsumer {

        void onRender(PoseStack stack, float tickDelta);

    }

    interface WidgetConsumer {

        void onTick(Renderable widget);

    }

}
