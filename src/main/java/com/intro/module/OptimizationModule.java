package com.intro.module;

import com.intro.BlockEntityCullingMode;
import com.intro.OsmiumOptions;
import com.intro.mixin.WorldRendererAccessor;
import com.intro.module.event.Event;
import com.intro.module.event.EventRender;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class OptimizationModule extends Module{

    private ArrayList<BlockEntity> RemovedBlockEntities = new ArrayList<BlockEntity>();
    private ArrayList<BlockEntity> QueuedRemovalBlockEntities = new ArrayList<BlockEntity>();


    public OptimizationModule() {
        super("OptimizationModule");
    }

    public void OnEvent(Event event) {

        if(event instanceof EventRender) {
            mc.getProfiler().push("OsmiumBlockEntityCulling");
            RenderSystem.disableLighting();
            RenderSystem.disableBlend();
            RenderSystem.disableTexture();
            if(!(OsmiumOptions.BlockEntityCulling == BlockEntityCullingMode.DISABLED)) {
                QueuedRemovalBlockEntities.clear();
                ClientWorld world = mc.world;
                for(BlockEntity blockEntity : world.blockEntities) {
                    if(ShouldBeCulled(blockEntity.getPos()) && !RemovedBlockEntities.contains(blockEntity)) {
                        blockEntity.markRemoved();
                        RemovedBlockEntities.add(blockEntity);
                    }
                }

                for(BlockEntity blockEntity : RemovedBlockEntities) {
                    if (!(ShouldBeCulled(blockEntity.getPos()) && blockEntity.isRemoved())) {
                        blockEntity.cancelRemoval();
                        world.setBlockEntity(blockEntity.getPos(), blockEntity);
                        QueuedRemovalBlockEntities.add(blockEntity);
                    }
                }

                for(BlockEntity blockEntity : QueuedRemovalBlockEntities) {
                    RemovedBlockEntities.remove(blockEntity);
                }
            } else {
                if(!RemovedBlockEntities.isEmpty()) {
                    for(BlockEntity blockEntity : RemovedBlockEntities) {
                        blockEntity.cancelRemoval();
                        mc.world.setBlockEntity(blockEntity.getPos(), blockEntity);
                        QueuedRemovalBlockEntities.add(blockEntity);
                    }
                    for(BlockEntity blockEntity : QueuedRemovalBlockEntities) {
                        RemovedBlockEntities.remove(blockEntity);
                    }
                    QueuedRemovalBlockEntities.clear();
                }
            }
            RenderSystem.enableLighting();
            RenderSystem.enableBlend();
            RenderSystem.enableTexture();
            mc.getProfiler().pop();
        }
    }

    public boolean ShouldBeCulled(BlockPos pos) {
        switch (OsmiumOptions.BlockEntityCulling) {
            case LOW:
                if(pos.isWithinDistance(mc.player.getBlockPos(), (((WorldRendererAccessor) mc.worldRenderer).getRenderDistance() * 16 / 6f))) {
                    return false;
                }
            case MEDIUM:
                if(pos.isWithinDistance(mc.player.getBlockPos(), (((WorldRendererAccessor) mc.worldRenderer).getRenderDistance() * 16 / 8f))) {
                    return false;
                }
            case HIGH:
                if(pos.isWithinDistance(mc.player.getBlockPos(), (((WorldRendererAccessor) mc.worldRenderer).getRenderDistance() * 16 / 12f))) {
                    return false;
                }
            case EXTREME:
                if(pos.isWithinDistance(mc.player.getBlockPos(), 10)) {
                    return false;
                }
        }
        return true;
    }

}
