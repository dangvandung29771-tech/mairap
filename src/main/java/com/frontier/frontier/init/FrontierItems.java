package com.frontier.frontier.init;

import com.frontier.frontier.FrontierMod;
import com.frontier.frontier.item.ExplorerCompassItem;
import com.frontier.frontier.item.ForgingMaterialItem;
import com.frontier.frontier.forge.ForgedProperty;
import com.frontier.frontier.item.RelicItem;
import com.frontier.frontier.item.VehicleItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class FrontierItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.Items.createItems(FrontierMod.MODID);

    // === Block items ===
    public static final DeferredItem<Item> MASTER_GRINDSTONE_ITEM = ITEMS.registerSimpleBlockItem(FrontierBlocks.MASTER_GRINDSTONE);
    public static final DeferredItem<Item> ANCIENT_STONE_ITEM = ITEMS.registerSimpleBlockItem(FrontierBlocks.ANCIENT_STONE);
    public static final DeferredItem<Item> ANCIENT_STONE_BRICKS_ITEM = ITEMS.registerSimpleBlockItem(FrontierBlocks.ANCIENT_STONE_BRICKS);
    public static final DeferredItem<Item> CRACKED_ANCIENT_STONE_BRICKS_ITEM = ITEMS.registerSimpleBlockItem(FrontierBlocks.CRACKED_ANCIENT_STONE_BRICKS);
    public static final DeferredItem<Item> CHISELED_ANCIENT_STONE_ITEM = ITEMS.registerSimpleBlockItem(FrontierBlocks.CHISELED_ANCIENT_STONE);
    public static final DeferredItem<Item> ANCIENT_RUNE_ITEM = ITEMS.registerSimpleBlockItem(FrontierBlocks.ANCIENT_RUNE_BLOCK);
    public static final DeferredItem<Item> ANCIENT_SECRET_DOOR_ITEM = ITEMS.registerSimpleBlockItem(FrontierBlocks.ANCIENT_SECRET_DOOR);
    public static final DeferredItem<Item> SARCOPHAGUS_ITEM = ITEMS.registerSimpleBlockItem(FrontierBlocks.SARCOPHAGUS);
    public static final DeferredItem<Item> WOOD_CANAL_ITEM = ITEMS.registerSimpleBlockItem(FrontierBlocks.WOOD_CANAL);
    public static final DeferredItem<Item> STONE_PIPE_ITEM = ITEMS.registerSimpleBlockItem(FrontierBlocks.STONE_PIPE);
    public static final DeferredItem<Item> SPRINKLER_ITEM = ITEMS.registerSimpleBlockItem(FrontierBlocks.SPRINKLER);
    public static final DeferredItem<Item> ENRICHED_FARMLAND_ITEM = ITEMS.registerSimpleBlockItem(FrontierBlocks.ENRICHED_FARMLAND);

    // === Forging materials ===
    public static final DeferredItem<ForgingMaterialItem> SHARP_WHETSTONE = ITEMS.registerItem("sharp_whetstone",
            properties -> new ForgingMaterialItem(properties, ForgedProperty.SHARP), new Item.Properties());
    public static final DeferredItem<ForgingMaterialItem> BALANCED_WHETSTONE = ITEMS.registerItem("balanced_whetstone",
            properties -> new ForgingMaterialItem(properties, ForgedProperty.BALANCED), new Item.Properties());
    public static final DeferredItem<ForgingMaterialItem> HEAVY_WHETSTONE = ITEMS.registerItem("heavy_whetstone",
            properties -> new ForgingMaterialItem(properties, ForgedProperty.HEAVY), new Item.Properties());
    public static final DeferredItem<ForgingMaterialItem> PRECISION_CRYSTAL = ITEMS.registerItem("precision_crystal",
            properties -> new ForgingMaterialItem(properties, ForgedProperty.PRECISION), new Item.Properties());
    public static final DeferredItem<ForgingMaterialItem> BLOOD_CRYSTAL = ITEMS.registerItem("blood_crystal",
            properties -> new ForgingMaterialItem(properties, ForgedProperty.BLOOD), new Item.Properties());
    public static final DeferredItem<ForgingMaterialItem> VOID_CRYSTAL = ITEMS.registerItem("void_crystal",
            properties -> new ForgingMaterialItem(properties, ForgedProperty.VOID), new Item.Properties());

    // === Materials ===
    public static final DeferredItem<Item> ANCIENT_SHARD = ITEMS.registerItem("ancient_shard", Item::new, new Item.Properties());
    public static final DeferredItem<Item> GOLD_FRAGMENT = ITEMS.registerItem("gold_fragment", Item::new, new Item.Properties());
    public static final DeferredItem<Item> COMPOST_CAKE = ITEMS.registerItem("compost_cake", Item::new, new Item.Properties());

    // === Relics ===
    public static final DeferredItem<RelicItem> CLIMBERS_CLAW = ITEMS.registerItem("climbers_claw",
            properties -> new RelicItem(properties, false), new Item.Properties());
    public static final DeferredItem<RelicItem> FEATHER_OF_GRACE = ITEMS.registerItem("feather_of_grace",
            properties -> new RelicItem(properties, false), new Item.Properties());
    public static final DeferredItem<RelicItem> INFERNAL_RING = ITEMS.registerItem("infernal_ring",
            properties -> new RelicItem(properties, true), new Item.Properties());
    public static final DeferredItem<ExplorerCompassItem> EXPLORER_COMPASS = ITEMS.registerItem("explorer_compass",
            ExplorerCompassItem::new, new Item.Properties());
    public static final DeferredItem<RelicItem> STONEHEART = ITEMS.registerItem("stoneheart",
            properties -> new RelicItem(properties, false), new Item.Properties());
    public static final DeferredItem<RelicItem> BLOOD_PENDANT = ITEMS.registerItem("blood_pendant",
            properties -> new RelicItem(properties, true), new Item.Properties());

    // === Spawn eggs ===
    public static final DeferredItem<net.neoforged.neoforge.common.DeferredSpawnEggItem> COPPER_SHIELD_SKELETON_SPAWN_EGG =
            ITEMS.registerItem("copper_shield_skeleton_spawn_egg",
                    properties -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(
                            FrontierEntities.COPPER_SHIELD_SKELETON, 0xC9C0B0, 0xC46A3A, properties),
                    new Item.Properties());
    public static final DeferredItem<net.neoforged.neoforge.common.DeferredSpawnEggItem> GOLD_DIGGER_ZOMBIE_SPAWN_EGG =
            ITEMS.registerItem("gold_digger_zombie_spawn_egg",
                    properties -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(
                            FrontierEntities.GOLD_DIGGER_ZOMBIE, 0x5E7A4A, 0xD8A040, properties),
                    new Item.Properties());

    // === Vehicles ===
    public static final DeferredItem<VehicleItem> LARGE_RAFT = ITEMS.registerItem("large_raft",
            properties -> new VehicleItem(properties, FrontierEntities.LARGE_RAFT), new Item.Properties());
    public static final DeferredItem<VehicleItem> POCKET_AIRSHIP = ITEMS.registerItem("pocket_airship",
            properties -> new VehicleItem(properties, FrontierEntities.POCKET_AIRSHIP), new Item.Properties());
}
