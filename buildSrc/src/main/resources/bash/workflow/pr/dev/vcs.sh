#!/bin/bash

echo "dev vcs start..."

CODE=0

VERSION="$(cat ${ASSEMBLY_PATH}/common.json | jq -r .version)"
if test -z "$VERSION"; then
 echo "Version is empty!"
 exit 11
fi

TAG="intermediate/$VERSION"

/bin/bash $RESOURCES_PATH/bash/workflow/vcs/tag_test.sh "$TAG"; CODE=$?

if test $CODE -ne 0; then
 echo "Test of $TAG failed!"
 exit 21
fi

PATH="/gh-pages"
mkdir "${PATH}"
git clone -q --depth=1 --branch=gh-pages https://$GITHUB_PAT@github.com/$GITHUB_OWNER/$GITHUB_REPO.git "${PATH}"
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
if test -z "$GIT_COMMIT_SHA"; then
 echo "GitHub run number is empty!"
 exit 34
fi

mkdir -p "${PATH}/documentation/$VERSION"
cp -r ${ASSEMBLY_PATH}/documentation/* "${PATH}/documentation/$VERSION"

git -C "${PATH}" config user.name "$WORKER_NAME" && \
 git -C "${PATH}" config user.email "$WORKER_EMAIL" && \
 git -C "${PATH}" add --all "${PATH}" && \
 git -C "${PATH}" commit -m \
  "GitHub build #$GITHUB_RUN_NUMBER | $WORKER_NAME added documentation for version $VERSION." && \
 git -C "${PATH}" push -f; CODE=$?
if test $CODE -ne 0; then
 echo "Git push failed!"
 exit 41
fi

PRERELESE=true
REQUEST_BODY="{\
\"tag_name\":\"$TAG\",
\"target_commitish\":\"$GIT_COMMIT_SHA\",
\"name\":\"$TAG\",
\"body\":\"GitHub build #$GITHUB_RUN_NUMBER | by ${WORKER_NAME}.\",
\"draft\":false,
\"prerelease\":$PRERELESE
}"
/bin/bash $RESOURCES_PATH/bash/workflow/vcs/tag.sh "$REQUEST_BODY"; CODE=$?

if test $CODE -ne 0; then
 echo "Tag $TAG failed!"
 exit 22
fi

echo "dev vcs success"

exit 0
