package com.intro.module.event;

import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

public class EventRender extends Event{
    public float getTickDelta() {
        return tickDelta;
    }

    public long getLimitTime() {
        return limitTime;
    }

    public MatrixStack getMatrixStack() {
        return matrixStack;
    }

    public boolean isB1() {
        return b1;
    }

    public MatrixStack getMatrixStack2() {
        return matrixStack2;
    }

    public Matrix4f getMatrix4f() {
        return matrix4f;
    }

    public Camera getCamera() {
        return camera;
    }

    public float tickDelta;
    public long limitTime;
    public MatrixStack matrixStack;
    public boolean b1;
    public MatrixStack matrixStack2;
    public Matrix4f matrix4f;
    public Camera camera;

    public EventRender(EventDirection direction, float tickDelta, long limitTime, MatrixStack matrix) {
        super(direction, false);
        this.limitTime = limitTime;
        this.matrixStack = matrix;
        this.tickDelta = tickDelta;
    }
}
