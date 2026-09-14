package com.frontier.frontier.world.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiece;

/**
 * Base for the four Ancient Crypt structure types. Crypts anchor below the
 * deepslate layers (y -56 .. -6) and are otherwise hand-designed pieces.
 */
public abstract class CryptStructure extends Structure {
    protected CryptStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected java.util.Optional<GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        var random = context.random();
        int x = context.chunkPos().getMinBlockX() + random.nextInt(16);
        int z = context.chunkPos().getMinBlockZ() + random.nextInt(16);
        int y = -56 + random.nextInt(44); // -56 .. -13
        BlockPos pos = new BlockPos(x, y, z);
        return java.util.Optional.of(new GenerationStub(pos, builder -> builder.addPiece(createPiece(pos))));
    }

    protected abstract StructurePiece createPiece(BlockPos pos);

    @Override
    protected int maxDistanceFromCenter() {
        return 32;
    }
}
