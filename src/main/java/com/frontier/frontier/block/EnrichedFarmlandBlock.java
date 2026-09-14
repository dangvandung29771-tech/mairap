package com.frontier.frontier.block;

import com.frontier.frontier.init.FrontierItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Farmland nourished with Compost Cake. Carries a fertility reserve that
 * gently accelerates crop growth and slowly depletes as it feeds them.
 */
public class EnrichedFarmlandBlock extends FarmBlock {
    public static final IntegerProperty FERTILITY = IntegerProperty.create("fertility", 0, 7);

    public EnrichedFarmlandBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(MOISTURE, 0).setValue(FERTILITY, 7));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FERTILITY);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        BlockState current = level.getBlockState(pos);
        if (!current.is(this)) {
            return;
        }
        int fertility = current.getValue(FERTILITY);
        if (fertility <= 0) {
            return;
        }
        BlockPos cropPos = pos.above();
        BlockState crop = level.getBlockState(cropPos);
        if (crop.getBlock() instanceof BonemealableBlock bonemealable
                && bonemealable.isValidBonemealTarget(level, cropPos, crop)
                && random.nextFloat() < 0.05F + fertility * 0.02F) {
            bonemealable.performBonemeal(level, random, cropPos, crop);
            if (random.nextFloat() < 0.4F) {
                level.setBlock(pos, current.setValue(FERTILITY, fertility - 1), 2);
            }
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack held, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hit) {
        if (held.is(FrontierItems.COMPOST_CAKE.get()) && state.getValue(FERTILITY) < 7) {
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(FERTILITY, Math.min(7, state.getValue(FERTILITY) + 4)), 3);
                held.shrink(1);
                level.playSound(null, pos, SoundEvents.COMPOSTER_FILL_SUCCESS, SoundSource.BLOCKS, 1.0F, 1.0F);
                ((ServerLevel) level).sendParticles(ParticleTypes.HAPPY_VILLAGER,
                        pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5, 8, 0.4, 0.2, 0.4, 0.1);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    /** Fertility bar string used by the inspection readout, e.g. ██████░░. */
    public static String fertilityBar(int fertility) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            builder.append(i < fertility ? '█' : '░');
        }
        return builder.toString();
    }
}
