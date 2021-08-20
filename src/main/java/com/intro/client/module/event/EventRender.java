package com.intro.client.module.event;

import com.mojang.blaze3d.vertex.PoseStack;

public class EventRender extends Event{

    public PoseStack getPoseStack() {
        return poseStack;
    }


    public final float tickDelta;
    public final long limitTime;
    public final PoseStack poseStack;

    public EventRender(EventDirection direction, float tickDelta, long limitTime, PoseStack stack) {
        super(direction);
        this.limitTime = limitTime;
        this.poseStack = stack;
        this.tickDelta = tickDelta;
    }
}
