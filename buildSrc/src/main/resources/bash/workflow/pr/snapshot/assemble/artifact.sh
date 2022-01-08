#!/bin/bash

echo "snapshot assemble artifact..."

VERSION="$(cat ${ASSEMBLY_PATH}/common.json | jq -r .version)"
for it in VERSION; do
 if test -z "${!it}"; then echo "$it is empty!"; exit 11; fi; done

CODE=0

POSTFIX="assemble.snapshot.artifact"

docker build --no-cache \
 -t "${DOCKER_PREFIX}.image.${POSTFIX}" \
 --build-arg IMAGE_SOURCE="${DOCKER_PREFIX}.image.verified" \
 -f $RESOURCES_PATH/docker/assemble/Dockerfile.snapshot .; CODE=$?

if test $CODE -ne 0; then
 echo "Build error $CODE!"
 exit 11
fi

DST_PATH="${ASSEMBLY_PATH}/snapshot/artifact"
rm -rf $DST_PATH
mkdir -p $DST_PATH || exit 21
ARRAY=(
 "run --name ${DOCKER_PREFIX}.container.${POSTFIX} ${DOCKER_PREFIX}.image.${POSTFIX}"
 "cp ${DOCKER_PREFIX}.container.${POSTFIX}:/lib/build/libs/. $DST_PATH"
 "stop ${DOCKER_PREFIX}.container.${POSTFIX}"
 "container rm -f ${DOCKER_PREFIX}.container.${POSTFIX}"
)
SIZE=${#ARRAY[*]}
for ((i = 0; i < SIZE; i++)); do
 it=${ARRAY[$i]}; docker $it || CODE=$?
 if test $CODE -ne 0; then
  echo "Task \"$it\" error $CODE!"; exit $((100 + i))
 fi
done

ARRAY=(".pom" ".aar" "-sources.jar")
SIZE=${#ARRAY[*]}
for ((i = 0; i < SIZE; i++)); do
 FILE="$DST_PATH/${MAVEN_ARTIFACT_ID}-$VERSION-SNAPSHOT${ARRAY[$i]}"
 if [ ! -f "$FILE" ]; then
  echo "The file $FILE does not exist!"; exit $((200 + i))
 fi
done

echo "snapshot assemble artifact success"

exit 0
