#!/bin/bash

echo "tag..."

if test $# -ne 1; then
 echo "Script needs for 1 argument but actual $#"
 exit 11
fi

TAG=$1

if test -z "$TAG"; then
 echo "Tag is empty!"
 exit 12
fi

CODE=0

if test -z "$GIT_SOURCE_BRANCH"; then
 echo "Git source branch is empty!"
 exit 34
fi

REPOSITORY="$HOME/tag"
mkdir $REPOSITORY || exit 1 # todo
git clone --depth=1 --branch=$GIT_SOURCE_BRANCH \
 https://$GITHUB_PAT@github.com/$GITHUB_OWNER/${GITHUB_REPO}.git $REPOSITORY || exit 1 # todo

WORKER_NAME="$(cat $ASSEMBLY_PATH/vcs/worker.json | jq -r .name)"
if test -z "$WORKER_NAME"; then
 echo "Worker name is empty!"
 exit 31
fi
WORKER_EMAIL="$(cat $ASSEMBLY_PATH/vcs/worker.json | jq -r .email)"
if test -z "$WORKER_EMAIL"; then
 echo "Worker email is empty!"
 exit 32
fi

git -C $REPOSITORY config user.name "$WORKER_NAME" && \
 git -C $REPOSITORY config user.email "$WORKER_EMAIL" && \
 git -C $REPOSITORY tag $TAG && \
 git -C $REPOSITORY push --tag; CODE=$?
if test $CODE -ne 0; then
 echo "Git push failed!"
 exit 41
fi

echo "tag $TAG success"

exit 0
