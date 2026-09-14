package com.frontier.frontier.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Open wooden irrigation canal. Fill it with a water bucket; water flows to
 * neighbouring canals, pipes and sprinklers with a reduced level.
 */
public class WoodCanalBlock extends Block {
    public static final IntegerProperty LEVEL = IntegerProperty.create("water_level", 0, 3);

    protected static final VoxelShape SHAPE = Shapes.or(
            Block.box(0, 0, 0, 16, 5, 16),
            Block.box(0, 5, 0, 16, 11, 2),
            Block.box(0, 5, 14, 16, 11, 16),
            Block.box(0, 5, 2, 2, 11, 14),
            Block.box(14, 5, 2, 16, 11, 14)
    );

    public WoodCanalBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack held, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hit) {
        if (held.is(Items.WATER_BUCKET) && state.getValue(LEVEL) < 3) {
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(LEVEL, 3), 3);
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.2F);
                if (!player.isCreative()) {
                    player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                }
                updateNeighbours(level, pos);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    /** Recompute water level from the best neighbour source. */
    public static void updateFromNeighbours(Level level, BlockPos pos, BlockState state) {
        if (state.getValue(LEVEL) == 3) {
            return; // bucket-filled sources keep their level
        }
        int best = 0;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockState neighbor = level.getBlockState(pos.relative(direction));
            int neighborLevel = neighborLevel(neighbor);
            if (neighborLevel > best) {
                best = neighborLevel;
            }
        }
        int newLevel = best > 1 ? best - 1 : 0;
        if (newLevel != state.getValue(LEVEL)) {
            level.setBlock(pos, state.setValue(LEVEL, newLevel), 3);
        }
    }

    private static int neighborLevel(BlockState neighbor) {
        if (neighbor.getBlock() instanceof WoodCanalBlock) {
            return neighbor.getValue(LEVEL);
        }
        if (neighbor.getBlock() instanceof StonePipeBlock) {
            return neighbor.getValue(StonePipeBlock.LEVEL);
        }
        if (neighbor.is(net.minecraft.world.level.block.Blocks.WATER)) {
            return 3;
        }
        return 0;
    }

    public static void updateNeighbours(Level level, BlockPos pos) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos relative = pos.relative(direction);
            BlockState neighbor = level.getBlockState(relative);
            if (neighbor.getBlock() instanceof WoodCanalBlock) {
                updateFromNeighbours(level, relative, neighbor);
            } else if (neighbor.getBlock() instanceof StonePipeBlock) {
                StonePipeBlock.updateFromNeighbours(level, relative, neighbor);
            } else if (neighbor.getBlock() instanceof SprinklerBlock) {
                level.sendBlockUpdated(relative, neighbor, neighbor, 3);
            }
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock,
                                   BlockPos neighborPos, boolean movedByPiston) {
        if (!level.isClientSide) {
            updateFromNeighbours(level, pos, state);
            updateNeighbours(level, pos);
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                     LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return state;
    }
}
