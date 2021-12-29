#!/bin/bash

echo "assemble snapshot start..."

BUILD_TYPE="Snapshot"
BUILD_VARIANT="$BUILD_TYPE"

CODE=0

gradle lib:assemble${BUILD_VARIANT} && \
 gradle lib:assemble${BUILD_VARIANT}Source && \
 gradle lib:assemble${BUILD_VARIANT}Pom; CODE=$?

if test $CODE -ne 0; then
  echo "gradle error"
  exit 1
fi

echo "assemble snapshot success"

exit 0
