#!/bin/bash
# Reason client launcher (prebuilt jar)
# Requires: JDK 11+ on PATH, server running on 127.0.0.1:43594

set -e

# --- Preflight: warn if the server isn't reachable ---
if ! (echo > /dev/tcp/127.0.0.1/43594) 2>/dev/null; then
  echo "WARNING: No server detected on 127.0.0.1:43594"
  echo "  The client will load but you won't be able to log in."
  echo "  Start the server first:  cd ../server && ./gradlew :kronos-boot:run"
  echo ""
fi

java -ea \
  --add-opens=java.base/java.lang=ALL-UNNAMED \
  --add-exports=java.desktop/com.apple.eawt=ALL-UNNAMED \
  -jar client-localhost.jar \
  --debug --developer-mode
