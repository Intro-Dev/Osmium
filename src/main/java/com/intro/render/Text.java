package com.intro.render;

public class Text {
    public String text = "";
    public int posX, posY;
    public int color;

    public Text(int posX, int posY, String text, int color) {
        this.posX = posX;
        this.posY = posY;
        this.text = text;
        this.color = color;
        RenderManager.textArrayList.add(this);
    }

    public Text(int posX, int posY, Object text, int color) {
        this(posX, posY, String.valueOf(text), color);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setText(Object text) {
        this.text = String.valueOf(text);
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void DestroySelf() {
        RenderManager.textArrayList.remove(this);
    }
}
