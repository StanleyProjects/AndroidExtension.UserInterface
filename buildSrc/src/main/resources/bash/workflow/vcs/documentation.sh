#!/bin/bash

echo "vcs documentation start..."

CODE=0

VERSION="$(cat ${ASSEMBLY_PATH}/common.json | jq -r .version)"
if test -z "$VERSION"; then
 echo "Version is empty!"
 exit 11
fi

REPOSITORY="$HOME/gh-pages"
mkdir "${REPOSITORY}" || exit 1 # todo
git clone --depth=1 --branch=gh-pages \
 https://$GITHUB_PAT@github.com/$GITHUB_OWNER/$GITHUB_REPO.git "${REPOSITORY}" || exit 1 # todo
WORKER_NAME="$(cat ${ASSEMBLY_PATH}/vcs/worker.json | jq -r .name)"
if test -z "$WORKER_NAME"; then
 echo "Worker name is empty!"
 exit 31
fi
WORKER_EMAIL="$(cat ${ASSEMBLY_PATH}/vcs/worker.json | jq -r .email)"
if test -z "$WORKER_EMAIL"; then
 echo "Worker email is empty!"
 exit 32
fi
if test -z "$GITHUB_RUN_NUMBER"; then
 echo "GitHub run number is empty!"
 exit 33
fi

mkdir -p "${REPOSITORY}/documentation/$VERSION"
cp -r ${ASSEMBLY_PATH}/documentation/* "${REPOSITORY}/documentation/$VERSION"

git -C "${REPOSITORY}" config user.name "$WORKER_NAME" && \
 git -C "${REPOSITORY}" config user.email "$WORKER_EMAIL" && \
 git -C "${REPOSITORY}" add --all "${REPOSITORY}" && \
 git -C "${REPOSITORY}" commit -m \
  "GitHub build #$GITHUB_RUN_NUMBER | $WORKER_NAME added documentation for version $VERSION." && \
 git -C "${REPOSITORY}" push -f; CODE=$?
if test $CODE -ne 0; then
 echo "Git push failed!"
 exit 41
fi

echo "vcs documentation success"

exit 0
