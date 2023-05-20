package dev.lobstershack.client.util;

public class MathUtil {

    public static boolean isPositionWithinBounds(int x, int y, int posX, int posY, int width, int height) {
        return x > posX && x < posX + width && y > posY && y < posY + height;
    }

    public static double roundUp(double number) {
        return Math.floor(number) + 1;
    }

}
