#!/bin/bash

echo "verify start..."

BUILD_TYPE="Snapshot"
BUILD_VARIANT="$BUILD_TYPE"

gradle verifyReadme && \
gradle verifyLicense && \
gradle verifyCodeStyle && \
gradle lib:verifyDocumentation && \
gradle lib:test${BUILD_VARIANT}UnitTest && \
gradle lib:test${BUILD_VARIANT}CoverageReport && \
gradle lib:test${BUILD_VARIANT}CoverageVerification; CODE=$?

if test $CODE -ne 0; then
  echo "gradle error"
  exit 1
fi

echo "verify success"

exit 0
