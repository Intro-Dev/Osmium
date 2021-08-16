package com.intro.client.module.event;

import com.mojang.blaze3d.vertex.PoseStack;

public class EventRender extends Event{
    public float getTickDelta() {
        return tickDelta;
    }

    public long getLimitTime() {
        return limitTime;
    }

    public PoseStack getPoseStack() {
        return poseStack;
    }

    public boolean isB1() {
        return b1;
    }


    public float tickDelta;
    public long limitTime;
    public PoseStack poseStack;
    public boolean b1;

    public EventRender(EventDirection direction, float tickDelta, long limitTime, PoseStack stack) {
        super(direction, false);
        this.limitTime = limitTime;
        this.poseStack = stack;
        this.tickDelta = tickDelta;
    }
}
