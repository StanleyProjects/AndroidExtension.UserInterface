#!/bin/bash

echo "diagnostics start..."

CODE=0

docker build --no-cache \
 -t "${DOCKER_PREFIX}.image.diagnosed" \
 --build-arg IMAGE_SOURCE="${DOCKER_PREFIX}.image.prepared" \
 -f $RESOURCES_PATH/docker/Dockerfile.diagnostics .; CODE=$?

if test $CODE -ne 0; then
 echo "Build error $CODE!"
 exit 11
fi


echo "diagnostics success"

exit 0
