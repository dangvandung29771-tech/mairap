package com.frontier.frontier.world.structure;

import com.frontier.frontier.init.FrontierEntities;
import com.frontier.frontier.world.CryptLoot;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

/**
 * A mining tunnel network abandoned in a hurry: timber supports, ore seams,
 * rails — and unstable charges left under the gold face.
 */
public class ForgottenMinePiece extends BaseCryptPiece {
    public static final int WIDTH = 17;
    public static final int HEIGHT = 10;
    public static final int DEPTH = 17;
    private static final BlockState ORE = Blocks.DEEPSLATE_GOLD_ORE.defaultBlockState();
    private static final BlockState LOG = Blocks.OAK_LOG.defaultBlockState();

    public ForgottenMinePiece(BlockPos origin) {
        super(com.frontier.frontier.init.FrontierStructures.FORGOTTEN_MINE_PIECE.get(), origin, WIDTH, HEIGHT, DEPTH);
    }

    public ForgottenMinePiece(CompoundTag tag) {
        super(com.frontier.frontier.init.FrontierStructures.FORGOTTEN_MINE_PIECE.get(), tag);
    }

    @Override
    protected void build(WorldGenLevel level, BoundingBox box, RandomSource random) {
        // Main shaft running north-south.
        room(level, box, 3, 0, 0, 13, 6, 16, STONE);
        floor(level, box, 3, 0, 13, 16, 0, STONE);

        // Timber support frames every few blocks.
        for (int z = 2; z <= 14; z += 3) {
            pillar(level, box, 4, z, 1, 4, LOG);
            pillar(level, box, 12, z, 1, 4, LOG);
            for (int x = 4; x <= 12; x++) {
                this.put(level, box, x, 5, z, LOG);
            }
        }

        // Rails with a parked minecart.
        for (int z = 1; z <= 15; z++) {
            this.put(level, box, 8, 1, z, Blocks.RAIL.defaultBlockState());
        }
        // A forgotten supply chest sits where the last cart was parked.
        chest(level, box, random, 8, 1, 12, CryptLoot.FORGOTTEN_MINE_CHEST);

        // Gold seam on the east wall — with a surprise underneath.
        for (int i = 0; i < 5; i++) {
            this.put(level, box, 13, 1 + random.nextInt(3), 4 + i, ORE);
        }
        this.put(level, box, 14, 0, 6, Blocks.TNT.defaultBlockState());
        this.put(level, box, 14, 0, 7, Blocks.TNT.defaultBlockState());

        // Side drift with the digger's camp.
        room(level, box, 0, 0, 6, 3, 4, 11, STONE);
        torch(level, box, random, 2, 3, 8);
        chest(level, box, random, 1, 1, 9, CryptLoot.FORGOTTEN_MINE_CHEST);
        this.put(level, box, 2, 1, 7, Blocks.CRAFTING_TABLE.defaultBlockState());
        this.put(level, box, 1, 1, 7, Blocks.ANVIL.defaultBlockState());

        // Rubble fall near the entrance.
        for (int i = 0; i < 6; i++) {
            rubble(level, box, random, 4 + random.nextInt(2), 1 + random.nextInt(2), 1 + random.nextInt(3));
        }

        spawner(level, box, 8, 1, 4, FrontierEntities.GOLD_DIGGER_ZOMBIE.get());
        spawner(level, box, 6, 1, 14, FrontierEntities.COPPER_SHIELD_SKELETON.get());

        // Gilded flecks dropped in the panic.
        this.put(level, box, 10, 1, 8, ORE);
    }
}
