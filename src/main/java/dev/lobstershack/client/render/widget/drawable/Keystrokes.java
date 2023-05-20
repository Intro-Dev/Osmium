package dev.lobstershack.client.render.widget.drawable;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.lobstershack.client.config.Options;
import dev.lobstershack.client.render.color.Color;
import dev.lobstershack.client.render.color.Colors;
import dev.lobstershack.client.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;

public class Keystrokes extends Scalable {

    private static Keystrokes INSTANCE;

    private final Minecraft mc = Minecraft.getInstance();

    private ElementPosition wKeyPos;
    private ElementPosition aKeyPos;
    private ElementPosition sKeyPos;
    private ElementPosition dKeyPos;

    private Vector2d wKeyTextPos;
    private Vector2d aKeyTextPos;
    private Vector2d sKeyTextPos;
    private Vector2d dKeyTextPos;

    private final int ELEMENT_OFFSET = 10;

    private final int trueWidth, trueHeight;

    private int sectionWidth;
    private int sectionHeight;

    private final RgbColorGenerator colorGenerator = new RgbColorGenerator(128);


    protected Keystrokes() {
        super(Options.KeystrokesPosition, 212, 138, Component.empty());

        this.trueWidth = 192;
        this.trueHeight = 128;

        sectionWidth = trueWidth / 3;
        sectionHeight = trueHeight / 2;
        wKeyPos = new ElementPosition(this.getX() + sectionWidth + ELEMENT_OFFSET, this.getY(), 1);
        aKeyPos = new ElementPosition(this.getX(), this.getY() + sectionHeight + ELEMENT_OFFSET, 1);
        sKeyPos = new ElementPosition(this.getX() + sectionWidth + ELEMENT_OFFSET, this.getY() + sectionHeight + ELEMENT_OFFSET, 1);
        dKeyPos = new ElementPosition(this.getX() + sectionWidth * 2 + (ELEMENT_OFFSET * 2), this.getY() + sectionHeight + ELEMENT_OFFSET, 1);
        wKeyTextPos = new Vector2d(wKeyPos.x + (sectionWidth / 2f), wKeyPos.y + (sectionHeight / 4f));
        aKeyTextPos = new Vector2d(aKeyPos.x + (sectionWidth / 2f), aKeyPos.y + (sectionHeight / 4f));
        sKeyTextPos = new Vector2d(sKeyPos.x + (sectionWidth / 2f), sKeyPos.y + (sectionHeight / 4f));
        dKeyTextPos = new Vector2d(dKeyPos.x + (sectionWidth / 2f), dKeyPos.y + (sectionHeight / 4f));


        boundOption.addChangeListener((newPos) -> {
            sectionWidth = trueWidth / 3;
            sectionHeight = trueHeight / 2;
            wKeyPos = new ElementPosition(newPos.x + sectionWidth + ELEMENT_OFFSET, newPos.y, 1);
            aKeyPos = new ElementPosition(newPos.x, newPos.y + sectionHeight + ELEMENT_OFFSET, 1);
            sKeyPos = new ElementPosition(newPos.x + sectionWidth + ELEMENT_OFFSET, newPos.y + sectionHeight + ELEMENT_OFFSET, 1);
            dKeyPos = new ElementPosition(newPos.x + sectionWidth * 2 + (ELEMENT_OFFSET * 2), newPos.y + sectionHeight + ELEMENT_OFFSET, 1);
            wKeyTextPos = new Vector2d(wKeyPos.x + (sectionWidth / 2f), wKeyPos.y + (sectionHeight / 4f));
            aKeyTextPos = new Vector2d(aKeyPos.x + (sectionWidth / 2f), aKeyPos.y + (sectionHeight / 4f));
            sKeyTextPos = new Vector2d(sKeyPos.x + (sectionWidth / 2f), sKeyPos.y + (sectionHeight / 4f));
            dKeyTextPos = new Vector2d(dKeyPos.x + (sectionWidth / 2f), dKeyPos.y + (sectionHeight / 4f));
        });


    }

