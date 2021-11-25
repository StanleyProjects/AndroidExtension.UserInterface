#!/bin/bash

echo "verify start..."

CODE=0

gradle clean; CODE=$?

if test $CODE -ne 0; then
  echo "gradle clean error"
  exit 11
fi

gradle verifyReadme && \
  gradle verifyLicense && \
  gradle verifyCodeStyle && \
  gradle lib:verifyDocumentation; CODE=$?

if test $CODE -ne 0; then
  echo "gradle verify error"
  exit 12
fi

BUILD_TYPE="Snapshot"
BUILD_VARIANT="$BUILD_TYPE"

gradle lib:test${BUILD_VARIANT}UnitTest && \
  gradle lib:test${BUILD_VARIANT}CoverageReport && \
  gradle lib:test${BUILD_VARIANT}CoverageVerification; CODE=$?

if test $CODE -ne 0; then
  echo "gradle test error"
  exit 13
fi

echo "verify success"

exit 0
