#!/usr/bin/env python3
"""Item models, particles, lang, recipes, loot, tags, worldgen for FRONTIER."""
import json, os

ROOT = os.path.join(os.path.dirname(__file__), '..', 'src', 'main', 'resources')
def w(path, obj):
    p = os.path.join(ROOT, path)
    os.makedirs(os.path.dirname(p), exist_ok=True)
    with open(p, 'w') as f:
        json.dump(obj, f, indent=2, ensure_ascii=False)
        f.write('\n')

IM = 'assets/frontier/models/item/'

def item_model(name):
    w(IM + name + '.json', {"parent": "minecraft:item/generated", "textures": {"layer0": f"frontier:item/{name}"}})

for name in ['sharp_whetstone','balanced_whetstone','heavy_whetstone','precision_crystal','blood_crystal','void_crystal',
             'ancient_shard','gold_fragment','compost_cake','climbers_claw','feather_of_grace','infernal_ring',
             'explorer_compass','stoneheart','blood_pendant','large_raft','pocket_airship']:
    item_model(name)

# ---------- particles ----------
w('assets/frontier/particles/forge_spark.json', {"textures": [f"frontier:forge_spark_{i}" for i in range(8)]})
w('assets/frontier/particles/rune_glow.json', {"textures": [f"frontier:rune_glow_{i}" for i in range(4)]})
w('assets/frontier/particles/steam_puff.json', {"textures": [f"minecraft:big_smoke_{i}" for i in range(12)]})

