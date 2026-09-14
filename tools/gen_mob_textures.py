#!/usr/bin/env python3
"""High-quality hand-shaded mob textures for the two Frontier mobs.

Rewrites entity/copper_shield_skeleton.png and entity/gold_digger_zombie.png
(80x80) with layered shading: gradients, cracks, verdigris, glowing eyes and
worn equipment, while keeping the exact UV regions used by the models.
"""
import os
import random
from PIL import Image, ImageDraw

OUT = os.path.join(os.path.dirname(__file__), '..', 'src', 'main', 'resources', 'assets', 'frontier', 'textures')
rng = random.Random(1460197)


def save(img, rel):
    p = os.path.join(OUT, rel)
    os.makedirs(os.path.dirname(p), exist_ok=True)
    img.save(p)


def px(img):
    return img.load()


def fill(img, x0, y0, x1, y1, color):
    ImageDraw.Draw(img).rectangle([x0, y0, x1, y1], fill=color + (255,))


def noise(img, x0, y0, x1, y1, color, amount=8, alpha=255):
    p = px(img)
    for y in range(y0, y1 + 1):
        for x in range(x0, x1 + 1):
            d = rng.randint(-amount, amount)
            p[x, y] = (max(0, min(255, color[0] + d)), max(0, min(255, color[1] + d)),
                       max(0, min(255, color[2] + d)), alpha)


def vgrad(img, x0, y0, x1, y1, top, bot):
    """Vertical gradient fill with light noise."""
    p = px(img)
    h = max(1, y1 - y0)
    for y in range(y0, y1 + 1):
        t = (y - y0) / h
        c = tuple(int(top[i] + (bot[i] - top[i]) * t) for i in range(3))
        d = rng.randint(-5, 5)
        c = tuple(max(0, min(255, v + d)) for v in c)
        for x in range(x0, x1 + 1):
            p[x, y] = c + (255,)


def outline(img, x0, y0, x1, y1, color):
    ImageDraw.Draw(img).rectangle([x0, y0, x1, y1], outline=color + (255,))


def dots(img, x0, y0, x1, y1, color, n):
    d = ImageDraw.Draw(img)
    for _ in range(n):
        d.point((rng.randint(x0, x1), rng.randint(y0, y1)), fill=color + (255,))


# ============================================================= SKELETON ====
img = Image.new('RGBA', (80, 80), (0, 0, 0, 0))
d = ImageDraw.Draw(img)
BONE = (216, 210, 197)
BONE_HI = (238, 233, 222)
BONE_LO = (168, 160, 146)

# --- head ---
vgrad(img, 8, 8, 15, 15, BONE_HI, BONE_LO)          # face (front)
vgrad(img, 0, 8, 7, 15, BONE, BONE_LO)              # right side
vgrad(img, 16, 8, 23, 15, BONE, BONE_LO)            # left side
vgrad(img, 24, 8, 31, 15, BONE, (150, 143, 130))    # back
vgrad(img, 8, 0, 15, 7, BONE_HI, BONE)              # top
fill(img, 16, 0, 23, 7, (150, 143, 130))            # bottom
# eye sockets 2x2 with cyan glow pupils
fill(img, 9, 10, 10, 12, (24, 22, 20)); fill(img, 13, 10, 14, 12, (24, 22, 20))
px(img)[9, 11] = (110, 224, 208); px(img)[14, 11] = (110, 224, 208)
px(img)[10, 10] = (60, 120, 112); px(img)[13, 10] = (60, 120, 112)
# nose hole + jaw cracks
px(img)[11, 12] = (96, 90, 80); px(img)[12, 12] = (96, 90, 80)
for x in (9, 11, 13):
    px(img)[x, 14] = (140, 133, 120)                # teeth line
dots(img, 8, 8, 15, 9, (190, 184, 170), 6)
# hairline cracks
px(img)[15, 9] = BONE_LO; px(img)[15, 10] = BONE_LO; px(img)[8, 13] = BONE_LO

# --- worn copper helmet (band 32..67, 9..12; top 41..49, 0..8) ---
vgrad(img, 41, 0, 49, 8, (196, 128, 74), (140, 88, 50))
vgrad(img, 32, 9, 67, 12, (186, 120, 68), (126, 78, 44))
d.line([(32, 9), (67, 9)], fill=(226, 164, 102))    # rim highlight
dots(img, 33, 10, 66, 12, (96, 168, 146), 16)       # verdigris
dots(img, 33, 10, 66, 12, (70, 128, 110), 8)
for x in (36, 44, 52, 60):                           # rivets
    px(img)[x, 11] = (240, 200, 140)
# dent
px(img)[57, 10] = (110, 66, 38); px(img)[58, 11] = (110, 66, 38)

