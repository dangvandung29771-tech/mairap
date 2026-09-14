package com.frontier.frontier.block;

import com.frontier.frontier.init.FrontierParticles;
import com.frontier.frontier.init.FrontierSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Carved rune stone found in Ancient Crypts. Runes can be awoken by hand;
 * lit runes glow and shed magical dust. Used as simple environmental puzzles:
 * hidden doors in the crypts open when their flanking runes are lit.
 */
public class AncientRuneBlock extends Block {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public AncientRuneBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack held, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hit) {
        boolean lit = !state.getValue(LIT);
        level.setBlock(pos, state.setValue(LIT, lit), 3);
        level.playSound(null, pos, FrontierSounds.RUNE_ACTIVATE.get(), SoundSource.BLOCKS, 0.9F, lit ? 1.0F : 0.6F);
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT) && random.nextInt(6) == 0) {
            level.addParticle(FrontierParticles.RUNE_GLOW.get(),
                    pos.getX() + 0.2 + random.nextDouble() * 0.6,
                    pos.getY() + 0.2 + random.nextDouble() * 0.6,
                    pos.getZ() + 0.2 + random.nextDouble() * 0.6,
                    0, 0.015, 0);
        }
    }
}
