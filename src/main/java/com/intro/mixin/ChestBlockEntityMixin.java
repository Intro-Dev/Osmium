package com.intro.mixin;

import com.intro.module.OptimizationModule;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlockEntity.class)
public abstract class ChestBlockEntityMixin  {



    @Inject(method = "clientTick", at = @At("HEAD"))
    private static void onTick(World world, BlockPos pos, BlockState state, ChestBlockEntity blockEntity, CallbackInfo ci) {
            if(world != null) {
                // System.out.println(Arrays.toString(OptimizationModule.RemovedBlockEntities.toArray()));
                for(ChunkPos b : OptimizationModule.RemovedBlockEntities) {
                    if(world.getChunk(blockEntity.getPos()).getPos().equals(b)) {
                        System.out.println("removing shit");
                        blockEntity.markRemoved();
                    }
                }
                if(OptimizationModule.PreviouslyRemovedBlockEntities.contains(world.getChunk(blockEntity.getPos()).getPos())) {
                    for(BlockPos b : world.getChunk(blockEntity.getPos()).getBlockEntityPositions()) {
                        if(b == null) {
                            System.out.println("what the fuck");
                        }
                        if(b != null && MinecraftClient.getInstance().world != null) {
                            BlockEntity worldBlockEntity = MinecraftClient.getInstance().world.getBlockEntity(b);
                            if(worldBlockEntity != null) {
                                worldBlockEntity.cancelRemoval();
                            }
                        } else {
                            System.out.println("why is this null");
                        }
                    }
                    OptimizationModule.PreviouslyRemovedBlockEntities.remove(world.getChunk(blockEntity.getPos()).getPos());
                }
            }




    }

}
