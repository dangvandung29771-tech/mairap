#!/usr/bin/env python3
"""Procedural pixel-art texture generator for FRONTIER. Deterministic (seeded)."""
import os, random, math
from PIL import Image, ImageDraw

OUT = os.path.join(os.path.dirname(__file__), '..', 'src', 'main', 'resources', 'assets', 'frontier', 'textures')
rng = random.Random(20260914)

def save(img, rel):
    p = os.path.join(OUT, rel)
    os.makedirs(os.path.dirname(p), exist_ok=True)
    img.convert('RGBA').save(p)

def noise_fill(img, base, vary=10, x0=0, y0=0, x1=None, y1=None, alpha=255):
    """Fill region with jittered color for a natural blocky feel."""
    x1 = img.width if x1 is None else x1
    y1 = img.height if y1 is None else y1
    px = img.load()
    for y in range(y0, y1):
        for x in range(x0, x1):
            j = rng.randint(-vary, vary)
            px[x, y] = (max(0, min(255, base[0] + j)), max(0, min(255, base[1] + j)),
                        max(0, min(255, base[2] + j)), alpha)

def border(img, x0, y0, x1, y1, light, dark):
    d = ImageDraw.Draw(img)
    d.rectangle([x0, y0, x1, y1], outline=light)
    d.rectangle([x0 + 1, y0 + 1, x1 - 1, y1 - 1], outline=dark)

def speckle(img, x0, y0, x1, y1, color, n, seed_step=1):
    px = img.load()
    for _ in range(n):
        x = rng.randint(x0, x1 - 1); y = rng.randint(y0, y1 - 1)
        px[x, y] = color

def px_rect(img, x, y, w, h, color):
    ImageDraw.Draw(img).rectangle([x, y, x + w - 1, y + h - 1], fill=color)

# ================= BLOCKS =================
GRAY = (72, 76, 86)
DARK = (46, 49, 58)
BLUEISH = (58, 62, 74)

def block_ancient_stone():
    img = Image.new('RGBA', (16, 16))
    noise_fill(img, GRAY, 12)
    px = img.load()
    # cracks and darker strata
    for _ in range(6):
        x = rng.randint(0, 15); y = rng.randint(0, 15)
        for _ in range(rng.randint(2, 5)):
            px[x, y] = DARK
            x = max(0, min(15, x + rng.choice([-1, 0, 1])))
            y = max(0, min(15, y + rng.choice([0, 1])))
    speckle(img, 0, 0, 16, 16, (96, 100, 112), 10)
    border(img, 0, 0, 15, 15, (88, 92, 104), DARK)
    return img

def block_bricks(cracked=False):
    img = block_ancient_stone()
    d = ImageDraw.Draw(img)
    px = img.load()
    for row in range(4):
        y = row * 4 + 3
        d.line([(0, y), (15, y)], fill=DARK)
        offset = 4 if row % 2 else 0
        for col in range(4):
            x = (offset + col * 8 + 3) % 16
            d.line([(x, row * 4), (x, y)], fill=DARK)
    if cracked:
        for _ in range(5):
            x = rng.randint(2, 13); y = rng.randint(2, 13)
            for _ in range(4):
                px[x, y] = (30, 32, 40)
                x = max(0, min(15, x + rng.choice([-1, 1])))
                y = max(0, min(15, y + rng.choice([0, 1])))
    return img

def block_chiseled():
    img = Image.new('RGBA', (16, 16))
    noise_fill(img, (64, 68, 80), 8)
    d = ImageDraw.Draw(img)
    border(img, 0, 0, 15, 15, (92, 96, 110), DARK)
    d.rectangle([3, 3, 12, 12], outline=DARK)
    d.rectangle([5, 5, 10, 10], outline=(92, 96, 110))
    d.point((7, 7), fill=(96, 100, 112)); d.point((8, 8), fill=(96, 100, 112))
    return img

RUNE_PIX = [(3,4),(4,3),(5,4),(6,5),(7,4),(8,3),(9,4),(10,5),(11,4),(12,3),
            (5,7),(6,8),(7,9),(8,10),(9,9),(10,8),(11,7),
            (6,11),(7,12),(8,12),(9,12),(10,11)]

def block_rune(lit):
    img = block_bricks()
    px = img.load()
    glow = (96, 235, 230) if lit else (38, 68, 70)
    soft = (60, 150, 148) if lit else (44, 60, 66)
    for (x, y) in RUNE_PIX:
        px[x, y] = glow
    if lit:
        for (x, y) in RUNE_PIX:
            for dx, dy in [(-1,0),(1,0),(0,-1),(0,1)]:
                if 0 <= x+dx < 16 and 0 <= y+dy < 16:
                    px[x+dx, y+dy] = soft
    return img

