#!/bin/bash

echo "GitHub release snapshot..."

/bin/bash $RESOURCES_PATH/bash/util/check_variables.sh \
 ASSEMBLY_PATH MAVEN_GROUP_ID MAVEN_ARTIFACT_ID \
 GITHUB_OWNER GITHUB_REPO GITHUB_PAT GIT_COMMIT_SHA GITHUB_RUN_NUMBER GITHUB_RUN_ID || exit 1 # todo

VERSION="$(cat ${ASSEMBLY_PATH}/common.json | jq -r .version)"
WORKER_NAME="$(cat ${ASSEMBLY_PATH}/vcs/worker.json | jq -r .name)"
WORKER_URL="$(cat ${ASSEMBLY_PATH}/vcs/worker.json | jq -r .html_url)"

for it in VERSION WORKER_NAME WORKER_URL; do
 if test -z "${!it}"; then echo "$it is empty!"; exit 11; fi; done

TAG="$VERSION-SNAPSHOT"

REPO_URL=https://github.com/$GITHUB_OWNER/$GITHUB_REPO
RUN_URL=$REPO_URL/actions/runs/$GITHUB_RUN_ID
MAVEN_BASE_URL="https://s01.oss.sonatype.org/content/repositories/snapshots/${MAVEN_GROUP_ID//.//}"
BODY="GitHub build [#$GITHUB_RUN_NUMBER]($RUN_URL) | by [$WORKER_NAME]($WORKER_URL):
 - maven [snapshot](${MAVEN_BASE_URL}/${MAVEN_ARTIFACT_ID}/${VERSION}-SNAPSHOT)"
REQUEST_BODY="{\
\"tag_name\":\"$TAG\",
\"target_commitish\":\"$GIT_COMMIT_SHA\",
\"name\":\"$TAG\",
\"body\":\"${BODY//$'\n'/\\n}\",
\"draft\":false,
\"prerelease\":true
}"

CODE=0

CODE=$(curl -w %{http_code} -o file -X POST \
 https://api.github.com/repos/$GITHUB_OWNER/$GITHUB_REPO/releases \
 -H "Authorization: token $GITHUB_PAT" -d "$REQUEST_BODY")
if test $CODE -ne 201; then
 echo "GitHub release $TAG error!"
 echo "Request error with response code $CODE!"
 exit 21
fi
BODY=$(<file); rm file
RELEASE_ID="$(echo $BODY | jq -r .id)"

echo "The release $RELEASE_ID is created."

ARRAY=(".pom" ".aar" "-sources.jar")
SIZE=${#ARRAY[*]}
for ((i = 0; i < SIZE; i++)); do
 ARTIFACT_NAME="${MAVEN_ARTIFACT_ID}-${VERSION}-SNAPSHOT${ARRAY[$i]}"
 FILE="${ASSEMBLY_PATH}/snapshot/artifact/$ARTIFACT_NAME"
 if [ ! -f "$FILE" ]; then
  echo "The file $FILE does not exist!"; exit $((100 + i))
 fi
 URL="https://uploads.github.com/repos/$GITHUB_OWNER/$GITHUB_REPO/releases/$RELEASE_ID/assets"
 echo "upload ${FILE}..."
 CODE=$(curl -w %{http_code} -o /dev/null -X POST "$URL" \
  -G -d "name=$ARTIFACT_NAME" -d "label=$ARTIFACT_NAME" \
  -H "Authorization: token $GITHUB_PAT" --data-binary "@$FILE")
 if test $CODE -ne 201; then
  echo "GitHub release $TAG error!"
  echo "Request error with response code $CODE!"
  exit $((110 + i))
 fi
done

echo "GitHub release $TAG success"

exit 0
