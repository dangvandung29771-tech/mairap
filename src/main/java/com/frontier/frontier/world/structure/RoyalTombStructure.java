package com.frontier.frontier.world.structure;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.StructurePiece;

public class RoyalTombStructure extends CryptStructure {
    public static final MapCodec<RoyalTombStructure> CODEC = simpleCodec(RoyalTombStructure::new);

    public RoyalTombStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected StructurePiece createPiece(BlockPos pos) {
        return new RoyalTombPiece(pos);
    }

    @Override
    public StructureType<?> type() {
        return com.frontier.frontier.init.FrontierStructures.ROYAL_TOMB.get();
    }
}
