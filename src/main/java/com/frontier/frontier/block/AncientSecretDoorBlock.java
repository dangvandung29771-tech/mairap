package com.frontier.frontier.block;

import com.frontier.frontier.init.FrontierSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A hidden crypt passage disguised as ancient stone. Only a lit rune beside
 * it reveals the mechanism; interacting then swings it open.
 */
public class AncientSecretDoorBlock extends Block {
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public AncientSecretDoorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(OPEN, false).setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OPEN, FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    /** The door only answers to a player when a lit rune touches it. */
    private boolean isRevealed(Level level, BlockPos pos) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockState neighbor = level.getBlockState(pos.relative(direction));
            if (neighbor.getBlock() instanceof AncientRuneBlock && neighbor.getValue(AncientRuneBlock.LIT)) {
                return true;
            }
            BlockState above = level.getBlockState(pos.relative(direction).above());
            if (above.getBlock() instanceof AncientRuneBlock && above.getValue(AncientRuneBlock.LIT)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected ItemInteractionResult useItemOn(net.minecraft.world.item.ItemStack held, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hit) {
        if (!isRevealed(level, pos)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        boolean open = !state.getValue(OPEN);
        level.setBlock(pos, state.setValue(OPEN, open), 3);
        level.playSound(null, pos, FrontierSounds.CRYPT_DOOR.get(), SoundSource.BLOCKS, 0.9F, open ? 1.0F : 0.7F);
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(OPEN)) {
            Direction facing = state.getValue(FACING);
            return switch (facing) {
                case NORTH -> Block.box(0, 0, 13, 16, 16, 16);
                case SOUTH -> Block.box(0, 0, 0, 16, 16, 3);
                case WEST -> Block.box(13, 0, 0, 16, 16, 16);
                default -> Block.box(0, 0, 0, 3, 16, 16);
            };
        }
        return Shapes.block();
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(OPEN) ? Shapes.empty() : Shapes.block();
    }
}
