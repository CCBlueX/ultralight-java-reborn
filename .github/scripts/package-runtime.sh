#!/usr/bin/env bash
# Packs the Ultralight runtime that LiquidBounce downloads for its players: the shared libraries,
# the resources, and the EULA and notices the Ultralight license requires us to pass on.
#
# usage: package-runtime.sh <ultralight-sdk.7z> <output.tar.gz>
set -euo pipefail

sdk=$(realpath "$1")
out=$(realpath -m "$2")
work=$(mktemp -d)
trap 'rm -rf "$work"' EXIT

cmake -E chdir "$work" cmake -E tar xf "$sdk"
sdk_root="$work"
# Archives may wrap everything in a single top-level folder
if [ ! -d "$sdk_root/bin" ]; then
    sdk_root=$(find "$work" -mindepth 2 -maxdepth 2 -type d -name bin -printf '%h\n' | head -n1)
fi

pkg="$work/package"
mkdir -p "$pkg/bin" "$pkg/license"
find "$sdk_root/bin" -maxdepth 1 -type f \( -name '*.so*' -o -name '*.dylib' -o -name '*.dll' \) -exec cp {} "$pkg/bin/" \;
cp -r "$sdk_root/resources" "$pkg/resources"
cp "$sdk_root/license/EULA.txt" "$sdk_root/license/NOTICES.md" "$pkg/license/"

tar --sort=name --mtime='2025-04-15 00:00Z' --owner=0 --group=0 --numeric-owner --mode='u+rwX,go+rX,go-w' \
    -C "$pkg" -cf - bin resources license | gzip -n -9 > "$out"
