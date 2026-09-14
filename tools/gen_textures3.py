#!/usr/bin/env python3
"""GUI atlases, entity textures and particle sprites for FRONTIER."""
import os, random, math
from PIL import Image, ImageDraw

OUT = os.path.join(os.path.dirname(__file__), '..', 'src', 'main', 'resources', 'assets', 'frontier', 'textures')
rng = random.Random(4242)

def save(img, rel):
    p = os.path.join(OUT, rel)
    os.makedirs(os.path.dirname(p), exist_ok=True)
    img.convert('RGBA').save(p)

def noisy(d, img, x0, y0, x1, y1, base, vary=8):
    px = img.load()
    for y in range(y0, y1 + 1):
        for x in range(x0, x1 + 1):
            j = rng.randint(-vary, vary)
            px[x, y] = (max(0, min(255, base[0] + j)), max(0, min(255, base[1] + j)),
                        max(0, min(255, base[2] + j)), 255)

def mc_frame(img, x0, y0, x1, y1, base=(64, 52, 40)):
    """Minecraft-container style frame: outer dark, inner bevel."""
    d = ImageDraw.Draw(img)
    d.rectangle([x0, y0, x1, y1], fill=(24, 20, 16))
    noisy(d, img, x0 + 1, y0 + 1, x1 - 1, y1 - 1, base)
    d.line([(x0 + 1, y0 + 1), (x1 - 1, y0 + 1)], fill=(min(255, base[0] + 46), min(255, base[1] + 46), min(255, base[2] + 46)))
    d.line([(x0 + 1, y0 + 1), (x0 + 1, y1 - 1)], fill=(min(255, base[0] + 30), min(255, base[1] + 30), min(255, base[2] + 30)))
    d.line([(x0 + 1, y1 - 1), (x1 - 1, y1 - 1)], fill=(max(0, base[0] - 26), max(0, base[1] - 26), max(0, base[2] - 26)))
    d.line([(x1 - 1, y0 + 1), (x1 - 1, y1 - 1)], fill=(max(0, base[0] - 26), max(0, base[1] - 26), max(0, base[2] - 26)))

def slot(img, x, y):
    d = ImageDraw.Draw(img)
    d.rectangle([x - 1, y - 1, x + 16, y + 16], fill=(30, 26, 22))
    d.rectangle([x, y, x + 15, y + 15], fill=(120, 110, 96))
    d.line([(x, y), (x + 15, y)], fill=(64, 56, 48))
    d.line([(x, y), (x, y + 15)], fill=(64, 56, 48))
    d.line([(x + 15, y), (x + 15, y + 15)], fill=(190, 180, 160))
    d.line([(x, y + 15), (x + 15, y + 15)], fill=(190, 180, 160))

def parchment(img, x0, y0, x1, y1):
    d = ImageDraw.Draw(img)
    noisy(d, img, x0, y0, x1, y1, (216, 198, 158), 6)
    d.rectangle([x0, y0, x1, y1], outline=(110, 92, 62))
    d.line([(x0 + 1, y0 + 1), (x1 - 1, y0 + 1)], fill=(236, 222, 186))
    d.line([(x0 + 1, y1 - 1), (x1 - 1, y1 - 1)], fill=(160, 140, 104))
    # corner nails
    for cx, cy in [(x0 + 2, y0 + 2), (x1 - 2, y0 + 2), (x0 + 2, y1 - 2), (x1 - 2, y1 - 2)]:
        d.point((cx, cy), fill=(86, 70, 46))

# ============ MASTER GRINDSTONE GUI (256x256) ============
img = Image.new('RGBA', (256, 256), (0, 0, 0, 0))
mc_frame(img, 0, 0, 175, 249, base=(56, 54, 58))          # dark stone body
d = ImageDraw.Draw(img)
noisy(d, img, 4, 4, 171, 21, (44, 42, 48), 5)              # title bar
# slot frames
slot(img, 79, 25)
slot(img, 52, 60)
slot(img, 106, 60)
# parchment comparison panel
parchment(img, 10, 90, 166, 142)
# decorative rivets along frame
for x in range(10, 170, 20):
    d.point((x, 2), fill=(96, 92, 100)); d.point((x, 247), fill=(96, 92, 100))
# FORGE button states at (176, 0), (176, 20), (176, 40), size 60x20
for i, (face, edge_l, edge_d, text_band) in enumerate([
        ((74, 68, 62), (110, 104, 96), (34, 30, 28), (60, 54, 50)),     # idle
        ((58, 54, 50), (90, 84, 78), (28, 26, 24), (50, 46, 44)),       # disabled
        ((94, 82, 58), (150, 128, 84), (40, 34, 24), (80, 68, 46))]):   # hover
    x0, y0 = 176, i * 20
    d.rectangle([x0, y0, x0 + 59, y0 + 19], fill=edge_l)
    noisy(d, img, x0 + 1, y0 + 1, x0 + 58, y0 + 18, face, 5)
    d.rectangle([x0 + 2, y0 + 2, x0 + 57, y0 + 17], outline=edge_d)
    d.line([(x0 + 3, y0 + 3), (x0 + 56, y0 + 3)], fill=(min(255, face[0] + 30),) * 3 + (255,))
    # gold studs
    d.point((x0 + 4, y0 + 4), fill=(230, 199, 119)); d.point((x0 + 55, y0 + 4), fill=(230, 199, 119))
    d.point((x0 + 4, y0 + 15), fill=(230, 199, 119)); d.point((x0 + 55, y0 + 15), fill=(230, 199, 119))
