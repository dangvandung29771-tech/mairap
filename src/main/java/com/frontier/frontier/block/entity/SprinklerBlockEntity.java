package com.frontier.frontier.block.entity;

import com.frontier.frontier.block.SprinklerBlock;
import com.frontier.frontier.init.FrontierBlockEntities;
import com.frontier.frontier.init.FrontierSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/** Waters and gently encourages crops around an active sprinkler. */
public class SprinklerBlockEntity extends BlockEntity {
    private int workTimer = 0;

    public SprinklerBlockEntity(BlockPos pos, BlockState state) {
        super(FrontierBlockEntities.SPRINKLER.get(), pos, state);
    }

    public boolean isActive() {
        return level != null && SprinklerBlock.hasWater(level, worldPosition);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SprinklerBlockEntity entity) {
        if (!SprinklerBlock.hasWater(level, pos)) {
            return;
        }
        entity.workTimer++;
        if (entity.workTimer % 20 != 0) {
            return;
        }
        ServerLevel serverLevel = (ServerLevel) level;
        int range = SprinklerBlock.RANGE;

        // Hydrate farmland and nudge a handful of crops each second.
        for (int i = 0; i < 10; i++) {
            int dx = level.random.nextInt(range * 2 + 1) - range;
            int dz = level.random.nextInt(range * 2 + 1) - range;
            if (dx * dx + dz * dz > range * range) {
                continue;
            }
            BlockPos target = pos.offset(dx, 0, dz).below();
            BlockState ground = level.getBlockState(target);
            if (ground.getBlock() instanceof FarmBlock && ground.getValue(FarmBlock.MOISTURE) < 7) {
                level.setBlock(target, ground.setValue(FarmBlock.MOISTURE, 7), Block.UPDATE_CLIENTS);
            }
            if (ground.getBlock() instanceof FarmBlock) {
                BlockPos cropPos = target.above();
                BlockState crop = level.getBlockState(cropPos);
                if (crop.getBlock() instanceof BonemealableBlock bonemealable
                        && bonemealable.isValidBonemealTarget(level, cropPos, crop)
                        && level.random.nextFloat() < 0.16F) {
                    bonemealable.performBonemeal(serverLevel, level.random, cropPos, crop);
                }
            }
        }

        if (entity.workTimer % 60 == 0) {
            level.playSound(null, pos, FrontierSounds.SPRINKLER_SPRAY.get(), SoundSource.BLOCKS, 0.6F, 1.0F);
        }
    }
}
