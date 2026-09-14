package com.frontier.frontier.block;

import com.frontier.frontier.block.entity.SprinklerBlockEntity;
import com.frontier.frontier.init.FrontierBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Brass-and-wood sprinkler. When fed by an adjacent water-carrying canal or
 * pipe it rotates and showers crops in a 5 block radius, hydrating farmland
 * and gently speeding crop growth — without fully automating the farm.
 */
public class SprinklerBlock extends BaseEntityBlock {
    public static final int RANGE = 5;

    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(5, 0, 5, 11, 8, 11),
            Block.box(4, 8, 4, 12, 10, 12)
    );

    public SprinklerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE; // custom model via BER (rotating head)
    }

    public static boolean hasWater(Level level, BlockPos pos) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockState neighbor = level.getBlockState(pos.relative(direction));
            if (neighbor.getBlock() instanceof WoodCanalBlock && neighbor.getValue(WoodCanalBlock.LEVEL) > 0) {
                return true;
            }
            if (neighbor.getBlock() instanceof StonePipeBlock && neighbor.getValue(StonePipeBlock.LEVEL) > 0) {
                return true;
            }
        }
        BlockState below = level.getBlockState(pos.below());
        return (below.getBlock() instanceof StonePipeBlock && below.getValue(StonePipeBlock.LEVEL) > 0)
                || below.is(net.minecraft.world.level.block.Blocks.WATER);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SprinklerBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null
                : createTickerHelper(type, FrontierBlockEntities.SPRINKLER.get(), SprinklerBlockEntity::serverTick);
    }
}
