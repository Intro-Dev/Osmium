package com.intro.client.render.drawables;

import com.intro.client.OsmiumClient;
import com.intro.client.render.color.Color;
import com.intro.client.render.color.Colors;
import com.intro.client.util.*;
import com.intro.common.config.Options;
import com.intro.common.config.options.Option;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;

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
        OsmiumClient.options.getElementPositionOption(Options.KeystrokesPosition).get().loadToScalable(this);
        this.width = 212;
        this.height = 138;

        this.trueWidth = 192;
        this.trueHeight = 128;

        sectionWidth = trueWidth / 3;
        sectionHeight = trueHeight / 2;
        wKeyPos = new ElementPosition(this.posX + sectionWidth + ELEMENT_OFFSET, this.posY, 1);
        aKeyPos = new ElementPosition(this.posX, this.posY + sectionHeight + ELEMENT_OFFSET, 1);
        sKeyPos = new ElementPosition(this.posX + sectionWidth + ELEMENT_OFFSET, this.posY + sectionHeight + ELEMENT_OFFSET, 1);
        dKeyPos = new ElementPosition(this.posX + sectionWidth * 2 + (ELEMENT_OFFSET * 2), this.posY + sectionHeight + ELEMENT_OFFSET, 1);
        wKeyTextPos = new Vector2d(wKeyPos.x + (sectionWidth / 2f), wKeyPos.y + (sectionHeight / 4f));
        aKeyTextPos = new Vector2d(aKeyPos.x + (sectionWidth / 2f), aKeyPos.y + (sectionHeight / 4f));
        sKeyTextPos = new Vector2d(sKeyPos.x + (sectionWidth / 2f), sKeyPos.y + (sectionHeight / 4f));
        dKeyTextPos = new Vector2d(dKeyPos.x + (sectionWidth / 2f), dKeyPos.y + (sectionHeight / 4f));
    }

    @Override
    public void render(PoseStack stack) {
        if(OsmiumClient.options.getBooleanOption(Options.KeystrokesEnabled).get()) {
            this.visible = true;

            Color temp = OsmiumClient.options.getColorOption(Options.KeystrokesColor).get();
            temp.setA((int) (OsmiumClient.options.getDoubleOption(Options.KeystrokesAlpha).get() * 255));
            int BG_COLOR = temp.getInt();

            int KEY_DOWN_COLOR;

            MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());

            // the gore here
            // :|
            if(OsmiumClient.options.getBooleanOption(Options.KeystrokesRgb).get()) {
                colorGenerator.tick();
                colorGenerator.setAlpha((int) (OsmiumClient.options.getDoubleOption(Options.KeystrokesAlpha).get() * 255));

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
            RenderSystem.disableTexture();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            Tesselator.getInstance().end();
            RenderSystem.disableBlend();
            RenderSystem.enableTexture();

            RenderUtil.renderCenteredScaledText(stack, mc.font, bufferSource, mc.options.keyUp.getTranslatedKeyMessage().getString().toUpperCase(), ((int) wKeyTextPos.getX()), ((int) wKeyTextPos.getY()), Colors.WHITE.getColor().getInt(), 3f);
            RenderUtil.renderCenteredScaledText(stack, mc.font, bufferSource, mc.options.keyLeft.getTranslatedKeyMessage().getString().toUpperCase(), ((int) aKeyTextPos.getX()), ((int) aKeyTextPos.getY()), Colors.WHITE.getColor().getInt(), 3f);
            RenderUtil.renderCenteredScaledText(stack, mc.font, bufferSource, mc.options.keyDown.getTranslatedKeyMessage().getString().toUpperCase(), ((int) sKeyTextPos.getX()), ((int) sKeyTextPos.getY()), Colors.WHITE.getColor().getInt(), 3f);
            RenderUtil.renderCenteredScaledText(stack, mc.font, bufferSource, mc.options.keyRight.getTranslatedKeyMessage().getString().toUpperCase(), ((int) dKeyTextPos.getX()), ((int) dKeyTextPos.getY()), Colors.WHITE.getColor().getInt(), 3f);
            bufferSource.endBatch();
        } else {
            this.visible = false;
        }

    }

    @Override
    public void destroySelf() {

    }

    @Override
    public void onPositionChange(int newX, int newY, int oldX, int oldY) {
        sectionWidth = trueWidth / 3;
        sectionHeight = trueHeight / 2;
        wKeyPos = new ElementPosition(this.posX + sectionWidth + ELEMENT_OFFSET, this.posY, 1);
        aKeyPos = new ElementPosition(this.posX, this.posY + sectionHeight + ELEMENT_OFFSET, 1);
        sKeyPos = new ElementPosition(this.posX + sectionWidth + ELEMENT_OFFSET, this.posY + sectionHeight + ELEMENT_OFFSET, 1);
        dKeyPos = new ElementPosition(this.posX + sectionWidth * 2 + (ELEMENT_OFFSET * 2), this.posY + sectionHeight + ELEMENT_OFFSET, 1);
        wKeyTextPos = new Vector2d(wKeyPos.x + (sectionWidth / 2f), wKeyPos.y + (sectionHeight / 4f));
        aKeyTextPos = new Vector2d(aKeyPos.x + (sectionWidth / 2f), aKeyPos.y + (sectionHeight / 4f));
        sKeyTextPos = new Vector2d(sKeyPos.x + (sectionWidth / 2f), sKeyPos.y + (sectionHeight / 4f));
        dKeyTextPos = new Vector2d(dKeyPos.x + (sectionWidth / 2f), dKeyPos.y + (sectionHeight / 4f));

        OsmiumClient.options.put(Options.KeystrokesPosition, new Option<>(Options.KeystrokesPosition, new ElementPosition(newX, newY, this.scale)));
    }

    public static Keystrokes getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Keystrokes();
        }
        return INSTANCE;
    }

    @Override
    public void onScaleChange(double oldScale, double newScale) {
        OsmiumClient.options.put(Options.KeystrokesPosition, new Option<>(Options.KeystrokesPosition, new ElementPosition(this.posX, this.posY, newScale)));
    }
}
