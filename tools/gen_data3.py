#!/usr/bin/env python3
"""Chest loot, tags, worldgen structures, advancements for FRONTIER."""
import json, os

ROOT = os.path.join(os.path.dirname(__file__), '..', 'src', 'main', 'resources')
def w(path, obj):
    p = os.path.join(ROOT, path)
    os.makedirs(os.path.dirname(p), exist_ok=True)
    with open(p, 'w') as f:
        json.dump(obj, f, indent=2, ensure_ascii=False)
        f.write('\n')

def item_entry(name, weight=1, count=None):
    e = {"type": "minecraft:item", "name": name, "weight": weight}
    if count: e["functions"] = [{"function": "minecraft:set_count", "count": count}]
    return e

def pool(entries, rolls=None):
    return {"rolls": rolls if rolls else 1, "bonus_rolls": 0, "entries": entries}

# ---------- crypt chest loot ----------
CH = 'data/frontier/loot_table/chests/'

w(CH + 'crypt_fallen_barracks.json', {"type": "minecraft:chest", "pools": [
    pool([item_entry("frontier:ancient_shard", 4, {"min": 1, "max": 3}),
          item_entry("frontier:sharp_whetstone", 2),
          item_entry("frontier:heavy_whetstone", 2),
          item_entry("frontier:ancient_stone_bricks", 3, {"min": 4, "max": 12}),
          item_entry("minecraft:chainmail_chestplate", 1),
          item_entry("minecraft:iron_sword", 1),
          item_entry("minecraft:bone", 3, {"min": 2, "max": 6}),
          item_entry("minecraft:arrow", 2, {"min": 4, "max": 10})], {"min": 3, "max": 5}),
    pool([item_entry("frontier:stoneheart", 1),
          item_entry("frontier:climbers_claw", 1)], rolls=1)
]})

w(CH + 'crypt_forgotten_mine.json', {"type": "minecraft:chest", "pools": [
    pool([item_entry("frontier:ancient_shard", 4, {"min": 1, "max": 3}),
          item_entry("frontier:gold_fragment", 4, {"min": 2, "max": 5}),
          item_entry("minecraft:raw_gold", 3, {"min": 1, "max": 4}),
          item_entry("minecraft:iron_pickaxe", 2),
          item_entry("minecraft:rail", 2, {"min": 4, "max": 8}),
          item_entry("minecraft:torch", 3, {"min": 4, "max": 12}),
          item_entry("frontier:balanced_whetstone", 2)], {"min": 3, "max": 5}),
    pool([item_entry("frontier:explorer_compass", 1),
          item_entry("frontier:climbers_claw", 1)], rolls=1)
]})

w(CH + 'crypt_ritual_crypt.json', {"type": "minecraft:chest", "pools": [
    pool([item_entry("frontier:ancient_shard", 4, {"min": 2, "max": 4}),
          item_entry("frontier:precision_crystal", 2),
          item_entry("frontier:blood_crystal", 2),
          item_entry("frontier:void_crystal", 1),
          item_entry("minecraft:experience_bottle", 2, {"min": 2, "max": 5}),
          item_entry("minecraft:amethyst_shard", 2, {"min": 2, "max": 6}),
          item_entry("minecraft:book", 1)], {"min": 3, "max": 5}),
    pool([item_entry("frontier:blood_pendant", 1),
          item_entry("frontier:infernal_ring", 1),
          item_entry("frontier:feather_of_grace", 1)], rolls=1)
]})

w(CH + 'crypt_royal_tomb.json', {"type": "minecraft:chest", "pools": [
    pool([item_entry("frontier:ancient_shard", 4, {"min": 3, "max": 6}),
          item_entry("minecraft:gold_ingot", 3, {"min": 2, "max": 6}),
          item_entry("minecraft:diamond", 2, {"min": 1, "max": 2}),
          item_entry("minecraft:golden_apple", 1),
          item_entry("frontier:sharp_whetstone", 1),
          item_entry("frontier:precision_crystal", 1),
          item_entry("frontier:void_crystal", 1),
          item_entry("minecraft:enchanted_book", 1, {"min": 1, "max": 1})], {"min": 4, "max": 6}),
    pool([item_entry("frontier:blood_pendant", 1),
          item_entry("frontier:infernal_ring", 1),
          item_entry("frontier:explorer_compass", 1)], rolls=1)
]})

