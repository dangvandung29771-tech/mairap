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
 * A circular ritual chamber: four rune pillars, a chiseled altar, and a
 * reliquary hidden beneath the altar behind a rune-sealed floor hatch.
 * The piece origin sits at the vault floor (4 below the chamber floor).
 */
public class RitualCryptPiece extends BaseCryptPiece {
    public static final int WIDTH = 15;
    public static final int HEIGHT = 14;
    public static final int DEPTH = 15;

    public RitualCryptPiece(BlockPos origin) {
        super(com.frontier.frontier.init.FrontierStructures.RITUAL_CRYPT_PIECE.get(), origin, WIDTH, HEIGHT, DEPTH);
    }

    public RitualCryptPiece(CompoundTag tag) {
        super(com.frontier.frontier.init.FrontierStructures.RITUAL_CRYPT_PIECE.get(), tag);
    }

    @Override
    protected void build(WorldGenLevel level, BoundingBox box, RandomSource random) {
        // Hidden reliquary vault (y 0..3), chamber floor at y 4.
        room(level, box, 5, 0, 5, 9, 3, 9, BRICKS);
        chest(level, box, random, 7, 1, 7, CryptLoot.RITUAL_CRYPT_CHEST);
        rune(level, box, 6, 2, 6, true);
        rune(level, box, 8, 2, 8, true);

        // Octagonal-ish chamber.
        room(level, box, 2, 4, 1, 12, 13, 13, STONE);
        room(level, box, 1, 4, 3, 13, 13, 11, STONE);
        floor(level, box, 1, 3, 13, 11, 4, BRICKS);
        floor(level, box, 2, 1, 12, 13, 4, BRICKS);

        // Rune-sealed hatch under the altar.
        this.put(level, box, 7, 4, 7, SECRET_DOOR);

        // Four rune pillars.
        int[][] pillars = {{4, 4}, {10, 4}, {4, 10}, {10, 10}};
        for (int[] p : pillars) {
            pillar(level, box, p[0], p[1], 5, 10, CHISELED);
            rune(level, box, p[0], 8, p[1], random.nextBoolean());
            torch(level, box, random, p[0], 11, p[1]);
        }

        // Central altar with soul-flame offerings.
        this.put(level, box, 7, 5, 7, CHISELED);
        this.put(level, box, 7, 6, 7, Blocks.POLISHED_DEEPSLATE_SLAB.defaultBlockState());
        this.put(level, box, 6, 5, 7, Blocks.SOUL_FIRE.defaultBlockState());
        this.put(level, box, 8, 5, 7, Blocks.SOUL_FIRE.defaultBlockState());
        this.put(level, box, 7, 5, 6, Blocks.SOUL_FIRE.defaultBlockState());
        this.put(level, box, 7, 5, 8, Blocks.SOUL_FIRE.defaultBlockState());

        // Guardian.
        spawner(level, box, 7, 5, 3, FrontierEntities.COPPER_SHIELD_SKELETON.get());

        // Wall niches with bones.
        this.put(level, box, 1, 5, 7, Blocks.BONE_BLOCK.defaultBlockState());
        this.put(level, box, 13, 5, 7, Blocks.BONE_BLOCK.defaultBlockState());
    }
}
