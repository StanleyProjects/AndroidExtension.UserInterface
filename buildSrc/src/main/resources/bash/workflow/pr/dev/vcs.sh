#!/bin/bash

echo "dev vcs start..."

CODE=0

VERSION="$(cat ${ASSEMBLY_PATH}/common.json | jq -r .version)"
WORKER_NAME="$(cat $ASSEMBLY_PATH/vcs/worker.json | jq -r .name)"

for it in WORKER_NAME VERSION; do
 if test -z "${!it}"; then echo "$it is empty!"; exit 11; fi; done

TAG="intermediate/$VERSION"

/bin/bash $RESOURCES_PATH/bash/workflow/vcs/tag_test.sh "$TAG"; CODE=$?
if test $CODE -ne 0; then
 echo "Test of $TAG failed!"
 exit 21
fi

/bin/bash $RESOURCES_PATH/bash/workflow/vcs/documentation.sh; CODE=$?
if test $CODE -ne 0; then
 echo "Git push documentation failed!"
 exit 31
fi

git commit -m "Merge $GIT_COMMIT_SHA -> $PR_SOURCE_BRANCH by $WORKER_NAME." && \
 git tag "$TAG" && \
 git push && git push --tag; CODE=$?
if test $CODE -ne 0; then
 echo "Git push failed!"
 exit 41
fi

echo "dev vcs success"

exit 0
