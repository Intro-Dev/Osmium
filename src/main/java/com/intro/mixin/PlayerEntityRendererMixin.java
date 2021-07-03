package com.intro.mixin;

import com.intro.render.CapeRenderer;
import com.intro.render.ElytraRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;

import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    public PlayerEntityRendererMixin(Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = ("<init>"), at = @At("RETURN"))
    private void ConstructorMixinPlayerEntityRenderer(Context ctx, boolean slim, CallbackInfo ci) {
        this.addFeature(new CapeRenderer(this));
        this.addFeature(new ElytraRenderer<>(this, ctx.getModelLoader()));
        this.features.removeIf(renderer -> renderer instanceof CapeFeatureRenderer);
        this.features.removeIf(renderer -> renderer instanceof ElytraFeatureRenderer);

    }
}
