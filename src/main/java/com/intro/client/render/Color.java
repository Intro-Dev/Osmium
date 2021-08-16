package com.intro.client.render;

public class Color {

    private int r;
    private int g;
    private int b;
    private int a;

    public static final int MAX_COLOR_VALUE = 254;

    public Color(int color) {
        this.r = toRGBAR(color);
        this.g = toRGBAG(color);
        this.b = toRGBAB(color);
        this.a = toRGBAA(color);
    }

    public Color(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color(byte b) {
        this.r = toRGBAR(b);
        this.g = toRGBAG(b);
        this.b = toRGBAB(b);
        this.a = toRGBAA(b);
    }

    public Color(byte r, byte g, byte b, byte a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color(float r, float g, float b, float a) {
        Color c = fromFloatRGBA(r, g, b, a);
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public static int fromRGBA(int r, int g, int b, int a) {
        return (r << 16) + (g << 8) + (b) + (a << 24);
    }

    public static int toRGBAR(int color) {
        return (color >> 16) & 0x000000FF;
    }

    public static int toRGBAG(int color) {
        return (color >> 8) & 0x000000FF;
    }

    public static int toRGBAB(int color) {
        return (color) & 0x000000FF;
    }

    public static int toRGBAA(int color) {
        return (color >> 24) & 0x000000FF;
    }

    public static int getDefaultAlpha() {
        return 0;
    }

    public int getInt() {
        return fromRGBA(this.getR(), this.getG(), this.getB(), this.getA());
    }

    public float getFloatR() {
        return (this.r / 255f);
    }

    public float getFloatG() {
        return (this.g / 255f);
    }

    public float getFloatB() {
        return (this.b / 255f);
    }

    public float getFloatA() {
        return (this.a / 255f);
    }

    public static Color fromFloatRGBA(float r, float g, float b, float a) {
        return new Color((int) ((r * 255) + 0.5), (int) ((g * 255) + 0.5), (int) ((b * 255) + 0.5), (int) ((a * 255) + 0.5));
    }

    public Color getNextColor() {
        if(this.getR() > MAX_COLOR_VALUE) {
            if(this.getG() > MAX_COLOR_VALUE) {
                if(this.getB() > MAX_COLOR_VALUE) {
                    return new Color(MAX_COLOR_VALUE, MAX_COLOR_VALUE, MAX_COLOR_VALUE, this.getA());
                } else {
                    return new Color(MAX_COLOR_VALUE, MAX_COLOR_VALUE, this.getB() + 1, this.getA());
                }
            } else {
                return new Color(MAX_COLOR_VALUE, this.getG() + 1, this.getB(), this.getA());
            }
        } else {
            return new Color(this.getR() + 1, this.getG(), this.getB(), this.getA());
        }
    }

    public Color getNextColorIncrement(int increment) {
        if(this.getR() > MAX_COLOR_VALUE) {
            if(this.getG() > MAX_COLOR_VALUE) {
                if(this.getB() > MAX_COLOR_VALUE) {
                    return new Color(MAX_COLOR_VALUE, MAX_COLOR_VALUE, MAX_COLOR_VALUE, this.getA());
                } else {
                    return new Color(MAX_COLOR_VALUE, MAX_COLOR_VALUE, this.getB() + increment, this.getA());
                }
            } else {
                return new Color(MAX_COLOR_VALUE, this.getG() + increment, this.getB(), this.getA());
            }
        } else {
            return new Color(this.getR() + increment, this.getG(), this.getB(), this.getA());
        }
    }

    public static Color fromFloatArray(float[] arr) {
        return fromFloatRGBA(arr[0], arr[1], arr[2], arr[3]);
    }

    public String toString() {
        return this.getR() + ", " + this.getG() + ", " + this.getB() + ", " + this.getA();
    }

    public String toStringNoAlpha() {
        return this.getR() + ", " + this.getG() + ", " + this.getB();
    }

}
