#!/usr/bin/env python3
"""Item textures for FRONTIER."""
import os, random, math
from PIL import Image, ImageDraw

OUT = os.path.join(os.path.dirname(__file__), '..', 'src', 'main', 'resources', 'assets', 'frontier', 'textures')
rng = random.Random(991)

def save(img, rel):
    p = os.path.join(OUT, rel)
    os.makedirs(os.path.dirname(p), exist_ok=True)
    img.convert('RGBA').save(p)

def item(): return Image.new('RGBA', (16, 16))

def whetstone(edge, body, glow=None):
    img = item(); d = ImageDraw.Draw(img)
    # angled grindstone slab with a bright edge
    d.polygon([(3, 11), (6, 4), (13, 4), (13, 11)], fill=body)
    d.polygon([(3, 11), (6, 4), (7, 4), (4, 11)], fill=edge)
    d.polygon([(13, 4), (13, 11), (12, 11), (12, 4)], fill=(40, 38, 40))
    for y in range(5, 11, 2):
        d.line([(7, y), (11, y)], fill=(0, 0, 0, 60))
    if glow:
        d.point((9, 6), fill=glow); d.point((10, 8), fill=glow)
    return img

def crystal(main, dark, sparkle):
    img = item(); d = ImageDraw.Draw(img)
    d.polygon([(8, 1), (11, 4), (11, 10), (8, 14), (5, 10), (5, 4)], fill=main)
    d.polygon([(8, 1), (11, 4), (11, 10), (8, 14)], fill=dark)
    d.line([(8, 1), (8, 14)], fill=sparkle)
    d.point((6, 4), fill=sparkle); d.point((7, 3), fill=sparkle)
    return img

save(whetstone((236, 236, 240), (104, 106, 116)), 'item/sharp_whetstone.png')
save(whetstone((150, 200, 200), (96, 128, 132)), 'item/balanced_whetstone.png')
save(whetstone((216, 160, 64), (88, 74, 60)), 'item/heavy_whetstone.png')
save(crystal((240, 224, 96), (176, 160, 60), (255, 250, 200)), 'item/precision_crystal.png')
save(crystal((224, 64, 64), (140, 30, 30), (255, 160, 150)), 'item/blood_crystal.png')
save(crystal((144, 64, 224), (80, 30, 130), (220, 180, 255)), 'item/void_crystal.png')

# ancient shard — angular dark-teal fragment
img = item(); d = ImageDraw.Draw(img)
d.polygon([(4, 12), (3, 7), (7, 2), (12, 5), (13, 11), (8, 14)], fill=(48, 74, 76))
d.polygon([(7, 2), (12, 5), (9, 8)], fill=(70, 108, 110))
d.line([(7, 2), (9, 8), (8, 14)], fill=(96, 190, 186))
save(img, 'item/ancient_shard.png')

# gold fragment
img = item(); d = ImageDraw.Draw(img)
d.polygon([(5, 10), (4, 6), (8, 3), (12, 6), (11, 11), (7, 13)], fill=(196, 152, 44))
d.polygon([(8, 3), (12, 6), (9, 8)], fill=(240, 208, 96))
d.point((6, 8), fill=(250, 232, 140))
save(img, 'item/gold_fragment.png')

# compost cake
img = item(); d = ImageDraw.Draw(img)
d.ellipse([2, 5, 13, 13], fill=(92, 64, 38))
d.ellipse([3, 4, 12, 11], fill=(120, 88, 50))
for x, y in [(5,6),(8,7),(10,8),(6,9),(9,10),(4,8)]:
    d.point((x, y), fill=(58, 92, 40))
save(img, 'item/compost_cake.png')

