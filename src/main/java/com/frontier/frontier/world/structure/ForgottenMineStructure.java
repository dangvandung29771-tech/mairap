package com.frontier.frontier.world.structure;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.StructurePiece;

public class ForgottenMineStructure extends CryptStructure {
    public static final MapCodec<ForgottenMineStructure> CODEC = simpleCodec(ForgottenMineStructure::new);

    public ForgottenMineStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected StructurePiece createPiece(BlockPos pos) {
        return new ForgottenMinePiece(pos);
    }

    @Override
    public StructureType<?> type() {
        return com.frontier.frontier.init.FrontierStructures.FORGOTTEN_MINE.get();
    }
}
