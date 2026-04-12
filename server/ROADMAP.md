# Reason — Cleanup Roadmap

Engineering roadmap for cleaning up the inherited Treason/Kronos codebase into something I actually want to work in. Focus: ergonomic content APIs (Zenyte-style), functional composition over inheritance, consolidation of duplicated systems.

**Not a launch plan. Not a feature list. This is a refactor plan.**

---

## Guiding principles

These are the rules every decision on this roadmap is measured against. When in doubt, re-read this section.

1. **Functional composition over inheritance.** If a content primitive currently requires extending a nested interface tree, it's getting deleted and replaced with a lambda-taking registry or a Kotlin DSL. No exceptions.
2. **One canonical way.** Every system in the codebase should have exactly one public entry point. Two task schedulers = bug. Two combat APIs = bug. Three `Player` types = bug.
3. **Content code never imports engine code.** If writing a boss requires `import org.rsmod.*` or `import io.ruin.*`, the content API has failed. Content code imports only from the content API module.
4. **Kotlin-first for new code.** Java stays in the engine layer where it exists, but no new Java is written in this repo. All new APIs are Kotlin, idiomatic, using coroutines for any async/tick-delayed work.
5. **Deletion is progress.** LOC going down is a win. Every phase of this roadmap should end with fewer lines of code than it started with, not more.
6. **Zero behavior change per phase.** Each phase is a pure refactor. If behavior changes, it's a bug. This is how we refactor a 490k-LOC codebase without breaking the game.

---

## What I love about Zenyte that needs to exist here

For reference — this is the target ergonomic level.

### Dialogue (Zenyte)
Stage-based, declarative, subclass-or-lambda, all in one place. Writing a dialogue is 20 lines, not 200. The content author never touches the underlying packet layer or the interface system.

### Commands (Zenyte)
Single `HashMap<String, Command>` registry in `GameCommands.java`. Add a command by adding an entry. That's it. No interface to implement, no hook to register, no annotation to scan. One file, one place, greppable.

### Tasks (Zenyte)
One `WorldTasksManager.schedule(task, delay, period)` call. Done. Not two systems, not a decision, not a dispatcher choice.

### Combat scripts (Zenyte)
Per-NPC `CombatScript` interface with one `attack()` method. Registry dispatches. Adding a boss is implementing one method, not standing up a new module with its own state machine.

---

## What Reason currently has that I hate

Specific pain points from direct code inspection. These are the targets.

| Pain | Where it lives | Why it's broken |
|---|---|---|
| Three `Player` types | `org.rsmod.game.entity.Player`, `PlayerAttributes`, `io.ruin.model.player.Player` | Every content author has to pick a layer. The answer differs per boss module. Mental-model tax on every line of content code. |
| Dialogue is a suspend fun hidden in a coupled module | `kronos-server-kotlin-coupled/.../api/Player.kt` | Not discoverable. Not composable. Mixes engine primitives with content helpers. No DSL. |
| Commands via nested interface hooks | `io.ruin.network.incoming.handlers.CommandHandler` + `HooksV2<Hook>` | This is the "nested interface mess" I refuse to accept. A command shouldn't require implementing an interface. |
| Two task systems | `scheduler/TaskScheduler.kt` (coroutines), `core/task/Continuations.java` (callbacks) | Every time I schedule a task I have to pick. Pick once, at the API level. |
| Combat fragmented across 8+ boss modules | `npc-nex`, `yama` (28 files), `tormenteddemon`, `royaltitans`, `gemstone-crab`, `doom-of-mokhaiotl`, `dominion-of-echoes`, `theatre-of-blood` | No base class. Each boss invents its own scaffolding. |
| Two combat APIs | `core-combat-api`, `core-combat-api-reason` | One is clearly a migration target and nobody finished. |
| Three server modules | `kronos-server`, `kronos-server-kotlin`, `kronos-server-kotlin-coupled` | Incremental-migration accident. Should be one module. |
| Six `player-*` modules | `player-attributes`, `player-attributes-api`, `player-mongo`, `player-music`, `player-chat-filter`, `player-groupiron` | Over-modularised. Most are 2-file modules with a dep on nothing. |
| Two inherited namespaces | `io.ruin.*` and `org.rsmod.*` | Lineage scar from Zenyte → rsmod migration. Pick one. |