# ---------- lang ----------
lang = {
  "itemGroup.frontier": "FRONTIER",
  "key.categories.frontier": "FRONTIER",
  "key.frontier.open_relics": "Open Relic Pouch",

  "block.frontier.master_grindstone": "Master Grindstone",
  "block.frontier.ancient_stone": "Ancient Stone",
  "block.frontier.ancient_stone_bricks": "Ancient Stone Bricks",
  "block.frontier.cracked_ancient_stone_bricks": "Cracked Ancient Stone Bricks",
  "block.frontier.chiseled_ancient_stone": "Chiseled Ancient Stone",
  "block.frontier.ancient_rune_block": "Ancient Rune",
  "block.frontier.ancient_secret_door": "Ancient Stone Wall",
  "block.frontier.sarcophagus": "Royal Sarcophagus",
  "block.frontier.wood_canal": "Wood Canal",
  "block.frontier.stone_pipe": "Stone Pipe",
  "block.frontier.sprinkler": "Sprinkler",
  "block.frontier.enriched_farmland": "Enriched Farmland",

  "item.frontier.sharp_whetstone": "Sharp Whetstone",
  "item.frontier.sharp_whetstone.tooltip": "Honed to a whisper-thin edge.",
  "item.frontier.balanced_whetstone": "Balanced Whetstone",
  "item.frontier.balanced_whetstone.tooltip": "Weighted for effortless swings.",
  "item.frontier.heavy_whetstone": "Heavy Whetstone",
  "item.frontier.heavy_whetstone.tooltip": "Dense iron grit that bites deep.",
  "item.frontier.precision_crystal": "Precision Crystal",
  "item.frontier.precision_crystal.tooltip": "Focuses strikes on weak points.",
  "item.frontier.blood_crystal": "Blood Crystal",
  "item.frontier.blood_crystal.tooltip": "Grows hungry when you bleed.",
  "item.frontier.void_crystal": "Void Crystal",
  "item.frontier.void_crystal.tooltip": "Hums with a distant, cold echo.",
  "material.frontier.grants": "Forging material: grants %s",

  "item.frontier.ancient_shard": "Ancient Shard",
  "item.frontier.ancient_shard.tooltip": "A fragment of the old kingdom's craft.",
  "item.frontier.gold_fragment": "Gold Fragment",
  "item.frontier.gold_fragment.tooltip": "Chipped from deep veins by tireless hands.",
  "item.frontier.compost_cake": "Compost Cake",
  "item.frontier.compost_cake.tooltip": "Rich feed for tired soil. Use on farmland.",

  "item.frontier.climbers_claw": "Climber's Claw",
  "item.frontier.climbers_claw.tooltip": "Grips sheer walls for a short climb.",
  "item.frontier.climbers_claw.effect": "Scale walls briefly while airborne",
  "item.frontier.feather_of_grace": "Feather of Grace",
  "item.frontier.feather_of_grace.tooltip": "Falls feel like drifting.",
  "item.frontier.feather_of_grace.effect": "Greatly softens fall damage",
  "item.frontier.infernal_ring": "Infernal Ring",
  "item.frontier.infernal_ring.tooltip": "Burns bright when you are near death.",
  "item.frontier.infernal_ring.effect": "Glows with light below 30% health",
  "item.frontier.explorer_compass": "Explorer Compass",
  "item.frontier.explorer_compass.tooltip": "Use it to attune to a nearby crypt.",
  "item.frontier.explorer_compass.effect": "Whispers the way to Ancient Crypts",
  "item.frontier.explorer_compass.attuned": "The needle shivers... it knows the way.",
  "item.frontier.explorer_compass.not_found": "No crypt stirs within reach of the needle.",
  "item.frontier.explorer_compass.attuned_pos": "Attuned: %s, %s, %s",
  "item.frontier.explorer_compass.unattuned": "Not yet attuned — use the compass.",
  "item.frontier.stoneheart": "Stoneheart",
  "item.frontier.stoneheart.tooltip": "Your roots run deep.",
  "item.frontier.stoneheart.effect": "Resists knockback",
  "item.frontier.blood_pendant": "Blood Pendant",
  "item.frontier.blood_pendant.tooltip": "Drinks a sliver of every victory.",
  "item.frontier.blood_pendant.effect": "Mends you slightly after kills",

  "item.frontier.large_raft": "Large Raft",
  "item.frontier.large_raft.tooltip": "A slow, sturdy home on the water. Sneak to open its chest.",
  "item.frontier.pocket_airship": "Pocket Airship",
  "item.frontier.pocket_airship.tooltip": "Feed it coal, mind the ceiling. W/S fly, A/D steer, jump/sneak climb/descend.",

  "entity.frontier.large_raft": "Large Raft",
  "entity.frontier.pocket_airship": "Pocket Airship",
  "entity.frontier.copper_shield_skeleton": "Copper Shield Skeleton",
  "entity.frontier.gold_digger_zombie": "Gold Digger Zombie",
  "item.frontier.copper_shield_skeleton_spawn_egg": "Copper Shield Skeleton Spawn Egg",
  "item.frontier.gold_digger_zombie_spawn_egg": "Gold Digger Zombie Spawn Egg",

  "container.frontier.master_grindstone": "MASTER GRINDSTONE",
  "container.frontier.relics": "Relic Pouch",
  "container.frontier.large_raft": "Raft Storage",
  "gui.frontier.forge": "FORGE",
  "gui.frontier.weapon": "Weapon",
  "gui.frontier.whetstone": "Whetstone",
  "gui.frontier.crystal": "Crystal",
  "gui.frontier.before": "BEFORE",
  "gui.frontier.after": "AFTER",
  "gui.frontier.ready": "The forge is ready.",
  "gui.frontier.stat.damage": "Damage",
  "gui.frontier.stat.crit": "Crit",
  "gui.frontier.stat.reach": "Reach",
  "gui.frontier.stat.durability": "Durability",
  "gui.frontier.relic.slot": "Relic",
  "gui.frontier.relics.active": "Active Relics",
  "gui.frontier.relics.none": "No relics equipped.",

  "forge.frontier.error.no_weapon": "Place a sword, axe or trident to forge.",
  "forge.frontier.error.no_material": "Add a whetstone or crystal.",
  "forge.frontier.error.max_level": "That edge can take no more.",
  "forge.frontier.error.too_many": "A weapon holds at most two forged properties.",

  "forged.frontier.header": "Forged:",
  "forged.frontier.summary": "+%s%% crit chance, reach %s",
  "forged.frontier.sharp": "Sharp Edge %s",
  "forged.frontier.balanced": "Keen Balance %s",
  "forged.frontier.heavy": "Heavy Hand %s",
  "forged.frontier.precision": "Precision Rune %s",
  "forged.frontier.blood": "Blood Edge %s",
  "forged.frontier.void": "Void Mark %s",

  "relic.frontier.type": "Relic — equip in the Relic Pouch",
  "relic.frontier.compass.nearby": "A crypt is very close...",
  "relic.frontier.compass.direction": "Crypt %s (~%s blocks)",
  "relic.frontier.compass.ahead": "ahead",
  "relic.frontier.compass.behind": "behind you",
  "relic.frontier.compass.left": "to the left",
  "relic.frontier.compass.right": "to the right",

  "frontier.fertility.display": "Fertility: %s (%s/7)",
  "frontier.fertility.natural": "Fertility: natural soil. Enrich with Compost Cake.",

  "hud.frontier.fuel": "Fuel",
  "hud.frontier.altitude": "Altitude: %s",
  "hud.frontier.speed": "Speed: %s m/s",
  "hud.frontier.engine.on": "Engine running",
  "hud.frontier.engine.off": "Engine cold",

  "subtitles.frontier.grindstone.grind": "Grindstone whirs",
  "subtitles.frontier.grindstone.impact": "Metal rings",
  "subtitles.frontier.grindstone.complete": "Forging completes",
  "subtitles.frontier.crypt.ambient": "Crypt echoes",
  "subtitles.frontier.crypt_door": "Stone door grinds",
  "subtitles.frontier.rune": "Rune awakens",
  "subtitles.frontier.raft.creak": "Timbers creak",
  "subtitles.frontier.raft.water": "Water splashes",
  "subtitles.frontier.airship.engine": "Engine crackles",
  "subtitles.frontier.airship.steam": "Steam hisses",
  "subtitles.frontier.relic": "Relic stirs",
  "subtitles.frontier.sprinkler": "Sprinkler sprays",

  "advancements.frontier.root.title": "FRONTIER",
  "advancements.frontier.root.description": "Explore, discover, collect, forge, travel.",
  "advancements.frontier.master_forge.title": "Master of the Wheel",
  "advancements.frontier.master_forge.description": "Forge a property onto a weapon at the Master Grindstone.",

  "tooltip.frontier.whetstone.class": "Whetstone",
  "tooltip.frontier.crystal.class": "Crystal"
}
w('assets/frontier/lang/en_us.json', lang)

