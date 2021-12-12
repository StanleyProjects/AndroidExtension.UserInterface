#!/bin/bash

echo "assemble common start..."

CODE=0

POSTFIX="assemble.common"

docker build --no-cache \
 -t "${DOCKER_IMAGE_NAME}.${POSTFIX}" \
 --build-arg IMAGE_SOURCE="${DOCKER_IMAGE_NAME}.verified" \
 -f $RESOURCES_PATH/docker/assemble/Dockerfile.common .; CODE=$?

if test $CODE -ne 0; then
 echo "Build error $CODE!"
 exit 11
fi

DST_PATH="${ASSEMBLY_PATH}"
rm $DST_PATH/common.json
mkdir -p $DST_PATH || exit 21
ARRAY=(
 "docker run --name ${DOCKER_CONTAINER_NAME}.${POSTFIX} ${DOCKER_IMAGE_NAME}.${POSTFIX}"
 "docker cp ${DOCKER_CONTAINER_NAME}.${POSTFIX}:/repository/build/common.json $DST_PATH/common.json"
 "docker stop ${DOCKER_CONTAINER_NAME}.${POSTFIX}"
 "docker container rm -f ${DOCKER_CONTAINER_NAME}.${POSTFIX}"
)
SIZE=${#ARRAY[*]}
for ((i = 0; i < SIZE; i++)); do
 TASK=${ARRAY[$i]}
 $TASK || CODE=$?
 if test $CODE -ne 0; then
  echo "Task \"$TASK\" error $CODE!"
  exit $((100 + i))
 fi
done

if [ ! -f "$DST_PATH/common.json" ]; then
 echo "$DST_PATH/common.json does not exist."
 exit 31
fi

ARTIFACT_ID="$(cat ${ASSEMBLY_PATH}/common.json | jq -r .artifactId | base64 -d)"
if test -z "$ARTIFACT_ID"; then
 echo "Artifact id is empty!"
 exit 41
fi
if test "$ARTIFACT_ID" != "$GITHUB_REPO"; then
 echo "Actual artifact id is $ARTIFACT_ID, but expected id is $GITHUB_REPO!"
 exit 42
fi

echo "assemble common success"

exit 0
