#!/usr/bin/env python3
"""Generates all data-driven assets for FRONTIER (blockstates, models, lang, recipes, loot, tags, worldgen)."""
import json, os

ROOT = os.path.join(os.path.dirname(__file__), '..', 'src', 'main', 'resources')
def w(path, obj):
    p = os.path.join(ROOT, path)
    os.makedirs(os.path.dirname(p), exist_ok=True)
    with open(p, 'w') as f:
        json.dump(obj, f, indent=2, ensure_ascii=False)
        f.write('\n')

BS = 'assets/frontier/blockstates/'
BM = 'assets/frontier/models/block/'
IM = 'assets/frontier/models/item/'

def tex(name): return 'frontier:block/' + name

# ---------- simple stone blocks ----------
for name in ['ancient_stone', 'ancient_stone_bricks', 'cracked_ancient_stone_bricks', 'chiseled_ancient_stone']:
    w(BM + name + '.json', {"parent": "minecraft:block/cube_all", "textures": {"all": tex(name)}})
    w(BS + name + '.json', {"variants": {"": {"model": f"frontier:block/{name}"}}})
    w(IM + name + '.json', {"parent": f"frontier:block/{name}"})

# ---------- rune block (lit + unlit) ----------
w(BM + 'ancient_rune_block.json', {"parent": "minecraft:block/cube_all", "textures": {"all": tex('ancient_rune')}})
w(BM + 'ancient_rune_block_lit.json', {"parent": "minecraft:block/cube_all", "textures": {"all": tex('ancient_rune_lit')}})
w(BS + 'ancient_rune_block.json', {"variants": {
    "lit=false": {"model": "frontier:block/ancient_rune_block"},
    "lit=true": {"model": "frontier:block/ancient_rune_block_lit"}}})
w(IM + 'ancient_rune_block.json', {"parent": "frontier:block/ancient_rune_block_lit"})

# ---------- secret door ----------
w(BM + 'ancient_secret_door_closed.json', {"parent": "minecraft:block/cube_all", "textures": {"all": tex('ancient_stone_bricks')}})
w(BM + 'ancient_secret_door_open.json', {
    "parent": "minecraft:block/block",
    "textures": {"frame": tex('ancient_stone_bricks'), "particle": tex('ancient_stone_bricks')},
    "elements": [
        {"from": [0, 0, 0], "to": [2, 16, 16], "faces": {k: {"texture": "#frame"} for k in ["north","south","east","west","up","down"]}},
        {"from": [14, 0, 0], "to": [16, 16, 16], "faces": {k: {"texture": "#frame"} for k in ["north","south","east","west","up","down"]}},
        {"from": [2, 14, 0], "to": [14, 16, 16], "faces": {k: {"texture": "#frame"} for k in ["north","south","east","west","up","down"]}}
    ]})
w(BS + 'ancient_secret_door.json', {"variants": {
    "facing=north,open=false": {"model": "frontier:block/ancient_secret_door_closed"},
    "facing=east,open=false": {"model": "frontier:block/ancient_secret_door_closed"},
    "facing=south,open=false": {"model": "frontier:block/ancient_secret_door_closed"},
    "facing=west,open=false": {"model": "frontier:block/ancient_secret_door_closed"},
    "facing=north,open=true": {"model": "frontier:block/ancient_secret_door_open"},
    "facing=east,open=true": {"model": "frontier:block/ancient_secret_door_open", "y": 90},
    "facing=south,open=true": {"model": "frontier:block/ancient_secret_door_open", "y": 180},
    "facing=west,open=true": {"model": "frontier:block/ancient_secret_door_open", "y": 270}}})
w(IM + 'ancient_secret_door.json', {"parent": "frontier:block/ancient_secret_door_closed"})