---

## Phase 0 — Warm-up refactors (1 week, low risk)

Pure structural cleanup. No behavior change. No API surface change. Goal: shrink the module graph and build confidence before the hard work.

### 0.1 — Collapse the three server modules
**Target:** `kronos-server` + `kronos-server-kotlin` + `kronos-server-kotlin-coupled` → one module, `reason-server`.

- Create `reason-server/` directory
- Move all `src/main/java` from the three modules into `reason-server/src/main/java`, preserving package structure
- Move all `src/main/kotlin` from the three modules into `reason-server/src/main/kotlin`, preserving package structure
- Merge the three `build.gradle.kts` files (union of dependencies, deduplicated)
- Update `settings.gradle.kts`: remove the three old includes, add `reason-server`
- Update every other module's `build.gradle.kts` that depended on any of the three — point at `reason-server`
- Verify compile. Fix imports as needed (should be zero if package structure was preserved).

**Success criteria:** `./gradlew :kronos-boot:run` boots a working server. Zero behavioral diff. Three fewer modules in the tree.

**Estimated:** half a day.

---

### 0.2 — Merge the `player-*` modules
**Target:** All six `player-*` modules → one `reason-player-systems` module, with sub-packages matching the old module names.

- Audit each `player-*` module. Which are actually independent? (`player-mongo` might genuinely want isolation for persistence reasons; `player-attributes-api` is probably just a tiny interface file.)
- Candidates for merge: `player-attributes`, `player-attributes-api`, `player-music`, `player-chat-filter`, `player-groupiron`
- Candidate to keep isolated: `player-mongo` (persistence dependency isolation is a legit reason)
- Package structure inside the merged module: `reason.player.attributes`, `reason.player.music`, `reason.player.chatfilter`, etc.

**Success criteria:** Six modules become two. Imports change, behavior does not.

**Estimated:** half a day.

---

### 0.3 — Dead-module hunt
**Target:** Identify and delete modules that are unused, abandoned, or vestigial.

Candidates to audit (not necessarily delete — audit first):
- `interface-ikod`, `interface-advancedsettings`, `interface-tabsettings`, `interface-charactercreator` — interface modules, possibly abandoned
- `donationdeals`, `tradepost`, `collectionlog` — content modules, possibly stub
- `core-module-test` — test-only module?
- `logging-sentry` — actively used?

Audit process per module:
1. `grep -r "import <module_package>" --include="*.kt" --include="*.java"` — any consumers?
2. Read the module's top-level classes — is there real logic or just scaffolding?
3. If no consumers and no logic: delete.
4. If consumers exist but module is tiny: candidate for merging into its consumer.

**Success criteria:** Module count drops from 42 toward ~30. Every remaining module justifies its existence.

**Estimated:** 1 day.

---

### 0.4 — Namespace decision
**Target:** Pick one of `io.ruin.*`, `org.rsmod.*`, or a new `reason.*`. Document the decision. Do NOT rename yet — this is just the decision.

Recommendation: **new `reason.*` namespace** for anything that's ours going forward. Leave existing `io.ruin.*` and `org.rsmod.*` packages alone for now — rename in a later phase once the dust settles. This means:
- All new code goes under `reason.*`
- All new modules use `reason.*` as their root package
- The content API (Phase 1) lives under `reason.api.*`

**Success criteria:** Decision documented in this file. New code convention established.

**Estimated:** decision is already made above. ✓

---

## Phase 1 — Content API foundation (1–2 weeks)

Create the module that every content author will import. Populate it with stubs. Wire the first real API through it end-to-end so the pattern is established.

### 1.1 — Create `reason-content-api` module
**Target:** New Gradle module that is the single public API surface for content authors.

