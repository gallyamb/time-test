#!/usr/bin/env bash
set -euo pipefail
: "${RELEASE_TAG:?RELEASE_TAG is required}"
: "${GITHUB_ENV:?GITHUB_ENV is required}"
[[ "$RELEASE_TAG" =~ ^v[0-9]+\.[0-9]+\.[0-9]+(-[A-Za-z0-9.-]+)?$ ]]
test "$(git rev-parse HEAD)" = "$(git rev-parse --verify "refs/tags/$RELEASE_TAG^{commit}")"
printf 'RELEASE_TAG=%s\nRELEASE_VERSION=%s\n' "$RELEASE_TAG" "${RELEASE_TAG#v}" >> "$GITHUB_ENV"
