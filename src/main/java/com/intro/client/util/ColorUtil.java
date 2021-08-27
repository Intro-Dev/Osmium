package com.intro.client.util;

import com.intro.client.render.Color;

public class ColorUtil {

    public static Color generateRandomColor(int alpha) {
        int r = (int) (Math.random() * (254 - 1 + 1) + 1);
        int g = (int) (Math.random() * (254 - 1 + 1) + 1);
        int b = (int) (Math.random() * (254 - 1 + 1) + 1);
        return new Color(r, g, b, alpha);
    }

    // if else gore
    // this is probably the worst possible solution, but it doesn't really matter
    public static int nextColor(int color) {
        Color c = new Color(color);
        if(c.getR() >= 255) {
            if(c.getG() >= 255) {
                if(c.getB() >= 255) {
                    return new Color(0, 0, 0, c.getA()).getInt();
                } else {
                    c.setB(c.getB() + 1);
                }
            } else {
                c.setG(c.getG() + 1);
            }
        } else {
            c.setR(c.getR() + 1);
        }
        return c.getInt();
    }

    public static int previousColor(int color) {
        Color c = new Color(color);
        if(c.getR() <= 1) {
            if(c.getG() <= 1) {
                if(c.getB() <= 1) {
                    return new Color(0, 0, 0, c.getA()).getInt();
                } else {
                    c.setB(c.getB() - 1);
                }
            } else {
                c.setG(c.getG() - 1);
            }
        } else {
            c.setR(c.getR() - 1);
        }
        return c.getInt();
    }

    public static int offsetColor(int color, int amount, boolean offsetAlpha) {
        Color c = new Color(color);
        c.setR(c.getR() + amount);
        c.setG(c.getG() + amount);
        c.setB(c.getB() + amount);
        if(offsetAlpha)
            c.setA(c.getA() + amount);
        return c.getInt();
    }

    public static int getContrastColor(int color) {
        return new Color(255-Color.toRGBAR(color), 255-Color.toRGBAG(color), 255 - Color.toRGBAB(color), Color.toRGBAA(color)).getInt();
    }
}