- New directory: `reason-content-api/`
- `build.gradle.kts` depends on `reason-server` as `implementation` (not `api` — content modules must not transitively see engine types)
- Root package: `reason.api`
- Sub-packages, one per subsystem:
  - `reason.api.player` — Player wrapper
  - `reason.api.npc` — Npc wrapper
  - `reason.api.world` — World/global access
  - `reason.api.dialogue` — Dialogue DSL (Phase 2)
  - `reason.api.command` — Command registry (Phase 2)
  - `reason.api.task` — Task scheduler (Phase 2)
  - `reason.api.combat` — Combat script base (Phase 3)
  - `reason.api.inventory` — Inventory operations
  - `reason.api.event` — Typed event bus

Stub each sub-package with a single placeholder file so the structure is visible from day one.

**Success criteria:** Module compiles. No consumers yet. Structure is obvious from the package tree.

**Estimated:** 1 day.

---

### 1.2 — Unify the task scheduler behind `reason.api.task.WorldTasks`
**Target:** One canonical task-scheduling API. Delete one of the two existing systems.

Pick the winner: **`TaskScheduler.kt` (coroutine-based).** Reason: aligns with the tick loop's existing coroutine dispatcher, idiomatic Kotlin, no callback nesting, suspend-native so dialogues and delayed actions compose naturally.

Shape of the API:

```kotlin
object WorldTasks {
    /** Run [block] after [delay] ticks, once. Returns a handle for cancellation. */
    fun schedule(delay: Int, block: suspend () -> Unit): TaskHandle

    /** Run [block] every [period] ticks, starting after [delay]. Forever until cancelled. */
    fun repeat(delay: Int = 0, period: Int, block: suspend () -> Unit): TaskHandle

    /** Run [block] each tick until it returns false. */
    fun tickUntil(block: suspend () -> Boolean): TaskHandle

    /** Suspend the current coroutine for [ticks] game ticks. */
    suspend fun delay(ticks: Int)
}

interface TaskHandle {
    fun cancel()
    val isActive: Boolean
}
```

Steps:
1. Implement `WorldTasks` as a wrapper over the existing `TaskScheduler.kt` + coroutine dispatcher
2. Migrate every call site of `Continuations.java` to `WorldTasks`
3. Migrate every direct call to `TaskScheduler` inside content code to `WorldTasks`
4. Delete `core/task/Continuations.java`
5. Make `TaskScheduler` package-private or move to `reason-server` internal

**Success criteria:** `Continuations.java` is gone. Content code only calls `WorldTasks`. The engine still runs.

**Estimated:** 2–3 days.

---

### 1.3 — Consolidate `Player` behind `reason.api.player.Player`
**Target:** One public `Player` type that content code sees. The existing three types either merge into it or become engine-internal.

Strategy: **Wrapper pattern, not merge.** Creating a new `reason.api.player.Player` class that holds a reference to the underlying rsmod `Player` and exposes only the methods content code needs. Don't try to unify the three engine types — let them stay messy internally, but hide them behind one content-facing wrapper.

```kotlin
package reason.api.player

class Player internal constructor(
    internal val core: org.rsmod.game.entity.Player,
) {
    val username: String
    val tile: Tile
    val inventory: Inventory
    val equipment: Equipment
    val bank: Bank
    val skills: Skills

    fun sendMessage(message: String)
    fun teleport(tile: Tile)
    fun animate(id: Int)
    suspend fun dialogue(block: DialogueBuilder.() -> Unit)
    // ... ~30 methods total, nothing more
}
```

Rules:
- `internal` visibility for the engine reference — content code cannot reach past the wrapper
- Every method on this `Player` is hand-chosen. If a method isn't here, content code can't call it.
- The method count is a feature. If it grows past ~50, we're leaking engine concerns.

Steps:
1. Write the wrapper class with ~30 hand-chosen methods
2. Write an extension function `org.rsmod.game.entity.Player.api(): reason.api.player.Player` that wraps
3. At every engine → content boundary (dialogue handler, command handler, event dispatcher), wrap the raw player into the API type before passing
4. Content code receives `reason.api.player.Player`, not the rsmod type

**Success criteria:** All new content imports only `reason.api.player.Player`. The three engine types still exist but are no longer a decision content authors make.

