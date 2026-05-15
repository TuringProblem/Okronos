#!/usr/bin/env bash
# Kronos / Reason RSPS — unified launcher
#
#   ./start.sh                  start server + client
#   ./start.sh --server-only    start server, skip client
#   ./start.sh --client-only    connect client (server must already be running)
#   ./start.sh --stop           kill any running server and client

set -uo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SERVER_DIR="$SCRIPT_DIR/server"
CLIENT_DIR="$SCRIPT_DIR/client"

GAME_PORT=43594
WORLD_PORT=9292
SERVER_HOST="127.0.0.1"
BOOT_TIMEOUT=120

SERVER_PID=""
CLIENT_PID=""
TAIL_PID=""
STOPPING=false

# ── Colors ────────────────────────────────────────────────────────────────────
RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'
CYAN='\033[0;36m'; BOLD='\033[1m'; DIM='\033[2m'; NC='\033[0m'

ok()      { echo -e "  ${GREEN}[OK]${NC}  $*"; }
fail()    { echo -e "  ${RED}[FAIL]${NC} $*" >&2; exit 1; }
warn()    { echo -e "  ${YELLOW}[!]${NC}   $*"; }
info()    { echo -e "  ${CYAN}[--]${NC}  $*"; }
section() { echo -e "\n${BOLD}=== $* ===${NC}"; }

# ── Cleanup on exit / Ctrl-C ──────────────────────────────────────────────────
cleanup() {
  $STOPPING && return
  STOPPING=true
  echo ""
  section "Shutting down"
  [ -n "$TAIL_PID" ] && kill "$TAIL_PID" 2>/dev/null || true
  if [ -n "$CLIENT_PID" ] && kill -0 "$CLIENT_PID" 2>/dev/null; then
    info "Stopping client (PID $CLIENT_PID)"
    kill "$CLIENT_PID" 2>/dev/null || true
  fi
  # Find server JVM by port — more reliable than tracking the gradlew wrapper PID
  local sjvm; sjvm=$(lsof -ti :"$GAME_PORT" 2>/dev/null | head -1)
  if [ -n "$sjvm" ]; then
    info "Stopping server JVM (PID $sjvm)"
    kill "$sjvm" 2>/dev/null || true
  fi
  [ -n "$SERVER_PID" ] && kill "$SERVER_PID" 2>/dev/null || true
  ok "All stopped"
}
trap cleanup EXIT INT TERM

# ── Helpers ───────────────────────────────────────────────────────────────────
port_open() { (echo > /dev/tcp/"$SERVER_HOST"/"$1") 2>/dev/null; }

kill_port() {
  local pid; pid=$(lsof -ti :"$1" 2>/dev/null | head -1)
  [ -z "$pid" ] && return 0
  warn "Killing existing process on :$1 (PID $pid)"
  kill -9 "$pid" 2>/dev/null
  sleep 1
}

wait_for_port() {
  local port=$1 label=$2 elapsed=0
  printf "  Waiting for %s" "$label"
  while ! port_open "$port"; do
    sleep 2; elapsed=$((elapsed + 2)); printf "."
    if [ "$elapsed" -ge "$BOOT_TIMEOUT" ]; then
      printf "\n"
      fail "Timed out (${BOOT_TIMEOUT}s) — check $SERVER_DIR/server-run.err"
    fi
    # If gradlew died before the port came up, something went wrong
    if [ -n "$SERVER_PID" ] && ! kill -0 "$SERVER_PID" 2>/dev/null; then
      printf "\n"
      fail "Server process exited unexpectedly — check $SERVER_DIR/server-run.err"
    fi
  done
  printf "\n"; ok "$label ready"
}

# Locate JDK 24 — required by the server (uses --enable-preview + jvmToolchain(24))
find_java24() {
  # 1. Explicit override
  [ -n "${JAVA24_HOME:-}" ] && echo "$JAVA24_HOME" && return
  # 2. macOS helper
  if command -v /usr/libexec/java_home &>/dev/null; then
    local h; h=$(/usr/libexec/java_home -v 24 2>/dev/null) && echo "$h" && return
  fi
  # 3. Common install paths
  for d in \
    "$HOME/Library/Java/JavaVirtualMachines/jdk-24+36.jdk/Contents/Home" \
    "/Library/Java/JavaVirtualMachines/jdk-24.jdk/Contents/Home" \
    "/usr/lib/jvm/java-24-openjdk-amd64"; do
    [ -d "$d" ] && echo "$d" && return
  done
  echo ""
}

