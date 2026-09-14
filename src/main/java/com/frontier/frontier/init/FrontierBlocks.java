package com.frontier.frontier.init;

import com.frontier.frontier.FrontierMod;
import com.frontier.frontier.block.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class FrontierBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.Blocks.createBlocks(FrontierMod.MODID);

    // === Forging ===
    public static final DeferredBlock<MasterGrindstoneBlock> MASTER_GRINDSTONE = BLOCKS.registerBlock("master_grindstone",
            MasterGrindstoneBlock::new, BlockBehaviour.Properties.of()
                    .strength(4.5F, 9.0F)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()
                    .lightLevel(state -> state.getValue(MasterGrindstoneBlock.ACTIVE) ? 10 : 0));

    // === Ancient Crypt masonry ===
    public static final DeferredBlock<Block> ANCIENT_STONE = BLOCKS.registerSimpleBlock("ancient_stone",
            BlockBehaviour.Properties.of().strength(3.5F, 9.0F).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops());
    public static final DeferredBlock<Block> ANCIENT_STONE_BRICKS = BLOCKS.registerSimpleBlock("ancient_stone_bricks",
            BlockBehaviour.Properties.of().strength(3.2F, 9.0F).sound(SoundType.DEEPSLATE_BRICKS).requiresCorrectToolForDrops());
    public static final DeferredBlock<Block> CRACKED_ANCIENT_STONE_BRICKS = BLOCKS.registerSimpleBlock("cracked_ancient_stone_bricks",
            BlockBehaviour.Properties.of().strength(3.0F, 8.0F).sound(SoundType.DEEPSLATE_BRICKS).requiresCorrectToolForDrops());
    public static final DeferredBlock<Block> CHISELED_ANCIENT_STONE = BLOCKS.registerSimpleBlock("chiseled_ancient_stone",
            BlockBehaviour.Properties.of().strength(3.2F, 9.0F).sound(SoundType.POLISHED_DEEPSLATE).requiresCorrectToolForDrops());

    public static final DeferredBlock<AncientRuneBlock> ANCIENT_RUNE_BLOCK = BLOCKS.registerBlock("ancient_rune_block",
            AncientRuneBlock::new, BlockBehaviour.Properties.of()
                    .strength(3.4F, 9.0F)
                    .sound(SoundType.POLISHED_DEEPSLATE)
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(AncientRuneBlock.LIT) ? 9 : 0)
                    .emissiveRendering((state, level, pos) -> state.getValue(AncientRuneBlock.LIT)));

    public static final DeferredBlock<AncientSecretDoorBlock> ANCIENT_SECRET_DOOR = BLOCKS.registerBlock("ancient_secret_door",
            AncientSecretDoorBlock::new, BlockBehaviour.Properties.of()
                    .strength(3.4F, 9.0F)
                    .sound(SoundType.DEEPSLATE_BRICKS)
                    .noOcclusion());

    public static final DeferredBlock<SarcophagusBlock> SARCOPHAGUS = BLOCKS.registerBlock("sarcophagus",
            SarcophagusBlock::new, BlockBehaviour.Properties.of()
                    .strength(3.6F, 9.0F)
                    .sound(SoundType.POLISHED_DEEPSLATE)
                    .requiresCorrectToolForDrops()
                    .noOcclusion());

    // === Irrigation ===
    public static final DeferredBlock<WoodCanalBlock> WOOD_CANAL = BLOCKS.registerBlock("wood_canal",
            WoodCanalBlock::new, BlockBehaviour.Properties.of()
                    .strength(1.6F)
                    .sound(SoundType.WOOD)
                    .noOcclusion());

    public static final DeferredBlock<StonePipeBlock> STONE_PIPE = BLOCKS.registerBlock("stone_pipe",
            StonePipeBlock::new, BlockBehaviour.Properties.of()
                    .strength(2.4F, 8.0F)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()
                    .noOcclusion());

    public static final DeferredBlock<SprinklerBlock> SPRINKLER = BLOCKS.registerBlock("sprinkler",
            SprinklerBlock::new, BlockBehaviour.Properties.of()
                    .strength(2.2F)
                    .sound(SoundType.COPPER)
                    .noOcclusion());

    // === Farming ===
    public static final DeferredBlock<EnrichedFarmlandBlock> ENRICHED_FARMLAND = BLOCKS.registerBlock("enriched_farmland",
            EnrichedFarmlandBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND));
}
