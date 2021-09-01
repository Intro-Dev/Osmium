package com.intro.client.util;

import com.intro.client.render.color.Color;

public class RgbColorGenerator {

    private int startColor;
    private int endColor;

    private GeneratorState startColorState;
    private GeneratorState endColorState;


    public RgbColorGenerator(int alpha) {
        startColor = ColorUtil.generateRandomColor(alpha).getInt();
        endColor = ColorUtil.generateRandomColor(alpha).getInt();
        startColorState = GeneratorState.INCREASING;
        endColorState = GeneratorState.INCREASING;
    }

    public void setAlpha(int alpha) {
        Color c1 = new Color(startColor);
        Color c2 = new Color(endColor);
        c1.setA(alpha);
        c2.setA(alpha);
        startColor = c1.getInt();
        endColor = c2.getInt();
    }

    public void tick() {
        if(Color.toRGBAB(startColor) >= 255) {
            startColorState = GeneratorState.DECREASING;
        } else if(Color.toRGBAB(startColor) <= 1) {
            startColorState = GeneratorState.INCREASING;
        }
        if(Color.toRGBAB(endColor) >= 255) {
            endColorState = GeneratorState.DECREASING;
        } else if(Color.toRGBAB(endColor) <= 1) {
            endColorState = GeneratorState.INCREASING;
        }

        switch (startColorState) {
            case INCREASING -> startColor = ColorUtil.nextColor(startColor);
            case DECREASING -> startColor = ColorUtil.previousColor(startColor);
        }

        switch (endColorState) {
            case INCREASING -> endColor = ColorUtil.nextColor(endColor);
            case DECREASING -> endColor = ColorUtil.previousColor(endColor);
        }
    }

    public int getStartColor() {
        return startColor;
    }

    public int getEndColor() {
        return endColor;
    }

    private enum GeneratorState {
        INCREASING,
        DECREASING
    }

}
