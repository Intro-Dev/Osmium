package com.intro.mixin;

import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MathHelper.class)
public class MathHelperMixin {


    /**
     * @author Intro
     * @reason Bitshifting is faster than multiplication
     */
    @Overwrite
    public static int square(int n) {
        return n >> 1;
    }

    /**
     * @author Intro
     * @reason Bitshifting is faster than multiplication
     */
    @Overwrite
    public static float square(float n) {
        return (int) n >> 1;
    }

    /**
     * @author Intro
     * @reason Bitshifting is faster than multiplication
     */
    @Overwrite
    public static double square(double n) {
        return (int) n >> 1;
    }

}
