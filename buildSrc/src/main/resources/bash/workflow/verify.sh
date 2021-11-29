#!/bin/bash

echo "verify start..."

CODE=0

docker build --no-cache \
 -t "${DOCKER_IMAGE_NAME}.verified" \
 --build-arg IMAGE_SOURCE="${DOCKER_IMAGE_NAME}.prepared" \
 -f $RESOURCES_PATH/docker/Dockerfile.verify .; CODE=$?

if test $CODE -ne 0; then
 echo "Build error $CODE!"
 exit 11
fi

echo "verify success"

exit 0