**Estimated:** 3–4 days. Method count will grow as you port content.

---

## Phase 2 — Port the Zenyte ergonomic APIs (2 weeks)

This is the heart of the cleanup. These are the APIs I actually want to use every day.

### 2.1 — Dialogue DSL (`reason.api.dialogue`)
**Target:** A Kotlin DSL that lets a dialogue be written in 20 lines of declarative code. Functional composition with suspend control flow. Zero inheritance.

Target API:

```kotlin
dialogue(player) {
    npc(HANS, "Greetings, traveller. What brings you to Lumbridge?")
    val choice = options(
        "I'm looking for work.",
        "Just passing through.",
        "Nothing, goodbye.",
    )
    when (choice) {
        0 -> {
            npc(HANS, "There's always work at the castle. Speak to the duke.")
            player("Thanks, I'll head there now.")
        }
        1 -> {
            npc(HANS, "Safe travels then.")
        }
        2 -> Unit  // closes the dialogue
    }
}
```

**Why this beats both Zenyte AND current Reason:**
- Vs. Zenyte's stage-based map: you get real control flow (`when`, `if`, `for`) instead of encoding it as stage jumps
- Vs. Reason's nested-interface suspend helpers: it's a single declarative block, not scattered suspend calls wired through a player wrapper

Implementation pattern:
- `DialogueBuilder` is a Kotlin DSL receiver class
- `npc()`, `player()`, `options()`, `item()`, `plain()`, `level()` are suspend functions on the receiver
- Each suspend function opens the underlying RSProt dialogue, yields until the player clicks continue, then closes
- `options()` returns the chosen index as an `Int`
- Composable: helpers like `confirm()`, `yesNo()`, `teleportOptions()` can be written on top

Steps:
1. Design the `DialogueBuilder` interface (1 day)
2. Implement the builder as a suspend-function facade over RSProt's `NPCDialogue` / `PlayerDialogue` primitives (2 days)
3. Port 3 existing dialogues from the current suspend-helper style to the new DSL to validate the shape (1 day)
4. Port the remaining dialogues in batches (ongoing)
5. Delete the old `api/Player.kt` dialogue helpers

**Success criteria:** Writing a new dialogue is a single `dialogue { ... }` block. No subclassing. No stage numbers. No interface to implement.

**Estimated:** 4–5 days for the framework + first ports.

---

### 2.2 — Command registry (`reason.api.command`)
**Target:** A single HashMap-backed registry. Adding a command is one `Commands.register` call. No interfaces. No hooks. No annotations.

**This is the one I'm most allergic to in the current codebase.** The nested-interface `HooksV2<Hook>` pattern is the exact thing I want to kill.

Target API:

```kotlin
object Commands {
    fun register(
        name: String,
        privilege: Privilege = Privilege.PLAYER,
        description: String = "",
        block: suspend CommandContext.() -> Unit,
    )
}

// Usage:
Commands.register("give", Privilege.ADMIN, "Spawn an item") {
    val itemId = args.int(0)
    val amount = args.intOr(1, default = 1)
    player.inventory.add(itemId, amount)
    player.sendMessage("Spawned $amount × ${ItemDef.name(itemId)}.")
}

Commands.register("tele", Privilege.ADMIN) {
    val x = args.int(0)
    val y = args.int(1)
    val z = args.intOr(2, default = player.tile.z)
    player.teleport(Tile(x, y, z))
}

Commands.register("home", Privilege.PLAYER) {
    player.teleport(HOME_TILE)
}
```

Implementation:
- `Commands` is an `object` with an internal `MutableMap<String, CommandEntry>`
- `CommandContext` exposes `player: Player`, `args: CommandArgs` (with `int`, `str`, `intOr`, `strOr`, etc.)
- `Privilege` is an enum with ordered levels (PLAYER < MOD < ADMIN < DEV)
- Privilege gating is automatic — the registry refuses to dispatch if the player's rank is below the command's requirement
- Commands register themselves in a static init block inside their module
- A single `CommandHandler` in `reason-server` reads `player.inputs.command` from the tick and dispatches by map lookup

