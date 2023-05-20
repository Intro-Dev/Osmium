package dev.lobstershack.client.render.widget;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.lobstershack.client.config.Options;
import dev.lobstershack.client.config.options.Option;
import dev.lobstershack.client.render.color.Color;
import dev.lobstershack.client.render.color.Colors;
import dev.lobstershack.client.util.MathUtil;
import dev.lobstershack.client.util.TextureUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;


/**
 * <p>Guess what</p>
 * <p>Chicken butt</p>
 * @author Intro
 * @since 1.0.7
 * @see Color
 * @see Options
 */
public class ColorOptionWidget extends SimpleWidget {

    private final Option<Color> attachedOption;

    private final ResourceLocation BAKED_TEXTURE = new ResourceLocation("osmium", "/textures/gui/gradient.png");

    private final NativeImage TEXTURE;

    public ColorOptionWidget(int x, int y, int squareSideLength, Option<Color> attachedOption) {
        super(x, y, squareSideLength, squareSideLength, Component.empty());
        this.attachedOption = attachedOption;
        this.TEXTURE = TextureUtil.getImageAtLocation(BAKED_TEXTURE);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        PoseStack stack = graphics.pose();
        stack.pushPose();
        graphics.blit(BAKED_TEXTURE, this.getX(), this.getY(), 0, 0, this.width, this.height, this.width, this.height);
        graphics.drawCenteredString(Minecraft.getInstance().font, this.attachedOption.get().toStringNoAlpha(), this.getX() + this.width / 2, this.getY() + this.height + this.height / 8, Colors.WHITE.getColor().getInt());
        graphics.drawCenteredString(Minecraft.getInstance().font, Component.translatable(  "osmium.widget.color_picker"), this.getX() + this.width / 2, this.getY() - this.height / 8, Colors.WHITE.getColor().getInt());
        stack.popPose();
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if(MathUtil.isPositionWithinBounds((int) MathUtil.roundUp(mouseX), (int) MathUtil.roundUp(mouseY), this.getX(), this.getY(), this.width, this.height)) {
            int imageSpaceX = Mth.clamp((int) ((mouseX - this.getX()) * (256f / this.width)), 0, 256);
            int imageSpaceY = Mth.clamp((int) ((mouseY - (this.getY())) * (256f / this.height)), 0, 256);
            // this function is lying
            // it actually returns the data in BGRA format
            // I think this is due to an error in how the buffer access offsets are calculated
            int color = TEXTURE.getPixelRGBA(imageSpaceX, imageSpaceY);
            attachedOption.set(new Color(Color.toRGBAB(color), Color.toRGBAG(color), Color.toRGBAR(color), Color.toRGBAA(color)));
            super.onClick(mouseX, mouseY);
        }

    }
}
