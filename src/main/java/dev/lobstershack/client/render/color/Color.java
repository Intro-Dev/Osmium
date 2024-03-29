package dev.lobstershack.client.render.color;

public class Color {

    private int r;
    private int g;
    private int b;
    private int a;

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
        copy(c);
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


    public static Color fromFloatArray(float[] arr) {
        return fromFloatRGBA(arr[0], arr[1], arr[2], arr[3]);
    }

    public String toString() {
        return this.getR() + ", " + this.getG() + ", " + this.getB() + ", " + this.getA();
    }

    public String toStringNoAlpha() {
        return this.getR() + ", " + this.getG() + ", " + this.getB();
    }

    public void copy(Color color) {
        this.r = color.getR();
        this.g = color.getG();
        this.b = color.getB();
        this.a = color.getA();
    }

    public void multiply(double value) {
        this.r = (int) (r * value);
        this.g = (int) (g * value);
        this.b = (int) (b * value);
        this.a = (int) (a * value);
    }

}