# ---------- master grindstone (BER in world; item model shows the machine) ----------
w(BM + 'master_grindstone.json', {"parent": "minecraft:block/block", "textures": {"particle": tex('ancient_stone')}})
w(BS + 'master_grindstone.json', {"variants": {
    "facing=north,lit=false": {"model": "frontier:block/master_grindstone"},
    "facing=north,lit=true": {"model": "frontier:block/master_grindstone"},
    "facing=east,lit=false": {"model": "frontier:block/master_grindstone", "y": 90},
    "facing=east,lit=true": {"model": "frontier:block/master_grindstone", "y": 90},
    "facing=south,lit=false": {"model": "frontier:block/master_grindstone", "y": 180},
    "facing=south,lit=true": {"model": "frontier:block/master_grindstone", "y": 180},
    "facing=west,lit=false": {"model": "frontier:block/master_grindstone", "y": 270},
    "facing=west,lit=true": {"model": "frontier:block/master_grindstone", "y": 270}}})
faces6 = {k: {"texture": "#stone"} for k in ["north","south","east","west","up","down"]}
faces6m = {k: {"texture": "#metal"} for k in ["north","south","east","west","up","down"]}
faces6w = {k: {"texture": "#wheel"} for k in ["north","south","east","west","up","down"]}
w(IM + 'master_grindstone.json', {
    "parent": "minecraft:block/block",
    "textures": {"stone": tex('ancient_stone'), "metal": tex('dark_metal'), "wheel": tex('grindstone_wheel'),
                 "particle": tex('ancient_stone')},
    "elements": [
        {"from": [2, 0, 2], "to": [14, 4, 14], "faces": faces6},
        {"from": [3, 4, 3], "to": [13, 6, 13], "faces": faces6},
        {"from": [3, 6, 6], "to": [5, 13, 10], "faces": faces6m},
        {"from": [11, 6, 6], "to": [13, 13, 10], "faces": faces6m},
        {"from": [5, 8, 6.5], "to": [11, 14, 9.5], "faces": faces6w},
        {"from": [5, 14, 5], "to": [11, 15, 11], "faces": faces6m}
    ],
    "display": {"gui": {"rotation": [30, 45, 0], "scale": [0.62, 0.62, 0.62]},
                "thirdperson_righthand": {"scale": [0.5, 0.5, 0.5]},
                "firstperson_righthand": {"scale": [0.5, 0.5, 0.5]}}})

# ---------- sarcophagus ----------
sarc_faces = {k: {"texture": "#sarc"} for k in ["north","south","east","west","up","down"]}
lid_faces = {k: {"texture": "#lid"} for k in ["north","south","east","west","up","down"]}
w(BM + 'sarcophagus.json', {
    "parent": "minecraft:block/block",
    "textures": {"sarc": tex('sarcophagus_side'), "lid": tex('sarcophagus_lid'), "particle": tex('sarcophagus_side')},
    "elements": [
        {"from": [1, 0, 1], "to": [15, 8, 15], "faces": sarc_faces},
        {"from": [2, 8, 2], "to": [14, 10, 14], "faces": lid_faces}
    ]})
w(BM + 'sarcophagus_open.json', {
    "parent": "minecraft:block/block",
    "textures": {"sarc": tex('sarcophagus_side'), "lid": tex('sarcophagus_lid'), "particle": tex('sarcophagus_side')},
    "elements": [
        {"from": [1, 0, 1], "to": [15, 8, 15], "faces": sarc_faces},
        {"from": [2, 8, 11], "to": [14, 10, 14], "faces": lid_faces}
    ]})
variants = {}
for opened, model in [("false", "frontier:block/sarcophagus"), ("true", "frontier:block/sarcophagus_open")]:
    for i, facing in enumerate(["south", "west", "north", "east"]):
        variants[f"facing={facing},opened={opened}"] = {"model": model, "y": i * 90}
w(BS + 'sarcophagus.json', {"variants": variants})
w(IM + 'sarcophagus.json', {"parent": "frontier:block/sarcophagus"})

