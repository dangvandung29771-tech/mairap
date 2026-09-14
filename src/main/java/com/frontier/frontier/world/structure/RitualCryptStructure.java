package com.frontier.frontier.world.structure;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiece;

public class RitualCryptStructure extends CryptStructure {
    public static final MapCodec<RitualCryptStructure> CODEC = simpleCodec(RitualCryptStructure::new);

    public RitualCryptStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected StructurePiece createPiece(BlockPos pos) {
        return new RitualCryptPiece(pos);
    }

    @Override
    public StructureType<?> type() {
        return com.frontier.frontier.init.FrontierStructures.RITUAL_CRYPT.get();
    }
}
