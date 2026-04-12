# Running & Debugging the Reason Server

Operational guide for booting, inspecting, and debugging the Kronos-based Reason server. For architecture, module layout, and configuration reference, see [`README.md`](./README.md).

---

## 1. Prerequisites

| Requirement | Version | Notes |
|---|---|---|
| **JDK** | **24** (Temurin/Adoptium) | Mandatory. Build uses `--enable-preview` + `jvmToolchain(24)`. |
| Gradle | wrapper-managed | Do not install system Gradle — always use `./gradlew`. |
| Docker (optional) | any recent | Only if you enable MariaDB/MongoDB persistence. |
| Free port | `43594` | Game server. Also `9292` for the world list HTTP service. |

### Verify JDK 24 is active

```bash
java -version
# Expected: openjdk version "24" ... Temurin-24+36
```

If your shell defaults to a different JDK:

```bash
# macOS — point at Adoptium 24
export JAVA_HOME=$(/usr/libexec/java_home -v 24)
export PATH="$JAVA_HOME/bin:$PATH"

# SDKMAN
sdk use java 24-tem
```

Make it permanent by adding the `export` lines to `~/.zshrc` / `~/.bashrc`, or by setting `org.gradle.java.home` in `server/gradle.properties`.

---

## 2. Running the Server

### 2.1 Quickest path — Gradle `run`

```bash
cd ~/Documents/treason/server
./gradlew :kronos-boot:run
```