# ---------- recipes ----------
R = 'data/frontier/recipe/'
def shaped(name, pattern, key, result, count=1):
    w(R + name + '.json', {"type": "minecraft:crafting_shaped", "pattern": pattern, "key": key,
        "result": {"id": result, "count": count}})
def shapeless(name, ingredients, result, count=1):
    w(R + name + '.json', {"type": "minecraft:crafting_shapeless", "ingredients": ingredients,
        "result": {"id": result, "count": count}})

shaped('master_grindstone', ["SSS","DID","ABA"],
       {"S": {"item": "minecraft:smooth_stone"}, "D": {"item": "minecraft:deepslate"},
        "I": {"item": "minecraft:iron_ingot"}, "A": {"item": "minecraft:anvil"},
        "B": {"item": "frontier:ancient_stone"}},
       "frontier:master_grindstone")
shaped('sharp_whetstone', [" F ","FIF"," F "],
       {"F": {"item": "minecraft:flint"}, "I": {"item": "minecraft:iron_ingot"}},
       "frontier:sharp_whetstone")
shaped('balanced_whetstone', [" W ","SIS"," W "],
       {"W": {"item": "minecraft:string"}, "S": {"item": "minecraft:smooth_stone"},
        "I": {"item": "minecraft:iron_ingot"}},
       "frontier:balanced_whetstone")
shaped('heavy_whetstone', [" O ","OIO"," O "],
       {"O": {"item": "minecraft:obsidian"}, "I": {"item": "minecraft:iron_ingot"}},
       "frontier:heavy_whetstone")
