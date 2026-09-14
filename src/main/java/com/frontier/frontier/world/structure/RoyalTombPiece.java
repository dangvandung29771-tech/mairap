package com.frontier.frontier.world.structure;

import com.frontier.frontier.init.FrontierEntities;
import com.frontier.frontier.world.CryptLoot;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

/**
 * The grand tomb: a pillared processional hall leading to the sarcophagus,
 * flanked by a treasury alcove sealed with a rune lock.
 */
public class RoyalTombPiece extends BaseCryptPiece {
    public static final int WIDTH = 21;
    public static final int HEIGHT = 9;
    public static final int DEPTH = 17;

    public RoyalTombPiece(BlockPos origin) {
        super(com.frontier.frontier.init.FrontierStructures.ROYAL_TOMB_PIECE.get(), origin, WIDTH, HEIGHT, DEPTH);
    }

    public RoyalTombPiece(net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext context, CompoundTag tag) {
        this(tag);
    }

    public RoyalTombPiece(CompoundTag tag) {
        super(com.frontier.frontier.init.FrontierStructures.ROYAL_TOMB_PIECE.get(), tag);
    }

    @Override
    protected void build(WorldGenLevel level, BoundingBox box, RandomSource random) {
        // Processional hall + tomb chamber.
        room(level, box, 0, 0, 4, 8, 7, 8, BRICKS);
        room(level, box, 8, 0, 0, 20, 8, 12, CHISELED);
        floor(level, box, 8, 0, 20, 12, 0, BRICKS);

        // Pillared colonnade.
        for (int x = 10; x <= 18; x += 4) {
            pillar(level, box, x, 2, 1, 6, CHISELED);
            pillar(level, box, x, 10, 1, 6, CHISELED);
            torch(level, box, random, x, 7, 2);
            torch(level, box, random, x, 7, 10);
        }

        // Royal sarcophagus at the heart, guardians at its feet.
        this.put(level, box, 17, 1, 6, com.frontier.frontier.init.FrontierBlocks.SARCOPHAGUS.get().defaultBlockState());
        spawner(level, box, 14, 1, 3, FrontierEntities.COPPER_SHIELD_SKELETON.get());
        spawner(level, box, 14, 1, 9, FrontierEntities.GOLD_DIGGER_ZOMBIE.get());

        // Treasury alcove behind a rune-warded secret wall.
        room(level, box, 12, 0, 13, 16, 4, 16, BRICKS);
        this.put(level, box, 14, 1, 12, SECRET_DOOR);
        rune(level, box, 13, 2, 11, false);
        rune(level, box, 15, 2, 11, false);
        chest(level, box, random, 13, 1, 15, CryptLoot.ROYAL_TOMB_CHEST);
        chest(level, box, random, 15, 1, 15, CryptLoot.ROYAL_TOMB_CHEST);
        this.put(level, box, 14, 1, 14, Blocks.GOLD_BLOCK.defaultBlockState());

        // Tribute table near the entrance.
        this.put(level, box, 4, 1, 6, CHISELED);
        chest(level, box, random, 3, 1, 6, CryptLoot.ROYAL_TOMB_CHEST);

        // Cobweb dust of ages.
        this.put(level, box, 10, 1, 6, Blocks.COBWEB.defaultBlockState());
        this.put(level, box, 19, 1, 1, Blocks.COBWEB.defaultBlockState());
    }
}
