#!/bin/bash

echo "snapshot deploy start..."

CODE=0

VERSION="$(cat ${ASSEMBLY_PATH}/common.json | jq -r .version)"
if test -z "$VERSION"; then
 echo "Version is empty!"; exit 11
fi

TAG="$VERSION-SNAPSHOT"

/bin/bash $RESOURCES_PATH/bash/workflow/vcs/tag_test.sh "$TAG"; CODE=$?
if test $CODE -ne 0; then
 echo "Test of $TAG failed!"; exit 21
fi

/bin/bash $RESOURCES_PATH/bash/workflow/maven/deploy/snapshot.sh; CODE=$?
if test $CODE -ne 0; then
 echo "Maven deploy $TAG failed!"; exit 21
fi

/bin/bash $RESOURCES_PATH/bash/workflow/pr/snapshot/github_release.sh; CODE=$?
if test $CODE -ne 0; then
 echo "Github release $TAG failed!"; exit 31
fi

echo "snapshot deploy success"

exit 0
