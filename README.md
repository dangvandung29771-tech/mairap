# FRONTIER — Vanilla+ Expansion

A polished, cohesive Vanilla+ expansion for **Minecraft 1.21.1 (NeoForge)**.
Everything in FRONTIER is built to feel like something Mojang *could* ship:
blocky geometry, readable silhouettes, restrained VFX, simple-but-deep
mechanics, and Minecraft-faithful UIs.

> Explore → Discover → Collect → Forge → Customize → Travel → Explore further.

---

## Features

### ⚒ Master Grindstone (weapon forging)
- New workstation with a fully custom 3D model: stone plinth, dark-metal frame,
  segmented grinding wheel that idles slowly and spins up while forging, sparks and smoke.
- Custom GUI (dark stone frame + parchment panel) with live **BEFORE → AFTER**
  stat comparison (Damage / Crit / Reach / Durability), slot hover glow, tooltips
  and a styled **FORGE** button. Forging is server-authoritative (payload-validated).
- **6 forging materials** with diminishing progression and hard caps
  (max level III per property, max **2 properties** per weapon):

  | Material            | Grants |
  |---------------------|--------|
  | Sharp Whetstone     | damage, crit chance |
  | Balanced Whetstone  | attack speed, reach |
  | Heavy Whetstone     | knockback, damage |
  | Precision Crystal   | crit chance, crit damage, opening strike |
  | Blood Crystal       | damage bonus below 50% HP |
  | Void Crystal        | Void Mark burst every 4th hit |

- Forged weapons read in tooltips (`Forged: ◆ Precision Rune II …`), emit
  restrained per-property particles, and modify real attributes
  (attack damage/speed/knockback/entity interaction range) plus a durability bonus.

### 🏛 Ancient Crypts (4 hand-designed dungeons)
Generate below the deepslate layers (y −56 … −13), spread across the overworld:
- **Fallen Barracks** — bunk rows, collapsed rubble, guardian spawners, rune-hidden officer's alcove.
- **Forgotten Mine** — timber frames, rails, gold seams guarded by buried TNT, digger's camp.
- **Ritual Crypt** — rune-pillar circle, soul-flame altar, rune-sealed reliquary vault under the floor.
- **Royal Tomb** — pillared processional hall, lootable **Sarcophagus** that wakes guardians, rune-warded treasury.

Custom masonry (ancient stone/bricks/chiseled), glowing runes with particle dust,
secret doors opened by lighting adjacent runes, traps, hidden rooms, and
crypt-exclusive loot — including **Ancient Shards**, the mod's progression currency.

### 👹 Custom enemies (custom models + keyframe animations)
- **Copper Shield Skeleton** — blocks ~65% of *frontal* attacks with its corroded
  copper shield (flank it!). Idle/attack/block/hit keyframe animations with
  anticipation → impact → follow-through.
- **Gold Digger Zombie** — relentless melee, periodically excavates soft blocks
  with its pickaxe and drops Gold Fragments / Ancient Shards.

### 🧿 Relics (2-slot relic pouch, Curios-compatible slot design)
Open the pouch with **R** (configurable): player portrait, gilded sockets,
active-effect parchment panel.
- **Climber's Claw** — brief wall-climbing charge with cooldown
- **Feather of Grace** — softens falls
- **Infernal Ring** — emits light below 30% HP (cleans up after itself)
- **Explorer Compass** — use to attune to the nearest Ancient Crypt; whispers direction while equipped
- **Stoneheart** — heavy knockback resistance
- **Blood Pendant** — tiny heal after kills

### 🛶 Large Raft
Custom 3D model: plank deck, rope-lashed rails, mast + swaying lantern,
storage chest (27 slots, sneak + use), furnace, animated oars while rowing,
bobbing/rocking on water. Deliberately slower than a boat — a mobile base, not a speedboat.

### 🎈 Pocket Airship
Wooden gondola, fabric balloon, rigging, copper-trimmed steam engine and
spinning propeller. Fed with **coal/charcoal** (right-click to refuel).
W/S thrust · A/D steer · jump/sneak climb/descend. Balanced against Elytra:
moderate top speed, slow spool-up, +46 m altitude ceiling, real fuel burn.
A compact bronze **flight HUD** shows fuel / altitude / speed / engine state
and hides when you land.

### 💧 Irrigation & fertility
- **Wood Canal** (bucket-fillable, water visually flows to neighbours with a
  diminishing level) → **Stone Pipe** → **Sprinkler**.
- Sprinkler: rotating head, water-arc particles, hydrates farmland and gently
  boosts crops in a 5-block radius — helpful, never fully automatic.
- **Enriched Farmland** + **Compost Cake**: a fertility bar (`███████░░░`) you can
  inspect by sneak-interacting farmland; high fertility speeds growth, and it
  depletes slowly as it feeds crops. Low maintenance by design.

---

## Getting the jar

A ready-to-install, CI-built jar is committed to this repository:

**`release/frontier-1.0.0.jar`** — drop it into your NeoForge `mods/` folder.

It is produced by GitHub Actions on every push (`.github/workflows/build.yml`:
JDK 21 + Gradle 8.14.2 + ModDevGradle) and re-published automatically.

## Building from source

Requirements: **JDK 21** and Gradle 8.14.2 (or run `gradle wrapper` once).

```bash
gradle build          # produces build/libs/frontier-1.0.0.jar
gradle runClient      # launch a dev client with the mod
gradle runServer      # launch a dev server
```

The mod has **no hard dependencies** beyond NeoForge itself. Curios is an
*optional* dependency (declared in `neoforge.mods.toml`); the relic system
ships with its own two-slot pouch so nothing breaks without it.

## Project layout

```
src/main/java/com/frontier/frontier/
├── FrontierMod.java          entrypoint + registry wiring
├── init/                     deferred registries (blocks, items, entities, menus,
│                             sounds, particles, components, attachments, structures, tabs)
├── forge/                    forging properties, components, server-authoritative rules
├── block/ + block/entity/    grindstone, crypt masonry, irrigation, enriched farmland
├── item/                     relics, forging materials, vehicle items, compass
├── entity/ (+model, renderer) raft, airship, two custom mobs + keyframe animations
├── menu/ + client/gui/       containers + Minecraft-styled screens, FORGE button, HUD
├── relic/                    relic attachment, pouch container, server-side effects
├── world/ (+structure, gen)  4 hand-designed crypts, loot keys, crypt locator
├── network/                  client→server payloads (forge, open pouch, airship input)
└── event/                    combat rules, relic ticks, farming interactions, client loop
```

All textures are original pixel art in `assets/frontier/textures`; all sounds are
mapped onto vanilla audio so the jar stays lean.

## Balance notes
- Forged properties cap at **III** with diminishing weights (1.0 / 0.6 / 0.35).
- Airship top speed ≈ 4.2 m/s; ceiling 46 blocks above departure (and y ≤ 150).
- Sprinklers never plant, harvest or bone-meal fully — they only *encourage*.
- Crypts are spaced ~22 chunks apart (random_spread, separation 8).
