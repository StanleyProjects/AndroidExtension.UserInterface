#!/bin/bash

echo "diagnostics start..."

CODE=0

DST_PATH="/diagnostics"
rm -rf $DST_PATH
mkdir -p $DST_PATH || exit 21

#gradle verifyService; CODE=$? # todo
#gradle verifyReadme; CODE=$? # todo
#gradle verifyLicense; CODE=$? # todo

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

echo "Diagnostics should have determined the cause of the failure!"

exit 1
