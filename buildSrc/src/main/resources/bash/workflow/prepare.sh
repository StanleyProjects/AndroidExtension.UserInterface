#!/bin/bash

echo "prepare start..."

CODE=0

docker image prune -f

docker build --no-cache \
 -t "${DOCKER_PREFIX}.image.prepared" \
 -f $RESOURCES_PATH/docker/Dockerfile.prepare .; CODE=$?

if test $CODE -ne 0; then
 echo "Build error $CODE!"
 exit 11
fi

echo "prepare success"

exit 0
