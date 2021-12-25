#!/bin/bash

echo "test start..."

CODE=0

BUILD_TYPE="Snapshot"
BUILD_VARIANT="$BUILD_TYPE"

gradle lib:test${BUILD_VARIANT}UnitTest; CODE=$?
if test $CODE -ne 0; then
 echo "Unit testing ended in failure!"
 exit 101
fi

gradle lib:test${BUILD_VARIANT}CoverageReport; CODE=$?
if test $CODE -ne 0; then
 echo "Test coverage report generation ended in failure!"
 exit 201
fi

gradle lib:test${BUILD_VARIANT}CoverageVerification; CODE=$?
if test $CODE -ne 0; then
 echo "Test coverage verification failed!"
 exit 202
fi

echo "test success"

exit 0