Steps:
1. Write `Commands`, `CommandContext`, `CommandArgs`, `Privilege` (1 day)
2. Write the dispatcher that reads the incoming command packet and calls the registry (half a day)
3. Migrate all existing commands from the HooksV2 system to `Commands.register` (2 days — this is grunt work)
4. Delete `CommandHandler.java` and every HooksV2 registration for commands

**Success criteria:** Every command in the codebase is a single `Commands.register` call. Searching for a command by name takes 1 second with grep. No interface implementations exist for commands.

**Estimated:** 3–4 days total, most of it the migration.

---

### 2.3 — Typed event bus (`reason.api.event`)
**Target:** Functional event registration. No listener interfaces, no annotation scanning.

```kotlin
Events.on<PlayerLoginEvent> { event ->
    event.player.sendMessage("Welcome to Reason.")
}

Events.on<NpcDeathEvent> { event ->
    if (event.npc.id == GIANT_MOLE) {
        event.player.inventory.add(MOLE_CLAW)
    }
}
```

Implementation:
- `Events` is an `object` with an internal `MutableMap<KClass<*>, MutableList<suspend (Any) -> Unit>>`
- Typed via `reified` generics
- Fired from engine points via `Events.fire(event)`
- Exceptions in listeners are logged but do not abort event dispatch

Steps:
1. Define the core event types: `PlayerLoginEvent`, `PlayerLogoutEvent`, `NpcDeathEvent`, `NpcSpawnEvent`, `ObjectClickEvent`, etc. (1 day)
2. Implement `Events` registry (half a day)
3. Wire engine-side fire points (1 day)
4. Migrate existing listener interfaces to `Events.on` (variable)

**Success criteria:** Hooking into a game event is one `Events.on<T>` call. No subclassing.

**Estimated:** 2–3 days.

---

## Phase 3 — Combat consolidation (1–2 weeks)

The big one. This is where we pay down the "combat fragmented across 8 boss modules with no base class" debt.

### 3.1 — Delete one of the two combat APIs
**Target:** `core-combat-api` OR `core-combat-api-reason`, pick the survivor, migrate, delete the loser.

Recommendation: keep `core-combat-api-reason` (the newer one by naming convention), migrate `core-combat-api` consumers to it, delete `core-combat-api`.

**Estimated:** 1–2 days.

---

### 3.2 — `reason.api.combat.CombatScript` base class
**Target:** One canonical way to write a boss. Every boss in the codebase extends this single class.

Target API:

```kotlin
abstract class CombatScript {
    /** Called once per tick while the NPC is in combat. */
    abstract suspend fun attack(npc: Npc, target: Entity)

    /** Optional: override to customise the max hit per attack style. */
    open fun maxHit(npc: Npc, style: AttackStyle): Int = npc.def.maxHit(style)

    /** Optional: override to customise attack speed. */
    open fun attackSpeed(npc: Npc): Int = npc.def.attackSpeed

    /** NPC IDs this script applies to. Registry dispatches by ID. */
    abstract val npcIds: IntArray
}
```

Registry dispatches an NPC to its CombatScript by ID. Each boss module provides one or more CombatScripts.

Steps:
1. Write `CombatScript`, `AttackStyle`, `CombatResult`, `Hit` as a clean content-API-layer type (1 day)
2. Port one simple boss (Gemstone Crab) to validate the pattern (1 day)
3. Port each remaining boss in turn (varies — Yama with its 28 files will be the hardest)
4. Delete per-boss ad-hoc scaffolding as you go

**Success criteria:** Every boss is a `CombatScript` subclass. The 8 boss modules still exist but each contains primarily CombatScript implementations, not bespoke state machines.

**Estimated:** 1–2 weeks depending on how deep the boss-specific scaffolding goes.

---

## Phase 4 — Polish and finalisation (1 week)

Final pass. Delete the last remnants of the old APIs. Namespace rename. LOC audit.

### 4.1 — Namespace rename
**Target:** All code owned by Reason lives under `reason.*`. Inherited `io.ruin.*` and `org.rsmod.*` packages are renamed or hidden.

