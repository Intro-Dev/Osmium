package dev.lobstershack.client.render.color;

public enum Colors {
    RED(0xff, 0, 0, 0xff),
    GREEN(0, 0xff, 0, 0xff),
    BLUE(0, 0, 0xff, 0xff),
    TRANSPARENT(0, 0, 0, 0),
    BLACK(0, 0, 0, 0xff),
    WHITE(0xff, 0xff, 0xff, 0xff),
    ORANGE(0xff, 0xa5, 0, 0xff),
    BACKGROUND_GRAY(26, 26, 26, 52),
    DARK_GRAY(15, 15, 15, 52),
    PURPLE(128, 0, 128, 0xff);

    private final Color color;

    Colors(Color color) {
        this.color = color;
    }

    Colors(int r, int g, int b, int a) {
        this.color = new Color(r, g, b, a);
    }

    public Color getColor() {
        return this.color;
    }
}
