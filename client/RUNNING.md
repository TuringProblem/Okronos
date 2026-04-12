# Running & Debugging the Reason Client

Operational guide for launching, iterating on, and debugging the Reason (RuneLite-based) client against a local Kronos server. For project layout and upstream docs, see [`README.md`](./README.md).

> **Server must be running first.** The client connects to `127.0.0.1:43594` (game) and `127.0.0.1:9292` (world list). If the server isn't up, the client will open but show a "connection error" on the login screen. See [`../server/RUNNING.md`](../server/RUNNING.md) to start the server.

---

## 1. Prerequisites

| Requirement | Version | Notes |
|---|---|---|
| **JDK (run prebuilt jar)** | **11+** (17 or 21 recommended) | Any modern JDK can run `client-localhost.jar`. |
| **JDK (source build)** | **11+** | `pom.xml` targets Java 11 bytecode. JDK 17/21/24 all compile fine. |
| Maven | wrapper-managed (`./mvnw`) | Do not install system Maven — always use the wrapper. |
| Free ports on the **server** | `43594`, `9292` | Client reaches the server on these — if it can't, login will fail. |

### Verify a JDK is on PATH

```bash
java -version
# Anything >= 11 works. 17/21 are the tested paths.
```

---

## 2. Running the Client

### 2.1 Prebuilt jar (fastest — just play)

```bash
java -ea \
  --add-opens=java.base/java.lang=ALL-UNNAMED \
  --add-exports=java.desktop/com.apple.eawt=ALL-UNNAMED \
  -jar client-localhost.jar
```

- `client-localhost.jar` is the shipped, pre-injected client wired to `127.0.0.1`.
- `--add-opens` and `--add-exports` silence JDK 17+ module access warnings. Mandatory on JDK 17+.
- The jar's baked-in RSA public key must match the server's RSA keys in `server/server.properties`. If login hangs or silently fails, that mismatch is the first thing to check.

### 2.2 Maven source run (iteration on client/plugin code)

```bash
./start.sh
```

This compiles and runs `runelite-client` via Maven. It also checks whether the server is reachable before launching.

Equivalent to:

```bash
MAVEN_OPTS="-ea \
  --add-opens=java.base/java.lang=ALL-UNNAMED \
  --add-exports=java.desktop/com.apple.eawt=ALL-UNNAMED" \
  ./mvnw -pl runelite-client compile exec:java \
  -Dexec.mainClass="net.runelite.client.RuneLite" \
  -Dexec.arguments="--debug,--developer-mode"
```

Full clean + rebuild (useful after invasive refactors or when target/ is stale):

```bash
./mvnw clean install -DskipTests
./start.sh
```

---

## 3. Logging In

1. Client window opens and the login screen renders.
2. Enter **any username** (DEV mode auto-creates accounts).
3. Enter the **master password** from `server/server.properties` (`login_master_password`). Default: `l`.
4. Click **Login**. The server log should print player-join events and you should land in the game world.

---

## 4. Command-line Flags

| Flag | Effect |
|---|---|
| `--debug` | Sets root logger to DEBUG. Essential for diagnosing anything. |
| `--developer-mode` | Enables developer tools plugin (widget inspector, etc.). Forced on in this fork. |
| `--safe-mode` | Disables external plugins and the GPU plugin. |
| `--jav_config=<url>` | Override the jav_config URL (swaps which server the client connects to). |

Pass multiple via `exec:java` as a comma-separated list:

```bash
-Dexec.arguments="--debug,--developer-mode,--safe-mode"
```

---

## 5. Common Problems

| Symptom | Cause | Fix |
|---|---|---|
| Client opens but says "error connecting" / can't log in | **Server not running** | Start the server first: `cd ../server && ./gradlew :kronos-boot:run` |
| `InaccessibleObjectException` on startup | Missing `--add-opens` on JDK 17+ | Use `./start.sh` or add `--add-opens=java.base/java.lang=ALL-UNNAMED` |
| Login button does nothing, server logs a decrypt error | RSA key mismatch | Use shipped `client-localhost.jar`, or match keys to `server.properties` |
| "Unknown opcode" WARNs on the server | Protocol version drift | Client revision must match server's RSProt expectations (currently v231) |
| Maven build fails after `mvn clean` | `runelite-injected.bin` not found | Fixed: pom.xml now copies it from `lib/`. Just run `./start.sh` again. |
| Maven downloads lots of artifacts | First run (or after `--offline` was removed) | Normal. After first successful build, deps are cached locally. |
| World list empty / "error connecting" | Server's Ktor world list not up on 9292 | `curl http://127.0.0.1:9292/worlds.ws` — if empty, restart the server |
| Splash screen hangs | ClientLoader stalled | Delete `~/.reason/.runelite/cache/` and re-run with `--debug` |

---

## 6. Quick Command Reference

```bash
# === Check server is running ===
lsof -i :43594                                  # game port bound?
curl -s http://127.0.0.1:9292/worlds.ws | head  # world list reachable?

# === Run prebuilt jar ===
java -ea --add-opens=java.base/java.lang=ALL-UNNAMED \
  --add-exports=java.desktop/com.apple.eawt=ALL-UNNAMED \
  -jar client-localhost.jar

# === Run from source ===
./start.sh

# === Clean rebuild ===
./mvnw clean install -DskipTests

# === Kill stuck client ===
jps | grep RuneLite
pgrep -f 'net.runelite.client.RuneLite'
kill <pid>

# === Remote debugger (attach IntelliJ on port 5006) ===
MAVEN_OPTS="-ea -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5006 \
  --add-opens=java.base/java.lang=ALL-UNNAMED \
  --add-exports=java.desktop/com.apple.eawt=ALL-UNNAMED" \
  ./mvnw -pl runelite-client compile exec:java \
  -Dexec.mainClass="net.runelite.client.RuneLite" \
  -Dexec.arguments="--debug,--developer-mode"
```

---

## 7. See Also

- [`start.sh`](./start.sh) — canonical Maven-based run script
- [`../server/RUNNING.md`](../server/RUNNING.md) — operational guide for the Kronos server
- [`../server/server.properties`](../server/server.properties) — server config (RSA keys, master password, ports)
- [`runelite-client/src/main/resources/jav_config.ws`](./runelite-client/src/main/resources/jav_config.ws) — client connection config (codebase, world list URL)