# ---------- tags ----------
T = 'data/'
def tag(path, values):
    w(path, {"replace": False, "values": values})

tag(T + 'minecraft/tags/block/mineable/pickaxe.json', [
    "frontier:master_grindstone","frontier:ancient_stone","frontier:ancient_stone_bricks",
    "frontier:cracked_ancient_stone_bricks","frontier:chiseled_ancient_stone",
    "frontier:ancient_rune_block","frontier:ancient_secret_door","frontier:sarcophagus","frontier:stone_pipe"])
tag(T + 'minecraft/tags/block/mineable/axe.json', ["frontier:wood_canal","frontier:sprinkler"])
tag(T + 'minecraft/tags/block/mineable/hoe.json', ["frontier:enriched_farmland"])
tag(T + 'minecraft/tags/block/maintains_farmland.json', ["frontier:enriched_farmland"])
tag(T + 'frontier/tags/worldgen/structure/ancient_crypts.json', [
    "frontier:fallen_barracks","frontier:forgotten_mine","frontier:ritual_crypt","frontier:royal_tomb"])
tag(T + 'frontier/tags/worldgen/biome/has_structure/ancient_crypt.json', [
    {"id": "#minecraft:is_overworld", "required": False}])

# ---------- worldgen structures ----------
W = 'data/frontier/worldgen/'
def structure(name, stype):
    w(W + f'structure/{name}.json', {
        "type": f"frontier:{stype}",
        "biomes": "#frontier:has_structure/ancient_crypt",
        "step": "underground_structures",
        "terrain_adaptation": "none",
        "max_distance_from_center": 32,
        "spawn_overrides": {
            "monster": {
                "bounding_box": "piece",
                "spawns": [
                    {"type": "frontier:copper_shield_skeleton", "weight": 60, "minCount": 1, "maxCount": 2},
                    {"type": "frontier:gold_digger_zombie", "weight": 40, "minCount": 1, "maxCount": 2}
                ]
            }
        }
    })

structure('fallen_barracks', 'fallen_barracks')
structure('forgotten_mine', 'forgotten_mine')
structure('ritual_crypt', 'ritual_crypt')
structure('royal_tomb', 'royal_tomb')

w(W + 'structure_set/ancient_crypts.json', {
    "structures": [
        {"structure": "frontier:fallen_barracks", "weight": 3},
        {"structure": "frontier:forgotten_mine", "weight": 3},
        {"structure": "frontier:ritual_crypt", "weight": 2},
        {"structure": "frontier:royal_tomb", "weight": 2}
    ],
    "placement": {
        "type": "minecraft:random_spread",
        "spacing": 22,
        "separation": 8,
        "salt": 1460197
    }
})

# ---------- advancements ----------
w('data/frontier/advancement/root.json', {
    "display": {
        "icon": {"id": "frontier:master_grindstone"},
        "title": {"translate": "advancements.frontier.root.title"},
        "description": {"translate": "advancements.frontier.root.description"},
        "background": "frontier:textures/block/ancient_stone_bricks.png",
        "announce_to_chat": False, "show_toast": False
    },
    "criteria": {"crafting_table": {"trigger": "minecraft:inventory_changed",
        "conditions": {"items": [{"items": "minecraft:crafting_table"}]}}}
})
w('data/frontier/advancement/master_forge.json', {
    "parent": "frontier:root",
    "display": {
        "icon": {"id": "frontier:sharp_whetstone"},
        "title": {"translate": "advancements.frontier.master_forge.title"},
        "description": {"translate": "advancements.frontier.master_forge.description"},
        "announce_to_chat": True
    },
    "criteria": {"used_grindstone": {"trigger": "minecraft:item_used_on_block",
        "conditions": {"location": [{"condition": "minecraft:location_check",
            "predicate": {"block": {"blocks": ["frontier:master_grindstone"]}}}]}}}
})

print("loot/tags/worldgen/advancements done")
