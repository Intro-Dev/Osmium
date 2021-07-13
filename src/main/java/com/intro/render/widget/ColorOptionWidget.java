package com.intro.render.widget;

import com.intro.Osmium;
import com.intro.Vector2d;
import com.intro.config.ColorOption;
import com.intro.mixin.ResourceTextureAccessor;
import com.intro.mixin.ResourceTextureSubclassAccessor;
import com.intro.render.Color;
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
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourceManager;
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

    // random defaults
    // actually loads from shader
    private NativeImage TEXTURE = new NativeImage(NativeImage.Format.ABGR, 256, 256, false);

    private boolean firstRun = true;

    private final int x;
    private final int y;

    // width and height are always the same as baked image
    private final int WIDTH = 256;
    private final int HEIGHT = 256;



    /*
    I have no idea how this works
    On class init it loads a NativeImage object
    from an identifier, but after that you have to load the image
    again from a shader on the first render.
    you have to do both or it wont work
    please help me
    */
    public ColorOptionWidget(int x, int y, ColorOption attachedOption) {
        this.attachedOption = attachedOption;
        this.x = x;
        this.y = y;
        mc.getTextureManager().bindTexture(BAKED_TEXTURE);
        ResourceTexture texture = (ResourceTexture) mc.getTextureManager().getTexture(BAKED_TEXTURE);
        try {
            TEXTURE = this.getNativeImage(mc.getResourceManager(), texture);
        } catch (Exception e) {
            System.out.println("Error in making color picking widget!");
            e.printStackTrace();
        }
    }
    
    
    

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BAKED_TEXTURE);
        drawTexture(matrices, x, y, 0, 0, 256, 256);
        mc.textRenderer.drawWithShadow(matrices, Osmium.options.getColorOption(this.attachedOption.identifier).color.toString(), (float) x, (float) y + 20, 0xffffff);
        drawCenteredTextWithShadow(matrices, mc.textRenderer, new TranslatableText("osmium.widget.colorpicker"));
        // this hack is nasty
        // loads a NativeImage object from the current shader texture
        // this gotta be like this because I can't find a way to get a NativeImage from an Identifier
        if(firstRun) {
            // 0 is default texture level
            TEXTURE.loadFromTextureImage(0, false);
            System.out.println("loading texture from shader");
            firstRun = false;
        }

    }

    @Override
    public SelectionType getType() {
        return SelectionType.NONE;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(this.isPositionWithinBounds((int) mouseX, ((int) mouseY))) {
            Vector2d vec2 = this.getImagePixels((int) mouseX, (int) mouseY);
            Osmium.options.getColorOption(this.attachedOption.identifier).color = new Color(TEXTURE.getPixelColor((int) vec2.getX(), (int) vec2.getY()));
        }
        return Element.super.mouseClicked(mouseX, mouseY, button);
    }


    // gets image coordinates from screen coordinates
    public Vector2d getImagePixels(int screenX, int screenY) {
        Screen screen = mc.currentScreen;
        // compute scale
        double imageScale = Math.min(screen.width / this.WIDTH, screen.height / this.HEIGHT);

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

        return new Vector2d(x, y);
    }

    public boolean isPositionWithinBounds(int x, int y) {
        return x > this.x  && x < this.x + this.WIDTH && y > this.y && y < this.y + HEIGHT;
    }

    // this is by far the most disgusting code i have ever produced
    // the spaghetti is getting to me
    public NativeImage getNativeImage(ResourceManager manager, ResourceTexture texture) {
        ResourceTexture.TextureData textureData = this.loadTextureData(manager, texture);
        NativeImage nativeImage = ((ResourceTextureSubclassAccessor) textureData).getImage();
        return nativeImage;

    }

    protected ResourceTexture.TextureData loadTextureData(ResourceManager resourceManager, ResourceTexture texture) {
        return ResourceTexture.TextureData.load(resourceManager, ((ResourceTextureAccessor) texture).getLocation());
    }


}
