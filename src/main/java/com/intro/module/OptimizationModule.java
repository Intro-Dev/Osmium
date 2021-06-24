package com.intro.module;

import com.intro.config.BlockEntityCullingMode;
import com.intro.Osmium;
import com.intro.OsmiumChunkManager;
import com.intro.config.EnumOption;
import com.intro.config.OptionUtil;
import com.intro.module.event.Event;
import com.intro.module.event.EventTick;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;

public class OptimizationModule extends Module{

    public volatile static ArrayList<ChunkPos> RemovedBlockEntities = new ArrayList<>();
    private volatile ArrayList<ChunkPos> QueuedRemovalBlockEntities = new ArrayList<>();
    public volatile static ArrayList<ChunkPos> PreviouslyRemovedBlockEntities = new ArrayList<>();


    private ChunkPos lastChunkPos;


    public OptimizationModule() {
        super("OptimizationModule");
    }

    public void OnEvent(Event event) {

        if(event instanceof EventTick && mc.player != null) {
            if(((EnumOption) OptionUtil.Options.BlockEntityCullingMode.get()).variable != BlockEntityCullingMode.DISABLED && lastChunkPos != null) {
                if(OsmiumChunkManager.getPlayerChunk() != mc.world.getChunk(lastChunkPos.getStartPos())) {
                    for(Chunk c : Osmium.chunkManager.getLoadedChunks()) {
                        for(Chunk k : OsmiumChunkManager.getAdjacentChunks()) {
                            if(c.getPos() != k.getPos()) {
                                QueuedRemovalBlockEntities.add(c.getPos());
                            } else {
                                PreviouslyRemovedBlockEntities.add(c.getPos());
                                RemovedBlockEntities.remove(c.getPos());
                            }
                        }

                    }
                }


                    for(ChunkPos b : QueuedRemovalBlockEntities) {
                        if(!RemovedBlockEntities.contains(b)) {
                            RemovedBlockEntities.add(b);
                        }
                    }
            }
            QueuedRemovalBlockEntities.clear();
            lastChunkPos = mc.player.getChunkPos();
        }


        }









}
