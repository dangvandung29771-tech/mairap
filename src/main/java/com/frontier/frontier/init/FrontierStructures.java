package com.frontier.frontier.init;

import com.frontier.frontier.FrontierMod;
import com.frontier.frontier.world.structure.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FrontierStructures {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES =
            DeferredRegister.create(Registries.STRUCTURE_TYPE, FrontierMod.MODID);
    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES =
            DeferredRegister.create(Registries.STRUCTURE_PIECE, FrontierMod.MODID);

    public static final Supplier<StructureType<FallenBarracksStructure>> FALLEN_BARRACKS =
            STRUCTURE_TYPES.register("fallen_barracks", () -> () -> FallenBarracksStructure.CODEC);
    public static final Supplier<StructureType<ForgottenMineStructure>> FORGOTTEN_MINE =
            STRUCTURE_TYPES.register("forgotten_mine", () -> () -> ForgottenMineStructure.CODEC);
    public static final Supplier<StructureType<RitualCryptStructure>> RITUAL_CRYPT =
            STRUCTURE_TYPES.register("ritual_crypt", () -> () -> RitualCryptStructure.CODEC);
    public static final Supplier<StructureType<RoyalTombStructure>> ROYAL_TOMB =
            STRUCTURE_TYPES.register("royal_tomb", () -> () -> RoyalTombStructure.CODEC);

    public static final Supplier<StructurePieceType> FALLEN_BARRACKS_PIECE =
            STRUCTURE_PIECE_TYPES.register("fallen_barracks_piece", () -> FallenBarracksPiece::new);
    public static final Supplier<StructurePieceType> FORGOTTEN_MINE_PIECE =
            STRUCTURE_PIECE_TYPES.register("forgotten_mine_piece", () -> ForgottenMinePiece::new);
    public static final Supplier<StructurePieceType> RITUAL_CRYPT_PIECE =
            STRUCTURE_PIECE_TYPES.register("ritual_crypt_piece", () -> RitualCryptPiece::new);
    public static final Supplier<StructurePieceType> ROYAL_TOMB_PIECE =
            STRUCTURE_PIECE_TYPES.register("royal_tomb_piece", () -> RoyalTombPiece::new);
}