def block_dark_metal():
    img = Image.new('RGBA', (16, 16))
    noise_fill(img, (52, 50, 54), 7)
    d = ImageDraw.Draw(img)
    for y in range(0, 16, 4):
        d.line([(0, y), (15, y)], fill=(36, 34, 38))
    speckle(img, 0, 0, 16, 16, (76, 72, 78), 8)
    for x, y in [(2,2),(13,2),(2,13),(13,13),(8,6)]:
        d.point((x, y), fill=(88, 84, 90))
    return img

def block_grindstone_wheel():
    img = Image.new('RGBA', (16, 16))
    noise_fill(img, (84, 80, 78), 9)
    d = ImageDraw.Draw(img)
    for i in range(4):
        d.line([(0, i*4+1), (15, i*4+1)], fill=(60, 57, 56))
    speckle(img, 0, 0, 16, 16, (108, 102, 98), 10)
    border(img, 0, 0, 15, 15, (100, 96, 92), (48, 45, 44))
    return img

def block_canal_wood():
    img = Image.new('RGBA', (16, 16))
    noise_fill(img, (122, 90, 54), 12)
    d = ImageDraw.Draw(img)
    for x in [3, 7, 11]:
        d.line([(x, 0), (x, 15)], fill=(92, 66, 38))
    speckle(img, 0, 0, 16, 16, (150, 114, 70), 12)
    d.line([(0, 0), (15, 0)], fill=(146, 110, 66))
    return img

def block_stone_pipe():
    img = Image.new('RGBA', (16, 16))
    noise_fill(img, (108, 108, 112), 8)
    d = ImageDraw.Draw(img)
    d.line([(0, 4), (15, 4)], fill=(86, 86, 90))
    d.line([(0, 11), (15, 11)], fill=(86, 86, 90))
    speckle(img, 0, 0, 16, 16, (130, 130, 136), 8)
    border(img, 0, 0, 15, 15, (128, 128, 134), (78, 78, 82))
    return img

def block_sprinkler_base():
    img = Image.new('RGBA', (16, 16))
    noise_fill(img, (150, 106, 62), 10)  # wood base
    px_rect(img, 4, 2, 8, 6, (60, 58, 62))   # column metal
    px_rect(img, 3, 8, 10, 5, (184, 122, 66))  # brass head
    px_rect(img, 5, 9, 6, 2, (230, 170, 96))
    for x, y in [(2, 3), (13, 3), (2, 12), (13, 12)]:
        img.load()[x, y] = (210, 190, 140)
    return img

def block_enriched_farmland():
    img = Image.new('RGBA', (16, 16))
    noise_fill(img, (110, 78, 48), 10)
    d = ImageDraw.Draw(img)
    for y in [2, 5, 8, 11, 14]:
        d.line([(0, y), (15, y)], fill=(86, 60, 36))
    speckle(img, 0, 0, 16, 16, (64, 110, 44), 22)   # green flecks
    speckle(img, 0, 0, 16, 16, (142, 104, 62), 14)
    return img

def block_sarcophagus(lid):
    img = Image.new('RGBA', (16, 16))
    if lid:
        noise_fill(img, (96, 90, 104), 8)
        d = ImageDraw.Draw(img)
        border(img, 0, 0, 15, 15, (130, 124, 140), (58, 54, 66))
        d.rectangle([4, 3, 11, 12], outline=(200, 170, 90))
        d.line([(7, 5), (8, 5)], fill=(200, 170, 90))
        d.point((7, 8), fill=(200, 170, 90)); d.point((8, 8), fill=(200, 170, 90))
    else:
        noise_fill(img, (70, 66, 80), 8)
        d = ImageDraw.Draw(img)
        for y in [4, 11]:
            d.line([(0, y), (15, y)], fill=(48, 44, 56))
        d.rectangle([2, 6, 13, 9], outline=(200, 170, 90))
        speckle(img, 0, 0, 16, 16, (92, 86, 102), 10)
    return img

save(block_ancient_stone(), 'block/ancient_stone.png')
save(block_bricks(), 'block/ancient_stone_bricks.png')
save(block_bricks(cracked=True), 'block/cracked_ancient_stone_bricks.png')
save(block_chiseled(), 'block/chiseled_ancient_stone.png')
save(block_rune(False), 'block/ancient_rune.png')
save(block_rune(True), 'block/ancient_rune_lit.png')
save(block_dark_metal(), 'block/dark_metal.png')
save(block_grindstone_wheel(), 'block/grindstone_wheel.png')
save(block_canal_wood(), 'block/canal_wood.png')
save(block_stone_pipe(), 'block/stone_pipe.png')
save(block_sprinkler_base(), 'block/sprinkler_base.png')
save(block_enriched_farmland(), 'block/enriched_farmland_top.png')
save(block_sarcophagus(False), 'block/sarcophagus_side.png')
save(block_sarcophagus(True), 'block/sarcophagus_lid.png')
print("blocks ok")
