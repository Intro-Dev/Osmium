package com.intro.client.scripting.implementation.rendering;

import com.intro.client.scripting.ScriptingManager;
import com.intro.client.scripting.api.math.Mat4;
import com.intro.client.scripting.api.rendering.RenderingInterface;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;

public class RenderingInterfaceImpl implements RenderingInterface {

    private BufferBuilder builder;
    private String setShader;

    private final PoseStack stack;

    public RenderingInterfaceImpl(PoseStack stack) {
        this.stack = stack;
    }

    @Override
    public void begin(int mode, int format) {
        builder = new BufferBuilder(RenderType.MEDIUM_BUFFER_SIZE);
        VertexFormat.Mode actualMode;
        VertexFormat actualFormat;

        switch (mode) {
            case 0 -> actualMode = VertexFormat.Mode.QUADS;
            case 1 -> actualMode = VertexFormat.Mode.TRIANGLES;
            case 2 -> actualMode = VertexFormat.Mode.TRIANGLE_FAN;
            case 3 -> actualMode = VertexFormat.Mode.LINES;
            default -> {
                ScriptingManager.log("A script supplied an invalid draw mode");
                return;
            }
        }

        switch (format) {
            case 0 -> actualFormat = DefaultVertexFormat.POSITION;
            case 1 -> actualFormat = DefaultVertexFormat.POSITION_COLOR;
            case 2 -> actualFormat = DefaultVertexFormat.POSITION_COLOR_TEX;
            case 3 -> actualFormat = DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL;
            default -> {
                ScriptingManager.log("A script supplied an invalid vertex format");
                return;
            }
        }

        builder.begin(actualMode, actualFormat);
    }

    @Override
    public void vertex(float x, float y, float z) {
        builder.vertex(stack.last().pose(), x, y, z);
    }

    @Override
    public void color(float r, float g, float b, float a) {
        builder.color(r, g, b, a);
    }

    @Override
    public void texture(int u, int v) {
        builder.uv(u, v);
    }

    @Override
    public void shader(String shader) {
        setShader = shader;
        RenderSystem.setShader(this::shaderInterface);
    }

    private ShaderInstance shaderInterface() {
        return Minecraft.getInstance().gameRenderer.getShader(this.setShader);
    }

    @Override
    public void normal(float x, float y, float z) {
        builder.normal(stack.last().normal(), x, y, z);
    }

    @Override
    public void end() {
        builder.end();
        BufferUploader.end(builder);
    }

    @Override
    public void translate(double x, double y, double z) {
        stack.translate(x, y, z);
    }

    @Override
    public void rotate(Mat4 matrix) {
        // stack.mulPoseMatrix(((Mat4Impl) matrix).getMatrix());
    }

    @Override
    public void setPos(double x, double y, double z) {
        stack.clear();
        stack.translate(x, y, z);
    }

    @Override
    public void push() {
        stack.pushPose();
    }

    @Override
    public void pop() {
        stack.popPose();
    }

    @Override
    public void drawText(String text, float x, float y, int color, boolean shadowed) {
        Minecraft.getInstance().font.drawShadow(stack, text, x, y, color, shadowed);
    }

    @Override
    public void endVertex() {
        builder.endVertex();
    }
}
