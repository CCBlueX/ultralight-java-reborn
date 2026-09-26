#!/usr/bin/env bash
# Downloads a file of Ultralight from our storage and checks it against the checksum the storage reports.
#
# usage: download-ultralight.sh <ultralight-sdk|ultralight> <ident> <output>
set -euo pipefail

url="$RESOURCE_API/$1/$ULTRALIGHT_VERSION/$2"
mkdir -p "$(dirname "$3")"
curl -fsSL --retry 3 -o "$3" "$url"

expected=$(curl -fsS --retry 3 "$url/checksum")
if command -v sha256sum > /dev/null; then
    actual=$(sha256sum "$3" | cut -d' ' -f1)
else
    actual=$(shasum -a 256 "$3" | cut -d' ' -f1)
fi

if [ "$expected" != "$actual" ]; then
    echo "Checksum mismatch for $url: expected $expected, got $actual"
    exit 1
fi
