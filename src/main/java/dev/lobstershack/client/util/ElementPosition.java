package dev.lobstershack.client.util;

public class ElementPosition {

    public int x, y;
    public double scale;

    public ElementPosition(int x, int y, double scale) {
        this.x = x;
        this.y = y;
        this.scale = scale;
    }

    @Override
    public String toString() {
        return "ElementPosition{" +
                "x=" + x +
                ", y=" + y +
                ", scale=" + scale +
                '}';
    }
}
