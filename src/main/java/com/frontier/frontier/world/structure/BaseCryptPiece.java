package com.frontier.frontier.world.structure;

import com.frontier.frontier.block.AncientRuneBlock;
import com.frontier.frontier.init.FrontierBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.resources.ResourceKey;

/**
 * Shared building kit for hand-designed crypt interiors. Coordinates are
 * piece-local (0,0,0 = piece origin, +y up).
 */
public abstract class BaseCryptPiece extends StructurePiece {
    protected static final BlockState STONE = FrontierBlocks.ANCIENT_STONE.get().defaultBlockState();
    protected static final BlockState BRICKS = FrontierBlocks.ANCIENT_STONE_BRICKS.get().defaultBlockState();
    protected static final BlockState CRACKED = FrontierBlocks.CRACKED_ANCIENT_STONE_BRICKS.get().defaultBlockState();
    protected static final BlockState CHISELED = FrontierBlocks.CHISELED_ANCIENT_STONE.get().defaultBlockState();
    protected static final BlockState RUNE = FrontierBlocks.ANCIENT_RUNE_BLOCK.get().defaultBlockState();
    protected static final BlockState SECRET_DOOR = FrontierBlocks.ANCIENT_SECRET_DOOR.get().defaultBlockState();
    protected static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();

    protected BaseCryptPiece(StructurePieceType type, BlockPos origin, int width, int height, int depth) {
        super(type, 0, new BoundingBox(origin.getX(), origin.getY(), origin.getZ(),
                origin.getX() + width - 1, origin.getY() + height - 1, origin.getZ() + depth - 1));
    }

    protected BaseCryptPiece(StructurePieceType type, CompoundTag tag) {
        super(type, tag);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
    }

    /** Carves a room: walls of {@code wall}, interior filled with cave air. */
    protected void room(WorldGenLevel level, BoundingBox box, int x1, int y1, int z1, int x2, int y2, int z2, BlockState wall) {
        this.generateBox(level, box, x1, y1, z1, x2, y2, z2, wall, AIR, false);
    }

    protected void floor(WorldGenLevel level, BoundingBox box, int x1, int z1, int x2, int z2, int y, BlockState state) {
        this.generateBox(level, box, x1, y, z1, x2, y, z2, state, state, false);
    }

    protected void put(WorldGenLevel level, BoundingBox box, int x, int y, int z, BlockState state) {
        this.placeBlock(level, state, x, y, z, box);
    }

    protected void pillar(WorldGenLevel level, BoundingBox box, int x, int z, int y1, int y2, BlockState state) {
        for (int y = y1; y <= y2; y++) {
            this.put(level, box, x, y, z, state);
        }
    }

    protected void torch(WorldGenLevel level, BoundingBox box, RandomSource random, int x, int y, int z) {
        this.put(level, box, x, y, z, Blocks.TORCH.defaultBlockState());
    }

    protected void rune(WorldGenLevel level, BoundingBox box, int x, int y, int z, boolean lit) {
        this.put(level, box, x, y, z, RUNE.setValue(AncientRuneBlock.LIT, lit));
    }

    protected void spawner(WorldGenLevel level, BoundingBox box, int x, int y, int z, EntityType<?> type) {
        BlockPos pos = this.getWorldPos(x, y, z);
        if (!box.isInside(pos)) {
            return;
        }
        level.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), 2);
        if (level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner) {
            spawner.setEntityId(type, level.getRandom());
        }
    }

    protected boolean chest(WorldGenLevel level, BoundingBox box, RandomSource random, int x, int y, int z,
                            ResourceKey<LootTable> loot) {
        return this.createChest(level, box, random, x, y, z, loot);
    }

    protected void rubble(WorldGenLevel level, BoundingBox box, RandomSource random, int x, int y, int z) {
        if (random.nextInt(3) == 0) {
            this.put(level, box, x, y, z, CRACKED);
        } else if (random.nextInt(4) == 0) {
            this.put(level, box, x, y, z, Blocks.GRAVEL.defaultBlockState());
        } else {
            this.put(level, box, x, y, z, STONE);
        }
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structureManager,
                            ChunkGenerator generator, RandomSource random, BoundingBox box,
                            net.minecraft.world.level.ChunkPos chunkPos, BlockPos origin) {
        build(level, box, random);
    }

    /** Hand-designed layout for this crypt type. */
    protected abstract void build(WorldGenLevel level, BoundingBox box, RandomSource random);
}