    @Override
    public void render(GuiGraphics graphics) {
        PoseStack stack = graphics.pose();
        stack.pushPose();
        if(Options.KeystrokesEnabled.get()) {
            this.visible = true;

            Color temp = Options.KeystrokesColor.get();
            temp.setA((int) (Options.KeystrokesAlpha.get() * 255));
            int BG_COLOR = temp.getInt();

            int KEY_DOWN_COLOR;

            MultiBufferSource.BufferSource bufferSource = graphics.bufferSource();
            // the gore here
            // :|
            if(Options.KeystrokesRgb.get()) {
                colorGenerator.tick();
                colorGenerator.setAlpha((int) (Options.KeystrokesAlpha.get() * 255));

                int rgbColorStart = colorGenerator.getStartColor();
                int rgbColorEnd = colorGenerator.getEndColor();
                KEY_DOWN_COLOR = ColorUtil.getContrastColor(rgbColorStart);

                BufferBuilder buffer = Tesselator.getInstance().getBuilder();
                buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
                RenderUtil.fillGradient(stack, buffer, wKeyPos.x, wKeyPos.y, sectionWidth, sectionHeight, (mc.options.keyUp.isDown() ? KEY_DOWN_COLOR : rgbColorStart), rgbColorEnd);
                RenderUtil.fillGradient(stack, buffer, aKeyPos.x, aKeyPos.y, sectionWidth, sectionHeight, (mc.options.keyLeft.isDown() ? KEY_DOWN_COLOR : rgbColorStart), rgbColorEnd);
                RenderUtil.fillGradient(stack, buffer, sKeyPos.x, sKeyPos.y, sectionWidth, sectionHeight, (mc.options.keyDown.isDown() ? KEY_DOWN_COLOR : rgbColorStart), rgbColorEnd);
                RenderUtil.fillGradient(stack, buffer, dKeyPos.x, dKeyPos.y, sectionWidth, sectionHeight, (mc.options.keyRight.isDown() ? KEY_DOWN_COLOR : rgbColorStart), rgbColorEnd);

            } else {
                KEY_DOWN_COLOR = new Color(0.6f, 0.2f, 0.2f, 0.4f).getInt();
                BufferBuilder buffer = Tesselator.getInstance().getBuilder();
                buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
                RenderUtil.fill(stack, buffer, wKeyPos.x, wKeyPos.y, sectionWidth, sectionHeight, (mc.options.keyUp.isDown() ? KEY_DOWN_COLOR : BG_COLOR));
                RenderUtil.fill(stack, buffer, aKeyPos.x, aKeyPos.y, sectionWidth, sectionHeight, (mc.options.keyLeft.isDown() ? KEY_DOWN_COLOR : BG_COLOR));
                RenderUtil.fill(stack, buffer, sKeyPos.x, sKeyPos.y, sectionWidth, sectionHeight, (mc.options.keyDown.isDown() ? KEY_DOWN_COLOR : BG_COLOR));
                RenderUtil.fill(stack, buffer, dKeyPos.x, dKeyPos.y, sectionWidth, sectionHeight, (mc.options.keyRight.isDown() ? KEY_DOWN_COLOR : BG_COLOR));
            }

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            Tesselator.getInstance().end();
            RenderSystem.disableBlend();

            RenderUtil.renderCenteredScaledText(graphics, mc.font, bufferSource, mc.options.keyUp.getTranslatedKeyMessage().getString().toUpperCase(), ((int) wKeyTextPos.getX()), ((int) wKeyTextPos.getY()), Colors.WHITE.getColor().getInt(), 3f);
            RenderUtil.renderCenteredScaledText(graphics, mc.font, bufferSource, mc.options.keyLeft.getTranslatedKeyMessage().getString().toUpperCase(), ((int) aKeyTextPos.getX()), ((int) aKeyTextPos.getY()), Colors.WHITE.getColor().getInt(), 3f);
            RenderUtil.renderCenteredScaledText(graphics, mc.font, bufferSource, mc.options.keyDown.getTranslatedKeyMessage().getString().toUpperCase(), ((int) sKeyTextPos.getX()), ((int) sKeyTextPos.getY()), Colors.WHITE.getColor().getInt(), 3f);
            RenderUtil.renderCenteredScaledText(graphics, mc.font, bufferSource, mc.options.keyRight.getTranslatedKeyMessage().getString().toUpperCase(), ((int) dKeyTextPos.getX()), ((int) dKeyTextPos.getY()), Colors.WHITE.getColor().getInt(), 3f);
            bufferSource.endBatch();
        } else {
            this.visible = false;
        }
        stack.popPose();
    }



    public static Keystrokes getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Keystrokes();
        }
        return INSTANCE;
    }

    public static void invalidateInstance() {
        INSTANCE = null;
    }


}
