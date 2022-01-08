#!/bin/bash

echo "GitHub release..."

if test $# -ne 1; then
 echo "Script needs for 1 argument but actual $#"
 exit 11
fi

/bin/bash $RESOURCES_PATH/bash/util/check_variables.sh \
 ASSEMBLY_PATH GITHUB_OWNER GITHUB_REPO GITHUB_PAT PR_SOURCE_BRANCH GIT_COMMIT_SHA GITHUB_RUN_NUMBER || exit 1 # todo

TAG=$1
WORKER_NAME="$(cat ${ASSEMBLY_PATH}/vcs/worker.json | jq -r .name)"

for it in TAG WORKER_NAME; do
 if test -z "${!it}"; then echo "$it is empty!"; exit 11; fi; done

PRERELESE=true
if [ "$PR_SOURCE_BRANCH" == master ]; then
 PRERELESE=false
fi
REQUEST_BODY="{\
\"tag_name\":\"$TAG\",
\"target_commitish\":\"$GIT_COMMIT_SHA\",
\"name\":\"$TAG\",
\"body\":\"GitHub build #$GITHUB_RUN_NUMBER | by ${WORKER_NAME}.\",
\"draft\":false,
\"prerelease\":$PRERELESE
}"

CODE=0

DST_PATH="${ASSEMBLY_PATH}/github"
if [ ! -d "$DST_PATH" ]; then
 mkdir -p $DST_PATH || exit 21
fi
rm -f $DST_PATH/release.json
CODE=$(curl -w %{http_code} -o "$DST_PATH/release.json" -X POST \
 -s https://api.github.com/repos/$GITHUB_OWNER/$GITHUB_REPO/releases \
 -H "Authorization: token $GITHUB_PAT" \
 -d "$REQUEST_BODY")
if test $CODE -ne 201; then
 echo "GitHub release $TAG error!"
 echo "Request error with response code $CODE!"
 exit 21
fi
RELEASE_ID="$(cat $DST_PATH/release.json | jq -r .id)"

echo "The release $RELEASE_ID is created."
echo "GitHub release $TAG success"

exit 0
