package com.intro.render.widget;

import com.intro.Osmium;
import com.intro.config.ColorOption;
import com.intro.render.Color;
import com.intro.util.TextureUtil;
import com.intro.util.Vector2d;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;


/**
 * <h1>ye who enter here abandon all hope</h1>
 * <p>This is by far the wost class I have ever had the torment of writing.</p>
 * <p>Nothing here makes sense, please don't look into it.</p>
 * <p>But if you do, see the comments to avoid wasting 8 hours of your life</p>
 *
 * @author Intro
 * @since 1.0.7
 * @see Color
 * @see com.intro.config.Options
 * @see com.intro.render.screen.OsmiumBlockOptionsScreen
 */
public class ColorOptionWidget extends DrawableHelper implements Element, Drawable, Selectable {

    private final ColorOption attachedOption;

    private static MinecraftClient mc = MinecraftClient.getInstance();

    private final Identifier BAKED_TEXTURE = new Identifier("osmium", "/textures/gui/gradient.png");

    private final NativeImage TEXTURE;

    private final int x;
    private final int y;

    // width and height are always the same as baked image
    private final int WIDTH = 256;
    private final int HEIGHT = 256;

    private double scale;



    /*
    I have no idea how this works
    On class init it loads a NativeImage object
    from an identifier, but after that you have to load the image
    again from a shader on the first render.
    you have to do both, or it won't work
    please help me

    edit: turns out im stupid
    */
    public ColorOptionWidget(int x, int y, ColorOption attachedOption) {
        this.attachedOption = attachedOption;
        this.x = x;
        this.y = y;
        this.TEXTURE = TextureUtil.convertIdentifierToNativeImage(BAKED_TEXTURE);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BAKED_TEXTURE);
        this.scale = 1;
        drawTexture(matrices, x, y, 0, 0, 256, 256);
        drawCenteredText(matrices, mc.textRenderer, Osmium.options.getColorOption(this.attachedOption.identifier).color.toStringNoAlpha(), x + (TEXTURE.getWidth() / 2), y + TEXTURE.getHeight() + 20, 0xffffff);
        drawCenteredText(matrices, mc.textRenderer, new TranslatableText("osmium.widget.colorpicker"), x + (TEXTURE.getWidth() / 2), y - 20, 0xffffff);
    }

    @Override
    public SelectionType getType() {
        return SelectionType.NONE;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    @SuppressWarnings("deprecated")
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(this.isPositionWithinBounds((int) mouseX, ((int) mouseY))) {
            Vector2d vec2 = this.getImagePixels((int) mouseX, (int) mouseY);
            int color;
            // for some reason NativeImage isn't reading color data properly.
            // so we gotta get the base pixel data
            int[] bytes;
            bytes = TEXTURE.makePixelArray();
            color = getColorAtLocation(bytes, TEXTURE, (int) vec2.getX(), (int) vec2.getY());

            Osmium.options.getColorOption(this.attachedOption.identifier).color = new Color(color);
        }
        return Element.super.mouseClicked(mouseX, mouseY, button);
    }


    // gets image coordinates from screen coordinates
    public Vector2d getImagePixels(int screenX, int screenY) {
        Screen screen = mc.currentScreen;
        // compute scale
        double imageScale = Math.min(screen.width / TEXTURE.getWidth(), screen.height / TEXTURE.getHeight()) * this.scale;

        // compute image offset
        double scaledWidth = this.WIDTH * imageScale;
        double scaledHeight = this.HEIGHT * imageScale;

        // clamp image coordinates
        if(screenX < this.x || this.x + scaledWidth < screenX) {
            return null;
        }
        if(screenY < this.y || this.y + scaledHeight < screenY) {
            return null;
        }

        // normalize output
        double x = (screenX - this.x) / imageScale;
        double y = (screenY - this.y) / imageScale;

        // de-scale output
        x *= 2;
        y *= 2;

        return new Vector2d(x, y);
    }

    public boolean isPositionWithinBounds(int x, int y) {
        return x > this.x  && x < this.x + this.WIDTH && y > this.y && y < this.y + HEIGHT;
    }

    
    public int getColorAtLocation(int[] arr, NativeImage baseImage, int x, int y) {
        int index = (baseImage.getWidth() * y) + x;
        return arr[index];
    }


}
