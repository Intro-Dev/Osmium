package com.intro;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;

public class OsmiumChunkManager {

    public ArrayList<Chunk> chunks = new ArrayList<Chunk>();

    public ArrayList<Chunk> getLoadedChunks() {
        return chunks;
    }

    public void addChunk(Chunk chunk) {
        chunks.add(chunk);
    }

    public void removeChunk(Chunk chunk) {
        chunks.remove(chunk);
    }

    public static ArrayList<Chunk> getAdjacentChunks() {
        ArrayList<Chunk> newChunks = new ArrayList<>();
        MinecraftClient mc = MinecraftClient.getInstance();
        for(int i = -1; i < 4; ++i) {
            for(int j = -1; j < 4; ++j) {
                int k = getPlayerChunk().getPos().x + i;
                int l = getPlayerChunk().getPos().z + j;
                newChunks.add(mc.world.getChunk(k ,l));
            }
        }
        return newChunks;
    }

    public static Chunk getPlayerChunk() {
        return MinecraftClient.getInstance().world.getChunk(MinecraftClient.getInstance().player.getBlockPos());
    }

}
