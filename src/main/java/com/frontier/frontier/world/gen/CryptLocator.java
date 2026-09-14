package com.frontier.frontier.world.gen;

import com.frontier.frontier.FrontierMod;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;

/** Finds the nearest Ancient Crypt for the Explorer Compass. */
public final class CryptLocator {
    public static final TagKey<Structure> ANCIENT_CRYPTS =
            TagKey.create(Registries.STRUCTURE, FrontierMod.id("ancient_crypts"));

    private CryptLocator() {
    }

    public static BlockPos findNearestCrypt(ServerLevel level, BlockPos origin, int radius) {
        Registry<Structure> registry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);
        HolderSet.Named<Structure> crypts = registry.getTag(ANCIENT_CRYPTS).orElse(null);
        if (crypts == null || crypts.size() == 0) {
            return null;
        }
        Pair<BlockPos, Holder<Structure>> found = level.getChunkSource().getGenerator()
                .findNearestMapStructure(level, crypts, origin, 100, false);
        return found == null ? null : found.getFirst();
    }
}
