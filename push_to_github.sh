#!/usr/bin/env bash
set -euo pipefail

if ! command -v git >/dev/null 2>&1; then
  echo "git command not found" >&2
  exit 1
fi

if [ $# -lt 2 ]; then
  cat <<USAGE >&2
Usage: $0 <github-username-or-org>/<repo> <commit-message> [branch]

Example:
  $0 my-user/my-tp-repo "Initial TP import" main

The script assumes you have already created the remote repository on GitHub
and configured authentication via SSH or a Git credential helper.
USAGE
  exit 2
fi

REMOTE_SLUG="$1"
COMMIT_MESSAGE="$2"
BRANCH="${3:-main}"

if git status --short | grep -q '^'; then
  echo "Committing local changes with message: $COMMIT_MESSAGE"
  git add -A
  git commit -m "$COMMIT_MESSAGE"
else
  echo "No local changes detected. Skipping commit step."
fi

if ! git remote get-url origin >/dev/null 2>&1; then
  echo "Setting GitHub remote: git@github.com:${REMOTE_SLUG}.git"
  git remote add origin "git@github.com:${REMOTE_SLUG}.git"
fi

CURRENT_BRANCH=$(git symbolic-ref --short HEAD)
if [ "$CURRENT_BRANCH" != "$BRANCH" ]; then
  echo "Creating or switching to branch '$BRANCH'"
  git checkout -B "$BRANCH"
fi

echo "Pushing branch '$BRANCH' to origin"
git push -u origin "$BRANCH"
