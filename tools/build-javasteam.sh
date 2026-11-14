#!/usr/bin/env bash
# Builds JavaSteam (https://github.com/joshuatam/JavaSteam), the Steam network library that also
# implements the Steam sign-in, from source at a pinned commit and places the two jars where
# app/build.gradle.kts picks them up.
#
# The commit is fetched by its full SHA, so git itself guarantees the source is exactly the
# reviewed revision. To move to a newer JavaSteam, change JAVASTEAM_COMMIT and mirror any
# dependency version changes in the javasteam-* entries of gradle/libs.versions.toml.
#
# Usage: tools/build-javasteam.sh [extra gradle arguments]
#   JAVASTEAM_SRC_DIR  where the source is checked out (default: build/javasteam)
set -euo pipefail

JAVASTEAM_URL="https://github.com/joshuatam/JavaSteam.git"
# Branch gamenative-latest, version 1.8.0.1-26-SNAPSHOT.
JAVASTEAM_COMMIT="433f2ad15c36d5e690a4fe77401ec3f6b960641e"

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
SRC_DIR="${JAVASTEAM_SRC_DIR:-$REPO_ROOT/build/javasteam}"
OUT_DIR="$REPO_ROOT/app/libs/javasteam"

if [ ! -d "$SRC_DIR/.git" ]; then
    git init --quiet "$SRC_DIR"
    git -C "$SRC_DIR" remote add origin "$JAVASTEAM_URL"
fi
if [ "$(git -C "$SRC_DIR" rev-parse --quiet --verify HEAD 2>/dev/null || true)" != "$JAVASTEAM_COMMIT" ]; then
    git -C "$SRC_DIR" fetch --quiet --depth 1 origin "$JAVASTEAM_COMMIT"
    git -C "$SRC_DIR" checkout --quiet --detach FETCH_HEAD
fi
if [ "$(git -C "$SRC_DIR" rev-parse HEAD)" != "$JAVASTEAM_COMMIT" ]; then
    echo "error: $SRC_DIR is not at JavaSteam commit $JAVASTEAM_COMMIT" >&2
    exit 1
fi
if [ -n "$(git -C "$SRC_DIR" status --porcelain --untracked-files=no)" ]; then
    echo "error: $SRC_DIR has local modifications; refusing to build from a modified tree" >&2
    exit 1
fi

(cd "$SRC_DIR" && ./gradlew --no-daemon --console=plain "$@" :jar :javasteam-depotdownloader:jar)

rm -rf "$OUT_DIR"
mkdir -p "$OUT_DIR"
cp "$SRC_DIR"/build/libs/javasteam-*.jar \
   "$SRC_DIR"/javasteam-depotdownloader/build/libs/javasteam-depotdownloader-*.jar \
   "$OUT_DIR/"
echo "JavaSteam $JAVASTEAM_COMMIT:"
ls -l "$OUT_DIR"
