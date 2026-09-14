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
 * A barracks swallowed mid-muster: bunk rows, a collapsed corner, weapon
 * racks and a sealed officer's alcove behind a rune-hidden door.
 */
public class FallenBarracksPiece extends BaseCryptPiece {
    public static final int WIDTH = 19;
    public static final int HEIGHT = 9;
    public static final int DEPTH = 19;

    public FallenBarracksPiece(BlockPos origin) {
        super(com.frontier.frontier.init.FrontierStructures.FALLEN_BARRACKS_PIECE.get(), origin, WIDTH, HEIGHT, DEPTH);
    }

    public FallenBarracksPiece(net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext context, CompoundTag tag) {
        this(tag);
    }

    public FallenBarracksPiece(CompoundTag tag) {
        super(com.frontier.frontier.init.FrontierStructures.FALLEN_BARRACKS_PIECE.get(), tag);
    }

    @Override
    protected void build(WorldGenLevel level, BoundingBox box, RandomSource random) {
        // Shell: entry hall + main barracks hall.
        room(level, box, 0, 0, 0, 18, 8, 4, BRICKS);          // entry corridor
        room(level, box, 2, 0, 4, 16, 8, 14, BRICKS);          // main hall
        floor(level, box, 2, 5, 16, 13, 0, CRACKED);

        // Bunk rows (fallen slabs) down the hall.
        for (int i = 0; i < 3; i++) {
            int z = 7 + i * 2;
            this.put(level, box, 4, 1, z, Blocks.STONE_SLAB.defaultBlockState());
            this.put(level, box, 5, 1, z, Blocks.STONE_SLAB.defaultBlockState());
            this.put(level, box, 13, 1, z, Blocks.STONE_SLAB.defaultBlockState());
            this.put(level, box, 14, 1, z, Blocks.STONE_SLAB.defaultBlockState());
        }

        // Collapsed corner: rubble slope in the south-west.
        for (int i = 0; i < 5; i++) {
            rubble(level, box, random, 2 + random.nextInt(3), 1 + i / 2, 12 + random.nextInt(2));
        }
        this.put(level, box, 3, 3, 13, Blocks.GRAVEL.defaultBlockState());

        // Pillars + torches along the hall.
        pillar(level, box, 6, 8, 1, 6, CHISELED);
        pillar(level, box, 12, 8, 1, 6, CHISELED);
        torch(level, box, random, 6, 7, 8);
        torch(level, box, random, 12, 7, 8);
        torch(level, box, random, 9, 5, 2);

        // Guardian spawners flanking the muster point.
        spawner(level, box, 5, 1, 10, FrontierEntities.COPPER_SHIELD_SKELETON.get());
        spawner(level, box, 13, 1, 10, FrontierEntities.COPPER_SHIELD_SKELETON.get());

        // Loot: supply chest behind the rubble.
        chest(level, box, random, 3, 1, 11, CryptLoot.FALLEN_BARRACKS_CHEST);

        // Officer's alcove: hidden behind rune + secret door in the back wall.
        room(level, box, 7, 0, 15, 11, 4, 18, BRICKS); // carve hidden room beyond wall
        this.put(level, box, 9, 1, 14, SECRET_DOOR);    // fake wall panel
        rune(level, box, 8, 2, 13, false);
        chest(level, box, random, 9, 1, 17, CryptLoot.FALLEN_BARRACKS_CHEST);
        this.put(level, box, 9, 1, 16, Blocks.BONE_BLOCK.defaultBlockState());
    }
}
