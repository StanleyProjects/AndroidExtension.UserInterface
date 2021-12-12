#!/bin/bash

echo "verify start..."

CODE=0

gradle verifyService && \
 gradle verifyReadme && \
 gradle verifyLicense && \
 gradle verifyCodeStyle && \
 gradle lib:verifyDocumentation; CODE=$?

if test $CODE -ne 0; then
 echo "gradle verify error"
 exit 11
fi

echo "verify success"

exit 0
