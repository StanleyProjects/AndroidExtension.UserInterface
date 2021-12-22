#!/bin/bash

echo "diagnostics start..."

CODE=0

DST_PATH="/failed"
rm -rf $DST_PATH
mkdir -p $DST_PATH || exit 21

#gradle verifyService; CODE=$? # todo
#gradle verifyReadme; CODE=$? # todo
#gradle verifyLicense; CODE=$? # todo
#gradle lib:verifyDocumentation; CODE=$? # todo

gradle verifyCodeStyle; CODE=$?
if test $CODE -ne 0; then
 echo "Diagnostics have determined the cause of the failure - this is code style."
 rm -rf $DST_PATH/report/CODE_STYLE
 mkdir -p $DST_PATH/report/CODE_STYLE || exit 31
 cp build/reports/analysis/style/html/index.html $DST_PATH/report/CODE_STYLE/index.html || exit 32
 echo '{"type":"CODE_STYLE"}' > $DST_PATH/summary.json || exit 33
 exit 0
fi

echo "Diagnostics should have determined the cause of the failure!"

exit 1
