#!/bin/bash

echo "verify start..."

CODE=0

docker image prune -f
export DOCKER_IMAGE_NAME=docker_${GITHUB_RUN_NUMBER}_${GITHUB_RUN_ID}_image
docker build --no-cache \
  -t $DOCKER_IMAGE_NAME \
  -f $RESOURCES_PATH/docker/Dockerfile.verify .; CODE=$?

if test $CODE -ne 0; then
  echo "Build error $CODE!"
  return 11
fi

echo "verify success"

exit 0
