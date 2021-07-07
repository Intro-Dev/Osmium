package com.intro;

public class Vector2d {

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    private double x, y;

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return String.valueOf(x) + ", " + String.valueOf(y);
    }
}
