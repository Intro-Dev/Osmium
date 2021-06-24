package com.intro.render;

import com.intro.Osmium;
import com.intro.Vector2d;
import com.intro.module.event.EventDirection;
import com.intro.module.event.EventRender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

public class RenderManager {
    public static ArrayList<Text> textArrayList = new ArrayList<Text>();

    private MatrixStack matrix;
    private Matrix4f matrix4f;
    private double scale;
    private Vector4f V4 = new Vector4f();
    private Camera camera;

    static MinecraftClient mc = MinecraftClient.getInstance();


    public void RenderHud(MatrixStack stack) {
        TextRenderer renderer = mc.textRenderer;
        mc.getProfiler().push("OsmiumHudRenderer");
        for(Text text : textArrayList) {
            renderer.draw(stack, new TranslatableText(text.text), text.posX, text.posY, text.color);
        }
        mc.getProfiler().pop();
    }

    public void render(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo info, boolean bl, Camera camera, MatrixStack matrixStack2, Matrix4f matrix4f) {
        this.matrix = matrix;
        this.matrix4f = matrix4f;
        this.camera = camera;
        mc.getProfiler().push("OsmiumRenderer");
        EventRender EventRenderPre = new EventRender(EventDirection.PRE, tickDelta, limitTime, matrix, bl, camera, matrixStack2, matrix4f);
        Osmium.EVENT_BUS.PostEvent(EventRenderPre);
        //if(EventRenderPre.isCanceled() && info.isCancellable())
        //    info.cancel();
        Osmium.EVENT_BUS.PostEvent(new EventRender(EventDirection.POST, tickDelta, limitTime, matrix, bl, camera, matrixStack2, matrix4f));
        draw3DString(new BlockPos(0, 128, 0), "test", 0xfffff, matrix);

        mc.getProfiler().pop();
    }

    public void draw3DString(BlockPos pos, String text, int color, MatrixStack matrix) {
        TextRenderer renderer = mc.textRenderer;
        Vector2d screenPos = to2D(pos.getX(), pos.getY(), pos.getZ(), (float) mc.getCameraEntity().getX(), (float) mc.cameraEntity.getY(), (float) mc.cameraEntity.getZ());
        System.out.println(screenPos.toString());
        renderer.draw(matrix, new TranslatableText(text), (float) screenPos.getX(), (float) screenPos.getY(), color);

    }


    public static Vector2d to2D(float x, float y, float z, float cameraX, float cameraY, float cameraZ) {


        System.out.println(x);
        System.out.println(y);
        System.out.println(z);
        return new Vector2d(x / z, y / z);
    }





    public static RenderManager CreateInstance() {
        return new RenderManager();
    }



}