# ── Arg parsing ───────────────────────────────────────────────────────────────
MODE="all"
for arg in "$@"; do
  case $arg in
    --server-only) MODE="server" ;;
    --client-only) MODE="client" ;;
    --stop)
      echo -e "\n${BOLD}Stopping Kronos...${NC}"
      kill_port $GAME_PORT; kill_port $WORLD_PORT
      ok "Done"; STOPPING=true; exit 0 ;;
    --help|-h)
      echo ""
      echo -e "${BOLD}Kronos RSPS Launcher${NC}"
      echo ""
      echo "  Usage: $(basename "$0") [options]"
      echo ""
      echo "  (no args)       start server + client"
      echo "  --server-only   start server only"
      echo "  --client-only   connect client (server must already be running)"
      echo "  --stop          kill running server and client"
      echo "  --help          show this message"
      echo ""
      echo "  Override JDK path:  JAVA24_HOME=/path/to/jdk24 ./start.sh"
      echo ""
      STOPPING=true; exit 0 ;;
    *) warn "Ignoring unknown argument: $arg" ;;
  esac
done

# ── Sanity checks ─────────────────────────────────────────────────────────────
[ -d "$SERVER_DIR" ] || fail "server/ not found — run this script from the repo root"
[ -d "$CLIENT_DIR" ] || fail "client/ not found — run this script from the repo root"

# ── Banner ────────────────────────────────────────────────────────────────────
echo ""
echo -e "${BOLD}  Kronos / Reason RSPS${NC}  ${DIM}(mode: $MODE)${NC}"
echo ""

# ── Server ────────────────────────────────────────────────────────────────────
if [ "$MODE" != "client" ]; then
  section "Server"

  JAVA24="$(find_java24)"
  [ -z "$JAVA24" ] && fail "JDK 24 not found. Install Temurin 24 → https://adoptium.net/temurin/releases/?version=24"
  ok "JDK 24: $JAVA24"

  if port_open $GAME_PORT; then
    warn "Port $GAME_PORT is already in use"
    printf "  Kill the existing server and restart? [y/N] "
    read -r ans </dev/tty
    [[ $ans =~ ^[Yy]$ ]] || fail "Aborted — server appears to already be running"
    kill_port $GAME_PORT
    kill_port $WORLD_PORT
  fi

  # Truncate logs from previous run
  : > "$SERVER_DIR/server-run.log"
  : > "$SERVER_DIR/server-run.err"

  info "Starting server (logs → server/server-run.log) ..."
  (
    export JAVA_HOME="$JAVA24"
    export PATH="$JAVA_HOME/bin:$PATH"
    cd "$SERVER_DIR"
    exec ./gradlew :kronos-boot:run
  ) >> "$SERVER_DIR/server-run.log" 2>> "$SERVER_DIR/server-run.err" &
  SERVER_PID=$!
  ok "Gradle launched (PID $SERVER_PID)"

  wait_for_port $GAME_PORT "game server  [:$GAME_PORT]"
  wait_for_port $WORLD_PORT "world list   [:$WORLD_PORT]"
fi

# ── Client ────────────────────────────────────────────────────────────────────
if [ "$MODE" != "server" ]; then
  section "Client"

  port_open $GAME_PORT  || fail "Game server not reachable on :$GAME_PORT — start the server first"
  port_open $WORLD_PORT || fail "World list not reachable on :$WORLD_PORT — start the server first"

  [ -f "$CLIENT_DIR/client-localhost.jar" ] || fail "client-localhost.jar not found in $CLIENT_DIR"

  # Clear stale JS5 cache — CRC mismatches cause a silent 30s login timeout
  CACHE_DIR="$HOME/jagexcache/oldschool/LIVE"
  if [ -d "$CACHE_DIR" ]; then
    info "Clearing stale JS5 cache ($CACHE_DIR) ..."
    rm -rf "$CACHE_DIR"
    ok "Cache cleared"
  fi
  JAGEX_PREFS="$HOME/.runelite/jagex_cl_oldschool_LIVE.dat"
  [ -f "$JAGEX_PREFS" ] && rm -f "$JAGEX_PREFS" && ok "Cleared stale session prefs"

  LOGIN_PW=$(grep '^login_master_password=' "$SERVER_DIR/server.properties" 2>/dev/null | cut -d= -f2 || echo "see server.properties")
  info "Launching client (first JS5 sync: ~15-30s) ..."
  echo -e "  ${DIM}Login: any username  /  password: ${LOGIN_PW}${NC}"

  (
    cd "$CLIENT_DIR"
    exec java -ea \
      --add-opens=java.base/java.lang=ALL-UNNAMED \
      --add-exports=java.desktop/com.apple.eawt=ALL-UNNAMED \
      -Djava.net.preferIPv4Stack=true \
      -jar client-localhost.jar \
      --debug --developer-mode
  ) >> "$CLIENT_DIR/client-run.log" 2>> "$CLIENT_DIR/client-run.err" &
  CLIENT_PID=$!
  ok "Client launched (PID $CLIENT_PID)"
fi

# ── Keep-alive — stream server logs and wait ──────────────────────────────────
section "Running"
info "Server logs streaming below — press Ctrl+C to stop everything"
echo ""

if [ "$MODE" != "client" ]; then
  tail -n 0 -f "$SERVER_DIR/server-run.log" &
  TAIL_PID=$!
  wait "$SERVER_PID" 2>/dev/null || true
else
  wait "$CLIENT_PID" 2>/dev/null || true
fi
