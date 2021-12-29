#!/bin/bash

echo "maven deploy snapshot..."

/bin/bash $RESOURCES_PATH/bash/util/check_variables.sh \
 ASSEMBLY_PATH MAVEN_GROUP_ID MAVEN_ARTIFACT_ID MAVEN_USERNAME MAVEN_PASSWORD || exit 1 # todo

VERSION="$(cat ${ASSEMBLY_PATH}/common.json | jq -r .version)"

for it in VERSION; do
 if test -z "${!it}"; then echo "$it is empty!"; exit 11; fi; done

BASE_URL="https://s01.oss.sonatype.org/content/repositories/snapshots/${MAVEN_GROUP_ID//.//}"

ARRAY=(".pom" ".aar" "-sources.jar")
SIZE=${#ARRAY[*]}
for ((i = 0; i < SIZE; i++)); do
 ARTIFACT_NAME="${MAVEN_ARTIFACT_ID}-${VERSION}-SNAPSHOT${ARRAY[$i]}"
 FILE="${ASSEMBLY_PATH}/snapshot/artifact/$ARTIFACT_NAME"
 if [ ! -f "$FILE" ]; then
  echo "The file $FILE does not exist!"; exit $((100 + i))
 fi
 URL="${BASE_URL}/${MAVEN_ARTIFACT_ID}/${VERSION}-SNAPSHOT/$ARTIFACT_NAME"
 echo "upload ${FILE}..."
 CODE=$(curl -X POST "$URL" \
  -o /dev/null \
  -w %{http_code} \
  -u $MAVEN_USERNAME:$MAVEN_PASSWORD \
  -H 'Content-Type: text/plain' \
  --data-binary "@$FILE")
 if test $CODE -ne 201; then
  echo "Maven deploy ${VERSION}-SNAPSHOT error!"
  echo "Request error with response code $CODE!"
  exit 21
 fi
done

echo "maven deploy snapshot success"

exit 0