# ---------- wood canal ----------
wood = {k: {"texture": "#wood"} for k in ["north","south","east","west","up","down"]}
w(BM + 'wood_canal.json', {
    "parent": "minecraft:block/block",
    "textures": {"wood": tex('canal_wood'), "particle": tex('canal_wood')},
    "elements": [
        {"from": [0, 0, 0], "to": [16, 5, 16], "faces": wood},
        {"from": [0, 5, 0], "to": [16, 11, 2], "faces": wood},
        {"from": [0, 5, 14], "to": [16, 11, 16], "faces": wood},
        {"from": [0, 5, 2], "to": [2, 11, 14], "faces": wood},
        {"from": [14, 5, 2], "to": [16, 11, 14], "faces": wood}
    ]})
w(BM + 'wood_canal_water.json', {
    "parent": "minecraft:block/block",
    "textures": {"water": "minecraft:block/water_still"},
    "elements": [
        {"from": [2, 6.5, 2], "to": [14, 6.5, 14],
         "faces": {"up": {"texture": "#water", "tintindex": 0}, "down": {"texture": "#water", "tintindex": 0}}}
    ]})
canal_variants = {}
for level in range(4):
    models = [{"model": "frontier:block/wood_canal"}]
    if level > 0:
        models.append({"model": "frontier:block/wood_canal_water"})
    canal_variants[f"water_level={level}"] = models
w(BS + 'wood_canal.json', {"multipart": [
    {"apply": {"model": "frontier:block/wood_canal"}},
    {"when": {"water_level": "1|2|3"}, "apply": {"model": "frontier:block/wood_canal_water"}}]})
w(IM + 'wood_canal.json', {"parent": "frontier:block/wood_canal"})

# ---------- stone pipe ----------
w(BM + 'stone_pipe.json', {
    "parent": "minecraft:block/block",
    "textures": {"pipe": tex('stone_pipe'), "particle": tex('stone_pipe')},
    "elements": [
        {"from": [5, 0, 5], "to": [11, 16, 11], "faces": {k: {"texture": "#pipe"} for k in ["north","south","east","west","up","down"]}},
        {"from": [4, 4, 4], "to": [12, 5, 12], "faces": {k: {"texture": "#pipe"} for k in ["north","south","east","west","up","down"]}},
        {"from": [4, 11, 4], "to": [12, 12, 12], "faces": {k: {"texture": "#pipe"} for k in ["north","south","east","west","up","down"]}}
    ]})
w(BS + 'stone_pipe.json', {"variants": {
    f"water_level={i}": {"model": "frontier:block/stone_pipe"} for i in range(4)}})
w(IM + 'stone_pipe.json', {"parent": "frontier:block/stone_pipe"})

# ---------- sprinkler (BER) ----------
w(BM + 'sprinkler.json', {"parent": "minecraft:block/block", "textures": {"particle": tex('sprinkler_base')}})
w(BS + 'sprinkler.json', {"variants": {"": {"model": "frontier:block/sprinkler"}}})
w(IM + 'sprinkler.json', {
    "parent": "minecraft:block/block",
    "textures": {"base": tex('sprinkler_base'), "particle": tex('sprinkler_base')},
    "elements": [
        {"from": [5, 0, 5], "to": [11, 8, 11], "faces": {k: {"texture": "#base"} for k in ["north","south","east","west","up","down"]}},
        {"from": [4, 8, 4], "to": [12, 10, 12], "faces": {k: {"texture": "#base"} for k in ["north","south","east","west","up","down"]}}
    ],
    "display": {"gui": {"rotation": [30, 45, 0], "scale": [0.9, 0.9, 0.9]}}})

# ---------- enriched farmland ----------
w(BM + 'enriched_farmland.json', {
    "parent": "minecraft:block/template_farmland",
    "textures": {"dirt": "minecraft:block/dirt", "top": tex('enriched_farmland_top'), "particle": "minecraft:block/dirt"}})
w(BS + 'enriched_farmland.json', {"variants": {
    f"fertility={f},moisture={m}": {"model": "frontier:block/enriched_farmland"}
    for f in range(8) for m in range(8)}})
w(IM + 'enriched_farmland.json', {"parent": "frontier:block/enriched_farmland"})

print("blockstates/models done")
