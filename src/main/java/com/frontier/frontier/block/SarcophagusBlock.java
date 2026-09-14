package com.frontier.frontier.block;

import com.frontier.frontier.init.FrontierEntities;
import com.frontier.frontier.init.FrontierSounds;
import com.frontier.frontier.world.CryptLoot;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Royal tomb centerpiece. Opening it yields treasure — but the tomb does not
 * forgive trespassers.
 */
public class SarcophagusBlock extends Block {
    public static final BooleanProperty OPENED = BooleanProperty.create("opened");
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 10, 15);

    public SarcophagusBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(OPENED, false).setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OPENED, FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack held, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(OPENED) || level.isClientSide) {
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        ServerLevel serverLevel = (ServerLevel) level;
        level.setBlock(pos, state.setValue(OPENED, true), 3);
        level.playSound(null, pos, FrontierSounds.CRYPT_DOOR.get(), SoundSource.BLOCKS, 1.0F, 0.6F);

        LootTable table = serverLevel.getServer().reloadableRegistries().getLootTable(CryptLoot.ROYAL_TOMB_CHEST);
        LootParams params = new LootParams.Builder(serverLevel)
                .withParameter(LootContextParams.ORIGIN, pos.getCenter())
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .create(LootContextParamSets.CHEST);
        table.getRandomItems(params).forEach(stack ->
                Block.popResource(level, pos.above(), stack));

        // The tomb's guardians wake.
        for (int i = 0; i < 2; i++) {
            Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(level.random);
            BlockPos spawnPos = pos.relative(direction, 2);
            FrontierEntities.COPPER_SHIELD_SKELETON.get().spawn(serverLevel, spawnPos, MobSpawnType.TRIGGERED);
        }
        return ItemInteractionResult.CONSUME;
    }
}
