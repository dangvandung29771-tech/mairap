#!/usr/bin/env python3
"""Vehicle + mob entity atlases and particle sprites for FRONTIER."""
import os, random
from PIL import Image, ImageDraw

OUT = os.path.join(os.path.dirname(__file__), '..', 'src', 'main', 'resources', 'assets', 'frontier', 'textures')
rng = random.Random(777)

def save(img, rel):
    p = os.path.join(OUT, rel)
    os.makedirs(os.path.dirname(p), exist_ok=True)
    img.convert('RGBA').save(p)

def noisy(img, x0, y0, x1, y1, base, vary=8):
    px = img.load()
    for y in range(max(0, y0), min(img.height, y1 + 1)):
        for x in range(max(0, x0), min(img.width, x1 + 1)):
            j = rng.randint(-vary, vary)
            px[x, y] = (max(0, min(255, base[0] + j)), max(0, min(255, base[1] + j)),
                        max(0, min(255, base[2] + j)), 255)

# ============ LARGE RAFT (128x128) ============
img = Image.new('RGBA', (128, 128), (0, 0, 0, 0))
d = ImageDraw.Draw(img)
# deck planks (rows 0..36)
noisy(img, 0, 0, 95, 36, (124, 92, 56), 12)
for y in range(2, 36, 6):
    d.line([(0, y), (95, y)], fill=(92, 66, 38))
for _ in range(60):
    d.point((rng.randint(0, 95), rng.randint(0, 36)), fill=(150, 116, 72))
# chest (96,0..19)
noisy(img, 96, 0, 127, 11, (140, 104, 62), 8)
noisy(img, 96, 12, 127, 19, (110, 80, 46), 8)
d.line([(96, 11), (127, 11)], fill=(60, 44, 24))
d.rectangle([109, 4, 113, 8], outline=(216, 180, 96))
# furnace (96,20..40)
noisy(img, 96, 20, 127, 33, (96, 94, 98), 8)
d.rectangle([96, 34, 127, 40], fill=(40, 38, 40))
noisy(img, 98, 35, 125, 39, (220, 120, 40), 30)
# beams/posts/rails rows 58..73
noisy(img, 0, 58, 127, 73, (96, 70, 42), 10)
d.line([(0, 62), (127, 62)], fill=(70, 50, 30))
# rope rows 74..85
noisy(img, 0, 74, 31, 85, (188, 164, 118), 10)
for y in range(75, 85, 2):
    d.line([(0, y), (31, y)], fill=(148, 128, 90))
# mast + crossbar (64,58..83)
noisy(img, 64, 58, 79, 83, (110, 82, 50), 8)
d.line([(71, 58), (71, 83)], fill=(80, 58, 34))
# lantern (64,84..95) glowing amber
noisy(img, 64, 84, 79, 91, (255, 208, 110), 16)
noisy(img, 64, 92, 79, 95, (90, 70, 44), 6)
# oars rows 86..98
noisy(img, 0, 86, 39, 91, (116, 86, 52), 8)
noisy(img, 0, 92, 39, 98, (96, 70, 42), 8)
save(img, 'entity/large_raft.png')

# ============ POCKET AIRSHIP (128x128) ============
img = Image.new('RGBA', (128, 128), (0, 0, 0, 0))
d = ImageDraw.Draw(img)
# gondola rows 0..24
noisy(img, 0, 0, 95, 24, (116, 86, 52), 10)
for y in range(3, 24, 5):
    d.line([(0, y), (95, y)], fill=(84, 60, 34))
noisy(img, 0, 20, 95, 24, (92, 68, 40), 8)   # hull shadow
# copper trim rows 25..29
noisy(img, 0, 25, 127, 29, (186, 110, 62), 14)
for x in range(2, 126, 12):
    d.point((x, 27), fill=(238, 178, 110))
# prow + engine + propeller rows 48..55
noisy(img, 0, 48, 15, 55, (104, 78, 46), 8)      # prow wood
noisy(img, 16, 48, 45, 55, (60, 58, 62), 8)      # engine dark iron
d.rectangle([38, 49, 44, 54], fill=(44, 42, 46))
noisy(img, 46, 48, 63, 55, (150, 140, 130), 10)  # propeller metal
d.point((54, 51), fill=(60, 56, 54))
# ropes rows 56..63
d.rectangle([0, 56, 127, 63], fill=(0, 0, 0, 0))
noisy(img, 0, 56, 63, 63, (96, 80, 56), 8)
# balloon canvas rows 64..127
noisy(img, 0, 64, 127, 127, (198, 174, 134), 10)
for y in range(66, 127, 8):
    d.line([(0, y), (127, y)], fill=(168, 146, 110))
for x in range(8, 127, 16):
    d.line([(x, 64), (x, 127)], fill=(178, 156, 118))
# top ridge highlight (72,64..70)
d.line([(72, 65), (127, 65)], fill=(236, 220, 186))
save(img, 'entity/pocket_airship.png')

# ============ COPPER SHIELD SKELETON (80x80) ============
img = Image.new('RGBA', (80, 80), (0, 0, 0, 0))
d = ImageDraw.Draw(img)
BONE = (214, 208, 196)
noisy(img, 0, 0, 31, 15, BONE, 8)                    # head
px = img.load()
for x, y in [(10, 10), (13, 10), (18, 10), (21, 10)]:  # eye sockets (face region x8..15,y8..15)
    px[x, y] = (40, 38, 36)
