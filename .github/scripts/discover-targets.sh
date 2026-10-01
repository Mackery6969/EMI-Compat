#!/usr/bin/env bash
# Prints the GitHub Actions matrix for every target in gradle/minecraft-versions.properties.
set -euo pipefail

MANIFEST=gradle/minecraft-versions.properties

value() {
  awk -F= -v key="$1" '$1 == key { print $2 }' "$MANIFEST"
}

value supported_versions | tr ',' '\n' | while read -r version; do
  java_version="$(value "version.${version}.java_version")"
  game_versions="$(value "version.${version}.game_versions")"
  if [ -z "$java_version" ] || [ -z "$game_versions" ]; then
    echo "Minecraft ${version} is missing java_version or game_versions in ${MANIFEST}" >&2
    exit 1
  fi
  jq -cn \
    --arg minecraft_version "$version" \
    --arg java_version "$java_version" \
    --arg game_versions "$game_versions" \
    '{minecraft_version: $minecraft_version, java_version: $java_version, game_versions: $game_versions}'
done | jq -sc '{include: .}'
