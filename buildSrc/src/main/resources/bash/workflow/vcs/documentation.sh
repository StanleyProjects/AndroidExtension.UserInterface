#!/bin/bash

echo "vcs documentation start..."

VERSION="$(cat ${ASSEMBLY_PATH}/common.json | jq -r .version)"
WORKER_NAME="$(cat ${ASSEMBLY_PATH}/vcs/worker.json | jq -r .name)"
WORKER_EMAIL="$(cat ${ASSEMBLY_PATH}/vcs/worker.json | jq -r .email)"

for it in VERSION WORKER_NAME WORKER_EMAIL; do
 if test -z "${!it}"; then echo "$it is empty!"; exit 11; fi; done

/bin/bash $RESOURCES_PATH/bash/util/check_variables.sh \
 GITHUB_PAT GITHUB_OWNER GITHUB_REPO GITHUB_RUN_NUMBER || exit 12 # todo

REPOSITORY="$HOME/gh-pages"
mkdir $REPOSITORY || exit 1 # todo
git clone --depth=1 --branch=gh-pages \
 https://$GITHUB_PAT@github.com/$GITHUB_OWNER/$GITHUB_REPO.git $REPOSITORY || exit 1 # todo

RELATIVE_PATH="$REPOSITORY/documentation/$VERSION"
if [ -d "$RELATIVE_PATH" ]; then
 echo "Directory $RELATIVE_PATH already exists!"
 exit 101
fi
mkdir -p "$RELATIVE_PATH" || exit 1 # todo
cp -r ${ASSEMBLY_PATH}/documentation/* "$RELATIVE_PATH"

CODE=0
COMMIT_MESSAGE="GitHub build #$GITHUB_RUN_NUMBER | $WORKER_NAME added documentation for version ${VERSION}."
git -C $REPOSITORY config user.name "$WORKER_NAME" && \
 git -C $REPOSITORY config user.email "$WORKER_EMAIL" && \
 git -C $REPOSITORY add --all $REPOSITORY && \
 git -C $REPOSITORY commit -m "$COMMIT_MESSAGE" && \
 git -C $REPOSITORY tag "documentation/$VERSION" && \
 git -C $REPOSITORY push -f && \
 git -C $REPOSITORY push --tag; CODE=$?
if test $CODE -ne 0; then
 echo "Git push failed!"
 exit 21
fi

echo "vcs documentation success"

exit 0
