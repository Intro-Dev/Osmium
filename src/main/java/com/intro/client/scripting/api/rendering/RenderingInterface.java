package com.intro.client.scripting.api.rendering;

import com.intro.client.scripting.api.math.Mat4;

public interface RenderingInterface {

    void begin(int mode, int format);

    void vertex(float x, float y, float z);

    void color(float r, float g, float b, float a);

    void texture(int u, int v);

    void shader(String shader);

    void normal(float x, float y, float z);

    void end();

    void translate(double x, double y, double z);

    void rotate(Mat4 matrix);

    void setPos(double x, double y, double z);

    void push();

    void pop();

    void drawText(String text, float x, float y, int color, boolean shadowed);

    void endVertex();


    public interface DrawMode {

        int getQuads();

        int getTriangles();

        int getTriangleFan();

        int getLines();

    }

    public interface Format {

        int getPosition();

        int getPositionColor();

        int getPositionColorTexture();

        int getPositionColorTextureNormal();

    }



}
