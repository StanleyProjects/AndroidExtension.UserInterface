#!/bin/bash

echo "test start..."

CODE=0

BUILD_TYPE="Snapshot"
BUILD_VARIANT="$BUILD_TYPE"

gradle lib:test${BUILD_VARIANT}UnitTest && \
 gradle lib:test${BUILD_VARIANT}CoverageReport && \
 gradle lib:test${BUILD_VARIANT}CoverageVerification; CODE=$?

if test $CODE -ne 0; then
 echo "gradle test error"
 exit 11
fi

echo "test success"

exit 0
