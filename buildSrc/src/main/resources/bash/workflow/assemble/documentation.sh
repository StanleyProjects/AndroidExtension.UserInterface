#!/bin/bash

echo "assemble documentation start..."

CODE=0

POSTFIX="assemble.documentation"

docker build --no-cache \
 -t "${DOCKER_IMAGE_NAME}.${POSTFIX}" \
 --build-arg IMAGE_SOURCE="${DOCKER_IMAGE_NAME}.verified" \
 -f $RESOURCES_PATH/docker/assemble/Dockerfile.documentation .; CODE=$?

if test $CODE -ne 0; then
 echo "Build error $CODE!"
 exit 11
fi

DST_PATH="${ASSEMBLY_PATH}/documentation"
rm -rf $DST_PATH
mkdir -p $DST_PATH || return 21
ARRAY=(
 "docker run --name ${DOCKER_CONTAINER_NAME}.${POSTFIX} ${DOCKER_IMAGE_NAME}.${POSTFIX}"
 "docker cp ${DOCKER_CONTAINER_NAME}.${POSTFIX}:/repository/lib/build/documentation/. $DST_PATH"
 "docker stop ${DOCKER_CONTAINER_NAME}.${POSTFIX}"
 "docker container rm -f ${DOCKER_CONTAINER_NAME}.${POSTFIX}"
)
SIZE=${#ARRAY[*]}
for ((i = 0; i < SIZE; i++)); do
 TASK=${ARRAY[$i]}
 $TASK || CODE=$?
 if test $CODE -ne 0; then
  echo "Task \"$TASK\" error $CODE!"
  return $((100 + i))
 fi
done

if [ ! -f "$DST_PATH/index.html" ]; then
 echo "$DST_PATH/index.html does not exist."
 return 31
fi

echo "assemble documentation success"

exit 0