Handles compile + cache pack + boot in one command. Gradle keeps the JVM alive under the `run` task (progress bar will sit at `99% EXECUTING` — that's normal, not a hang).

### 2.2 Faster iteration — installed distribution

Build once, then relaunch the native script directly. Skips Gradle startup on every run:

```bash
./gradlew :kronos-boot:installDist
./kronos-boot/build/install/kronos-boot/bin/kronos-boot
```

### 2.3 Hot reload (JRebel)

```bash
./gradlew :kronos-boot:run_hotswap
```

Requires JRebel configured in `server/.jrebel/`. Use for content iteration without full restarts.

### 2.4 Clean rebuild

```bash
./gradlew clean :kronos-boot:installDist
```

Use when you suspect stale Kotlin incremental compilation, a corrupted cache pack, or after pulling invasive changes.

---

## 3. Boot Success Signals

Tail the console and watch for this sequence:

```
Loading FileStore ...
Loaded definitions ...
Loading server scripts & handlers...
Started server in <N>ms.                      <-- core game engine up
Starting world list server on port 9292...
Application started in 0.116 seconds.          <-- Ktor (world list) up
Responding at http://0.0.0.0:9292
```

The **`Started server in Nms`** log line from `io.ruin.Server:309` is the definitive "game engine ready" marker. Expected cold-boot time: ~9–15s after compilation finishes.

**Healthcheck from another terminal:**

```bash
# Game port is raw TCP — just verify it's bound
lsof -i :43594

# World list is HTTP
curl -s http://127.0.0.1:9292/ | head
```

---

## 4. Connecting the Client

```bash
cd ~/Documents/treason/client
java -ea --add-opens=java.base/java.lang=ALL-UNNAMED -jar client-localhost.jar
```

- Client connects to `127.0.0.1:43594` (baked into `client-localhost.jar`).
- Log in with any username + the `login_master_password` from `server/server.properties` (DEV mode).
- RSA keys in the client JAR must match `server.properties`. If login fails silently or the client hangs at login, that's the first thing to check.

---

## 5. Shutting Down

### Gradle `run` foreground

`Ctrl+C` — Gradle traps SIGINT and passes it through. If it gets stuck, a second `Ctrl+C` forces Gradle to kill the daemon.

### installDist script

`Ctrl+C` in the terminal, or:

```bash
# Find the running process
lsof -i :43594
# Or by main class
pgrep -f 'boot.Boot'

kill <pid>         # graceful (SIGTERM)
kill -9 <pid>      # force (only if SIGTERM is ignored)
```

### Stale port (`Address already in use`)

```bash
lsof -i :43594                     # get the PID
kill -9 <pid>
```

Also check port `9292` if the world list Ktor service complains.

---

## 6. Logs

| Location | Contents |
|---|---|
| Console (stdout) | Live logback output — boot sequence, INFO/WARN/ERROR, tick warnings |
| `server/server-run.log` | stdout capture from prior runs (if launched via wrapper scripts) |
| `server/server-run.err` | stderr capture from prior runs — **first place to look after a crash** |
| `server/data/runtime/logs/` | Runtime game-event logs (trade, PvP, drops, etc.) |
| `server/logging/`, `logging-sentry/` | Logback + Sentry configuration modules |

Log config is chosen by the `log_config` property in `server.properties` (default `dev.xml`). Switch to `prod.xml` for LIVE stage.

### Useful filters

```bash
# Just errors & warns from a live tail
tail -f server-run.log | grep -E 'ERROR|WARN'

# Stack trace context from the err file
grep -A 20 'Exception' server-run.err | less
```

---

## 7. Debugging

### 7.1 Remote debugger (recommended)

Attach IntelliJ / VSCode / `jdb` to a running server via JDWP.

**Launch with debug agent:**

```bash
# Edit kronos-boot/build.gradle.kts application block, OR pass via CLI:
./gradlew :kronos-boot:run \
  -Dorg.gradle.jvmargs="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
```

For the installed distribution:

```bash
JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005" \
  ./kronos-boot/build/install/kronos-boot/bin/kronos-boot
```

- `suspend=n` — JVM boots without waiting for the debugger to attach. Flip to `suspend=y` when debugging startup code.
- `address=*:5005` — listens on all interfaces. Use `127.0.0.1:5005` if you want to be stricter.

**Attach from IntelliJ:** Run → Edit Configurations → `+` → Remote JVM Debug → Host `localhost`, Port `5005` → select the `kronos-server` module for source lookups.

### 7.2 Debugging tick stalls / freezes

The main game loop runs in `io.ruin.process.CoreWorker` at 600ms ticks. Symptoms of a stalled tick: players lagging, NPCs frozen, no new log lines for >600ms.

1. **Thread dump** — cheapest first step:
   ```bash
   jps                            # find the kronos-boot PID
   jstack <pid> > /tmp/dump.txt
   grep -A 30 '"main"' /tmp/dump.txt
   ```
   Look for what the `main` thread (or the CoreWorker thread) is parked on. A thread blocked in content code for >1 tick is your culprit.

2. **Repeat jstacks** 3×, 1s apart. A thread that appears in the *same* frame across dumps is actively stuck, not just passing through.

3. **Async profiler** for sustained-but-not-frozen lag:
   ```bash
   # https://github.com/async-profiler/async-profiler
   asprof -d 30 -f /tmp/profile.html <pid>
   ```

### 7.3 Debugging packet / networking issues

RSProt lives in `kronos-server` under `io.ruin.rsprot/`. For protocol-level debugging:

- Toggle `rsprot_dev=true` in `server.properties` (already on for DEV) — enables verbose packet logging inside RSProt.
- Protocol version mismatches between the client build and `ClientProt184.java` / `ClientProt204.java` show up as "unknown opcode" warnings. Check that the client JAR's baked-in revision matches what the server expects.
- Use **RSProx** ([github.com/blurite/rsprox](https://github.com/blurite/rsprox)) to intercept traffic between a stock OSRS client and your server when comparing against Jagex-authentic behavior.

### 7.4 Debugging cache / definition loading

Custom cache content is packed by `tool-cache-packer` (Rust) reading `data/cache/toml/` during `installDist`. If new items/NPCs/interfaces aren't appearing in-game:

```bash
./gradlew :kronos-boot:build_cache    # re-pack the cache standalone
```

Watch for cache packer errors *before* the JVM even starts. If the pack fails, the server will still boot with a stale cache — and that's often the source of "my new item isn't showing up" reports.

### 7.5 Database debugging (if enabled)

If `game_db_enabled=true` or `mongo_enabled=true`:

```bash
# Start local DBs
docker compose -f docker-compose.yml -f docker-compose-local.yml \
  --env-file docker-compose.env up -d

# Admin UIs
open http://localhost:8080    # Adminer (MariaDB)
open http://localhost:8081    # Mongo Express
```

Shut them down:

```bash
docker compose -f docker-compose.yml -f docker-compose-local.yml \
  --env-file docker-compose.env down
```

Flat file saves live at `server/data/runtime/saves/` — safe to `cat` / diff for player state inspection in DEV.

---

## 8. Common Failure Modes

| Symptom | Diagnosis | Fix |
|---|---|---|
| `UnsupportedClassVersionError` or "preview features not enabled" | `JAVA_HOME` is not 24 | `export JAVA_HOME=$(/usr/libexec/java_home -v 24)` and rerun |
| `Address already in use: bind` on port 43594 | Prior run didn't shut down | `lsof -i :43594` → `kill -9 <pid>` |
| Gradle sits at `99% EXECUTING` forever | **Not a hang** — `:kronos-boot:run` keeps the JVM alive | Look at the console *above* the progress bar for log output |
| Client connects then immediately disconnects | RSA key mismatch between client JAR and `server.properties` | Use `client-localhost.jar` shipped with the repo, or rebuild with matching keys |
| "Unknown opcode" WARNs flood the console | Protocol version drift between client and `ClientProt184/204.java` | Verify client revision; update RSProt handlers |
| New TOML cache content not appearing in-game | Cache pack silently failed, or skipped | `./gradlew :kronos-boot:build_cache` and read its output carefully |
| Kotlin incremental compile weirdness | Stale compile cache | `./gradlew clean :kronos-boot:installDist` |
| World list port 9292 conflict | Another Ktor/HTTP service bound | `lsof -i :9292` → kill or reconfigure |
| Tests unexpectedly skipped | By design — disabled by default | `./gradlew test` to force, or `:kronos-server:test` for one module |

---

## 9. Quick Command Reference

```bash
# === Environment ===
export JAVA_HOME=$(/usr/libexec/java_home -v 24)
export PATH="$JAVA_HOME/bin:$PATH"
java -version

# === Build & run ===
cd ~/Documents/treason/server
./gradlew :kronos-boot:run                          # build + run
./gradlew :kronos-boot:installDist                  # build distribution
./kronos-boot/build/install/kronos-boot/bin/kronos-boot   # run distribution
./gradlew :kronos-boot:run_hotswap                  # JRebel hotswap
./gradlew clean :kronos-boot:installDist            # clean rebuild

# === Cache / tests ===
./gradlew :kronos-boot:build_cache                  # re-pack custom cache
./gradlew test                                      # run all tests
./gradlew :kronos-server:test                       # run one module's tests

# === Client ===
cd ~/Documents/treason/client
java -ea --add-opens=java.base/java.lang=ALL-UNNAMED -jar client-localhost.jar

# === Databases (optional) ===
docker compose -f docker-compose.yml -f docker-compose-local.yml --env-file docker-compose.env up -d
docker compose -f docker-compose.yml -f docker-compose-local.yml --env-file docker-compose.env down

# === Process management ===
lsof -i :43594                 # find the game port owner
lsof -i :9292                  # find the world list owner
pgrep -f 'boot.Boot'           # find the server PID
jps                            # list all JVMs + main classes
jstack <pid> > /tmp/dump.txt   # thread dump

# === Remote debugger ===
# Add to JAVA_OPTS or pass via -Dorg.gradle.jvmargs:
-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
```

---

## 10. See Also

- [`README.md`](./README.md) — project overview, module breakdown, architecture
- [`server.properties`](./server.properties) — runtime configuration
- `data/runtime/` — ephemeral runtime state (saves, logs, bans)
- [RSProt](https://github.com/blurite/rsprot) — networking library
- [RSProx](https://github.com/blurite/rsprox) — packet interception proxy
