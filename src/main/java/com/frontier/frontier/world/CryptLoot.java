package com.frontier.frontier.world;

import com.frontier.frontier.FrontierMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

/** Loot table keys for the four Ancient Crypt types. */
public final class CryptLoot {
    public static final ResourceKey<LootTable> FALLEN_BARRACKS_CHEST =
            key("chests/crypt_fallen_barracks");
    public static final ResourceKey<LootTable> FORGOTTEN_MINE_CHEST =
            key("chests/crypt_forgotten_mine");
    public static final ResourceKey<LootTable> RITUAL_CRYPT_CHEST =
            key("chests/crypt_ritual_crypt");
    public static final ResourceKey<LootTable> ROYAL_TOMB_CHEST =
            key("chests/crypt_royal_tomb");

    private CryptLoot() {
    }

    private static ResourceKey<LootTable> key(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, FrontierMod.id(path));
    }
}
