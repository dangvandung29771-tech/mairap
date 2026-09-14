package com.frontier.frontier.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Closed stone conduit connecting canals to sprinklers over longer runs.
 * Carries the same diminishing water level as canals.
 */
public class StonePipeBlock extends Block {
    public static final IntegerProperty LEVEL = IntegerProperty.create("water_level", 0, 3);

    private static final VoxelShape CORE = Block.box(5, 5, 5, 11, 11, 11);
    private static final VoxelShape DOWN = Block.box(5, 0, 5, 11, 5, 11);
    private static final VoxelShape UP = Block.box(5, 11, 5, 11, 16, 11);

    public StonePipeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.or(CORE, DOWN, UP);
    }

    public static void updateFromNeighbours(Level level, BlockPos pos, BlockState state) {
        int best = 0;
        for (Direction direction : Direction.values()) {
            BlockState neighbor = level.getBlockState(pos.relative(direction));
            int neighborLevel = 0;
            if (neighbor.getBlock() instanceof WoodCanalBlock canal) {
                neighborLevel = neighbor.getValue(WoodCanalBlock.LEVEL);
            } else if (neighbor.getBlock() instanceof StonePipeBlock) {
                neighborLevel = neighbor.getValue(LEVEL);
            } else if (neighbor.is(net.minecraft.world.level.block.Blocks.WATER)) {
                neighborLevel = 3;
            }
            if (neighborLevel > best) {
                best = neighborLevel;
            }
        }
        int newLevel = best > 1 ? best - 1 : 0;
        if (newLevel != state.getValue(LEVEL)) {
            level.setBlock(pos, state.setValue(LEVEL, newLevel), 3);
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock,
                                   BlockPos neighborPos, boolean movedByPiston) {
        if (!level.isClientSide) {
            updateFromNeighbours(level, pos, state);
            WoodCanalBlock.updateNeighbours(level, pos);
        }
    }
}