# --- ribcage torso ---
vgrad(img, 20, 20, 27, 31, (206, 200, 187), (160, 153, 140))   # front
vgrad(img, 16, 20, 19, 31, BONE, BONE_LO)
vgrad(img, 28, 20, 31, 31, BONE, BONE_LO)
vgrad(img, 32, 20, 39, 31, (180, 173, 160), (146, 139, 126))   # back
vgrad(img, 20, 16, 27, 19, BONE_HI, BONE)
fill(img, 16, 32, 23, 36, (150, 143, 130))
# spine + rib shading
d.line([(23, 20), (24, 20), (23, 31), (24, 31)], fill=(150, 143, 130))
for y in (22, 25, 28):
    d.line([(21, y), (22, y)], fill=(178, 171, 158)); d.line([(25, y), (26, y)], fill=(178, 171, 158))
# collarbone highlight
d.line([(20, 20), (27, 20)], fill=BONE_HI)

# --- leather armor plate (band 16..43, 37..43; top 21..29, 32..36) ---
vgrad(img, 21, 32, 29, 36, (116, 92, 66), (92, 72, 50))
vgrad(img, 16, 37, 43, 43, (104, 82, 58), (76, 58, 40))
outline(img, 16, 37, 43, 43, (56, 44, 30))
d.line([(17, 38), (42, 38)], fill=(140, 112, 82))   # top stitch
for x in (20, 27, 34, 40):                           # studs
    px(img)[x, 41] = (206, 168, 108)
# worn scratches
dots(img, 18, 39, 41, 42, (132, 106, 78), 6)

# --- pauldron ---
vgrad(img, 40, 17, 49, 20, (176, 112, 62), (120, 74, 40))
d.line([(40, 17), (49, 17)], fill=(226, 164, 102))
dots(img, 41, 18, 48, 20, (96, 168, 146), 4)

# --- arms & legs (bone, joints highlighted) ---
vgrad(img, 42, 26, 43, 37, BONE_HI, BONE_LO)
d.line([(40, 26), (41, 37)], fill=BONE)              # left column shades
d.line([(44, 26), (47, 37)], fill=BONE_LO)
for y in (27, 31, 35):                               # joints
    px(img)[42, y] = BONE_HI; px(img)[43, y] = BONE_HI
vgrad(img, 2, 18, 3, 29, BONE_HI, BONE_LO)
d.line([(0, 18), (1, 29)], fill=BONE)
d.line([(4, 18), (7, 29)], fill=BONE_LO)
for y in (19, 23, 27):
    px(img)[2, y] = BONE_HI

# --- shield: copper face with verdigris streaks, rivets, boss ---
vgrad(img, 50, 34, 61, 49, (198, 130, 74), (132, 82, 46))     # front face
vgrad(img, 48, 34, 49, 49, (160, 104, 58), (120, 74, 40))     # edge L
vgrad(img, 62, 34, 63, 49, (160, 104, 58), (120, 74, 40))     # edge R
vgrad(img, 64, 34, 75, 49, (146, 94, 52), (108, 66, 38))      # back
vgrad(img, 50, 32, 61, 33, (226, 164, 102), (186, 120, 68))   # top edge
outline(img, 50, 34, 61, 49, (96, 60, 32))
# verdigris streaks dripping from the rim
for x in (51, 54, 58, 60):
    h = rng.randint(3, 8)
    for i in range(h):
        px(img)[x, 35 + i] = (92, 168, 146) if i % 2 == 0 else (70, 128, 110)
dots(img, 50, 40, 61, 48, (96, 168, 146), 10)
# corner rivets
for (x, y) in [(50, 34), (61, 34), (50, 49), (61, 49)]:
    px(img)[x, y] = (240, 200, 140)
# battle dents
px(img)[56, 44] = (104, 62, 34); px(img)[57, 44] = (104, 62, 34); px(img)[57, 45] = (104, 62, 34)
# boss
vgrad(img, 54, 52, 57, 57, (226, 164, 102), (140, 88, 50))
px(img)[54, 52] = (250, 210, 150); px(img)[55, 52] = (250, 210, 150)
outline(img, 54, 52, 57, 57, (96, 60, 32))
save(img, 'entity/copper_shield_skeleton.png')

# ============================================================= ZOMBIE =====
img = Image.new('RGBA', (80, 80), (0, 0, 0, 0))
d = ImageDraw.Draw(img)
SKIN = (104, 132, 84)
SKIN_HI = (132, 160, 108)
SKIN_LO = (72, 96, 60)

# --- head ---
vgrad(img, 8, 8, 15, 15, SKIN_HI, SKIN_LO)
vgrad(img, 0, 8, 7, 15, SKIN, SKIN_LO)
vgrad(img, 16, 8, 23, 15, SKIN, SKIN_LO)
vgrad(img, 24, 8, 31, 15, SKIN, (60, 82, 50))
vgrad(img, 8, 0, 15, 7, SKIN_HI, SKIN)
fill(img, 16, 0, 23, 7, (60, 82, 50))
# amber glowing eyes under the hat brim
fill(img, 9, 10, 10, 11, (20, 24, 16)); fill(img, 13, 10, 14, 11, (20, 24, 16))
px(img)[9, 10] = (248, 190, 70); px(img)[14, 10] = (248, 190, 70)
# grim mouth + rot patches
for x in (10, 12, 14):
    px(img)[x, 13] = (44, 56, 36)
