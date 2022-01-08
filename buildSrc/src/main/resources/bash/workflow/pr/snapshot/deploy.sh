#!/bin/bash

echo "snapshot deploy start..."

CODE=0

VERSION="$(cat ${ASSEMBLY_PATH}/common.json | jq -r .version)"
WORKER_NAME="$(cat $ASSEMBLY_PATH/vcs/worker.json | jq -r .name)"
WORKER_EMAIL="$(cat $ASSEMBLY_PATH/vcs/worker.json | jq -r .email)"

for it in WORKER_NAME WORKER_EMAIL VERSION; do
 if test -z "${!it}"; then echo "$it is empty!"; exit 11; fi; done

TAG="$VERSION-SNAPSHOT"

/bin/bash $RESOURCES_PATH/bash/workflow/vcs/tag_test.sh "$TAG"; CODE=$?
if test $CODE -ne 0; then
 echo "Test of $TAG failed!"; exit 21
fi

/bin/bash $RESOURCES_PATH/bash/workflow/maven/deploy/snapshot.sh; CODE=$?
if test $CODE -ne 0; then
 echo "Maven deploy $TAG failed!"; exit 21
fi

git config user.name "$WORKER_NAME" && \
 git config user.email "$WORKER_EMAIL" && \
 git commit -m "Merge $GIT_SOURCE_BRANCH -> $PR_SOURCE_BRANCH by $WORKER_NAME." && \
 git push; CODE=$?
if test $CODE -ne 0; then
 echo "Git push failed!"
 exit 41
fi

/bin/bash $RESOURCES_PATH/bash/workflow/pr/snapshot/github_release.sh; CODE=$?
if test $CODE -ne 0; then
 echo "Github release $TAG failed!"; exit 31
fi

echo "snapshot deploy success"

exit 0
