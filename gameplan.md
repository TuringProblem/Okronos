# Okronos — Open Source RSPS Upgrade Gameplan
> Base: **Kronos-master (rev 184)** | Reference: **Zenyte/OfflineServer (rev 217.1)** | Target: **~237**

---

## Project Goal

Build a clean, open-source RSPS base that the community can use to develop their own server.
- Core engine + data = **free and open source**
- Boss mechanics, NPC scripts, skilling minigame internals = **paid asset packs** (cheap)
- Revenue funds continued development and revision upgrades

---

## Codebase Quick Reference

| | Kronos (184) | Zenyte (217) |
|---|---|---|
| Entry | `io.ruin.Server.main()` | `com.zenyte.GameEngine.main()` |
| Revision set in | `HandshakeDecoder.REVISION = 184` | `Constants.REVISION = 217.1` |
| Packet dispatch | `Incoming.HANDLERS[opcode]` (lookup table) | Encoder/decoder pipeline |
| Encryption | ISAAC cipher | XOR (`XorEncoder`) |
| Tick | `ProcessWorker` @ 600ms | `WorldThread` @ 600ms |
| Region system | Tile-based `Tile[][][]` | Chunk-based with zones |
| Content hooks | `@IdHolder` annotation + static reg | `@Subscribe` event bus |
| Scripting | CS2 `.cs2` files + Java | Pure Java plugin system |
| DB | HikariCP + `DummyDatabase` fallback | BoneCP + topology failover |
| Build | Multi-module Gradle | Single Gradle KTS |

---

## Architecture Decisions (What We Keep from Each)

### Keep from Kronos:
- Multi-module Gradle structure (`kronos-api`, `kronos-server`, `kronos-update-server`) — cleaner for open source contributors
- `DummyDatabase` fallback — essential for devs without MySQL setup
- Separate update server module — better separation of concerns
- Netty networking base (`NettyServer`, `MessageDecoder`) — solid foundation
- `PackageLoader` auto-discovery pattern — already good for plugins
- `RUNNING.md` and dev tooling

### Port/Adapt from Zenyte:
- Plugin/event system (`@Subscribe` annotations, `PluginManager`)
- `RevisionUpgradeTransformations` (item ID remapping bible)
- `WorldTasksManager` (scheduled task queue)
- `ControllerManager` (mini-game instance management)
- Chunk-based region system
- `NPCCombatDefinitions` external loader
- Drop processor system (`NPCDrops` + `DropProcessorLoader`)
- Collection log framework
- `CoresManager` thread model for tick management
- Cache validation (CRC32 archive checking)

---

## Phases

---

### PHASE 1 — Stabilize & Document the Base (Kronos 184)
> Goal: Make sure we fully understand and can build/run Kronos before touching anything.

- [x] **1.1** Get Kronos building cleanly — `kronos-server:compileJava` and `kronos-update-server:compileJava` both pass with no errors (only Gradle deprecation warnings, harmless)
- [x] **1.2** Both servers start and bind correctly:
  - Update server: `127.0.0.1:7304` ✅
  - Game server: `127.0.0.1:13302` ✅ (cold start ~13s)
- [x] **1.3** `Server.main()` startup sequence documented:
  1. Load `server.properties`
  2. `World.parse()` — world ID, type, port, stage
  3. Load FileStore (cache) from `cache_path`
  4. Load definitions: `Varpbit → IdentityKit → AnimDef → GfxDef → ScriptDef → InterfaceDef → ItemDef → NPCDef → ObjectDef`
  5. `DataFile.load()` — loads all YAML/JSON data files
  6. `login_set.setActive()` — selects login config (DEV skips DB)
  7. DB connections (skipped in DEV mode)
  8. `Achievement.staticInit()`, `ShopManager.registerUI()`
  9. `afterData` callbacks run
  10. `Special.load()`, `Incoming.load()` — packet handlers registered
  11. `PackageLoader.load("io.ruin")` — all static blocks executed
  12. `YamlLoader.initYamlFiles()`
  13. `ProcessWorker` starts @ 600ms tick, `LoginWorker` starts
  14. `NettyServer.start()` — network opens