# climber's claw
img = item(); d = ImageDraw.Draw(img)
d.polygon([(3, 13), (5, 9), (8, 10), (6, 13)], fill=(150, 110, 70))
for cx, curve in [(6, -2), (9, 0), (12, 2)]:
    d.line([(5, 10), (cx, 8), (cx + curve // 2, 4), (cx + curve, 2)], fill=(228, 224, 216), width=1)
    d.point((cx + curve, 2), fill=(255, 255, 255))
save(img, 'item/climbers_claw.png')

# feather of grace
img = item(); d = ImageDraw.Draw(img)
pts = [(12, 2)]
for i in range(9):
    pts.append((12 - i, 2 + i))
for x, y in pts:
    d.line([(x, y), (x - 3, y + 1)], fill=(238, 240, 246))
d.line([(12, 2), (3, 11)], fill=(200, 204, 214))
d.point((3, 12), fill=(180, 140, 90))
d.point((7, 5), fill=(240, 200, 120))
save(img, 'item/feather_of_grace.png')

# infernal ring
img = item(); d = ImageDraw.Draw(img)
d.ellipse([3, 4, 12, 13], outline=(216, 160, 60), width=2)
d.rectangle([6, 2, 9, 5], fill=(228, 60, 40))
d.point((7, 3), fill=(255, 170, 120))
save(img, 'item/infernal_ring.png')

# explorer compass
img = item(); d = ImageDraw.Draw(img)
d.ellipse([2, 2, 13, 13], fill=(70, 66, 60))
d.ellipse([3, 3, 12, 12], fill=(222, 214, 190))
d.polygon([(7, 4), (9, 7), (7, 12), (6, 7)], fill=(200, 70, 50))
d.point((7, 7), fill=(50, 46, 40))
for x, y in [(7, 3), (7, 12), (3, 7), (12, 7)]:
    d.point((x, y), fill=(90, 84, 70))
save(img, 'item/explorer_compass.png')

# stoneheart
img = item(); d = ImageDraw.Draw(img)
d.polygon([(8, 13), (3, 8), (3, 5), (5, 3), (8, 5), (11, 3), (13, 5), (13, 8)], fill=(118, 120, 128))
d.polygon([(8, 5), (11, 3), (13, 5), (13, 8), (8, 13)], fill=(96, 98, 106))
d.point((5, 5), fill=(160, 162, 170))
d.line([(4, 7), (6, 9)], fill=(60, 62, 68))
save(img, 'item/stoneheart.png')

# blood pendant
img = item(); d = ImageDraw.Draw(img)
d.arc([4, 1, 11, 8], 200, 340, fill=(216, 190, 120), width=1)
d.polygon([(8, 7), (10, 9), (8, 13), (6, 9)], fill=(210, 40, 44))
d.point((7, 9), fill=(255, 140, 130))
save(img, 'item/blood_pendant.png')

# large raft icon
img = item(); d = ImageDraw.Draw(img)
for y in range(7, 13):
    d.line([(2, y), (13, y)], fill=(122, 90, 54) if y % 2 else (104, 76, 44))
for x in [2, 13]:
    d.line([(x, 7), (x, 12)], fill=(76, 56, 32))
d.line([(8, 2), (8, 8)], fill=(90, 66, 38))
d.polygon([(8, 2), (12, 4), (8, 6)], fill=(224, 218, 200))
save(img, 'item/large_raft.png')

# pocket airship icon
img = item(); d = ImageDraw.Draw(img)
d.ellipse([2, 2, 13, 8], fill=(200, 176, 136))
d.line([(2, 5), (13, 5)], fill=(160, 138, 104))
d.rectangle([5, 8, 10, 12], fill=(122, 90, 54))
d.rectangle([5, 8, 10, 8], fill=(150, 114, 70))
d.line([(12, 10), (14, 8)], fill=(80, 76, 74))
d.line([(12, 8), (14, 12)], fill=(80, 76, 74))
save(img, 'item/pocket_airship.png')

# spawn eggs (template tinting uses layer0+layer1 masks in vanilla; provide generic)
for name, c1, c2 in [('copper_shield_skeleton', (201, 192, 176), (196, 106, 58)),
                     ('gold_digger_zombie', (94, 122, 74), (216, 160, 64))]:
    img = item(); d = ImageDraw.Draw(img)
    d.ellipse([2, 3, 13, 14], fill=c1)
    d.ellipse([4, 5, 11, 12], fill=c2)
    d.point((5, 6), fill=(255, 255, 255))
    save(img, f'item/{name}_spawn_egg.png')

print("items ok")
