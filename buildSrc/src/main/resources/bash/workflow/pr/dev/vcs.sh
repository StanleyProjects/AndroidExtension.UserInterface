#!/bin/bash

echo "dev vcs start..."

/bin/bash $RESOURCES_PATH/bash/util/check_variables.sh \
 ASSEMBLY_PATH GIT_COMMIT_SRC GIT_COMMIT_DST GITHUB_RUN_NUMBER || exit 1 # todo

CODE=0

VERSION="$(cat $ASSEMBLY_PATH/common.json | jq -r .version)"

if test -z "$VERSION"; then
 echo "Version is empty!"; exit 11
fi

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

git commit -m \
  "Merge ${GIT_COMMIT_SRC::7} -> ${GIT_COMMIT_DST::7} by GitHub build #${GITHUB_RUN_NUMBER}." && \
 git tag "$TAG" && \
 git push && git push --tag; CODE=$?
if test $CODE -ne 0; then
 echo "Git push failed!"
 exit 41
fi

echo "dev vcs success"

exit 0