save(img, 'gui/master_grindstone.png')

# ============ RELIC POUCH GUI (256x256, container 176x184) ============
img = Image.new('RGBA', (256, 256), (0, 0, 0, 0))
mc_frame(img, 0, 0, 175, 183, base=(70, 56, 40))           # dark wood body
d = ImageDraw.Draw(img)
noisy(d, img, 3, 3, 172, 18, (58, 46, 32), 5)
# portrait recess
d.rectangle([10, 24, 46, 86], fill=(30, 26, 20))
d.rectangle([11, 25, 45, 85], outline=(96, 80, 56))
# two relic sockets (gold-rimmed) side by side, clear of the parchment list
for sx in [60, 100]:
    slot(img, sx, 30)
    d.rectangle([sx - 2, 28, sx + 19, 49], outline=(184, 148, 74))
    d.point((sx - 2, 28), fill=(240, 214, 140)); d.point((sx + 19, 28), fill=(240, 214, 140))
    d.point((sx - 2, 49), fill=(240, 214, 140)); d.point((sx + 19, 49), fill=(240, 214, 140))
# parchment strip (active relics list)
parchment(img, 58, 62, 168, 96)
save(img, 'gui/relic_pouch.png')

# ============ AIRSHIP HUD (128x64) ============
img = Image.new('RGBA', (128, 64), (0, 0, 0, 0))
d = ImageDraw.Draw(img)
# panel: dark bronze plate with riveted edge (92x54 at 0,0)
d.rectangle([0, 0, 91, 53], fill=(38, 34, 30))
noisy(d, img, 1, 1, 90, 52, (52, 46, 40), 4)
d.rectangle([0, 0, 91, 53], outline=(120, 100, 60))
d.line([(1, 1), (90, 1)], fill=(160, 140, 96))
# fuel gauge trough at (30,8)-(66,13)
d.rectangle([29, 7, 67, 13], fill=(24, 22, 20))
d.rectangle([29, 7, 67, 13], outline=(96, 84, 60))
# fuel fill strip (36px wide) at atlas (0,54)
d.rectangle([0, 54, 36, 59], fill=(214, 138, 50))
d.line([(0, 54), (36, 54)], fill=(240, 180, 90))
# engine lamp icons at (92,54) off, (100,54) on — 8x8
d.rectangle([92, 54, 99, 61], fill=(60, 56, 52))
d.ellipse([93, 55, 98, 60], fill=(120, 60, 50))
d.rectangle([100, 54, 107, 61], fill=(60, 56, 52))
d.ellipse([101, 55, 106, 60], fill=(150, 230, 150))
for x, y in [(4, 4), (87, 4), (4, 49), (87, 49)]:
    d.point((x, y), fill=(150, 130, 90))
save(img, 'gui/airship_hud.png')

# ============ GRINDSTONE ENTITY ATLAS (64x64) ============
img = Image.new('RGBA', (64, 64), (0, 0, 0, 0))
d = ImageDraw.Draw(img)
noisy(d, img, 0, 0, 63, 35, (78, 76, 84), 8)      # stone regions (base/tier)
noisy(d, img, 0, 36, 35, 63, (48, 46, 52), 6)     # dark metal frames
noisy(d, img, 36, 20, 63, 35, (92, 86, 82), 8)    # wheel hub/segments
# rune glints in the plinth area
for x, y in [(8, 10), (12, 12), (16, 10), (20, 12), (24, 10)]:
    d.point((x, y), fill=(96, 220, 214))
for x, y in [(6, 18), (26, 18)]:
    d.point((x, y), fill=(60, 150, 146))
# wheel grit
for _ in range(40):
    d.point((rng.randint(36, 63), rng.randint(20, 35)), fill=(60, 56, 54))
save(img, 'entity/master_grindstone.png')

# ============ SPRINKLER ENTITY ATLAS (32x32) ============
img = Image.new('RGBA', (32, 32), (0, 0, 0, 0))
d = ImageDraw.Draw(img)
noisy(d, img, 0, 0, 31, 13, (130, 96, 58), 10)    # wooden column
noisy(d, img, 0, 14, 31, 31, (184, 122, 66), 10)  # brass head
d.rectangle([0, 24, 31, 31], outline=(120, 76, 36))
for _ in range(10):
    d.point((rng.randint(0, 31), rng.randint(14, 31)), fill=(230, 170, 96))
save(img, 'entity/sprinkler.png')

print("gui + block-entity atlases ok")