- Use IntelliJ's refactor tooling for package renames (`Refactor → Rename` on the package root, not text replace)
- Commit each rename as a separate commit so git blame stays readable
- Two passes: first `io.ruin.*` → `reason.legacy.*` (temporary), then bit-by-bit port to `reason.*`

**Estimated:** 2–3 days.

---

### 4.2 — LOC audit
**Target:** Verify the cleanup actually shrunk the codebase.

- Run `cloc` against the repo and record Kotlin/Java LOC
- Compare against the Phase 0 baseline
- If LOC grew, figure out why (usually: DSLs have more scaffolding than the code they replaced, which is fine if ergonomics improved)
- Record the delta in this file

**Baseline (2026-04-11):** 490,634 LOC (Java 418,972, Kotlin 71,662)

**Target after Phase 4:** Under 400k LOC, with Kotlin percentage climbing toward 25–30%.

**Estimated:** half a day.

---

### 4.3 — Module template (`modules/_template`)
**Target:** A copy-pasteable Gradle module that demonstrates every piece of the content API.

Contains:
- One NPC with a `CombatScript`
- One dialogue using the `dialogue { }` DSL
- One command using `Commands.register`
- One event listener using `Events.on`
- One scheduled task using `WorldTasks.schedule`

This becomes the canonical "how do I add content to Reason" answer, and doubles as the `docs/ADDING_CONTENT.md` source.

**Estimated:** 1 day.

---

## Out of scope for this roadmap

Things that are NOT part of this cleanup (but are eventually needed):
- Open-source launch prep (LICENSE, README, .gitignore, bootstrap script, docker compose hardening) — separate workstream
- Rev upgrades to protocol 220+ — do not touch until cleanup is done
- New content features — do not add while refactoring
- Performance optimisation — not a problem right now
- Tests — will be added incrementally as content API stabilises, not as a dedicated sprint
- Client-side changes — this roadmap is server-only

---

## Order of execution

Strict sequence. Do not skip ahead.

| Phase | Block | Est. | Risk |
|---|---|---|---|
| 0.1 | Collapse three server modules | 0.5d | Low |
| 0.2 | Merge `player-*` modules | 0.5d | Low |
| 0.3 | Dead-module hunt | 1d | Low |
| 0.4 | Namespace decision | ✓ done | — |
| 1.1 | Create `reason-content-api` module | 1d | Low |
| 1.2 | `WorldTasks` — unify schedulers | 2–3d | Medium |
| 1.3 | `Player` wrapper | 3–4d | High |
| 2.1 | Dialogue DSL | 4–5d | Medium |
| 2.2 | Command registry | 3–4d | Medium |
| 2.3 | Event bus | 2–3d | Medium |
| 3.1 | Kill one combat API | 1–2d | Medium |
| 3.2 | `CombatScript` base class | 1–2w | High |
| 4.1 | Namespace rename | 2–3d | Low (mechanical) |
| 4.2 | LOC audit | 0.5d | — |
| 4.3 | Module template | 1d | Low |

Total estimated effort: **6–8 weeks of focused work.**

Realistic calendar time with life and other projects in the mix: **2–3 months.**

---

## Tracking

Mark phases complete as they land. Record surprises and deviations inline so the next person reading this file (probably me in 2 weeks) knows what actually happened vs what was planned.

- [ ] Phase 0.1 — three servers → one
- [ ] Phase 0.2 — `player-*` merge
- [ ] Phase 0.3 — dead module hunt
- [x] Phase 0.4 — namespace decision (new `reason.*`)
- [ ] Phase 1.1 — `reason-content-api` module
- [ ] Phase 1.2 — `WorldTasks`
- [ ] Phase 1.3 — `Player` wrapper
- [ ] Phase 2.1 — Dialogue DSL
- [ ] Phase 2.2 — Command registry
- [ ] Phase 2.3 — Event bus
- [ ] Phase 3.1 — Kill one combat API
- [ ] Phase 3.2 — `CombatScript` base
- [ ] Phase 4.1 — Namespace rename
- [ ] Phase 4.2 — LOC audit
- [ ] Phase 4.3 — Module template
