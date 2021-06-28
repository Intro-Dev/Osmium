package com.intro.mixin;

import com.intro.Osmium;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.biome.source.BiomeArray;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.BitSet;
import java.util.function.BooleanSupplier;

@Mixin(ClientChunkManager.class)
public class ClientChunkManagerMixin {

    @Shadow @Final
    ClientWorld world;

    @Inject(method = "loadChunkFromPacket", at = @At("TAIL"))
    private void load(int x, int z, BiomeArray biomes, PacketByteBuf buf, NbtCompound nbt, BitSet bitSet, CallbackInfoReturnable<WorldChunk> cir) {
        // Osmium.chunkManager.addChunk(world.getChunk(x, z));
    }

    @Inject(method = "unload", at = @At(value = "TAIL"))
    private void unload(int x, int z, CallbackInfo info) {
        // Osmium.chunkManager.removeChunk(world.getChunk(x, z));
    }

}

