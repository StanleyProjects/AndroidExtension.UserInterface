#!/bin/bash

echo "diagnostics start..."

CODE=0

POSTFIX="diagnosed"

docker build --no-cache \
 -t "${DOCKER_PREFIX}.image.${POSTFIX}" \
 --build-arg IMAGE_SOURCE="${DOCKER_PREFIX}.image.prepared" \
 -f $RESOURCES_PATH/docker/Dockerfile.diagnostics .; CODE=$?

if test $CODE -ne 0; then
 echo "Build error $CODE!"
 exit 11
fi

docker container rm -f ${DOCKER_PREFIX}.container.${POSTFIX}

DST_PATH="${ASSEMBLY_PATH}/failed"
rm -rf $DST_PATH
mkdir -p $DST_PATH || exit 21
ARRAY=(
 "run --name ${DOCKER_PREFIX}.container.${POSTFIX} ${DOCKER_PREFIX}.image.${POSTFIX}"
 "cp ${DOCKER_PREFIX}.container.${POSTFIX}:/failed/. $DST_PATH"
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

echo "diagnostics success"

exit 0