dots(img, 8, 8, 15, 15, (84, 108, 68), 8)
px(img)[15, 12] = SKIN_LO; px(img)[8, 11] = SKIN_LO

# --- gold hard hat ---
vgrad(img, 41, 0, 49, 8, (236, 188, 84), (172, 130, 44))
vgrad(img, 32, 9, 67, 12, (226, 178, 74), (158, 118, 40))
d.line([(32, 9), (67, 9)], fill=(250, 214, 120))    # brim highlight
d.line([(44, 1), (46, 8)], fill=(250, 214, 120))    # crown ridge
dots(img, 33, 10, 66, 12, (140, 104, 36), 8)        # grime
# dents + glints
px(img)[58, 10] = (120, 88, 28); px(img)[59, 11] = (120, 88, 28)
px(img)[37, 10] = (255, 236, 170); px(img)[63, 11] = (255, 236, 170)

# --- torso (skin above overalls bib) ---
vgrad(img, 20, 20, 27, 31, SKIN_HI, SKIN_LO)
vgrad(img, 16, 20, 19, 31, SKIN, SKIN_LO)
vgrad(img, 28, 20, 31, 31, SKIN, SKIN_LO)
vgrad(img, 32, 20, 39, 31, (88, 112, 72), (64, 84, 52))
vgrad(img, 20, 16, 27, 19, SKIN_HI, SKIN)
# --- denim overalls ---
vgrad(img, 21, 32, 29, 36, (92, 104, 132), (66, 76, 100))     # bib top
vgrad(img, 16, 37, 43, 48, (80, 92, 120), (56, 64, 86))       # body
outline(img, 16, 37, 43, 48, (40, 46, 64))
# straps + buckles
d.line([(22, 37), (22, 40)], fill=(52, 60, 82)); d.line([(36, 37), (36, 40)], fill=(52, 60, 82))
px(img)[22, 40] = (236, 188, 84); px(img)[36, 40] = (236, 188, 84)
# chest pocket with a gold nugget peeking
outline(img, 27, 41, 31, 45, (40, 46, 64))
px(img)[29, 41] = (250, 210, 110)
# patches + stitching
d.line([(18, 44), (20, 44)], fill=(120, 132, 160)); dots(img, 17, 38, 44, 47, (98, 110, 138), 6)

# --- prospector pack + bedroll ---
vgrad(img, 47, 32, 53, 34, (150, 116, 72), (120, 90, 56))
vgrad(img, 44, 35, 63, 43, (136, 104, 64), (100, 76, 46))
outline(img, 44, 35, 63, 43, (72, 54, 32))
d.line([(49, 35), (49, 43)], fill=(84, 62, 38)); d.line([(58, 35), (58, 43)], fill=(84, 62, 38))  # straps
vgrad(img, 47, 44, 53, 46, (186, 164, 128), (150, 130, 100))
vgrad(img, 44, 47, 63, 49, (172, 150, 116), (136, 116, 88))
d.line([(44, 47), (63, 47)], fill=(206, 186, 150))

# --- arms (torn sleeves) ---
vgrad(img, 42, 16, 43, 21, SKIN_HI, SKIN_LO)
vgrad(img, 42, 22, 43, 29, (88, 100, 128), (60, 68, 92))      # sleeve remnant
d.line([(40, 16), (41, 29)], fill=SKIN); d.line([(44, 16), (47, 29)], fill=SKIN_LO)
px(img)[42, 21] = (52, 60, 82); px(img)[43, 21] = (52, 60, 82)

# --- muddy trousers ---
vgrad(img, 2, 18, 3, 29, (104, 96, 72), (72, 66, 48))
d.line([(0, 18), (1, 29)], fill=(92, 84, 62)); d.line([(4, 18), (7, 29)], fill=(60, 54, 40))
dots(img, 0, 26, 7, 29, (58, 50, 36), 8)                       # mud at the cuffs

# --- pickaxe: wooden haft + steel head with gold glints ---
vgrad(img, 0, 48, 33, 49, (126, 94, 56), (96, 70, 40))
for x in (6, 14, 22, 30):
    px(img)[x, 48] = (84, 60, 34)
vgrad(img, 16, 32, 17, 47, (126, 94, 56), (96, 70, 40))
vgrad(img, 2, 52, 5, 55, (176, 174, 178), (120, 118, 124))    # hub
vgrad(img, 8, 50, 25, 53, (196, 194, 198), (140, 138, 144))   # blades top
vgrad(img, 8, 54, 25, 57, (150, 148, 152), (104, 102, 108))   # blades shade
d.line([(8, 50), (25, 50)], fill=(235, 233, 237))             # sharp edge
px(img)[9, 51] = (255, 244, 180); px(img)[24, 51] = (255, 244, 180)  # gold glints
save(img, 'entity/gold_digger_zombie.png')
print("mob textures regenerated")
