#!/bin/bash

echo "diagnostics start..."

CODE=0

DST_PATH="/diagnostics"
rm -rf $DST_PATH
mkdir -p $DST_PATH || exit 21

#gradle verifyService; CODE=$? # todo
#gradle verifyLicense; CODE=$? # todo

gradle verifyReadme; CODE=$?
if test $CODE -ne 0; then
 echo "Diagnostics have determined the cause of the failure - this is readme."
 TYPE="README"
 REPORT_PATH=$DST_PATH/report/$TYPE
 rm -rf $REPORT_PATH
 mkdir -p $REPORT_PATH || exit 111
 cp build/reports/analysis/readme/index.html $REPORT_PATH/index.html || exit 112
 echo "{\"type\":\"$TYPE\"}" > $DST_PATH/summary.json || exit 113
 exit 0
fi

gradle verifyCodeStyle; CODE=$?
if test $CODE -ne 0; then
 echo "Diagnostics have determined the cause of the failure - this is code style."
 TYPE="CODE_STYLE"
 REPORT_PATH=$DST_PATH/report/$TYPE
 rm -rf $REPORT_PATH
 mkdir -p $REPORT_PATH || exit 31
 cp build/reports/analysis/style/html/index.html $REPORT_PATH/index.html || exit 32
 echo "{\"type\":\"$TYPE\"}" > $DST_PATH/summary.json || exit 33
 exit 0
fi

gradle lib:verifyDocumentation; CODE=$?
if test $CODE -ne 0; then
 echo "Diagnostics have determined the cause of the failure - this is documentation."
 TYPE="DOCUMENTATION"
 REPORT_PATH=$DST_PATH/report/$TYPE
 rm -rf $REPORT_PATH
 mkdir -p $REPORT_PATH && \
 cp lib/build/reports/documentation/html/index.html $REPORT_PATH/index.html && \
 echo "{\"type\":\"$TYPE\"}" > $DST_PATH/summary.json || exit 41
 exit 0
fi

BUILD_TYPE="Snapshot"
BUILD_VARIANT="$BUILD_TYPE"

gradle lib:test${BUILD_VARIANT}UnitTest; CODE=$?
if test $CODE -ne 0; then
 SRC_PATH=lib/build/reports/tests/test${BUILD_VARIANT}UnitTest
 if test -f "$SRC_PATH/index.html"; then
  echo "Diagnostics have determined the cause of the failure - this is unit test."
 else
  echo "Diagnostics cannot find test report!"
  exit 101
 fi
 TYPE="UNIT_TEST"
 REPORT_PATH=$DST_PATH/report/$TYPE
 rm -rf $REPORT_PATH
 mkdir -p $REPORT_PATH && \
 cp -r $SRC_PATH/* $REPORT_PATH && \
 echo "{\"type\":\"$TYPE\"}" > $DST_PATH/summary.json || exit 41
 exit 0
fi

gradle lib:test${BUILD_VARIANT}CoverageReport; CODE=$?
if test $CODE -ne 0; then
 echo "Test coverage report generation ended in failure!"
 exit 201 # todo
fi

gradle lib:test${BUILD_VARIANT}CoverageVerification; CODE=$?
if test $CODE -ne 0; then
 SRC_PATH=lib/build/reports/jacoco/test${BUILD_VARIANT}CoverageReport/html
 if test -f "$SRC_PATH/index.html"; then
  echo "Test coverage verification failed!"
 else
  echo "Diagnostics cannot find test coverage report!"
  exit 202
 fi
 TYPE="COVERAGE_VERIFICATION"
 REPORT_PATH=$DST_PATH/report/$TYPE
 rm -rf $REPORT_PATH
 mkdir -p $REPORT_PATH && \
 cp -r $SRC_PATH/* $REPORT_PATH && \
 echo "{\"type\":\"$TYPE\"}" > $DST_PATH/summary.json || exit 41
 exit 0
fi

echo "Diagnostics should have determined the cause of the failure!"

exit 1
