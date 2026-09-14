package com.frontier.frontier.world.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.StructurePiece;

public class FallenBarracksStructure extends CryptStructure {
    public static final MapCodec<FallenBarracksStructure> CODEC = simpleCodec(FallenBarracksStructure::new);

    public FallenBarracksStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected StructurePiece createPiece(BlockPos pos) {
        return new FallenBarracksPiece(pos);
    }

    @Override
    public StructureType<?> type() {
        return com.frontier.frontier.init.FrontierStructures.FALLEN_BARRACKS.get();
    }
}