shaped('precision_crystal', [" S ","QAQ"," S "],
       {"S": {"item": "frontier:ancient_shard"}, "Q": {"item": "minecraft:quartz"},
        "A": {"item": "minecraft:amethyst_shard"}},
       "frontier:precision_crystal")
shaped('blood_crystal', [" S ","RNR"," S "],
       {"S": {"item": "frontier:ancient_shard"}, "R": {"item": "minecraft:redstone"},
        "N": {"item": "minecraft:nether_wart"}},
       "frontier:blood_crystal")
shaped('void_crystal', [" S ","EPE"," S "],
       {"S": {"item": "frontier:ancient_shard"}, "E": {"item": "minecraft:ender_pearl"},
        "P": {"item": "minecraft:purpur_block"}},
       "frontier:void_crystal")
shapeless('compost_cake', [{"item":"minecraft:wheat"},{"item":"minecraft:bone_meal"},
                           {"item":"minecraft:egg"},{"item":"minecraft:brown_mushroom"}],
          "frontier:compost_cake")
shaped('wood_canal', ["P P","PPP"],
       {"P": {"tag": "minecraft:planks"}}, "frontier:wood_canal", 3)
shaped('stone_pipe', ["SSS","   ","SSS"],
       {"S": {"item": "minecraft:stone"}}, "frontier:stone_pipe", 4)
shaped('sprinkler', [" C ","PIP","W W"],
       {"C": {"item": "minecraft:copper_ingot"}, "P": {"item": "minecraft:stone"},
        "I": {"item": "minecraft:iron_ingot"}, "W": {"tag": "minecraft:planks"}},
       "frontier:sprinkler")
shaped('large_raft', ["PPP","LPL","PPP"],
       {"P": {"tag": "minecraft:planks"}, "L": {"item": "minecraft:lead"}},
       "frontier:large_raft")
shaped('pocket_airship', ["LLL","SCS","IFI"],
       {"L": {"item": "minecraft:leather"}, "S": {"item": "frontier:ancient_shard"},
        "C": {"item": "minecraft:copper_ingot"}, "I": {"item": "minecraft:iron_ingot"},
        "F": {"item": "minecraft:furnace"}},
       "frontier:pocket_airship")
shaped('climbers_claw', [" S ","CIC"," S "],
       {"S": {"item": "minecraft:string"}, "C": {"item": "minecraft:copper_ingot"},
        "I": {"item": "frontier:ancient_shard"}},
       "frontier:climbers_claw")
shaped('feather_of_grace', [" S ","FSF"," S "],
       {"S": {"item": "frontier:ancient_shard"}, "F": {"item": "minecraft:feather"}},
       "frontier:feather_of_grace")
shaped('stoneheart', [" S ","DHD"," S "],
       {"S": {"item": "frontier:ancient_shard"}, "D": {"item": "minecraft:deepslate"},
        "H": {"item": "minecraft:heart_of_the_sea"}},
       "frontier:stoneheart")

# ---------- loot tables: blocks ----------
LB = 'data/frontier/loot_table/blocks/'
def self_drop(block, conditions=None):
    entry = {"type": "minecraft:item", "name": f"frontier:{block}"}
    if conditions: entry["conditions"] = conditions
    w(LB + block + '.json', {"type": "minecraft:block",
        "pools": [{"rolls": 1, "bonus_rolls": 0, "entries": [entry]}]})

silk = [{"condition": "minecraft:match_tool",
         "predicate": {"predicates": {"minecraft:enchantments": [{"enchantments": "minecraft:silk_touch", "levels": {"min": 1}}]}}}]
for b in ['master_grindstone','ancient_stone','ancient_stone_bricks','cracked_ancient_stone_bricks',
          'chiseled_ancient_stone','ancient_rune_block','ancient_secret_door','sarcophagus',
          'wood_canal','stone_pipe','sprinkler']:
    self_drop(b)
w(LB + 'enriched_farmland.json', {"type": "minecraft:block",
    "pools": [{"rolls": 1, "bonus_rolls": 0,
               "entries": [{"type": "minecraft:item", "name": "minecraft:dirt"}]}]})

print("items/particles/lang/recipes/block loot done")