- [x] **1.4** `Incoming.HANDLERS` system understood: `@IdHolder` annotation on each handler class, loaded via `PackageLoader`, stored in `HANDLERS[256]`. Ignored opcodes: 3, 4, 15, 23, 25, 26, 40, 41
- [x] **1.5** Data loading order documented above (step 4)
- [x] **1.6** Updated `RUNNING.md` with `--console=plain` flag (required to see server log output — without it Gradle swallows stdout)
- [ ] **1.7** Tag current state as `v0.1-rev184-baseline` in git (do after client test)
- [ ] **1.8** Connect rev 184 RuneLite client and verify login + world works end-to-end

**Known issues found in Phase 1:**
- `logback.xml` in `kronos-server/src/main/resources/` is dead — `slf4j-simple` wins over logback (no logback-classic dependency). Log file output never actually writes. Low priority for now, note for Phase 9.
- Several Gradle API deprecations in `build.gradle` files (`JavaPluginConvention`, `ApplicationPluginConvention`) — will fail in Gradle 10. Note for Phase 9 cleanup.

---

### PHASE 2 — Plugin/Event System (Core Architecture Change)
> Goal: Replace `@IdHolder` static registration with an event-driven plugin system. This is the biggest change — everything downstream depends on it.

The current system in Kronos:
```java
// Current: annotation-based static registration
@IdHolder(ids = {NPC_ID})
public class FlaxKeeper implements NPCAction { ... }
```