px[11, 10] = (120, 200, 190); px[20, 10] = (120, 200, 190)  # faint glow
noisy(img, 32, 0, 67, 12, (146, 108, 68), 10)        # worn helmet
for _ in range(8):
    d.point((rng.randint(33, 66), rng.randint(1, 11)), fill=(96, 150, 130))  # verdigris
noisy(img, 16, 16, 39, 31, BONE, 8)                  # ribcage torso
for x in [18, 22, 26, 30]:
    d.line([(x, 18), (x, 28)], fill=(170, 164, 152))
noisy(img, 16, 32, 43, 43, (92, 74, 54), 8)          # leather armor plate
d.rectangle([17, 33, 42, 42], outline=(60, 48, 34))
noisy(img, 40, 16, 48, 20, (150, 112, 70), 8)        # pauldron
noisy(img, 40, 24, 47, 37, BONE, 8)                  # arms
noisy(img, 0, 16, 7, 29, BONE, 8)                    # legs
# shield: copper with verdigris (48,32..47) and wrapped region (0,48..63)
noisy(img, 48, 32, 79, 47, (176, 112, 62), 12)
noisy(img, 0, 48, 27, 63, (176, 112, 62), 12)
for _ in range(14):
    d.point((rng.randint(49, 78), rng.randint(33, 46)), fill=(92, 168, 148))
for _ in range(10):
    d.point((rng.randint(1, 26), rng.randint(49, 62)), fill=(92, 168, 148))
d.rectangle([49, 33, 78, 46], outline=(120, 74, 40))
d.rectangle([1, 49, 26, 62], outline=(120, 74, 40))
noisy(img, 52, 50, 59, 55, (216, 150, 84), 10)       # shield boss
save(img, 'entity/copper_shield_skeleton.png')

# ============ GOLD DIGGER ZOMBIE (80x80) ============
img = Image.new('RGBA', (80, 80), (0, 0, 0, 0))
d = ImageDraw.Draw(img)
SKIN = (112, 140, 92)
noisy(img, 0, 0, 31, 15, SKIN, 10)                   # head
px = img.load()
for x, y in [(10, 10), (13, 10), (18, 10), (21, 10)]:
    px[x, y] = (30, 36, 26)
px[19, 13] = (60, 74, 48); px[20, 13] = (60, 74, 48)  # grim mouth
noisy(img, 32, 0, 67, 12, (216, 172, 64), 12)        # hard hat
d.line([(32, 3), (67, 3)], fill=(170, 132, 44))
for _ in range(5):
    d.point((rng.randint(33, 66), rng.randint(4, 11)), fill=(240, 210, 110))
noisy(img, 16, 16, 39, 31, SKIN, 10)                 # torso
noisy(img, 16, 32, 43, 44, (86, 96, 120), 10)        # overalls
d.line([(22, 32), (22, 44)], fill=(60, 68, 88))
d.line([(34, 32), (34, 44)], fill=(60, 68, 88))
d.rectangle([27, 36, 30, 40], outline=(60, 68, 88))
noisy(img, 44, 32, 63, 44, (120, 90, 56), 10)        # prospector pack
noisy(img, 44, 44, 63, 49, (160, 140, 110), 8)       # bedroll
noisy(img, 40, 16, 47, 29, SKIN, 10)                 # arms
noisy(img, 0, 16, 7, 29, (90, 100, 74), 10)          # legs (muddy trousers)
# pickaxe: handle wood (0,32..48), hub + blades steel
noisy(img, 0, 32, 35, 48, (110, 82, 50), 8)
d.line([(17, 32), (17, 48)], fill=(80, 58, 34))
noisy(img, 0, 50, 7, 54, (150, 148, 150), 10)        # hub
noisy(img, 8, 50, 25, 53, (168, 166, 168), 10)       # blades
noisy(img, 8, 54, 25, 57, (128, 126, 130), 10)
d.point((9, 51), fill=(240, 238, 240)); d.point((24, 51), fill=(240, 238, 240))
save(img, 'entity/gold_digger_zombie.png')

# ============ PARTICLES ============
for i in range(8):
    img = Image.new('RGBA', (8, 8), (0, 0, 0, 0))
    px = img.load()
    a = max(0, 255 - i * 30)
    core = (255, 200 - i * 12, 60, a)
    mid = (255, 140 - i * 10, 30, a // 2)
    px[3, 3] = core; px[4, 3] = core; px[3, 4] = core; px[4, 4] = core
    for x, y in [(2, 3), (5, 4), (3, 2), (4, 5)]:
        px[x, y] = mid
    save(img, f'particle/forge_spark_{i}.png')

for i in range(4):
    img = Image.new('RGBA', (8, 8), (0, 0, 0, 0))
    px = img.load()
    a = max(0, 230 - i * 50)
    core = (150, 240, 235, a)
    mid = (90, 190, 185, a // 2)
    px[3, 3] = core; px[4, 4] = core
    for x, y in [(2, 2), (5, 3), (3, 5), (4, 2), (2, 4), (5, 5)]:
        px[x, y] = mid
    save(img, f'particle/rune_glow_{i}.png')

print("entities + particles ok")
