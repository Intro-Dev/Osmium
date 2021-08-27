package com.intro.client.render.drawables;

import com.intro.client.OsmiumClient;
import com.intro.client.render.Color;
import com.intro.client.render.Colors;
import com.intro.client.util.ElementPosition;
import com.intro.client.util.RenderUtil;
import com.intro.client.util.RgbColorGenerator;
import com.intro.client.util.Vector2d;
import com.intro.common.config.Options;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;

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

    private int sectionWidth;
    private int sectionHeight;

    private final int KEY_DOWN_COLOR = new Color(1f, 0.2f, 0.2f, 0.4f).getInt();

    private final RgbColorGenerator colorGenerator = new RgbColorGenerator(128);


    protected Keystrokes() {
        sectionWidth = width / 3;
        sectionHeight = height / 2;
        wKeyPos = new ElementPosition(this.posX + sectionWidth + ELEMENT_OFFSET, this.posY, 1);
        aKeyPos = new ElementPosition(this.posX, this.posY + sectionHeight + ELEMENT_OFFSET, 1);
        sKeyPos = new ElementPosition(this.posX + sectionWidth + ELEMENT_OFFSET, this.posY + sectionHeight + ELEMENT_OFFSET, 1);
        dKeyPos = new ElementPosition(this.posX + sectionWidth * 2 + (ELEMENT_OFFSET * 2), this.posY + sectionHeight + ELEMENT_OFFSET, 1);
        wKeyTextPos = new Vector2d(wKeyPos.x + (sectionWidth / 2f), wKeyPos.y + (sectionHeight / 4f));
        aKeyTextPos = new Vector2d(aKeyPos.x + (sectionWidth / 2f), aKeyPos.y + (sectionHeight / 4f));
        sKeyTextPos = new Vector2d(sKeyPos.x + (sectionWidth / 2f), sKeyPos.y + (sectionHeight / 4f));
        dKeyTextPos = new Vector2d(dKeyPos.x + (sectionWidth / 2f), dKeyPos.y + (sectionHeight / 4f));

        this.width = 192 + (ELEMENT_OFFSET * 3);
        this.height = 128;

    }

    @Override
    public void render(PoseStack stack) {

        if(OsmiumClient.options.getBooleanOption(Options.KeystrokesEnabled).variable) {
            Color temp = OsmiumClient.options.getColorOption(Options.KeystrokesColor).color;
            temp.setA((int) (OsmiumClient.options.getDoubleOption(Options.KeystrokesAlpha).variable * 255));
            int BG_COLOR = temp.getInt();

            // fill key backgrounds
            // uses set color if key is down
            if(OsmiumClient.options.getBooleanOption(Options.KeystrokesRgb).variable) {
                // if else gore
                colorGenerator.tick();

                int rgbColorStart = colorGenerator.getStartColor();
                int rgbColorEnd = colorGenerator.getEndColor();

                fillGradient(stack, wKeyPos.x, wKeyPos.y, wKeyPos.x + sectionWidth, wKeyPos.y + sectionHeight, (mc.options.keyUp.isDown() ? KEY_DOWN_COLOR : rgbColorStart), rgbColorEnd);
                fillGradient(stack, aKeyPos.x, aKeyPos.y, aKeyPos.x + sectionWidth, aKeyPos.y + sectionHeight, (mc.options.keyLeft.isDown() ? KEY_DOWN_COLOR : rgbColorStart), rgbColorEnd);
                fillGradient(stack, sKeyPos.x, sKeyPos.y, sKeyPos.x + sectionWidth, sKeyPos.y + sectionHeight, (mc.options.keyDown.isDown() ? KEY_DOWN_COLOR : rgbColorStart), rgbColorEnd);
                fillGradient(stack, dKeyPos.x, dKeyPos.y, dKeyPos.x + sectionWidth, dKeyPos.y + sectionHeight, (mc.options.keyRight.isDown() ? KEY_DOWN_COLOR : rgbColorStart), rgbColorEnd);
            } else {
                fill(stack, wKeyPos.x, wKeyPos.y, wKeyPos.x + sectionWidth, wKeyPos.y + sectionHeight, (mc.options.keyUp.isDown() ? KEY_DOWN_COLOR : BG_COLOR));
                fill(stack, aKeyPos.x, aKeyPos.y, aKeyPos.x + sectionWidth, aKeyPos.y + sectionHeight, (mc.options.keyLeft.isDown() ? KEY_DOWN_COLOR : BG_COLOR));
                fill(stack, sKeyPos.x, sKeyPos.y, sKeyPos.x + sectionWidth, sKeyPos.y + sectionHeight, (mc.options.keyDown.isDown() ? KEY_DOWN_COLOR : BG_COLOR));
                fill(stack, dKeyPos.x, dKeyPos.y, dKeyPos.x + sectionWidth, dKeyPos.y + sectionHeight, (mc.options.keyRight.isDown() ? KEY_DOWN_COLOR : BG_COLOR));
            }

            RenderUtil.renderCenteredScaledText(stack, mc.font, mc.options.keyUp.getTranslatedKeyMessage(), ((int) wKeyTextPos.getX()), ((int) wKeyTextPos.getY()), Colors.WHITE.getColor().getInt(), 2f);
            RenderUtil.renderCenteredScaledText(stack, mc.font, mc.options.keyLeft.getTranslatedKeyMessage(), ((int) aKeyTextPos.getX()), ((int) aKeyTextPos.getY()), Colors.WHITE.getColor().getInt(), 2f);
            RenderUtil.renderCenteredScaledText(stack, mc.font, mc.options.keyDown.getTranslatedKeyMessage(), ((int) sKeyTextPos.getX()), ((int) sKeyTextPos.getY()), Colors.WHITE.getColor().getInt(), 2f);
            RenderUtil.renderCenteredScaledText(stack, mc.font, mc.options.keyRight.getTranslatedKeyMessage(), ((int) dKeyTextPos.getX()), ((int) dKeyTextPos.getY()), Colors.WHITE.getColor().getInt(), 2f);
            /*
            vLine(stack, aKeyPos.x + sectionWidth, aKeyPos.y, aKeyPos.y + sectionHeight, Colors.WHITE.getColor().getInt());
            vLine(stack, sKeyPos.x + sectionWidth, sKeyPos.y, sKeyPos.y + sectionHeight, Colors.WHITE.getColor().getInt());
            hLine(stack, wKeyPos.x, wKeyPos.x + sectionWidth, wKeyPos.y + sectionHeight, Colors.WHITE.getColor().getInt());
             */

        }
    }

    @Override
    public void destroySelf() {

    }

    @Override
    public void onPositionChange(int newX, int newY, int oldX, int oldY) {
        sectionWidth = width / 3;
        sectionHeight = height / 2;
        wKeyPos = new ElementPosition(this.posX + sectionWidth + ELEMENT_OFFSET, this.posY, 1);
        aKeyPos = new ElementPosition(this.posX, this.posY + sectionHeight + ELEMENT_OFFSET, 1);
        sKeyPos = new ElementPosition(this.posX + sectionWidth + ELEMENT_OFFSET, this.posY + sectionHeight + ELEMENT_OFFSET, 1);
        dKeyPos = new ElementPosition(this.posX + sectionWidth * 2 + (ELEMENT_OFFSET * 2), this.posY + sectionHeight + ELEMENT_OFFSET, 1);
        wKeyTextPos = new Vector2d(wKeyPos.x + (sectionWidth / 2f), wKeyPos.y + (sectionHeight / 4f));
        aKeyTextPos = new Vector2d(aKeyPos.x + (sectionWidth / 2f), aKeyPos.y + (sectionHeight / 4f));
        sKeyTextPos = new Vector2d(sKeyPos.x + (sectionWidth / 2f), sKeyPos.y + (sectionHeight / 4f));
        dKeyTextPos = new Vector2d(dKeyPos.x + (sectionWidth / 2f), dKeyPos.y + (sectionHeight / 4f));
    }

    public static Keystrokes getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Keystrokes();
        }
        return INSTANCE;
    }

    @Override
    public void onScaleChange(float oldScale, float newScale) {

    }
}