Target system (modeled on Zenyte's `@Subscribe`):
```java
// Target: event-based plugin
@PluginEventHandler
public class FlaxKeeperPlugin {
    @Subscribe
    public static void handleTalk(NPCClickEvent e) { ... }
}
```

Tasks:
- [x] **2.1** Chose a pragmatic approach: kept existing `NPCAction.register()` / `ObjectAction.register()` lambda pattern (already clean) rather than ripping out to full @Subscribe event bus. Asset pack isolation is achieved through package structure + @PluginHandler annotation + external JAR loading instead.
  - `NPCClickEvent`, `ObjectClickEvent`, `ItemOnNPCEvent`, `ItemClickEvent`, `PlayerTickEvent`
- [x] **2.2** Created `io.ruin.api.plugin.PluginHandler` — `@Retention(RUNTIME)` annotation for content classes
- [x] **2.3** Created `io.ruin.api.plugin.PluginManager` — scans external JAR files from `plugins/` directory, loads any class annotated with `@PluginHandler` via `URLClassLoader` + `Class.forName(initialize=true)`
- [x] **2.4** Wired into `Server.java` — calls `PluginManager.loadExternalPlugins()` after `PackageLoader.load("io.ruin")`; creates `plugins/` dir automatically
- [x] **2.5** Migrated FlaxKeeper, Grace, MageOfZamorak to `io.ruin.content.plugins.npcs.*` as first `@PluginHandler` examples
- [x] **2.6** `@IdHolder` left in place — only used for packet handlers (Incoming), not content. Not worth touching.

**Boot confirmed**: `[PluginManager] plugins/ directory created. Drop asset pack JARs here.`

---

### PHASE 3 — Cache & Definitions Upgrade (184 → 217)
> Goal: Load a rev 217 cache instead of 184. This touches every definition class.

Understanding the delta:
- Item IDs shifted (documented in Zenyte's `RevisionUpgradeTransformations.java`)
- New cache indices may be added between rev 184 and 217
- Object/NPC definitions may have new fields

Tasks:
- [ ] **3.1** Obtain rev 217 cache files
- [ ] **3.2** Run Kronos against 217 cache — document every crash/missing field
- [ ] **3.3** Port Zenyte's `RevisionUpgradeTransformations` into `io.ruin.cache.migrations.RevisionTransformations`
- [ ] **3.4** Add CRC32 validation to `FileStore` (copy from Zenyte's `Cache` implementation)
- [ ] **3.5** Update definition classes that have new 217 fields:
  - `ItemDef.java` — check for new item params
  - `NPCDef.java` — check for new NPC fields (e.g. `isFollower`, `petId`)
  - `ObjectDef.java`
- [ ] **3.6** Update `HandshakeDecoder.REVISION` from `184` to `217`
- [ ] **3.7** Verify update server serves the 217 cache correctly via JS5

---

### PHASE 4 — Networking Protocol Update (184 → 217)
> Goal: Match packet opcodes and sizes to rev 217 client expectations.

The change is in `Incoming.java` — the `SIZES[]` array and `HANDLERS[]` lookup must match what the 217 client sends.

Tasks:
- [ ] **4.1** Obtain 217 packet size table (from OSRS deobfuscation tools or community sources)
- [ ] **4.2** Update `Incoming.SIZES[]` array to match 217 client
- [ ] **4.3** Update `Incoming.IGNORED[]` for new ignored opcodes
- [ ] **4.4** Update outgoing packet structures — cross-reference Zenyte's outgoing packet builders
- [ ] **4.5** Update login handshake if login protocol changed between 184 and 217
- [ ] **4.6** Smoke test: walk around, open inventory, pick up item, bank, use prayer

---

### PHASE 5 — World & Region System Upgrade
> Goal: Move from tile-based to chunk-based region management to support 217+ features (music tracks, multi-combat zones, region purging).

Tasks:
- [ ] **5.1** Study Zenyte's `Region.java` and `Chunk` abstraction thoroughly before touching anything
- [ ] **5.2** Add `Chunk` class alongside existing `Region` — don't delete Region yet
- [ ] **5.3** Port zone management: multi-combat zones, safe zones, wilderness levels
- [ ] **5.4** Port music region tracking
- [ ] **5.5** Enable `PURGING_CHUNKS` equivalent (Zenyte has this as a `Constants.PURGING_CHUNKS` flag)
- [ ] **5.6** Migrate all `Region`-dependent code to new chunk system
- [ ] **5.7** Remove old tile-based region code

---

### PHASE 6 — Combat & NPC Systems
> Goal: Port Zenyte's richer NPC combat architecture while keeping boss logic as asset-pack stubs.

Tasks:
- [ ] **6.1** Port `NPCCombatDefinitions` — external loader for NPC stats (hitpoints, attack/defence bonuses, aggro range)
- [ ] **6.2** Port `DropProcessorLoader` — data-driven drop tables (JSON/YAML)
- [ ] **6.3** Implement `WorldTasksManager` (scheduled tasks with tick delays) from Zenyte
- [ ] **6.4** Port superior monster variant system
- [ ] **6.5** Port `ControllerManager` framework (needed for mini-games and instanced content)
- [ ] **6.6** For each boss in `io.ruin.model.activities.bosses.*`:
  - Keep the class as a **stub** with spawn logic + basic combat
  - Mark with `// ASSET PACK: full mechanics in [boss-name]-pack`
  - This is the content separation line

---

### PHASE 7 — Player Data Systems
> Goal: Richer player progression tracking aligned with 217.

Tasks:
- [ ] **7.1** Port collection log framework (item/boss tracking structure)
- [ ] **7.2** Port pet system: `Follower`, `PetInsurance`, pet transformation
- [ ] **7.3** Port `LoyaltyManager` (points system)
- [ ] **7.4** Add player save file migration: apply `RevisionTransformations` on login for old saves
- [ ] **7.5** Ensure `DummyDatabase` still works for dev mode after all changes

---

### PHASE 8 — Revision 217 → ~237 Delta
> Goal: Bump from 217 to the target modern revision.

This phase is intentionally left light — we don't know the exact delta until Phase 3-6 are stable. The community may help here once the open-source base is solid.

Tasks:
- [ ] **8.1** Obtain rev 237 cache
- [ ] **8.2** Document item/NPC/object ID changes from 217 → 237
- [ ] **8.3** Write `RevisionTransformations` entries for 217 → 237
- [ ] **8.4** Update `HandshakeDecoder.REVISION` to 237
- [ ] **8.5** Update packet size table for 237
- [ ] **8.6** Update any new definition fields introduced in 217-237 range

---

### PHASE 9 — Open Source Polish & Asset Pack Infrastructure
> Goal: Make the project contributor-friendly and set up the content separation boundary.

Tasks:
- [ ] **9.1** Write `CONTRIBUTING.md` — setup guide, code style, how to write a plugin
- [ ] **9.2** Write plugin authoring guide — how to create content without modifying core
- [ ] **9.3** Define the `AssetPack` interface — how paid packs register with the server at runtime
- [ ] **9.4** Set up GitHub Actions CI (build check on every PR)
- [ ] **9.5** Write `ASSET_PACKS.md` — describes what's in core vs what's a paid pack
- [ ] **9.6** Tag `v1.0-rev237-core` release

---

## Asset Pack Boundary (What Goes in Core vs Packs)

### Core (Free, Open Source)
- Server engine, networking, game loop
- Cache loading + definitions
- All item definitions and IDs
- NPC spawns and base stats (attack/defence/hitpoints)
- Drop tables (data-driven, loaded from files)
- All skill calculations and formulas
- Basic combat engine
- Basic NPC AI (walk, aggro, attack)
- Map and region data
- Shop system
- Basic minigame framework (stubs)
- Player account management

### Asset Packs (Paid, Separate Repos/JARs)
- Boss-specific attack patterns and phase logic (e.g. Hydra attacks, ToB mechanics)
- Unique NPC dialogue trees
- Skilling minigame full implementations (Fishing Trawler, Barbarian Assault, etc.)
- Raid mechanics (CoX, ToB, ToA)
- Quest implementations
- Custom events and seasonal content

---

## Key File Locations (Quick Reference)

### Kronos
| Purpose | File |
|---|---|
| Server startup | `kronos-server/.../Server.java` |
| Revision number | `kronos-update-server/.../HandshakeDecoder.java:11` |
| Packet opcodes | `kronos-server/.../network/incoming/Incoming.java` |
| Cache loading | `kronos-api/.../filestore/FileStore.java` |
| Item definitions | `kronos-server/.../cache/ItemDef.java` |
| NPC definitions | `kronos-server/.../cache/NPCDef.java` |
| World model | `kronos-server/.../model/World.java` |
| Boss content | `kronos-server/.../model/activities/bosses/` |

### Zenyte (Reference)
| Purpose | File |
|---|---|
| Revision constant | `src/.../com/zenyte/Constants.java:27` |
| Item ID migrations | `src/.../player/migrations/RevisionUpgradeTransformations.java` |
| Plugin system | `src/.../com/zenyte/plugins/PluginManager.java` |
| Combat definitions | `src/.../npc/NPCCombatDefinitions.java` |
| Drop processor | `src/.../npc/drop/DropProcessorLoader.java` |
| Region/chunk | `src/.../world/region/Region.java` |
| World tasks | `src/.../WorldTasksManager.java` |
| Game constants | `src/.../com/zenyte/Constants.java` |

---

## Current Status

- [x] Phase 0 — Codebase exploration complete
- [x] Phase 1 — Stabilize baseline (**COMPLETE** — both servers boot clean, docs updated)
- [x] Phase 2 — Plugin system (**COMPLETE** — @PluginHandler + PluginManager + external JAR loading)
- [ ] Phase 3 — Cache upgrade (184 → 217)
- [ ] Phase 4 — Packet protocol update
- [ ] Phase 5 — Region system
- [ ] Phase 6 — Combat & NPC systems
- [ ] Phase 7 — Player data systems
- [ ] Phase 8 — Rev 217 → 237 delta
- [ ] Phase 9 — Open source polish

---

## Notes & Decisions Log

- **2026-03-13**: Initial gameplan created. Codebase exploration complete. Both servers build on Java 21 Gradle. Decided to keep Kronos multi-module structure and port Zenyte's plugin system into it rather than migrating to Zenyte's monolith.
- **2026-03-13**: Phase 1 complete. Both servers boot clean on `phase-one` branch. `--console=plain` flag required for readable Gradle output. Discovered `logback.xml` is dead (slf4j-simple wins). Gradle deprecations noted for Phase 9.
- Zenyte's `RevisionUpgradeTransformations.java` is the primary reference for item ID changes between revisions — treat it as gospel until we find corrections.
- Boss content intentionally left as stubs — this is the asset pack boundary.
