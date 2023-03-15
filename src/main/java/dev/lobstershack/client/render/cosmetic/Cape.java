package dev.lobstershack.client.render.cosmetic;

import net.minecraft.resources.ResourceLocation;

public class Cape implements Cloneable {

    private final DynamicAnimation texture;
    public boolean isOptifine;
    public boolean animated;
    public String source;
    public String name;
    public String creator;
    public int textureScale;


    public Cape(DynamicAnimation texture, boolean isOptifine, boolean animated, String source, String name, String creator, int textureScale) {
        this.name = name;
        this.texture = texture;
        this.isOptifine = isOptifine;
        this.animated = animated;
        this.source = source;
        this.creator = creator;
        this.textureScale = textureScale;
    }

    public DynamicAnimation getTexture() {
        return texture;
    }

    public ResourceLocation getFrameTexture() {
        return texture.getCurrentFrameLocation();
    }

    public ResourceLocation getFirstFrameTexture() {
        return texture.getFrameLocation(0);
    }

    public void nextFrame() {
        texture.tick();
    }

    public void free() {
        texture.free();
    }


    @Override
    public Cape clone() {
        try {
            return (Cape) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        return "Cape{" +
                "texture=" + texture +
                ", isOptifine=" + isOptifine +
                ", isAnimated=" + animated +
                ", source='" + source + '\'' +
                ", name='" + name + '\'' +
                ", creator='" + creator + '\'' +
                '}';
    }
}
